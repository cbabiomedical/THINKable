package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class TwoBySixGrid extends AppCompatActivity {

    Button[] btnArr;
    GameUtilities util;
    public static boolean isStarted = false;
    Long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_by_six_grid);
        Button btn1 = (Button) findViewById(R.id.Btn2x6A1);
        Button btn2 = (Button) findViewById(R.id.Btn2x6A2);
        Button btn3 = (Button) findViewById(R.id.Btn2x6B1);
        Button btn4 = (Button) findViewById(R.id.Btn2x6B2);
        Button btn5 = (Button) findViewById(R.id.Btn2x6C1);
        Button btn6 = (Button) findViewById(R.id.Btn2x6C2);
        Button btn7 = (Button) findViewById(R.id.Btn2x6D1);
        Button btn8 = (Button) findViewById(R.id.Btn2x6D2);
        Button btn9 = (Button) findViewById(R.id.Btn2x6E1);
        Button btn10 = (Button) findViewById(R.id.Btn2x6E2);
        Button btn11 = (Button) findViewById(R.id.Btn2x6F1);
        Button btn12 = (Button) findViewById(R.id.Btn2x6F2);
        btnArr = new Button[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12};
        util = new GameUtilities(12);
        startTime = System.currentTimeMillis();
        isStarted = true;
        setUpButtons();
    }

    public void setUpButtons() {
        for (int i = 0; i < 12; i++) {
            resetButton(i);
        }
    }

    public void resetButton(int index) {
        btnArr[index].setText("?");
        btnArr[index].setTextSize(util.getQuestionMarkSize());
    }

    public void clearButton(int index) {
        btnArr[index].setText("");
        util.setClickableFalse(index);
    }

    public void printWord(int index) {
        btnArr[index].setText(util.getWordAt(index));
        btnArr[index].setTextSize(util.wordSize);
    }

    public void updateScorecard() {
        util.updateMovesUsed();
        TextView view = (TextView) findViewById(R.id.movesUsed5);
        view.setText(String.valueOf(util.getMovesUsed()));
    }

    public void checkGuess(View view) {
        if (util.fullyGuessed()) {
            updateScorecard();
            if (util.matches()) {
                clearButton(util.getFirstGuessIndex());
                clearButton(util.getSecondGuessIndex());
                util.updateButtonsLeft();
            } else {
                resetButton(util.getFirstGuessIndex());
                resetButton(util.getSecondGuessIndex());
            }
            util.resetGuess();
            if (util.getButtonsLeft() == 0) {
                endTime = System.currentTimeMillis();
                Long seconds = (endTime - startTime) / 1000;
                Intent intent = new Intent(this, VictoryPage.class).putExtra("time", seconds);
                GlobalElements.getInstance().addToScore(GlobalElements.getPar(GlobalElements.getInstance().getLevel()) - util.getMovesUsed());
                startActivity(intent);
            }
        }
    }

    public void whenClicked(int index) {
        if (util.isClickable(index) && util.canGuess()) {
            printWord(index);
            util.setGuess(index);
        }
    }

    public void Btn2x6A1WhenClicked(View view) {
        whenClicked(0);
    }

    public void Btn2x6A2WhenClicked(View view) {
        whenClicked(1);
    }

    public void Btn2x6B1WhenClicked(View view) {
        whenClicked(2);
    }

    public void Btn2x6B2WhenClicked(View view) {
        whenClicked(3);
    }

    public void Btn2x6C1WhenClicked(View view) {
        whenClicked(4);
    }

    public void Btn2x6C2WhenClicked(View view) {
        whenClicked(5);
    }

    public void Btn2x6D1WhenClicked(View view) {
        whenClicked(6);
    }

    public void Btn2x6D2WhenClicked(View view) {
        whenClicked(7);
    }

    public void Btn2x6E1WhenClicked(View view) {
        whenClicked(8);
    }

    public void Btn2x6E2WhenClicked(View view) {
        whenClicked(9);
    }

    public void Btn2x6F1WhenClicked(View view) {
        whenClicked(10);
    }

    public void Btn2x6F2WhenClicked(View view) {
        whenClicked(11);
    }
}
