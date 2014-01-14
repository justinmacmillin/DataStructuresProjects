/* Board.java */

package player;

import list.*;

public class Board {
	
	protected final int BLACK = 0;
	protected final int WHITE = 1;
	protected final int EMPTY = 2;
	protected final int OUTOFBOUNDS = 3;
	protected final int MAX_VALUE = Integer.MAX_VALUE, MIN_VALUE = Integer.MIN_VALUE;
	private final int sides = 8;
	private int[][] board;
	private int numWhite = 0;
	private int numBlack = 0;
	protected int longestBlack = 0;
	protected int longestWhite = 0;

	/**
	 * Creates a new Board object. Each spot on the board is initially set to empty. 
	 **/
	public Board() {
		board = new int[sides][sides];
		for (int x = 0; x < sides; x++) {
			for (int y = 0; y < sides; y++) {
				board[x][y] = EMPTY;
			}
		}
	}
	
	/**
	 * Returns the color of the chip at the given spot on the board, catches an 
	 * array index out of bounds exception if it tries to call a position
	 * (x,y) that is not on the board. 
	 * @param x value
	 * @param y value
	 * @return the color of the chip at the given spot. 
	 **/
	protected int contents(int x, int y) {
		try {
			return board[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return OUTOFBOUNDS;
		}
	}
	
	/**
	 * Returns the number of neighbors of a position (x,y) 
	 * @param x value
	 * @param y value
	 * @return int value of number of same color neighbors
	 **/
	protected int sameNeighbors(int x, int y, int color) {
		int tempX, tempY;
		int count = 0;	
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				tempX = x + i;
				tempY = y + j;
				if ( !((tempX == x) && (tempY == y)) ) {
					if (contents(tempX, tempY) == color) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Considers all the rules for a legal move. Takes in a move object and a color, returns
	 * true if given move is legal for the given color. 
	 * @param move the move the color wants to make
	 * @param color of the chip
	 * @return true if the move is legal
	 **/
	protected boolean isLegalMove(Move move, int color) {
		if (move.moveKind == Move.ADD) {
			if (color == WHITE) {
				if (numWhite == 10) {
					return false;
				}
			} else if (color == BLACK) {
				if (numBlack == 10) {
					return false;
				}
			}
		} else if (move.moveKind == Move.STEP) {
			if (contents(move.x2, move.y2) != color) {
				return false;
			}
		} else  if (move.moveKind == Move.QUIT) {
			return true;
		}
		int x = move.x1, y = move.y1;
	
		//Rule 1: No chip may be placed in any of the four corners.
		boolean corner = (x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7);
		if (corner) {
			return false;
		}
		//Rule 2: No chip may be placed in a goal of the opposite color.
		boolean oppGoal = false;
		if (color == WHITE) {
			oppGoal = (y == 0 || y == 7);
		} else if (color == BLACK) {
			oppGoal = (x == 0 || x == 7);
		}
		if (oppGoal) {
			return false;
		}
		//Rule 3: No chip may be placed in a square that is already occupied.
		boolean notTaken = (contents(x,y) == EMPTY);
		if (!notTaken) {
			return false;
		}
		// Rule 4: A player may not have more than two chips in a connected group, whether
		if (move.moveKind == Move.STEP) {
			board[move.x2][move.y2] = EMPTY;
		}
		int neighbors = sameNeighbors(x,y, color);
		if (neighbors > 1) {
			if (move.moveKind == Move.STEP) {
				board[move.x2][move.y2] = color;
			}
			return false;
		}
		int tempX, tempY;	
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				tempX = x + i;
				tempY = y + j;
				if (!((tempX == x) && (tempY == y))) {
					if (contents (tempX, tempY) == color) {
						if (sameNeighbors(tempX, tempY, color) > 0) {
							if (move.moveKind == Move.STEP) {
								board[move.x2][move.y2] = color;
							}
							return false;
						}
					}
				}
			}
		}
		if (move.moveKind == Move.STEP) {
			board[move.x2][move.y2] = color;
		}
		return true;
	}
	
	/**
	 * Returns a DList of every possible move for a given color. 
	 * @param color 
	 * @return DList of all possible moves
	 **/
	protected DList allPossibleMoves(int color) {
		int numberOfColor = 0;
		if (color == WHITE) {
			numberOfColor = numWhite;
		} else if (color == BLACK) {
			numberOfColor = numBlack;
		}
		DList result = new DList();
		Move possible;
		//Still adding
		if (numberOfColor < 10) {
			for (int x1 = 0; x1 <= 7; x1++) {
				for (int y1 = 0; y1 <= 7; y1++) {
					possible = new Move(x1, y1);
					if (isLegalMove(possible, color)) {
						result.insertFront(possible);
					}
				}
			}
		} else { // Considers STEP
			for (int x1 = 0; x1 <= 7; x1++) {
				for (int y1 = 0; y1 <= 7; y1++) {
					if (contents (x1, y1) == color) {
						for (int x2 = 0; x2 <= 7; x2++) {
							for (int y2 = 0; y2 <= 7; y2++) {
								possible = new Move(x2, y2, x1, y1);
								if (isLegalMove(possible, color)) {
									result.insertFront(possible);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Evaluates the winning chances of a board for a given color. First it checks 
	 * to see if its a winning board for it, or its opponent. If there isn't a winning
	 * network, then the board is given a score based on the chances of a possible
	 * winning network. It will give a high value score if the given color has a good
	 * chance of winning, it will give a negative value score if the opponent has a better
	 * chance of winning. 
	 * @param color that we want to give a score to
	 **/
	protected int eval(int color) {
		if (winNetwork(color)) {
			return MAX_VALUE;
		} else if (winNetwork(1-color)) {
			return MIN_VALUE;
		}
		boolean meInBoth = false, meInOne = false, oppInBoth = false, oppInOne = false;
		if (bothEndZones(color)) {
			meInBoth = true;
		} else if (oneEndZones(color)) {
			meInOne = true;
		}

		if (bothEndZones(1 - color)) {
			oppInBoth = true;
		} else if (oneEndZones(1 - color)) {
			oppInOne = true;
		}
		
		int myConn = numConnections(color);
		int oppConn = numConnections(1 - color);
		int myLongest = longestNetwork(color);
		int oppLongest = longestNetwork(1-color);
		int result = 10*(myConn - oppConn) + 30*(myLongest - oppLongest);
		if (meInBoth) {
			result += 10;
		} else if (meInOne) {
			result += 5;
		}
		
		if (oppInBoth) {
			result -= 10;
		} else if (oppInOne) {
			result -= 5;
		}
		
		return result;
		
	}
	
	/**
	 * Adds a chip to a given position (x,y). Does not check for legality of the
	 * move because the doMove method does. 
	 * @param x position
	 * @param y position
	 * @param color 
	 **/
	protected void addChip(int x, int y, int color) {
		switch (color) {
			case WHITE:
				numWhite++;
				board[x][y] = WHITE;
				break;
			case BLACK:
				numBlack++;
				board[x][y] = BLACK;
				break;
		}
	}

	/**
	 * Moves a chip from (x2,y2) to (x1,y1) and sets (x2,y2) to empty. Does not
	 * check for legality of the move becuase the doMove method does. 
	 * @param x1 new x position
	 * @param y1 new y position
	 * @param x2 old x position
	 * @param y2 old y position
	 **/
	protected void moveChip(int x1, int y1, int x2, int y2) {
		board[x1][y1] = contents(x2,y2);
		board[x2][y2] = EMPTY;
	}

	/**
	 * Do move performs a move on the board. It returns true if the move is legal,
	 * false otherwise.
	 * @param move in question
	 * @param color of the chip being added or moved
	 * @return true if the move is legal, false otherwise
	 **/
	protected boolean doMove(Move m, int color) {
		if (isLegalMove(m, color)) {
			if (m.moveKind == Move.ADD) {
				addChip(m.x1, m.y1, color);
			} else if (m.moveKind == Move.STEP) {
				moveChip(m.x1, m.y1, m.x2, m.y2);
			} else if (m.moveKind == Move.QUIT) {
				return true;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Undoes a move. If it was an add move, it sets the spot in that move to 
	 * empty. It it was a step move, it moves the chip back to its old spot. 
	 * @param move in question
	 * @param color of the chip that was moved
	 **/
	protected void undoMove(Move m, int color) {
		if (m.moveKind == Move.ADD) {
			switch (color) {
				case WHITE:
					numWhite--;
					board[m.x1][m.y1] = EMPTY;
					break;
				case BLACK:
					numBlack--;
					board[m.x1][m.y1] = EMPTY;
					break;
			}
		} else if (m.moveKind == Move.STEP) {
			moveChip(m.x2, m.y2, m.x1, m.y1);
		}
	}
	
	/**
	 * Returns an integer array of length 3 of each direction away from a position (x,y). Goes
	 * through each direction and puts the int value of the first chip it sees that isn't 
	 * EMPTY in index 0 of the array. Index 1 of the array is the x value of that chip, index 2
	 * of that array is the y value of that chip.
	 * @param x position
	 * @param y position
	 * @return int[] of [integer of chip color, x value, y value]
	 **/
	protected int[] clockwiseDiagnols(int x, int y, int direction) {
		int[] chips = new int[3];
		int diagx;
		int diagy;

		if (direction == 0) { // up		
			diagx = x;
			diagy = y-1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy--;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 1) {// up right
			diagx = x+1;
			diagy = y-1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy--;
				diagx++;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 2) { // right
			diagx = x+1;
			diagy = y;		
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagx++;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 3) {// right down
			diagx = x+1;
			diagy = y+1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy++;
				diagx++;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 4) { // down
			diagx = x;
			diagy = y+1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy++;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 5) { // down left
			diagx = x-1;
			diagy = y+1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy++;
				diagx--;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 6) { // left
			diagx = x-1;
			diagy = y;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagx--;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		} else if (direction == 7) { // up left
			diagx = x-1;
			diagy = y-1;
			while (contents(diagx,diagy)!= OUTOFBOUNDS && contents(diagx,diagy) == 2) {
				diagy--;
				diagx--;
			}
			chips[0] = contents(diagx,diagy);
			chips[1] = diagx;
			chips[2] = diagy;
		}
		return chips;
	}

	/**
	 * Goes through every chip on the board and returns the number of connections it has 
	 * with chips of its same color. Returns the number of connections, divided by 2 to 
	 * account for overcounting connections.
	 * @param color
	 * @return integer of the number of connections
	 **/
	protected int numConnections(int color) {
		int total = 0;
		for (int ind1 = 0; ind1 <= 7; ind1++) {
			for (int ind2 = 0; ind2 <= 7; ind2++) {
				for(int i = 0; i<8; i++){
					if ((clockwiseDiagnols(ind1, ind2, i))[0] == color) {
						total++;
					}
				}
			}
		}
		return total;
	}
	
	/**
	 * Returns true if a black chip is in the bottom win zone, or if 
	 * a white chip is in its right win zone. 
	 * @param x position
	 * @param y position
	 * @return true if there is a chip in the win zone, false otherwise
	 **/
	protected boolean chipInWinZone(int x, int y) {
		if (contents(x,y) == BLACK) {
			if (y == 7) {
				return true;
			}
			return false;
		} else if (contents(x,y) == WHITE) {
			if (x == 7) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Returns true if a black chip its in its top win zone, or if
	 * a white chip is in its left win zone. 
	 * @param x position
	 * @param y position
	 * @return true if there is a chip in the win zone, false otherwise
	 **/
	protected boolean chipInWinZone0(int x, int y) {
		if (contents(x,y) == BLACK) {
			if (y == 0) {
				return true;
			}
			return false;
		} else if (contents(x,y) == WHITE) {
			if (x == 0) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Converts a position (x,y) to an index for the board.
	 * @param x position
	 * @param y position
	 * @return index
	 **/
	protected int index(int x, int y) {
		return x + y * sides;
	}
	
	/**
	 * Checks an index of already visited spots and looks for a chip in it. Returns
	 * true if the spot given to the method is already visited. 
	 * @param x position
	 * @param y position
	 * @param exceptions is an integer array of spaces already visited
	 * @param spot is the index in question
	 * @return true if the spot has been visited
	 **/
	protected boolean inIndex(int x, int y, int[] exceptions, int spot) {
		int index = index(x,y);
		for (int i = 0; i < spot; i++) {
			int ind = exceptions[i];
			if (ind == index) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Takes in an integer representing a direction and returns in integer 
	 * that represents the opposite direction.
	 * @param x represents a direction
	 * @return the opposite direction of x
	 **/
	protected int oppositeDir(int x) {
		return (x+4)%8;
	}
	
	/**
	 * Looks at the end zones on the board. Returns true if the given
	 * color only has one chip in either of its two endzones. 
	 * @param color 
	 * @return true if there is only one chip in either of the winzones for that color, false otherwise
	 **/
	protected boolean oneEndZones(int color) {
		if (color == WHITE) {
			for (int y = 1; y < 7; y++) {
				if ((contents(0,y) == color) || (contents(7,y) == color)){
					return true;
				}
			}
		} else if (color == BLACK) {
			for (int x = 1; x < 7; x++) {
				if ((contents(x,0) == color) || (contents(x,7) == color)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Looks at the end zones on the board. Returns true if the given 
	 * color only has a chip in both of its winzones.
	 * @param color
	 * @return true if the color has a chip in both of its winzones, false otherwise
	 **/
	protected boolean bothEndZones(int color) {
		if (color == WHITE) {
			for (int y = 1; y < 7; y++) {
				if (contents(0,y) == color){
					for (int y1 = 1; y1 < 7; y1++) {
						if (contents(7, y1) == color) {
							return true;
						}
					}
				}
			}
		} else if (color == BLACK) {
			for (int x = 1; x < 7; x++) {
				if (contents(x,0) == color){
					for (int x1 = 1; x1 < 7; x1++) {
						if (contents(x1, 7) == color) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Takes in a color and returns the instance variable that represents that 
	 * color's longest network.
	 * @param color
	 * @return instance variable for longest network for the given color
	 **/
	protected int longestNetwork (int color) {
		if (color == BLACK) {
			return longestBlack;
		} else {
			return longestWhite;
		}
	}
	
	/**
	 * Returns true if there is a valid network for a color. The function takes in 
	 * a lastDir parameter so that it will not check for another chip in the same
	 * direction it came from. This method runs recursively on the board through
	 * each position on the board to find a network. If it finds another chip of 
	 * the same color, it will recursively call network again on that position, 
	 * and go in a different direction looking for another chip of the same color.
	 * If the count reaches six, and the first and last chips are in the win zone, 
	 * then it will return true.
	 * @param x position
	 * @param y position
	 * @param color 
	 * @param lastDir is the last direction that the network came from
	 * @param visited is an int array of already visited position
	 * @param count is the length of the network
	 * @param spot is the number of spots to view in the array.
	 * @return true if there is a valid network for a color, false otherwise
	 **/
	protected boolean network(int x, int y, int color, int lastDir, int[] visited, int count, int spot) {
		// base case
		if (chipInWinZone(x,y)) {
			if (count == 6) {
				return true;
			} else {
				return false;
			}
		}
		// already visited spots
		visited[spot] = index(x,y);
		spot++;	
		
		// updates longest networks
		if (color == BLACK) {
			if (count > longestBlack) {
				longestBlack = count;
			}
		} else {
			if (count > longestWhite) {
				longestWhite = count;
			}
		}
		
		// checks each direction
		for (int dir = 0; dir < 8; dir ++) {
			int[] chip = clockwiseDiagnols(x,y,dir);
			
			// if same direction as last, go to next diag
			if (dir == lastDir) {
				continue;
			}
			
			// if finds chip of same color that isn't already visited
			if (chip[0] == color && !inIndex(chip[1],chip[2],visited, spot+1)) {
				
				// checks if turns corner
				if (oppositeDir(lastDir) != dir) {
					if (network (chip[1], chip[2], color, oppositeDir(dir), visited, count+1, spot) == true) {
						return true;
					} 
				// if doesn't, go to next diagnol
				} else {
					continue;
				}
			}
		}
		return false;
	}

	/**
	 * The printBoard method takes in no parameters and does not 
	 * return anything, it only prints the board for testing purposes. 
	 **/
	public void printBoard() {
		for (int j = 0; j < 8; j++) {
			System.out.print("| ");
			for (int i = 0; i < 8; i++) {
				System.out.print(board[i][j]);
				System.out.print(" | ");
			}
			System.out.println();
			System.out.println(" ________________________________ ");
		}
	}
	
	/** 
	 * This method returns true if there is a winning network for a given
	 * color. It checks if there is a winning network for white by 
	 * calling network on each of the chips in its win zone on the left
	 * of the board. It checks if there is a winning network for black 
	 * by calling network on each of the chips in its win zone on the 
	 * top of the board. 
	 * @param color
	 * @return true if there is a winning network for the color
	 **/
	protected boolean winNetwork (int color) {
		longestBlack = 0;
		longestWhite = 0;
		if (color == BLACK) {
			for (int ind = 1; ind < 7; ind++) {
				if (contents(ind, 0) == BLACK) {
					if (network (ind, 0, BLACK, 2, new int[10], 1, 0) == true) {
						return true;
					}
				}
			}
			return false;
		}
		if (color == WHITE) {
			for (int ind = 1; ind < 7; ind++) {
				if (contents(0, ind) == WHITE) {
					if (network (0, ind, WHITE, 0, new int[10], 1, 0) == true) {
						return true;
					}
				}
			}
			
			return false;
		}
		return false;
	}
		
	public static void main (String[] args) {
		// Board board = new Board();
		// System.out.println();

		// board.addChip(6,0,0);
		// board.addChip(6,5,0);
		// board.addChip(5,5,0);
		// board.addChip(3,3,0);
		// board.addChip(3,5,0);
		// board.addChip(5,7,0);

		// board.addChip(2,0,0);
		// board.addChip(2,5,0);
		// board.addChip(3,5,0);
		// board.addChip(1,3,0);
		// board.addChip(3,3,0);
		// board.addChip(5,5,0);
		// board.addChip(5,7,0);
		// to break the second network add a white chip at square (5,6)
		// board.addChip(5,6,1);
		
		// board.printBoard();
		// System.out.println("number of connections: " + board.numConnections(0));
		// System.out.println("win for black: " + board.winNetwork(0));
		// System.out.println("win for white: " + board.winNetwork(1));
		
	}
	
}