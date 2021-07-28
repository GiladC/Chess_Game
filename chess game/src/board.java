import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class board {
    static Piece currPiece = null; //the piece that was last clicked on
	private static List<int[]> legalDests = new ArrayList<>(); //all the legal destinations for the current piece, in cords (x,y)
	static boolean Wlose = false, Blose = false; //either white or black lost
	static boolean Wking = false, Bking = false; //either white or black king has moved, relevant to castle
	//either white or black rooks moved, relevant to castle. the [0] entry for the rook with y=0, and [1] for y=7.
	static boolean[] Wrook = {false, false}, Brook = {false, false}; 
	static boolean castle = false; //this boolean just to simplify stuff
	public static void main(String[] args) throws IOException{
		legalDests = new ArrayList<>();
		Set<Piece> pieces = Piece.pieces;
		BufferedImage piecesImage = ImageIO.read(new File("chess_pieces.png"));
		Image[] allPieces = new Image[12]; //6 pieces for black and 6 pieces for white.
		int index = 0;
		for(int i = 0; i < 400; i +=200) {
			for(int j = 0; j < 1200; j+=200) {
				allPieces[index] = piecesImage.getSubimage(j, i, 200, 200) //just a way to get the pieces from the image
						.getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH); 
				index++;
			}
		}
		for (int i = 0; i < 8; i++) {
			pieces.add(new Piece(i, 1, false, PieceType.PAWN)); //adds the black pawns
			pieces.add(new Piece(i, 6, true, PieceType.PAWN));  // adds the white pawns
			switch(i) { //adds all the other pieces based on their placement on the board
			case 7:
			case 0:
				pieces.add(new Piece(i, 0, false, PieceType.ROOK)); 
				pieces.add(new Piece(i, 7, true, PieceType.ROOK)); 
				break;
			case 1:
			case 6:
				pieces.add(new Piece(i, 0, false, PieceType.KNIGHT));
				pieces.add(new Piece(i, 7, true, PieceType.KNIGHT));
				break;
			case 2:
			case 5:
				pieces.add(new Piece(i, 0, false, PieceType.BISHOP));
				pieces.add(new Piece(i, 7, true, PieceType.BISHOP));
				break;
			case 3:
				pieces.add(new Piece(i, 0, false, PieceType.QUEEN));
				pieces.add(new Piece(i, 7, true, PieceType.QUEEN));
				break;
			case 4:
				pieces.add(new Piece(i, 0, false, PieceType.KING));
				pieces.add(new Piece(i, 7, true, PieceType.KING));
				break;
			}
		}
		final JFrame frame = new JFrame();
		frame.setBounds(10, 10, 525, 549);
	    JPanel panel = new JPanel() {	    	
		private static final long serialVersionUID = -4396970882832779043L;
        /*
         *checks whether the block in position (x, y) with the color IsWhite, threatened by an opposite piece
         */
        boolean threatened(int x, int y, boolean isWhite) {
        	for (Piece p : pieces) {
        		//pawns are a special case, since they only attack diagonally
        		if (p.type == PieceType.PAWN && p.white != isWhite) {
        			if (p.white && (x == p.i -1 || x == p.i +1) && y == p.j - 1) { return true; } 
        			if (!p.white && (x == p.i -1 || x == p.i +1) && y == p.j +1 ) { return true; }
        		}
        		else {
        	    	if (p.white != isWhite) {
        		    	for (int[] arr : p.getDests()) {
        			    	if (arr[0] == x && arr[1] == y) {
        				    	return true;
        	     	        }
        		    	}
        	    	}
        		}
        	}
        	return false;
        }
        //get piece for mouse coords pressed
        public Piece getPiece(int x, int y) {		
			for (Piece piece : pieces) {
				if (x == piece.i && y == piece.j) {
					return piece;
				}
			}
			return null;
		}
		public void paint(Graphics g) {
			super.paint(g);
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if ((x%2) == (y%2)) {  //statement check whether this block is white or black
						g.setColor(Color.white);
					}
					else {
						g.setColor(new Color(120, 155, 85));
					}
					g.fillRect(x*64, y*64, 64, 64); //fill the squares of the board
				}
			}
			if (currPiece != null) {
				g.setColor(new Color(51, 0, 103));
				g.fillRect(64*currPiece.i, 64*currPiece.j, 64, 64); //the color that signs the piece that has been chosen
				g.setColor(new Color(51,255,255)); //the color of the places the piece can go
				List<int[]> lst = currPiece.getDests();
				int[] arr;
				for (int i = 0; i < lst.size(); i++) { //going through all the possible destinations
					arr = lst.get(i);
					if(currPiece.type == PieceType.KING) {
						if (!threatened(arr[0], arr[1], currPiece.white)) {
						g.fillRect(1+64*arr[0], 1+64*arr[1], 62, 62);
						legalDests.add(arr);
						}
					}
					else {
						g.fillRect(1+64*arr[0], 1+64*arr[1], 62, 62);
						legalDests.add(arr);
			    	}
				}
				//next checking for castle option
				if(currPiece.type == PieceType.KING) {
					if (currPiece.white && !Wking) {
						if (!Wrook[0]) {
							if (getPiece(3, 7) == null && getPiece(2, 7) == null &&
									!threatened(2,7, true) && getPiece(1,7) == null &&
									!threatened(3,7,true) && !threatened(4,7,true)) {
								castle = true; g.fillRect(1 + 2*64, 1 + 7*64, 62, 62);
								int[] arr2 = {2, 7}; legalDests.add(arr2);
							}
						}
						if (!Wrook[1]) {
							if (getPiece(5, 7) == null && getPiece(6, 7) == null
									&& !threatened(4,7, true) && 
									!threatened(5,7,true) && !threatened(6,7,true)) {
								castle = true; g.fillRect(1 + 6*64, 1 + 7*64, 62, 62);
								int[] arr2 = {6, 7}; legalDests.add(arr2);
							}
						}
					}
					if (!currPiece.white && !Bking) {
						if (!Brook[0]) {
							if (getPiece(3, 0) == null && getPiece(2,0) == null 
									&& !threatened(2,0, false) && getPiece(1,0) == null &&
									!threatened(3,0,false) && !threatened(4,0,false)) {
								castle = true; g.fillRect(1 + 2*64, 1 + 0*64, 62, 62);
								int[] arr2 = {2, 0}; legalDests.add(arr2);
							}
						}
						if (!Brook[1]) {
							if (getPiece(5, 0) == null && getPiece(6,0) == null  
									&& !threatened(4,0, false) && 
									!threatened(5,0,false) && !threatened(6,0,false)) {
								castle = true; g.fillRect(1 + 6*64, 1 + 0*64, 62, 62);
								int[] arr2 = {6, 0}; legalDests.add(arr2);
							}
						}
					}
				}
			}
			for (Piece piece : pieces) {
				int i = 0;
				switch(piece.type) { //deciding over a num based on what is the piece to be painted
				case KING:
					i = 0;
					break;
				case QUEEN:
					i = 1;
					break;
				case BISHOP:
					i = 2;
					break;
				case KNIGHT:
					i = 3;
					break;
				case ROOK:
					i = 4;
					break;
				case PAWN:
					i = 5;
					break;
				}
				if (!piece.white) { i += 6; }
				g.drawImage(allPieces[i], piece.i*64, piece.j*64, this); //adds the images of all the pieces
			}
			Wlose = true; Blose = true;
			for (Piece p : pieces) { //checks whether white or black lost
				if (p.type == PieceType.KING) {
					if (p.white) { Wlose = false;  }
					else { Blose = false; }
				}
			}
			Font f = g.getFont();
			Font newFont = f.deriveFont(f.getSize() * 5F); //change the size of the win message
			g.setFont(newFont);
			g.setColor(new Color(0, 0, 153));
			if (Wlose) { 
				g.drawString("Black win", 126, 276);
			}
			if (Blose) {
				g.drawString("White win", 126, 276);
			}
		}
	};
	frame.addMouseListener(new MouseListener() {
		//get piece for mouse coords pressed
		public Piece getPiece(int x, int y) {		
			for (Piece piece : pieces) {
				if (x == piece.i && y == piece.j) {
					return piece;
				}
			}
			return null;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		boolean whiteTurn = true; //white moves first at chess
		@Override
		public void mousePressed(MouseEvent e) {
			if (Wlose || Blose) { return; } //the game doesn't continue after someone lost
			int x =  (int) Math.floor((e.getX()-7)/64);
			int y = (int) Math.floor((e.getY()-30)/64);
			if (currPiece == null) { 
				Piece p = getPiece(x, y);
				if (p != null) {
					if (p.white == whiteTurn) {
						currPiece = p; //updating the current piece
					}
				}
			}
			/*this statement checks for castle option. castle == true iff castling is an actual option 
			 *(the square is blue) and the destination coords are the destinations that correspond with castle.
			 */
			else if (castle && (x == 2 || x == 6) && (y == 7 || y == 0)) {
				for (int[] arr : legalDests) {
					if (x == arr[0] && y == arr[1]) {
						try {
							currPiece.move(x, y);
							if (x == 2) { getPiece(0, y).move(3, y); }
							if (x == 6) { getPiece(7, y).move(5, y); }
							whiteTurn = !whiteTurn;
							if (currPiece.white) { Wking = true; }
							else { Bking = true; }	
						}
						catch (Exception e1) {
							e1.printStackTrace();
							System.out.println("an error has occurred while trying to castle");
						}
					}
				}
				currPiece = null; legalDests.clear(); castle = false;
			}  
			else { //moving the piece to the block
				for (int[] arr : legalDests) {
					if (x == arr[0] && y == arr[1]) {
						try {
							if (currPiece.type == PieceType.ROOK) { //when a rook moves from its starting positin
								if (currPiece.i == 0 && currPiece.j == 0) { Brook[0] = true; }
								if (currPiece.i == 7 && currPiece.j == 0) { Brook[1] = true; }
								if (currPiece.i == 0 && currPiece.j == 7) { Wrook[0] = true; }
								if (currPiece.i == 7 && currPiece.j == 7) { Wrook[1] = true; }
							}
							//queen promoting
							currPiece.move(x, y);
							if (currPiece.type == PieceType.PAWN && (currPiece.j == 0 || currPiece.j == 7)) {
								currPiece.type = PieceType.QUEEN;
							}
							whiteTurn = !whiteTurn;
							if (currPiece.type == PieceType.KING) {
								if (currPiece.white) { Wking = true; }
								else { Bking = true; }
							}
						}
							catch (Exception e1) {
							System.out.println("an error has occurred");
						}
					}
				}
				currPiece = null; legalDests.clear(); castle = false;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			frame.repaint(); //updating changes on board
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	});
	frame.add(panel);
	frame.setDefaultCloseOperation(3);
	frame.setVisible(true);
	}
}
