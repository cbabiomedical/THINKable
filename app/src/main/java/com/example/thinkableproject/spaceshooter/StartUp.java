package com.example.thinkableproject.spaceshooter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.thinkableproject.R;

public class StartUp extends AppCompatActivity {
    ImageButton imageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        imageButton=findViewById(R.id.imagePlay);
    }

    public void startGame(View view) {
        startActivity(new Intent(this, SpaceShooterMainActivity.class));
        finish();
    }
}