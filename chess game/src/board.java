import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

public class board {
    static Piece currPiece = null; //the piece that was last clicked on
	public static void main(String[] args) throws IOException{
		Set<Piece> pieces = Piece.pieces;
		BufferedImage piecesImage = ImageIO.read(new File("chess_pieces.png"));
		Image[] allPieces = new Image[12]; //6 pieces for black and 6 pieces for white.
		int index = 0;
		for(int i = 0; i < 400; i +=200) {
			for(int j = 0; j < 1200; j+=200) {
				allPieces[index] = piecesImage.getSubimage(j, i, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				index++;
			}
		}
		for (int i = 0; i < 8; i++) {
			pieces.add(new Piece(i, 1, false, PieceType.PAWN));
			pieces.add(new Piece(i, 6, true, PieceType.PAWN));
			switch(i) {
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
	    boolean legalDest(int x, int y) { //Checks whether x,y is a legal destination for currPiece.
		   	if( x > 7 || y > 7 || x < 0 || y < 0) {
		   		return false;
		   	}
		   	for (Piece piece : pieces) {
		   		if (piece.i == x && piece.j == y && piece.white == currPiece.white) {
		   			return false;
		   		}
		   	}
		   	return true;
	    }
		    	
		private static final long serialVersionUID = -4396970882832779043L;
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
					g.fillRect(x*64, y*64, 64, 64);
				}
			}
			for (Piece piece : pieces) {
				int i = 0;
				switch(piece.type) {
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
				g.drawImage(allPieces[i], piece.i*64, piece.j*64, this);
			}
			if (currPiece != null) {
				g.setColor(new Color(51,255,255));
				switch(currPiece.type) {
				case KING: //has 8 optional moves 
					
				}
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

		@Override
		public void mousePressed(MouseEvent e) {
			if (currPiece == null) {
				currPiece = getPiece(e.getX(), e.getY());
			}			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			frame.repaint();
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
