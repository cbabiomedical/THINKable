package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideo extends AppCompatActivity {

    Thread updateVideo;
    boolean videoIsPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        MediaController mediaController = new MediaController(this);
        VideoView simpleVideoView = (VideoView) findViewById(R.id.videoExample);
        simpleVideoView.setVisibility(View.VISIBLE);
        simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cardgame));
        simpleVideoView.start();
        simpleVideoView.setMediaController(mediaController);
        Log.d("Video Duration", String.valueOf(simpleVideoView.getDuration()));
        Log.d("Current Position", String.valueOf(simpleVideoView.getCurrentPosition()));

        updateVideo = new Thread() {
            @Override
            public void run() {
                int totalDuration = simpleVideoView.getDuration();
                Log.d("Total Duration", String.valueOf(totalDuration));
                int currentPosition = 0;
                while (currentPosition < simpleVideoView.getDuration()) {
                    try {
                        sleep(500);
                        currentPosition = simpleVideoView.getCurrentPosition();
                        Log.d("Current position", String.valueOf(simpleVideoView.getCurrentPosition()));
                        Log.d("UpCurrent time", String.valueOf(currentPosition));

                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }

                    if (simpleVideoView.getCurrentPosition() > simpleVideoView.getDuration()) {
                        simpleVideoView.stopPlayback();
                        startActivity(new Intent(getApplicationContext(), ColorPatternGame.class));
                    }
                }
            }


//        simpleVideoView.setMediaController(mediaController);
//        if(!simpleVideoView.isPlaying()){
//            startActivity(new Intent(getApplicationContext(),ColorPatternGame.class));
//        }


        };
    }
}