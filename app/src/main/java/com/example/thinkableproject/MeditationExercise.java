package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeditationExercise extends AppCompatActivity implements MeditationAdapter.OnNoteListner{
    RecyclerView recyclerView;
    LinearLayout linearLayoutManager;
    ArrayList<MeditationModelClass> meditationList;
    MeditationAdapter adapter;

    private static String JSON_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_exercise);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);
        meditationList = new ArrayList<>();

        initData();
        //Calling initRecyclerView function
    }

    private void initData() {
        meditationList = new ArrayList<>();
//        Adding user preferences to arraylist
        meditationList.add(new MeditationModelClass( "Mindfulness",R.drawable.mindful, "0","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationList.add(new MeditationModelClass( "Body Scan",R.drawable.maxresdefault, "1","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationList.add(new MeditationModelClass( "Loving", R.drawable.love_kind,"2","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationList.add(new MeditationModelClass( "Transcendental ",R.drawable.transidental,"3","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new MeditationAdapter(meditationList,getApplicationContext(),this::onNoteClick);
                        recyclerView.setAdapter(adapter);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject meditationObject = response.getJSONObject(i);
//                        MeditationModelClass meditationModelClass = new MeditationModelClass();
//                        // key name should be given exactly as in json file
//                        meditationModelClass.setMeditationName(meditationObject.getString("title").toString());
////                        meditationModelClass.setImageView(meditationObject.getString("userId").toString());
//                        meditationModelClass.setImageView(R.drawable.love_kind);
////                        meditationModelClass.setMeditation_url(meditationObject.getString("title").toString());
//                        meditationList.add(meditationModelClass);
//                        Log.d("List", String.valueOf(meditationList));
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("TAG", String.valueOf(error));
//            }
//        });
//        requestQueue.add(jsonArrayRequest);
//
//
    }


    @Override
    public void onNoteClick(int position) {
        meditationList.get(position);
        String songName=meditationList.get(position).getMeditationName();
        String url=meditationList.get(position).getUrl();
        int image=meditationList.get(position).getImageView();
        Log.d("Url",url);
        startActivity(new Intent(getApplicationContext(),PlayMeditation.class).putExtra("url",url).putExtra("name",songName).putExtra("image",image));
    }
}