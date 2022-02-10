package com.example.thinkableproject.puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.Lang;
import com.example.thinkableproject.puzzle.Class.Sound;

import java.util.Locale;

public class PuzzleMainActivity extends AppCompatActivity {

    Animation topAnimation,bottomAnimation,middleAnimation;
    View line1,line2,line3,line4,line5,line6;
    TextView signture,startLogo;
    RelativeLayout logo;
    Sound sound=new Sound();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound.createSound(PuzzleMainActivity.this);


        Lang.lang = Locale.getDefault().getLanguage();


        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        middleAnimation= AnimationUtils.loadAnimation(this,R.anim.middle_animation);

        line1 = findViewById(R.id.first_line);
        line2 = findViewById(R.id.second_line);
        line3 = findViewById(R.id.third_line);
        line4 = findViewById(R.id.fourth_line);
        line5 = findViewById(R.id.fiveth_line);
        line6 = findViewById(R.id.sixth_line);

        logo = findViewById(R.id.logo);
        startLogo = findViewById(R.id.startLogo);
        signture = findViewById(R.id.signature);

        line1.setAnimation(topAnimation);
        line2.setAnimation(topAnimation);
        line3.setAnimation(topAnimation);
        line4.setAnimation(topAnimation);
        line5.setAnimation(topAnimation);
        line6.setAnimation(topAnimation);

        logo.setAnimation(middleAnimation);

        signture.setAnimation(bottomAnimation);
        int TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.Swing)
                        .duration(1000)
                        .repeat(1)
                        .playOn(startLogo);
            }
        }, TIME_OUT /2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PuzzleMainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);



    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }
}