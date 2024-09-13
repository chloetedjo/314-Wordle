package application;
// CSCE 314-599
// Chloe Tedjo
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Wordle extends Application {
    private static final int WORD_LENGTH = 5;
    private static final int MAX_GUESSES = 6;

    // GUI
    private GridPane gridPane;
    private TextField inputField;
    private Button guessButton;
    private Button resetButton;
    private Button statsButton;
    private Map<String, Button> keyboardKeys;
    
    // statistic variables
    private int currentGuess;
    private int gamesPlayed = 0; // track number of games played
    private int gamesWon = 0; // track number of games won
    private int currentWinStreak = 0; // current win streak
    private int maxWinStreak = 0; // maximum win streak
    private int[] guessDistribution = new int[MAX_GUESSES + 1]; // guess distribution
    
    
    private List<String> wordList;
    private String secretWord;

    @Override
    public void start(Stage stage) {
    	wordList = loadWordsFromFile("fiveLetterWords.txt"); 
    	if (wordList == null) { // if error, initialize to empty list
            wordList = new ArrayList<>();
        }
    	
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)
        ));

        setupGame(root); // initialize the game
        
        // root.getChildren();

        Scene scene = new Scene(root, 500, 600); // adjusted scene size for keyboard
        stage.setTitle("Wordle Game");
        stage.setScene(scene);
        stage.show();
    }

    private void setupGame(VBox root) {
    	// resetStats();	// testing purposes
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // input box for user input
        inputField = new TextField();
        guessButton = new Button("Guess");

        guessButton.setOnAction(e -> makeGuess());

        HBox inputBox = new HBox(10, inputField, guessButton);
        inputBox.setAlignment(Pos.CENTER);
        
        // HBox for Rules, Player Stats, and Load/Save buttons
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(20));
        
        statsButton = new Button("Player Stats");
        statsButton.setOnAction(e -> showAlert("Player Statistics", getStats()));
             
        topBar.getChildren().add(statsButton);
        
        // HBox for the Reset Game button at the bottom
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.BOTTOM_CENTER); // Align to the bottom left
        bottomBar.setPadding(new Insets(10)); // Add padding

        resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame()); // reset action
        bottomBar.getChildren().add(resetButton); // add the reset button to the HBox

              
        // create new secret word
        secretWord = getRandomWord();

        // clear previous elements
        root.getChildren().clear();

        // add the grid, input box, and keyboard to the VBox
        root.getChildren().addAll(topBar, gridPane, inputBox, createKeyboard(), bottomBar, addImageView());
    }
    
    private GridPane createKeyboard() {
        GridPane keyboard = new GridPane();
        keyboard.setAlignment(Pos.CENTER);
        keyboard.setHgap(10);
        keyboard.setVgap(10);

        keyboardKeys = new HashMap<>();

        String[] rows = {
            "QWERTYUIOP",
            "ASDFGHJKL←",	// added backspace button
            "ZXCVBNM" 
        };

        // make row constraints to ensure proper centering
        for (String rowKeys : rows) {
            int row = keyboard.getRowCount();
            char[] keys = rowKeys.toCharArray();

            // add padding columns to center the rows
            int paddingCols = (10 - keys.length) / 2;

            // add padding columns to the left
            for (int i = 0; i < paddingCols; i++) {
                keyboard.add(new Text(""), i, row);
            }

            for (int col = 0; col < keys.length; col++) {
                Button keyButton = new Button(String.valueOf(keys[col]));
                keyButton.setFont(Font.font(16));
                keyButton.setMinSize(40, 40);

                if (keys[col] == '←') {
                    keyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleBackspace);
                } else {
                    keyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleKeyboardClick);
                }
                
                keyboard.add(keyButton, col + paddingCols, row); // offset
                keyboardKeys.put(keyButton.getText(), keyButton); // store reference
            }
        }

        return keyboard;
    }

    private void handleKeyboardClick(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        String letter = clickedButton.getText();

        // append the letter to the input field, ensuring the length doesn't exceed 5
        if (inputField.getText().length() < WORD_LENGTH) {
            inputField.appendText(letter);
        }
    }

    private void handleBackspace(MouseEvent event) {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            // remove the last character from the input field
            inputField.setText(text.substring(0, text.length() - 1));
        }
    }
    
     private void makeGuess() {
        String guess = inputField.getText().toUpperCase().trim();

        // invalid word length
        if (guess.length() != WORD_LENGTH) {
            showAlert("Error", "Please enter a 5-letter word.");
            inputField.clear();
            return;
        }

        currentGuess++; // increment guess count
        
        // disable buttons for incorrect letters
        for (char letter : guess.toCharArray()) {
            if (!secretWord.contains(String.valueOf(letter))) {
                Button keyButton = keyboardKeys.get(String.valueOf(letter));
                if (keyButton != null) {
                    keyButton.setDisable(true);
                }
            }
        }

        addGuessToGrid(guess);

        if (guess.equals(secretWord)) {
            gamesWon++; // increment games won
            currentWinStreak++; // increment current win streak
            if (currentWinStreak > maxWinStreak) {
                maxWinStreak = currentWinStreak; // update max win streak
            }
            guessDistribution[currentGuess]++; // update guess distribution
            gamesPlayed++; // increment games played
            showAlert("You Win!", getStats()); // show winning stats
            resetGame(); // reset the game
        } else if (currentGuess == MAX_GUESSES) {
        	guessDistribution[currentGuess]++;
        	gamesPlayed++;
            showAlert("Game Over! The word was: " + secretWord, getStats());
            resetGame(); // reset after loss
        }

        inputField.clear();
    }

    private void addGuessToGrid(String guess) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            Text text = new Text(String.valueOf(guess.charAt(i)));
            text.setFont(Font.font(20));
            text.setFill(Color.WHITE);

            if (guess.charAt(i) == secretWord.charAt(i)) {
                text.setFill(Color.GREEN); // correct position and letter
            } else if (secretWord.contains(String.valueOf(guess.charAt(i)))) {
                text.setFill(Color.YELLOW); // correct letter, wrong position
            } else {
                text.setFill(Color.RED); // incorrect letter
            }

            gridPane.add(text, i, currentGuess - 1); // add to grid
        }
    }

    private void resetGame() {
    	currentGuess = 0;
    	setupGame((VBox) gridPane.getParent());
    }

    // pop up messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getStats() {
        double winRate = (gamesWon / (double) gamesPlayed) * 100;
        StringBuilder stats = new StringBuilder();
        stats.append("Games Played: ").append(gamesPlayed).append("\n");
        stats.append("Win Rate: ").append(String.format("%.2f%%", winRate)).append("\n");
        stats.append("Current Win Streak: ").append(currentWinStreak).append("\n");
        stats.append("Max Win Streak: ").append(maxWinStreak).append("\n");
        stats.append("Guess Distribution:\n");
        for (int i = 1; i <= MAX_GUESSES; i++) {
            stats.append("Guess ").append(i).append(": ").append(guessDistribution[i]).append("\n");
        }
        return stats.toString();
    }
    
    // for testing purposes
    @SuppressWarnings("unused")	
	private void resetStats() {
    	currentGuess = 0;
        gamesPlayed = 0;
        gamesWon = 0;
        currentWinStreak = 0;
        maxWinStreak = 0;
        guessDistribution = new int[MAX_GUESSES + 1];
    }
    
    private List<String> loadWordsFromFile(String filename) {
    	// System.out.println(System.getProperty("user.dir"));		// testing C:\eclipse-workspace\testSB
        List<String> words = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/"+filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // split line into words and add only those with correct length
                String[] lineWords = line.split("\\s+"); // split by whitespace
                for (String word : lineWords) {
                    word = word.trim().toUpperCase(); // trim and convert to uppercase
                    if (word.length() == WORD_LENGTH) { // check valid length
                        words.add(word);	// add if valid word
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace(); // Log error details
            showAlert("Error", "Unable to load words from file: " + e.getMessage());
        }
        return words;
    }
    
    private String getRandomWord() {
    	
    	if (wordList == null || wordList.isEmpty()) {
            return "ERROR"; // 
        }
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size())); // random word
    	
        // return "AGGIE";	// testing
    }
    
    private ImageView addImageView() {
    	// System.out.println(System.getProperty("user.dir"));		// testing C:\eclipse-workspace\testSB
        ImageView imageView = null;
        try {
            // load the image
            Image image = new Image("file:resources/rev1.jpg"); // replace with your image file path
            imageView = new ImageView(image);
            imageView.setFitHeight(150); // set height
            imageView.setPreserveRatio(true); // maintain pic ratio
        }
        catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Unable to load image: " + e.getMessage());
        }
        return imageView; // return the image view
    }
    
    // main
    public static void main(String[] args) {
        launch(args);
    }
}
