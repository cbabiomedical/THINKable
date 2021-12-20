package com.example.thinkableproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.EEG_Values;
import com.example.thinkableproject.adapters.EEGAdapter;
import com.example.thinkableproject.adapters.PostAdapter;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.example.thinkableproject.sample.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;


public class Meditation extends AppCompatActivity {
    RecyclerView recyclerView;
    JsonPlaceHolder jsonPlaceHolder;
    List<EEG_Values> eeg_values;
    EEGAdapter eegAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        recyclerView = findViewById(R.id.recycler_view);

        //Initialize and Assign Variable

        //Perform ItemSelectedListener

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
//


        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.105:8000/")
                .addConverterFactory(GsonConverterFactory.create(gson)) //important
                .build();

        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
        Call<List<EEG_Values>> call1 = jsonPlaceHolder.getValues();
        call1.enqueue(new Callback<List<EEG_Values>>() {
            @Override
            public void onResponse(Call<List<EEG_Values>> call, Response<List<EEG_Values>> response) {
                Log.d("EEG Response", String.valueOf(response.body()));

                if (response.isSuccessful()) {
                    Toast.makeText(Meditation.this, " Get Successful", Toast.LENGTH_SHORT).show();
                    Log.d("EEG Response", String.valueOf(response.body()));
                    eeg_values = response.body();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    eegAdapter = new EEGAdapter(eeg_values, Meditation.this);
                    recyclerView.setAdapter(eegAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<EEG_Values>> call, Throwable t) {
                Log.d("Error", String.valueOf(t));

            }
        });

        createPostEEG();


    }

    private void createPostEEG() {
        Retrofit retrofit1 = new Retrofit.Builder().baseUrl("http://192.168.8.105:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolder jsonPlaceHolder1 = retrofit1.create(JsonPlaceHolder.class);

        EEG_Values eeg_value = new EEG_Values(63, 24, 63, 64, 25);
        Call<Void> call = jsonPlaceHolder1.PostData(63, 24, 63, 64, 25);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Response Code", String.valueOf(response.code()));
                Toast.makeText(Meditation.this, "Post Successful", Toast.LENGTH_SHORT).show();
                EEG_Values eeg_value = new EEG_Values(63, 24, 63, 64, 25);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Meditation.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));


            }
        });

    }
}