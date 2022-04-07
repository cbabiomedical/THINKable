package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.thinkableproject.sample.Concentration;
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

import pl.droidsonroids.gif.GifImageView;

public class ConcentrationReportMonthly extends AppCompatActivity {
    BarChart barChart, barChart2;
    private Context context;
    File fileName, localFile, fileName1, localFile1;
    String text;
    AppCompatButton daily, weekly, yearly, whereAmI;
    FirebaseUser mUser;
    GifImageView c1gif, c2gif;
    Animation scaleUp, scaleDown;

    int color;
    View c1, c2;
    Double total1 = 0.0;
    Double sumIn1 = 0.0;
    Double total2 = 0.0;
    Double sumIn2 = 0.0;
    Double total3 = 0.0;
    Double sumIn3 = 0.0;
    Double total4 = 0.0;
    Double sumIn4 = 0.0;
    Double total5 = 0.0;
    Double sumIn5 = 0.0;
    Double total6 = 0.0;
    Double sumIn6 = 0.0;
    Double total7 = 0.0;
    Double sumIn7 = 0.0;
    Double total8 = 0.0;
    Double sumIn8 = 0.0;
    Double total9 = 0.0;
    Double sumIn9 = 0.0;
    Double total10 = 0.0;
    Double sumIn10 = 0.0;
    Double total11 = 0.0;
    Double sumIn11 = 0.0;
    Double total12 = 0.0;
    Double sumIn12 = 0.0;
    Double avg1, avg2, avg3, avg4, avg5, avg6, avg7, avg8, avg9, avg10, avg11, avg12;
    ImageView relaxationBtn, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Long average1, average2, average3, average4, average5, average6, average7, average8, average9, average10, average11, average12;
    Long average21, average22, average23, average24, average25, average26, average27, average28, average29, average210, average211, average212;


    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_monthly);
// Adding identifier to front end elements
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        yearly = findViewById(R.id.yearly);
        barChart = (BarChart) findViewById(R.id.barChartMonthly);
        barChart2 = findViewById(R.id.barChartMonthly2);
        relaxationBtn = findViewById(R.id.relaxation);
        whereAmI = findViewById(R.id.whereAmI);
        memory = findViewById(R.id.memory);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartMonthly);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        getEntries();


        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.reports);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reports:
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
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                    sum += av1;
                                }
                            }
                        }
                        Log.d("Monthly Array", String.valueOf(sumElement));
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                            sum += av1;
                                        }
                                    }
                                }
                                Log.d("Monthly Array", String.valueOf(sumElement));
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                    sum += av1;
                                                }
                                            }
                                        }
                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                            sum += av1;
                                                        }
                                                    }
                                                }
                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average4 = 0L;
                                                }
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                    sum += av1;
                                                                }
                                                            }
                                                        }
                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                        Log.d("SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average5 = 0L;
                                                        }

                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                            sum += av1;
                                                                        }
                                                                    }
                                                                }
                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                Log.d("SUM", String.valueOf(sum));
                                                                if (sum != 0) {
                                                                    average6 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                } else {
                                                                    average6 = 0L;
                                                                }
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                    sum += av1;
                                                                                }
                                                                            }
                                                                        }
                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                        Log.d("SUM", String.valueOf(sum));
                                                                        if (sum != 0) {
                                                                            average7 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                        } else {
                                                                            average7 = 0L;
                                                                        }
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                            sum += av1;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                if (sum != 0) {
                                                                                    average8 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                } else {
                                                                                    average8 = 0L;
                                                                                }
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                    sum += av1;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                        Log.d("SUM", String.valueOf(sum));
                                                                                        if (sum != 0) {
                                                                                            average9 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                        } else {
                                                                                            average9 = 0L;
                                                                                        }
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                            sum += av1;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                                if (sum != 0) {
                                                                                                    average10 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                } else {
                                                                                                    average10 = 0L;
                                                                                                }
                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                                    sum += av1;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                        Log.d("SUM", String.valueOf(sum));
                                                                                                        if (sum != 0) {
                                                                                                            average11 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                        } else {
                                                                                                            average11 = 0L;
                                                                                                        }
                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                                            sum += av1;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                                                if (sum != 0) {
                                                                                                                    average12 = sum / Long.parseLong(String.valueOf(sumElement.size()));
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
                                                                                                                List<BarEntry> entries = new ArrayList<>();
                                                                                                                entries.add(new BarEntry(1, Float.parseFloat(String.valueOf(average1))));
                                                                                                                entries.add(new BarEntry(2, Float.parseFloat(String.valueOf(average2))));
                                                                                                                entries.add(new BarEntry(3, Float.parseFloat(String.valueOf(average3))));
                                                                                                                entries.add(new BarEntry(4, Float.parseFloat(String.valueOf(average4))));
                                                                                                                entries.add(new BarEntry(5, Float.parseFloat(String.valueOf(average5))));
                                                                                                                entries.add(new BarEntry(6, Float.parseFloat(String.valueOf(average6))));
                                                                                                                entries.add(new BarEntry(7, Float.parseFloat(String.valueOf(average7))));
                                                                                                                entries.add(new BarEntry(8, Float.parseFloat(String.valueOf(average8))));
                                                                                                                entries.add(new BarEntry(9, Float.parseFloat(String.valueOf(average9))));
                                                                                                                entries.add(new BarEntry(10, Float.parseFloat(String.valueOf(average10))));
                                                                                                                entries.add(new BarEntry(11, Float.parseFloat(String.valueOf(average11))));
                                                                                                                entries.add(new BarEntry(12, Float.parseFloat(String.valueOf(average12))));

                                                                                                                List<Float> credits = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f, 56f, 53f, 87f, 23f, 45f));

                                                                                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
                                                                                                                float textSize = 7f;
                                                                                                                //Initializing object of MyBarDataset class
                                                                                                                MyBarDataset dataSet = new MyBarDataset(entries, "data", credits);
                                                                                                                dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                                                                                BarData data = new BarData(dataSet);
                                                                                                                data.setDrawValues(false);
                                                                                                                data.setBarWidth(0.9f);
                                                                                                                barChart.getXAxis().setLabelCount(25, true);
                                                                                                                barChart.setExtraBottomOffset(3f);

                                                                                                                barChart.setData(data);
                                                                                                                barChart.setFitBars(true);
                                                                                                                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                                                                                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                                                                barChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart.getXAxis().setTextSize(textSize);
                                                                                                                barChart.getAxisLeft().setTextSize(textSize);

                                                                                                                barChart.getAxisRight().setEnabled(false);
                                                                                                                Description desc = new Description();
                                                                                                                desc.setText("");
                                                                                                                barChart.setDescription(desc);
                                                                                                                barChart.getLegend().setEnabled(false);
                                                                                                                barChart.getXAxis().setDrawGridLines(false);
                                                                                                                barChart.getAxisLeft().setDrawGridLines(false);
                                                                                                                barChart.setNoDataText("Data Loading Please Wait....");

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


            }
        }, delay);


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();


        final Handler handler1 = new Handler();
        final int delay1 = 5000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                    sum += av1;
                                }
                            }
                        }
                        Log.d("Monthly Array", String.valueOf(sumElement));
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average21 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average21 = 0L;
                        }

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                            sum += av1;
                                        }
                                    }
                                }
                                Log.d("Monthly Array", String.valueOf(sumElement));
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average22 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average22 = 0L;
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                    sum += av1;
                                                }
                                            }
                                        }
                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average23 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average23 = 0L;
                                        }

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                            sum += av1;
                                                        }
                                                    }
                                                }
                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average24 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average24 = 0L;
                                                }
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                    sum += av1;
                                                                }
                                                            }
                                                        }
                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                        Log.d("SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average25 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average25 = 0L;
                                                        }

                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                            sum += av1;
                                                                        }
                                                                    }
                                                                }
                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                Log.d("SUM", String.valueOf(sum));
                                                                if (sum != 0) {
                                                                    average26 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                } else {
                                                                    average26 = 0L;
                                                                }
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                    sum += av1;
                                                                                }
                                                                            }
                                                                        }
                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                        Log.d("SUM", String.valueOf(sum));
                                                                        if (sum != 0) {
                                                                            average27 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                        } else {
                                                                            average27 = 0L;
                                                                        }
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                            sum += av1;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                if (sum != 0) {
                                                                                    average28 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                } else {
                                                                                    average28 = 0L;
                                                                                }
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                    sum += av1;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                        Log.d("SUM", String.valueOf(sum));
                                                                                        if (sum != 0) {
                                                                                            average29 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                        } else {
                                                                                            average29 = 0L;
                                                                                        }
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                            sum += av1;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                                if (sum != 0) {
                                                                                                    average210 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                } else {
                                                                                                    average210 = 0L;
                                                                                                }
                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                                    sum += av1;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                        Log.d("SUM", String.valueOf(sum));
                                                                                                        if (sum != 0) {
                                                                                                            average211 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                        } else {
                                                                                                            average211 = 0L;
                                                                                                        }
                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                                            sum += av1;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                                Log.d("SUM", String.valueOf(sum));
                                                                                                                if (sum != 0) {
                                                                                                                    average212 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                } else {
                                                                                                                    average212 = 0L;
                                                                                                                }
                                                                                                                Log.d("Average Jan", String.valueOf(average21));
                                                                                                                Log.d("Average Feb", String.valueOf(average22));
                                                                                                                Log.d("Average Mar", String.valueOf(average23));
                                                                                                                Log.d("Average Apr", String.valueOf(average24));
                                                                                                                Log.d("Average May", String.valueOf(average25));
                                                                                                                Log.d("Average Jun", String.valueOf(average26));
                                                                                                                Log.d("Average Jul", String.valueOf(average27));
                                                                                                                Log.d("Average Aug", String.valueOf(average28));
                                                                                                                Log.d("Average Sep", String.valueOf(average29));
                                                                                                                Log.d("Average Oct", String.valueOf(average210));
                                                                                                                Log.d("Average Nov", String.valueOf(average211));
                                                                                                                Log.d("Average Dec", String.valueOf(average212));
                                                                                                                List<BarEntry> entries2 = new ArrayList<>();
                                                                                                                ArrayList<Float> credits1 = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 56f, 87f, 12f, 54f, 04f, 45f, 65f, 34f));
                                                                                                                entries2.add(new BarEntry(1, Float.parseFloat(String.valueOf(average21))));
                                                                                                                entries2.add(new BarEntry(2, Float.parseFloat(String.valueOf(average22))));
                                                                                                                entries2.add(new BarEntry(3, Float.parseFloat(String.valueOf(average23))));
                                                                                                                entries2.add(new BarEntry(4, Float.parseFloat(String.valueOf(average24))));
                                                                                                                entries2.add(new BarEntry(5, Float.parseFloat(String.valueOf(average25))));
                                                                                                                entries2.add(new BarEntry(6, Float.parseFloat(String.valueOf(average26))));
                                                                                                                entries2.add(new BarEntry(7, Float.parseFloat(String.valueOf(average27))));
                                                                                                                entries2.add(new BarEntry(8, Float.parseFloat(String.valueOf(average28))));
                                                                                                                entries2.add(new BarEntry(9, Float.parseFloat(String.valueOf(average29))));
                                                                                                                entries2.add(new BarEntry(10, Float.parseFloat(String.valueOf(average210))));
                                                                                                                entries2.add(new BarEntry(11, Float.parseFloat(String.valueOf(average211))));
                                                                                                                entries2.add(new BarEntry(12, Float.parseFloat(String.valueOf(average212))));
                                                                                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
                                                                                                                float textSize = 6f;
                                                                                                                MyBarDataset dataSet1 = new MyBarDataset(entries2, "data", credits1);
                                                                                                                dataSet1.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                                                                                                        ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                                                                                BarData data1 = new BarData(dataSet1);
                                                                                                                data1.setDrawValues(false);
                                                                                                                data1.setBarWidth(0.8f);

                                                                                                                barChart2.setData(data1);
                                                                                                                barChart2.setFitBars(true);
                                                                                                                barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                                                                                barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                                                                barChart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));

                                                                                                                barChart2.getXAxis().setTextSize(textSize);
                                                                                                                barChart2.getAxisLeft().setTextSize(textSize);
                                                                                                                barChart2.setExtraBottomOffset(3f);
                                                                                                                barChart2.getXAxis().setLabelCount(25, true);

                                                                                                                barChart2.getAxisRight().setEnabled(false);
                                                                                                                Description desc1 = new Description();
                                                                                                                desc1.setText("");
                                                                                                                barChart2.setDescription(desc1);
                                                                                                                barChart2.getLegend().setEnabled(false);
                                                                                                                barChart2.getXAxis().setDrawGridLines(false);
                                                                                                                barChart2.getAxisLeft().setDrawGridLines(false);
                                                                                                                barChart2.setNoDataText("Data Loading Please Wait...");
                                                                                                                barChart2.invalidate();


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

//
            }

            //Downloading file and displaying chart
        }, delay);

        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//

        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        daily.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    daily.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    daily.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
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

        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportYearly.class);
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
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportMonthly.class);
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
        // On click listener of where am i toggle button
        whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        whereAmI.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    whereAmI.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    whereAmI.startAnimation(scaleDown);
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


            @Override
            public void run() {

                lineEntries = new ArrayList();

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(1));

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesMonth3", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesMonth3Data", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataMonth1", String.valueOf(dataSnapshot2.getKey()));
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Log.d("DataMonth3Sn", String.valueOf(dataSnapshot3.getValue()));
                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                            Log.d("KeyMonth", dataSnapshot4.getKey());
                                            if (dataSnapshot4.getKey().equals("index")) {
                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                sumElement.add(av1);
                                                sumIn1 += av1;
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalMonth1", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sumIn1 = 0.0;
                            avg1 = 0.0;
                        } else {
                            Log.d("SUMMonth1", String.valueOf(total1));
                            avg1 = total1 / sumElement.size();
                            Log.d("AverageMonth1", String.valueOf(avg1));
                        }
                        Log.d("AverageMonth1", String.valueOf(avg1));
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(2));

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesMonth3", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesMonth2Data", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataMonth2", String.valueOf(dataSnapshot2.getKey()));
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("DataMonth2Sn", String.valueOf(dataSnapshot3.getValue()));
                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                    Log.d("KeyMonth2", dataSnapshot4.getKey());
                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                        sumElement.add(av1);
                                                        sumIn2 += av1;
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalMonth2", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sumIn2 = 0.0;
                                    avg2 = 0.0;
                                } else {
                                    Log.d("SUMMonth2", String.valueOf(total2));
                                    avg2 = total2 / sumElement.size();
                                    Log.d("AverageMonth2", String.valueOf(avg2));
                                }
                                Log.d("AverageMonth2", String.valueOf(avg2));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(3));

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesMonth3", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesMonth3Data", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataMonth3", String.valueOf(dataSnapshot2.getKey()));
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Log.d("DataMonth3Sn", String.valueOf(dataSnapshot3.getValue()));
                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                            Log.d("KeyMonth3", dataSnapshot4.getKey());
                                                            if (dataSnapshot4.getKey().equals("index")) {
                                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                                sumElement.add(av1);
                                                                sumIn3 += av1;
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalMonth3", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sumIn3 = 0.0;
                                            avg3 = 0.0;
                                        } else {
                                            Log.d("SUMMonth3", String.valueOf(total3));
                                            avg3 = total3 / sumElement.size();
                                            Log.d("AverageMonth3", String.valueOf(avg3));
                                        }
                                        Log.d("AverageMonth3", String.valueOf(avg3));
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(4));

                                        reference1.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("ValuesMonth4", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesMonth4Data", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("DataMonth4", String.valueOf(dataSnapshot2.getKey()));
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Log.d("DataMonth4Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                    Log.d("KeyMonth4", dataSnapshot4.getKey());
                                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                                        sumElement.add(av1);
                                                                        sumIn4 += av1;
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total4 += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalMonth4", String.valueOf(total4));

                                                if (total4 == 0.0) {
                                                    sumIn4 = 0.0;
                                                    avg4 = 0.0;
                                                } else {
                                                    Log.d("SUMMonth4", String.valueOf(total4));
                                                    avg4 = total4 / sumElement.size();
                                                    Log.d("AverageMonth4", String.valueOf(avg4));
                                                }
                                                Log.d("AverageMonth4", String.valueOf(avg4));
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(5));

                                                reference1.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("ValuesMonth5", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("ValuesMonth5Data", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                    Log.d("DataMonth5", String.valueOf(dataSnapshot2.getKey()));
                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                        Log.d("DataMonth5Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                            Log.d("KeyMonth5", dataSnapshot4.getKey());
                                                                            if (dataSnapshot4.getKey().equals("index")) {
                                                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                                                sumElement.add(av1);
                                                                                sumIn5 += av1;
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                            total5 += (Double) sumElement.get(i);
                                                        }
                                                        Log.d("TotalMonth5", String.valueOf(total5));

                                                        if (total5 == 0.0) {
                                                            sumIn5 = 0.0;
                                                            avg5 = 0.0;
                                                        } else {
                                                            Log.d("SUMMonth5", String.valueOf(total5));
                                                            avg5 = total5 / sumElement.size();
                                                            Log.d("AverageMonth5", String.valueOf(avg5));
                                                        }
                                                        Log.d("AverageMonth5", String.valueOf(avg5));
                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(6));

                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();

                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("ValuesMonth6", String.valueOf(dataSnapshot.getValue()));
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("ValuesMonth6Data", String.valueOf(dataSnapshot1.getValue()));
                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                            Log.d("DataMonth6", String.valueOf(dataSnapshot2.getKey()));
                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                Log.d("DataMonth6Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                    Log.d("KeyMonth6", dataSnapshot4.getKey());
                                                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                                                        sumElement.add(av1);
                                                                                        sumIn6 += av1;
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                    total6 += (Double) sumElement.get(i);
                                                                }
                                                                Log.d("TotalMonth6", String.valueOf(total6));

                                                                if (total6 == 0.0) {
                                                                    sumIn6 = 0.0;
                                                                    avg6 = 0.0;
                                                                } else {
                                                                    Log.d("SUMMonth6", String.valueOf(total6));
                                                                    avg6 = total6 / sumElement.size();
                                                                    Log.d("AverageMonth6", String.valueOf(avg6));
                                                                }
                                                                Log.d("AverageMonth6", String.valueOf(avg6));
                                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(7));

                                                                reference1.addValueEventListener(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("ValuesMonth7", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("ValuesMonth7Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                    Log.d("DataMonth7", String.valueOf(dataSnapshot2.getKey()));
                                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                        Log.d("DataMonth7Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                            Log.d("KeyMonth7", dataSnapshot4.getKey());
                                                                                            if (dataSnapshot4.getKey().equals("index")) {
                                                                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                sumElement.add(av1);
                                                                                                sumIn7 += av1;
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                }
                                                                            }
                                                                        }
                                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                                            total7 += (Double) sumElement.get(i);
                                                                        }
                                                                        Log.d("TotalMonth7", String.valueOf(total7));

                                                                        if (total7 == 0.0) {
                                                                            sumIn7 = 0.0;
                                                                            avg7 = 0.0;
                                                                        } else {
                                                                            Log.d("SUMMonth7", String.valueOf(total7));
                                                                            avg7 = total7 / sumElement.size();
                                                                            Log.d("AverageMonth7", String.valueOf(avg7));
                                                                        }
                                                                        Log.d("AverageMonth7", String.valueOf(avg7));
//
                                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                .child(String.valueOf(8));

                                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                ArrayList sumElement = new ArrayList();

                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                    Log.d("ValuesMonth8", String.valueOf(dataSnapshot.getValue()));
                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                        Log.d("ValuesMonth8Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                            Log.d("DataMonth8", String.valueOf(dataSnapshot2.getKey()));
                                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                Log.d("DataMonth8Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                                    Log.d("KeyMonth8", dataSnapshot4.getKey());
                                                                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                        sumElement.add(av1);
                                                                                                        sumIn8 += av1;
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                                    total8 += (Double) sumElement.get(i);
                                                                                }
                                                                                Log.d("TotalMonth8", String.valueOf(total8));

                                                                                if (total8 == 0.0) {
                                                                                    sumIn8 = 0.0;
                                                                                    avg8 = 0.0;
                                                                                } else {
                                                                                    Log.d("SUMMonth8", String.valueOf(total8));
                                                                                    avg8 = total8 / sumElement.size();
                                                                                    Log.d("AverageMonth8", String.valueOf(avg8));
                                                                                }
                                                                                Log.d("AverageMonth8", String.valueOf(avg8));
                                                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                        .child(String.valueOf(9));

                                                                                reference1.addValueEventListener(new ValueEventListener() {

                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        ArrayList sumElement = new ArrayList();

                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                            Log.d("ValuesMonth9", String.valueOf(dataSnapshot.getValue()));
                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                Log.d("ValuesMonth9Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                                    Log.d("DataMonth9", String.valueOf(dataSnapshot2.getKey()));
                                                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                        Log.d("DataMonth9Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                                            Log.d("KeyMonth9", dataSnapshot4.getKey());
                                                                                                            if (dataSnapshot4.getKey().equals("index")) {
                                                                                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                                sumElement.add(av1);
                                                                                                                sumIn9 += av1;
                                                                                                            }
                                                                                                        }
                                                                                                    }

                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                                                            total9 += (Double) sumElement.get(i);
                                                                                        }
                                                                                        Log.d("TotalMonth9", String.valueOf(total9));

                                                                                        if (total9 == 0.0) {
                                                                                            sumIn9 = 0.0;
                                                                                            avg9 = 0.0;
                                                                                        } else {
                                                                                            Log.d("SUMMonth9", String.valueOf(total9));
                                                                                            avg9 = total9 / sumElement.size();
                                                                                            Log.d("AverageMonth9", String.valueOf(avg9));
                                                                                        }
                                                                                        Log.d("AverageMonth9", String.valueOf(avg9));
                                                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                .child(String.valueOf(10));

                                                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                ArrayList sumElement = new ArrayList();

                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                                    Log.d("ValuesMonth10", String.valueOf(dataSnapshot.getValue()));
                                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                        Log.d("ValuesMonth10Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                                            Log.d("DataMonth10", String.valueOf(dataSnapshot2.getKey()));
                                                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                                Log.d("DataMonth10Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                                                    Log.d("KeyMonth10", dataSnapshot4.getKey());
                                                                                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                                                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                                        sumElement.add(av1);
                                                                                                                        sumIn10 += av1;
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                                                    total10 += (Double) sumElement.get(i);
                                                                                                }
                                                                                                Log.d("TotalMonth10", String.valueOf(total10));

                                                                                                if (total10 == 0.0) {
                                                                                                    sumIn10 = 0.0;
                                                                                                    avg10 = 0.0;
                                                                                                } else {
                                                                                                    Log.d("SUMMonth10", String.valueOf(total10));
                                                                                                    avg10 = total10 / sumElement.size();
                                                                                                    Log.d("AverageMonth10", String.valueOf(avg10));
                                                                                                }
                                                                                                Log.d("AverageMonth10", String.valueOf(avg10));
                                                                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                        .child(String.valueOf(11));

                                                                                                reference1.addValueEventListener(new ValueEventListener() {

                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                        ArrayList sumElement = new ArrayList();

                                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                                            Log.d("ValuesMonth11", String.valueOf(dataSnapshot.getValue()));
                                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                Log.d("ValuesMonth11Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                                                    Log.d("DataMonth11", String.valueOf(dataSnapshot2.getKey()));
                                                                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                                        Log.d("DataMonth11Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                                                            Log.d("KeyMonth11", dataSnapshot4.getKey());
                                                                                                                            if (dataSnapshot4.getKey().equals("index")) {
                                                                                                                                Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                                                sumElement.add(av1);
                                                                                                                                sumIn11 += av1;
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                                                                            total11 += (Double) sumElement.get(i);
                                                                                                        }
                                                                                                        Log.d("TotalMonth11", String.valueOf(total11));

                                                                                                        if (total11 == 0.0) {
                                                                                                            sumIn11 = 0.0;
                                                                                                            avg11 = 0.0;
                                                                                                        } else {
                                                                                                            Log.d("SUMMonth11", String.valueOf(total11));
                                                                                                            avg11 = total11 / sumElement.size();
                                                                                                            Log.d("AverageMonth11", String.valueOf(avg11));
                                                                                                        }
                                                                                                        Log.d("AverageMonth11", String.valueOf(avg10));
                                                                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                                .child(String.valueOf(12));

                                                                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                ArrayList sumElement = new ArrayList();

                                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                                                    Log.d("ValuesMonth12", String.valueOf(dataSnapshot.getValue()));
                                                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                        Log.d("ValuesMonth12Data", String.valueOf(dataSnapshot1.getValue()));
                                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                                                            Log.d("DataMonth12", String.valueOf(dataSnapshot2.getKey()));
                                                                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                                                Log.d("DataMonth12Sn", String.valueOf(dataSnapshot3.getValue()));
                                                                                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                                                                                    Log.d("KeyMonth12", dataSnapshot4.getKey());
                                                                                                                                    if (dataSnapshot4.getKey().equals("index")) {
                                                                                                                                        Double av1 = (Double) dataSnapshot4.getValue();
                                                                                                                                        sumElement.add(av1);
                                                                                                                                        sumIn12 += av1;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }

                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                                                                    total12 += (Double) sumElement.get(i);
                                                                                                                }
                                                                                                                Log.d("TotalMonth12", String.valueOf(total12));

                                                                                                                if (total12 == 0.0) {
                                                                                                                    sumIn12 = 0.0;
                                                                                                                    avg12 = 0.0;
                                                                                                                } else {
                                                                                                                    Log.d("SUMMonth12", String.valueOf(total12));
                                                                                                                    avg12 = total12 / sumElement.size();
                                                                                                                    Log.d("AverageMonth12", String.valueOf(avg12));
                                                                                                                }
                                                                                                                Log.d("Average Jan", String.valueOf(avg1));
                                                                                                                Log.d("Average Feb", String.valueOf(avg2));
                                                                                                                Log.d("Average Mar", String.valueOf(avg3));
                                                                                                                Log.d("Average Apr", String.valueOf(avg4));
                                                                                                                Log.d("Average May", String.valueOf(avg5));
                                                                                                                Log.d("Average Jun", String.valueOf(avg6));
                                                                                                                Log.d("Average Jul", String.valueOf(avg7));
                                                                                                                Log.d("Average Aug", String.valueOf(avg8));
                                                                                                                Log.d("Average Sep", String.valueOf(avg9));
                                                                                                                Log.d("Average Oct", String.valueOf(avg10));
                                                                                                                Log.d("Average Nov", String.valueOf(avg11));
                                                                                                                Log.d("Average Dec", String.valueOf(avg12));

                                                                                                                lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(avg1))));
                                                                                                                lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(avg2))));
                                                                                                                lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(avg3))));
                                                                                                                lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(avg4))));
                                                                                                                lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(avg5))));
                                                                                                                lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(avg6))));
                                                                                                                lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(avg7))));
                                                                                                                lineEntries.add(new Entry(8, Float.parseFloat(String.valueOf(avg8))));
                                                                                                                lineEntries.add(new Entry(9, Float.parseFloat(String.valueOf(avg9))));
                                                                                                                lineEntries.add(new Entry(10, Float.parseFloat(String.valueOf(avg10))));
                                                                                                                lineEntries.add(new Entry(11, Float.parseFloat(String.valueOf(avg11))));
                                                                                                                lineEntries.add(new Entry(12, Float.parseFloat(String.valueOf(avg12))));
                                                                                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));

                                                                                                                lineDataSet = new LineDataSet(lineEntries, "Concentration Monthly Progress");
                                                                                                                lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

                                                                                                                lineData = new LineData(lineDataSet);
                                                                                                                lineChart.setData(lineData);

                                                                                                                lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                                                                                                lineDataSet.setValueTextColor(Color.WHITE);
                                                                                                                lineDataSet.setValueTextSize(10f);

                                                                                                                lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                lineChart.getXAxis().setTextSize(7f);
                                                                                                                lineChart.setBorderColor(Color.TRANSPARENT);
                                                                                                                lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                lineChart.getAxisLeft().setDrawGridLines(false);
                                                                                                                lineChart.getXAxis().setDrawGridLines(false);
                                                                                                                lineChart.getAxisRight().setDrawGridLines(false);
                                                                                                                lineChart.getXAxis().setLabelCount(12, true);
                                                                                                                lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                                                                                                lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                                                                                                lineChart.setTouchEnabled(true);
                                                                                                                lineChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                lineChart.setDragEnabled(true);
                                                                                                                lineChart.getXAxis().setLabelCount(23, true);
                                                                                                                lineChart.setExtraBottomOffset(2f);
                                                                                                                lineChart.getXAxis().setAvoidFirstLastClipping(true);

                                                                                                                lineChart.setScaleEnabled(false);
                                                                                                                lineChart.setPinchZoom(false);
                                                                                                                lineChart.setDrawGridBackground(false);
                                                                                                                lineChart.getDescription().setTextColor(R.color.white);
                                                                                                                lineChart.invalidate();
                                                                                                                lineChart.refreshDrawableState();
                                                                                                                XAxis xAxis = lineChart.getXAxis();
                                                                                                                xAxis.setGranularity(1f);
                                                                                                                xAxis.setCenterAxisLabels(true);
                                                                                                                xAxis.setEnabled(true);
                                                                                                                xAxis.setDrawGridLines(false);
                                                                                                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                                                                                                            }

                                                                                                            //
                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                                            }
                                                                                                        });
                                                                                                    }

                                                                                                    //
                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                            //
                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });
                                                                                    }

                                                                                    //
                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });
//
                                                                            }

                                                                            //
                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
//
////
                                                                    }

                                                                    //
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

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
////
//////
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
//////
//////
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//
//////
                            }

                            //
//                            //
//                            //
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
            }
        }, 10000);
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