import java.util.HashSet;
import java.util.Set;

public class Piece {
    int i, j; //cords
    boolean white; //whether white piece or not
	public PieceType type;
	static Set<Piece> pieces = new HashSet<Piece>();
	public Piece(int i, int j, boolean isWhite, PieceType type) {
		this.i = i; this.j = j; 
		this.white = isWhite;
		this.type = type;
		pieces.add(this);
	}
	public void move(int i, int j) throws Exception{
		for (Piece piece : pieces) {
			if (piece.i == i && piece.j == j) {
				if (piece.white == this.white) {
					throw new Exception("You can't kill your own piece.");
				}
				this.i = i;	this.j = j;
				piece.kill();  //kill method will be added later
			}
		}
		this.i = i; this.j = j;
	}
	private void kill() {
		pieces.remove(this);
	}
}
