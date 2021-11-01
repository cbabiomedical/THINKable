package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.SquareBarVisualizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MusicPlayer extends AppCompatActivity {

    private ImageView imageViewPlayPause, music_lib;
    private TextView textCurrentTime, textTotalTimeDuration, music_title;
    private SeekBar playSeekBar;
    private static MediaPlayer mediaPlayer;
    private String time_selected;
    private int time;
    private String name;
    boolean play = true;
    int image;
    String uri;
    Thread updateSeekBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        imageViewPlayPause = findViewById(R.id.imagePlayPause);
        playSeekBar = findViewById(R.id.playerSeekBar);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalTimeDuration = findViewById(R.id.totalDuration);
        music_title = findViewById(R.id.music_title);
        music_lib = findViewById(R.id.imagePlayIcon);
        mediaPlayer = new MediaPlayer();
        play = mediaPlayer.isPlaying();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
        }

        imageViewPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                imageViewPlayPause.setImageResource(R.drawable.ic_play_circle);
            } else {
                mediaPlayer.start();
                imageViewPlayPause.setImageResource(R.drawable.ic_pause_circle);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString("url");
//            time_selected = extras.getString("time");
            name = extras.getString("name");
//            time = Integer.parseInt(time_selected);
            music_title.setText(name);
            image=extras.getInt("image");

            prepareMediaPlayer();

            Log.d("MUSIC", uri + "");
//            Log.d("DURATION", time_selected);
//            Log.d("NAME", name);

        } else {
            Log.d("ERROR", "Error in getting null value");
        }
        textTotalTimeDuration.setText(millisecondsToTimer(time));

        ProgressDialog progressDialog = ProgressDialog.show(this,
                "Loading Music", "Please Wait");

        // MEDIA STARTS FUNCTION

        mediaPlayer.setOnPreparedListener(mp -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            mediaPlayer.start();
            reqestPermission();
            TranslateAnimation animation = new TranslateAnimation(-25, 25, -25, 25);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(688);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(1);
            music_lib.startAnimation(animation);

            updateSeekBar = new Thread() {
                @Override
                public void run() {

                    int totalDuration = time;
                    int currentPosition = 0;
                    while (currentPosition < totalDuration) {
                        try {
                            sleep(500);
                            currentPosition = mediaPlayer.getCurrentPosition();
                            Log.d("upCurrent time", String.valueOf(currentPosition));
//
                            playSeekBar.setProgress(currentPosition);
                        } catch (InterruptedException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                    if (currentPosition > totalDuration) {
                        mediaPlayer.stop();

                        imageViewPlayPause.setImageResource(R.drawable.ic_play_circle);
                        Intent intent = new Intent(getApplicationContext(), concentration_music.class);
                        startActivity(intent);
                        String notification = "Media finished Playing";
                        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_notifications).setContentTitle("New Notification").setContentText(notification)
                                .setAutoCancel(true);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, builder.build());

//                       Intent intent=new Intent(getApplicationContext(),MainActivity.class);

                    }



                }
            };

            playSeekBar.setMax(time);
            updateSeekBar.start();
            playSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.MULTIPLY);
            playSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_500), PorterDuff.Mode.SRC_IN);
            playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            });
            int noOfRuns = time / mediaPlayer.getDuration();


            final Handler handler = new Handler();
            final int delay = 1000;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String currentTime = millisecondsToTimer(mediaPlayer.getCurrentPosition());
                    textCurrentTime.setText(currentTime);
                    handler.postDelayed(this, delay);
                    if (mediaPlayer.getDuration() < time) {

                        Log.d("RUNS", String.valueOf(noOfRuns));
                        int remain = time % mediaPlayer.getDuration();
                        Log.d("REMAIN", String.valueOf(remain));
                        int playNo = noOfRuns + 1;
                        Log.d("play", String.valueOf(playNo));

                    }


                }
            }, delay);

        });
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(uri.toString());
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reqestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now

                            startAudioVisulizer();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }

                })
                .onSameThread()
                .check();

    }

    private void startAudioVisulizer() {     //Audio Visualizer

        SquareBarVisualizer squareBarVisualizer = findViewById(R.id.visualizer);
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.purple_700));
        squareBarVisualizer.setDensity(65);
        squareBarVisualizer.setGap(2);
        squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

    }

    @Override
    protected void onStart() {
        super.onStart();
        imageViewPlayPause.setImageResource(R.drawable.ic_pause_circle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
//        imageViewPlayPause.setImageResource(R.drawable.ic_play_circle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }


    private String millisecondsToTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = " " + seconds;
        }
        timerString = timerString + minutes + ":" + secondsString;

        return timerString;
    }
}