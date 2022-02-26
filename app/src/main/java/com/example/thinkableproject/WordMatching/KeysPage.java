package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class KeysPage extends AppCompatActivity {

    int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_page);
        GlobalElements.getInstance().resetKeySet(GlobalElements.getInstance().getLevel());
        level = GlobalElements.getInstance().getLevel();
        save();
        printKeys(level);
    }
    public void save(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Score",GlobalElements.getInstance().getScore());
        editor.putInt("Level",GlobalElements.getInstance().getLevel());
        editor.commit();
    }
    public void printKeys(int level){
        int pairs = GlobalElements.getNumOfKeyPairsNeeded(level);
        switch(pairs){
            case 1:
                printFirstRow();
                break;
            case 2:
                printSecondRow();
                break;
            case 3:
                printThirdRow();
                break;
            case 4:
                printFourthRow();
                break;
            case 5:
                printFifthRow();
                break;
            case 6:
                printSixthRow();
                break;
        }
    }
    public void printFirstRow(){
        String firstRowFirstColumnString = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(0);
        String firstRowSecondColumnString = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(0);
        TextView firstRowFirstColumn = (TextView) findViewById(R.id.firstRowFirstColumn);
        TextView firstRowSecondColumn = (TextView)findViewById(R.id.firstRowSecondColumn);
        firstRowFirstColumn.setText(firstRowFirstColumnString);
        firstRowSecondColumn.setText(firstRowSecondColumnString);
    }
    public void printSecondRow(){
        printFirstRow();
        String secondRowFirstColumnString = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(1);
        String secondRowSecondColumnString = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(1);
        TextView secondRowFirstColumn = (TextView) findViewById(R.id.secondRowFirstColumn);
        TextView secondRowSecondColumn = (TextView) findViewById(R.id.secondRowSecondColumn);
        secondRowFirstColumn.setText(secondRowFirstColumnString);
        secondRowSecondColumn.setText(secondRowSecondColumnString);
        secondRowFirstColumn.setTextColor(Color.YELLOW);
        secondRowSecondColumn.setTextColor(Color.YELLOW);
    }
    public void printThirdRow(){
        printSecondRow();
        String a = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(2);
        String b = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(2);
        TextView firstColumn = (TextView) findViewById(R.id.thirdRowFirstColumn);
        TextView secondColumn = (TextView) findViewById(R.id.thirdRowSecondColumn);
        firstColumn.setText(a);
        secondColumn.setText(b);
        firstColumn.setTextColor(Color.BLUE);
        secondColumn.setTextColor(Color.BLUE);
    }
    public void printFourthRow(){
        printThirdRow();
        String a = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(3);
        String b = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(3);
        TextView firstColumn = (TextView) findViewById(R.id.fourthRowFirstColumn);
        TextView secondColumn = (TextView) findViewById(R.id.fourthRowSecondColumn);
        firstColumn.setText(a);
        secondColumn.setText(b);
        firstColumn.setTextColor(Color.GREEN);
        secondColumn.setTextColor(Color.GREEN);
    }
    public void printFifthRow(){
        printFourthRow();
        String a = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(4);
        String b = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(4);
        TextView firstColumn = (TextView) findViewById(R.id.fifthRowFirstColumn);
        TextView secondColumn = (TextView) findViewById(R.id.fifthRowSecondColumn);
        firstColumn.setText(a);
        secondColumn.setText(b);
        firstColumn.setTextColor(Color.RED);
        secondColumn.setTextColor(Color.RED);
    }
    public void printSixthRow(){
        printFifthRow();
        String a = GlobalElements.getInstance().getKeySet().getKeyFromFirstList(5);
        String b = GlobalElements.getInstance().getKeySet().getKeyFromSecondList(5);
        TextView firstColumn = (TextView) findViewById(R.id.sixthRowFirstColumn);
        TextView secondColumn = (TextView) findViewById(R.id.sixthRowSecondColumn);
        firstColumn.setText(a);
        secondColumn.setText(b);
        firstColumn.setTextColor(Color.MAGENTA);
        secondColumn.setTextColor(Color.MAGENTA);
    }
    public void continueToGame(View view){
        Intent intent = new Intent(this,TwoByTwoGrid.class);
        if(level >= 3 && level<= 5){
            intent = new Intent(this,TwoByThreeGrid.class);
        }
        else if(level >= 6 && level <= 9){
            intent = new Intent(this,TwoByFourGrid.class);
        }
        else if(level >= 10 && level <= 14){
            intent = new Intent(this,TwoByFiveGrid.class);
        }
        else if(level >= 15 && level <= 20){
            intent = new Intent(this,TwoBySixGrid.class);
        }
        startActivity(intent);
    }
}
