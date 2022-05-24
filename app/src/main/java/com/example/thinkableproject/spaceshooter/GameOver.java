package com.example.thinkableproject.spaceshooter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.BroadcastReceiver_BTLE_GATT;
import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    private InterstitialAd mInterstitialAd;
    FirebaseFirestore database;
    User user;
    Double average = 0.0;
    Double sum = 0.0;
    int updatedCoins;
    int c;
    JsonPlaceHolder jsonPlaceHolder;
    Dialog dialogIntervention;
    LineData lineData;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    FirebaseUser mUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        database = FirebaseFirestore.getInstance();
        int points = getIntent().getExtras().getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);
        dialogIntervention = new Dialog(this);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(points));
                updatedCoins = (int) (user.getCoins() + points);
                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                database.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(GameOver.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        SharedPreferences shs = getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        c = shs.getInt("firstStartTimeConWH", 0);
        Log.d("CVAL", String.valueOf(c));

// We can then use the data
        Log.d("A Count", String.valueOf(c));
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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS).build();

        if (BroadcastReceiver_BTLE_GATT.concentration_indexes.size() > 0) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.104:5000/").client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
            ArrayList data = new ArrayList((Arrays.asList(0.5, 0.6, 0.7, 0.3, 0.8, 0.55, 0.65, 0.75, 0.45, 0.7, 0.65, 0.55, 0.1, 0.45)));

            Call<List> callMusRel = jsonPlaceHolder.PostTimeToConcentrate(BroadcastReceiver_BTLE_GATT.concentration_indexes);
            callMusRel.enqueue(new Callback<List>() {
                @Override
                public void onResponse(Call<List> call, Response<List> response) {
                    Toast.makeText(getApplicationContext(), "Post Space Successful", Toast.LENGTH_SHORT).show();
                    Log.d("SUMCONResponseTime SP", String.valueOf(response.code()));
                    Log.d("SUMCONResTime Message", response.message());
                    Log.d("SUMCONResTime Body", String.valueOf(response.body()));
                    List com = response.body();
                    LinkedTreeMap hashmap = new LinkedTreeMap();
                    hashmap = (LinkedTreeMap) com.get(0);
                    Log.d("SUMCONH", String.valueOf(hashmap.get("time_to_concentrate")));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReference.setValue(hashmap.get("time_to_concentrate"));
                    DatabaseReference databaseReferencet = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReferencet.setValue(hashmap.get("time concentrated"));

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
                    LinkedTreeMap finalHashmap = hashmap;
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("Time to Concentrate").child(String.valueOf(snapshot.getValue())).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference2.setValue(finalHashmap.get("time_to_concentrate"));
                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("Time Stayed Concentrate").child(String.valueOf(snapshot.getValue())).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference3.setValue(finalHashmap.get("time concentrated"));
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
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 20 && age <= 30) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 30 && age <= 40) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 40 && age <= 50) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 50 && age <= 60) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 60 && age <= 70) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 70 && age <= 80) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 80 && age <= 90) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.d("SUMCOGETI", String.valueOf(com.get(0)));

                }

                @Override
                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                    Log.d("ErrorValSpa", String.valueOf(t));
                }
            });
        }


        Log.d("SUMCON", String.valueOf(BroadcastReceiver_BTLE_GATT.concentration_indexes));
        if (BroadcastReceiver_BTLE_GATT.concentration_indexes.size() > 0) {
            for (int i = 0; i < BroadcastReceiver_BTLE_GATT.concentration_indexes.size(); i++) {
                sum += (Double) BroadcastReceiver_BTLE_GATT.concentration_indexes.get(i);
            }
            Log.d("SUMCONCS", String.valueOf(sum));
            average = sum / BroadcastReceiver_BTLE_GATT.concentration_indexes.size();
            Log.d("SUMCONCA", String.valueOf(average));
            Double averageD = Double.valueOf(String.format("%.3g%n", average));

            DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("SpaceShooterIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention.setValue(averageD);
        }
        if (BroadcastReceiver_BTLE_GATT.concentration_indexes.size() == 0) {
            average = 0.0;
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createPersonalizedAd();
            }
        });
        Log.d("CHECK", String.format("%.3g%n", 0.912300));
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
                                startActivity(new Intent(getApplicationContext(), StartUp.class));
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
        openLineChart();
    }

    private void openLineChart() {
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
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);
        Button ok;
        LineChart lineChart;
//        TextView points;
//        TextView totalPoints;

        dialogIntervention.setContentView(R.layout.game_intervention_popup);
        dialogIntervention.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lineChart = (LineChart) dialogIntervention.findViewById(R.id.lineChartInterventionGame);
        lineEntries = new ArrayList();

        DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("SpaceShooterIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str);
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

                    lineDataSet = new LineDataSet(lineEntries, "SpaceHooter Progress on " + str);
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

    public void restart(View view) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(GameOver.this);
        } else {
            Intent intent = new Intent(GameOver.this, StartUp.class);
            startActivity(intent);
            finish();
        }


    }


//

    public void exit(View view) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(GameOver.this);
        } else {

            finish();
        }
    }
}