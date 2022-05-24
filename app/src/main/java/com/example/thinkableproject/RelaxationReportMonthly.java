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

import pl.droidsonroids.gif.GifImageView;

public class RelaxationReportMonthly extends AppCompatActivity {

    BarChart barChartMonthlytimeto, barChartMonthlytimestayed;
    private Context context;
    File fileName, localFile, fileName2, localFile2;
    String text, text2;
    GifImageView c1gif, c2gif;
    int color;
    Animation scaleUp, scaleDown;

    View c1, c2;
    AppCompatButton daily, weekly, yearly, whereAmI, progress, timetorel, timestayedrel;
    FirebaseUser mUser;
    ImageView concentration, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<Float> floatList2 = new ArrayList<>();
    Long average1, average2, average3, average4, average5, average6, average7, average8, average9, average10, average11, average12;
    Long average21, average22, average23, average24, average25, average26, average27, average28, average29, average210, average211, average212;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Double sumIn1 = 0.0;
    Double sumIn2 = 0.0;
    Double sumIn3 = 0.0;
    Double sumIn4 = 0.0;
    Double sumIn5 = 0.0;
    Double sumIn6 = 0.0;
    Double sumIn7 = 0.0;
    Double sumIn8 = 0.0;
    Double sumIn9 = 0.0;
    Double sumIn10 = 0.0;
    Double sumIn11 = 0.0;
    Double sumIn12 = 0.0;

    Double averageIn1 = 0.0;
    Double averageIn2 = 0.0;
    Double averageIn3 = 0.0;
    Double averageIn4 = 0.0;
    Double averageIn5 = 0.0;
    Double averageIn6 = 0.0;
    Double averageIn7 = 0.0;
    Double averageIn8 = 0.0;
    Double averageIn9 = 0.0;
    Double averageIn10 = 0.0;
    Double averageIn11 = 0.0;
    Double averageIn12 = 0.0;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_monthly);

        //Initialize buttons
        whereAmI = findViewById(R.id.whereAmI);
        progress = findViewById(R.id.progress);
//        timetorel = findViewById(R.id.btn_timeCon);
//        timestayedrel = findViewById(R.id.btn_timeStayedCon);
        //Initialize bar chart
        barChartMonthlytimeto = findViewById(R.id.barChartMonthly);
        barChartMonthlytimestayed = findViewById(R.id.barChartMonthly2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");
        //Initialize List entries
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        //Initialize buttons
        concentration = findViewById(R.id.concentration);
        daily = findViewById(R.id.daily);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
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

        //go to relaxation daily page
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportDaily.class);
                startActivity(intentr1);
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
        //go to relaxation weekly page
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportWeekly.class);
                startActivity(intentr1);
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
        //go to relaxation yearly page
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportYearly.class);
                startActivity(intentr1);
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

        getEntries();

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportDaily.class);
                startActivity(intent);
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

        //go to RQ Page with comparison to occupation and age factors
        whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportWhereamI.class);
                startActivity(intentr1);
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

        //go to concentration daily landing page
        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportMonthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        concentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    concentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    concentration.startAnimation(scaleDown);
                }

                return false;
            }
        });


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //initialize file handler
        final Handler handler = new Handler();
        final int delay = 7000;
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
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                                barChartMonthlytimeto.getXAxis().setLabelCount(25, true);
                                                                                                                barChartMonthlytimeto.setExtraBottomOffset(3f);

                                                                                                                barChartMonthlytimeto.setData(data);
                                                                                                                barChartMonthlytimeto.setFitBars(true);
                                                                                                                barChartMonthlytimeto.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                                                                                barChartMonthlytimeto.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                                                                barChartMonthlytimeto.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChartMonthlytimeto.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChartMonthlytimeto.getXAxis().setTextSize(textSize);
                                                                                                                barChartMonthlytimeto.getAxisLeft().setTextSize(textSize);

                                                                                                                barChartMonthlytimeto.getAxisRight().setEnabled(false);
                                                                                                                Description desc = new Description();
                                                                                                                desc.setText("");
                                                                                                                barChartMonthlytimeto.setDescription(desc);
                                                                                                                barChartMonthlytimeto.getLegend().setEnabled(false);
                                                                                                                barChartMonthlytimeto.getXAxis().setDrawGridLines(false);
                                                                                                                barChartMonthlytimeto.getAxisLeft().setDrawGridLines(false);
                                                                                                                barChartMonthlytimeto.setNoDataText("Data Loading Please Wait....");

                                                                                                                barChartMonthlytimeto.invalidate();


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


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //initialize file handler
        final Handler handler1 = new Handler();
        final int delay1 = 7000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                                                                                                barChartMonthlytimestayed.setData(data1);
                                                                                                                barChartMonthlytimestayed.setFitBars(true);
                                                                                                                barChartMonthlytimestayed.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                                                                                barChartMonthlytimestayed.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                                                                barChartMonthlytimestayed.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                                                                barChartMonthlytimestayed.getAxisLeft().setTextColor(getResources().getColor(R.color.white));

                                                                                                                barChartMonthlytimestayed.getXAxis().setTextSize(textSize);
                                                                                                                barChartMonthlytimestayed.getAxisLeft().setTextSize(textSize);
                                                                                                                barChartMonthlytimestayed.setExtraBottomOffset(3f);
                                                                                                                barChartMonthlytimestayed.getXAxis().setLabelCount(25, true);

                                                                                                                barChartMonthlytimestayed.getAxisRight().setEnabled(false);
                                                                                                                Description desc1 = new Description();
                                                                                                                desc1.setText("");
                                                                                                                barChartMonthlytimestayed.setDescription(desc1);
                                                                                                                barChartMonthlytimestayed.getLegend().setEnabled(false);
                                                                                                                barChartMonthlytimestayed.getXAxis().setDrawGridLines(false);
                                                                                                                barChartMonthlytimestayed.getAxisLeft().setDrawGridLines(false);
                                                                                                                barChartMonthlytimestayed.setNoDataText("Data Loading Please Wait...");
                                                                                                                barChartMonthlytimestayed.invalidate();


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
        }, delay1);

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();


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

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(1));
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    Log.d("DataSnapshotMonth1", String.valueOf(dataSnapshot2.getValue()));
                                    Double av1 = (Double) dataSnapshot2.getValue();
                                    sumIn1 += av1;
                                    sumElement.add(av1);
                                }
                            }
                        }
                        if (sumIn1 != 0.0) {
                            Log.d("SUMWEEK1", String.valueOf(sumIn1));
                            averageIn1 = sumIn1 / sumElement.size();
                            Log.d("AVERAGEWEEK1", String.valueOf(averageIn1));
                        } else {
                            sumIn1 = 0.0;
                            averageIn1 = 0.0;
                        }
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(2));
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            Log.d("DataSnapshotMonth2", String.valueOf(dataSnapshot2.getValue()));
                                            Double av1 = (Double) dataSnapshot2.getValue();
                                            sumIn2 += av1;
                                            sumElement.add(av1);
                                        }
                                    }
                                }
                                if (sumIn2 != 0.0) {
                                    Log.d("SUMWEEK2", String.valueOf(sumIn2));
                                    averageIn2 = sumIn2 / sumElement.size();
                                    Log.d("AVERAGEWEEK2", String.valueOf(averageIn2));
                                } else {
                                    sumIn2 = 0.0;
                                    averageIn2 = 0.0;
                                }
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(3));
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    Log.d("DataSnapshotMonth4", String.valueOf(dataSnapshot2.getValue()));
                                                    Double av1 = (Double) dataSnapshot2.getValue();
                                                    sumIn3 += av1;
                                                    sumElement.add(av1);
                                                }
                                            }
                                        }
                                        if (sumIn3 != 0.0) {
                                            Log.d("SUMWEEK3", String.valueOf(sumIn3));
                                            averageIn3 = sumIn3 / sumElement.size();
                                            Log.d("AVERAGEWEEK3", String.valueOf(averageIn3));
                                        } else {
                                            sumIn3 = 0.0;
                                            averageIn3 = 0.0;
                                        }

                                        lineEntries = new ArrayList();
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(4));
                                        reference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                            Log.d("DataSnapshotMonth4", String.valueOf(dataSnapshot2.getValue()));
                                                            Double av1 = (Double) dataSnapshot2.getValue();
                                                            sumIn4 += av1;
                                                            sumElement.add(av1);
                                                        }
                                                    }
                                                }
                                                if (sumIn4 != 0.0) {
                                                    Log.d("SUMWEEK4", String.valueOf(sumIn4));
                                                    averageIn4 = sumIn4 / sumElement.size();
                                                    Log.d("AVERAGEWEEK4", String.valueOf(averageIn4));
                                                } else {
                                                    sumIn4 = 0.0;
                                                    averageIn4 = 0.0;
                                                }
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(5));
                                                reference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                    Log.d("DataSnapshotMonth4", String.valueOf(dataSnapshot2.getValue()));
                                                                    Double av1 = (Double) dataSnapshot2.getValue();
                                                                    sumIn5 += av1;
                                                                    sumElement.add(av1);
                                                                }
                                                            }
                                                        }
                                                        if (sumIn5 != 0.0) {
                                                            Log.d("SUMWEEK5", String.valueOf(sumIn5));
                                                            averageIn5 = sumIn5 / sumElement.size();
                                                            Log.d("AVERAGEWEEK5", String.valueOf(averageIn5));
                                                        } else {
                                                            sumIn5 = 0.0;
                                                            averageIn5 = 0.0;
                                                        }
                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(6));
                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                            Log.d("DataSnapshotMonth6", String.valueOf(dataSnapshot2.getValue()));
                                                                            Double av1 = (Double) dataSnapshot2.getValue();
                                                                            sumIn6 += av1;
                                                                            sumElement.add(av1);
                                                                        }
                                                                    }
                                                                }
                                                                if (sumIn6 != 0.0) {
                                                                    Log.d("SUMWEEK6", String.valueOf(sumIn6));
                                                                    averageIn6 = sumIn6 / sumElement.size();
                                                                    Log.d("AVERAGEWEEK6", String.valueOf(averageIn6));
                                                                } else {
                                                                    sumIn6 = 0.0;
                                                                    averageIn6 = 0.0;
                                                                }
                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(7));
                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();
                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                    Log.d("DataSnapshotMonth7", String.valueOf(dataSnapshot2.getValue()));
                                                                                    Double av1 = (Double) dataSnapshot2.getValue();
                                                                                    sumIn7 += av1;
                                                                                    sumElement.add(av1);
                                                                                }
                                                                            }
                                                                        }
                                                                        if (sumIn7 != 0.0) {
                                                                            Log.d("SUMWEEK7", String.valueOf(sumIn7));
                                                                            averageIn7 = sumIn7 / sumElement.size();
                                                                            Log.d("AVERAGEWEEK7", String.valueOf(averageIn7));
                                                                        } else {
                                                                            sumIn7 = 0.0;
                                                                            averageIn7 = 0.0;
                                                                        }
                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                .child(String.valueOf(8));
                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                ArrayList sumElement = new ArrayList();
                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                            Log.d("DataSnapshotMonth8", String.valueOf(dataSnapshot2.getValue()));
                                                                                            Double av1 = (Double) dataSnapshot2.getValue();
                                                                                            sumIn8 += av1;
                                                                                            sumElement.add(av1);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (sumIn8 != 0.0) {
                                                                                    Log.d("SUMWEEK8", String.valueOf(sumIn8));
                                                                                    averageIn8 = sumIn8 / sumElement.size();
                                                                                    Log.d("AVERAGEWEEK8", String.valueOf(averageIn8));
                                                                                } else {
                                                                                    sumIn8 = 0.0;
                                                                                    averageIn8 = 0.0;
                                                                                }
                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                        .child(String.valueOf(9));
                                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        ArrayList sumElement = new ArrayList();
                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                                    Log.d("DataSnapshotMonth7", String.valueOf(dataSnapshot2.getValue()));
                                                                                                    Double av1 = (Double) dataSnapshot2.getValue();
                                                                                                    sumIn9 += av1;
                                                                                                    sumElement.add(av1);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (sumIn9 != 0.0) {
                                                                                            Log.d("SUMWEEK9", String.valueOf(sumIn9));
                                                                                            averageIn9 = sumIn9 / sumElement.size();
                                                                                            Log.d("AVERAGEWEEK9", String.valueOf(averageIn9));
                                                                                        } else {
                                                                                            sumIn9 = 0.0;
                                                                                            averageIn9 = 0.0;
                                                                                        }
                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                .child(String.valueOf(10));
                                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                ArrayList sumElement = new ArrayList();
                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                                            Log.d("DataSnapshotMonth10", String.valueOf(dataSnapshot2.getValue()));
                                                                                                            Double av1 = (Double) dataSnapshot2.getValue();
                                                                                                            sumIn10 += av1;
                                                                                                            sumElement.add(av1);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if (sumIn10 != 0.0) {
                                                                                                    Log.d("SUMWEEK10", String.valueOf(sumIn10));
                                                                                                    averageIn10 = sumIn10 / sumElement.size();
                                                                                                    Log.d("AVERAGEWEEK10", String.valueOf(averageIn10));
                                                                                                } else {
                                                                                                    sumIn10 = 0.0;
                                                                                                    averageIn10 = 0.0;
                                                                                                }

                                                                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                        .child(String.valueOf(11));
                                                                                                reference2.addValueEventListener(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                        ArrayList sumElement = new ArrayList();
                                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                                                    Log.d("DataSnapshotMonth11", String.valueOf(dataSnapshot2.getValue()));
                                                                                                                    Double av1 = (Double) dataSnapshot2.getValue();
                                                                                                                    sumIn11 += av1;
                                                                                                                    sumElement.add(av1);
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        if (sumIn11 != 0.0) {
                                                                                                            Log.d("SUMWEEK11", String.valueOf(sumIn11));
                                                                                                            averageIn11 = sumIn11 / sumElement.size();
                                                                                                            Log.d("AVERAGEWEEK11", String.valueOf(averageIn11));
                                                                                                        } else {
                                                                                                            sumIn11 = 0.0;
                                                                                                            averageIn11 = 0.0;
                                                                                                        }

                                                                                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                                .child(String.valueOf(12));
                                                                                                        reference2.addValueEventListener(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                ArrayList sumElement = new ArrayList();
                                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                                                            Log.d("DataSnapshotMonth11", String.valueOf(dataSnapshot2.getValue()));
                                                                                                                            Double av1 = (Double) dataSnapshot2.getValue();
                                                                                                                            sumIn12 += av1;
                                                                                                                            sumElement.add(av1);
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                if (sumIn12 != 0.0) {
                                                                                                                    Log.d("SUMWEEK12", String.valueOf(sumIn12));
                                                                                                                    averageIn12 = sumIn12 / sumElement.size();
                                                                                                                    Log.d("AVERAGEWEEK11", String.valueOf(averageIn12));
                                                                                                                } else {
                                                                                                                    sumIn12 = 0.0;
                                                                                                                    averageIn12 = 0.0;
                                                                                                                }


                                                                                                                lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(averageIn1))));
                                                                                                                lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(averageIn2))));
                                                                                                                lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(averageIn3))));
                                                                                                                lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(averageIn4))));
                                                                                                                lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(averageIn5))));
                                                                                                                lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(averageIn6))));
                                                                                                                lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(averageIn7))));
                                                                                                                lineEntries.add(new Entry(8, Float.parseFloat(String.valueOf(averageIn8))));
                                                                                                                lineEntries.add(new Entry(9, Float.parseFloat(String.valueOf(averageIn9))));
                                                                                                                lineEntries.add(new Entry(10, Float.parseFloat(String.valueOf(averageIn10))));
                                                                                                                lineEntries.add(new Entry(11, Float.parseFloat(String.valueOf(averageIn11))));
                                                                                                                lineEntries.add(new Entry(12, Float.parseFloat(String.valueOf(averageIn12))));

                                                                                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));

                                                                                                                lineDataSet = new LineDataSet(lineEntries, "Relaxation Monthly Progress");
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
            }
        }, 3000);

    }

    //set up x and y axis data
    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        //set up color of bars on chart
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

    //delete temporary file
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
}