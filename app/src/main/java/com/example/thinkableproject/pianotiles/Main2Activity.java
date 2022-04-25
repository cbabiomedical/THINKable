package com.example.thinkableproject.pianotiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    public static final String TRUE = "true";
    public static final String TAG = "Main2ctivity";
    Intent i = null;
    Button b1, b2, b3;
    FirebaseFirestore database;
    User user;
    FirebaseUser mUser;
    int updatedCoins;
    Dialog dialogIntervention;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5);
        dialogIntervention=new Dialog(this);
        Log.d(TAG, "onCreate: has been called");
        i = new Intent(this, MainActivity.class);
        final Intent in = getIntent();
        final String score = in.getStringExtra("SCORE");
        Log.d("Piano Score,",score);

        Double scoreFinal=Double.parseDouble(score);

        Log.d("Score Final",String.valueOf(scoreFinal));
//

//            }
//        });
        final String tiles = in.getStringExtra(MainActivity.MAXTILES);
        TextView tvs = (TextView) findViewById(R.id.tvs);
        b1 = (Button) findViewById(R.id.btnnwgme);
        b2 = (Button) findViewById(R.id.btnqt);
        b3 = (Button) findViewById(R.id.btnshare);
        if (score.equals("FAILED")) {
            ((TextView) findViewById(R.id.tvs)).setText(score);


            ((LinearLayout) findViewById(R.id.linear2)).setBackgroundResource(R.color.Red);
            b1.setBackgroundResource(R.color.Red);
            b2.setBackgroundResource(R.color.Red);
            b3.setBackgroundResource(R.color.Red);
        } else {
            ((TextView) findViewById(R.id.tvscore)).setText(score + "s");
        }
        final Intent i2 = new Intent(this, Main4Activity.class);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score.equals("FAILED")) {
                    Toast.makeText(Main2Activity.this, "You have failed", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey I just completed " + String.valueOf(tiles) + " tiles in " + score + "seconds!!\n");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "My Score!!");
                    startActivity(Intent.createChooser(intent, "Share..."));
                }
            }
        });
        openLineChart();
    }

    private void openLineChart() {
        Button ok;
        LineChart lineChart;
//        TextView points;
//        TextView totalPoints;

        dialogIntervention.setContentView(R.layout.game_intervention_popup);
        dialogIntervention.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lineChart = (LineChart) dialogIntervention.findViewById(R.id.lineChartInterventionGame);

        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "PianoTiles Progress");
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
        ok = (Button) dialogIntervention.findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIntervention.dismiss();
            }
        });

        dialogIntervention.show();

    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));
    }

    @Override
    public void onBackPressed() {
        Intent i2 = new Intent(this, Main3Activity.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i2.putExtra("EXIT", true);
        startActivity(i2);
//            super.onBackPressed();
    }

}
