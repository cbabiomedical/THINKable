package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Concentration_Daily extends AppCompatActivity {
    BarChart barChartdaily;
    private Context context;
    AppCompatButton monthly, yearly, weekly;
    ActivityMainBinding binding;
    FirebaseUser mUser;
    TextView textView;
    private ArrayList<String> contents;
    String text;
    File localFile;
    private final String filename = "";
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding=ActivityMainBinding.inflate(getLayoutInflater().inflate());
        setContentView(R.layout.activity_concentration__daily);
        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        List<BarEntry> entries = new ArrayList<>();


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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/daily.txt");

        try {
            localFile = File.createTempFile("tempFile", ".txt");
            text = localFile.getAbsolutePath();
            Log.d("Bitmap", text);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Concentration_Daily.this, "Success", Toast.LENGTH_SHORT).show();

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
                    String[] days = new String[]{"Mon", "Thu", "Wed", "Thur", "Fri", "Sat", "Sun"};
                    List<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));
                    float[] strengthDay = new float[]{90f, 30f, 70f, 50f, 10f, 15f, 85f};

                    for (int j = 0; j < floatList.size(); ++j) {
                        entries.add(new BarEntry(j, floatList.get(j)));
                    }


                    float textSize = 16f;
                    MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                    dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                            ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                            ContextCompat.getColor(getApplicationContext(), R.color.blue),
                            ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                            ContextCompat.getColor(getApplicationContext(), R.color.dark));
                    BarData data = new BarData(dataSet);
                    data.setDrawValues(false);
                    data.setBarWidth(0.8f);

                    barChartdaily.setData(data);
                    barChartdaily.setFitBars(true);
                    barChartdaily.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
                    barChartdaily.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
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

                    barChartdaily.invalidate();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Concentration_Daily.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (IOException exception) {
            exception.printStackTrace();
        }


        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Monthly.class);
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
                Intent intent = new Intent(getApplicationContext(), Concentration_Yearly.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");

    }

    public void gotoPopup1(View view) {
        Intent intentgp1 = new Intent(Concentration_Daily.this, Concentration_popup.class);

        startActivity(intentgp1);

    }

    private boolean isExternalStorageAvailableForRW() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


    public void calidaily(View view) {
        Intent intentcd = new Intent(Concentration_Daily.this, Calibration.class);

        startActivity(intentcd);
    }

    private void retreive() {

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
//    public void compare1(View view) {
//        Intent intent2 = new Intent(this, Compare1.class);
//        startActivity(intent2);
//
//    }

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
        Intent intent2 = new Intent(this, Concentration_Monthly.class);
        startActivity(intent2);

    }

    public void yearly(View view) {
        Intent intent2 = new Intent(this, Concentration_Yearly.class);
        startActivity(intent2);
    }

    public void weekly(View view) {
        Intent intent2 = new Intent(this, Concentration_Weekly.class);
        startActivity(intent2);
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
}