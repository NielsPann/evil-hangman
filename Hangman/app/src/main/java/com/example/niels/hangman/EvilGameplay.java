package com.example.niels.hangman;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by Niels on 26-11-2015.
 */
public class EvilGameplay extends MainActivity{

    private static int gallowState = 0;
    private static int scoreMultiplier = 0;
    private static String currentGuess;

    // handle the letter input and avoid possible guesses of the user
    public static List<String> handleInputEvilMode(char ButtonClick, List<String> possibleWordsA) {

        // a hashtable will be filled with with lists containing words with the given letter in the
        // same locations. the key will be the location of those letters, like a__a_ or ___b_.
        Hashtable<String, List<String>> hashtable = new Hashtable<String, List<String>>();
        for(int i = 0; i < possibleWordsA.size(); i++) {
            List<String> tempWordList = new ArrayList<String>();
            currentGuess = resultWord.getText().toString();
            StringBuilder newGuessBuilder = new StringBuilder(currentGuess);
            String temWord = possibleWordsA.get(i);

            for (int j = 0; j < temWord.length(); j++) {
                if (ButtonClick == temWord.charAt(j)) {
                    newGuessBuilder.setCharAt(j, temWord.charAt(j));
                }
            }
            // the newly build StringBuilder wil be the key for the list of words.
            // if the key already exists, the word is added to the containing list, if not, the
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
        //System.out.println(hashtable);

        // Get the biggest list out of the hashtable (most possible words) and his key.
        Set<String> keys = hashtable.keySet();
        List<String> WordListFromHash = new ArrayList<String>();
        String toKey = resultWord.getText().toString();
        for(String key: keys) {
            List<String> tempWordListA = hashtable.get(key);
            if(tempWordListA.size() > WordListFromHash.size()) {
                WordListFromHash = tempWordListA;
                toKey = key;
            }
        }
        // set the TextView to the key of the biggest list, possibly revealing new letters.
        resultWord.setText(toKey);

        // check if Textview has a new letter added, update consequences accordingly.
        if(currentGuess.equals(toKey)) {
            gallowState++;
            guessIndicator--;
            scoreMultiplier = 0;
        }
        else {
            scoreMultiplier++;
            Score.setScore(ButtonClick, toKey, scoreMultiplier);
        }

        return WordListFromHash;
    }

    // fill the list with words of the right length from the given array
    public static List<String> fillListRightLength (String[] wordArray) {
        List<String> possibleWordsA = new ArrayList<String>();
        for (String aWordArray : wordArray) {
            if (aWordArray.length() == wordLength) {
                possibleWordsA.add(aWordArray.toLowerCase());
            }
        }
        return possibleWordsA;
    }

    public static int getGallowState() {
        return gallowState;
    }

    public static void OnRecreate() {
        gallowState = 0;
    }
}
