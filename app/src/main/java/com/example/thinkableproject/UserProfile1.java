package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.EEG_Values;
import com.example.thinkableproject.adapters.PostAdapter;
import com.example.thinkableproject.sample.Brain_Waves;
import com.example.thinkableproject.sample.Concentration;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.example.thinkableproject.sample.Memory;
import com.example.thinkableproject.sample.Post;
import com.example.thinkableproject.sample.Relaxation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserProfile1 extends AppCompatActivity {

    JsonPlaceHolder jsonPlaceHolder;
    List<Post> postList;
    PostAdapter postAdapter;
    List<Concentration> concentrationList;
    List<Relaxation> relaxationList;
    List<EEG_Values> eeg_valuesList;
    List<Brain_Waves> brain_wavesList;
    List<Memory> memoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile1);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        postList = new ArrayList<>();
        concentrationList = new ArrayList<>();
        relaxationList = new ArrayList<>();
        eeg_valuesList = new ArrayList<>();
        brain_wavesList = new ArrayList<>();
        memoryList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.137:5000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);

        //GET CONCENTRATION VALUES
        Call<List<Concentration>> callConcentration = jsonPlaceHolder.getConcentrationValues();
        callConcentration.enqueue(new Callback<List<Concentration>>() {
            @Override
            public void onResponse(Call<List<Concentration>> call, Response<List<Concentration>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
                    Log.d("GET", "Successful");
                    concentrationList = (response.body());
                    Log.d("Concentration Response", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(Call<List<Concentration>> call, Throwable t) {

            }
        });

        //get eeg values

        Call<List<EEG_Values>> eeg = jsonPlaceHolder.getValues();
        eeg.enqueue(new Callback<List<EEG_Values>>() {
            @Override
            public void onResponse(Call<List<EEG_Values>> call, Response<List<EEG_Values>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
                    Log.d("GET", "Successful");
                    eeg_valuesList = (response.body());
                    Log.d("EEG Response", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(Call<List<EEG_Values>> call, Throwable t) {

            }
        });

        // get Memory Value

        Call<List<Memory>> memory = jsonPlaceHolder.getMemoryValues();
        memory.enqueue(new Callback<List<Memory>>() {
            @Override
            public void onResponse(Call<List<Memory>> call, Response<List<Memory>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
                    Log.d("GET", "Successful");
                    memoryList = (response.body());
                    Log.d("EEG Response", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(Call<List<Memory>> call, Throwable t) {

            }
        });

        //Post Memory Data

        Call<Void> call6 = jsonPlaceHolder.PostMemoryData(56, 35, 54, 14, 64);
        call6.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
                Log.d("Memory Data", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));

            }
        });

        // Post EEG data

        Call<Void> call4 = jsonPlaceHolder.PostData(56, 35, 54, 14, 64);
        call4.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
                Log.d("EEG Post", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));

            }
        });

        //post method
//        Call<List<Post>> call1 = jsonPlaceHolder.getPost();
//        call1.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                Log.d("Response Post", String.valueOf(response.body()));
//                if (response.isSuccessful()) {
////                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
//                    Log.d("GET", "Successful");
//                    postList = (response.body());
//                    Log.d("Response Post", String.valueOf(response.body()));
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Log.d("GET", "Failed");
//
//            }
//        });

        // Post Concentration Data

        Call<Void> call2 = jsonPlaceHolder.PostConcentrationData(64, 83, 47, 25, 34);
        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
                Log.d("Response Code Con", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));

            }
        });

        // get Relaxation Values

        Call<List<Relaxation>> callRelaxation = jsonPlaceHolder.getRelaxationValues();
        callRelaxation.enqueue(new Callback<List<Relaxation>>() {
            @Override
            public void onResponse(Call<List<Relaxation>> call, Response<List<Relaxation>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
                    Log.d("GET", "Successful");
                    relaxationList = (response.body());
                    Log.d("Relaxation Response", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(Call<List<Relaxation>> call, Throwable t) {

            }
        });

        // get brain waves
        Call<List<Brain_Waves>> callBrainWaves = jsonPlaceHolder.getBrainWavesValues();
        callBrainWaves.enqueue(new Callback<List<Brain_Waves>>() {
            @Override
            public void onResponse(Call<List<Brain_Waves>> call, Response<List<Brain_Waves>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(UserProfile1.this,"Get Successful",Toast.LENGTH_SHORT).show();
                    Log.d("GET", "Successful");
                    brain_wavesList = (response.body());
                    Log.d(" Response BW", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(Call<List<Brain_Waves>> call, Throwable t) {

            }
        });

        Call<Void> call5 = jsonPlaceHolder.PostBrainWavesData(34, 45, 24, 53, 65);
        call5.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
                Log.d("Response Code BW", String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));

            }
        });


        // Post Relaxation Data

        Call<Void> call3 = jsonPlaceHolder.PostRelaxationData(23, 46, 64, 74, 26);
        call3.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
                Log.d("Response Code Rel", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
                Log.d("ErrorVal", String.valueOf(t));

            }
        });


        createPost();

    }

    private void createPost() {

        Post post = new Post("Sanduni", "24", "Unknown");
        Gson gson = new Gson();

//        Call<Void> call = jsonPlaceHolder.createPostVal("Sanduni", "24", "Devi Balika Vidhyalaya");
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.d("Response Code", String.valueOf(response.code()));
//                Toast.makeText(UserProfile1.this, "Post Successful", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(UserProfile1.this, "Failed Post", Toast.LENGTH_SHORT).show();
//                Log.d("ErrorVal", String.valueOf(t));
//
//            }
//        });

//
    }
}