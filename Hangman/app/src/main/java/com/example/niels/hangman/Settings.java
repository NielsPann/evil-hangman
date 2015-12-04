package com.example.niels.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Niels on 29-11-2015.
 */
public class Settings extends Activity {

    private static SeekBar wordLengthBar, guessesBar;
    private static Switch EvilModeSwitch;
    private Button saveButton;
    private TextView guessesValTextView, wordLengthTextView;
    private int guessesVal, wordLengthVal;
    private boolean EvilGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        //Intent calledActivity = getIntent();
        wordLengthBar = (SeekBar)findViewById(R.id.wordLengthSlider);
        guessesBar = (SeekBar)findViewById(R.id.guessesSlider);
        wordLengthBar = (SeekBar)findViewById(R.id.wordLengthSlider);
        EvilModeSwitch = (Switch)findViewById(R.id.evilModeSwitch);
        saveButton = (Button)findViewById(R.id.SaveButton);
        guessesValTextView = (TextView)findViewById(R.id.guessesValTextView);
        wordLengthTextView = (TextView)findViewById(R.id.wordLengthTextView);

        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        guessesVal = settings.getInt("guesses", 10);
        wordLengthVal = settings.getInt("wordLength", 4);
        EvilGame = settings.getBoolean("EvilGame", true);

        wordLengthBar.setProgress(wordLengthVal);
        guessesBar.setProgress(guessesVal);
        EvilModeSwitch.setChecked(EvilGame);
        guessesValTextView.setText(String.valueOf(guessesVal));
        wordLengthTextView.setText(String.valueOf(wordLengthVal));



        guessesBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                String progressValString = String.valueOf(progress);
                guessesValTextView.setText(progressValString);
                guessesVal = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        wordLengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                String progressValString = String.valueOf(progress);
                wordLengthTextView.setText(progressValString);
                wordLengthVal = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void SaveButtonOnClick(View view) {
        if(EvilModeSwitch.isChecked()) {
            EvilGame = true;
        }
        else {
            EvilGame = false;
        }
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= settings.edit();
        editor.putInt("guesses", guessesVal);
        editor.putInt("wordLength", wordLengthVal);
        editor.putBoolean("EvilGame", EvilGame);
        editor.commit();
        Toast.makeText(this, "Settings will be applied in the next game", Toast.LENGTH_LONG).show();
        finish();
    }

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
