import java.util.HashSet;
import java.util.Set;

public class Piece {
    int x, y; //cords
    boolean isWhite; //whether white piece or not
	public PieceType type;
	static Set<Piece> pieces = new HashSet<Piece>();
	public Piece(int x, int y, boolean isWhite, PieceType type) {
		this.x = x; this.y = y; 
		this.isWhite = isWhite;
		this.type = type;
		pieces.add(this);
	}
	public void move(int x, int y) throws Exception{
		for (Piece piece : pieces) {
			if (piece.x == this.x && piece.y == this.y) {
				if (piece.isWhite == this.isWhite) {
					throw new Exception("You can't kill your own piece.");
				}
				this.x = x;	this.y = y;
				piece.kill();  //kill method will be added later
			}
		}
		this.x = x; this.y = y;
	}
	private void kill() {
		pieces.remove(this);
	}
}
