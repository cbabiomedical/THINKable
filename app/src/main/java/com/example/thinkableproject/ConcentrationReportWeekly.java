package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConcentrationReportWeekly extends AppCompatActivity {
    BarChart barChart1, barChart2;
    AppCompatButton monthly;
    AppCompatButton yearly;
    AppCompatButton daily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_weekly);
        barChart1 = (BarChart) findViewById(R.id.barChartWeekly);
        barChart2 = findViewById(R.id.barChartWeekly2);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        daily = findViewById(R.id.daily);

        String[] weeks = new String[]{"One", "Two", "Three", "Four"};
        List<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 10f));
        float[] strengthWeek = new float[]{90f, 30f, 70f, 10f};

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < strengthWeek.length; ++i) {
            entries.add(new BarEntry(i, strengthWeek[i]));
        }

        float textSize = 16f;
        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsWeek);
        dataSet.setColors(ContextCompat.getColor(this, R.color.Bwhite),
                ContextCompat.getColor(this, R.color.Lblue),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.Ldark),
                ContextCompat.getColor(this, R.color.dark));
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);

        barChart1.setData(data);
        barChart1.setFitBars(true);
        barChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weeks));
        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart1.getXAxis().setTextSize(textSize);
        barChart1.getAxisLeft().setTextSize(textSize);
        barChart1.setExtraBottomOffset(10f);

        barChart1.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        barChart1.setDescription(desc);
        barChart1.getLegend().setEnabled(false);
        barChart1.getXAxis().setDrawGridLines(false);
        barChart1.getAxisLeft().setDrawGridLines(false);

        barChart1.invalidate();

        barChart2.setData(data);
        barChart2.setFitBars(true);
        barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weeks));
        barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart2.getXAxis().setTextSize(textSize);
        barChart2.getAxisLeft().setTextSize(textSize);
        barChart2.setExtraBottomOffset(10f);

        barChart2.getAxisRight().setEnabled(false);
        Description desc1 = new Description();
        desc1.setText("");
        barChart2.setDescription(desc);
        barChart2.getLegend().setEnabled(false);
        barChart2.getXAxis().setDrawGridLines(false);
        barChart2.getAxisLeft().setDrawGridLines(false);

        barChart2.invalidate();
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportDaily.class);
                startActivity(intent);
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportMonthly.class);
                startActivity(intent);
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportYearly.class);
                startActivity(intent);
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


}