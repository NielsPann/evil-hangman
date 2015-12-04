package com.example.niels.hangman;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.jar.Manifest;

/**
 * Created by Niels on 26-11-2015.
 */
public class GoodGameplay extends MainActivity{

    // initiate variables
    private static int gallowState = 0;
    public static int scoreMultiplier = 0;

    // picks a random word out of the given string array.
    public static String pickWord(String[] wordArray) {

        // fills list with words of chosen length.
        List<String> possibleWords = new ArrayList<String>();
        for (String aWordArray : wordArray) {
            if (aWordArray.length() == wordLength) {
                possibleWords.add(aWordArray);
            }
        }
        // gets a random number in range of length of the word list.
        Random rand = new Random();
        int wordPicker = rand.nextInt(possibleWords.size());

        return possibleWords.get(wordPicker);
    }

    // check the secret word on the given letter. reveals the letter if guessed right.
    public static StringBuilder HandleInputGoodMode(char ButtonClick) {
        // creates a stringbuilder from the already displayed textfield.
        String currentGuess = resultWord.getText().toString();
        StringBuilder newGuessBuilder = new StringBuilder(currentGuess);

        // reveals characters in the stringbuilder if they are found in the guessing word.
        for (int i = 0; i < guessingWord.length(); i++) {
            char character = guessingWord.charAt(i);

            if (ButtonClick == character) {
                newGuessBuilder.setCharAt(i, ButtonClick);
            }
        }
        // updates the textview with right guesses.
        resultWord.setText(newGuessBuilder);

        // checks if the textview has been changed, and acts accordingly.
        String CompareWord = resultWord.getText().toString();
        if (currentGuess.equals(CompareWord)) {
            gallowState++;
            guessIndicator--;
            scoreMultiplier = 0;
        }
        else {
            scoreMultiplier++;
            Score.setScore(ButtonClick, CompareWord, scoreMultiplier);
        }
         return newGuessBuilder;
    }

    public static int getGallowState() {
        return gallowState;
    }

    public static void OnRecreate() {
        gallowState = 0;
    }
}
