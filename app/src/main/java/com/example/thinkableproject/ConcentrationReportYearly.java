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

public class ConcentrationReportYearly extends AppCompatActivity {
    BarChart barChart2, barChart1;
    AppCompatButton daily, weekly, monthly, whereAmI;
    File fileName, localFile, fileName1, localFile1;
    String text;
    GifImageView c1gif, c2gif;
    Animation scaleUp, scaleDown;

    int color;
    View c1, c2;
    ImageView relaxationBtn, memory;
    FirebaseUser mUser;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Double avg1, avg2, avg3, avg4;
    Double sumIn1 = 0.0;
    Double total1 = 0.0;
    Double sumIn2 = 0.0;
    Double total2 = 0.0;
    Double sumIn3 = 0.0;
    Double total3 = 0.0;
    Double sumIn4 = 0.0;
    Double total4 = 0.0;
    Long average, average1, average2, average3;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_yearly);
        barChart2 = (BarChart) findViewById(R.id.barChartYearly);
        barChart1 = findViewById(R.id.barChartYearly2);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);
        relaxationBtn = findViewById(R.id.relaxation);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        whereAmI = findViewById(R.id.whereAmI);
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
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(year1));
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(year2));
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

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(year3));
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
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
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

                                                barChart2.setData(data);
                                                barChart2.setFitBars(true);
                                                barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                barChart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                barChart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                barChart2.getXAxis().setTextSize(textSize);
                                                barChart2.getAxisLeft().setTextSize(textSize);
                                                barChart2.setExtraBottomOffset(5f);
                                                barChart2.getXAxis().setAvoidFirstLastClipping(true);
                                                barChart2.getXAxis().setLabelCount(9, true);

                                                barChart2.getAxisRight().setEnabled(false);
                                                Description desc = new Description();
                                                desc.setText("");
                                                barChart2.setDescription(desc);
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
        }, delay);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        final Handler handler1 = new Handler();
        final int delay1 = 5000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(year1));
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(year2));
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

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(year3));
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
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
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
                                                List<BarEntry> entries2=new ArrayList<>();
                                                entries2.add(new BarEntry(1,Float.parseFloat(String.valueOf(average1))));
                                                entries2.add(new BarEntry(2,Float.parseFloat(String.valueOf(average2))));
                                                entries2.add(new BarEntry(3,Float.parseFloat(String.valueOf(average3))));
                                                entries2.add(new BarEntry(4,Float.parseFloat(String.valueOf(average))));

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

                                                barChart1.setData(data1);
                                                barChart1.setFitBars(true);
                                                barChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                                barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                barChart1.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                barChart1.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                barChart1.getXAxis().setTextSize(textSize);
                                                barChart1.getAxisLeft().setTextSize(textSize);
                                                barChart1.getXAxis().setAvoidFirstLastClipping(true);
                                                barChart1.setExtraBottomOffset(5f);
                                                barChart1.getXAxis().setLabelCount(9, true);

                                                barChart1.getAxisRight().setEnabled(false);
                                                Description desc1 = new Description();
                                                desc1.setText("");
                                                barChart1.setDescription(desc1);
                                                barChart1.getLegend().setEnabled(false);
                                                barChart1.getXAxis().setDrawGridLines(false);
                                                barChart1.getAxisLeft().setDrawGridLines(false);
                                                barChart1.setNoDataText("Data Loading Please Wait");

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
        }, delay);

        mUser = FirebaseAuth.getInstance().

                getCurrentUser(); // get current user
        mUser.getUid();


        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
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
        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        // On click listener of relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportYearly.class);
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

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(year1));

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesYear1", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesYear1Data", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataYear1", String.valueOf(dataSnapshot2.getKey()));
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Log.d("DataFinYear", dataSnapshot3.getKey());
                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                            Log.d("KEY", dataSnapshot4.getKey());
                                            for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()) {
                                                Log.d("KEYFINAL", dataSnapshot5.getKey());
                                                if (dataSnapshot5.getKey().equals("index")) {
                                                    dataSnapshot5.getValue();
                                                    Log.d("DataF", String.valueOf(dataSnapshot5.getValue()));
                                                    Double av1 = (Double) dataSnapshot5.getValue();
                                                    sumElement.add(av1);
                                                    sumIn1 += av1;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalYear1", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sumIn1 = 0.0;
                            avg1 = 0.0;
                        } else {
                            Log.d("SUMYear1", String.valueOf(total1));
                            avg1 = total1 / sumElement.size();
                            Log.d("AverageYear1", String.valueOf(avg1));
                        }
                        Log.d("AverageYear1", String.valueOf(avg1));
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(year2));

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesYear2", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesYear2Data", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataYear2", String.valueOf(dataSnapshot2.getKey()));
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("DataFinYear2", dataSnapshot3.getKey());
                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                    Log.d("KEY2", dataSnapshot4.getKey());
                                                    for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()) {
                                                        Log.d("KEYFINAL2", dataSnapshot5.getKey());
                                                        if (dataSnapshot5.getKey().equals("index")) {
                                                            dataSnapshot5.getValue();
                                                            Log.d("DataF2", String.valueOf(dataSnapshot5.getValue()));
                                                            Double av1 = (Double) dataSnapshot5.getValue();
                                                            sumElement.add(av1);
                                                            sumIn2 += av1;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalYear2", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sumIn2 = 0.0;
                                    avg2 = 0.0;
                                } else {
                                    Log.d("SUMYear2", String.valueOf(total2));
                                    avg2 = total2 / sumElement.size();
                                    Log.d("AverageYear2", String.valueOf(avg2));
                                }
                                Log.d("AverageYear2", String.valueOf(avg2));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(year3));

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesYear3", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesYear3Data", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataYear3", String.valueOf(dataSnapshot2.getKey()));
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Log.d("DataFinYear3", dataSnapshot3.getKey());
                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                            Log.d("KEY", dataSnapshot4.getKey());
                                                            for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()) {
                                                                Log.d("KEYFINAL3", dataSnapshot5.getKey());
                                                                if (dataSnapshot5.getKey().equals("index")) {
                                                                    dataSnapshot5.getValue();
                                                                    Log.d("DataF3", String.valueOf(dataSnapshot5.getValue()));
                                                                    Double av1 = (Double) dataSnapshot5.getValue();
                                                                    sumElement.add(av1);
                                                                    sumIn3 += av1;
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalYear3", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sumIn3 = 0.0;
                                            avg3 = 0.0;
                                        } else {
                                            Log.d("SUMYear3", String.valueOf(total3));
                                            avg3 = total3 / sumElement.size();
                                            Log.d("AverageYear3", String.valueOf(avg3));
                                        }
                                        Log.d("AverageYear3", String.valueOf(avg3));
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)));

                                        reference1.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("ValuesYear4", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesYear4Data", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("DataYear4", String.valueOf(dataSnapshot2.getKey()));
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Log.d("DataFinYear4", dataSnapshot3.getKey());
                                                                for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                                    Log.d("KEY", dataSnapshot4.getKey());
                                                                    for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()) {
                                                                        Log.d("KEYFINAL4", dataSnapshot5.getKey());
                                                                        if (dataSnapshot5.getKey().equals("index")) {
                                                                            dataSnapshot5.getValue();
                                                                            Log.d("DataF4", String.valueOf(dataSnapshot5.getValue()));
                                                                            Double av1 = (Double) dataSnapshot5.getValue();
                                                                            sumElement.add(av1);
                                                                            sumIn4 += av1;
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total4 += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalYear4", String.valueOf(total4));

                                                if (total4 == 0.0) {
                                                    sumIn4 = 0.0;
                                                    avg4 = 0.0;
                                                } else {
                                                    Log.d("SUMYear4", String.valueOf(total4));
                                                    avg4 = total4 / sumElement.size();
                                                    Log.d("AverageYear4", String.valueOf(avg4));
                                                }
                                                Log.d("AverageYear4", String.valueOf(avg4));
                                                Log.d("Average1", String.valueOf(avg1));
                                                Log.d("Average2", String.valueOf(avg2));
                                                Log.d("Average3", String.valueOf(avg3));
                                                Log.d("Average4", String.valueOf(avg4));


                                                lineEntries.add(new Entry(year1, Float.parseFloat(String.valueOf(avg1))));
                                                lineEntries.add(new Entry(year2, Float.parseFloat(String.valueOf(avg2))));
                                                lineEntries.add(new Entry(year3, Float.parseFloat(String.valueOf(avg2))));
                                                lineEntries.add(new Entry(now.get(Calendar.YEAR), Float.parseFloat(String.valueOf(avg4))));

//                                                    List<String> xAxisValues = new ArrayList<>(Arrays.asList(String.valueOf(year1),String.valueOf(year2),String.valueOf(year3),String.valueOf(now.get(Calendar.YEAR))));
                                                List<String> xAxisValues = new ArrayList<>(Arrays.asList("", String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))));
                                                lineDataSet = new LineDataSet(lineEntries, "Concentration Yearly Progress");
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
                                                lineChart.getXAxis().setLabelCount(12, true);
                                                lineChart.setExtraBottomOffset(5f);
                                                lineChart.getXAxis().setAvoidFirstLastClipping(true);

                                                lineChart.setScaleEnabled(false);
                                                lineChart.setPinchZoom(false);
                                                lineChart.setDrawGridBackground(false);
                                                lineChart.getDescription().setTextColor(R.color.white);
                                                lineChart.invalidate();
                                                lineChart.refreshDrawableState();

                                                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
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

    public class MyBarDataset extends BarDataSet {

        private List<Float> creditsWeek;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> creditsWeek) {
            super(yVals, label);
            this.creditsWeek = creditsWeek;
        }

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
}