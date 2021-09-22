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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relaxation_Daily extends AppCompatActivity {

    BarChart barChartdaily;
    private Context context;
    AppCompatButton monthly, yearly, weekly;
    AppCompatButton realtime, improverelaxation;
    ImageButton concentration, music, meditation, video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_daily);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);

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

        music = findViewById(R.id.music);
        meditation = findViewById(R.id.meditations);
        video = findViewById(R.id.video);

//        music.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(getApplicationContext(), Exercise_Music.class);
////                startActivity(intent);
//            }
//        });
//
//        meditation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(getApplicationContext(), Exercise_Meditation.class);
////                startActivity(intent);
//            }
//        });
//
//        video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(getApplicationContext(), Exercise_Video.class);
////                startActivity(intent);
//            }
//        });

        realtime = findViewById(R.id.realTime);
        improverelaxation = findViewById(R.id.improveRelaxation);

        monthly = findViewById(R.id.monthly);
        yearly =  findViewById(R.id.yearly);
        weekly =  findViewById(R.id.weekly);
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), Relaxation_Monthly.class);
                startActivity(intentr1);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), Relaxation_Weekly.class);
                startActivity(intentr1);
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 =new Intent(getApplicationContext(), Relaxation_Yearly.class);
                startActivity(intentr1);
            }
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), Reports.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), UserProfile1.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        String[] days = new String[]{"Mon", "Thu", "Wed", "Thur", "Fri", "Sat", "Sun"};
        List<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));
        float[] strengthDay = new float[]{90f, 30f, 70f, 50f, 10f, 15f, 85f};

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < strengthDay.length; ++i) {
            entries.add(new BarEntry(i, strengthDay[i]));
        }

        float textSize = 16f;
        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
        dataSet.setColors(ContextCompat.getColor(this, R.color.Bwhite),
                ContextCompat.getColor(this, R.color.Lblue),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.Ldark),
                ContextCompat.getColor(this, R.color.dark));
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);

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

    public void calidaily1(View view) {
        Intent intentrd = new Intent(Relaxation_Daily.this,Calibration.class);

        startActivity(intentrd);
    }

    public void gotoPopup5(View view) {
        Intent intentgp5 = new Intent(Relaxation_Daily.this,Relaxation_popup.class);

        startActivity(intentgp5);
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

}