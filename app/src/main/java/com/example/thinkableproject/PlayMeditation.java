package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.BarVisualizer;
import com.chibde.visualizer.SquareBarVisualizer;
import com.example.thinkableproject.sample.MusicModelClass;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class PlayMeditation extends AppCompatActivity {
    AppCompatButton btnPLay, btnNext, btnPrev, btnff, btnfr;
    TextView txtsongName, txtStart, txtStop;
    SeekBar seekBar;
    BarVisualizer barVisualizer;
    ImageView imageView;
    String uri;
    String name;
    Dialog dialog_meditation;
    String music_title;
//    int time;

    public static final String EXTRA_NAME = "songName";
    static MediaPlayer mediaPlayer;
    int position;
    String sName;
    Thread updateSeekBar;
    ArrayList<MusicModelClass> songs = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_meditation);

        btnPLay = findViewById(R.id.playBtn);
        seekBar = findViewById(R.id.seekBar);
        txtStart = findViewById(R.id.txtStart);
        txtStop = findViewById(R.id.txtStop);
        txtsongName = findViewById(R.id.txtsongName);
        mediaPlayer = new MediaPlayer();
        btnff = findViewById(R.id.fForward);
        btnfr = findViewById(R.id.fRewind);
        dialog_meditation = new Dialog(this);
//        barVisualizer = findViewById(R.id.visualizer);
        mediaPlayer = new MediaPlayer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
        }

        btnPLay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPLay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                mediaPlayer.start();
                btnPLay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString("url");
//            time_selected = extras.getString("time");
            name = extras.getString("name");
//            time = Integer.parseInt(time_selected);
//            time = extras.getInt("time");
            txtsongName.setText(name);
//            image=extras.getInt("image");
//            linearLayout.setBackgroundResource(image);

            prepareMediaPlayer();

            Log.d("MUSIC", uri + "");
//            Log.d("DURATION", time_selected);
//            Log.d("NAME", name);

        } else {
            Log.d("ERROR", "Error in getting null value");
        }

//        ProgressDialog progressDialog = new ProgressDialog(this, R.style.Theme_MyDialog);
//
//        progressDialog.show(this,
//                "Loading Music", "Please Wait");
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // MEDIA STARTS FUNCTION

        mediaPlayer.setOnPreparedListener(mp -> {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
            mediaPlayer.start();
            reqestPermission();
            TranslateAnimation animation = new TranslateAnimation(-25, 25, -25, 25);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(688);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(1);

            updateSeekBar = new Thread() {
                @Override
                public void run() {

                    int totalDuration = mediaPlayer.getDuration();
                    int currentPosition = 0;
                    while (currentPosition < totalDuration) {
                        try {
                            sleep(500);
                            currentPosition = mediaPlayer.getCurrentPosition();
                            Log.d("Current position", String.valueOf(mediaPlayer.getCurrentPosition()));
                            Log.d("upCurrent time", String.valueOf(currentPosition));
//
                            seekBar.setProgress(currentPosition);
                        } catch (InterruptedException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                        if (currentPosition > mediaPlayer.getDuration()) {
                            mediaPlayer.stop();
//                            openGraphPopup();
                            startActivity(new Intent(getApplicationContext(), MeditationLineChart.class));
                        }
                    }

                }
            };

            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar.start();
            seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
            seekBar.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

            String endTime = millisecondsToTimer(mediaPlayer.getDuration());
            txtStop.setText(endTime);
//            int noOfRuns = time / mediaPlayer.getDuration();


            final Handler handler = new Handler();
            final int delay = 1000;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String currentTime = millisecondsToTimer(mediaPlayer.getCurrentPosition());
                    txtStart.setText(currentTime);
                    seekBar.setProgress(mp.getCurrentPosition());
                    handler.postDelayed(this, delay);

                }
            }, delay);

        });
        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 2000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 2000);
                }
            }
        });

    }

    private void openGraphPopup() {
        Button ok;
        dialog_meditation.setContentView(R.layout.meditation_intervention_popup);
        ok = (Button) dialog_meditation.findViewById(R.id.lineChartMeditationIntervention);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_meditation.dismiss();
            }
        });

        dialog_meditation.show();
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
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.white));
        squareBarVisualizer.setDensity(65);
        squareBarVisualizer.setGap(2);
        squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

    }

    @Override
    protected void onStart() {
        super.onStart();
        btnPLay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
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