package com.example.thinkableproject.duckhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class GameOver extends AppCompatActivity {

    TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int score = getIntent().getExtras().getInt("score");
        tvScore = findViewById(R.id.tvScore);
        tvScore.setText(""+score);
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, DuckHuntMainActivity.class);
        startActivity(intent);
        finish();
    }
}