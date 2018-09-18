import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.*;

public class HighscoreTest {
	private void outputScore(int[] score, String[] player, String filename) {	
		try {
			File file = new File(filename);
			if (!file.exists()) {
				File folder = new File(file.getParentFile().getAbsolutePath());
				folder.mkdirs();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < player.length; i++) {
				bw.write(player[i] + " " + score[i]);
				bw.newLine();
			}
			bw.close();
			fw.close();
			
		} catch (IOException e) {
			System.out.println("Output to the test score file failed.");
		}
	}
	
	/**
	 * Test handling of an empty scoring file
	 */
	@Test
	public void testEmptyFile() {
		int[] score0 = {};
		String[] player0 = {};
		String fname = "files/highscoreTest.txt";
		outputScore(score0, player0, fname);
		Highscore highscores = new Highscore(4, "K4", fname);
		int[] score = {4};
		String[] player = {"K4"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
	}
	
	/**
	 * Test the handling of an input name with space in the middle.
	 */
	@Test
	public void testName2Words() {
		int[] score0 = {};
		String[] player0 = {};
		String fname="files/highscoreTest.txt";
		outputScore(score0, player0, fname);
		Highscore highscores = new Highscore(4, "Katie Y", fname);
		int[] score = {4};
		String[] player = {"Katie-Y"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
	}
	
	/**
	 * Test when adding a new game/score to a score file with 2 games/scores
	 * Also a player name has white space in the leading and trailing positions.
	 */
	@Test
	public void testHighscore2Players() {
		int[] score0 = {2, 1};
		String[] player0 = {"K2", "K1"};
		String fname="files/highscoreTest.txt";
		outputScore(score0, player0, fname);
		Highscore highscores = new Highscore(4, " K 4 ", fname);
		int[] score = {1, 2, 4};
		String[] player = {"K1", "K2", "K-4"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
		assertEquals(5, highscores.getTopKNumber());
	}
	
	/**
	 * Test when you add a score and the file you want doesn't exist
	 */
	@Test
	public void testFileNotFound() {
		String fname ="files/highscoreTest2.txt";
		File file = new File(fname);
		if (file.exists()) {
				file.delete();		
		} 
		Highscore highscores = new Highscore(4, "K4", fname);
		int[] score = {4 };
		String[] player = {"K4"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
	}
	
	
	/**
	 * Test when adding a new score to a score file with 3 players
	 */
	@Test
	public void testHighscore4Scores() {
		int[] score0 = {2, 1, 5 };
		String[] player0 = {"K2", "K1", "K5"};
		String fname="files/highscoreTest.txt";
		outputScore(score0, player0, fname);
		Highscore highscores = new Highscore(4, "K4 extra", fname);
		int[] score = {1, 2, 4, 5};
		String[] player = {"K1", "K2", "K4-extra", "K5"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
	}
	
	/**
	 * Test when adding a new score to a score file with 5 players
	 */
	@Test
	public void testHighscore6Scores() {
		int[] score0 = {2, 1, 5, 3, 6};
		String[] player0 = {"K2", "K1", "K5", "K3", "K6"};
		String fname="files/highscoreTest3.txt";
		outputScore(score0, player0, fname);
		Highscore highscores = new Highscore(4, "K5", fname);
		int[] score = {1, 2, 3, 4, 5, 6};
		String[] player = {"K1", "K2", "K3", "K5", "K5", "K6"};
		assertArrayEquals(score, highscores.getScores());
		assertArrayEquals(player, highscores.getPlayers());
	}
}