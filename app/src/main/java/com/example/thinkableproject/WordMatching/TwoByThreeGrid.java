package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class TwoByThreeGrid extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    GameUtilities util;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_by_three_grid);
        util = new GameUtilities(6);
        btn1 = (Button)findViewById(R.id.topLeft2);
        btn2 = (Button)findViewById(R.id.topRight2);
        btn3 = (Button)findViewById(R.id.middleLeft);
        btn4 = (Button)findViewById(R.id.middleRight);
        btn5 = (Button)findViewById(R.id.bottomLeft2);
        btn6 = (Button)findViewById(R.id.bottomRight2);
        setUpButtons();
    }
    public void setUpButtons(){
        for(int i = 0;i<util.numOfButtons;i++){
            changeFontSize(i,util.getQuestionMarkSize());
        }
    }
    public void printWord(int index){
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
    public void writeToButton(int index,String string){
        switch(index){
            case 0:
                btn1.setText(string);
                break;
            case 1:
                btn2.setText(string);
                break;
            case 2:
                btn3.setText(string);
                break;
            case 3:
                btn4.setText(string);
                break;
            case 4:
                btn5.setText(string);
                break;
            case 5:
                btn6.setText(string);
                break;
        }
    }
    public void changeFontSize(int index,int font){
        switch(index){
            case 0:
                btn1.setTextSize(font);
                break;
            case 1:
                btn2.setTextSize(font);
                break;
            case 2:
                btn3.setTextSize(font);
                break;
            case 3:
                btn4.setTextSize(font);
                break;
            case 4:
                btn5.setTextSize(font);
                break;
            case 5:
                btn6.setTextSize(font);
                break;
        }
    }
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
                Intent intent = new Intent(this,VictoryPage.class);
                GlobalElements.getInstance().addToScore(GlobalElements.getPar(GlobalElements.getInstance().getLevel())-util.getMovesUsed());
                startActivity(intent);
            }
        }
    }
    public void whenClicked(int index){
        if(util.isClickable(index) && util.canGuess()){
            printWord(index);
            util.setGuess(index);
        }
    }
    public void updateScorecard(){
        util.updateMovesUsed();
        TextView view2 = (TextView)findViewById(R.id.movesUsed2);
        view2.setText(String.valueOf(util.getMovesUsed()));
    }
    public void topLeftWhenClickedB(View view){whenClicked(0);}
    public void topRightWhenClickedB(View view){whenClicked(1);}
    public void middleLeftWhenClickedB(View view){whenClicked(2);}
    public void middleRightWhenClickedB(View view){whenClicked(3);}
    public void bottomLeftWhenClickedB(View view){whenClicked(4);}
    public void bottomRightWhenClickedB(View view){whenClicked(5);}
}
