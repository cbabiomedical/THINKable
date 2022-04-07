package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlayVideo extends AppCompatActivity {

    Thread updateVideo;
    String uri;
    String name;
    boolean videoIsPlaying = false;
    FirebaseFirestore database;
    Animation scaleUp, scaleDown;

    User user;
    FirebaseUser mUser;
    int updatedCoins;
    int points;
    int x;
    Long startTime, endTime;
    public static boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        database = FirebaseFirestore.getInstance();
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        isStarted = true;
        startTime = System.currentTimeMillis();

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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
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

    @Override
    protected void onPause() {
        super.onPause();
        isStarted = false;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        endTime = System.currentTimeMillis();
        Long seconds = (endTime - startTime) / 1000;
        SharedPreferences sh = getSharedPreferences("prefsTimeRelWH", MODE_APPEND);
        x = sh.getInt("firstStartTimeRelWH", 0);
        Log.d("A Count", String.valueOf(x));

        int y = x + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsTimeRelWH", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartTimeRelWH", y);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsTimeRelWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int x1 = sha.getInt("firstStartTimeRelWH", 0);
        DatabaseReference referenceTime = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
        Log.d("ReferencePath", String.valueOf(referenceTime));
        referenceTime.setValue(seconds);
    }
}