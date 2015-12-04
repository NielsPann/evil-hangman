package com.example.niels.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.niels.hangman.R.xml.*;

public class MainActivity extends AppCompatActivity {

    public static TextView resultWord, guessesIndicatorText, scoreValText, guessedLetters;
    public static int amountOfGuesses, guessIndicator, scoreVal, wordLength;
    public static String startText, guessingWord;
    public static boolean EvilGame = true;
    private List<Character> alreadyPressedLetters;
    private StringBuilder newGuessBuilder;
    private static String compareWord;
    private List<String> evilWordList;
    private ImageView gallowImage;
    private EditText inputLetter;
    private String[] wordArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define interface views
        resultWord = (TextView) findViewById(R.id.wordResult);
        gallowImage = (ImageView) findViewById(R.id.imageView);
        guessesIndicatorText = (TextView)findViewById(R.id.guessesNumberText);
        scoreValText = (TextView)findViewById(R.id.scoreValText);
        guessedLetters = (TextView)findViewById(R.id.guessedLetters);

        // get shared prefrences
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        amountOfGuesses = settings.getInt("guesses", 10);
        wordLength = settings.getInt("wordLength", 4);
        EvilGame = settings.getBoolean("EvilGame", true);

        // initiate list to keep track of already pressed letters
        alreadyPressedLetters = new ArrayList<Character>();

        // initiate first appearance of the word to be guessed
        startText = buildIndicatorString(wordLength);
        resultWord.setText(startText);

        // display current amount of guesses.
        guessIndicator = amountOfGuesses;
        guessesIndicatorText.setText(String.valueOf(guessIndicator));

        // sets score and scoremultipliers to 0 on create
        scoreVal = 0;
        GoodGameplay.scoreMultiplier = 0;

        // get word list from resource
        wordArray = getResources().getStringArray(R.array.words);

        // call the good or evil classes depending on the game mode
        if (EvilGame) {
            evilWordList = EvilGameplay.fillListRightLength(wordArray);
        }
        else {
            guessingWord = GoodGameplay.pickWord(wordArray).toLowerCase();
        }

        //Toast.makeText(this, guessingWord, Toast.LENGTH_SHORT).show();

        // initiate a hidden EditText for handling direct user input
        inputLetter = (EditText)findViewById(R.id.letterInputField);
        inputLetter.requestFocus();
        inputLetter.setBackgroundResource(android.R.color.transparent);
        hiddenTextEditListener();
    }

    // builds the text that is displayed at the start of the game containing only "_" characters
    private String buildIndicatorString(int wordLength){
        StringBuilder builder = new StringBuilder();
        while (wordLength > 0) {
            builder.append('_');
            wordLength--;
        }
        return builder.toString();
    }

    // Listens to a change in the hidden EditText field and empties the text field after handling
    // the input with the good or evil gameplay classes
    private void hiddenTextEditListener() {
        inputLetter.addTextChangedListener(new TextWatcher() {
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
                        inputLetter.setText(null);
                        return;
                    }
                    // looks if character is already pressed once, if so, it will not be handled
                    for (int i = 0; i < alreadyPressedLetters.size(); i++) {
                        if (alreadyPressedLetters.get(i) == inputChar) {
                            System.out.println(alreadyPressedLetters);
                            System.out.println(inputChar);
                            makeAlreadyChosenStatement();
                            inputLetter.setText(null);
                            return;
                        }
                    }
                    if (EvilGame) {
                        //set the word list so that it avoids the users guess
                        evilWordList = EvilGameplay.handleInputEvilMode(inputChar, evilWordList);
                        System.out.println(evilWordList);
                        // check whether the word still contains underscores, if not, its is a win
                        String compareWord = resultWord.getText().toString();
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
                            GoodGameplay.OnRecreate();
                            winOrLose(true);
                        }
                    }
                    // remove letter that has been added to the EditText after handling it
                    inputLetter.setText(null);

                    // add pressed letter to list to check for same input.
                    alreadyPressedLetters.add(inputChar);
                    guessedLetters.setText(alreadyPressedLetters.toString());

                    updateGallow();
                    // set the guess counter to the current state
                    guessesIndicatorText.setText(String.valueOf(guessIndicator));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // updates the Gallow image to display the progress of the failed guesses
    // The progress depends on the percentage of failed guesses from possible guesses.
    public void updateGallow() {
        int gallowState;
        if(EvilGame){
            gallowState = EvilGameplay.getGallowState();
        }
        else {
            gallowState = GoodGameplay.getGallowState();
        }

        int gallowPercentage = (gallowState * 100 / amountOfGuesses);

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
            // reset gallowstate back to 0 after a loss
            GoodGameplay.OnRecreate();
            EvilGameplay.OnRecreate();
            winOrLose(false);
        }
    }

    // alerts the user with a dialog after winning or losing, giving options to continue or to
    // save the score
    public void winOrLose(boolean win) {
        if (win) {
            AlertDialog.Builder winAlertBuilder = new AlertDialog.Builder(this);
            winAlertBuilder.setMessage("You won! your scrore is " + String.valueOf(scoreVal));
            winAlertBuilder.setTitle("Congatulations!");
            winAlertBuilder.setNegativeButton("Save Score", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    goToHighscore();
                }
            });
            winAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
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
            loseAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    recreate();
                }
            });
            AlertDialog loseAlertDialog = loseAlertBuilder.create();
            loseAlertDialog.show();
        }
    }
    public void goToHighscore () {
        Intent HighScoreIntent = new Intent(this, HighScore.class);
        HighScoreIntent.putExtra("scoreVal",scoreVal);
        HighScoreIntent.putExtra("gameMode", EvilGame);
        startActivity(HighScoreIntent);
        recreate();
        Toast.makeText(this, "Score has been saved!", Toast.LENGTH_SHORT).show();

    }

    // initiates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // handles the selection of different menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.SettingsMenuOption) {
            Intent GetSettingsIntent = new Intent(this, Settings.class);
            startActivity(GetSettingsIntent);
            return true;
        }
        else if (id == R.id.HighscoreMenuOption) {
            Intent HighScoreIntent = new Intent(this, HighScore.class);
            HighScoreIntent.putExtra("scoreVal",0);
            startActivity(HighScoreIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void makeSymbolStatement() {
        Toast.makeText(this, "please give a character!", Toast.LENGTH_SHORT).show();
    }
    private void makeAlreadyChosenStatement() {
        Toast.makeText(this, "letter is already pressed!", Toast.LENGTH_SHORT).show();
    }
}

