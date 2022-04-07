package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlayMeditation extends AppCompatActivity {
    AppCompatButton btnPLay, btnNext, btnPrev, btnff, btnfr;
    TextView txtsongName, txtStart, txtStop;
    SeekBar seekBar;
    ImageView imageView;
    Animation scaleUp, scaleDown;

    String uri;
    String name;
    int x;
    FirebaseUser mUser;
    Dialog dialog;
    User user;
    Long startTime, endTime;
    FirebaseFirestore database;
    LineChart lineChart;
    LineData lineData;
    int points;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    public static boolean isStarted = false;
    //    int time;
    String music_title;

    public static final String EXTRA_NAME = "songName";
    static MediaPlayer mediaPlayer;
    Thread updateSeekBar;
    ArrayList<MusicModelClass> songs = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
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
        dialog = new Dialog(this);
        btnff = findViewById(R.id.fForward);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        btnfr = findViewById(R.id.fRewind);
        database = FirebaseFirestore.getInstance();
        startTime = System.currentTimeMillis();
        isStarted = true;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        lineChart = findViewById(R.id.lineChartIntervention);
        mediaPlayer = new MediaPlayer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
        }

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        btnPLay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPLay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                mediaPlayer.start();
                btnPLay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });

        btnPLay.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnPLay.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnPLay.startAnimation(scaleDown);
                }

                return false;
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString("url");
            name = extras.getString("name");
//            time = extras.getInt("time");
            txtsongName.setText(name);

            prepareMediaPlayer();

            Log.d("MUSIC", uri + "");
        } else {
            Log.d("ERROR", "Error in getting null value");
        }

//        ProgressDialog progressDialog = new ProgressDialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
//        ProgressDialog.show(this,
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
                    while (currentPosition < mediaPlayer.getDuration()) {
                        try {
                            sleep(500);
                            currentPosition = mediaPlayer.getCurrentPosition();
                            Log.d("Current position", String.valueOf(mediaPlayer.getCurrentPosition()));
                            Log.d("upCurrent time", String.valueOf(currentPosition));
                            if (mediaPlayer.getDuration() < 60000) {
                                if (mediaPlayer.getCurrentPosition() <= 30000) {
                                    points = 5;
                                } else {
                                    points = 10;
                                }

                            } else if (60000 < mediaPlayer.getDuration() && mediaPlayer.getDuration() < 120000) {
                                if (mediaPlayer.getCurrentPosition() <= 60000) {
                                    points = 5;
                                } else {
                                    points = 10;
                                }
                            } else if (120000 < mediaPlayer.getDuration() && mediaPlayer.getDuration() <= 180000) {
                                if (mediaPlayer.getCurrentPosition() <= 150000) {
                                    points = 5;
                                } else {
                                    points = 10;
                                }

                            } else if (180000 < mediaPlayer.getDuration() && mediaPlayer.getDuration() < 240000) {
                                if (mediaPlayer.getCurrentPosition() <= 210000) {
                                    points = 5;
                                } else {
                                    points = 10;
                                }
                            }
                            Log.d("POINTS", String.valueOf(points));

//
                            seekBar.setProgress(currentPosition);
                        } catch (InterruptedException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                        if (currentPosition > mediaPlayer.getDuration()) {
                            mediaPlayer.stop();

                            Log.d("End Point", String.valueOf(points));

                            SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("value", points);
                            editor.apply();
                            database.collection("users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    user = documentSnapshot.toObject(User.class);
//
                                    Log.d("Current Coins", String.valueOf(user.getCoins()));
                                    int updateCoins = (int) (user.getCoins() + points);
                                    database.collection("users").document(FirebaseAuth.getInstance().getUid())
                                            .update("coins", updateCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(PlayMeditation.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Error", String.valueOf(e));
//                                            Toast.makeText(PlayMeditation.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                                        }
                                    });

//                Log.d("Updated Coin", String.valueOf(updateCoins));


//                binding.currentCoins.setText(user.getCoins() + "");

                                }
                            });

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

        btnff.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnff.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnff.startAnimation(scaleDown);
                }

                return false;
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

        btnfr.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnfr.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnfr.startAnimation(scaleDown);
                }

                return false;
            }
        });
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//
//                dialog.setContentView(R.layout.intervention_graph_popup);
//                lineChart = (LineChart) dialog.findViewById(R.id.lineChartIntervention);
//                getEntries();
//                lineDataSet = new LineDataSet(lineEntries, "Concentration Index");
//                lineData = new LineData(lineDataSet);
//                lineChart.setData(lineData);
//
//                lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                lineDataSet.setValueTextColor(Color.WHITE);
//                lineDataSet.setValueTextSize(10f);
//
//                lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                lineChart.setBorderColor(Color.TRANSPARENT);
//                lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                lineChart.getAxisLeft().setDrawGridLines(false);
//                lineChart.getXAxis().setDrawGridLines(false);
//                lineChart.getAxisRight().setDrawGridLines(false);
//                lineChart.getXAxis().setTextColor(R.color.white);
//                lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
//                lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
//                lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
//                lineChart.getDescription().setTextColor(R.color.white);
//
//
//            }
//        });
//
//    }
    }

    private void openPopUp() {
        Button ok;
        TextView coins;
        TextView totalCoins;

        dialog.setContentView(R.layout.music_graph_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ok = (Button) dialog.findViewById(R.id.click);
//        lineChart = (LineChart) dialog.findViewById(R.id.lineChartIntervention);
        ok = (Button) dialog.findViewById(R.id.click);
        coins = (TextView) dialog.findViewById(R.id.points);
        totalCoins = (TextView) dialog.findViewById(R.id.total);
//        getEntries();
//        lineDataSet = new LineDataSet(lineEntries, "Concentration Index");
//        lineData = new LineData(lineDataSet);
//        lineChart.setData(lineData);
//
//        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        lineDataSet.setValueTextColor(Color.WHITE);
//        lineDataSet.setValueTextSize(10f);
//
//        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//        lineChart.setBorderColor(Color.TRANSPARENT);
//        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//        lineChart.getAxisLeft().setDrawGridLines(false);
//        lineChart.getXAxis().setDrawGridLines(false);
//        lineChart.getAxisRight().setDrawGridLines(false);
//        lineChart.getXAxis().setTextColor(R.color.white);
//        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
//        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
//        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
//        lineChart.getDescription().setTextColor(R.color.white);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
//

    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));
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
        endTime = System.currentTimeMillis();
        Long seconds = (endTime - startTime) / 1000;
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
        endTime=System.currentTimeMillis();
        DatabaseReference referenceTime = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
        Log.d("ReferencePath", String.valueOf(referenceTime));
        referenceTime.setValue(seconds);
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