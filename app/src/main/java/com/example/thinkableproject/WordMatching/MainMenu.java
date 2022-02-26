package com.example.thinkableproject.WordMatching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.thinkableproject.R;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    public void play(View view){
        GlobalElements.getInstance().init(1,0);
        Intent intent = new Intent(this,KeysPage.class);
        startActivity(intent);    }
    public void continueFromPreviousGame(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int lev = pref.getInt("Level",1);
        int score = pref.getInt("Score",0);
        GlobalElements.getInstance().init(lev,score);
        Intent intent = new Intent(this,KeysPage.class);
        startActivity(intent);
    }
}
