package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class MeditationExercise extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout linearLayoutManager;
    List<MeditationModelClass> meditationList;
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
//        meditationList = new ArrayList<>();
        //Adding user preferences to arraylist
//        meditationList.add(new MeditationModelClass(R.drawable.mindful, "Mindfulness", R.drawable.ic_favorite));
//        meditationList.add(new MeditationModelClass(R.drawable.maxresdefault, "Body Scan", R.drawable.ic_favorite));
//        meditationList.add(new MeditationModelClass(R.drawable.love_kind, "Loving", R.drawable.ic_favorite));
//        meditationList.add(new MeditationModelClass(R.drawable.transidental, "transcendental ", R.drawable.ic_favorite));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject meditationObject = response.getJSONObject(i);
                        MeditationModelClass meditationModelClass = new MeditationModelClass();
                        // key name should be given exactly as in json file
                        meditationModelClass.setMeditationName(meditationObject.getString("title").toString());
//                        meditationModelClass.setImageView(meditationObject.getString("userId").toString());
                        meditationModelClass.setImageView(R.drawable.love_kind);
                        meditationModelClass.setMeditation_url(meditationObject.getString("title").toString());
                        meditationList.add(meditationModelClass);
                        Log.d("List", String.valueOf(meditationList));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new MeditationAdapter(meditationList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", String.valueOf(error));
            }
        });
        requestQueue.add(jsonArrayRequest);


    }


}