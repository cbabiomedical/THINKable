package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class TwoByFourGrid extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    GameUtilities util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_by_four_grid);
        btn1 = (Button)findViewById(R.id.Btn2x4A1);
        btn2 = (Button)findViewById(R.id.Btn2x4A2);
        btn3 = (Button)findViewById(R.id.Btn2x4B1);
        btn4 = (Button)findViewById(R.id.Btn2x4B2);
        btn5 = (Button)findViewById(R.id.Btn2x4C1);
        btn6 = (Button)findViewById(R.id.Btn2x4C2);
        btn7 = (Button)findViewById(R.id.Btn2x4D1);
        btn8 = (Button)findViewById(R.id.Btn2x4D2);
        util = new GameUtilities(8);
        setUpButtons();
    }
    public void setUpButtons(){
        for(int i = 0;i<8;i++){
            writeToButton(i,"?");
            changeFontSize(i,util.getQuestionMarkSize());
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
    public void whenClicked(int index){
        if(util.isClickable(index) && util.canGuess()){
            printWord(index);
            util.setGuess(index);
        }
    }
    public void Btn2x4A1WhenClicked(View view){whenClicked(0);}
    public void Btn2x4A2WhenClicked(View view){whenClicked(1);}
    public void Btn2x4B1WhenClicked(View view){whenClicked(2);}
    public void Btn2x4B2WhenClicked(View view){whenClicked(3);}
    public void Btn2x4C1WhenClicked(View view){whenClicked(4);}
    public void Btn2x4C2WhenClicked(View view){whenClicked(5);}
    public void Btn2x4D1WhenClicked(View view){whenClicked(6);}
    public void Btn2x4D2WhenClicked(View view){whenClicked(7);}
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
            case 6:
                btn7.setText(string);
                break;
            case 7:
                btn8.setText(string);
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
            case 4:
                btn5.setTextSize(size);
                break;
            case 5:
                btn6.setTextSize(size);
                break;
            case 6:
                btn7.setTextSize(size);
                break;
            case 7:
                btn8.setTextSize(size);
                break;
        }
    }
    public void updateScorecard(){
        util.updateMovesUsed();
        TextView view = (TextView)findViewById(R.id.movesUsed3);
        view.setText(String.valueOf(util.getMovesUsed()));
    }
}
