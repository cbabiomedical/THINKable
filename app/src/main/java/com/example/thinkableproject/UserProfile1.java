package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.EEG_Values;
import com.example.thinkableproject.adapters.PostAdapter;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.example.thinkableproject.sample.Post;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserProfile1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    JsonPlaceHolder jsonPlaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile1);
        //Initialize and Assign Variable


        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://192.168.8.137:8000/").addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolder= retrofit.create(JsonPlaceHolder.class);

        createPost();
    }

    private void createPost() {
        EEG_Values eeg_values=new EEG_Values(23,75,86,35,86);
        Call<EEG_Values> call=jsonPlaceHolder.createPost(eeg_values);

        call.enqueue(new Callback<EEG_Values>() {
            @Override
            public void onResponse(Call<EEG_Values> call, Response<EEG_Values> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfile1.this, "Failed to get Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<EEG_Values> postList=new ArrayList<>();
                postList.add(response.body());

                PostAdapter postAdapter=new PostAdapter(postList,UserProfile1.this);
                recyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onFailure(Call<EEG_Values> call, Throwable t) {
                Toast.makeText(UserProfile1.this,"Failed to get Data",Toast.LENGTH_SHORT).show();

            }
        });
    }
}