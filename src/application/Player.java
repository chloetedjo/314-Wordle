package application;
// did not use this
public class Player {
    // Player statistics
    private int currentGuess = 0; // Current guess count for the game
    private int gamesPlayed = 0; // Number of games played
    private int gamesWon = 0; // Number of games won
    private int currentWinStreak = 0; // Current win streak
    private int maxWinStreak = 0; // Maximum win streak
    private int[] guessDistribution; // Distribution of guesses

    public Player() {
        this.guessDistribution = new int[7]; // Initialize guess distribution
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
}
