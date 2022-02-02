package com.example.thinkableproject.duckhunt;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkableproject.R;

public class StartGame extends AppCompatActivity {

    GameView gameView;
    MediaPlayer bg_music;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        gameView = new GameView(this);
        setContentView(gameView);
        bg_music = MediaPlayer.create(this, R.raw.bg_music);
        if (bg_music != null){
            bg_music.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bg_music != null){
            bg_music.stop();
            bg_music.release();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (bg_music != null){
//            bg_music.stop();
//            bg_music.release();
//    }
//}
}
