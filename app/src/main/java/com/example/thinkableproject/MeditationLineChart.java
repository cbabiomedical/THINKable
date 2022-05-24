package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.sample.JsonPlaceHolder;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeditationLineChart extends AppCompatActivity {

    AppCompatButton ok;
    LineChart lineChart;
    LineData lineData;
    Double sum=0.0;
    Double average=0.0;
    JsonPlaceHolder jsonPlaceHolder;
    private InterstitialAd mInterstitialAd;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();
    int c;
    FirebaseUser mUser;
    //    TextView earnedPoints;
    FirebaseFirestore database;
    User user;
    //    TextView total_points;
    int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_line_chart);
        ok = findViewById(R.id.ok);
        database = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        int value = sharedPreferences.getInt("value", 0);

        Log.d("Coins", String.valueOf(value));
        mUser=FirebaseAuth.getInstance().getCurrentUser();

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                createPersonalizedAd();
//            }
//        });
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

        SharedPreferences shs = getSharedPreferences("prefsTimeRelWH", MODE_APPEND);

        c = shs.getInt("firstStartTimeRelWH", 0);

        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        Log.d("MeditationIndex", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_indexMed));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS).build();



        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.137:5000/").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);

        Log.d("BroadcastMus", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_indexMed));
        if (BroadcastReceiver_BTLE_GATT.relaxation_indexMed.size() > 0) {

            //Post Time to Relax and Time stayed Relaxed

            Call<List> callMusRel = jsonPlaceHolder.PostTimeToRelax(BroadcastReceiver_BTLE_GATT.relaxation_indexMed);
            callMusRel.enqueue(new Callback<List>() {
                @Override
                public void onResponse(Call<List> call, Response<List> response) {
                    Toast.makeText(getApplicationContext(), "Post Space Successful", Toast.LENGTH_SHORT).show();
                    Log.d("SUMRELResponseTime SP", String.valueOf(response.code()));
                    Log.d("SUMRELResTime Message", response.message());
                    Log.d("SUMRELResTime Body", String.valueOf(response.body()));
                    List com = response.body();
                    LinkedTreeMap hashmap = new LinkedTreeMap();
                    hashmap = (LinkedTreeMap) com.get(0);
                    Log.d("HashMapMus", String.valueOf(hashmap));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReference.setValue(hashmap.get("time_to_relax"));
                    DatabaseReference databaseReferencet = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Relaxation").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReferencet.setValue(hashmap.get("time relaxed"));

                    // Where Am I data saving
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
                    LinkedTreeMap finalHashmap = hashmap;
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("Time to Relax").child(String.valueOf(snapshot.getValue())).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference2.setValue(finalHashmap.get("time_to_relax"));
                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("Time Stayed Relaxed").child(String.valueOf(snapshot.getValue())).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference3.setValue(finalHashmap.get("time relaxed"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference referenceAge = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("dob");
                    LinkedTreeMap finalHashmap1 = hashmap;
                    referenceAge.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("Ageof User", String.valueOf(snapshot.getValue()));
                            String date = snapshot.getValue().toString();
                            String[] splited = date.split(" ");
                            Log.d("DateHR", splited[2]);
                            int year = Integer.parseInt(splited[2]);
                            int currentYear = now.get(Calendar.YEAR);
                            int age = currentYear - year;
                            Log.d("Age", String.valueOf(age));

                            if (age >= 10 && age <= 20) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 20 && age <= 30) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 30 && age <= 40) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 40 && age <= 50) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 50 && age <= 60) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 60 && age <= 70) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 70 && age <= 80) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));

                            } else if (age > 80 && age <= 90) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Relax").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_relax"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time Stayed Relaxed").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time relaxed"));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                    Log.d("ErrorValSpa", String.valueOf(t));
                }
            });

        }

        if (BroadcastReceiver_BTLE_GATT.relaxation_indexMed.size() > 0) {
            for (int i = 0; i < BroadcastReceiver_BTLE_GATT.relaxation_indexMed.size(); i++) {
                Log.d("GETI", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_indexMed));
                sum += (Double) BroadcastReceiver_BTLE_GATT.relaxation_indexMed.get(i);
            }
            Log.d("SUMRELS", String.valueOf(sum));
            average = sum / BroadcastReceiver_BTLE_GATT.relaxation_indexMed.size();
            Log.d("SUMRELS", String.valueOf(average));
            Double averageD = Double.valueOf(String.format("%.3g%n", average));

            DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("MeditationIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention.setValue(averageD);

            DatabaseReference referenceIntervention1 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention1.setValue(averageD);
        }
        if (BroadcastReceiver_BTLE_GATT.relaxation_indexMed.size() == 0) {
            average = 0.0;
        }


        lineChart = (LineChart)findViewById(R.id.lineChart);
        lineEntries = new ArrayList();

        DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("MeditationIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str);
        referenceIntervention.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("XVAL", dataSnapshot.getKey());
                    float xxVal = (Float.parseFloat(dataSnapshot.getKey()));

                    Log.d("XArrayList", String.valueOf(xVal));
                    float yyVal = (Float.parseFloat(String.valueOf((Double) dataSnapshot.getValue())));
                    Log.d("YVAL", String.valueOf(yyVal));
                    Log.d("YArrayList", String.valueOf(yVal));
                    lineEntries.add(new Entry(xxVal, yyVal));

                    lineDataSet = new LineDataSet(lineEntries, "Meditation Relaxation Progress on " + str);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ok  =findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MeditationExercise.class));
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
                                startActivity(new Intent(getApplicationContext(), MeditationExercise.class));
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