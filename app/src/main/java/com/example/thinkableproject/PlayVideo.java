package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayVideo extends AppCompatActivity {

    Thread updateVideo;
    String uri;
    String name;
    boolean videoIsPlaying = false;
    FirebaseFirestore database;
    User user;
    int updatedCoins;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        database = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString("url");
            name = extras.getString("name");
//            time = extras.getInt("time");


            Log.d("MUSIC", uri + "");
        } else {
            Log.d("ERROR", "Error in getting null value");
        }

        MediaController mediaController = new MediaController(this);
        VideoView simpleVideoView = (VideoView) findViewById(R.id.videoExample);

        simpleVideoView.setVisibility(View.VISIBLE);
        simpleVideoView.setVideoURI(Uri.parse(uri));
        simpleVideoView.start();
        simpleVideoView.setMediaController(mediaController);
        simpleVideoView.getDuration();
        simpleVideoView.getCurrentPosition();
        Log.d("Duration", String.valueOf(simpleVideoView.getDuration()));
        Log.d("Position", String.valueOf(simpleVideoView.getCurrentPosition()));
        Log.d("Video Duration", String.valueOf(simpleVideoView.getDuration()));
        Log.d("Current Position", String.valueOf(simpleVideoView.getCurrentPosition()));
//
        simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), VideoInterventionActivity.class));
                        points = 10;
                        database.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                                Log.d("Current Coins", String.valueOf(user.getCoins()));
                                Log.d("High Score Inside", String.valueOf(points));
                                updatedCoins = (int) (user.getCoins() + points);
                                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                                database.collection("users").document(FirebaseAuth.getInstance().getUid())
                                        .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ColorPatternGame.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });
                    }
                }, 1000);

            }
        });

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

//                    if (simpleVideoView.getCurrentPosition() == simpleVideoView.getDuration()) {
////                        startActivity(new Intent(getApplicationContext(), VideoInterventionActivity.class));
////                    }
                }
            }


//        simpleVideoView.setMediaController(mediaController);
//        if(!simpleVideoView.isPlaying()){
//            startActivity(new Intent(getApplicationContext(),ColorPatternGame.class));
//        }


        };
    }
}