package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relaxation_Yearly extends AppCompatActivity {
    BarChart barChart2;
    private Context context;
    AppCompatButton daily, weekly, monthly;
    ImageButton concentration;
    FirebaseUser mUser;
    String text;
    File localFile;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_yearly);

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        barChart2 = findViewById(R.id.barChartYearly);
        List<BarEntry> entries = new ArrayList<>();
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");

        concentration = findViewById(R.id.concentration);
        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Daily.class);
                startActivity(intent);
            }
        });


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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/yearly.txt");

        try {
            localFile = File.createTempFile("tempFile", ".txt");
            text = localFile.getAbsolutePath();
            Log.d("Bitmap", text);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Relaxation_Yearly.this, "Success", Toast.LENGTH_SHORT).show();

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
                    String[] weeks = new String[]{"2018", "2019", "2020", "2021"};
                    List<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 10f));
                    float[] strengthWeek = new float[]{90f, 30f, 70f, 10f};

                    for (int j = 0; j < floatList.size(); ++j) {
                        entries.add(new BarEntry(j, floatList.get(j)));
                    }


                    float textSize = 16f;
                    Relaxation_Yearly.MyBarDataset dataSet = new Relaxation_Yearly.MyBarDataset(entries, "data", creditsWeek);
                    dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                            ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                            ContextCompat.getColor(getApplicationContext(), R.color.blue),
                            ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                            ContextCompat.getColor(getApplicationContext(), R.color.dark));
                    BarData data = new BarData(dataSet);
                    data.setDrawValues(false);
                    data.setBarWidth(0.8f);

                    barChart2.setData(data);
                    barChart2.setFitBars(true);
                    barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weeks));
                    barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart2.getXAxis().setTextSize(textSize);
                    barChart2.getAxisLeft().setTextSize(textSize);
                    barChart2.setExtraBottomOffset(10f);

                    barChart2.getAxisRight().setEnabled(false);
                    Description desc = new Description();
                    desc.setText("");
                    barChart2.setDescription(desc);
                    barChart2.getLegend().setEnabled(false);
                    barChart2.getXAxis().setDrawGridLines(false);
                    barChart2.getAxisLeft().setDrawGridLines(false);

                    barChart2.invalidate();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Relaxation_Yearly.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        //Initialize and Assign Variable

    }

    public void caliyearly1(View view) {
        Intent intentry = new Intent(Relaxation_Yearly.this, Calibration.class);

        startActivity(intentry);
    }

    public void gotoPopup8(View view) {
        Intent intentgp8 = new Intent(Relaxation_Yearly.this, Relaxation_popup.class);

        startActivity(intentgp8);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChangingConfigurations()) {
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