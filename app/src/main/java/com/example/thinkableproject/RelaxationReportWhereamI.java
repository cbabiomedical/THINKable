package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
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

public class RelaxationReportWhereamI extends AppCompatActivity {

    private ScatterChart chart4a, chart5, chart6, chart7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_wheream_i);


        chart4a = findViewById(R.id.charta);
        Description descChartDescription = new Description();
        descChartDescription.setEnabled(true);
        chart4a.setDescription(descChartDescription);
        chart4a.setDrawGridBackground(false);
        chart4a.setTouchEnabled(true);
        chart4a.setMaxHighlightDistance(50f);
        chart4a.setDragEnabled(true);
        chart4a.setScaleEnabled(true);
        chart4a.setMaxVisibleValueCount(200);
        chart4a.setPinchZoom(true);
        Legend la = chart4a.getLegend();

        chart5 = findViewById(R.id.chartb);
        Description descChartDescription1 = new Description();
        descChartDescription1.setEnabled(true);
        chart5.setDescription(descChartDescription1);
        chart5.setDrawGridBackground(false);
        chart5.setTouchEnabled(true);
        chart5.setMaxHighlightDistance(50f);
        chart5.setDragEnabled(true);
        chart5.setScaleEnabled(true);
        chart5.setMaxVisibleValueCount(200);
        chart5.setPinchZoom(true);
        Legend l1a = chart5.getLegend();

        chart7 = findViewById(R.id.chartc);
        Description descChartDescription2 = new Description();
        descChartDescription2.setEnabled(true);
        chart7.setDescription(descChartDescription);
        chart7.setDrawGridBackground(false);
        chart7.setTouchEnabled(true);
        chart7.setMaxHighlightDistance(50f);
        chart7.setDragEnabled(true);
        chart7.setScaleEnabled(true);
        chart7.setMaxVisibleValueCount(200);
        chart7.setPinchZoom(true);
        Legend l3a = chart7.getLegend();

        chart6 = findViewById(R.id.chartd);
        Description descChartDescription3 = new Description();
        descChartDescription3.setEnabled(true);
        chart6.setDescription(descChartDescription);
        chart6.setDrawGridBackground(false);
        chart6.setTouchEnabled(true);
        chart6.setMaxHighlightDistance(50f);
        chart6.setDragEnabled(true);
        chart6.setScaleEnabled(true);
        chart6.setMaxVisibleValueCount(200);
        chart6.setPinchZoom(true);
        Legend l2a = chart6.getLegend();

        la.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        la.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        la.setOrientation(Legend.LegendOrientation.VERTICAL);
        la.setDrawInside(false);
        la.setXOffset(5f);

        YAxis yl = chart4a.getAxisLeft();
        yl.setAxisMinimum(0f);
        chart4a.getAxisRight().setEnabled(false);
        XAxis xl = chart4a.getXAxis();
        xl.setDrawGridLines(false);
        String[] daysS = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis = chart4a.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(daysS));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);

        YAxis yl1 = chart5.getAxisLeft();
        yl1.setAxisMinimum(0f);
        chart5.getAxisRight().setEnabled(false);
        XAxis xl1 = chart5.getXAxis();
        xl1.setDrawGridLines(false);
        String[] daysS1 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis1 = chart5.getXAxis();
        xAxis1.setValueFormatter(new IndexAxisValueFormatter(daysS1));
        xAxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis1.setGranularity(1);
        xAxis1.setCenterAxisLabels(true);

        YAxis yl3 = chart7.getAxisLeft();
        yl3.setAxisMinimum(0f);
        chart7.getAxisRight().setEnabled(false);
        XAxis xl3 = chart7.getXAxis();
        xl3.setDrawGridLines(false);
        String[] daysS3 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis3 = chart7.getXAxis();
        xAxis3.setValueFormatter(new IndexAxisValueFormatter(daysS3));
        xAxis3.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis3.setGranularity(1);
        xAxis3.setCenterAxisLabels(true);

        YAxis yl2 = chart6.getAxisLeft();
        yl2.setAxisMinimum(0f);
        chart6.getAxisRight().setEnabled(false);
        XAxis xl2 = chart6.getXAxis();
        xl2.setDrawGridLines(false);
        String[] daysS2 = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        XAxis xAxis2 = chart6.getXAxis();
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
        chart4a.setData(data);
        chart4a.invalidate();

        chart5.setData(data);
        chart5.invalidate();

        chart7.setData(data);
        chart7.invalidate();

        chart6.setData(data);
        chart6.invalidate();
    }

    public void concq(View view) {
        Intent intentconcq = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
        startActivity(intentconcq);
    }
}