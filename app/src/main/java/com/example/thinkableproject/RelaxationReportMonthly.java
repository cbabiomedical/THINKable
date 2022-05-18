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

        //Array list to write data to file
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f, 43f, 23f, 70f, 73f, 10f));

        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/relaxationmonthlytimeto.txt");
            String line = "";
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj.size();
            for (int i = 0; i < size; i++) {
                output.write(obj.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1.child("relaxationmonthlytimeto.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //initialize file handler
        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //Downloading file and displaying chart
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/relaxationmonthlytimeto.txt");

                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportMonthly.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                Log.d("FileName", localFile.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list));

                                for (int i = 0; i < list.size(); i++) {
                                    floatList.add(Float.parseFloat(list.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("floatListTest", String.valueOf(floatList));
                            String[] months = new String[]{"Ja", "Fe", "Mar", "Ap", "May", "Jun", "Jul", "Au", "Se", "Oc", "No", "De"};
                            List<Float> credits = new ArrayList<>(Arrays.asList(90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f));
                            float[] strength = new float[]{90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f};


                            for (int j = 0; j < floatList.size(); ++j) {
                                entries.add(new BarEntry(j, floatList.get(j)));
                            }


                            float textSize = 16f;
                            RelaxationReportMonthly.MyBarDataset dataSet = new RelaxationReportMonthly.MyBarDataset(entries, "data", credits);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.8f);

                            barChartMonthlytimeto.setData(data);
                            barChartMonthlytimeto.setFitBars(true);
                            barChartMonthlytimeto.getXAxis
                                    ().setValueFormatter(new IndexAxisValueFormatter(months));
                            barChartMonthlytimeto.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartMonthlytimeto.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            barChartMonthlytimeto.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            barChartMonthlytimeto.getXAxis().setTextSize(textSize);
                            barChartMonthlytimeto.getAxisLeft().setTextSize(textSize);
                            barChartMonthlytimeto.setExtraBottomOffset(10f);

                            barChartMonthlytimeto.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("");
                            barChartMonthlytimeto.setDescription(desc);
                            barChartMonthlytimeto.getLegend().setEnabled(false);
                            barChartMonthlytimeto.getXAxis().setDrawGridLines(false);
                            barChartMonthlytimeto.getAxisLeft().setDrawGridLines(false);
                            barChartMonthlytimeto.setNoDataText("Data Loading Please Wait...");

                            barChartMonthlytimeto.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportMonthly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
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

        //Array list to write data to file
        ArrayList<Float> obj2 = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f, 43f, 23f, 70f, 73f, 10f));

        //Writing data to file
        try {
            fileName2 = new File(getCacheDir() + "/relaxationmonthlytimestayed.txt");
            String line = "";
            FileWriter fw;
            fw = new FileWriter(fileName2);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj2.size();
            for (int i = 0; i < size; i++) {
                output.write(obj2.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference2.child("relaxationmonthlytimestayed.txt");
            InputStream stream = new FileInputStream(new File(fileName2.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //initialize file handler
        final Handler handler1 = new Handler();
        final int delay1 = 7000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                //Downloading file and displaying chart
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/relaxationmonthlytimestayed.txt");

                try {
                    localFile2 = File.createTempFile("tempFile", ".txt");
                    text2 = localFile2.getAbsolutePath();
                    Log.d("Bitmap", text2);
                    storageReference.getFile(localFile2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportMonthly.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile2.getAbsolutePath()));

                                Log.d("FileName", localFile2.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list2.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list2.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list2));

                                for (int i = 0; i < list2.size(); i++) {
                                    floatList2.add(Float.parseFloat(list2.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList2));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("floatListTest", String.valueOf(floatList2));
                            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                            List<Float> credits = new ArrayList<>(Arrays.asList(90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f));
                            float[] strength = new float[]{90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f};


                            for (int j = 0; j < floatList2.size(); ++j) {
                                entries2.add(new BarEntry(j, floatList2.get(j)));
                            }


                            float textSize = 16f;
                            RelaxationReportMonthly.MyBarDataset dataSet = new RelaxationReportMonthly.MyBarDataset(entries2, "data", credits);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.8f);

                            barChartMonthlytimestayed.setData(data);
                            barChartMonthlytimestayed.setFitBars(true);
                            barChartMonthlytimestayed.getXAxis
                                    ().setValueFormatter(new IndexAxisValueFormatter(months));
                            barChartMonthlytimestayed.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartMonthlytimestayed.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            barChartMonthlytimestayed.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            barChartMonthlytimestayed.getXAxis().setTextSize(textSize);
                            barChartMonthlytimestayed.getAxisLeft().setTextSize(textSize);
                            barChartMonthlytimestayed.setExtraBottomOffset(10f);

                            barChartMonthlytimestayed.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("");
                            barChartMonthlytimestayed.setDescription(desc);
                            barChartMonthlytimestayed.getLegend().setEnabled(false);
                            barChartMonthlytimestayed.getXAxis().setDrawGridLines(false);
                            barChartMonthlytimestayed.getAxisLeft().setDrawGridLines(false);
                            barChartMonthlytimestayed.setNoDataText("Data Loading Please Wait...");

                            barChartMonthlytimestayed.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportMonthly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }, delay1);

        try {
            fileName = new File(getCacheDir() + "/relRepMonthlyX.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageXAxis.child("relRepMonthlyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/relRepMonthlyY.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageYAxis.child("relRepMonthlyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportMonthly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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