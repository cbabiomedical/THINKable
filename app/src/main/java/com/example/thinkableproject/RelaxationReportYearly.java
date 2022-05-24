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

public class RelaxationReportYearly extends AppCompatActivity {

    BarChart barChartYearlytimeto, barChartYearlytimestayed;
    AppCompatButton daily, weekly, monthly, whereAmI, progress, timetorel, timestayedrel;
    FirebaseUser mUser;
    String text;
    String text2;
    File localFile;
    Animation scaleUp, scaleDown;
    Double averageIn1 = 0.0;
    Double averageIn2 = 0.0;
    Double averageIn3 = 0.0;
    Double averageIn = 0.0;
    Double sumIn1 = 0.0;
    Double sumIn2 = 0.0;
    Double sumIn3 = 0.0;
    Double sumIn4 = 0.0;
    Long average, average1, average2, average3;

    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    File localFile2;
    File fileName;
    File fileName2;
    ImageView concentration, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<Float> floatList2 = new ArrayList<>();

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_yearly);

        //Initialize buttons
        whereAmI = findViewById(R.id.whereAmI);
        progress = findViewById(R.id.progress);
//        timetorel = findViewById(R.id.btn_timeCon);
//        timestayedrel = findViewById(R.id.barChartDailytimeto);
        //Initialize bar chart
        barChartYearlytimeto = findViewById(R.id.barChartYearly);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        barChartYearlytimestayed = findViewById(R.id.barChartYearly2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");
        //Initialize List entries
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        //Initialize buttons
        concentration = findViewById(R.id.concentration);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);
        memory = findViewById(R.id.memory);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartYearly);

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

        getEntries();
//        lineDataSet = new LineDataSet(lineEntries, "concentration");
//        lineData = new LineData(lineDataSet);
//        lineChart.setData(lineData);
//
//        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        lineDataSet.setValueTextColor(Color.WHITE);
//        lineDataSet.setValueTextSize(10f);
//        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//        lineChart.setBorderColor(Color.TRANSPARENT);
//        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//        lineChart.getAxisLeft().setDrawGridLines(false);
//        lineChart.getXAxis().setDrawGridLines(false);
//        lineChart.getAxisRight().setDrawGridLines(false);

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
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportYearly.class);
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
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);
        //initialize file handler
        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year1));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                        Long av1 = (Long) snapshot3.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum += av1;
                                    }
                                }
                            }
                        }
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year2));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                Long av1 = (Long) snapshot3.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum += av1;
                                            }
                                        }
                                    }
                                }
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year3));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                        Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                        Long av1 = (Long) snapshot3.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum += av1;
                                                    }
                                                }
                                            }
                                        }
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                                Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                                Long av1 = (Long) snapshot3.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum += av1;
                                                            }
                                                        }
                                                    }
                                                }
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average = 0L;
                                                }
                                                Log.d("Average", String.valueOf(average));
                                                Log.d("Average1", String.valueOf(average1));
                                                Log.d("Average2", String.valueOf(average2));
                                                Log.d("Average4", String.valueOf(average3));
                                                List<BarEntry> entries = new ArrayList<>();


                                                entries.add(new BarEntry(year1, Float.parseFloat(String.valueOf(average1))));
                                                entries.add(new BarEntry(year2, Float.parseFloat(String.valueOf(average2))));
                                                entries.add(new BarEntry(year3, Float.parseFloat(String.valueOf(average3))));
                                                entries.add(new BarEntry(now.get(Calendar.YEAR), Float.parseFloat(String.valueOf(average))));

//                                                    List<String> xAxisValues = new ArrayList<>(Arrays.asList(String.valueOf(year1),String.valueOf(year2),String.valueOf(year3),String.valueOf(now.get(Calendar.YEAR))));
                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))));
                                                ArrayList<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f));
                                                String[] years = new String[]{"", String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))};

                                                float textSize = 10f;
                                                //Initializing object of MyBarDataset class
                                                MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsWeek);
                                                dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                BarData data = new BarData(dataSet);
                                                data.setDrawValues(false);
                                                data.setBarWidth(0.8f);

                                                barChartYearlytimeto.setData(data);
                                                barChartYearlytimeto.setFitBars(true);
                                                barChartYearlytimeto.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                barChartYearlytimeto.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                barChartYearlytimeto.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                barChartYearlytimeto.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                barChartYearlytimeto.getXAxis().setTextSize(textSize);
                                                barChartYearlytimeto.getAxisLeft().setTextSize(textSize);
                                                barChartYearlytimeto.setExtraBottomOffset(5f);
                                                barChartYearlytimeto.getXAxis().setAvoidFirstLastClipping(true);
                                                barChartYearlytimeto.getXAxis().setLabelCount(9, true);

                                                barChartYearlytimeto.getAxisRight().setEnabled(false);
                                                Description desc = new Description();
                                                desc.setText("");
                                                barChartYearlytimeto.setDescription(desc);
                                                barChartYearlytimeto.getLegend().setEnabled(false);
                                                barChartYearlytimeto.getXAxis().setDrawGridLines(false);
                                                barChartYearlytimeto.getAxisLeft().setDrawGridLines(false);
                                                barChartYearlytimeto.setNoDataText("Data Loading Please Wait...");
                                                barChartYearlytimeto.invalidate();

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
        final int delay2 = 7000;

        handler2.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year1));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                        Long av1 = (Long) snapshot3.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum += av1;
                                    }
                                }
                            }
                        }
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year2));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                Long av1 = (Long) snapshot3.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum += av1;
                                            }
                                        }
                                    }
                                }
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(year3));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                        Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                        Long av1 = (Long) snapshot3.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum += av1;
                                                    }
                                                }
                                            }
                                        }
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                                Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                                Long av1 = (Long) snapshot3.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum += av1;
                                                            }
                                                        }
                                                    }
                                                }
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average = 0L;
                                                }
                                                Log.d("Average", String.valueOf(average));
                                                Log.d("Average1", String.valueOf(average1));
                                                Log.d("Average2", String.valueOf(average2));
                                                Log.d("Average4", String.valueOf(average3));


//                                                    List<String> xAxisValues = new ArrayList<>(Arrays.asList(String.valueOf(year1),String.valueOf(year2),String.valueOf(year3),String.valueOf(now.get(Calendar.YEAR))));
                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))));
                                                float textSize = 10f;
                                                ArrayList<Float> creditsMain1 = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f));
                                                List<BarEntry> entries2 = new ArrayList<>();
                                                entries2.add(new BarEntry(1, Float.parseFloat(String.valueOf(average1))));
                                                entries2.add(new BarEntry(2, Float.parseFloat(String.valueOf(average2))));
                                                entries2.add(new BarEntry(3, Float.parseFloat(String.valueOf(average3))));
                                                entries2.add(new BarEntry(4, Float.parseFloat(String.valueOf(average))));

                                                //Initializing object of MyBarDataset class
                                               MyBarDataset dataSet1 = new MyBarDataset(entries2, "data", creditsMain1);
                                                dataSet1.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                BarData data1 = new BarData(dataSet1);
                                                data1.setDrawValues(false);
                                                data1.setBarWidth(0.9f);

                                                barChartYearlytimestayed.setData(data1);
                                                barChartYearlytimestayed.setFitBars(true);
                                                barChartYearlytimestayed.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                barChartYearlytimestayed.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                barChartYearlytimestayed.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                barChartYearlytimestayed.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                barChartYearlytimestayed.getXAxis().setTextSize(textSize);
                                                barChartYearlytimestayed.getAxisLeft().setTextSize(textSize);
                                                barChartYearlytimestayed.getXAxis().setAvoidFirstLastClipping(true);
                                                barChartYearlytimestayed.setExtraBottomOffset(5f);
                                                barChartYearlytimestayed.getXAxis().setLabelCount(9, true);

                                                barChartYearlytimestayed.getAxisRight().setEnabled(false);
                                                Description desc1 = new Description();
                                                desc1.setText("");
                                                barChartYearlytimestayed.setDescription(desc1);
                                                barChartYearlytimestayed.getLegend().setEnabled(false);
                                                barChartYearlytimestayed.getXAxis().setDrawGridLines(false);
                                                barChartYearlytimestayed.getAxisLeft().setDrawGridLines(false);
                                                barChartYearlytimestayed.setNoDataText("Data Loading Please Wait");

                                                barChartYearlytimestayed.invalidate();


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


        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();


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
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                lineEntries = new ArrayList();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(year1));
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Log.d("YEARRELAXATION", String.valueOf(dataSnapshot3.getValue()));
                                        Double av1 = (Double) dataSnapshot3.getValue();
                                        sumElement.add(av1);
                                        sumIn1 += av1;
                                    }
                                }
                            }
                        }
                        if (sumIn1 != 0.0) {
                            Log.d("SUMYear1", String.valueOf(sumIn1));
                            averageIn1 = sumIn1 / sumElement.size();
                            Log.d("AverageYear1", String.valueOf(averageIn1));
                        } else {
                            averageIn1 = 0.0;
                            sumIn1 = 0.0;
                        }
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(year2));
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("YEARRELAXATION", String.valueOf(dataSnapshot3.getValue()));
                                                Double av1 = (Double) dataSnapshot3.getValue();
                                                sumElement.add(av1);
                                                sumIn2 += av1;
                                            }
                                        }
                                    }
                                }
                                if (sumIn2 != 0.0) {
                                    Log.d("SUMYear2", String.valueOf(sumIn2));
                                    averageIn2 = sumIn2 / sumElement.size();
                                    Log.d("AverageYear2", String.valueOf(averageIn2));
                                } else {
                                    averageIn2 = 0.0;
                                    sumIn2 = 0.0;
                                }
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(year3));
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Log.d("YEARRELAXATION", String.valueOf(dataSnapshot3.getValue()));
                                                        Double av1 = (Double) dataSnapshot3.getValue();
                                                        sumElement.add(av1);
                                                        sumIn3 += av1;
                                                    }
                                                }
                                            }
                                        }
                                        if (sumIn3 != 0.0) {
                                            Log.d("SUMYear3", String.valueOf(sumIn3));
                                            averageIn3 = sumIn3 / sumElement.size();
                                            Log.d("AverageYear3", String.valueOf(averageIn3));
                                        } else {
                                            averageIn3 = 0.0;
                                            sumIn3 = 0.0;
                                        }

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                        reference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Log.d("YEARRELAXATION", String.valueOf(dataSnapshot3.getValue()));
                                                                Double av1 = (Double) dataSnapshot3.getValue();
                                                                sumElement.add(av1);
                                                                sumIn4 += av1;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (sumIn4 != 0.0) {
                                                    Log.d("SUMCurrent", String.valueOf(sumIn4));
                                                    averageIn = sumIn4 / sumElement.size();
                                                    Log.d("AverageCurrent", String.valueOf(averageIn));
                                                } else {
                                                    averageIn = 0.0;
                                                    sumIn4 = 0.0;
                                                }
                                                lineEntries.add(new Entry(year1, Float.parseFloat(String.valueOf(averageIn1))));
                                                lineEntries.add(new Entry(year2, Float.parseFloat(String.valueOf(averageIn2))));
                                                lineEntries.add(new Entry(year3, Float.parseFloat(String.valueOf(averageIn3))));
                                                lineEntries.add(new Entry(now.get(Calendar.YEAR), Float.parseFloat(String.valueOf(averageIn))));

                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))));
                                                lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

                                                Log.d("Line Entry", String.valueOf(lineEntries));
                                                lineDataSet = new LineDataSet(lineEntries, "Yearly Relaxation Index");
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
        }, 10000);
    }

    //set up x and y axis data
    public class MyBarDataset extends BarDataSet {

        private List<Float> creditsWeek;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> creditsWeek) {
            super(yVals, label);
            this.creditsWeek = creditsWeek;
        }

        //set up color of bars on chart
        @Override
        public int getColor(int index) {
            float c = creditsWeek.get(index);

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