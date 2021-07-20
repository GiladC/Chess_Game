import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class board {
    static Piece currPiece = null; //the piece that was last clicked on
	private static List<int[]> legalDests; //all the legal destinations for the current piece, in cords (x,y)
	static boolean Wlose = false, Blose = false; //either white or black lost
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
		/*ret = 0 iff not legal destination
		  ret = 1 iff legal destination and NO enemy piece killed
		  ret = 2 iff legal destination and enemy piece killed
		  */
        int checkDest(int x, int y) { 
	   	if( x > 7 || y > 7 || x < 0 || y < 0) { //can't go out of board
	   		return 0;
	   	}
	   	for (Piece piece : pieces) {
	   		if (piece.i == x && piece.j == y) {
	   			if(piece.white == currPiece.white) { return 0; } //can't kill your own piece
	   			return 2;  //kills a piece
	   		}
	   	}
	   	return 1; //a possible move without any piece being killed
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
				int xp = currPiece.i; int yp = currPiece.j;
				g.setColor(new Color(51, 0, 103));
				g.fillRect(64*xp, 64*yp, 64, 64); //the color that signs the piece that has been chosen
				g.setColor(new Color(51,255,255)); //the color of the places the piece can go
				switch(currPiece.type) {
				case KING: //all the moves possible for a king 
					if(checkDest(xp, yp +1) != 0) { g.fillRect(1+64*xp,1+ 64*(yp+1), 62, 62); 
					int[] arr = {xp, yp+1};
				   	legalDests.add(arr);}
					if(checkDest(xp, yp -1) != 0) {g.fillRect(1+64*xp, 1+64*(yp-1), 62, 62); 
					int[] arr = {xp, yp-1};
				   	legalDests.add(arr);}
					if(checkDest(xp -1, yp) != 0) {g.fillRect(1+64*(xp-1),1+ 64*yp, 62, 62); 
					int[] arr = {xp-1, yp};
				   	legalDests.add(arr);}
					if(checkDest(xp +1, yp) != 0) {g.fillRect(1+64*(xp+1), 1+64*yp, 62, 62); 
					int[] arr = {xp+1, yp};
				   	legalDests.add(arr);}
					if(checkDest(xp -1, yp-1) != 0) {g.fillRect(1+64*(xp-1),1+ 64*(yp-1), 62, 62); 
					int[] arr = {xp-1, yp-1};
				   	legalDests.add(arr);}
					if(checkDest(xp -1, yp+1) != 0) {g.fillRect(1+64*(xp-1), 1+64*(yp+1), 62, 62); 
					int[] arr = {xp-1, yp+1};
				   	legalDests.add(arr);}
					if(checkDest(xp +1, yp+1) != 0) {g.fillRect(1+64*(xp+1), 1+64*(yp+1), 62, 62); 
					int[] arr = {xp+1, yp+1};
				   	legalDests.add(arr);}
					if(checkDest(xp +1, yp-1) != 0) {g.fillRect(1+64*(xp+1), 1+64*(yp-1), 62, 62); 
					int[] arr = {xp+1, yp-1};
				   	legalDests.add(arr);}
					break;
				case BISHOP: //all the moves possible for a bishop
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp +i, yp + i);
						if (k == 0) {break; }
						g.fillRect(1+64*(xp+i), 1+64*(yp+i), 62, 62);
						int[] arr = {xp+i, yp+i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp +i, yp - i);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp +i),1+ 64*(yp-i), 62, 62);
						int[] arr = {xp+i, yp-i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp - i);
						if (k == 0) {break; }
						g.fillRect(1+64*(xp-i), 1+64*(yp-i), 62, 62);
						int[] arr = {xp-i, yp-i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp + i);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp -i),1+ 64*(yp+i), 62, 62);
						int[] arr = {xp-i, yp+i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					break;
				case KNIGHT: //all the moves possible for a knight
					if(checkDest(xp+2, yp +1) != 0) { g.fillRect(1+64*(xp+2),1+ 64*(yp+1), 62, 62); 
					int[] arr = {xp+2, yp+1};
				   	legalDests.add(arr);}
					if(checkDest(xp+2, yp -1) != 0) { g.fillRect(1+64*(xp+2),1+ 64*(yp-1), 62, 62); 
					int[] arr = {xp+2, yp-1};
				   	legalDests.add(arr);}
					if(checkDest(xp-2, yp +1) != 0) { g.fillRect(1+64*(xp-2),1+ 64*(yp+1), 62, 62); 
					int[] arr = {xp-2, yp+1};
				   	legalDests.add(arr);}
					if(checkDest(xp-2, yp -1) != 0) { g.fillRect(1+64*(xp-2),1+ 64*(yp-1), 62, 62); 
					int[] arr = {xp-2, yp-1};
				   	legalDests.add(arr);}
					if(checkDest(xp+1, yp +2) != 0) { g.fillRect(1+64*(xp+1),1+ 64*(yp+2), 62, 62); 
					int[] arr = {xp+1, yp+2};
				   	legalDests.add(arr);}
					if(checkDest(xp-1, yp +2) != 0) { g.fillRect(1+64*(xp-1),1+ 64*(yp+2), 62, 62); 
					int[] arr = {xp-1, yp+2};
				   	legalDests.add(arr);}
					if(checkDest(xp+1, yp -2) != 0) { g.fillRect(1+64*(xp+1),1+ 64*(yp-2), 62, 62); 
					int[] arr = {xp+1, yp-2};
				   	legalDests.add(arr);}
					if(checkDest(xp-1, yp -2) != 0) { g.fillRect(1+64*(xp-1),1+ 64*(yp-2), 62, 62); 
					int[] arr = {xp-1, yp-2};
				   	legalDests.add(arr);}
					break;
				case PAWN:  //all the moves possible for a pawn
					if (currPiece.white) { //if its white 
						if (checkDest(xp-1, yp-1) == 2) { 
							g.fillRect(1+64*(xp-1), 1+64*(yp-1), 62, 62);
							int[] arr0 = {xp -1, yp-1};
							legalDests.add(arr0);
						}
						if (checkDest(xp +1, yp -1) == 2) {
							g.fillRect(1+64*(xp+1), 1+64*(yp-1), 62, 62);
							int[] arr1 = {xp +1, yp-1};
							legalDests.add(arr1);
						}
						int k = checkDest(xp, yp -1);
						if(k != 1) { break;} //can't kill in a straight line
						g.fillRect(1+64*xp,1+ 64*(yp-1), 62, 62);
						int[] arr = {xp, yp-1}; 
					   	legalDests.add(arr);
						if(checkDest(xp, yp-2) == 1 && yp == 6) { g.fillRect(1+64*xp, 1+64*(yp-2), 62, 62); 
						int[] arr2 = {xp, yp-2};
					   	legalDests.add(arr2);}
					}
					else { //if its black
						if (checkDest(xp-1, yp+1) == 2) {
							g.fillRect(1+64*(xp-1), 1+64*(yp+1), 62, 62);
							int[] arr0 = {xp -1, yp+1};
							legalDests.add(arr0);
						}
						if (checkDest(xp +1, yp +1) == 2) {
							g.fillRect(1+64*(xp+1), 1+64*(yp+1), 62, 62);
							int[] arr1 = {xp +1, yp+1};
							legalDests.add(arr1);
						}
						int k = checkDest(xp, yp + 1);
						if (k != 1) { break;}
						g.fillRect(1+64*xp, 1+64*(yp+1), 62, 62); 
						int[] arr = {xp, yp+1};
					   	legalDests.add(arr);
						if (yp == 1 && checkDest(xp, yp +2) != 0 && k == 1) { g.fillRect(1+64*xp, 1+64*(yp+2), 62, 62); 
						int[] arr2 = {xp, yp+2};
					   	legalDests.add(arr2);}
					}
					break;
				case QUEEN: //the moves possile for  a queen or the moves possible for a rook +  bishop 
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp, yp + i);
						if (k == 0) {break; }
						g.fillRect(1+64*xp, 1+64*(yp+i), 62, 62);
						int[] arr = {xp, yp+i};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp + i, yp);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp +i),1+ 64*yp, 62, 62);
						int[] arr = {xp+i, yp};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp, yp - i);
						if (k == 0) {break; }
						g.fillRect(1+64*xp, 1+64*(yp-i), 62, 62);
						int[] arr = {xp, yp-i};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp -i),1+ 64*yp, 62, 62);
						int[] arr = {xp-i, yp};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp +i, yp + i);
						if (k == 0) {break; }
						g.fillRect(1+64*(xp+i), 1+64*(yp+i), 62, 62);
						int[] arr = {xp+i, yp+i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp +i, yp - i);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp +i),1+ 64*(yp-i), 62, 62);
						int[] arr = {xp+i, yp-i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp - i);
						if (k == 0) {break; }
						g.fillRect(1+64*(xp-i), 1+64*(yp-i), 62, 62);
						int[] arr = {xp-i, yp-i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp + i);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp -i),1+ 64*(yp+i), 62, 62);
						int[] arr = {xp-i, yp+i};
					   	legalDests.add(arr);
					   	if (k == 2) { break; }
					}
					break;
				case ROOK:  //all the moves possible for a rook
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp, yp + i);
						if (k == 0) {break; }
						g.fillRect(1+64*xp, 1+64*(yp+i), 62, 62);
						int[] arr = {xp, yp+i};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp + i, yp);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp +i),1+ 64*yp, 62, 62);
						int[] arr = {xp+i, yp};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp, yp - i);
						if (k == 0) {break; }
						g.fillRect(1+64*xp, 1+64*(yp-i), 62, 62);
						int[] arr = {xp, yp-i};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					for (int i = 1; i < 8; i++) {
						int k = checkDest(xp -i, yp);
						if (k == 0) { break; }
						g.fillRect(1+64*(xp -i),1+ 64*yp, 62, 62);
						int[] arr = {xp-i, yp};
					   	legalDests.add(arr);
						if (k == 2) { break; }
					}
					break;
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
					if (p.white) { Wlose = false; }
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
		public Piece getPiece(int x, int y) {		//get piece for mouse cords pressed
			for (Piece piece : pieces) {
				if (Math.floor((x-7)/64) == piece.i && Math.floor((y-30)/64) == piece.j) {
					return piece;
				}
			}
			return null;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		boolean whiteTurn = true; //White moves first at chess
		@Override
		public void mousePressed(MouseEvent e) {
			if (Wlose || Blose) { return; } //the game doesn't continue after someone lost
			if (currPiece == null) { 
				Piece p = getPiece(e.getX(), e.getY());
				if (p != null) {
					if (p.white == whiteTurn) {
						currPiece = p; //updating the current piece
					}
				}
			}	
			else { //moving the piece to the block
				int x =  (int) Math.floor((e.getX()-7)/64);
				int y = (int) Math.floor((e.getY()-30)/64);
				for (int[] arr : legalDests) {
					if (x == arr[0] && y == arr[1]) {
						try {
							currPiece.move(x, y);
							whiteTurn = !whiteTurn;
						} catch (Exception e1) {
							System.out.println("Problem");
						}
					}
				}
				currPiece = null; legalDests.clear();
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
