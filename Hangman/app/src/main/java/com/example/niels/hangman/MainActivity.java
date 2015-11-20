package com.example.niels.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button buttonQ, buttonW, buttonE, buttonR, buttonT, buttonY, buttonU, buttonI, buttonO, buttonP,
            buttonA, buttonS, buttonD, buttonF, buttonG, buttonH, buttonJ, buttonK, buttonL,
            buttonZ, buttonX, buttonC, buttonV, buttonB, buttonN, buttonM;

    private TextView resultWord;

    private ImageView gallowImage;

    private String currentGuess;

    private String guessingWord;

    private StringBuilder newGuessBuilder;

    private int amountOfGuesses;

    private int gallowState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonQ = (Button) findViewById(R.id.buttonQ);
        buttonW = (Button) findViewById(R.id.buttonW);
        buttonE = (Button) findViewById(R.id.buttonE);
        buttonR = (Button) findViewById(R.id.buttonR);
        buttonT = (Button) findViewById(R.id.buttonT);
        buttonY = (Button) findViewById(R.id.buttonY);
        buttonU = (Button) findViewById(R.id.buttonU);
        buttonI = (Button) findViewById(R.id.buttonI);
        buttonO = (Button) findViewById(R.id.buttonO);
        buttonP = (Button) findViewById(R.id.buttonP);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonS = (Button) findViewById(R.id.buttonS);
        buttonD = (Button) findViewById(R.id.buttonD);
        buttonF = (Button) findViewById(R.id.buttonF);
        buttonG = (Button) findViewById(R.id.buttonG);
        buttonH = (Button) findViewById(R.id.buttonH);
        buttonJ = (Button) findViewById(R.id.buttonJ);
        buttonK = (Button) findViewById(R.id.buttonK);
        buttonL = (Button) findViewById(R.id.buttonL);
        buttonZ = (Button) findViewById(R.id.buttonZ);
        buttonX = (Button) findViewById(R.id.buttonX);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonV = (Button) findViewById(R.id.buttonV);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonN = (Button) findViewById(R.id.buttonN);
        buttonM = (Button) findViewById(R.id.buttonM);

        resultWord = (TextView) findViewById(R.id.wordResult);

        gallowImage = (ImageView)findViewById(R.id.imageView);

        guessingWord = pickWord();
        amountOfGuesses = 10;
        gallowState = 0;
    }

    public void HandleTheButton(char ButtonClick) {
        currentGuess = resultWord.getText().toString();
        newGuessBuilder = new StringBuilder(currentGuess);

        for (int i = 0; i < guessingWord.length(); i++) {
            char character = guessingWord.charAt(i);

            if (ButtonClick == character) {
                newGuessBuilder.setCharAt(i, ButtonClick);
            }
        }
        resultWord.setText(newGuessBuilder);
        String CompareWord = resultWord.getText().toString();

        if(currentGuess.equals(CompareWord)) {
            //Toast.makeText(this, "Fout!!", Toast.LENGTH_SHORT).show();
            gallowState++;
            updateGallow();
        }
    }

    public void updateGallow() {
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
        }
    }

    public String pickWord() {
        String[] wordList = {"browser", "chirurg", "fietsen", "dwergen", "monitor", "pudding",
                "planeet", "terreur"};

        Random rand = new Random();

        int wordPicker = rand.nextInt(8);

        String wordChoice = wordList[wordPicker];

        return wordChoice;
    }


    public void OnClickButtonQ(View view) {
        HandleTheButton('q');
        buttonQ.setEnabled(false);
    }

    public void OnClickButtonW(View view) {
        HandleTheButton('w');
        buttonW.setEnabled(false);
    }

    public void OnClickButtonE(View view) {
        HandleTheButton('e');
        buttonE.setEnabled(false);
    }

    public void OnClickButtonR(View view) {
        HandleTheButton('r');
        buttonR.setEnabled(false);
    }

    public void OnClickButtonT(View view) {
        HandleTheButton('t');
        buttonT.setEnabled(false);
    }

    public void OnClickButtonY(View view) {
        HandleTheButton('y');
        buttonY.setEnabled(false);
    }

    public void OnClickButtonU(View view) {
        HandleTheButton('u');
        buttonU.setEnabled(false);
    }

    public void OnClickButtonI(View view) {
        HandleTheButton('i');
        buttonI.setEnabled(false);
    }

    public void OnClickButtonO(View view) {
        HandleTheButton('o');
        buttonO.setEnabled(false);
    }

    public void OnClickButtonP(View view) {
        HandleTheButton('p');
        buttonP.setEnabled(false);
    }

    public void OnClickButtonA(View view) {
        HandleTheButton('a');
        buttonA.setEnabled(false);
    }

    public void OnClickButtonS(View view) {
        HandleTheButton('s');
        buttonS.setEnabled(false);
    }

    public void OnClickButtonD(View view) {
        HandleTheButton('d');
        buttonD.setEnabled(false);
    }

    public void OnClickButtonF(View view) {
        HandleTheButton('f');
        buttonF.setEnabled(false);
    }

    public void OnClickButtonG(View view) {
        HandleTheButton('g');
        buttonG.setEnabled(false);
    }

    public void OnClickButtonH(View view) {
        HandleTheButton('h');
        buttonH.setEnabled(false);
    }

    public void OnClickButtonJ(View view) {
        HandleTheButton('j');
        buttonJ.setEnabled(false);
    }

    public void OnClickButtonK(View view) {
        HandleTheButton('k');
        buttonK.setEnabled(false);
    }

    public void OnClickButtonL(View view) {
        HandleTheButton('l');
        buttonL.setEnabled(false);
    }

    public void OnClickButtonZ(View view) {
        HandleTheButton('z');
        buttonZ.setEnabled(false);
    }

    public void OnClickButtonX(View view) {
        HandleTheButton('x');
        buttonX.setEnabled(false);
    }

    public void OnClickButtonC(View view) {
        HandleTheButton('c');
        buttonC.setEnabled(false);
    }

    public void OnClickButtonV(View view) {
        HandleTheButton('v');
        buttonV.setEnabled(false);
    }

    public void OnClickButtonB(View view) {
        HandleTheButton('b');
        buttonB.setEnabled(false);
    }

    public void OnClickButtonN(View view) {
        HandleTheButton('n');
        buttonN.setEnabled(false);
    }

    public void OnClickButtonM(View view) {
        HandleTheButton('m');
        buttonM.setEnabled(false);
    }
}

