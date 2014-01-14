/* MachinePlayer.java */

package player;

import list.*;

/**
*  An implementation of an automatic Network player.  Keeps track of moves
*  made by both players.  Can select a move for itself.
*/
public class MachinePlayer extends Player {
	protected Board myBoard;
	protected int searchDepth = 3, myColor, oppColor, numTurn;
	protected final int MAX_VALUE = Integer.MAX_VALUE, MIN_VALUE = Integer.MIN_VALUE;
	protected Move nextMove;

	// Creates a machine player with the given color and search depth.  Color is
	// either 0 (black) or 1 (white).  (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this.myBoard = new Board();
		this.myColor = color;
		this.oppColor = 1 - this.myColor;
		this.searchDepth = searchDepth;
		this.numTurn = 0;
		this.nextMove = random();
	}

	// Creates a machine player with the given color.  Color is either 0 (black)
	// or 1 (white).  (White has the first move.)
	public MachinePlayer(int color) {
		this(color, 2);
	}

	// Returns a new move by "this" player.  Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		if (numTurn == 0) {
			Move temp = new Move(4,4);
			if(!myBoard.isLegalMove(temp, myColor)) {
				temp = new Move(4,5);
			}
			numTurn++;
			myBoard.doMove(temp, myColor); 
			return temp;
		} else if (numTurn == 1) {
			int x = 0, y = 0;
			if (myColor == 1) {
				x = 0;
				y = 4;
			} else if (myColor == 0) {
				x = 4;
				y = 0;
			}
			numTurn++;
			Move temp = new Move(x, y);
			myBoard.doMove(temp, myColor); 
			return temp;
		} else if (numTurn == 2) {
			int x = 0, y = 0;
			if (myColor == 1) {
				x = 7;
				y = 1;
			} else if (myColor == 0) {
				x = 1;
				y = 7;
			}
			numTurn++;
			Move temp = new Move(x, y); 
			myBoard.doMove(temp, myColor);
			return temp;
		}
		numTurn++;
		Best best = maxValue(myBoard, MIN_VALUE, MAX_VALUE, 0);
		myBoard.doMove(best.move, myColor);
		return best.move;

	} 

	private Move random() {
		Move result1;
		if (numTurn < 10) {
			result1 = new Move( (int)(Math.random()*8), (int)(Math.random()*8) );
			while (!myBoard.isLegalMove(result1, myColor)) {
				result1 = new Move( (int)(Math.random()*8), (int)(Math.random()*8) );
			}
		} else {
			result1 = new Move( (int)(Math.random()*8), (int)(Math.random()*8), (int)(Math.random()*6) + 1, (int)(Math.random()*6) + 1);
			while (!myBoard.isLegalMove(result1, myColor)) {
				result1 = new Move( (int)(Math.random()*8), (int)(Math.random()*8), (int)(Math.random()*6) + 1, (int)(Math.random()*6) + 1);
			}		
		}
		return result1;
	}

	protected void setBestMove(Move m) {
		this.nextMove = m;
	}

	/**
	 * This evaluates the max for minimax tree search, using alpha-beta pruning.
	 * @param board 
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return the best move available to this player
	 **/
	protected Best maxValue(Board board, int alpha, int beta, int depth){
		Best best = new Best(MIN_VALUE);
		Best reply;
		int temp;
		DList moves = board.allPossibleMoves(myColor);
		moves.restartIter();
		while (moves.hasNext()) {
			Move move =  (Move) moves.next().item();
			board.doMove(move, myColor);
			if (depth != searchDepth) {
				reply = minValue(board, alpha, beta, depth + 1);
				board.undoMove(move, myColor);
				if (reply.score >= best.score) {
					best.move = move;
					best.score = reply.score;
					alpha = reply.score;
				}
			} else {
				temp = board.eval(myColor);
				board.undoMove(move, myColor);
				if (temp >= best.score) {
					best.move = move;
					best.score = temp;
					alpha = temp;
				}
			}
			if (alpha >= beta) {
				return best;
			} 
		}
		return best;
	}

	/**
	 * This evaluates the min for minimax tree search, using alpha-beta pruning.
	 * @param board 
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return the best move available to this player
	 **/
	protected Best minValue(Board board, int alpha, int beta, int depth){
		Best best = new Best(MAX_VALUE);
		Best reply;
		int temp;
		DList moves = board.allPossibleMoves(oppColor);
		moves.restartIter();
		while (moves.hasNext()) {
			Move move =  (Move) moves.next().item();
			board.doMove(move, oppColor);
			if (depth != searchDepth) {
				reply = maxValue(board, alpha, beta, depth + 1);
				board.undoMove(move, oppColor);
				if (reply.score <= best.score) {
					best.move = move;
					best.score = reply.score;
					beta = reply.score;
				}
			} else {
				temp = board.eval(oppColor);
				board.undoMove(move, oppColor);
				if (temp <= best.score) {
					best.move = move;
					best.score = temp;
					beta = temp;
				}
			}
			if (alpha >= beta) {
				return best;
			} 
		}
		return best;
}



	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true.  If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player.  This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {
		return myBoard.doMove(m, this.oppColor);
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true.  If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player.  This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		numTurn++;
		return myBoard.doMove(m, this.myColor);
	}

}
