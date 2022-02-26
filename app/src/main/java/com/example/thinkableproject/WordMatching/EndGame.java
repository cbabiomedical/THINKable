package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class EndGame extends AppCompatActivity {

    static boolean newHighScore = false;
    static int highScorePrintout = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        checkHighScore();
        printScores();
    }
    public void checkHighScore(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int highScore = pref.getInt("HighScore",0);
        int userScore = GlobalElements.getInstance().getScore();
        if(userScore>highScore){
            highScorePrintout = userScore;
            newHighScore = true;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("HighScore",userScore);
            editor.commit();
        }
        else{
            highScorePrintout = highScore;
        }
    }
    public void printScores(){
        TextView view1 = (TextView)findViewById(R.id.userScore);
        TextView view2 = (TextView)findViewById(R.id.highScore);
        view1.setText(String.valueOf(GlobalElements.getInstance().getScore()));
        String highScorePrint = String.valueOf(highScorePrintout);
        if(newHighScore){
            highScorePrint+="(NEW!)";
        }
        view2.setText(highScorePrint);
    }
    public void mainMenu(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Score",0);
        editor.putInt("Level",1);
        editor.commit();
        Intent intent = new Intent(this,MainMenu.class);
        startActivity(intent);
    }
}
