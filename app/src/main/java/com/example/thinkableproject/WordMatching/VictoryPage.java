package com.example.thinkableproject.WordMatching;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkableproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class VictoryPage extends AppCompatActivity {
    Long seconds;
    Dialog dialog;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory_page);
        GlobalElements.getInstance().levelUp();
        dialog = new Dialog(this);
        updateScore();
        updateLevel();
        if (GlobalElements.getInstance().getLevel() == 21) {
            Intent intent = new Intent(this, EndGame.class);
            startActivity(intent);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            seconds = extras.getLong("time");
            Log.d("Time", String.valueOf(seconds));

        }
        openLineChart();
    }

    public void goToKeys(View view) {
        Intent intent = new Intent(this, KeysPage.class);
        startActivity(intent);
    }

    public void updateScore() {
        String update = "";
        update += String.valueOf(GlobalElements.getInstance().getScore());
        update += "(+";
        update += String.valueOf(GlobalElements.getInstance().getScoreDifference());
        update += ")";
        TextView view = (TextView) findViewById(R.id.scoreUpdate);
        view.setText(update);
    }

    public void updateLevel() {
        String level = String.valueOf(GlobalElements.getInstance().getLevel());
        TextView view = (TextView) findViewById(R.id.levelUpdate);
        view.setText(level);
    }

    private void openLineChart() {
        Button ok;
        TextView coins;
        TextView totalPoints;
        LineChart lineChart;
        dialog.setContentView(R.layout.game_intervention_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ok = (Button) dialog.findViewById(R.id.ok);
        coins = (TextView) dialog.findViewById(R.id.points);
        totalPoints = (TextView) dialog.findViewById(R.id.total);
        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
        lineEntries = new ArrayList();
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "Word Match Progress");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(10f);

        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.setBorderColor(Color.TRANSPARENT);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setTextColor(R.color.white);
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
        lineChart.getDescription().setTextColor(R.color.white);
        lineChart.invalidate();
        lineChart.refreshDrawableState();

//
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user = documentSnapshot.toObject(User.class);
////
//                Log.d("Current Coins", String.valueOf(user.getCoins()));
////
//                totalPoints.setText("Total Coins: " + user.getCoins());
////
//
//            }
//        });
//
//
        dialog.show();
    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));
    }
}
