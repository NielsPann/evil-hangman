package com.example.niels.hangman;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by Niels Pannekeet 11035668 on 26-11-2015.
 * Evil game play will look trough the given word list and sorts the words in lists depending on the
 * location of the letter that was guessed and passed into this class.
 * The words without the letter will all be put into the same list.
 * The biggest list will be passed back to mainActivity and depending on whether the words in this
 * list contain the guessed letter, the Textview will be updated with the letter(s) in the right place.
 */
public class EvilGameplay extends MainActivity{

    //private static int gallowState = 0;
    private static int scoreMultiplier = 0;
    private static String currentGuess;

    /**
     * Handle the letter input and avoid possible guesses of the user.
     */
    public static List<String> handleInputEvilMode(char ButtonClick, List<String> possibleWordsA) {

        // a hashtable will be filled with with lists containing words with the given letter in the
        // same locations. the key will be the location of those letters, like a__a_ or ___b_
        Hashtable<String, List<String>> hashtable = new Hashtable<String, List<String>>();
        for(int i = 0; i < possibleWordsA.size(); i++) {
            List<String> tempWordList = new ArrayList<String>();
            currentGuess = resultWordText.getText().toString();
            StringBuilder newGuessBuilder = new StringBuilder(currentGuess);
            String temWord = possibleWordsA.get(i);

            for (int j = 0; j < temWord.length(); j++) {
                if (ButtonClick == temWord.charAt(j)) {
                    newGuessBuilder.setCharAt(j, temWord.charAt(j));
                }
            }
            // The newly build StringBuilder wil be the key for the list of words.
            // If the key already exists, the word is added to the containing list, if not, the
            // key will be added with a new list containing the first word.
            String hashKey = newGuessBuilder.toString();
            if(hashtable.containsKey(hashKey)) {
                tempWordList = hashtable.get(hashKey);
                tempWordList.add(possibleWordsA.get(i));
                hashtable.put(hashKey, tempWordList);
            }
            else {
                tempWordList.add(possibleWordsA.get(i));
                hashtable.put(hashKey, tempWordList);
            }
        }

        // get the biggest list out of the hashtable (most possible words) and his key
        Set<String> keys = hashtable.keySet();
        List<String> WordListFromHash = new ArrayList<String>();
        String toKey = resultWordText.getText().toString();
        for(String key: keys) {
            List<String> tempWordListA = hashtable.get(key);
            if(tempWordListA.size() > WordListFromHash.size()) {
                WordListFromHash = tempWordListA;
                toKey = key;
            }
        }

        // set the TextView to the key of the biggest list, possibly revealing new letters
        resultWordText.setText(toKey);

        // check if TextView has a new letter added, update consequences accordingly
        if(currentGuess.equals(toKey)) {
            gallowState++;
            guessVal--;
            scoreMultiplier = 0;
        }
        else {
            scoreMultiplier++;
            Score.setScore(ButtonClick, toKey, scoreMultiplier);
        }

        return WordListFromHash;
    }

    /**
     * Fill the list with words of the right length from the given array.
     */
    public static List<String> fillListRightLength (String[] wordArray) {
        List<String> possibleWordsA = new ArrayList<String>();
        for (String aWordArray : wordArray) {
            if (aWordArray.length() == wordLength) {
                possibleWordsA.add(aWordArray.toLowerCase());
            }
        }
        return possibleWordsA;
    }
}
