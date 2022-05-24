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

public class MemoryReportDaily extends AppCompatActivity {
    BarChart barChartdaily, barChartdaily2;
    private Context context;
    AppCompatButton monthly, yearly, weekly, whereAmI;
    File fileName, fileName1, localFile, localFile1;
    FirebaseUser mUser;
    ImageView relaxationBtn, concentrationBtn;
    String text;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();
    Double sum = 0.0;

    Double sum4 = 0.0;
    Double sum1 = 0.0;
    Double sum2 = 0.0;
    Double sum3 = 0.0;
    Double sum5 = 0.0;
    Double total5 = 0.0;
    Double sum6 = 0.0;

    LineChart lineChart;
    Animation scaleUp, scaleDown;

    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_memory_daily);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        barChartdaily2 = (BarChart) findViewById(R.id.barChartDaily2);
        monthly = findViewById(R.id.monthly);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        relaxationBtn = findViewById(R.id.relaxation);
        whereAmI = findViewById(R.id.whereAmI);
        concentrationBtn = findViewById(R.id.concentration);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartDaily);

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
        //Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));
        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/reportMemDaily.txt");
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
            StorageReference mountainsRef = storageReference1.child("reportMemDaily.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemDaily.txt");
                //downloading the uploaded file and storing in arraylist
                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportDaily.this, "Success", Toast.LENGTH_SHORT).show();

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

                            String[] days = new String[]{"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
                            List<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));
                            float[] strengthDay = new float[]{90f, 30f, 70f, 50f, 10f, 15f, 85f};

                            List<BarEntry> entries = new ArrayList<>();
                            for (int j = 0; j < floatList.size(); ++j) {
                                entries.add(new BarEntry(j, floatList.get(j)));
                            }
                            float textSize = 16f;
                            //Initializing object of MyBarDataset class
                            MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.9f);

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
                            desc.setText("");
                            barChartdaily.setDescription(desc);
                            barChartdaily.getLegend().setEnabled(false);
                            barChartdaily.getXAxis().setDrawGridLines(false);
                            barChartdaily.getAxisLeft().setDrawGridLines(false);
                            barChartdaily.setNoDataText("Data Loading Please Wait....");

                            barChartdaily.invalidate();

//
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportDaily.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay);
//Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj1 = new ArrayList<>(
                Arrays.asList(60f, 40f, 70f, 20f, 20f, 50f, 80f));  //Array list to write data to file
        //Write input data to file
        try {
            fileName1 = new File(getCacheDir() + "/reportMemDaily2.txt");  //Writing data to file
            String line = "";
            FileWriter fw;
            fw = new FileWriter(fileName1);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj1.size();
            for (int i = 0; i < size; i++) {
                output.write(obj1.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        //downloading the uploaded file and storing in arraylist
        try {
            StorageReference mountainsRef = storageReference1.child("reportMemDaily2.txt");
            InputStream stream = new FileInputStream(new File(fileName1.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Handler handler1 = new Handler();
        final int delay1 = 5000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemDaily2.txt");

                try {
                    localFile1 = File.createTempFile("tempFile", ".txt");
                    text = localFile1.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportDaily.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReader1 = new InputStreamReader(new FileInputStream(localFile1.getAbsolutePath()));

                                Log.d("FileName", localFile1.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader1);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list1.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list1.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list1));

                                for (int i = 0; i < list1.size(); i++) {
                                    floatList1.add(Float.parseFloat(list1.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList1));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String[] days = new String[]{"Mo", "Tu", "We", "Thu", "Fr", "Sa", "Su"};
                            List<Float> creditsMain1 = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));
                            float[] strengthDay = new float[]{90f, 30f, 70f, 50f, 10f, 15f, 85f};

                            List<BarEntry> entries2 = new ArrayList<>();
                            for (int j = 0; j < floatList1.size(); ++j) {
                                entries2.add(new BarEntry(j, floatList1.get(j)));
                            }
                            float textSize = 16f;
                            MyBarDataset dataSet1 = new MyBarDataset(entries2, "data", creditsMain1);
                            dataSet1.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data1 = new BarData(dataSet1);
                            data1.setDrawValues(false);
                            data1.setBarWidth(0.9f);

                            barChartdaily2.setData(data1);
                            barChartdaily2.setFitBars(true);
                            barChartdaily2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
                            barChartdaily2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartdaily2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            barChartdaily2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            barChartdaily2.getXAxis().setTextSize(textSize);
                            barChartdaily2.getAxisLeft().setTextSize(textSize);
                            barChartdaily2.setExtraBottomOffset(10f);

                            barChartdaily2.getAxisRight().setEnabled(false);
                            Description desc1 = new Description();
                            desc1.setText("");
                            barChartdaily2.setDescription(desc1);
                            barChartdaily2.getLegend().setEnabled(false);
                            barChartdaily2.getXAxis().setDrawGridLines(false);
                            barChartdaily2.getAxisLeft().setDrawGridLines(false);
                            barChartdaily2.setNoDataText("Data Loading Please Wait...");

                            barChartdaily2.invalidate();

//
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportDaily.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay);

        try {
            fileName = new File(getCacheDir() + "/memRepDailyX.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageXAxis.child("memRepDailyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/memRepDailyY.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageYAxis.child("memRepDailyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportWeekly.class);
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
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportMonthly.class);
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
                Intent intent = new Intent(getApplicationContext(), MemoryReportYearly.class);
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
                Intent intent = new Intent(getApplicationContext(), RelaxationReportDaily.class);
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
                Intent intent = new Intent(getApplicationContext(), MemoryWhereAmI.class);
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
        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
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

    public void monthly(View v) {
        Intent intent2 = new Intent(this, ConcentrationReportMonthly.class);
        startActivity(intent2);

    }

    public void yearly(View view) {
        Intent intent2 = new Intent(this, ConcentrationReportYearly.class);
        startActivity(intent2);
    }

    public void weekly(View view) {
        Intent intent2 = new Intent(this, ConcentrationReportWeekly.class);
        startActivity(intent2);
    }

}