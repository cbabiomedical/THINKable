package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        image = findViewById(R.id.imageiew);

        image.setAnimation(topAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentStart =new Intent(StartActivity.this, SignInActivity.class);
                startActivity(intentStart);
                finish();

            }
        },SPLASH_SCREEN);


    }

    public void startSignup(View view) {
        Intent intent=new Intent(StartActivity.this, SignInActivity.class);
        startActivity(intent);
    }


}