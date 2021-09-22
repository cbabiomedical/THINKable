package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relaxation_Monthly extends AppCompatActivity {

    BarChart barChart,barChart1,barChart2;
    private Context context;
    AppCompatButton daily, yearly, weekly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_monthly);

        barChart = (BarChart) findViewById(R.id.barChartMonthly);

        daily = findViewById(R.id.daily);
        yearly =  findViewById(R.id.yearly);
        weekly =  findViewById(R.id.weekly);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Daily.class);
                startActivity(intent);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Weekly.class);
                startActivity(intent);
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Relaxation_Yearly.class);
                startActivity(intent);
            }
        });

        String[] months = new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        List<Float> credits = new ArrayList<>(Arrays.asList(90f,80f,70f,60f,50f,40f,30f,20f,10f,15f,85f,30f));
        float[] strength = new float[]{90f,80f,70f,60f,50f,40f,30f,20f,10f,15f,85f,30f};

        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < strength.length; ++i) {
            entries.add(new BarEntry(i, strength[i]));
        }

        float textSize = 16f;

        MyBarDataset dataSet = new MyBarDataset(entries, "data",credits);
        dataSet.setColors(ContextCompat.getColor(this,R.color.Bwhite) ,
                ContextCompat.getColor(this,R.color.Lblue),
                ContextCompat.getColor(this,R.color.blue),
                ContextCompat.getColor(this,R.color.Ldark),
                ContextCompat.getColor(this,R.color.dark));
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
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

        //Initialize and Assign Variable
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
    }

    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        @Override
        public int getColor(int index){
            float c = credits.get(index);

            if (c > 80){
                return mColors.get(0);
            }
            else if (c > 60) {
                return mColors.get(1);
            }
            else if (c > 40) {
                return mColors.get(2);
            }
            else if (c > 20) {
                return mColors.get(3);
            }
            else{
                return mColors.get(4);
            }

        }
    }
}