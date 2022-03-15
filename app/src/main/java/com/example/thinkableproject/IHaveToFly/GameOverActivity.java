package com.example.thinkableproject.IHaveToFly;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameOverActivity extends AppCompatActivity {
    ImageView gameOver;
    Dialog dialog;
    User user;
    TextView score;
    FirebaseFirestore database;
    FirebaseUser mUser;
    LineChart lineChart;
    LineData lineData;
    int scoreInt;
    LineDataSet lineDataSet;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();
    ArrayList lineEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        gameOver = findViewById(R.id.exit);
        dialog = new Dialog(this);
        database = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        score=findViewById(R.id.score);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scoreInt = extras.getInt("score");
            score.setText("Score: "+scoreInt);
        }


        openLineChart();

        gameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
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
        Bundle extras = getIntent().getExtras();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "I Have to Fly Progress");
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


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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