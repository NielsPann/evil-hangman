package com.example.niels.hangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Niels Pannekeet 11035668
 * MainActivity handles everything on the main screen. It decides which classes and activity's
 * should be called depending on the settings, progress of the game, and user interactions.
 */
public class MainActivity extends AppCompatActivity {

    public static TextView resultWordText, guessesIndicatorText, scoreValText, guessedLettersText;
    public static int guessStartVal, scoreVal, wordLength, guessVal, gallowState;
    public static String startText, guessingWord;
    private boolean evilGame;
    private List<CharSequence> alreadyPressedLetters;
    private StringBuilder newGuessBuilder;
    private String compareWord;
    private List<String> evilWordList;
    private ImageView gallowImage;
    private EditText hiddenInputField;
    private String[] wordArray;
    private boolean newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGame = false;

        // initiate interface items
        resultWordText = (TextView) findViewById(R.id.wordResultTextView);
        gallowImage = (ImageView) findViewById(R.id.gallowImageView);
        guessesIndicatorText = (TextView)findViewById(R.id.amountOfGuessesTextView);
        scoreValText = (TextView)findViewById(R.id.scoreValTextView);
        guessedLettersText = (TextView)findViewById(R.id.guessedLettersTextView);

        // get shared preferences
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        guessStartVal = settings.getInt("guesses", 10);
        wordLength = settings.getInt("wordLength", 4);
        evilGame = settings.getBoolean("evilGame", true);

        // initiate list to keep track of already pressed letters
        alreadyPressedLetters = new ArrayList<CharSequence>();

        // initiate first appearance of the word to be guessed
        startText = buildIndicatorString(wordLength);
        resultWordText.setText(startText);

        // display current amount of guesses.
        guessVal = guessStartVal;
        guessesIndicatorText.setText(String.valueOf(guessVal));

        // sets score and scoremultipliers to 0 on create
        scoreVal = 0;
        gallowState = 0;
        GoodGameplay.scoreMultiplier = 0;

        // get word list from resource
        wordArray = getResources().getStringArray(R.array.words);

        // call the good or evil classes depending on the game mode
        if (evilGame) {
            evilWordList = EvilGameplay.fillListRightLength(wordArray);
        }
        else {
            guessingWord = GoodGameplay.pickWord(wordArray).toLowerCase();
        }

        // initiate a hidden EditText for handling direct user input
        hiddenInputField = (EditText)findViewById(R.id.inputFieldEditText);
        hiddenInputField.requestFocus();
        hiddenInputField.setBackgroundResource(android.R.color.transparent);
        hiddenTextEditListener();
    }

    /**
     * Builds the text that is displayed at the start of the game containing only "_" characters.
     */
    private String buildIndicatorString(int wordLength){
        StringBuilder builder = new StringBuilder();
        while (wordLength > 0) {
            builder.append('_');
            wordLength--;
        }
        return builder.toString();
    }

    /**
     * Listens to a change in the hidden EditText field and empties the text field after handling
     * the input with the good or evil gameplay classes.
     */
    private void hiddenTextEditListener() {
        hiddenInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    Character inputChar = s.charAt(0);

                    // if uppercase is pressed, make it lowercase.
                    if (Character.isUpperCase(inputChar))
                        inputChar = Character.toLowerCase(inputChar);

                    // if character is a symbol it will not be handled
                    if (!Character.isAlphabetic(inputChar)) {
                        makeSymbolStatement();
                        hiddenInputField.setText(null);
                        return;
                    }

                    // looks if character is already pressed once, if so, it will not be handled
                    for (int i = 0; i < alreadyPressedLetters.size(); i++) {
                        if (alreadyPressedLetters.get(i).charAt(0) == inputChar) {
                            System.out.println(alreadyPressedLetters);
                            System.out.println(inputChar);
                            makeAlreadyChosenStatement();
                            hiddenInputField.setText(null);
                            return;
                        }
                    }
                    if (evilGame) {
                        //set the word list so that it avoids the users guess
                        evilWordList = EvilGameplay.handleInputEvilMode(inputChar, evilWordList);
                        System.out.println(evilWordList);
                        // check whether the word still contains underscores, if not, its is a win
                        String compareWord = resultWordText.getText().toString();
                        int underScoreCount = 0;
                        for(int i = 0; i < compareWord.length(); i++) {
                            if(compareWord.charAt(i) == '_') {
                                underScoreCount++;
                            }
                        }
                        if(underScoreCount == 0) {
                            winOrLose(true);
                        }
                    }
                    else {
                        // get revealed letters if chosen correctly
                        newGuessBuilder = GoodGameplay.HandleInputGoodMode(inputChar);
                        // check if the textView equals the guessing word, if so, it is a win
                        compareWord = newGuessBuilder.toString();
                        if (compareWord.equals(guessingWord)){

                            winOrLose(true);
                        }
                    }

                    // remove letter that has been added to the EditText after handling it
                    hiddenInputField.setText(null);

                    // add pressed letter to list to check for same input.
                    alreadyPressedLetters.add(inputChar.toString());
                    guessedLettersText.setText(alreadyPressedLetters.toString());

                    updateGallow();

                    // set the guess counter to the current state
                    guessesIndicatorText.setText(String.valueOf(guessVal));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void makeSymbolStatement() {
        Toast.makeText(this, "please give a character!", Toast.LENGTH_SHORT).show();
    }

    private void makeAlreadyChosenStatement() {
        Toast.makeText(this, "letter is already pressed!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the Gallow image to display the progress of the failed guesses.
     * The progress depends on the percentage of failed guesses from possible guesses.
     */
    public void updateGallow() {
        int gallowPercentage = (gallowState * 100 / guessStartVal);

        if (gallowPercentage >= 10) {
            gallowImage.setImageResource(R.drawable.state2);
        }
        if (gallowPercentage >= 20) {
            gallowImage.setImageResource(R.drawable.state3);
        }
        if (gallowPercentage >= 30) {
            gallowImage.setImageResource(R.drawable.state4);
        }
        if (gallowPercentage >= 40) {
            gallowImage.setImageResource(R.drawable.state5);
        }
        if (gallowPercentage >= 50) {
            gallowImage.setImageResource(R.drawable.state6);
        }
        if (gallowPercentage >= 60) {
            gallowImage.setImageResource(R.drawable.state7);
        }
        if (gallowPercentage >= 70) {
            gallowImage.setImageResource(R.drawable.state8);
        }
        if (gallowPercentage >= 80) {
            gallowImage.setImageResource(R.drawable.state9);
        }
        if (gallowPercentage >= 90) {
            gallowImage.setImageResource(R.drawable.state10);
        }
        if (gallowPercentage >= 100) {
            gallowImage.setImageResource(R.drawable.state11);

            // Give user a lose dialog.
            winOrLose(false);
        }
    }

    /**
     * Alerts the user with a dialog after winning or losing, giving options to continue or to
     * save the score.
     */
    public void winOrLose(boolean win) {
        if (win) {
            AlertDialog.Builder winAlertBuilder = new AlertDialog.Builder(this);
            winAlertBuilder.setMessage("You won! your score is " + String.valueOf(scoreVal));
            winAlertBuilder.setTitle("Congratulations!");

            // sets an extra button to save the users score
            winAlertBuilder.setNegativeButton("Save score", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    newGame = true;
                    goToHighscore();
                }
            });

            // sets an ok button that starts a new game without saving the score
            winAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    newGame = true;
                    recreate();
                }
            });
            AlertDialog winAlertDialog = winAlertBuilder.create();
            winAlertDialog.show();
        }
        else {
            AlertDialog.Builder loseAlertBuilder = new AlertDialog.Builder(this);
            loseAlertBuilder.setMessage("You lost!");
            loseAlertBuilder.setTitle("aww");

            // sets an ok button that starts a new game
            loseAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    newGame = true;
                    recreate();
                }
            });
            AlertDialog loseAlertDialog = loseAlertBuilder.create();
            loseAlertDialog.show();
        }
    }

    /**
     * Passes current game progress to high score activity, and starts a new game.
     */
    public void goToHighscore () {
        Intent HighScoreIntent = new Intent(this, HighScore.class);
        HighScoreIntent.putExtra("scoreVal",scoreVal);
        HighScoreIntent.putExtra("gameMode", evilGame);
        startActivity(HighScoreIntent);
        recreate();
        Toast.makeText(this, "Score has been saved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initiates the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handles the selection of different menu options.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        // Go to settings if settings menuItem is clicked.
        if (id == R.id.SettingsMenuOption) {
            Intent GetSettingsIntent = new Intent(this, Settings.class);
            startActivity(GetSettingsIntent);
            return true;
        }

        // Go to high scores if High Score menu option is clicked. Pass score value of 0 to indicate
        // that nothing needs to be added to the high score list.
        else if (id == R.id.HighscoreMenuOption) {
            Intent HighScoreIntent = new Intent(this, HighScore.class);
            HighScoreIntent.putExtra("scoreVal",0);
            startActivity(HighScoreIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Restores the game instances after it was destroyed, particularly when the phone is rotated.
     * This method will activate after onCreate is finished.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // the instances should only be restored if there is a game running, not when a game
        // is finished. newGame will always be false, unless the user starts a new game
        newGame = savedInstanceState.getBoolean("newGame");
        if (!newGame) {
            evilGame = savedInstanceState.getBoolean("gameMode");
            gallowState = savedInstanceState.getInt("gallowState");
            scoreVal = savedInstanceState.getInt("Score");
            guessVal = savedInstanceState.getInt("guessVal");
            guessStartVal = savedInstanceState.getInt("guessStartVal");
            wordLength = savedInstanceState.getInt("wordLength");
            alreadyPressedLetters = savedInstanceState.getCharSequenceArrayList("alreadyPressed");
            guessingWord = savedInstanceState.getString("guessingWord");
            if (alreadyPressedLetters != null) {
                guessedLettersText.setText(alreadyPressedLetters.toString());
            }
            evilWordList = savedInstanceState.getStringArrayList("EvilList");
            resultWordText.setText(savedInstanceState.getString("resultWordText"));
            guessesIndicatorText.setText(String.valueOf(guessVal));
            updateGallow();
            scoreValText.setText(String.valueOf(scoreVal));
        }
        newGame = false;
    }

    /**
     * Saves all necessarily instances when the app is destroyed, particularly when the phone is
     * rotated.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("newGame", newGame);
        outState.putInt("gallowState", gallowState);
        outState.putBoolean("gameMode", evilGame);
        outState.putInt("Score", scoreVal);
        outState.putInt("guessVal", guessVal);
        outState.putInt("guessStartVal", guessStartVal);
        outState.putInt("wordLength", wordLength);
        outState.putCharSequenceArrayList("alreadyPressed",
                (ArrayList<CharSequence>) alreadyPressedLetters);
        outState.putStringArrayList("EvilList", (ArrayList<String>) evilWordList);
        outState.putString("resultWordText", resultWordText.getText().toString());
        outState.putString("guessingWord", guessingWord);
    }
}

