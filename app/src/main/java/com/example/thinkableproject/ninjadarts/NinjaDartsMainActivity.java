package com.example.thinkableproject.ninjadarts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.thinkableproject.R;

public class NinjaDartsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set phone default to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Fullscreen view, no toolbar/snackbar
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //Set size of screen in Constants Class
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;

        SharedPreferences pref = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        Constants.PREF = pref;

        setContentView(new GamePanel(this));
    }
}