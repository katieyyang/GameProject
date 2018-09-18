import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Highscore {
	private String scoreFileName;
    private List<Player> playerList = new ArrayList<Player>();
	private final int TOP = 5;
	
	/**
	 * Constructor
	 * @param score - int score of game
	 * @param pname - name of player
	 * @param fileName - name of file
	 */
	public Highscore (int score, String playerName, String fileName) {
		scoreFileName = fileName;
		String name;
		playerName = playerName.trim();
		
		if(playerName.contains(" ")) {
			name = playerName.replace(' ', '-');
		} else {
			name = playerName;
		}
		
        playerList.add(new Player(name, score));
		try {
			File file = new File(scoreFileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// get a list of  name and score
			String line = reader.readLine();
			while (line != null) {
				String[] l = line.split("\\s+"); //skip one or more space
				String p = l[0];
				int s = Integer.parseInt(l[1]);
        		playerList.add(new Player(p, s));
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException f) {
			System.out.println("Score file "+ scoreFileName + " is not found. Create one");
		} catch (IOException e) {
			System.out.println("File format error in score file "+ scoreFileName +". Overwrite it");
		}
	
		// sort players and scores
		Collections.sort(playerList, new SortByScore()); 
		outputScore();
	}
	
	/**
	 * Output the updated high scores scores back to the file
	 */
	private void outputScore() {	
		try {
			File file = new File(scoreFileName);
			if (!file.exists()) {
				File folder = new File(file.getParentFile().getAbsolutePath());
				folder.mkdirs();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < playerList.size(); i++) {
				bw.write(playerList.get(i).name + " " + playerList.get(i).score);
				bw.newLine();
			}
			bw.close();
			fw.close();
			
		} catch (IOException e) {
			System.out.println("Output to score file " + scoreFileName + " failed.");
		}
	}
	
	/**
	 *  Get an array of scores
	 *  @return scores int array
	 */
	public int[] getScores() {
		int[] scores = new int[playerList.size()];
		
		for (int i = 0; i < playerList.size(); i++){
			scores[i] = playerList.get(i).score;
		}
		return scores;
	}
	/**
	 * Return an array of player's names
	 * @return players String array
	 */
	public String[] getPlayers() {
		String[]  players = new String[playerList.size()];
    	
		for (int i = 0; i < playerList.size(); i++){
			players[i] = playerList.get(i).name;
		}
		return players;
	}
	
	/**
	 * Return the top K number of scores to control what to display
	 * @return TOP number of top scores
	 */
	public int getTopKNumber(){
		return TOP;
	}
	
	// A class to represent a player.
	private class Player {
		private int score;
	    private String name;

	    // Constructor
	    public Player(String name, int s) {
	        this.score = s;
	        this.name = name;
	    }
	}

	/** 
	 * Used for sorting in ascending order of score
	 */
	private class SortByScore implements Comparator<Player> {
	    // Used for sorting in ascending order of score 
	    public int compare(Player a, Player b) {
	        return a.score - b.score;
	    }
	}
}
