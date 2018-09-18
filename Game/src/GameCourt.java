import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	private final String SCORE_FILE = "files/highscore.txt";
	private static final int DIM_SIZE = 8; // size of board
	private JButton button[][] = new JButton[DIM_SIZE][DIM_SIZE]; // buttons on the board
	private static int board[][] = new int[DIM_SIZE][DIM_SIZE];
	private JLabel status; // Current status text, i.e. "Score = __"
	private static ShipStates ships = new ShipStates();
	
	// Game constants
    public static final int COURT_WIDTH = 250;
    public static final int COURT_HEIGHT = 250;
    private static final int USE = 1; // ship component placed on tile/button
    private static final int EMPTY = 0; // tile is empty
    private static final int GUESS = -1; // player clicked on button
    private int guesses = 0; // number of guesses
    private int [] shipLengths = new int[]{2,3,4,5}; // length of the ships
   
	public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // define and add JButtons
        for (int r = 0; r < DIM_SIZE; r++) {
        	for (int c = 0; c < DIM_SIZE; c++) {
        		button[r][c] = new JButton();
                button[r][c].setPreferredSize(new Dimension(25, 25));
                button[r][c].addActionListener(new ButtonClicked(r,c));
                add(button[r][c]);
            }
        } 
        this.status = status;
	}
	
	/**
	 * Return the 2D array of buttons
	 * @return button
	 */
	public JButton[][] button() {
		return button;
	}
	
	/**
	 * Return the status of the game, i.e. Score =...
	 * @return JLabel status
	 */
	public JLabel status() {
		return status;
	}
	
    public class ButtonClicked implements ActionListener {   
    	int row; //integer to store the value of which row the button that was pressed is in
        int col; //integer to store the value of which column the button that was presses is in
        
        /**
         * Class Constructor.
         * 
         * @param   row which row the button pressed resides in
         * @param   column  which column the button pressed resides in
         */
        public ButtonClicked(int row, int column) {
        	this.row = row;
            col = column;
        }
        
        /**
         * Checks if the guess was a hit or miss.  Displays the result on the JFrame.
         * 
         * @param event the specific ActionEvent that was triggered
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        	if (board[row][col] != GUESS) { // if the tile has already been hit, do nothing
        		guesses++; // increment number of guesses
	        	switch (board[row][col]) {
	        		case EMPTY:  // if the tile hit is empty
	        			button[row][col].setBackground(Color.BLUE); // change the button to blue
	        			break;
	        		case USE: // if the tile has a ship
	        			button[row][col].setBackground(Color.RED);
	        			ships.addHit(row, col, guesses);
	        			break;
	        	}
	        	board[row][col] = GUESS; 
	            status.setText("Score = " + ships.getScore() + " Guesses = " + guesses); // update status
	            
	            // allows for the new background color to show
	            button[row][col].setOpaque(true);
	            button[row][col].setBorderPainted(false);
	            
	            // get high scores once game ends
	            if (gameOver()) {
	            	askHighscore();
	            }
        	} 
        }
    }
	
    /**
     * Reset the game.
     */
	public void reset() {
        guesses = 0;
        ships.reset();
        
        // set all of the tiles to empty and buttons to white
        for (int i = 0; i < DIM_SIZE; i++) {
            for (int j = 0; j < DIM_SIZE; j++) {
                board[i][j] = EMPTY;
                button[i][j].setBackground(Color.WHITE);
                button[i][j].setOpaque(true);
                button[i][j].setBorderPainted(false);
            }            
        }
        
        // place each ship successfully
        for (int length : shipLengths) {
        	boolean success = placeShip(length, 100);
        	if (!success) {
        		placeShip(length, 100);
        	}
        }
        // update score/status
        status.setText("Score = " + ships.getScore() + " Guesses = " + guesses);
    }
	
	/**
	 * Display the top 5 high scores
	 */
	private void askHighscore() {
		// update score
		int score = guesses + ships.getScore();
		
		// ask user for name
		String player = JOptionPane.showInputDialog("Congrats you won! Your score is "
				+ score + ". Enter your name!");
		if (player == null || player.equals("")) {
			player = "Player";
		}
		
		// update high scores
		Highscore highscores = new Highscore(score, player, SCORE_FILE);
		int[] scores = highscores.getScores();	
        String[] players = highscores.getPlayers();
        
        // display high scores
        String display = "";
        for (int i = 0; i < highscores.getTopKNumber() && i < scores.length; i++) {
        	if (players[i] == null) {
        		continue;
        	} else {
        		display = display + players[i] + " " + scores[i] +  "\n";
        	}
        }
        JOptionPane.showMessageDialog(null, display, "Highscores", 3);
        reset(); // reset game
    }
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
	
	/**
	 * Checks if the game is over
	 * @return true if the game is over
	 */
	private boolean gameOver() {
        for (int i = 0; i < DIM_SIZE; i++) {
            for (int j = 0; j < DIM_SIZE; j++) {
                // if the board doesn't contain a ship or if it contains a ship 
            	// but the position has already been located
                if (board[i][j] == USE) {
                    return false;
                }
            }
        }
        return true;
    }
	
	/**
	 * Place the ship of length to the board randomly with a trial limit
	 * @param length - length of this ship vertically or horizontally.
	 */
	private static boolean placeShip(int length, long limit) {
		if (limit <= 0) {
			return false;
		}
		
		//Randomize the position and orientation
        Random rand = new Random();
        boolean vertical = rand.nextBoolean(); // vertical vs. horizontally
        boolean direction = rand.nextBoolean(); //left-to-right, or top-down
        int x = rand.nextInt(DIM_SIZE);
        int y = rand.nextInt(DIM_SIZE);   
        
        // check if ship is placeable
        boolean success = isShipPlacable(x, y, length, vertical, direction); 
        if (success) {
        	placeShipBoard(x, y, length, vertical, direction);
        	ships.addShip(x, y, length, vertical, direction);
        	return true;
        } else {
        	return placeShip(length, limit - 1); // recursively call for more placements
        }
	}
	
	/**
	 * Check if this ship can be placed at the given location
	 * @param x - row coordinates
	 * @param y - col coordinates
	 * @param length - length of the size to be placed
	 * @param vertical - true if vertical, false if horizontal
	 * @param direction - true if the ship index goes from the current position towards a bigger index
	 * @return
	 */
	private static boolean isShipPlacable(int x, int y, int length, boolean vertical, boolean direction) {
		if( x < 0 || y < 0 || x > DIM_SIZE || y > DIM_SIZE) {
			return false;
		}
		
		if (length <= 0) {
			return true;
		}
		
		if (vertical) {
			if (direction) {
				if (y + length < DIM_SIZE && board[x][y] == EMPTY) {
					return isShipPlacable(x, y + 1, length - 1, vertical, direction);
			 	} else {
			 		return false;
			 	}
			} else{
				if (y - length >= 0 && board[x][y] == EMPTY) {
					return isShipPlacable(x, y - 1, length - 1, vertical, direction);
			 	} else {
			 		return false;
			 	}
			} 
		} else {
			if (direction) {
				if (x + length < DIM_SIZE && board[x][y] == EMPTY) {
					return isShipPlacable(x + 1, y, length - 1, vertical, direction);
		 		} else {
		 			return false;
		 		}
			} else {
				if(x - length >= 0 && board[x][y] == EMPTY) {
					return isShipPlacable(x - 1, y, length - 1, vertical, direction);
				} else {
					return false;
				}
			}
		}
	}
	
	/**
	 * Place a ship to the board at the given location
	 * @param x - row coordinates
	 * @param y - col coordinates
	 * @param size - length of ship
	 * @param vertical - vertical or horizontal
	 * @param direction - true if the ship index goes from the current position towards a bigger index
	 */
	private static void placeShipBoard(int x, int y, int size, boolean vertical, boolean direction) {
		if (vertical) {
			if (direction) {
				for (int i = y; i < y + size && i < DIM_SIZE; i++) {
					board[x][i] = USE;
                }                                 
            } else {
            	for (int i = y; i > y - size && i >= 0; i--) {
            		board[x][i] = USE;
            	} 
            }
        } else {        
        	if (direction) {
        		for (int i = x; i < x + size && i < DIM_SIZE; i++) {    
        			board[i][y] = USE;                         
                }
        	} else {
        		for (int i = x; i > x - size && i >= 0; i--) {    
        			board[i][y] = USE;                         
        		}
        	}
	    }
	}
}
