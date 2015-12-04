package com.example.niels.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Niels on 29-11-2015.
 */
public class HighScore extends Activity {

    private ListView HighScoreListView;
    private ArrayList HighscoreListArray;
    private ArrayAdapter<String> adapterItems;
    private String GameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_layout);

        // get the called activity and set variables to current values.
        Intent calledActivity = getIntent();
        int scoreVal = calledActivity.getExtras().getInt("scoreVal");
        boolean Evilgame = calledActivity.getExtras().getBoolean("gameMode");

        // set string to current game mode to display in new high score.
        if(Evilgame) {
            GameMode = "Evil Mode";
        }
        else {
            GameMode = "Good Mode";
        }

        // initiate the listview, array, arrayAdapter, and read textfile for existing scores.
        HighScoreListView = (ListView)findViewById(R.id.HighScoreListView);
        HighscoreListArray = new ArrayList<String>();
        ReadFile();
        adapterItems = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                HighscoreListArray);
        HighScoreListView.setAdapter(adapterItems);

        // if score value of 0 is passed in intent, it got called from the menu.
        // so only add a new score if the value is not 0 (called from win dialog).
        if (scoreVal != 0) {
            String date = DateFormat.getDateInstance().format(new Date());
            HighscoreListArray.add(date + "       " + GameMode + "        " +
                    String.valueOf(scoreVal));
            adapterItems.notifyDataSetChanged();
            writeToFile();
            finish();
        }
    }

    // method to write a new score to the txt file.
    private void writeToFile() {
        File fileDir = getFilesDir();
        File HighScoreFile = new File(fileDir, "HighScores.txt");

        try {
            FileUtils.writeLines(HighScoreFile, HighscoreListArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // method to read the txt file.
    private void ReadFile() {
        File fileDir = getFilesDir();
        File HighScoreFile = new File(fileDir, "HighScores.txt");

        // sets the ListArray from Highscores.txt
        // FileUtils is compiled with 'org.apache.commons:commons-io:1.3.2' see build.gradle app
        try {
            HighscoreListArray = new ArrayList<>(FileUtils.readLines(HighScoreFile));
        } catch (IOException e) {
            HighscoreListArray = new ArrayList<String>();
        }
    }

    // initiation for the menu, not working now, also not really necessarily.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.SettingsMenuOption) {
            Intent GetSettingsIntent = new Intent(this, Settings.class);
            startActivity(GetSettingsIntent);
            return true;
        }
        else if (id == R.id.HighscoreMenuOption) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
