package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
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

public class RelaxationReportWeekly extends AppCompatActivity {

    BarChart barChartWeeklytimeto, barChartWeeklytimestayed;
    AppCompatButton daily, yearly, monthly, whereAmI, progress;
    ImageView timetorel, timestayedrel;
    FirebaseUser mUser;
    String text;
    String text2;
    File localFile;
    File localFile2;
    Animation scaleUp, scaleDown;

    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    File fileName;
    File fileName2;
    ImageView concentration, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<Float> floatList2 = new ArrayList<>();
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Long average1, average2, average3, average4, average5;
    Long average11, average12, average13, average14, average15;
    Double averageIn1 = 0.0;
    Double averageIn2 = 0.0;
    Double averageIn3 = 0.0;
    Double averageIn4 = 0.0;
    Double averageIn5 = 0.0;
    Double sumIn1 = 0.0;
    Double sumIn2 = 0.0;
    Double sumIn3 = 0.0;
    Double sumIn4 = 0.0;
    Double sumIn5 = 0.0;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_weekly);

        //Initialize buttons
        whereAmI = findViewById(R.id.whereAmI);
        progress = findViewById(R.id.progress);
//        timetorel = findViewById(R.id.btn_timeCon);
//        timestayedrel = findViewById(R.id.barChartDailytimeto);
        //Initialize bar chart
        barChartWeeklytimeto = findViewById(R.id.barChartWeekly);
        barChartWeeklytimestayed = findViewById(R.id.barChartWeekly2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");
        //Initialize List entries
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        //Initialize buttons
        concentration = findViewById(R.id.concentration);
        daily = findViewById(R.id.daily);
        yearly = findViewById(R.id.yearly);
        monthly = findViewById(R.id.monthly);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        memory = findViewById(R.id.memory);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartWeekly);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        getEntries();

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
        //go to relaxation monthly page
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportMonthly.class);
                startActivity(intentr1);
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

        //go to concentration report weekly page
        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
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
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;

        final Handler handler = new Handler();
        final int delay = 5000;

        //initialize file handler
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float textSize = 8f;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(1));

                reference.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("Weekly1 Val", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Log.d("Weekly1Array", String.valueOf(snapshot1.getValue()));
                                Long av1 = (Long) snapshot1.getValue();
                                sumElement.add(snapshot1.getValue());
                                sum += av1;


                            }
                        }
                        Log.d("Weekly1 Array", String.valueOf(sumElement));
                        Log.d("Weekly1 SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));

                        reference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.d("Weekly2 Val", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("Weekly2 Array", String.valueOf(snapshot1.getValue()));
                                        Long av1 = (Long) snapshot1.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum += av1;


                                    }
                                }
                                Log.d("Weekly2 Array", String.valueOf(sumElement));
                                Log.d("Weekly2 SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));

                                reference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("Weekly3Val", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("Weekly3 Array", String.valueOf(snapshot1.getValue()));
                                                Long av1 = (Long) snapshot1.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum += av1;


                                            }
                                        }
                                        Log.d("Weekly 3 Array", String.valueOf(sumElement));
                                        Log.d(" Weekly3SUMT0", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Weekly3AverageTimeTO", String.valueOf(average3));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));

                                        reference.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("Weekly4Array", String.valueOf(snapshot1.getValue()));
                                                        Long av1 = (Long) snapshot1.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum += av1;


                                                    }
                                                }
                                                Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                Log.d("Weekly4 SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average4 = 0L;
                                                }
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));

                                                reference.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("Weekly4Array", String.valueOf(snapshot1.getValue()));
                                                                Long av1 = (Long) snapshot1.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum += av1;


                                                            }
                                                        }
                                                        Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                        Log.d("Weekly4 SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average5 = 0L;
                                                        }


                                                        Log.d("Average Week1", String.valueOf(average1));
                                                        Log.d("Average Week2", String.valueOf(average2));
                                                        Log.d("Average Week3", String.valueOf(average3));
                                                        Log.d("Average Week4", String.valueOf(average4));
                                                        Log.d("Average Week5", String.valueOf(average5));
                                                        List<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f));
                                                        String labels[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};
                                                        List<BarEntry> entries = new ArrayList<>();
                                                        entries.add(new BarEntry(1, Float.parseFloat(String.valueOf(average1))));
                                                        entries.add(new BarEntry(2, Float.parseFloat(String.valueOf(average2))));
                                                        entries.add(new BarEntry(3, Float.parseFloat(String.valueOf(average3))));
                                                        entries.add(new BarEntry(4, Float.parseFloat(String.valueOf(average4))));
                                                        entries.add(new BarEntry(5, Float.parseFloat(String.valueOf(average5))));
//                                                        Initializing arraylist and storinginput data to arraylist
                                                        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsWeek);
                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                        BarData data = new BarData(dataSet);
                                                        data.setDrawValues(false);
                                                        data.setBarWidth(0.7f);


                                                        barChartWeeklytimeto.setData(data);
                                                        barChartWeeklytimeto.setFitBars(true);
                                                        barChartWeeklytimeto.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                                        barChartWeeklytimeto.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                        barChartWeeklytimeto.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        barChartWeeklytimeto.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        barChartWeeklytimeto.getXAxis().setTextSize(textSize);
                                                        barChartWeeklytimeto.getAxisLeft().setTextSize(textSize);
                                                        barChartWeeklytimeto.getXAxis().setLabelCount(11, true);
                                                        barChartWeeklytimeto.setExtraBottomOffset(7f);

                                                        barChartWeeklytimeto.getAxisRight().setEnabled(false);
                                                        Description desc = new Description();
                                                        desc.setText("");
                                                        barChartWeeklytimeto.setDescription(desc);
                                                        barChartWeeklytimeto.getLegend().setEnabled(false);
                                                        barChartWeeklytimeto.getXAxis().setDrawGridLines(false);
                                                        barChartWeeklytimeto.getAxisLeft().setDrawGridLines(false);
                                                        barChartWeeklytimeto.setNoDataText("Data Loading Please Wait...");
                                                        barChartWeeklytimeto.invalidate();

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


        //initialize file handler
        final Handler handler2 = new Handler();
        final int delay2 = 5000;

        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                float textSize = 8f;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(1));

                reference.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("Weekly1 Val", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Log.d("Weekly1Array", String.valueOf(snapshot1.getValue()));
                                Long av1 = (Long) snapshot1.getValue();
                                sumElement.add(snapshot1.getValue());
                                sum += av1;


                            }
                        }
                        Log.d("Weekly1 Array", String.valueOf(sumElement));
                        Log.d("Weekly1 SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average11 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average11 = 0L;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));

                        reference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.d("Weekly2 Val", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("Weekly2 Array", String.valueOf(snapshot1.getValue()));
                                        Long av1 = (Long) snapshot1.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum += av1;


                                    }
                                }
                                Log.d("Weekly2 Array", String.valueOf(sumElement));
                                Log.d("Weekly2 SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average12 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average12 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));

                                reference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("Weekly3 Val", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("Weekly3 Array", String.valueOf(snapshot1.getValue()));
                                                Long av1 = (Long) snapshot1.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum += av1;


                                            }
                                        }
                                        Log.d("Weekly 3 Array", String.valueOf(sumElement));
                                        Log.d(" Weekly3SUMTimeS", String.valueOf(sum));
                                        if (sum != 0) {
                                            average13 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Weekly3AverageTimeS", String.valueOf(average13));
                                        } else {
                                            average13 = 0L;
                                        }

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));

                                        reference.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("Weekly4Array", String.valueOf(snapshot1.getValue()));
                                                        Long av1 = (Long) snapshot1.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum += av1;


                                                    }
                                                }
                                                Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                Log.d("Weekly4 SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average14 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average14 = 0L;
                                                }
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));

                                                reference.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("Weekly4Array", String.valueOf(snapshot1.getValue()));
                                                                Long av1 = (Long) snapshot1.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum += av1;


                                                            }
                                                        }
                                                        Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                        Log.d("Weekly4 SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average15 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average15 = 0L;
                                                        }


                                                        Log.d("Average Week1", String.valueOf(average11));
                                                        Log.d("Average Week2", String.valueOf(average12));
                                                        Log.d("Average Week3", String.valueOf(average13));
                                                        Log.d("Average Week4", String.valueOf(average14));
                                                        Log.d("Average Week5", String.valueOf(average15));
                                                        List<BarEntry> entries2 = new ArrayList<>();
                                                        entries2.add(new BarEntry(1, Float.parseFloat(String.valueOf(average11))));
                                                        entries2.add(new BarEntry(2, Float.parseFloat(String.valueOf(average12))));
                                                        entries2.add(new BarEntry(3, Float.parseFloat(String.valueOf(average13))));
                                                        entries2.add(new BarEntry(4, Float.parseFloat(String.valueOf(average14))));
                                                        entries2.add(new BarEntry(5, Float.parseFloat(String.valueOf(average15))));


                                                        List<Float> creditsMain1 = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f));
                                                        String labels[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};
                                                        //            Initializing object of MyBarDataset class
                                                        MyBarDataset dataSet1 = new MyBarDataset(entries2, "data", creditsMain1);
                                                        dataSet1.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                        BarData data1 = new BarData(dataSet1);
                                                        data1.setDrawValues(false);
                                                        data1.setBarWidth(0.7f);

                                                        barChartWeeklytimestayed.setData(data1);
                                                        barChartWeeklytimestayed.setFitBars(true);
                                                        barChartWeeklytimestayed.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                                        barChartWeeklytimestayed.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                        barChartWeeklytimestayed.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        barChartWeeklytimestayed.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        barChartWeeklytimestayed.getXAxis().setTextSize(textSize);
                                                        barChartWeeklytimestayed.getAxisLeft().setTextSize(textSize);
                                                        barChartWeeklytimestayed.getXAxis().setLabelCount(11, true);
                                                        barChartWeeklytimestayed.setExtraBottomOffset(7f);

                                                        barChartWeeklytimestayed.getAxisRight().setEnabled(false);
                                                        Description desc1 = new Description();
                                                        desc1.setText("");
                                                        barChartWeeklytimestayed.setDescription(desc1);
                                                        barChartWeeklytimestayed.getLegend().setEnabled(false);
                                                        barChartWeeklytimestayed.getXAxis().setDrawGridLines(false);
                                                        barChartWeeklytimestayed.getAxisLeft().setDrawGridLines(false);
                                                        barChartWeeklytimestayed.setNoDataText("Data Loading Please Wait...");

                                                        barChartWeeklytimestayed.invalidate();


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
        }, delay2);

        try {
            fileName = new File(getCacheDir() + "/relRepWeeklyX.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageXAxis.child("relRepWeeklyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/relRepWeeklyY.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageYAxis.child("relRepWeeklyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
                        .child(String.valueOf(month)).child(String.valueOf(1));
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("DataSnapshotWeek1", String.valueOf(dataSnapshot1.getValue()));
                                Double av1 = (Double) dataSnapshot1.getValue();
                                sumIn1 += av1;
                                sumElement.add(av1);
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
                                .child(String.valueOf(month)).child(String.valueOf(2));
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("DataSnapshotWeek2", String.valueOf(dataSnapshot1.getValue()));
                                        Double av1 = (Double) dataSnapshot1.getValue();
                                        sumIn2 += av1;
                                        sumElement.add(av1);
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
                                        .child(String.valueOf(month)).child(String.valueOf(3));
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("DataSnapshotWeek3", String.valueOf(dataSnapshot1.getValue()));
                                                Double av1 = (Double) dataSnapshot1.getValue();
                                                sumIn3 += av1;
                                                sumElement.add(av1);
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
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));
                                        reference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("DataSnapshotWeek4", String.valueOf(dataSnapshot1.getValue()));
                                                        Double av1 = (Double) dataSnapshot1.getValue();
                                                        sumIn4 += av1;
                                                        sumElement.add(av1);
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
                                                        .child(String.valueOf(month)).child(String.valueOf(5));
                                                reference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("DataSnapshotWeek5", String.valueOf(dataSnapshot1.getValue()));
                                                                Double av1 = (Double) dataSnapshot1.getValue();
                                                                sumIn5 += av1;
                                                                sumElement.add(av1);
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
                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(averageIn1))));
                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(averageIn2))));
                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(averageIn3))));
                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(averageIn4))));
                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(averageIn5))));
                                                        String xAxisValues[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};

                                                        lineDataSet = new LineDataSet(lineEntries, "Relaxation Weekly Progress");
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
                                                        lineChart.getXAxis().setLabelCount(7, true);
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