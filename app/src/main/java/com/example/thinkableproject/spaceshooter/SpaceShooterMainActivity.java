package com.example.thinkableproject.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.thinkableproject.R;

public class SpaceShooterMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_shooter_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}