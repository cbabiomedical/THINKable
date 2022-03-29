package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

public class ConcentrationReportDaily extends AppCompatActivity {
    BarChart barChartdaily, barChartdaily2;
    private Context context;
    AppCompatButton monthly, yearly, weekly, whereAmI;
    File fileName, fileName1, localFile, localFile1;
    FirebaseUser mUser;
    ImageView relaxationBtn, memory;
    GifImageView c1gif, c2gif;
    int color;
    Double sum = 0.0;
    Double total = 0.0;
    Double sum4 = 0.0;
    Double total4 = 0.0;
    Double sum1 = 0.0;
    Double total1 = 0.0;
    Double sum2 = 0.0;
    Double total2 = 0.0;
    Double sum3 = 0.0;
    Double total3 = 0.0;
    Double sum5 = 0.0;
    Double total5 = 0.0;
    Double sum6 = 0.0;
    Double total6 = 0.0;


    View c1, c2;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    String text;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();

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
        setContentView(R.layout.activity_concentration_report_daily);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        barChartdaily2 = (BarChart) findViewById(R.id.barChartDaily2);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        relaxationBtn = findViewById(R.id.relaxation);
        whereAmI = findViewById(R.id.whereAmI);
        memory = findViewById(R.id.memory);
        lineChart = findViewById(R.id.lineChartDaily);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);

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
//        //Initializing arraylist and storing input data to arraylist
//        ArrayList<Float> obj = new ArrayList<>(
//                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));
//        //Writing data to file
//        try {
//            fileName = new File(getCacheDir() + "/reportDaily.txt");
//            String line = "";
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

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
//        try {
//            StorageReference mountainsRef = storageReference1.child("reportDaily.txt");
//            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
//            UploadTask uploadTask = mountainsRef.putStream(stream);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(ConcentrationReportDaily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(ConcentrationReportDaily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        final Handler handler = new Handler();
        final int delay = 5000;

        // ================Displaying Time to Concentrate Daily Chart====================

        handler.postDelayed(new Runnable() {
            Long average1, average2, average3, average4, average5, average6, average7;
            List<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));

            @Override
            public void run() {
                lineEntries = new ArrayList();

                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                            sum += av1;

                        }
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("Average Mon Con", String.valueOf(average1));
                        } else {
                            average1 = Long.valueOf(0);
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                    sum += av1;

                                }
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    Log.d("Average Tue Con", String.valueOf(average2));

                                } else {
                                    average2 = Long.valueOf(0);
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                            sum += av1;

                                        }
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Average Wed Con", String.valueOf(average3));

                                        } else {
                                            average3 = Long.valueOf(0);
                                        }

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                    sum += av1;

                                                }
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    Log.d("Average Thur Con", String.valueOf(average4));
                                                } else {
                                                    average4 = Long.valueOf(0);
                                                }
                                                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                            sum += av1;

                                                        }
                                                        Log.d("SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("Average Fri Con", String.valueOf(average5));
                                                        } else {
                                                            average5 = Long.valueOf(0);
                                                        }
                                                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                    sum += av1;

                                                                }
                                                                Log.d("SUM", String.valueOf(sum));
                                                                if (sum != 0) {
                                                                    average6 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    Log.d("Average Sat Con", String.valueOf(average6));

                                                                } else {
                                                                    average6 = Long.valueOf(0);
                                                                }

                                                                DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                            sum += av1;

                                                                        }
                                                                        Log.d("SUM", String.valueOf(sum));
                                                                        if (sum != 0) {
                                                                            average7 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("Average Sun Con", String.valueOf(average7));
                                                                        } else {
                                                                            average7 = Long.valueOf(0);
                                                                        }
                                                                        Log.d("AverageOutside1Tim", String.valueOf(average7));
                                                                        Log.d("AverageOutside2 Tim", String.valueOf(average1));
                                                                        Log.d("AverageOutside3 Tim", String.valueOf(average2));
                                                                        Log.d("AverageOutside4 Tim", String.valueOf(average3));
                                                                        Log.d("AverageOutside5 Tim", String.valueOf(average4));
                                                                        Log.d("AverageOutside6 Tim", String.valueOf(average5));
                                                                        Log.d("AverageOutside7 Tim", String.valueOf(average6));
                                                                        final String[] weekdays = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                                                                        List<BarEntry> entries = new ArrayList<>();
                                                                        entries.add(new BarEntry(1, Float.parseFloat(String.valueOf(average7))));
                                                                        entries.add(new BarEntry(2, Float.parseFloat(String.valueOf(average1))));
                                                                        entries.add(new BarEntry(3, Float.parseFloat(String.valueOf(average2))));
                                                                        entries.add(new BarEntry(4, Float.parseFloat(String.valueOf(average3))));
                                                                        entries.add(new BarEntry(5, Float.parseFloat(String.valueOf(average4))));
                                                                        entries.add(new BarEntry(5, Float.parseFloat(String.valueOf(average5))));
                                                                        entries.add(new BarEntry(6, Float.parseFloat(String.valueOf(average6))));

                                                                        Log.d("ENTIRES", String.valueOf(entries));

                                                                        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                                        BarData data = new BarData(dataSet);
                                                                        data.setDrawValues(false);
                                                                        data.setBarWidth(0.7f);

                                                                        float textSize = 10f;
                                                                        barChartdaily.setData(data);
                                                                        barChartdaily.setFitBars(true);
                                                                        barChartdaily.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weekdays));
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
                                                                        barChartdaily.setNoDataText("Data Loading Please Wait...");
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
        }, 5000);

//Initializing arraylist and storing input data to arraylist
//        ArrayList<Float> obj1 = new ArrayList<>(
//                Arrays.asList(60f, 40f, 70f, 20f, 20f, 50f, 80f));  //Array list to write data to file
//        //Write input data to file
//        try {
//            fileName1 = new File(getCacheDir() + "/reportDaily2.txt");  //Writing data to file
//            String line = "";
//            FileWriter fw;
//            fw = new FileWriter(fileName1);
//            BufferedWriter output = new BufferedWriter(fw);
//            int size = obj1.size();
//            for (int i = 0; i < size; i++) {
//                output.write(obj1.get(i).toString() + "\n");
////                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
//            }
//            output.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
//        storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
//        //downloading the uploaded file and storing in arraylist
//        try {
//            StorageReference mountainsRef = storageReference1.child("reportDaily2.txt");
//            InputStream stream = new FileInputStream(new File(fileName1.getAbsolutePath()));
//            UploadTask uploadTask = mountainsRef.putStream(stream);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(ConcentrationReportDaily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(ConcentrationReportDaily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //===========Method for Time Stayed Concentrated Chart======================
        final Handler handler1 = new Handler();
        final int delay1 = 5000;

        handler1.postDelayed(new Runnable() {
            Long average1, average2, average3, average4, average5, average6, average7;

            @Override
            public void run() {
                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                            sum += av1;

                        }
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("Average Mon Con", String.valueOf(average1));
                        } else {
                            average1 = Long.valueOf(0);
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                    sum += av1;

                                }
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    Log.d("Average Tue Con", String.valueOf(average2));

                                } else {
                                    average2 = Long.valueOf(0);
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                            sum += av1;

                                        }
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Average Wed Con", String.valueOf(average3));

                                        } else {
                                            average3 = Long.valueOf(0);
                                        }

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                    sum += av1;

                                                }
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    Log.d("Average Thur Con", String.valueOf(average4));
                                                } else {
                                                    average4 = Long.valueOf(0);
                                                }
                                                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                            sum += av1;

                                                        }
                                                        Log.d("SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("Average Fri Con", String.valueOf(average5));
                                                        } else {
                                                            average5 = Long.valueOf(0);
                                                        }
                                                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                    sum += av1;

                                                                }
                                                                Log.d("SUM", String.valueOf(sum));
                                                                if (sum != 0) {
                                                                    average6 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    Log.d("Average Sat Con", String.valueOf(average6));

                                                                } else {
                                                                    average6 = Long.valueOf(0);
                                                                }

                                                                DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                                            sum += av1;

                                                                        }
                                                                        Log.d("SUM", String.valueOf(sum));
                                                                        if (sum != 0) {
                                                                            average7 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("Average Sun Con", String.valueOf(average7));
                                                                        } else {
                                                                            average7 = Long.valueOf(0);
                                                                        }
                                                                        Log.d("Average1Tim", String.valueOf(average7));
                                                                        Log.d("Average2 Tim", String.valueOf(average1));
                                                                        Log.d("Average3 Tim", String.valueOf(average2));
                                                                        Log.d("Average4  Tim", String.valueOf(average3));
                                                                        Log.d("Average5  Tim", String.valueOf(average4));
                                                                        Log.d("Average6  Tim", String.valueOf(average5));
                                                                        Log.d("Average7  Tim", String.valueOf(average6));
                                                                        final String[] weekdays = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                                                                        List<BarEntry> entries2 = new ArrayList<>();
                                                                        entries2.add(new BarEntry(1, Float.parseFloat(String.valueOf(average7))));
                                                                        entries2.add(new BarEntry(2, Float.parseFloat(String.valueOf(average1))));
                                                                        entries2.add(new BarEntry(3, Float.parseFloat(String.valueOf(average2))));
                                                                        entries2.add(new BarEntry(4, Float.parseFloat(String.valueOf(average3))));
                                                                        entries2.add(new BarEntry(5, Float.parseFloat(String.valueOf(average4))));
                                                                        entries2.add(new BarEntry(6, Float.parseFloat(String.valueOf(average5))));
                                                                        entries2.add(new BarEntry(7, Float.parseFloat(String.valueOf(average6))));
                                                                        List<Float> creditsMain1 = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));

                                                                        float textSize = 10f;
                                                                     MyBarDataset dataSet = new MyBarDataset(entries2, "data", creditsMain1);
                                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                                        BarData data = new BarData(dataSet);
                                                                        data.setDrawValues(false);
                                                                        data.setBarWidth(0.9f);

                                                                        barChartdaily2.setData(data);
                                                                        barChartdaily2.setFitBars(true);
                                                                        barChartdaily2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weekdays));
                                                                        barChartdaily2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                                        barChartdaily2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                                        barChartdaily2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                        barChartdaily2.getXAxis().setTextSize(textSize);
                                                                        barChartdaily2.getAxisLeft().setTextSize(textSize);
                                                                        barChartdaily2.setExtraBottomOffset(10f);

                                                                        barChartdaily2.getAxisRight().setEnabled(false);
                                                                        Description desc = new Description();
                                                                        desc.setText("");
                                                                        barChartdaily2.setDescription(desc);
                                                                        barChartdaily2.getLegend().setEnabled(false);
                                                                        barChartdaily2.getXAxis().setDrawGridLines(false);
                                                                        barChartdaily2.getAxisLeft().setDrawGridLines(false);
                                                                        barChartdaily2.setNoDataText("Data Loading Please Wait....");

                                                                        barChartdaily2.invalidate();
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
        }, 5000);


//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();


        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportYearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        // On click listener of where am i toggle button
        whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
            Double average1, average2, average3, average4, average5, average6, average7;

            @Override
            public void run() {

                lineEntries = new ArrayList();

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesSun", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesDataSun", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataSun", String.valueOf(dataSnapshot2.getKey()));
                                    if (dataSnapshot2.getKey().equals("index")) {
                                        dataSnapshot2.getValue();
                                        Log.d("DataSun", String.valueOf(dataSnapshot2.getValue()));
                                        Double av1 = (Double) dataSnapshot2.getValue();
                                        sumElement.add(av1);
                                        sum1 += av1;

                                    }
                                }

                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalSun", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sum1 = 0.0;
                            average1 = 0.0;
                        } else {
                            Log.d("SUMSun", String.valueOf(total1));
                            average1 = total1 / sumElement.size();
                            Log.d("AverageData1", String.valueOf(average1));
                        }
                        Log.d("AverageData1", String.valueOf(average1));

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesSun", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesDataSun", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataSun", String.valueOf(dataSnapshot2.getKey()));
                                            if (dataSnapshot2.getKey().equals("index")) {
                                                dataSnapshot2.getValue();
                                                Log.d("DataSun", String.valueOf(dataSnapshot2.getValue()));
                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                sumElement.add(av1);
                                                sum2 += av1;

                                            }
                                        }

                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalMon", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sum2 = 0.0;
                                    average2 = 0.0;
                                } else {
                                    Log.d("SUMMon", String.valueOf(total2));
                                    average2 = total2 / sumElement.size();
                                    Log.d("AverageData2", String.valueOf(average2));
                                }
                                Log.d("AverageData2", String.valueOf(average2));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesTue", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesDataTue", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataTue", String.valueOf(dataSnapshot2.getKey()));
                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                        dataSnapshot2.getValue();
                                                        Log.d("DataTue", String.valueOf(dataSnapshot2.getValue()));
                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                        sumElement.add(av1);
                                                        sum3 += av1;

                                                    }
                                                }

                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalTue", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sum3 = 0.0;
                                            average3 = 0.0;
                                        } else {
                                            Log.d("SUMTue", String.valueOf(total3));
                                            average3 = total3 / sumElement.size();
                                            Log.d("AverageData3", String.valueOf(average3));
                                        }
                                        Log.d("AverageData3", String.valueOf(average3));


                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                                        reference2.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("Values", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesData", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("Data2", String.valueOf(dataSnapshot2.getKey()));
                                                            if (dataSnapshot2.getKey().equals("index")) {
                                                                dataSnapshot2.getValue();
                                                                Log.d("DataF", String.valueOf(dataSnapshot2.getValue()));
                                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                                sumElement.add(av1);
                                                                sum += av1;

                                                            }
                                                        }

                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalDA", String.valueOf(total));

                                                if (total == 0.0) {
                                                    sum = 0.0;
                                                    average4 = 0.0;

                                                } else {
                                                    Log.d("SUMDA", String.valueOf(total));
                                                    average4 = total / sumElement.size();
                                                    Log.d("AverageData", String.valueOf(average4));
                                                }
                                                Log.d("AverageData", String.valueOf(average4));
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                                reference1.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("Values1", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("ValuesData1", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                    Log.d("Data21", String.valueOf(dataSnapshot2.getKey()));
                                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                                        dataSnapshot2.getValue();
                                                                        Log.d("DataF1", String.valueOf(dataSnapshot2.getValue()));
                                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                                        sumElement.add(av1);
                                                                        sum4 += av1;

                                                                    }
                                                                }

                                                            }
                                                        }
                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                            total4 += (Double) sumElement.get(i);
                                                        }
                                                        Log.d("TotalDA4", String.valueOf(total4));

                                                        if (total4 == 0.0) {
                                                            sum4 = 0.0;
                                                            average5 = 0.0;
                                                        } else {
                                                            Log.d("SUMDA4", String.valueOf(total4));
                                                            average5 = total4 / sumElement.size();
                                                            Log.d("AverageData4", String.valueOf(average4));
                                                        }
                                                        Log.d("AverageData4", String.valueOf(average4));
                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                                        reference1.addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();

                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("Values5", String.valueOf(dataSnapshot.getValue()));
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("ValuesDataFri", String.valueOf(dataSnapshot1.getValue()));
                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                            Log.d("DataFri", String.valueOf(dataSnapshot2.getKey()));
                                                                            if (dataSnapshot2.getKey().equals("index")) {
                                                                                dataSnapshot2.getValue();
                                                                                Log.d("DataFri", String.valueOf(dataSnapshot2.getValue()));
                                                                                Double av1 = (Double) dataSnapshot2.getValue();
                                                                                sumElement.add(av1);
                                                                                sum5 += av1;

                                                                            }
                                                                        }

                                                                    }
                                                                }
                                                                for (int i = 0; i < sumElement.size(); i++) {
                                                                    total5 += (Double) sumElement.get(i);
                                                                }
                                                                Log.d("TotalDA4", String.valueOf(total5));

                                                                if (total5 == 0.0) {
                                                                    sum5 = 0.0;
                                                                    average6 = 0.0;
                                                                } else {
                                                                    Log.d("SUMDA5", String.valueOf(total5));
                                                                    average6 = total5 / sumElement.size();
                                                                    Log.d("AverageData5", String.valueOf(average5));
                                                                }
                                                                Log.d("AverageData5", String.valueOf(average5));

                                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                                reference1.addValueEventListener(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("Values6", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("ValuesDataSat", String.valueOf(dataSnapshot1.getValue()));
                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                                    Log.d("DataSat", String.valueOf(dataSnapshot2.getKey()));
                                                                                    if (dataSnapshot2.getKey().equals("index")) {
                                                                                        dataSnapshot2.getValue();
                                                                                        Log.d("DataSat", String.valueOf(dataSnapshot2.getValue()));
                                                                                        Double av1 = (Double) dataSnapshot2.getValue();
                                                                                        sumElement.add(av1);
                                                                                        sum6 += av1;

                                                                                    }
                                                                                }

                                                                            }
                                                                        }
                                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                                            total6 += (Double) sumElement.get(i);
                                                                        }
                                                                        Log.d("TotalDA4", String.valueOf(total6));

                                                                        if (total6 == 0.0) {
                                                                            sum6 = 0.0;
                                                                            average7 = 0.0;
                                                                        } else {
                                                                            Log.d("SUMDA6", String.valueOf(total6));
                                                                            average7 = total6 / sumElement.size();
                                                                            Log.d("AverageData6", String.valueOf(average6));
                                                                        }
                                                                        Log.d("AverageData6", String.valueOf(average6));
                                                                        Log.d("Average Outside1 Con", String.valueOf(average7));
                                                                        Log.d("Average Outside2 Con", String.valueOf(average1));
                                                                        Log.d("Average Outside3 Con", String.valueOf(average2));
                                                                        Log.d("Average Outside4 Con", String.valueOf(average3));
                                                                        Log.d("Average Outside5 Con", String.valueOf(average4));
                                                                        Log.d("Average Outside6 Con", String.valueOf(average5));
                                                                        Log.d("Average Outside7 Con", String.valueOf(average6));
                                                                        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
//
//
                                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(average1))));
                                                                        Log.d("FloatAverage", String.valueOf(Float.parseFloat(String.valueOf(average1))));
                                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(average2))));
                                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(average3))));
                                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(average4))));
                                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(average5))));
                                                                        lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(average6))));
                                                                        lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(average7))));
                                                                        List<String> xAxisValues = new ArrayList<>(Arrays.asList("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", ""));

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
        }, 5000);

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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void yearly(View view) {
        Intent intent2 = new Intent(this, ConcentrationReportYearly.class);
        startActivity(intent2);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void weekly(View view) {
        Intent intent2 = new Intent(this, ConcentrationReportWeekly.class);
        startActivity(intent2);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}