package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Concentration_Monthly extends AppCompatActivity {
    Dialog dialogcm;

    BarChart barChart, barChart1;
    private Context context;
    AppCompatButton daily, weekly, yearly,realTime;
    ImageButton relaxationBtn;
    FirebaseUser mUser;
    File localFile;
    String text;
    ArrayList<String> list = new ArrayList();
    ArrayList<Float> floatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__monthly);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        yearly = findViewById(R.id.yearly);
        barChart = (BarChart) findViewById(R.id.barChartMonthly);
        realTime=findViewById(R.id.realTime);
        relaxationBtn=findViewById(R.id.relaxation);
        List<BarEntry> entries = new ArrayList<>();
        dialogcm = new Dialog(this);

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
                        startActivity(new Intent(getApplicationContext(), Reports.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), UserProfile1.class));
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/monthly.txt");

        try {
            localFile = File.createTempFile("tempFile", ".txt");
            text = localFile.getAbsolutePath();
            Log.d("Bitmap", text);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Concentration_Monthly.this, "Success", Toast.LENGTH_SHORT).show();

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
                    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    List<Float> credits = new ArrayList<>(Arrays.asList(90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f));
                    float[] strength = new float[]{90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f};


                    for (int j = 0; j < floatList.size(); ++j) {
                        entries.add(new BarEntry(j, floatList.get(j)));
                    }


                    float textSize = 16f;
                    MyBarDataset dataSet = new MyBarDataset(entries, "data", credits);
                    dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                            ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                            ContextCompat.getColor(getApplicationContext(), R.color.blue),
                            ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                            ContextCompat.getColor(getApplicationContext(), R.color.dark));
                    BarData data = new BarData(dataSet);
                    data.setDrawValues(false);
                    data.setBarWidth(0.8f);

                    barChart.setData(data);
                    barChart.setFitBars(true);
                    barChart.getXAxis
                            ().setValueFormatter(new IndexAxisValueFormatter(months));
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setTextSize(textSize);
                    barChart.getAxisLeft().setTextSize(textSize);
                    barChart.setExtraBottomOffset(10f);

                    barChart.getAxisRight().setEnabled(false);
                    Description desc = new Description();
                    desc.setText("");
                    barChart.setDescription(desc);
                    barChart.getLegend().setEnabled(false);
                    barChart.getXAxis().setDrawGridLines(false);
                    barChart.getAxisLeft().setDrawGridLines(false);

                    barChart.invalidate();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Concentration_Monthly.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (IOException exception) {
            exception.printStackTrace();
        }


        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Daily.class);
                startActivity(intent);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Weekly.class);
                startActivity(intent);
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Monthly.class);
                startActivity(intent);
            }
        });
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Monthly.class);
                startActivity(intent);
            }
        });
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });
    }

    public void gotoPopup2(View view) {
        ImageView cancelcon;
        dialogcm.setContentView(R.layout.activity_concentration_popup);
        cancelcon = (ImageView) dialogcm.findViewById(R.id.cancelcon);
        cancelcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogcm.dismiss();
            }
        });
        dialogcm.show();

    }

    public void calimonthly(View view) {
        Intent intentcm = new Intent(Concentration_Monthly.this, Calibration.class);

        startActivity(intentcm);

    }

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