package com.example.niels.hangman;

/**
 * Created by Niels on 1-12-2015.
 */
public class Score extends MainActivity {

    // Sets score, for every succession of right guesses adds extra points.
    public static void setScore(char ButtonClick, String compareWord, int scoreMultiplier) {
        int charCount = 0;
        for (int i = 0; i < compareWord.length(); i++) {
            if (ButtonClick == compareWord.charAt(i)) {
                charCount++;
            }
        }
        scoreVal += charCount + (scoreMultiplier -1);
        scoreValText.setText(String.valueOf(scoreVal));
    }
}
