package com.example.thinkableproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class LineChartExample extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    AppCompatButton ok;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
//    TextView earnedPoints;
    FirebaseFirestore database;
    User user;
//    TextView total_points;
    int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_example);
        ok = findViewById(R.id.ok);
        database = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        int value = sharedPreferences.getInt("value", 0);

        Log.d("Coins", String.valueOf(value));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createPersonalizedAd();
            }
        });

//        earnedPoints = findViewById(R.id.points);
//        total_points = findViewById(R.id.total);


//        earnedPoints.setText("Coins Earned:" + value);


//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user = documentSnapshot.toObject(User.class);
////
//                Log.d("Current Coins", String.valueOf(user.getCoins()));
////
//                total_points.setText("Total Coins: " + user.getCoins());
////
//
//            }
//        });
        lineChart = findViewById(R.id.lineChart);
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "Concentration and Memory Improvement");
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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(LineChartExample.this);
                } else
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });
    }

    private void createPersonalizedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createInstestialAd(adRequest);
    }

    private void createInstestialAd(AdRequest adRequest) {
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/8691691433", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                startActivity(new Intent(getApplicationContext(), Music.class));
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    private void getEntries() {
        lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(2f, 23f));
        lineEntries.add(new Entry(4f, 34f));
        lineEntries.add(new Entry(6f, 2f));
        lineEntries.add(new Entry(8f, 66f));
        lineEntries.add(new Entry(7f, 12f));
        lineEntries.add(new Entry(3f, 9f));
    }
}