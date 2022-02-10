package com.example.thinkableproject.puzzle.Class;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import com.example.thinkableproject.R;

public class Sound {
    public static  boolean activitySwitchFlag = false;
    public static  boolean backFromGame = false;
    public static MediaPlayer menuClickSound, buttonGameSound,winningSound,backgroundMusic,gameMusic;
    public static boolean check = true;

    public Sound(){
    }
    public void createSound(Context mContext){
        menuClickSound = MediaPlayer.create(mContext, R.raw.buttonpressed);
        buttonGameSound = MediaPlayer.create(mContext, R.raw.cardpress);
        winningSound =MediaPlayer.create(mContext,R.raw.victory);
        backgroundMusic = MediaPlayer.create(mContext,R.raw.backgroundsound);
        gameMusic = MediaPlayer.create(mContext,R.raw.gamesound);

        gameMusic.setLooping(true);
        backgroundMusic.setLooping(true);

        setMusic();
        setSounds();
    }
    public void  setSounds(){
        if (check) {
            buttonGameSound.setVolume(0.1f, 0.1f);
            winningSound.setVolume(0.1f, 0.1f);
            menuClickSound.setVolume(0.1f, 0.1f);
        }
        else {
            buttonGameSound.setVolume(0, 0);
            winningSound.setVolume(0, 0);
            menuClickSound.setVolume(0, 0);

        }


    }

    public void setMusic(){
        if(check){
            gameMusic.setVolume(0.5f,0.5f);
            backgroundMusic.setVolume(0.05f,0.05f);
        }
        else {
            gameMusic.setVolume(0, 0);
            backgroundMusic.setVolume(0, 0);
        }
    }
    public void switchMusic(final MediaPlayer in , MediaPlayer out){
        out.pause();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                in.start();
            }
        },1000);

    }
    public void release(){
        gameMusic.release();
        backgroundMusic.release();
        menuClickSound.release();
        winningSound.release();
        buttonGameSound.release();
    }


}
