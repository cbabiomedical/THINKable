package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class Concentration_Monthly extends AppCompatActivity {
    Dialog dialogcm;
    BarChart barChart;
    private Context context;
    ImageView games, music;
    AppCompatButton daily, weekly, yearly;
    ImageView relaxationBtn, memory;
    FirebaseUser mUser;
    File localFile, fileName;
    LottieAnimationView anim;
    GifImageView c1gif, c2gif;
    String text;
    int color;
    HorizontalScrollView scrollView;
    View c1, c2;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    AppCompatButton progressTime, improvement;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();
    Long average1, average2, average3, average4, average5, average6, average7, average8, average9, average10, average11, average12;
    int sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10, sum11, sum12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__monthly);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        yearly = findViewById(R.id.yearly);
        barChart = (BarChart) findViewById(R.id.barChartMonthly);
        relaxationBtn = findViewById(R.id.relaxation);
        List<BarEntry> entries = new ArrayList<>();
        dialogcm = new Dialog(this);
        memory = findViewById(R.id.memory);
        games = findViewById(R.id.game);
        music = findViewById(R.id.music);
        anim = findViewById(R.id.animation);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartMonthly);
        scrollView = findViewById(R.id.scroll);
        progressTime = findViewById(R.id.progressTime);
        improvement = findViewById(R.id.improvement);
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
        progressTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            }
        });
        improvement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        getEntries();

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        //Initializing bottom navigation bar
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
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());

        //getting current user id from Firebase User class
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        String[] months = new String[]{"","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 56f, 87f, 12f, 54f, 04f, 45f, 65f, 34f));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(1));

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                    Long av1 = (Long) snapshot2.getValue();
                                    sumElement.add(snapshot2.getValue());
                                    sum1 += av1;
                                }
                            }
                        }
                        Log.d("Monthly Array", String.valueOf(sumElement));
                        Log.d("SUM", String.valueOf(sum1));
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(2));

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                            Long av1 = (Long) snapshot2.getValue();
                                            sumElement.add(snapshot2.getValue());
                                            sum2 += av1;
                                        }
                                    }
                                }
                                Log.d("Monthly Array", String.valueOf(sumElement));
                                Log.d("SUM", String.valueOf(sum2));
                                if (sum2 != 0) {
                                    average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(3));

                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                    Long av1 = (Long) snapshot2.getValue();
                                                    sumElement.add(snapshot2.getValue());
                                                    sum3 += av1;
                                                }
                                            }
                                        }
                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                        Log.d("SUM", String.valueOf(sum3));
                                        if (sum3 != 0) {
                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(4));

                                        reference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                            Long av1 = (Long) snapshot2.getValue();
                                                            sumElement.add(snapshot2.getValue());
                                                            sum4 += av1;
                                                        }
                                                    }
                                                }
                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum4 != 0) {
                                                    average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average4 = 0L;
                                                }
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(5));

                                                reference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                    Long av1 = (Long) snapshot2.getValue();
                                                                    sumElement.add(snapshot2.getValue());
                                                                    sum5 += av1;
                                                                }
                                                            }
                                                        }
                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                        Log.d("SUM", String.valueOf(sum5));
                                                        if (sum5 != 0) {
                                                            average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average5 = 0L;
                                                        }

                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(6));

                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();
                                                                int sum = (0);
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                            Long av1 = (Long) snapshot2.getValue();
                                                                            sumElement.add(snapshot2.getValue());
                                                                            sum6 += av1;
                                                                        }
                                                                    }
                                                                }
                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                Log.d("SUM", String.valueOf(sum6));
                                                                if (sum6 != 0) {
                                                                    average6 = sum6 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                } else {
                                                                    average6 = 0L;
                                                                }
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(7));

                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();
                                                                        int sum = (0);
                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                    Long av1 = (Long) snapshot2.getValue();
                                                                                    sumElement.add(snapshot2.getValue());
                                                                                    sum7 += av1;
                                                                                }
                                                                            }
                                                                        }
                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                        Log.d("SUM", String.valueOf(sum7));
                                                                        if (sum7 != 0) {
                                                                            average7 = sum7 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                        } else {
                                                                            average7 = 0L;
                                                                        }
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                .child(String.valueOf(8));

                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                ArrayList sumElement = new ArrayList();
                                                                                int sum = (0);
                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                            Long av1 = (Long) snapshot2.getValue();
                                                                                            sumElement.add(snapshot2.getValue());
                                                                                            sum8 += av1;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                Log.d("SUM", String.valueOf(sum8));
                                                                                if (sum8 != 0) {
                                                                                    average8 = sum8 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                } else {
                                                                                    average8 = 0L;
                                                                                }
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                        .child(String.valueOf(9));

                                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        ArrayList sumElement = new ArrayList();

                                                                                        int sum = (0);
                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                    Long av1 = (Long) snapshot2.getValue();
                                                                                                    sumElement.add(snapshot2.getValue());
                                                                                                    sum9 += av1;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                        Log.d("SUM", String.valueOf(sum9));
                                                                                        if (sum9 != 0) {
                                                                                            average9 = sum9 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                        } else {
                                                                                            average9 = 0L;
                                                                                        }
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                .child(String.valueOf(10));

                                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                ArrayList sumElement = new ArrayList();
                                                                                                int sum = (0);
                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                            Long av1 = (Long) snapshot2.getValue();
                                                                                                            sumElement.add(snapshot2.getValue());
                                                                                                            sum10 += av1;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                Log.d("SUM", String.valueOf(sum10));
                                                                                                if (sum10 != 0) {
                                                                                                    average10 = sum10 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                } else {
                                                                                                    average10 = 0L;
                                                                                                }
                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                        .child(String.valueOf(11));

                                                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                        ArrayList sumElement = new ArrayList();
                                                                                                        int sum = (0);
                                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                            Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                    Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                    Long av1 = (Long) snapshot2.getValue();
                                                                                                                    sumElement.add(snapshot2.getValue());
                                                                                                                    sum11 += av1;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                        Log.d("SUM", String.valueOf(sum11));
                                                                                                        if (sum11 != 0) {
                                                                                                            average11 = sum11 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                        } else {
                                                                                                            average11 = 0L;
                                                                                                        }
                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                                .child(String.valueOf(12));

                                                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                ArrayList sumElement = new ArrayList();
                                                                                                                int sum = (0);
                                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                    Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                        Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                            Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                            Long av1 = (Long) snapshot2.getValue();
                                                                                                                            sumElement.add(snapshot2.getValue());
                                                                                                                            sum12 += av1;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                                                if (sum12 != 0) {
                                                                                                                    average12 = sum12 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                } else {
                                                                                                                    average12 = 0L;
                                                                                                                }
                                                                                                                Log.d("Average Jan", String.valueOf(average1));
                                                                                                                Log.d("Average Feb", String.valueOf(average2));
                                                                                                                Log.d("Average Mar", String.valueOf(average3));
                                                                                                                Log.d("Average Apr", String.valueOf(average4));
                                                                                                                Log.d("Average May", String.valueOf(average5));
                                                                                                                Log.d("Average Jun", String.valueOf(average6));
                                                                                                                Log.d("Average Jul", String.valueOf(average7));
                                                                                                                Log.d("Average Aug", String.valueOf(average8));
                                                                                                                Log.d("Average Sep", String.valueOf(average9));
                                                                                                                Log.d("Average Oct", String.valueOf(average10));
                                                                                                                Log.d("Average Nov", String.valueOf(average11));
                                                                                                                Log.d("Average Dec", String.valueOf(average12));
                                                                                                                float sum1min = TimeUnit.SECONDS.toMinutes(sum1);
                                                                                                                float sum2min = TimeUnit.SECONDS.toMinutes(sum2);
                                                                                                                float sum3min = TimeUnit.SECONDS.toMinutes(sum3);
                                                                                                                float sum4min = TimeUnit.SECONDS.toMinutes(sum4);
                                                                                                                float sum5min = TimeUnit.SECONDS.toMinutes(sum5);
                                                                                                                float sum6min = TimeUnit.SECONDS.toMinutes(sum6);
                                                                                                                float sum7min = TimeUnit.SECONDS.toMinutes(sum7);
                                                                                                                float sum8min = TimeUnit.SECONDS.toMinutes(sum8);
                                                                                                                float sum9min = TimeUnit.SECONDS.toMinutes(sum9);
                                                                                                                float sum10min = TimeUnit.SECONDS.toMinutes(sum10);
                                                                                                                float sum11min = TimeUnit.SECONDS.toMinutes(sum11);
                                                                                                                float sum12min = TimeUnit.SECONDS.toMinutes(sum12);

                                                                                                                entries.add(new BarEntry(1, sum1min));
                                                                                                                entries.add(new BarEntry(2, sum2min));
                                                                                                                entries.add(new BarEntry(3, sum3min));
                                                                                                                entries.add(new BarEntry(4, sum4min));
                                                                                                                entries.add(new BarEntry(5, sum5min));
                                                                                                                entries.add(new BarEntry(6, sum6min));
                                                                                                                entries.add(new BarEntry(7, sum7min));
                                                                                                                entries.add(new BarEntry(8, sum8min));
                                                                                                                entries.add(new BarEntry(9, sum9min));
                                                                                                                entries.add(new BarEntry(10, sum10min));
                                                                                                                entries.add(new BarEntry(11, sum11min));
                                                                                                                entries.add(new BarEntry(12, sum12min));


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

                                                                                                                barChart.setData(data);
                                                                                                                barChart.setFitBars(true);
                                                                                                                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                                                                                                                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                                                                barChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart.getXAxis().setTextSize(8f);
                                                                                                                barChart.getAxisLeft().setTextSize(6f);
                                                                                                                barChart.getXAxis().setLabelCount(25, true);
                                                                                                                barChart.setExtraBottomOffset(3f);



                                                                                                                barChart.getAxisRight().setEnabled(false);
                                                                                                                Description desc = new Description();
                                                                                                                desc.setText("Time Spent Monthly on Concentration");
                                                                                                                desc.setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart.setDescription(desc);
                                                                                                                barChart.getLegend().setEnabled(false);
                                                                                                                barChart.getXAxis().setDrawGridLines(false);
                                                                                                                barChart.getAxisLeft().setDrawGridLines(false);
                                                                                                                barChart.setNoDataText("Data Loading Please Wait...");
                                                                                                                barChart.animateXY(1500, 1500);
                                                                                                                barChart.invalidate();


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


//                Log.d("Line Entry", String.valueOf(lineEntries));


            }
        }, 3000);

        try {
            fileName = new File(getCacheDir() + "/monthlyX.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = xVal.size();
            for (int i = 0; i < size; i++) {
                output.write(xVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing X Data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            Toast.makeText(this, "Failed Writing X Data", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//
//        // Uploading saved data containing file to firebase storage
        StorageReference storageXAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageXAxis.child("monthlyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Concentration_Monthly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Concentration_Monthly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/monthlyY.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = yVal.size();
            for (int i = 0; i < size; i++) {
                output.write(yVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing Y data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        StorageReference storageYAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageYAxis.child("monthlyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Concentration_Monthly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Concentration_Monthly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Daily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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


        //  // On click listener of relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Monthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MemoryMonthly.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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


    }

    private void getEntries() {
        Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                lineEntries = new ArrayList();
//        Handler handler1=new Handler();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/monthlyX.txt");
                //Downloading file from firebase and storing data into a tempFile in cache memory
                try {
                    localFile = File.createTempFile("tempFileX", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Concentration_Monthly.this, "Success", Toast.LENGTH_SHORT).show();

                            // reading data from the tempFile and storing in array list

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                Log.d("FileName", localFile.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    xnewVal.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    xnewVal.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("X New Val", String.valueOf(xnewVal));
                                //Converting string arraylist to float array list
                                for (int i = 0; i < xnewVal.size(); i++) {
                                    floatxVal.add(Float.parseFloat(xnewVal.get(i)));
                                    Log.d("FloatXVal", String.valueOf(floatxVal));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/monthlyY.txt");
//        //Downloading file from firebase and storing data into a tempFile in cache memory
                            try {
                                localFile = File.createTempFile("tempFileY", ".txt");
                                text = localFile.getAbsolutePath();
                                Log.d("Bitmap", text);
                                storageReference1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Concentration_Daily.this, "Success", Toast.LENGTH_SHORT).show();

                                        // reading data from the tempFile and storing in array list

                                        try {
                                            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                            Log.d("FileName", localFile.getAbsolutePath());

                                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                            String line = "";

                                            Log.d("First", line);
                                            if ((line = bufferedReader.readLine()) != null) {
                                                ynewVal.add(line);
                                            }
                                            while ((line = bufferedReader.readLine()) != null) {

                                                ynewVal.add(line);
                                                Log.d("Line", line);
                                            }

                                            Log.d("YVal", String.valueOf(ynewVal));
                                            //Converting string arraylist to float array list
                                            for (int i = 0; i < ynewVal.size(); i++) {
                                                floatyVal.add(Float.parseFloat(ynewVal.get(i)));
                                                Log.d("OutX", String.valueOf(floatxVal));
                                                Log.d("FloatYArray", String.valueOf(floatyVal));
                                                Log.d("OutY", String.valueOf(floatyVal));


                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("floatYVal", String.valueOf(floatyVal));

                                        for (int x = 0; x < floatxVal.size(); x++) {

                                            lineEntries.add(new Entry(floatxVal.get(x), floatyVal.get(x)));

                                        }
                                        Log.d("Line Entry", String.valueOf(lineEntries));
                                        lineDataSet = new LineDataSet(lineEntries, "Monthly Concentration Index");
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
                                        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getDescription().setTextColor(R.color.white);
                                        lineChart.invalidate();
                                        lineChart.refreshDrawableState();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Concentration_Daily.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Concentration_Monthly.this, "Failed X", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }, 10000);

    }

    //popup window method to display suggestions to improve concentration
    public void gotoPopup2(View view) {
        startActivity(new Intent(getApplicationContext(), Connection.class));
//        ImageButton cancelcon, games, music1;
//        View c1, c2;
//        FirebaseUser mUser;
//
//
//        dialogcm.setContentView(R.layout.activity_concentration_popup);
//
//        games = (ImageButton) dialogcm.findViewById(R.id.gamespop1);
//        music1 = (ImageButton) dialogcm.findViewById(R.id.musicpop1);
//
//        c1 = (View) dialogcm.findViewById(R.id.c1);
//        c2 = (View) dialogcm.findViewById(R.id.c2);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
//        cancelcon = (ImageButton) dialogcm.findViewById(R.id.canclepop1);
//        cancelcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogcm.dismiss();
//            }
//        });
//        dialogcm.show();

    }

    public void calimonthly(View view) {
        Intent intentcm = new Intent(Concentration_Monthly.this, Calibration.class);

        startActivity(intentcm);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            deleteTempFiles(getCacheDir());
        }
    }

    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }

    public void calidaily(View view) {
    }

    public void gotoPopup1(View view) {
    }

    public void gotoPopup3(View view) {
    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
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


}