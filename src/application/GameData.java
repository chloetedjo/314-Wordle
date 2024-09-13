package application;

import java.io.*;
import java.util.*;
/*
* this was an attempt at load save
* i didnt finish it, disregard this
*/
public class GameData {
    private static final int MAX_GUESSES = 6;

    // Player statistics
    private int currentGuess;
    private int gamesPlayed = 0; // Track number of games played
    private int gamesWon = 0; // Track number of games won
    private int currentWinStreak = 0; // Current win streak
    private int maxWinStreak = 0; // Maximum win streak
    private int[] guessDistribution = new int[MAX_GUESSES + 1]; // Guess distribution

    // Backup saved statistic variables
    private int currentGuessSaved;
    private int gamesPlayedSaved = 0; // Track number of games played
    private int gamesWonSaved = 0; // Track number of games won
    private int currentWinStreakSaved = 0; // Current win streak
    private int maxWinStreakSaved = 0; // Maximum win streak
    private int[] guessDistributionSaved = new int[MAX_GUESSES + 1]; // Guess distribution

    // List to store guesses
    private List<String> guesses = new ArrayList<>();
    private String secretWord;

    public GameData() {
        // Initialize lists and arrays to avoid null references
        this.guessDistribution = new int[MAX_GUESSES + 1];
        this.guessDistributionSaved = new int[MAX_GUESSES + 1];
        this.guesses = new ArrayList<>();
    }
   
    // Save game method
    public void saveGame(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this); // Write the entire GameData object to file
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Load game method
    public static GameData loadGame(String fileName) {
        GameData gameData = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            gameData = (GameData) in.readObject(); // Read the GameData object from file
            System.out.println("Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
        return gameData;
    }
    
    
    // Getters and Setters for Player Statistics
    public int getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(int currentGuess) {
        this.currentGuess = currentGuess;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getCurrentWinStreak() {
        return currentWinStreak;
    }

    public void setCurrentWinStreak(int currentWinStreak) {
        this.currentWinStreak = currentWinStreak;
    }

    public int getMaxWinStreak() {
        return maxWinStreak;
    }

    public void setMaxWinStreak(int maxWinStreak) {
        this.maxWinStreak = maxWinStreak;
    }

    public int[] getGuessDistribution() {
        return guessDistribution;
    }

    public void setGuessDistribution(int[] guessDistribution) {
        this.guessDistribution = guessDistribution;
    }

    // Getters and Setters for Backup Saved Statistics
    public int getCurrentGuessSaved() {
        return currentGuessSaved;
    }

    public void setCurrentGuessSaved(int currentGuessSaved) {
        this.currentGuessSaved = currentGuessSaved;
    }

    public int getGamesPlayedSaved() {
        return gamesPlayedSaved;
    }

    public void setGamesPlayedSaved(int gamesPlayedSaved) {
        this.gamesPlayedSaved = gamesPlayedSaved;
    }

    public int getGamesWonSaved() {
        return gamesWonSaved;
    }

    public void setGamesWonSaved(int gamesWonSaved) {
        this.gamesWonSaved = gamesWonSaved;
    }

    public int getCurrentWinStreakSaved() {
        return currentWinStreakSaved;
    }

    public void setCurrentWinStreakSaved(int currentWinStreakSaved) {
        this.currentWinStreakSaved = currentWinStreakSaved;
    }

    public int getMaxWinStreakSaved() {
        return maxWinStreakSaved;
    }

    public void setMaxWinStreakSaved(int maxWinStreakSaved) {
        this.maxWinStreakSaved = maxWinStreakSaved;
    }

    public int[] getGuessDistributionSaved() {
        return guessDistributionSaved;
    }

    public void setGuessDistributionSaved(int[] guessDistributionSaved) {
        this.guessDistributionSaved = guessDistributionSaved;
    }

    // Getters and Setters for Guesses List
    public List<String> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<String> guesses) {
        this.guesses = guesses;
    }
    
    // SecretWord
    public String getSecretWord() {
    	return secretWord;
    }
    
    public void setSecretWord(String secretWord) {
    	this.secretWord = secretWord;
    }
}