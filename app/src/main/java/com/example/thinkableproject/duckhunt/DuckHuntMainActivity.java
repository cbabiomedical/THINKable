package com.example.thinkableproject.duckhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thinkableproject.R;

public class DuckHuntMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duck_hunt_main);
    }

    public void startGame(View view){

        //Log.i("ImageButton", "Clicked");
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
        finish();

    }
}