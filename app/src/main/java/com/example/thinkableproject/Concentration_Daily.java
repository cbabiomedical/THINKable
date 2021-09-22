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

public class Concentration_Daily extends AppCompatActivity {
    BarChart barChartdaily;
    private Context context;
    AppCompatButton monthly, yearly, weekly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__daily);
        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);

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
        monthly = findViewById(R.id.monthly);
        yearly =  findViewById(R.id.yearly);
        weekly =  findViewById(R.id.weekly);
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
                Intent intent=new Intent(getApplicationContext(),Concentration_Yearly.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");

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



    public void gotoPopup1(View view) {
        Intent intentgp1 = new Intent(Concentration_Daily.this,Concentration_popup.class);

        startActivity(intentgp1);

    }

    public void calidaily(View view) {
        Intent intentcd = new Intent(Concentration_Daily.this,Calibration.class);

        startActivity(intentcd);
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


//    public void compare(View view) {
//        Intent intent2 = new Intent(this, Compare.class);
//        startActivity(intent2);
//    }
}