=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Appropriately modeling state using 2-D arrays 
  Battleship consists of a grid/board. My implementation of Battleship uses a 
  8x8 board, and I'm using 2D arrays to represent this. I have two 2D-arrays:
  one for the board and the other for the buttons. The int[][] board is the
  "hidden" grid that stores the state of the tile: if it's been hit (-1), if it's
  empty (0), or if it has a ship (1). The JButton[][] button is what is on display 
  in the game. When a player clicks a white button (a button that has not been 
  hit), it checks if the board is empty or if it has a ship. If it's empty, the 
  button becomes blue, else the button becomes red. The board tile for that 
  button changes to -1 to represent that it has been hit. When the game starts or
  resets, all squares on the board become 0 and all buttons turn white,
  representing a blank board.

  2. Appropriately modeling state using collections
  The final game score of a player is the sum of two parts.
  Part 1: Number of total guesses, which represents how quickly the user can sink
  all of the ships.
  Part 2: Sum of all hit scores. Each hit score is the time step difference of
  this hit and the last hit to the same ship. This represents how quickly he/she 
  can hit the same ship when the first hit of this ship is started. The quicker the 
  player can guess the orientation of this ship is better.
   
  Since the scoring of Part 2 for each hit of a ship body depends on when this ship 
  was hit last time, my scoring method uses Collections (TreeSet) to maintain the 
  hit states of all ships in the game and maintain the last hit time step of each 
  ship. Based off of that I can differentiate the ships which are hit in different 
  time steps when the hit score is computed. The Part 2 hit score is computed 
  incrementally to reflect how quickly a player can predict the placement of a ship. 
   
  The class called ShipStates keeps track the state of when each ship was hit last time, 
  and accumulates points for each ship when it is clicked again by a player. For 
  example, if two ships are adjacent and I hit one and in the next guess, I hit the 
  other, the score is different from hitting the same ship twice because I did not 
  correctly guess where the ship I first hit is, and out of luck, the 2nd ship happened 
  to be where the other ship is.
  
  3. File I/O: using I/O to parse a novel file formats
  My Battleship implementation will use File I/O to enable persistent state between 
  runs of my game. My game reads all scores out to a file and write them back with the 
  new game score when a new game ends. The game scores are displayed in ascending order 
  as the lowest number of guesses + hit score has the highest score. The game only displays
  the best five game scores. The format of a score file is a sequence of lines and each 
  line contains a player name and the game score. My file I/O error handling considers if
  the score file does not exist, the directory in this file does not exist, the file format 
  error, or the end-of-file detection. A player is asked to enter the name so the program can
  store the name and game score to the file. It trims the unnecessary white space when 
  entering a name string, and replace the white space in the middle with a hyphen.
  

  
  4. Using JUnit on a testable component of your game
  Two major components of my game are 
  1) Highscore which involve file I/O of player names and scores, and their sorting.  
  2) ShipStates class which tracks the hit state of each ship.
  I used two JUnit test files to test the public methods of Highscore and ShipStates separately. 
  My game GUI class is Game.java and GameCourt.java which runs through GUI, and I test them 
  through GUI manually. 
  For ShipStates, I test to make sure that the total hit score update correctly as you hit a ship. 
  One edge case I test is where you hit different ships in a row and make sure the score still 
  updates correctly. In addition, these tests are test that the ships are aligned correctly when 
  placed, since as you hit them, it has to be in the given orientation and doesn't "curve" or 
  "split". 
  For HighscoreTest, I test if game scores are sorted properly, and if player names are formatted 
  properly, and also consider several edge cases of File I/O. One edge case I test is if you pass 
  in a file that doesn't exist (result: creates a file of that name with the  new score in it). 
  I also consider a score file may be empty, and the user names input are not well formatted.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  I used MushroomOfDoom to help start my good. Game.java and GameCourt.java is very
  similar to MushroomOfDoom.
  
  Game.java: The file contains a class with a simple main method to run the game.
  First, the instructions pane appears and then once you exit out of that pane (hit
  OK button or the exit button), the actual game frame will appear. The game has
  Reset and Exit buttons, an info panel that displays the four types of ships and 
  instructs what color means what, a Status line which tells the user the score and 
  guesses, and the GameCourt.
  
  GameCourt.java: This is where the real action happens. The GameCourt is a 
  JComponent on which the current game is displayed. It consists of JButtons so
  the game handles mouse clicks. Once the game is over, a JOptionPane appears 
  asking the user to type its name and then it displays the top 5 scores. 
  
  Highscore.java: This class opens and writes the final scores onto a file. It sorts
  and updates the scores. It saves the scores in an ArrayList for GameCourt to display
  the best games (e.g. top 5). This class has one inner class Player, which stores the
  game score and name of this player for the current game or a past game. With such 
  objects, I use the ArrayList to save a set of games played  and then call the sorting 
  library to sort. To utilize the collection sorting method, I define class SortByScore 
  which implements Comparator<Player>.

  ShipStates.java: This class stores the state of the ships. It computes the score 
  based on how fast you are able to guess where this ship is after the first discovery. 
  The first time of the ship you hit is 1 point, and then every time there is a hit
  to this ship, the points you get is the difference between this time step and last 
  time of hit to the same ship. The best is to predict the orientation of this ship 
  wisely and hit the same ship immediately in the next few consecutive guess steps. 
  Otherwise a large time gap from last hit and the next hit to the same ship results in 
  a high penalty.
  In this ShipStates class, I also use Ship inner class so that the collection data 
  structure can use TreeSet to maintain a set of ships. Ships are comparable under TreeSet
  and thus I defined the compareTo method.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  It was difficult to start my game since it was a little confusing on how to start.
  However once I started to use the base code of MushroomOfDoom, it got easier and
  I understood it a lot better.
  Once I finished implementing my game, I realized I didn't have Collections
  implemented since I changed my implementation of my game slightly. It took me
  a while to change the game scoring scheme that requires state-driven game playing, 
  to figure out a solution and to update all of my code to factor in the core concept.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I think my design is encapsulated pretty well. A lot of my code is private and 
  the only public methods are the ones that need to be used by other classes. In
  addition, all of my variables are private and I use the getter and setter methods
  to access the variables that I need to get in the game or tests. I think there's 
  a good separation of my functionalities. GameCourt consists of the main playing 
  field, ShipStates helps calculates the score and and Highscore uses File I/O to
  maintain the sorted game scores. The other class methods cannot directly access the 
  states and fields of an object unless using the provided public methods.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  https://www.javacodex.com/More-Examples/2/12
  https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
  https://docs.oracle.com/javase/tutorial/uiswing/components/button.html#abstractbutton
  https://docs.oracle.com/javase/tutorial/uiswing/components/button.html#abstractbutton
  