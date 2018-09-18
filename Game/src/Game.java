/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
    	final JFrame frame = new JFrame("Battleship");
        frame.setLocation(300, 300);
        frame.setResizable(false);
        
        // Instructions screen
    	JOptionPane.showMessageDialog(frame,
                "Sink all 4 battleships.\n \n"
                + "Use your mouse and click any square to guess what ships to sink.\n \n"
                + "The lower the number of your score and guesses the better.\n \n"
                + "Your total score is the summation of the two. Good luck!\n \n"
                + "Hint: Once you hit a ship, try to sink the ship before moving onto"
                + " any other ship.",
                "Instructions",
                JOptionPane.PLAIN_MESSAGE);
    	
    	// Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Score = 0 Guesses = 0");
        status_panel.add(status);
        
        // Info panel
        final JPanel info_panel = new JPanel();
        frame.add(info_panel, BorderLayout.WEST);
        final JLabel info = new JLabel("<html><font color='red'>Hit</font>"
        		+ "<br><font color='blue'>Miss</font><br><br><u>Ships</u>"
        		+ "<br>Carrier (5)<br>Battleship (4) <br>Cruiser (3)<br>Destroyer (2)</html>");
        info_panel.add(info);
        
        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.EAST);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        
        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        // Action listener on the exit button. When the button is pressed, the windows close
        final JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        control_panel.add(exit);
        
     // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}