package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class TwoByFiveGrid extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    GameUtilities util;
    public static boolean isStarted=false;
    Long startTime,endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_by_five_grid);
        btn1 = (Button)findViewById(R.id.Btn2x5A1);
        btn2 = (Button)findViewById(R.id.Btn2x5A2);
        btn3 = (Button)findViewById(R.id.Btn2x5B1);
        btn4 = (Button)findViewById(R.id.Btn2x5B2);
        btn5 = (Button)findViewById(R.id.Btn2x5C1);
        btn6 = (Button)findViewById(R.id.Btn2x5C2);
        btn7 = (Button)findViewById(R.id.Btn2x5D1);
        btn8 = (Button)findViewById(R.id.Btn2x5D2);
        btn9 = (Button)findViewById(R.id.Btn2x5E1);
        btn10 = (Button)findViewById(R.id.Btn2x5E2);
        util = new GameUtilities(10);
        startTime=System.currentTimeMillis();
        isStarted=true;
        setUpButtons();
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
            case 6:
                btn7.setText(string);
                break;
            case 7:
                btn8.setText(string);
                break;
            case 8:
                btn9.setText(string);
                break;
            case 9:
                btn10.setText(string);
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
            case 8:
                btn9.setTextSize(size);
                break;
            case 9:
                btn10.setTextSize(size);
                break;
        }
    }
    public void setUpButtons(){
        for(int i = 0;i<10;i++){
            resetButton(i);
        }
    }
    public void resetButton(int index){
        writeToButton(index,"?");
        changeFontSize(index,util.getQuestionMarkSize());
    }
    public void clearButton(int index){
        writeToButton(index,"");
        util.setClickableFalse(index);
    }
    public void printWord(int index){
        writeToButton(index,util.getWordAt(index));
        changeFontSize(index,util.wordSize);
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
                isStarted=false;
                endTime=System.currentTimeMillis();
                Long seconds=(endTime-startTime)/1000;
                Intent intent = new Intent(this,VictoryPage.class).putExtra("time",seconds);
                GlobalElements.getInstance().addToScore(GlobalElements.getPar(GlobalElements.getInstance().getLevel())-util.getMovesUsed());
                startActivity(intent);
            }
        }
    }
    public void updateScorecard(){
        util.updateMovesUsed();
        TextView view = (TextView)findViewById(R.id.movesUsed4);
        view.setText(String.valueOf(util.getMovesUsed()));
    }
    public void whenClicked(int index){
        if(util.isClickable(index) && util.canGuess()){
            printWord(index);
            util.setGuess(index);
        }
    }
    public void Btn2x5A1WhenClicked(View view){whenClicked(0);}
    public void Btn2x5A2WhenClicked(View view){whenClicked(1);}
    public void Btn2x5B1WhenClicked(View view){whenClicked(2);}
    public void Btn2x5B2WhenClicked(View view){whenClicked(3);}
    public void Btn2x5C1WhenClicked(View view){whenClicked(4);}
    public void Btn2x5C2WhenClicked(View view){whenClicked(5);}
    public void Btn2x5D1WhenClicked(View view){whenClicked(6);}
    public void Btn2x5D2WhenClicked(View view){whenClicked(7);}
    public void Btn2x5E1WhenClicked(View view){whenClicked(8);}
    public void Btn2x5E2WhenClicked(View view){whenClicked(9);}
}
