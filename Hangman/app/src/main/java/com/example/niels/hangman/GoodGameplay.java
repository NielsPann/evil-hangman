package com.example.niels.hangman;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Niels Pannekeet 11035668 on 26-11-2015.
 * Good game play mode picks out a random word from the given word list.
 * It checks if the letter that was guessed is in this chosen word, and reacts accordingly.
 */
public class GoodGameplay extends MainActivity{

    // initiate variables
    public static int scoreMultiplier = 0;

    // picks a random word out of the given string array
    public static String pickWord(String[] wordArray) {

        // fills list with words of chosen length
        List<String> possibleWords = new ArrayList<String>();
        for (String aWordArray : wordArray) {
            if (aWordArray.length() == wordLength) {
                possibleWords.add(aWordArray);
            }
        }
        // gets a random number in range of length of the word list
        Random rand = new Random();
        int wordPicker = rand.nextInt(possibleWords.size());

        return possibleWords.get(wordPicker);
    }

    /**
     * Check the secret word on the given letter. reveals the letter if guessed right.
     */
    public static StringBuilder HandleInputGoodMode(char ButtonClick) {

        // creates a StringBuilder from the already displayed textField
        String currentGuess = resultWordText.getText().toString();
        StringBuilder newGuessBuilder = new StringBuilder(currentGuess);

        // reveals characters in the stringBuilder if they are found in the guessing word
        for (int i = 0; i < guessingWord.length(); i++) {
            char character = guessingWord.charAt(i);

            if (ButtonClick == character) {
                newGuessBuilder.setCharAt(i, ButtonClick);
            }
        }
        // updates the TextView with right guesses
        resultWordText.setText(newGuessBuilder);

        // checks if the textView has been changed, and acts accordingly
        String CompareWord = resultWordText.getText().toString();
        if (currentGuess.equals(CompareWord)) {
            gallowState++;
            guessVal--;
            scoreMultiplier = 0;
        }
        else {
            scoreMultiplier++;
            Score.setScore(ButtonClick, CompareWord, scoreMultiplier);
        }
         return newGuessBuilder;
    }
}
