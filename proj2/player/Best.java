/* Best.java  */

package player;

public class Best {
	protected int score;
	protected Move move;
	
	public Best(int score) {
		this.score = score;
		this.move = null;
	}
	
	public Best() {
		this.score = 0;
		this.move = null;
	}
	
}