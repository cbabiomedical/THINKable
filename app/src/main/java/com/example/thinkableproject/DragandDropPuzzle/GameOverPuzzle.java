package com.example.thinkableproject.DragandDropPuzzle;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.thinkableproject.GameActivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
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

public class GameOverPuzzle extends AppCompatActivity {
    ImageView exit;
    Dialog dialog;
    TextView score;
    Double scoreVal;
    Long scoreLong;
    FirebaseFirestore database;
    User user;
    FirebaseUser mUser;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();
    ArrayList lineEntries;
    int a;
    int x;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_puzzle);
        score = findViewById(R.id.score);
        Bundle extras = getIntent().getExtras();
        database = FirebaseFirestore.getInstance();
        SharedPreferences prefsCountPuzA = getSharedPreferences("prefsCountPuzA", MODE_PRIVATE);
        int firstStartCountPuzA = prefsCountPuzA.getInt("firstStartCountA", 0);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (extras != null) {
            scoreVal = extras.getDouble("score");
            scoreLong = scoreVal.longValue();
            Log.d("Obtained Score", String.valueOf(scoreLong));

        }
        score.setText("Scores: " + scoreLong);
        database.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                int updateCoins = (int) (user.getCoins() + scoreLong);
                database.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .update("coins", updateCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                                            Toast.makeText(MusicPlayer.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", String.valueOf(e));
//                                            Toast.makeText(MusicPlayer.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        SharedPreferences sh = getSharedPreferences("prefsCountPuzA", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        a = sh.getInt("firstStartCountPuzA", 0);

// We can then use the data
        Log.d("A Count", String.valueOf(a));

        int b = a + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsCountPuzA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartCountPuzA", b);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsCountPuzA", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int a1 = sha.getInt("firstStartCountPuzA", 0);

        Log.d("A Count2", String.valueOf(a1));
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
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Puzzle Advanced").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month))
//                .child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//        reference.setValue(scoreLong);
        SharedPreferences sha1 = getSharedPreferences("prefsTimeMem", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        x = sha1.getInt("firstStartTimeMem", 0);

// We can then use the data
        Log.d("A Count", String.valueOf(x));

        int y = x + 1;

        SharedPreferences prefsCount = getSharedPreferences("prefsTimeMem", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefsCount.edit();
        editor1.putInt("firstStartTimeMem", y);
        editor1.apply();
        SharedPreferences sha2 = getSharedPreferences("prefsTimeMem", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int x1 = sha.getInt("firstStartTimeMem", 0);

        Log.d("A Count2", String.valueOf(x1));

//        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TimeChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month))
//                .child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
//        reference1.setValue(scoreLong);
        SharedPreferences sh1 = getSharedPreferences("prefsCountMem", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        i = sh1.getInt("firstStartCountMem", 0);

// We can then use the data
        Log.d("A Count Mem", String.valueOf(i));

        int c = i + 1;

        SharedPreferences prefsCount1Mem = getSharedPreferences("prefsCountMem", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = prefsCount1Mem.edit();
        editor2.putInt("firstStartCountMem", c);
        editor2.apply();
        SharedPreferences sha2w = getSharedPreferences("prefsCountMem", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int i1 = sha2w.getInt("firstStartCountMem", 0);

        Log.d("A Count2 Mem", String.valueOf(i1));


        exit = findViewById(R.id.exit);
        dialog = new Dialog(this);

        openLineChartIntervention();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
    }

    private void openLineChartIntervention() {
        Button ok;

        LineChart lineChart;
        dialog.setContentView(R.layout.game_intervention_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ok = (Button) dialog.findViewById(R.id.ok);

        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
        lineEntries = new ArrayList();
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "Puzzles Progress");
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
//        coins.setText("Score: "+scoreLong);
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

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Puzzle Advanced").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Log.d("XVAL", dataSnapshot.getKey());
//                    float xxVal = (Float.parseFloat(dataSnapshot.getKey()));
//
//                    Log.d("XArrayList", String.valueOf(xVal));
//                    float yyVal = (Float.parseFloat(String.valueOf((Long) dataSnapshot.getValue())));
//                    Log.d("YVAL", String.valueOf(yyVal));
//                    Log.d("YArrayList", String.valueOf(yVal));
//                    lineEntries.add(new Entry(xxVal, yyVal));
//
//                    lineDataSet = new LineDataSet(lineEntries, "Puzzle Advanced Progress on "+str);
//                    lineData = new LineData(lineDataSet);
//                    lineChart.setData(lineData);
//
//                    lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                    lineDataSet.setValueTextColor(Color.WHITE);
//                    lineDataSet.setValueTextSize(10f);
//
//                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                    lineChart.setBorderColor(Color.TRANSPARENT);
//                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                    lineChart.getAxisLeft().setDrawGridLines(false);
//                    lineChart.getXAxis().setDrawGridLines(false);
//                    lineChart.getAxisRight().setDrawGridLines(false);
//                    lineChart.getXAxis().setTextColor(R.color.white);
//                    lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getDescription().setTextColor(R.color.white);
//                    lineChart.invalidate();
//                    lineChart.refreshDrawableState();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        database.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//
                Log.d("Current Coins", String.valueOf(user.getCoins()));
//
//                totalPoints.setText("Total Coins: " + user.getCoins());
//

            }
        });


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