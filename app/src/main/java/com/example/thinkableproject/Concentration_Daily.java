package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SyncNotedAppOp;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.io.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class Concentration_Daily extends AppCompatActivity {
    Dialog dialogcd;
    BarChart barChartdaily;
    View c1, c2;
    Double sum = 0.0;
    Double total = 0.0;
    Double sum4 = 0.0;
    Double total4 = 0.0;
    Double sum1 = 0.0;
    Double total1 = 0.0;
    Double sum2 = 0.0;
    Double total2 = 0.0;
    Double sum3 = 0.0;
    Double total3 = 0.0;
    Double sum5 = 0.0;
    Double total5 = 0.0;
    Double sum6 = 0.0;
    Double total6 = 0.0;

    GifImageView c1gif, c2gif;
    ImageView games, relaxationBtn, memory, landingtwo, landingtwoday, musicNight, gameNight;
    AppCompatButton monthly, yearly, weekly,improveConcentration;
    ImageView music;
    FirebaseUser mUser;
    AppCompatButton progressTime, improvementChart;
    String text;
    LottieAnimationView anim;
    File localFile, fileName;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    int color;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;
    HorizontalScrollView scrollView;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Animation scaleUp, scaleDown;

    private Runnable runnable;
    private Handler handler2 = new Handler();
    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__daily);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        monthly = findViewById(R.id.monthly);
        games = findViewById(R.id.game);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        improveConcentration=findViewById(R.id.improveConcentration);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        music = findViewById(R.id.music);
        landingtwo = findViewById(R.id.landingtwo);
        landingtwoday = findViewById(R.id.landingtwoday);
        musicNight = findViewById(R.id.music1);
        gameNight = findViewById(R.id.game1);
        relaxationBtn = findViewById(R.id.relaxation);
        anim = findViewById(R.id.animation);
        List<BarEntry> entries = new ArrayList<>();
        dialogcd = new Dialog(this);
        memory = findViewById(R.id.memory);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartDaily);
        scrollView = findViewById(R.id.scroll);
        improvementChart = findViewById(R.id.improvement);
        progressTime = findViewById(R.id.progressTime);


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpleDateFormat.format(calendar.getTime());

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

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
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c1gif.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                        c1gif.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.INVISIBLE);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        improvementChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);


            }
        });
        improveConcentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improveConcentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improveConcentration.startAnimation(scaleDown);
                }

                return false;
            }
        });


        improvementChart.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improvementChart.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improvementChart.startAnimation(scaleDown);
                }

                return false;
            }
        });

        progressTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);

            }
        });

        progressTime.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    progressTime.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    progressTime.startAnimation(scaleDown);
                }

                return false;
            }
        });

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Memory_Daily.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        memory.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    memory.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    memory.startAnimation(scaleDown);
                }

                return false;
            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });

        //Initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Relaxation_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String[] days = new String[]{"", "Su", "Mn", "Tu", "We", "Th", "Fr", "Sa"};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));


        float textSize = 10f;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            Long average1, average2, average3, average4, average5, average6, average7;
            int sum1, sum2, sum3, sum4, sum5, sum6, sum7;

            @Override
            public void run() {

                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                reference0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                            Long av1 = (Long) dataSnapshot.getValue();
                            Log.d("AV1", String.valueOf(av1));
                            sumElement.add(av1);
                            sum1 += av1;

                        }
                        Log.d("SUM1", String.valueOf(sum1));
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("Average Mon", String.valueOf(average1));
                        } else {
                            average1 = Long.valueOf(0);
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                    Long av1 = (Long) dataSnapshot.getValue();
                                    Log.d("AV1", String.valueOf(av1));
                                    sumElement.add(av1);
                                    sum2 += av1;

                                }
                                Log.d("SUM2", String.valueOf(sum2));
                                if (sum2 != 0) {
                                    average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                    Log.d("Average Tue", String.valueOf(average2));

                                } else {
                                    average2 = Long.valueOf(0);
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                            Long av1 = (Long) dataSnapshot.getValue();
                                            Log.d("AV1", String.valueOf(av1));
                                            sumElement.add(av1);
                                            sum3 += av1;

                                        }
                                        Log.d("SUM3", String.valueOf(sum3));
                                        if (sum3 != 0) {
                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Average Wed", String.valueOf(average3));

                                        } else {
                                            average3 = Long.valueOf(0);
                                        }

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                        reference3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                    Long av1 = (Long) dataSnapshot.getValue();
                                                    Log.d("AV1", String.valueOf(av1));
                                                    sumElement.add(av1);
                                                    sum4 += av1;

                                                }
                                                Log.d("SUM4", String.valueOf(sum));
                                                if (sum4 != 0) {
                                                    average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                    Log.d("Average Thur", String.valueOf(average4));
                                                } else {
                                                    average4 = Long.valueOf(0);
                                                }
                                                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                                reference4.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                            Long av1 = (Long) dataSnapshot.getValue();
                                                            Log.d("AV1", String.valueOf(av1));
                                                            sumElement.add(av1);
                                                            sum5 += av1;

                                                        }
                                                        Log.d("SUM5", String.valueOf(sum5));
                                                        if (sum5 != 0) {
                                                            average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("Average Fri", String.valueOf(average5));
                                                        } else {
                                                            average5 = Long.valueOf(0);
                                                        }
                                                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                        reference5.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();
                                                                int sum = (0);
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                                    Long av1 = (Long) dataSnapshot.getValue();
                                                                    Log.d("AV1", String.valueOf(av1));
                                                                    sumElement.add(av1);
                                                                    sum6 += av1;

                                                                }
                                                                Log.d("SUM6", String.valueOf(sum6));
                                                                if (sum6 != 0) {
                                                                    average6 = sum6 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    Log.d("Average Sat", String.valueOf(average6));

                                                                } else {
                                                                    average6 = Long.valueOf(0);
                                                                }

                                                                DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                                                                reference6.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();
                                                                        int sum = (0);
                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                                            Long av1 = (Long) dataSnapshot.getValue();
                                                                            Log.d("AV1", String.valueOf(av1));
                                                                            sumElement.add(av1);
                                                                            sum7 += av1;

                                                                        }
                                                                        Log.d("SUM7", String.valueOf(sum7));
                                                                        if (sum7 != 0) {
                                                                            average7 = sum7 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("Average Sun", String.valueOf(average7));
                                                                        } else {
                                                                            average7 = Long.valueOf(0);
                                                                        }
                                                                        Log.d("Average Outside1", String.valueOf(average1));
                                                                        Log.d("Average Outside2", String.valueOf(average2));
                                                                        Log.d("Average Outside3", String.valueOf(average3));
                                                                        Log.d("Average Outside4", String.valueOf(average4));
                                                                        Log.d("Average Outside5", String.valueOf(average5));
                                                                        Log.d("Average Outside6", String.valueOf(average6));
                                                                        Log.d("Average Outside7", String.valueOf(average7));

                                                                        Log.d("SUM1 Outside1", String.valueOf(sum1));
                                                                        Log.d("SUM2 Outside2", String.valueOf(sum2));
                                                                        Log.d("SUM3 Outside3", String.valueOf(sum3));
                                                                        Log.d("SUM4 Outside4", String.valueOf(sum4));
                                                                        Log.d("SUM5 Outside5", String.valueOf(sum5));
                                                                        Log.d("SUM6 Outside6", String.valueOf(sum6));
                                                                        Log.d("SUM7 Outside7", String.valueOf(sum7));
                                                                        int sum1h = sum1 / 3600;
                                                                        Log.d("SUM1 in hours", String.valueOf(sum1h));
                                                                        float sum7min = TimeUnit.SECONDS.toMinutes(sum7);
                                                                        float sum1min = TimeUnit.SECONDS.toMinutes(sum1);
                                                                        float sum2min = TimeUnit.SECONDS.toMinutes(sum2);
                                                                        float sum3min = TimeUnit.SECONDS.toMinutes(sum3);
                                                                        float sum4min = TimeUnit.SECONDS.toMinutes(sum4);
                                                                        float sum5min = TimeUnit.SECONDS.toMinutes(sum5);
                                                                        float sum6min = TimeUnit.SECONDS.toMinutes(sum6);
                                                                        Log.d("SUM7 in hours", String.valueOf(sum7min));
                                                                        ArrayList<BarEntry> entries = new ArrayList();
                                                                        entries.add(new BarEntry(1, sum7min));
                                                                        entries.add(new BarEntry(2, sum1min));
                                                                        entries.add(new BarEntry(3, sum2min));
                                                                        entries.add(new BarEntry(4, sum3min));
                                                                        entries.add(new BarEntry(5, sum4min));
                                                                        entries.add(new BarEntry(6, sum5min));
                                                                        entries.add(new BarEntry(7, sum6min));


                                                                        //Initializing object of MyBarDataset class and passing th arraylist to y axis of chart
                                                                        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                                        BarData data = new BarData(dataSet);
                                                                        data.setDrawValues(false);
                                                                        data.setBarWidth(0.8f);

                                                                        barChartdaily.setData(data);
                                                                        barChartdaily.setFitBars(true);
                                                                        barChartdaily.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
                                                                        barChartdaily.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                        barChartdaily.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                        barChartdaily.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                        barChartdaily.getXAxis().setTextSize(textSize);
                                                                        barChartdaily.getAxisLeft().setTextSize(textSize);
                                                                        barChartdaily.setExtraBottomOffset(10f);
                                                                        barChartdaily.getXAxis().setLabelCount(16, true);
                                                                        barChartdaily.getXAxis().setAvoidFirstLastClipping(false);
                                                                        barChartdaily.getAxisRight().setEnabled(false);
                                                                        Description desc = new Description();
                                                                        desc.setText("Time Spent Daily on Concentration");
                                                                        desc.setTextColor(getResources().getColor(R.color.white));
                                                                        barChartdaily.setDescription(desc);
                                                                        barChartdaily.getLegend().setEnabled(false);
                                                                        barChartdaily.getXAxis().setDrawGridLines(false);
                                                                        barChartdaily.getAxisLeft().setDrawGridLines(false);
                                                                        barChartdaily.setNoDataText("Data Loading Please Wait...");
                                                                        barChartdaily.animateXY(1500, 1500);
                                                                        barChartdaily.invalidate();


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 3000);

        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();

        getEntries();


        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Monthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        monthly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    monthly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    monthly.startAnimation(scaleDown);
                }

                return false;
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        weekly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    weekly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    weekly.startAnimation(scaleDown);
                }

                return false;
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Yearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        yearly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    yearly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    yearly.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Daily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        relaxationBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    relaxationBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    relaxationBtn.startAnimation(scaleDown);
                }

                return false;
            }
        });
        // On click listener of real time indication button

        anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });
        anim.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    anim.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    anim.startAnimation(scaleDown);
                }

                return false;
            }
        });


    }

    private void getEntries() {
        Handler handler = new Handler();
        final int delay = 5000;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;

        handler.postDelayed(new Runnable() {
            Double average1, average2, average3, average4, average5, average6, average7;

            @Override
            public void run() {

                lineEntries = new ArrayList();

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesSun", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesDataSun", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataSun", String.valueOf(dataSnapshot2.getKey()));
                                    if (dataSnapshot2.getKey().equals("index")) {
                                        dataSnapshot2.getValue();
                                        Log.d("DataSun", String.valueOf(dataSnapshot2.getValue()));
                                        Double av1 = (Double) dataSnapshot2.getValue();
                                        sumElement.add(av1);
                                        sum1 += av1;

                                    }
                                }

                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalSun", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sum1 = 0.0;
                            average1 = 0.0;
                        } else {
                            Log.d("SUMSun", String.valueOf(total1));
                            average1 = total1 / sumElement.size();
                            Log.d("AverageData1", String.valueOf(average1));
                        }
                        Log.d("AverageData1", String.valueOf(average1));

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesSun", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesDataSun", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataSun", String.valueOf(dataSnapshot2.getKey()));
                                            if (dataSnapshot2.getKey().equals("index")) {
                                                dataSnapshot2.getValue();
                                                Log.d("DataSun", String.valueOf(dataSnapshot2.getValue()));
                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                sumElement.add(av1);
                                                sum2 += av1;

                                            }
                                        }

                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalMon", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sum2 = 0.0;
                                    average2 = 0.0;
                                } else {
                                    Log.d("SUMMon", String.valueOf(total2));
                                    average2 = total2 / sumElement.size();
                                    Log.d("AverageData2", String.valueOf(average2));
                                }
                                Log.d("AverageData2", String.valueOf(average2));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesTue", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesDataTue", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataTue", String.valueOf(dataSnapshot2.getKey()));
                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                        dataSnapshot2.getValue();
                                                        Log.d("DataTue", String.valueOf(dataSnapshot2.getValue()));
                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                        sumElement.add(av1);
                                                        sum3 += av1;

                                                    }
                                                }

                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalTue", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sum3 = 0.0;
                                            average3 = 0.0;
                                        } else {
                                            Log.d("SUMTue", String.valueOf(total3));
                                            average3 = total3 / sumElement.size();
                                            Log.d("AverageData3", String.valueOf(average3));
                                        }
                                        Log.d("AverageData3", String.valueOf(average3));


                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                                        reference2.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesData", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("Data2", String.valueOf(dataSnapshot2.getKey()));
                                                            if (dataSnapshot2.getKey().equals("index")) {
                                                                dataSnapshot2.getValue();
                                                                Log.d("DataF", String.valueOf(dataSnapshot2.getValue()));
                                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                                sumElement.add(av1);
                                                                sum += av1;

                                                            }
                                                        }

                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalDA", String.valueOf(total));

                                                if (total == 0.0) {
                                                    sum = 0.0;
                                                    average4 = 0.0;

                                                } else {
                                                    Log.d("SUMDA", String.valueOf(total));
                                                    average4 = total / sumElement.size();
                                                    Log.d("AverageData", String.valueOf(average4));
                                                }
                                                Log.d("AverageData", String.valueOf(average4));
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                                reference1.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("Values1", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("ValuesData1", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                    Log.d("Data21", String.valueOf(dataSnapshot2.getKey()));
                                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                                        dataSnapshot2.getValue();
                                                                        Log.d("DataF1", String.valueOf(dataSnapshot2.getValue()));
                                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                                        sumElement.add(av1);
                                                                        sum4 += av1;

                                                                    }
                                                                }

                                                            }
                                                        }
                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                            total4 += (Double) sumElement.get(i);
                                                        }
                                                        Log.d("TotalDA4", String.valueOf(total4));

                                                        if (total4 == 0.0) {
                                                            sum4 = 0.0;
                                                            average5 = 0.0;
                                                        } else {
                                                            Log.d("SUMDA4", String.valueOf(total4));
                                                            average5 = total4 / sumElement.size();
                                                            Log.d("AverageData4", String.valueOf(average4));
                                                        }
                                                        Log.d("AverageData4", String.valueOf(average4));
                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();

                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("Values5", String.valueOf(dataSnapshot.getValue()));
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("ValuesDataFri", String.valueOf(dataSnapshot1.getValue()));
                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                            Log.d("DataFri", String.valueOf(dataSnapshot2.getKey()));
                                                                            if (dataSnapshot2.getKey().equals("index")) {
                                                                                dataSnapshot2.getValue();
                                                                                Log.d("DataFri", String.valueOf(dataSnapshot2.getValue()));
                                                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                                                sumElement.add(av1);
                                                                                sum5 += av1;

                                                                            }
                                                                        }

                                                                    }
                                                                }
                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                    total5 += (Double) sumElement.get(i);
                                                                }
                                                                Log.d("TotalDA4", String.valueOf(total5));

                                                                if (total5 == 0.0) {
                                                                    sum5 = 0.0;
                                                                    average6 = 0.0;
                                                                } else {
                                                                    Log.d("SUMDA5", String.valueOf(total5));
                                                                    average6 = total5 / sumElement.size();
                                                                    Log.d("AverageData5", String.valueOf(average5));
                                                                }
                                                                Log.d("AverageData5", String.valueOf(average5));

                                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                                reference1.addValueEventListener(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("Values6", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("ValuesDataSat", String.valueOf(dataSnapshot1.getValue()));
                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                    Log.d("DataSat", String.valueOf(dataSnapshot2.getKey()));
                                                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                                                        dataSnapshot2.getValue();
                                                                                        Log.d("DataSat", String.valueOf(dataSnapshot2.getValue()));
                                                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                                                        sumElement.add(av1);
                                                                                        sum6 += av1;

                                                                                    }
                                                                                }

                                                                            }
                                                                        }
                                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                                            total6 += (Double) sumElement.get(i);
                                                                        }
                                                                        Log.d("TotalDA4", String.valueOf(total6));

                                                                        if (total6 == 0.0) {
                                                                            sum6 = 0.0;
                                                                            average7 = 0.0;
                                                                        } else {
                                                                            Log.d("SUMDA6", String.valueOf(total6));
                                                                            average7 = total6 / sumElement.size();
                                                                            Log.d("AverageData6", String.valueOf(average6));
                                                                        }
                                                                        Log.d("AverageData6", String.valueOf(average6));
                                                                        Log.d("Average Outside1 Con", String.valueOf(average7));
                                                                        Log.d("Average Outside2 Con", String.valueOf(average1));
                                                                        Log.d("Average Outside3 Con", String.valueOf(average2));
                                                                        Log.d("Average Outside4 Con", String.valueOf(average3));
                                                                        Log.d("Average Outside5 Con", String.valueOf(average4));
                                                                        Log.d("Average Outside6 Con", String.valueOf(average5));
                                                                        Log.d("Average Outside7 Con", String.valueOf(average6));
                                                                        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
//
//
                                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(average1))));
                                                                        Log.d("FloatAverage", String.valueOf(Float.parseFloat(String.valueOf(average1))));
                                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(average2))));
                                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(average3))));
                                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(average4))));
                                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(average5))));
                                                                        lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(average6))));
                                                                        lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(average7))));
                                                                        List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", ""));

                                                                        lineDataSet = new LineDataSet(lineEntries, "Concentration Daily Progress");
                                                                        lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

                                                                        lineData = new LineData(lineDataSet);
                                                                        lineChart.setData(lineData);

                                                                        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                                                        lineDataSet.setValueTextColor(Color.WHITE);
                                                                        lineDataSet.setValueTextSize(6f);

                                                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                        lineChart.getXAxis().setTextSize(8f);
                                                                        lineChart.setBorderColor(Color.TRANSPARENT);
                                                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                        lineChart.getAxisLeft().setDrawGridLines(false);
                                                                        lineChart.getXAxis().setDrawGridLines(false);
                                                                        lineChart.getAxisRight().setDrawGridLines(false);
//                                                                        lineChart.getXAxis().setLabelCount(7, true);
                                                                        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                                                        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                                                        lineChart.setTouchEnabled(true);
                                                                        lineChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                        lineChart.setDragEnabled(true);
                                                                        lineChart.setScaleEnabled(false);
                                                                        lineChart.setPinchZoom(false);
                                                                        lineChart.setDrawGridBackground(false);
                                                                        lineChart.setExtraBottomOffset(5f);
                                                                        lineChart.getXAxis().setLabelCount(13, true);
                                                                        lineChart.getXAxis().setAvoidFirstLastClipping(true);
                                                                        lineChart.getDescription().setTextColor(R.color.white);
                                                                        lineChart.invalidate();
                                                                        lineChart.refreshDrawableState();
                                                                        XAxis xAxis = lineChart.getXAxis();
                                                                        xAxis.setGranularity(1f);
                                                                        xAxis.setCenterAxisLabels(true);
                                                                        xAxis.setEnabled(true);
                                                                        xAxis.setDrawGridLines(false);
                                                                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
                                                                    }

                                                                    //
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
//

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
//
//
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

//
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
//
//
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//
//
                            }

                            //
                            //
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 10000);

    }

    // Popup window method for suggestions to improve concentration
    public void gotoPopup1(View view) {

        startActivity(new Intent(getApplicationContext(), Connection.class));
//        Intent intentgp1 = new Intent(Concentration_Daily.this, Concentration_popup.class);
//
//        startActivity(intentgp1);
//        ImageButton cancelcon, games, music1;
//        View c1, c2;
//        FirebaseUser mUser;
//
//
//        dialogcd.setContentView(R.layout.activity_concentration_popup);
//
//        games = (ImageButton) dialogcd.findViewById(R.id.gamespop1);
//        music1 = (ImageButton) dialogcd.findViewById(R.id.musicpop1);
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        c1 = (View) dialogcd.findViewById(R.id.c1);
//        c2 = (View) dialogcd.findViewById(R.id.c2);
//
//
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
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
//
//
//        games.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        music1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Music.class);
//                startActivity(intent);
//            }
//        });
//
//        cancelcon = (ImageButton) dialogcd.findViewById(R.id.canclepop1);
//        cancelcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogcd.dismiss();
//            }
//        });
//        dialogcd.show();

    }


    public void calidaily(View view) {
        Intent intentcd = new Intent(Concentration_Daily.this, Calibration.class);

        startActivity(intentcd);
    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, ArrayList<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        @Override
        public int getColor(int index) {
            float c = credits.get(index);

            if (c > 80) {
                return mColors.get(0);
            } else if (c > 60) {
                return mColors.get(1);
            } else if (c > 40) {
                return mColors.get(2);
            } else if (c > 20) {
                return mColors.get(3);
            } else {
                return mColors.get(4);
            }

        }
    }

    private class MyXAxisValueFormatter extends ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
            Log.d("Tag", "monthly clicked hi");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public void monthly(View v) {
        Intent intent2 = new Intent(this, Concentration_Monthly.class);
        startActivity(intent2);

    }

    public void yearly(View view) {
        Intent intent2 = new Intent(this, Concentration_Yearly.class);
        startActivity(intent2);
    }

    public void weekly(View view) {
        Intent intent2 = new Intent(this, Concentration_Weekly.class);
        startActivity(intent2);
    }

//    @Override
//    protected void onDestroy() {
//        Log.d("ONDESTROY  ","Cache  ");
//        super.onDestroy();
//        try {
//            trimCache(this);
//            Log.d("DELETE ","Cache Deleted");
//            // Toast.makeText(this,"onDestroy " ,Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Log.d("NOT DELETE ","Cache NOT Deleted");
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }


}