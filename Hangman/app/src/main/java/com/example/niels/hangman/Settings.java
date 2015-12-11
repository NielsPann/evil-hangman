package com.example.niels.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Niels Pannekeet 11035668 on 29-11-2015.
 * Activity that lets the user change different game settings, the length of the guessing word,
 * the amount of available guesses, and the game mode are changeable in this activity.
 * The settings will be applied when a new game is initiated.
 */
public class Settings extends Activity {

    private static SeekBar wordLengthBar, guessesBar;
    private static Switch EvilModeSwitch;
    private Button saveButton;
    private TextView guessesValTextView, wordLengthTextView;
    private int guessesVal, wordLengthVal;
    private boolean evilGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // Initiate interface items.
        wordLengthBar = (SeekBar)findViewById(R.id.wordLengthSlider);
        guessesBar = (SeekBar)findViewById(R.id.guessesSlider);
        EvilModeSwitch = (Switch)findViewById(R.id.evilModeSwitch);
        saveButton = (Button)findViewById(R.id.SaveButton);
        guessesValTextView = (TextView)findViewById(R.id.guessesValTextView);
        wordLengthTextView = (TextView)findViewById(R.id.wordLengthTextView);

        // Get variables from sharedPreferences. If settings haven't been changed before, use defaults.
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        guessesVal = settings.getInt("guesses", 10);
        wordLengthVal = settings.getInt("wordLength", 4);
        evilGame = settings.getBoolean("evilGame", true);

        // Set the interface items to variable from sharedPreferences.
        wordLengthBar.setProgress(wordLengthVal);
        guessesBar.setProgress(guessesVal);
        EvilModeSwitch.setChecked(evilGame);
        guessesValTextView.setText(String.valueOf(guessesVal));
        wordLengthTextView.setText(String.valueOf(wordLengthVal));

        // Listener to detect if user is interacting with the guesses SeekBar. if so, update the
        // text above to indicate the current position of the slider.
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

        // Listener to detect if user is interacting with the wordLength SeekBar. if so, update the
        // text above to indicate the current position of the slider.
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

    /**
     * When the save button is clicked, a new sharedPreferences is created and filled with the
     * values indicated with the interface items.
     */
    public void SaveButtonOnClick(View view) {
        evilGame = EvilModeSwitch.isChecked();
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("guesses", guessesVal);
        editor.putInt("wordLength", wordLengthVal);
        editor.putBoolean("evilGame", evilGame);
        editor.commit();
        Toast.makeText(this, "Settings will be applied in the next game", Toast.LENGTH_LONG).show();
        finish();
    }
}
