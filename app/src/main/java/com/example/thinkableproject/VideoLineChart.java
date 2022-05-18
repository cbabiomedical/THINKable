package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class VideoLineChart extends AppCompatActivity {
    AppCompatButton ok;
    LineChart lineChart;
    LineData lineData;
    Double sum = 0.0;
    Double average = 0.0;
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
        setContentView(R.layout.activity_video_line_chart);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

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
        Log.d("VideoIndex", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_index_Vid));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.104:5000/").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);

        Log.d("BroadcastVid", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_index_Vid));
        if (BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.size() > 0) {

            //Post Time to Relax and Time stayed Relaxed

            Call<List> callMusRel = jsonPlaceHolder.PostTimeToConcentrate(BroadcastReceiver_BTLE_GATT.relaxation_index_Vid);
            callMusRel.enqueue(new Callback<List>() {
                @Override
                public void onResponse(Call<List> call, Response<List> response) {
                    Toast.makeText(getApplicationContext(), "Post Space Successful", Toast.LENGTH_SHORT).show();
                    Log.d("SUMCONResponseTime SP", String.valueOf(response.code()));
                    Log.d("SUMCONResTime Message", response.message());
                    Log.d("SUMCONResTime Body", String.valueOf(response.body()));
                }

                @Override
                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                    Log.d("ErrorValSpa", String.valueOf(t));
                }
            });
        }

        if (BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.size() > 0) {
            for (int i = 0; i < BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.size(); i++) {
                Log.d("GETI", String.valueOf(BroadcastReceiver_BTLE_GATT.relaxation_index_Vid));
                sum += (Double) BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.get(i);
            }
            Log.d("SUMRELS", String.valueOf(sum));
            average = sum / BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.size();
            Log.d("SUMRELS", String.valueOf(average));
            Double averageD = Double.valueOf(String.format("%.3g%n", average));

            DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("VideoIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention.setValue(averageD);

            DatabaseReference referenceIntervention1 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention1.setValue(averageD);
        }
        if (BroadcastReceiver_BTLE_GATT.relaxation_index_Vid.size() == 0) {
            average = 0.0;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lineEntries=new ArrayList();
                lineChart=findViewById(R.id.lineChart);
                DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("VideoIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str);
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

                            lineDataSet = new LineDataSet(lineEntries, "Video Relaxation Progress on " + str);
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
                        startActivity(new Intent(getApplicationContext(),VideoInterventionActivity.class));
                    }
                });
            }

        }, 3000);
    }

}