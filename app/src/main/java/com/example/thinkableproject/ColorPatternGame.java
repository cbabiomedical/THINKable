package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.thinkableproject.sample.SoundPlayer;
import com.example.thinkableproject.spaceshooter.GameOver;
import com.example.thinkableproject.spaceshooter.StartUp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ColorPatternGame extends AppCompatActivity implements View.OnClickListener {

    TextView gameInfo, scoreInfo, scoreInfo2;
    Random rand;
    ArrayList<Animator> animList;
    Button[] btnArray;
    Button clickedBtn;
    int updatedCoins;
    private boolean isGameStarted;
    private int random;
    private int round;
    User user;
    private InterstitialAd mInterstitialAd;
    FirebaseFirestore database;
    private int counter;
    Dialog dialogColorPattern;
    private int actScore;
    private int highScore;
    ImageView colorPatternGameInfo;
    ConstraintLayout mainConstraint;
    View c1, c2;
    FirebaseUser mUser;
    VideoView gameVideo;
    int color;

    Dialog dialogIntervention;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;


    SoundPlayer sound;

    public ColorPatternGame() {
        // initial value of isGameStarted
        this.setIsGameStarted(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pattern_game);
        mainConstraint = findViewById(R.id.mainConstraint);
        database = FirebaseFirestore.getInstance();
        Log.d("UUid", FirebaseAuth.getInstance().getCurrentUser().getUid());

//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user = documentSnapshot.toObject(User.class);
////                binding.currentCoins.setText(String.valueOf(user.getCoins()));
//                Log.d("Current Coins", String.valueOf(user.getCoins()));
////                int updateCoins= (int) (user.getCoins()+highScore);
////                Log.d("Updated Coin", String.valueOf(updateCoins));
//
//
////                binding.currentCoins.setText(user.getCoins() + "");
//
//            }
//        });


        sound = new SoundPlayer(this);
        gameInfo = findViewById(R.id.gameInfo);
        scoreInfo = findViewById(R.id.scoreInfo);
        scoreInfo2 = findViewById(R.id.scoreInfo2);
        dialogColorPattern = new Dialog(this);
        gameVideo = findViewById(R.id.simpleVideo);
        dialogColorPattern = new Dialog(this);
        dialogIntervention = new Dialog(this);
        lineChart = findViewById(R.id.lineChartColorP);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        colorPatternGameInfo = findViewById(R.id.colorPatternGameinfo);


        colorPatternGameInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayColorPatternPop();
            }
        });


        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {  //light theme
                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
                    c2.setVisibility(View.VISIBLE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);


                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);


                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createPersonalizedAd();
            }
        });


        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        boolean firstStartColIn = prefsColIn.getBoolean("firstStartColIn", true);

        if (firstStartColIn) {
            displayColorPatternPop();
        }
//        ActionBar actionBar= getSupportActionBar();
//        getSupportActionBar().addTab(actionBar.newTab().setCustomView(R.menu.menu_info));
//        MediaController mediaController = new MediaController(this);
//        VideoView simpleVideoView = (VideoView) findViewById(R.id.videoExample);
//        simpleVideoView.setVisibility(View.GONE);
//        simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cardgame));
//        simpleVideoView.start();
//        Log.d("Video Duration", String.valueOf(simpleVideoView.getDuration()));
//        Log.d("Current Position", String.valueOf(simpleVideoView.getCurrentPosition()));
//        simpleVideoView.setMediaController(mediaController);
//        if (simpleVideoView.isPlaying()){2
//            simpleVideoView.setVisibility(View.GONE);
//            mainConstraint.setVisibility(View.VISIBLE);
//
//        }
//
//        else{
//            mainConstraint.setVisibility(View.GONE);
//
//        }


        rand = new Random();
        animList = new ArrayList<>();

        // value for endless loop of MediaPlayer
        sound.setIsActive(true);
        sound.playBackgroundMusic();

        fillBtnArray();
        btnSetOnClickListener();
        btnSetOnTouchListener();
        startGame();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getApplicationContext(), "Ready to Start <3", Toast.LENGTH_LONG).show();


            }
        }, 5000);
    }

    private void createPersonalizedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createInstestialAd(adRequest);


    }

    private void createInstestialAd(AdRequest adRequest) {
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/8691691433", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    private void displayColorPatternPop() {
        Button ok;
        View c1, c2;

        dialogColorPattern.setContentView(R.layout.colorpattern_instructions);
        dialogColorPattern.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ok = (Button) dialogColorPattern.findViewById(R.id.ok);
//        c1=(View)dialogColorPattern.findViewById(R.id.c1);
//        c2=(View)dialogColorPattern.findViewById(R.id.c2);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
//        colorreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("FirebaseColor PopUp", String.valueOf(snapshot.getValue()));
//                color = (int) snapshot.getValue(Integer.class);
//                Log.d("Color", String.valueOf(color));
//
//                if (color == 2) {  //light theme
//                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
//                    c2.setVisibility(View.VISIBLE);
//                } else if (color == 1) { //light theme
//
//                    c1.setVisibility(View.VISIBLE);
//                    c2.setVisibility(View.INVISIBLE);
//
//
//                } else {
//                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme
//
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
//                        c1.setVisibility(View.VISIBLE);
//                        c2.setVisibility(View.INVISIBLE);
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogColorPattern.dismiss();
                mainConstraint.setVisibility(View.VISIBLE);
                gameVideo.setVisibility(View.VISIBLE);
                mainConstraint.setVisibility(View.GONE);
                gameVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.colorgamefinal));
                gameVideo.start();

                gameVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        gameVideo.setVisibility(View.GONE);
                        mainConstraint.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        dialogColorPattern.show();

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsColIn.edit();
        editor.putBoolean("firstStartColIn", false);
        editor.apply();


    }

    @Override
    public void onClick(View view) {
        sound.playClickSound();
        clickedBtn = (Button) view;

        if (this.getIsGameStarted()) {
            Animator animator = animList.get(this.getCounter());
            ObjectAnimator checkAnimator = (ObjectAnimator) animator;
            Button correctBtn = (Button) checkAnimator.getTarget();

            if (clickedBtn == correctBtn) {
                gameInfo.setText(R.string.match);
                this.setActScore(this.getActScore() + 1);
                scoreInfo.setText(String.valueOf(this.getActScore()));
                scoreInfo2.setText(String.valueOf(this.getActScore()));


                if (this.getCounter() + 1 == this.getRound()) {
                    animateBtn();
                    this.setRound(this.getRound() + 1);
                    this.setCounter(0); // reset counter
                } else {
                    this.setCounter(this.getCounter() + 1);
                }
            } else {
                newHighscore();
                printHighscore();
                resetGame();
                openPopUpLineChart();
            }
        } else {
            showToast();
        }
    }

    // overwrite onPause to stop music, if activity_main is in the background
    @Override
    public void onPause() {
        super.onPause();
        sound.stopBackgroundMusic();
    }

    public void fillBtnArray() {
        btnArray = new Button[4];
        btnArray[0] = findViewById(R.id.redBtn);
        btnArray[1] = findViewById(R.id.blueBtn);
        btnArray[2] = findViewById(R.id.greenBtn);
        btnArray[3] = findViewById(R.id.yellowBtn);
    }

    // each button gets an OnClickListener
    public void btnSetOnClickListener() {
        for (int i = 0; i < btnArray.length; i++) {
            btnArray[i].setOnClickListener(this);
        }
    }

    // each button gets an OnTouchListener
    @SuppressLint("ClickableViewAccessibility")
    public void btnSetOnTouchListener() {
        for (int i = 0; i < btnArray.length; i++) {
            btnArray[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //view.getBackground().setColorFilter(getResources().getColor(R.color.background),PorterDuff.Mode.SRC_ATOP);
                            view.getBackground().setAlpha(128);
                            //view.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            //view.getBackground().clearColorFilter();
                            view.getBackground().setAlpha(255);
                            view.performClick();
                            //view.invalidate();
                            break;
                    }
                    return true;
                }
            });
        }
    }


    // configurations of animation
    public void configureAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(btnArray[this.getRandom()], "alpha", 0.3f);
        animation.setRepeatCount(1);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setDuration(300);
        animList.add(animation);
    }

    // show message of toast at a specific position
    public void showToast() {
        Toast toast = Toast.makeText(this, R.string.pushStart, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 50);
        toast.show();
    }

    // game is starting by clicking the start button
    public void startGame() {
        Button start = findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                animateBtn();
                round = round + 1;
                isGameStarted = true;
            }
        });
    }

    // animate buttons with AnimatorSet to play animation sequentially
    public void animateBtn() {
        this.setRandom(rand.nextInt(4));
        configureAnimation();

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);

        // addListener to avoid clicking buttons as long animation is running
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                for (int i = 0; i < btnArray.length; i++) {
                    btnArray[i].setEnabled(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                for (int i = 0; i < btnArray.length; i++) {
                    btnArray[i].setEnabled(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();
    }

    public void newHighscore() {
        if (this.getActScore() >= this.getHighScore()) {
            Toast toast = Toast.makeText(this, R.string.newHighscore, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 50);
            toast.show();
            this.setHighScore(this.getActScore());
        }
    }

//    private void timepopUpMenu() {
//        PopupMenu popupMenu=new PopupMenu(this);
//        popupMenu.inflate(R.menu.menu_info);
////        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.show();
//    }

    public void printHighscore() {
        gameInfo.setText(String.format("%s %s", getString(R.string.showHighscore), String.valueOf(this.getHighScore())));
        Log.d("Get High Score", String.valueOf(this.getHighScore()));
        highScore = this.getHighScore();

        database.collection("users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(highScore));
                updatedCoins = (int) (user.getCoins() + highScore);
                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                database.collection("users").document(mUser.getUid())
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


        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon

//set title
                .setTitle("You lost the Game")
//set message
                .setMessage("Do you want to start again?")
//set positive button
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(ColorPatternGame.this);
                        } else {
                            finish();
                            dialogInterface.dismiss();

                        }
                    }
                })
//set negative button
                .

                        setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                        openPopUpLineChart();
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(), "Press START button to start", Toast.LENGTH_LONG).show();
                            }
                        })
                .

                        show();

    }

    private void openPopUpLineChart() {
        Button ok;
        LineChart lineChart;
//        TextView points;
//        TextView totalPoints;

        dialogIntervention.setContentView(R.layout.color_pattern_intervention_chart);
        dialogIntervention.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lineChart = (LineChart) dialogIntervention.findViewById(R.id.lineChartColorP);
//        points=(TextView)dialogIntervention.findViewById(R.id.points);
//        totalPoints=(TextView)dialogIntervention.findViewById(R.id.total);

//        Log.d("highscore", String.valueOf(highScore));
//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user = documentSnapshot.toObject(User.class);
////
//                Log.d("Current Coins", String.valueOf(user.getCoins()));
////
//                totalPoints.setText("Total Coins: "+user.getCoins());
////
//
//            }
//        });


//        points.setText("Coins Earned:"+highScore);

        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "Memory Progress");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(10f);

        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.setBorderColor(Color.TRANSPARENT);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setTextColor(R.color.white);
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
        lineChart.getDescription().setTextColor(R.color.white);
        ok = (Button) dialogIntervention.findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIntervention.dismiss();
            }
        });

        dialogIntervention.show();

    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));

    }

    // if clicked button is incorrect, game will be reset
    public void resetGame() {
        this.setCounter(0);
        this.setRound(0);
        this.setActScore(0);
        this.setIsGameStarted(false);
        animList.clear(); // there is no animation in animList anymore
    }

    // set and get methods (data encapsulation)
    public void setIsGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public boolean getIsGameStarted() {
        return isGameStarted;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public int getRandom() {
        return random;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setActScore(int actScore) {
        this.actScore = actScore;
    }

    public int getActScore() {
        return actScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void animation(View view) {
        Button btn1 = (Button) findViewById(R.id.redBtn);
        Button btn2 = (Button) findViewById(R.id.yellowBtn);
        Button btn3 = (Button) findViewById(R.id.blueBtn);
        Button btn4 = (Button) findViewById(R.id.greenBtn);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        btn1.startAnimation(animation);
        btn2.startAnimation(animation);
        btn3.startAnimation(animation);
        btn4.startAnimation(animation);


    }

    @Override
    protected void onStart() {
        super.onStart();
        ConstraintLayout constraintLayout;
        constraintLayout = findViewById(R.id.mainConstraint);


    }
}