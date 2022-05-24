package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.thinkableproject.adapters.Adapter;
import com.example.thinkableproject.adapters.MemoryAdapter;
import com.example.thinkableproject.models.MemoryInterventionClass;
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

public class Memory_Daily extends AppCompatActivity implements MemoryAdapter.OnNoteListner {

    Dialog dialogcd;
    BarChart barChartdaily;
    AppCompatButton monthly, yearly, weekly, improveConcentration;
    LottieAnimationView realTime;
    ImageView relaxationBtn, concentrationBtn;
    ImageView games, meditation, music;
    View c1, c2;
    Double sum = 0.0;

    Double sum4 = 0.0;
    Double sum1 = 0.0;
    Double sum2 = 0.0;
    Double sum3 = 0.0;
    Double sum5 = 0.0;
    Double total5 = 0.0;
    Double sum6 = 0.0;
    Double total6 = 0.0;
    HorizontalScrollView scrollView;
    GifImageView c1gif, c2gif;
    Animation scaleUp, scaleDown;


    FirebaseUser mUser;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<MemoryInterventionClass> memoryInterventionClassList;
    MemoryAdapter memoryAdapter;
    String text;
    File localFile, fileName;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    int color;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_memory);
        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        monthly = findViewById(R.id.monthly);
        games = findViewById(R.id.game);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        improveConcentration = findViewById(R.id.improveConcentration);

        music = findViewById(R.id.music);
        meditation = findViewById(R.id.meditations);
        realTime = findViewById(R.id.animation);
        relaxationBtn = findViewById(R.id.relaxation);
        List<BarEntry> entries = new ArrayList<>();
        dialogcd = new Dialog(this);
        concentrationBtn = findViewById(R.id.concentration);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartDaily);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scrollView = findViewById(R.id.scroll);
        progressTime = findViewById(R.id.progressTime);
        improvement = findViewById(R.id.improvement);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        improvement.setOnClickListener(new View.OnClickListener() {
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

        improvement.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improvement.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improvement.startAnimation(scaleDown);
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

        getEntries();


//        initData();
//        initRecyclerView();


        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeditationMemory.class));
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

        //Initializing arraylist and storing data in arraylist
//        ArrayList<Float> obj = new ArrayList<>(
//                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));
//
//        //  Creating txt file and writing data in array list to file
//        try {
//            fileName = new File(getCacheDir() + "/memDaily.txt");  //Writing data to file
//            FileWriter fw;
//            fw = new FileWriter(fileName);
//            BufferedWriter output = new BufferedWriter(fw);
//            int size = obj.size();
//            for (int i = 0; i < size; i++) {
//                output.write(obj.get(i).toString() + "\n");
////                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
//            }
//            output.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }


        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();

        // Uploading saved data containing file to firebase storage
//        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
//        try {
//            StorageReference mountainsRef = storageReference1.child("memDaily.txt");
//            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
//            UploadTask uploadTask = mountainsRef.putStream(stream);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(Concentration_Daily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(Concentration_Daily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        //Providing handler to delay the process of displaying
        final Handler handler = new Handler();
        final int delay = 5000;
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
        String[] days = new String[]{"", "Su", "Mn", "Tu", "We", "Th", "Fr", "Sa"};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));

        handler.postDelayed(new Runnable() {
            Long average1, average2, average3, average4, average5, average6, average7;
            int sum1, sum2, sum3, sum4, sum5, sum6, sum7;


            @Override
            public void run() {

                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                        Log.d("SUM", String.valueOf(sum1));
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("Average Mon", String.valueOf(average1));
                        } else {
                            average1 = Long.valueOf(0);
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                Log.d("SUM", String.valueOf(sum2));
                                if (sum2 != 0) {
                                    average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                    Log.d("Average Tue", String.valueOf(average2));

                                } else {
                                    average2 = Long.valueOf(0);
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                        Log.d("SUM", String.valueOf(sum3));
                                        if (sum3 != 0) {
                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Average Wed", String.valueOf(average3));

                                        } else {
                                            average3 = Long.valueOf(0);
                                        }

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                Log.d("SUM", String.valueOf(sum4));
                                                if (sum4 != 0) {
                                                    average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                    Log.d("Average Thur", String.valueOf(average4));
                                                } else {
                                                    average4 = Long.valueOf(0);
                                                }
                                                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                        Log.d("SUM", String.valueOf(sum5));
                                                        if (sum5 != 0) {
                                                            average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("Average Fri", String.valueOf(average5));
                                                        } else {
                                                            average5 = Long.valueOf(0);
                                                        }
                                                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                Log.d("SUM", String.valueOf(sum6));
                                                                if (sum6 != 0) {
                                                                    average6 = sum6 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    Log.d("Average Sat", String.valueOf(average6));

                                                                } else {
                                                                    average6 = Long.valueOf(0);
                                                                }

                                                                DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                        Log.d("SUM", String.valueOf(sum7));
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

                                                                        float sum7min = TimeUnit.SECONDS.toMinutes(sum7);
                                                                        float sum1min = TimeUnit.SECONDS.toMinutes(sum1);
                                                                        float sum2min = TimeUnit.SECONDS.toMinutes(sum2);
                                                                        float sum3min = TimeUnit.SECONDS.toMinutes(sum3);
                                                                        float sum4min = TimeUnit.SECONDS.toMinutes(sum4);
                                                                        float sum5min = TimeUnit.SECONDS.toMinutes(sum5);
                                                                        float sum6min = TimeUnit.SECONDS.toMinutes(sum6);
                                                                        Log.d("SUN in hours", String.valueOf(sum7min));
                                                                        Log.d("MON in hours", String.valueOf(sum1min));
                                                                        Log.d("TUE in hours", String.valueOf(sum2min));
                                                                        Log.d("WED in hours", String.valueOf(sum3min));
                                                                        Log.d("THU in hours", String.valueOf(sum4min));
                                                                        Log.d("FRI in hours", String.valueOf(sum5min));
                                                                        Log.d("SAT in hours", String.valueOf(sum6min));
                                                                        ArrayList<BarEntry> entries = new ArrayList();
                                                                        entries.add(new BarEntry(1, sum7min));
                                                                        entries.add(new BarEntry(2, sum1min));
                                                                        entries.add(new BarEntry(3, sum2min));
                                                                        entries.add(new BarEntry(4, sum3min));
                                                                        entries.add(new BarEntry(5, sum4min));
                                                                        entries.add(new BarEntry(6, sum5min));
                                                                        entries.add(new BarEntry(7, sum6min));


                                                                        float textSize = 10f;
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


                                                                        barChartdaily.getAxisRight().setEnabled(false);
                                                                        Description desc = new Description();
                                                                        desc.setText("Time Spent Daily on Memory");
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

        try {
            fileName = new File(getCacheDir() + "/memdailyX.txt");  //Writing data to file
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
//            Toast.makeText(this, "Failed Writing X Data", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//
//        // Uploading saved data containing file to firebase storage
        StorageReference storageXAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageXAxis.child("memdailyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Memory_Daily.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Memory_Daily.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/memdailyY.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageYAxis.child("memdailyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Memory_Daily.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Memory_Daily.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoryMonthly.class);
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
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memory_Weekly.class);
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
                Intent intent = new Intent(getApplicationContext(), GamesMemory.class);
                startActivity(intent);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memory_Yearly.class);
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

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MusicMemory.class));
            }
        });
        // On click listener of real time indication button
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });

        realTime.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    realTime.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    realTime.startAnimation(scaleDown);
                }

                return false;
            }
        });
        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        concentrationBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    concentrationBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    concentrationBtn.startAnimation(scaleDown);
                }

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getEntries() {
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
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            Double average, average1, average2, average3, average4, average5, average6;

            @Override
            public void run() {

                lineEntries = new ArrayList();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                            Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                            dataSnapshot.getValue();
                            Double av1 = (Double) dataSnapshot.getValue();
                            sum += av1;
                            Log.d("DataSun", String.valueOf(dataSnapshot.getValue()));
                            sumElement.add(av1);

                        }
                        if (sum != 0.0) {
                            average = sum / sumElement.size();
                            Log.d("AverageSunData", String.valueOf(average));

                        } else {
                            sum = 0.0;
                            average = 0.0;
                        }

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                        reference2.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                    Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                    dataSnapshot.getValue();
                                    Double av1 = (Double) dataSnapshot.getValue();
                                    sum1 += av1;
                                    Log.d("DataMon", String.valueOf(dataSnapshot.getValue()));

                                    sumElement.add(av1);

                                }
                                if (sum1 != 0.0) {
                                    average1 = sum1 / sumElement.size();
                                    Log.d("AverageWedData", String.valueOf(average1));

                                } else {
                                    sum1 = 0.0;
                                    average1 = 0.0;
                                }


                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                                reference2.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                            Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                            dataSnapshot.getValue();
                                            Double av1 = (Double) dataSnapshot.getValue();
                                            sum2 += av1;
                                            Log.d("DataTue", String.valueOf(dataSnapshot.getValue()));

                                            sumElement.add(av1);

                                        }
                                        if (sum2 != 0.0) {
                                            average2 = sum2 / sumElement.size();
                                            Log.d("AverageTueData", String.valueOf(average2));

                                        } else {
                                            sum2 = 0.0;
                                            average2 = 0.0;
                                        }


                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                                        reference2.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                    Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                                    dataSnapshot.getValue();
                                                    Double av1 = (Double) dataSnapshot.getValue();
                                                    sum3 += av1;
                                                    Log.d("DataFTHUR", String.valueOf(dataSnapshot.getValue()));

                                                    sumElement.add(av1);

                                                }
                                                if (sum3 != 0.0) {
                                                    average3 = sum3 / sumElement.size();
                                                    Log.d("AverageWedData", String.valueOf(average3));

                                                } else {
                                                    sum3 = 0.0;
                                                    average3 = 0.0;
                                                }
                                                Log.d("AverageData", String.valueOf(average3));
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                                reference2.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                            Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                                            dataSnapshot.getValue();
                                                            Double av1 = (Double) dataSnapshot.getValue();
                                                            sum4 += av1;
                                                            Log.d("DataThu", String.valueOf(dataSnapshot.getValue()));

                                                            sumElement.add(av1);

                                                        }
                                                        if (sum4 != 0.0) {
                                                            average4 = sum4 / sumElement.size();
                                                            Log.d("AverageThuData", String.valueOf(average4));

                                                        } else {
                                                            sum4 = 0.0;
                                                            average4 = 0.0;
                                                        }
                                                        Log.d("AverageData", String.valueOf(average4));
                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                                        reference2.addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();

                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                                    Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                                                    dataSnapshot.getValue();
                                                                    Double av1 = (Double) dataSnapshot.getValue();
                                                                    sum5 += av1;
                                                                    Log.d("DataFri", String.valueOf(dataSnapshot.getValue()));

                                                                    sumElement.add(av1);

                                                                }
                                                                if (sum5 != 0.0) {
                                                                    average5 = sum5 / sumElement.size();
                                                                    Log.d("AverageFriData", String.valueOf(average5));

                                                                } else {
                                                                    sum5 = 0.0;
                                                                    average5 = 0.0;
                                                                }
                                                                Log.d("AverageData", String.valueOf(average5));
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                                reference2.addValueEventListener(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                                            Log.d("Data2", String.valueOf(dataSnapshot.getKey()));
                                                                            dataSnapshot.getValue();
                                                                            Double av1 = (Double) dataSnapshot.getValue();
                                                                            sum6 += av1;
                                                                            Log.d("DataSat", String.valueOf(dataSnapshot.getValue()));

                                                                            sumElement.add(av1);

                                                                        }
                                                                        if (sum6 != 0.0) {
                                                                            average6 = sum6 / sumElement.size();
                                                                            Log.d("AverageSatDat", String.valueOf(average6));

                                                                        } else {
                                                                            sum6 = 0.0;
                                                                            average6 = 0.0;
                                                                        }
                                                                        Log.d("AverageData", String.valueOf(average6));


                                                                        Log.d("AverageData6", String.valueOf(average6));
                                                                        Log.d("Average Outside2 Con", String.valueOf(average1));
                                                                        Log.d("Average Outside3 Con", String.valueOf(average2));
                                                                        Log.d("Average Outside4 Con", String.valueOf(average3));
                                                                        Log.d("Average Outside5 Con", String.valueOf(average4));
                                                                        Log.d("Average Outside6 Con", String.valueOf(average5));
                                                                        Log.d("Average Outside7 Con", String.valueOf(average6));
                                                                        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
//
//
                                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(average))));
                                                                        Log.d("FloatAverage", String.valueOf(Float.parseFloat(String.valueOf(average1))));
                                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(average1))));
                                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(average2))));
                                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(average3))));
                                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(average4))));
                                                                        lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(average5))));
                                                                        lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(average6))));
                                                                        List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", ""));

                                                                        lineDataSet = new LineDataSet(lineEntries, "Memory Daily Progress");
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


//    private void initData() {
//        memoryInterventionClassList = new ArrayList<>();
//
//        memoryInterventionClassList.add(new MemoryInterventionClass(R.drawable.gamescardwo, "Games", "Games for Memory"));
//        memoryInterventionClassList.add(new MemoryInterventionClass(R.drawable.medibg, "Meditation", "Meditation for Memory"));
//        memoryInterventionClassList.add(new MemoryInterventionClass(R.drawable.musiccardwo, "Music", "Music for Memory"));
//    }
////
//    private void initRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        //Setting layout of recylcerview
//        //Initializing adapter
//        memoryAdapter = new MemoryAdapter(this, (ArrayList<MemoryInterventionClass>) memoryInterventionClassList, this::onNoteClickMeditation);
//
//        recyclerView.setAdapter(memoryAdapter);
//        memoryAdapter.notifyDataSetChanged();
//    }

    // Popup window method for suggestions to improve concentration
    public void gotoPopup1(View view) {
//        Intent intentgp1 = new Intent(Concentration_Daily.this, Concentration_popup.class);
//
//        startActivity(intentgp1);
        ImageButton canpop1, gamespop1, musicpop1, meditationpop1;
        View c1, c2;
        FirebaseUser mUser;
        dialogcd.setContentView(R.layout.memory_popup);

        canpop1 = (ImageButton) dialogcd.findViewById(R.id.canpop1);
        gamespop1 = (ImageButton) dialogcd.findViewById(R.id.gamespop1);
        meditationpop1 = (ImageButton) dialogcd.findViewById(R.id.meditationpop1);
        musicpop1 = (ImageButton) dialogcd.findViewById(R.id.musicpop1);
        c1 = (View) dialogcd.findViewById(R.id.c1);
        c2 = (View) dialogcd.findViewById(R.id.c2);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor PopUp", String.valueOf(snapshot.getValue()));
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

        canpop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcd.dismiss();
            }
        });

        gamespop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
        meditationpop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeditationExercise.class));
            }
        });

        musicpop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });
        dialogcd.show();


    }

    public void gotoPopup5m(View view) {

        startActivity(new Intent(getApplicationContext(), Connection.class));

//        ImageButton imageViewcancle, imageViewmed, imageViewsong, imageViewgames;
//        View c1, c2;
//        FirebaseUser mUser;
//
//        dialogcd.setContentView(R.layout.memory_popup);
//        c1 = (View) dialogcd.findViewById(R.id.c1);
//        c2 = (View) dialogcd.findViewById(R.id.c2);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
//        imageViewmed = (ImageButton) dialogcd.findViewById(R.id.meditationpop1);
//        imageViewmed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MeditationExercise.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewsong = (ImageButton) dialogcd.findViewById(R.id.musicpop1);
//        imageViewsong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Music.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewgames = (ImageButton) dialogcd.findViewById(R.id.gamespop1);
//        imageViewgames.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), GameActivity.class));
//            }
//        });
//
//        imageViewcancle = (ImageButton) dialogcd.findViewById(R.id.canclepop1);
//        imageViewcancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogcd.dismiss();
//            }
//        });
//
//
//        dialogcd.show();

    }

    @Override
    public void onNoteClickMeditation(int position) {
        memoryInterventionClassList.get(position);
//        Toast.makeText(this, "You clicked"+memoryInterventionClassList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if (memoryInterventionClassList.get(position).getTitle().equals("Games")) {
            startActivity(new Intent(getApplicationContext(), GameActivity.class));
        } else if (memoryInterventionClassList.get(position).getTitle().equals("Music")) {
            startActivity(new Intent(getApplicationContext(), Music.class));
        } else {
            startActivity(new Intent(getApplicationContext(), MeditationExercise.class));
        }


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