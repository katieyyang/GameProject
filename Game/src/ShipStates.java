import java.util.Set;
import java.util.TreeSet;
/*
 * Class for Storing the states of ships
 * The score computed incrementally to reflect how fast
 * a player can predict the orientation of a ship.
 * 
 * A player receives a lower score if he/she can predict 
 * and hit the entire body of each ship quickly.
 * 
 * Final game score = # of guesses to hit the entire body of all ships
 *   + sum of fastness of hitting each ship.
 * The lower of this final score is, the higher ranking of 
 * this player is.
 * 
 * Thus this class keeps track the state when was hit last time 
 * in some body of each ship, and  accumulates points for each 
 * ship when it is clicked again by a player
 */
public class ShipStates {
	private static Set<Ship> ships; // set of ship states
	private static int totalScore; // ship hit score accumulated
	
	public ShipStates() {
		totalScore = 0;
		ships = new TreeSet<Ship>();	
	}
	
	/**
	 * Create a ship state
	 * 
	 * @param x - x coordinate/row value on the board
	 * @param y - y coordinate/col value on the board
	 * @param length - length of the ship
	 * @param vertical - true if vertical, false if horizontal
	 * @param direction - true if the ship index goes from the current position towards a bigger index
	 */
	public void addShip(int x, int y, int length, boolean vertical, boolean direction) {
		Ship s = new Ship(x, y, length, vertical, direction);
		ships.add(s);
	}
	
	/**
	 * Add a clicked position
	 * @param x - row coordinate
	 * @param y - col coordinate
	 * @param timeStep - # of guesses when tile is hit
	 */
	public void addHit(int x, int y, int timeStep) {
		for (Ship s : ships) {
			if (s.match(x, y)) {
				totalScore += s.computeScore(timeStep);
				s.setLastHitTime(timeStep);
			}
		}
	}
	
	/**
	 * Return the accumulated ship hit score
	 * @return total score
	 */
	public int getScore(){
		return totalScore;
	}
	
	/**
	 * Reset the ship states
	 */
	public void reset() {
		ships.clear();
		totalScore = 0;
	}
	
	/**
	 * Keep track per-ship score and the last hit time step
	 * A player gets better reward if he/she can hit the same ship quickly
	 */
	class Ship implements Comparable<Ship> {
		private int lastHitStep;
	    private boolean vertical; // vertical vs. horizontally
	    private boolean direction;
	    private int row;
	    private int col;
	    private int size;
	    
	    /**
	     * Constructor
	     * @param x - row coordinate
	     * @param y - col coordinate
	     * @param size - length of ship
	     * @param vertical - true if vertical, false if horizontal
	     * @param direction - true if the ship index goes from the current position towards a bigger index
	     */
		public Ship(int x, int y, int size, boolean vertical, boolean direction) {
			lastHitStep = -1;
			this.vertical = vertical;
			this.direction = direction;
			this.row = x;
			this.col = y;
			this.size = size;
		}
		
		/**
		 * Ship hit score depends on when this ship was last time
		 * @param timeStep number of guesses in the game
		 * @return the score based off of the hits
		 */
		private int computeScore(int timeStep) {
			if (lastHitStep != -1) {
				return timeStep - lastHitStep;
			}
			return 1;
		}
		
		/**
		 * Update the last ship hit time
		 * @param timeStep
		 */
		private void setLastHitTime(int timeStep) {
			lastHitStep = timeStep;	
		}
		
		/**
		 * Check if there is a ship at position (x, y)
		 * @param x - row coordinate
		 * @param y - col coordinate
		 * @return true if there ship is at position (x, y)
		 */
		private boolean match(int x, int y) {
			if (vertical) {
				if (x != row) {
					return false;
				}
				
				if (direction) {
					for (int i = col; i < col + size; i++) {
						if (i == y) {
							return true;
						}
					}
				} else {
					for (int i = col; i > col - size; i--) {
						if (i == y) {
							return true;
						}
					}
				}				
			} else {
				if (y != col) {
					return false;
				}
				
				if (direction) {
					for (int i = row; i < row + size; i++) {
						if (i == x) {
							return true;
						}
					}
				} else {
					for (int i = row; i > row - size; i--) {
						if (i == x) {
							return true;
						}
					}
				}
			}
			return false;
		}
		
		/**
		 * Make this Ship object comparable
		 * @param o  - other ship to compare to
		 * @return 0 if equal; -1 if smaller; 1 if bigger
		 */	
		@Override
		public int compareTo(Ship o) {
			if(this.row== o.row && this.col == o.col){
				return 0;
			}
			if( this.row < o.row) {
				return -1;
			}
			if ( this.row == o.row && this.col< o.col)
				return -1;
			return 1;
		}
	}
}