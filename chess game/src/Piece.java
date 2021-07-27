import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	/*ret = 0 iff not legal destination
	 *ret = 1 iff legal destination and NO enemy piece killed
	 *ret = 2 iff legal destination and enemy piece killed
	 *(x,y) are destination coords, isWhite is the white field of the piece.
     */
    int checkDest(int x, int y) {  
   	if( x > 7 || y > 7 || x < 0 || y < 0) { //can't go out of board
   		return 0;
   	}
   	for (Piece piece : pieces) {
   		if (piece.i == x && piece.j == y) {
   			if(piece.white == this.white) { return 0; } //can't kill your own piece
   			return 2;  //kills a piece
   		}

   	}
   	return 1; //a possible move without any piece being killed
    }
	/*
	 * generates all destinations possible for the piece
	 * doesn't include castle for king
	 */
	public List<int[]> getDests(){
		List<int[]> lst = new ArrayList<>();
		switch(this.type) {	
		case KING:
	    	if(checkDest(i, j +1) != 0) { 
	    	int[] arr = {i, j+1};
	     	lst.add(arr);}
	    	if(checkDest(i, j -1) != 0) { 
	    	int[] arr = {i, j-1};
	    	lst.add(arr);}
	    	if(checkDest(i -1, j) != 0) { 
	    	int[] arr = {i-1, j};
	    	lst.add(arr);}
	    	if(checkDest(i +1, j) != 0) {
	    	int[] arr = {i+1, j};
	    	lst.add(arr);}
	    	if(checkDest(i -1, j-1) != 0) { 
	    	int[] arr = {i-1, j-1};
	    	lst.add(arr);}
	    	if(checkDest(i -1, j+1) != 0) {
		    int[] arr = {i-1, j+1};
	    	lst.add(arr);}
	    	if(checkDest(i +1, j+1) != 0) { 
	    	int[] arr = {i+1, j+1};
	    	lst.add(arr);}
	    	if(checkDest(i +1, j-1) != 0) {
		    int[] arr = {i+1, j-1};
	    	lst.add(arr);}
			break;
		case BISHOP:
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i +ip, j + ip);
				if (k == 0) {break; }
				int[] arr = {i+ip, j+ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i +ip, j - ip);
				if (k == 0) { break; }
				int[] arr = {i+ip, j-ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j - ip);
				if (k == 0) {break; }
				int[] arr = {i-ip, j-ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j + ip);
				if (k == 0) { break; }
				int[] arr = {i-ip, j+ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			break;
		case KNIGHT:
			if(checkDest(i+2, j +1) != 0) {  
			int[] arr = {i+2, j+1};
		   	lst.add(arr);}
			if(checkDest(i+2, j -1) != 0) { 
			int[] arr = {i+2, j-1};
			lst.add(arr);}
			if(checkDest(i-2, j +1) != 0) {  
			int[] arr = {i-2, j+1};
			lst.add(arr);}
			if(checkDest(i-2, j -1) != 0) { 
			int[] arr = {i-2, j-1};
			lst.add(arr);}
			if(checkDest(i+1, j +2) != 0) {
			int[] arr = {i+1, j+2};
			lst.add(arr);}
			if(checkDest(i-1, j +2) != 0) { 
			int[] arr = {i-1, j+2};
			lst.add(arr);}
			if(checkDest(i+1, j -2) != 0) { 
			int[] arr = {i+1, j-2};
			lst.add(arr);}
			if(checkDest(i-1, j -2) != 0) { 
			int[] arr = {i-1, j-2};
			lst.add(arr);}
			break;
		case PAWN:
			if (this.white) { //if its white 
				if (checkDest(i-1, j-1) == 2) { 
					int[] arr0 = {i -1, j-1};
					lst.add(arr0);
				}
				if (checkDest(i +1, j -1) == 2) {
					int[] arr1 = {i +1, j-1};
					lst.add(arr1);
				}
				if(checkDest(i, j -1) != 1) { break;} //can't kill forward
				int[] arr = {i, j-1}; 
				lst.add(arr);
				if(checkDest(i, j-2) == 1 && j == 6) { 
				int[] arr2 = {i, j-2};
				lst.add(arr2);}
			}
			else { //if its black
				if (checkDest(i-1, j+1) == 2) {
					int[] arr0 = {i -1, j+1};
					lst.add(arr0);
				}
				if (checkDest(i +1, j +1) == 2) {
					int[] arr1 = {i +1, j+1};
					lst.add(arr1);
				}
				if (checkDest(i, j+1) != 1) { break;} //can't attack forward
				int[] arr = {i, j+1};
			   	lst.add(arr);
				if (j == 1 && checkDest(i, j +2) == 1 ) { 
				int[] arr2 = {i, j+2};
				lst.add(arr2);}
			}
			break;
		case ROOK:
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i, j + ip);
				if (k == 0) {break; }
				int[] arr = {i, j+ip};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i + ip, j);
				if (k == 0) { break; }
				int[] arr = {i+ip, j};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i, j - ip);
				if (k == 0) {break; }
				int[] arr = {i, j-ip};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j);
				if (k == 0) { break; }
				int[] arr = {i-ip, j};
				lst.add(arr);
				if (k == 2) { break; }
			}
			break;
		case QUEEN: //all destinations for queen are all destinations for rook + all destinations for bishop
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i +ip, j + ip);
				if (k == 0) {break; }
				int[] arr = {i+ip, j+ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i +ip, j - ip);
				if (k == 0) { break; }
				int[] arr = {i+ip, j-ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j - ip);
				if (k == 0) {break; }
				int[] arr = {i-ip, j-ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j + ip);
				if (k == 0) { break; }
				int[] arr = {i-ip, j+ip};
			   	lst.add(arr);
			   	if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i, j + ip);
				if (k == 0) {break; }
				int[] arr = {i, j+ip};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i + ip, j);
				if (k == 0) { break; }
				int[] arr = {i+ip, j};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i, j - ip);
				if (k == 0) {break; }
				int[] arr = {i, j-ip};
				lst.add(arr);
				if (k == 2) { break; }
			}
			for (int ip = 1; ip < 8; ip++) {
				int k = checkDest(i -ip, j);
				if (k == 0) { break; }
				int[] arr = {i-ip, j};
				lst.add(arr);
				if (k == 2) { break; }
			}
			break;
		}
		return lst;
	}
}
