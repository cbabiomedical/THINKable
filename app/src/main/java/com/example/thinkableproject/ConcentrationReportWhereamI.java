package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ConcentrationReportWhereamI extends AppCompatActivity {

    private ScatterChart chart,chart1,chart2,chart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_wheream_i);


        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setMaxHighlightDistance(50f);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setMaxVisibleValueCount(200);
        chart.setPinchZoom(true);
        Legend l = chart.getLegend();

        chart1 = findViewById(R.id.chart2);
        chart1.getDescription().setEnabled(false);
        chart1.setDrawGridBackground(false);
        chart1.setTouchEnabled(true);
        chart1.setMaxHighlightDistance(50f);
        chart1.setDragEnabled(true);
        chart1.setScaleEnabled(true);
        chart1.setMaxVisibleValueCount(200);
        chart1.setPinchZoom(true);
        Legend l1 = chart1.getLegend();

        chart3 = findViewById(R.id.chart3);
        chart3.getDescription().setEnabled(false);
        chart3.setDrawGridBackground(false);
        chart3.setTouchEnabled(true);
        chart3.setMaxHighlightDistance(50f);
        chart3.setDragEnabled(true);
        chart3.setScaleEnabled(true);
        chart3.setMaxVisibleValueCount(200);
        chart3.setPinchZoom(true);
        Legend l3 = chart3.getLegend();

        chart2 = findViewById(R.id.chart4);
        chart2.getDescription().setEnabled(false);
        chart2.setDrawGridBackground(false);
        chart2.setTouchEnabled(true);
        chart2.setMaxHighlightDistance(50f);
        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);
        chart2.setMaxVisibleValueCount(200);
        chart2.setPinchZoom(true);
        Legend l2 = chart2.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXOffset(5f);

        YAxis yl = chart.getAxisLeft();
        yl.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        XAxis xl = chart.getXAxis();
        xl.setDrawGridLines(false);
        String[] daysS = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(daysS));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);

        YAxis yl1 = chart1.getAxisLeft();
        yl1.setAxisMinimum(0f);
        chart1.getAxisRight().setEnabled(false);
        XAxis xl1 = chart1.getXAxis();
        xl1.setDrawGridLines(false);
        String[] daysS1 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis1 = chart1.getXAxis();
        xAxis1.setValueFormatter(new IndexAxisValueFormatter(daysS1));
        xAxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis1.setGranularity(1);
        xAxis1.setCenterAxisLabels(true);

        YAxis yl3 = chart3.getAxisLeft();
        yl3.setAxisMinimum(0f);
        chart3.getAxisRight().setEnabled(false);
        XAxis xl3 = chart3.getXAxis();
        xl3.setDrawGridLines(false);
        String[] daysS3 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis3 = chart3.getXAxis();
        xAxis3.setValueFormatter(new IndexAxisValueFormatter(daysS3));
        xAxis3.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis3.setGranularity(1);
        xAxis3.setCenterAxisLabels(true);

        YAxis yl2 = chart2.getAxisLeft();
        yl2.setAxisMinimum(0f);
        chart2.getAxisRight().setEnabled(false);
        XAxis xl2 = chart2.getXAxis();
        xl2.setDrawGridLines(false);
        String[] daysS2 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(daysS2));
        xAxis2.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis2.setGranularity(1);
        xAxis2.setCenterAxisLabels(true);


        ArrayList<Entry> scatterEntries = new ArrayList<>();
        scatterEntries.add(new BarEntry(1, 7f));
        scatterEntries.add(new BarEntry(2, 1f));
        scatterEntries.add(new BarEntry(3, 1f));
        scatterEntries.add(new BarEntry(4, 3f));
        scatterEntries.add(new BarEntry(5, 4f));
        scatterEntries.add(new BarEntry(6, 3f));
        scatterEntries.add(new BarEntry(7, 9f));

        //person avg
        ArrayList<Entry> scatterEntries1 = new ArrayList<>();
        scatterEntries1.add(new BarEntry(1, 6f));
        scatterEntries1.add(new BarEntry(2, 2f));
        scatterEntries1.add(new BarEntry(3, 5f));
        scatterEntries1.add(new BarEntry(4, 5f));
        scatterEntries1.add(new BarEntry(5, 6f));
        scatterEntries1.add(new BarEntry(6, 4f));
        scatterEntries1.add(new BarEntry(7, 8f));

        //person2


        //All of persons as one





        ScatterDataSet set1 = new ScatterDataSet(scatterEntries, "You");
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);


        ScatterDataSet set2 = new ScatterDataSet(scatterEntries1, "Other Avg");
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
        set2.setScatterShapeHoleRadius(3f);

        set2.setColor(ColorTemplate.COLORFUL_COLORS[1]);


        set1.setScatterShapeSize(8f);
        set2.setScatterShapeSize(8f);
//        set3.setScatterShapeSize(8f);


        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
//        dataSets.add(set3);


        ScatterData data = new ScatterData(dataSets);
        chart.setData(data);
        chart.invalidate();

        chart1.setData(data);
        chart1.invalidate();

        chart3.setData(data);
        chart3.invalidate();

        chart2.setData(data);
        chart2.invalidate();
    }

    public void relaxCQ(View view) {
        Intent intentrelaxCQ = new Intent(getApplicationContext(), RelaxationReportWhereamI.class);
        startActivity(intentrelaxCQ);
    }
}