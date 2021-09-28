package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConcentrationReportDaily extends AppCompatActivity {
    BarChart barChartdaily, barChartdaily2;
    FirebaseDatabase firebaseDatabase;
    private Context context;
    AppCompatButton monthly, yearly, weekly;


    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_daily);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        barChartdaily2 = (BarChart) findViewById(R.id.barChartDaily2);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
//        retrieveData();


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

        barChartdaily2.setData(data);
        barChartdaily2.setFitBars(true);
        barChartdaily2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
        barChartdaily2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartdaily2.getXAxis().setTextSize(textSize);
        barChartdaily2.getAxisLeft().setTextSize(textSize);
        barChartdaily2.setExtraBottomOffset(10f);

        barChartdaily2.getAxisRight().setEnabled(false);
        Description desc1 = new Description();
        desc1.setText("");
        barChartdaily2.setDescription(desc);
        barChartdaily2.getLegend().setEnabled(false);
        barChartdaily2.getXAxis().setDrawGridLines(false);
        barChartdaily2.getAxisLeft().setDrawGridLines(false);

        barChartdaily2.invalidate();
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
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

//    private void retrieveData() {
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Entry> dataVals = new ArrayList<>();
//                if (snapshot.hasChildren()) {
//                    for (DataSnapshot myDataSnapshot : snapshot.getChildren()) {
//                        PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
//                        dataVals.add(new Entry(pointValue.getxValue(), pointValue.getyValue()));
//                    }
//
//                } else {
//                    barChartdaily.clear();
//                    barChartdaily.invalidate();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


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