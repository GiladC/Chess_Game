import java.util.HashSet;
import java.util.Set;

public class Piece {
    int i, j; //cords
    boolean white; //whether white piece or not
	public PieceType type;
	static Set<Piece> pieces = new HashSet<Piece>(); //all the pieces on the board
	public Piece(int i, int j, boolean isWhite, PieceType type) {
		this.i = i; this.j = j; 
		this.white = isWhite;
		this.type = type;
		pieces.add(this);
	}
	public void move(int x, int y) throws Exception{
		Piece pieceDest = null;  //this is the piece that is currently placed on (i, j)
		for (Piece piece : pieces) {
			if (piece.i == x && piece.j == y) {
				if (piece.white == this.white) {
					throw new Exception("You can't kill your own piece."); 
				}
				pieceDest = piece;  
				break;
			}
		}
		this.i = x;	this.j = y;
		if (pieceDest != null) {
			pieces.remove(pieceDest); //killing the piece on destination
		}
	}
}
