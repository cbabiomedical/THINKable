package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class TwoByTwoGrid extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    GameUtilities util;
    public static boolean isStarted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_by_two_grid);
        btn1 = (Button)findViewById(R.id.topLeft);
        btn2 = (Button)findViewById(R.id.topRight);
        btn3 = (Button)findViewById(R.id.bottomLeft);
        btn4 = (Button)findViewById(R.id.bottomRight);
        util = new GameUtilities(4);
        isStarted=true;
    }
    public void writeToButton(int index,String text){
        switch(index){
            case 0:
                btn1.setText(text);
                break;
            case 1:
                btn2.setText(text);
                break;
            case 2:
                btn3.setText(text);
                break;
            case 3:
                btn4.setText(text);
                break;
        }
    }
    public void changeFontSize(int index,int size){
        switch(index){
            case 0:
                btn1.setTextSize(size);
                break;
            case 1:
                btn2.setTextSize(size);
                break;
            case 2:
                btn3.setTextSize(size);
                break;
            case 3:
                btn4.setTextSize(size);
                break;
        }
    }
    public void whenClicked(int index){
        if(util.isClickable(index) && util.canGuess()){
            printButton(index);
            util.setGuess(index);
        }
    }
    public void topLeftWhenClicked(View view){whenClicked(0);}
    public void topRightWhenClicked(View view){whenClicked(1);}
    public void bottomLeftWhenClicked(View view){whenClicked(2);}
    public void bottomRightWhenClicked(View view){whenClicked(3);}
    public void checkGuess(View view){
        if(util.fullyGuessed()){
            updateScorecard();
            if(util.matches()){
                clearButton(util.getFirstGuessIndex());
                clearButton(util.getSecondGuessIndex());
                util.updateButtonsLeft();
            }
            else{
                resetButton(util.getFirstGuessIndex());
                resetButton(util.getSecondGuessIndex());
            }
            util.resetGuess();
            if(util.getButtonsLeft()==0){
                isStarted=false;
                Intent intent = new Intent(this,VictoryPage.class);
                GlobalElements.getInstance().addToScore(GlobalElements.getPar(GlobalElements.getInstance().getLevel())-util.getMovesUsed());
                startActivity(intent);
            }
        }
    }
    public void printButton(int index){
        writeToButton(index,util.getWordAt(index));
        changeFontSize(index,util.wordSize);
    }
    public void clearButton(int index){
        writeToButton(index,"");
        util.setClickableFalse(index);
    }
    public void resetButton(int index){
        writeToButton(index,"?");
        changeFontSize(index,util.getQuestionMarkSize());
    }
    public void updateScorecard(){
        TextView view = (TextView)findViewById(R.id.movesUsed);
        util.updateMovesUsed();
        view.setText(String.valueOf(util.getMovesUsed()));
    }
}
