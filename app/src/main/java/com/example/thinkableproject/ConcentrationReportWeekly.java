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

public class ConcentrationReportWeekly extends AppCompatActivity {
    BarChart barChart1, barChart2;
    AppCompatButton monthly;
    AppCompatButton yearly;
    AppCompatButton daily, whereAmI;
    File fileName, localFile, fileName1, localFile1;
    FirebaseUser mUser;
    GifImageView c1gif, c2gif;
    Animation scaleUp, scaleDown;

    int color;
    View c1, c2;
    Double avg1, avg2, avg3, avg4, avg5;
    ImageView relaxationBtn, memory;
    String text;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();
    LineChart lineChart;
    LineData lineData;
    Long average1, average2, average3, average4, average5;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Double sum1In = 0.0;
    Double total1 = 0.0;
    Double sum2In = 0.0;
    Double total2 = 0.0;
    Double sum3In = 0.0;
    Double total3 = 0.0;
    Double sumIn = 0.0;
    Double total = 0.0;
    Double sum4In = 0.0;
    Double total4 = 0.0;
    Double sum5In = 0.0;
    Double total5 = 0.0;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_weekly);
        barChart1 = (BarChart) findViewById(R.id.barChartWeekly);
        barChart2 = findViewById(R.id.barChartWeekly2);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        daily = findViewById(R.id.daily);
        relaxationBtn = findViewById(R.id.relaxation);
        whereAmI = findViewById(R.id.whereAmI);
        memory = findViewById(R.id.memory);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartWeekly);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

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

        getEntries();


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
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;

        //Initializing arraylist and storing input data to arraylist

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage

        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                float textSize = 8f;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child(mUser.getUid()).child("Concentration").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                        Log.d(" Weekly3SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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


                                                        barChart1.setData(data);
                                                        barChart1.setFitBars(true);
                                                        barChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                                        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                        barChart1.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        barChart1.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        barChart1.getXAxis().setTextSize(textSize);
                                                        barChart1.getAxisLeft().setTextSize(textSize);
                                                        barChart1.getXAxis().setLabelCount(11, true);
                                                        barChart1.setExtraBottomOffset(7f);

                                                        barChart1.getAxisRight().setEnabled(false);
                                                        Description desc = new Description();
                                                        desc.setText("");
                                                        barChart1.setDescription(desc);
                                                        barChart1.getLegend().setEnabled(false);
                                                        barChart1.getXAxis().setDrawGridLines(false);
                                                        barChart1.getAxisLeft().setDrawGridLines(false);
                                                        barChart1.setNoDataText("Data Loading Please Wait...");
                                                        barChart1.invalidate();

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
        }, delay);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        final Handler handler1 = new Handler();

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {

                float textSize = 8f;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                        Log.d(" Weekly3SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                        List<BarEntry> entries2 = new ArrayList<>();
                                                        entries2.add(new BarEntry(1, Float.parseFloat(String.valueOf(average1))));
                                                        entries2.add(new BarEntry(2, Float.parseFloat(String.valueOf(average2))));
                                                        entries2.add(new BarEntry(3, Float.parseFloat(String.valueOf(average3))));
                                                        entries2.add(new BarEntry(4, Float.parseFloat(String.valueOf(average4))));
                                                        entries2.add(new BarEntry(5, Float.parseFloat(String.valueOf(average5))));


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

                                                        barChart2.setData(data1);
                                                        barChart2.setFitBars(true);
                                                        barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                                        barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                        barChart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        barChart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        barChart2.getXAxis().setTextSize(textSize);
                                                        barChart2.getAxisLeft().setTextSize(textSize);
                                                        barChart2.getXAxis().setLabelCount(11, true);
                                                        barChart2.setExtraBottomOffset(7f);

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


//


            //Downloading file and displaying chart
        }, delay);


//
        mUser = FirebaseAuth.getInstance().

                getCurrentUser(); // get current user
        mUser.getUid();


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

        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportMonthly.class);
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
                Intent intent = new Intent(getApplicationContext(), RelaxationReportWeekly.class);
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
        // On click listener of where am i button
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
                        .child(String.valueOf(month)).child(String.valueOf(1));

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesWeek1", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataWeek1", String.valueOf(dataSnapshot2.getKey()));
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Log.d("DataFin", dataSnapshot3.getKey());
                                        if (dataSnapshot3.getKey().equals("index")) {
                                            dataSnapshot3.getValue();
                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                            Double av1 = (Double) dataSnapshot3.getValue();
                                            sumElement.add(av1);
                                            sumIn += av1;
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalWeek1", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sumIn = 0.0;
                            avg1 = 0.0;
                        } else {
                            Log.d("SUMWeek1", String.valueOf(total1));
                            avg1 = total1 / sumElement.size();
                            Log.d("AverageWeek1", String.valueOf(avg1));
                        }
                        Log.d("AverageWeek1", String.valueOf(avg1));
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesWeek1", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataWeek1", String.valueOf(dataSnapshot2.getKey()));
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("DataFin", dataSnapshot3.getKey());
                                                if (dataSnapshot3.getKey().equals("index")) {
                                                    dataSnapshot3.getValue();
                                                    Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                    Double av1 = (Double) dataSnapshot3.getValue();
                                                    sumElement.add(av1);
                                                    sum1In += av1;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalWeek2", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sum1In = 0.0;
                                    avg2 = 0.0;
                                } else {
                                    Log.d("SUMWeek2", String.valueOf(total2));
                                    avg2 = total2 / sumElement.size();
                                    Log.d("AverageWeek2", String.valueOf(avg2));
                                }
                                Log.d("AverageWeek2", String.valueOf(avg1));

                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesWeek3", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataWeek3", String.valueOf(dataSnapshot2.getKey()));
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Log.d("DataFin", dataSnapshot3.getKey());
                                                        if (dataSnapshot3.getKey().equals("index")) {
                                                            dataSnapshot3.getValue();
                                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                            Double av1 = (Double) dataSnapshot3.getValue();
                                                            sumElement.add(av1);
                                                            sum2In += av1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalWeek3", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sum2In = 0.0;
                                            avg3 = 0.0;
                                        } else {
                                            Log.d("SUMWeek3", String.valueOf(total3));
                                            avg3 = total3 / sumElement.size();
                                            Log.d("AverageWeek3", String.valueOf(avg3));
                                        }
                                        Log.d("AverageWeek3", String.valueOf(avg3));
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));

                                        reference1.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("ValuesWeek3", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("DataWeek3", String.valueOf(dataSnapshot2.getKey()));
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Log.d("DataFin", dataSnapshot3.getKey());
                                                                if (dataSnapshot3.getKey().equals("index")) {
                                                                    dataSnapshot3.getValue();
                                                                    Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                                    Double av1 = (Double) dataSnapshot3.getValue();
                                                                    sumElement.add(av1);
                                                                    sum3In += av1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total4 += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalWeek4", String.valueOf(total4));

                                                if (total4 == 0.0) {
                                                    sum3In = 0.0;
                                                    avg4 = 0.0;
                                                } else {
                                                    Log.d("SUMWeek4", String.valueOf(total4));
                                                    avg4 = total4 / sumElement.size();
                                                    Log.d("AverageWeek4", String.valueOf(avg4));
                                                }
                                                Log.d("AverageWeek4", String.valueOf(avg4));

                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));

                                                reference1.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("ValuesWeek5", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                    Log.d("DataWeek5", String.valueOf(dataSnapshot2.getKey()));
                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                        Log.d("DataFin", dataSnapshot3.getKey());
                                                                        if (dataSnapshot3.getKey().equals("index")) {
                                                                            dataSnapshot3.getValue();
                                                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                                            Double av1 = (Double) dataSnapshot3.getValue();
                                                                            sumElement.add(av1);
                                                                            sum4In += av1;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                            total5 += (Double) sumElement.get(i);
                                                        }
                                                        Log.d("TotalWeek5", String.valueOf(total5));

                                                        if (total5 == 0.0) {
                                                            sum4In = 0.0;
                                                            avg5 = 0.0;
                                                        } else {
                                                            Log.d("SUMWeek5", String.valueOf(total5));
                                                            avg5 = total5 / sumElement.size();
                                                            Log.d("AverageWeek5", String.valueOf(avg5));
                                                        }
                                                        Log.d("AverageWeek5", String.valueOf(avg5));

                                                        Log.d("AverageData6", String.valueOf(avg1));
                                                        Log.d("Average Outside1 Con", String.valueOf(avg1));
                                                        Log.d("Average Outside2 Con", String.valueOf(avg2));
                                                        Log.d("Average Outside3 Con", String.valueOf(avg3));
                                                        Log.d("Average Outside4 Con", String.valueOf(avg4));
                                                        Log.d("Average Outside5 Con", String.valueOf(avg5));

                                                        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
////
////
                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(avg1))));
                                                        Log.d("FloatAverage", String.valueOf(Float.parseFloat(String.valueOf(avg1))));
                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(avg2))));
                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(avg3))));
                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(avg4))));
                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(avg5))));
                                                        String xAxisValues[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};

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
//
//                                                                    }

//                                                                    //
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                                    }
//                                                                });
////
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        });
////
////
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
//
////
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
////
////
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//
////
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


}