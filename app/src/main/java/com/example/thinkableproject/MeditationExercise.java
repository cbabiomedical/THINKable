package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MeditationExercise extends AppCompatActivity implements MeditationAdapter.OnNoteListner {
    RecyclerView recyclerView;
    ArrayList<MeditationModelClass> meditationList;
    MeditationAdapter adapter;
    int time;
    String selected_time;

    private static String JSON_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_exercise);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.exercise);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Relaxation_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.gridView);
        Spinner dropdown_time = (Spinner) findViewById(R.id.spinner2);
        String[] items = new String[]{"Audio track duration is: 1 min", "Audio track duration is: 1.5 min", "Audio track duration is: 2 min", "Audio track duration is: 2.5 min", "Audio track duration is: 3 min"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown_time.setAdapter(adapter1);

        dropdown_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_time = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    time = 60000;
                    Log.d("TIME", String.valueOf(time));
                } else if (position == 1) {
                    time = 90000;
                    Log.d("TIME", String.valueOf(time));
                } else if (position == 2) {
                    time = 120000;
                } else if (position == 3) {
                    time = 150000;
                } else {
                    time = 180000;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //  favouriteBtn = findViewById(R.id.favouritesIcon);
        meditationList = new ArrayList<>();

        initData();
        //Calling initRecyclerView function
    }

    private void initData() {
        meditationList = new ArrayList<>();
//        Adding user preferences to arraylist
//        meditationList.add(new MeditationModelClass("Mindfulness", R.drawable.mindful, "0", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
//        meditationList.add(new MeditationModelClass("Body Scan", R.drawable.maxresdefault, "1", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
//        meditationList.add(new MeditationModelClass("Loving", R.drawable.love_kind, "2", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
//        meditationList.add(new MeditationModelClass("Transcendental ", R.drawable.transidental, "3", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Meditation_Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MeditationModelClass post=dataSnapshot.getValue(MeditationModelClass.class);
                    meditationList.add(post);
                    Log.d("MeditationPost", String.valueOf(post));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new MeditationAdapter(meditationList, getApplicationContext(), this::onNoteClickMeditation);
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
    public void onNoteClickMeditation(int position) {
        meditationList.get(position);
        String songName = meditationList.get(position).getMeditateName();
        String url = meditationList.get(position).getUrl();
        String image = meditationList.get(position).getMeditateImage();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), PlayMeditation.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image).putExtra("time", time));
    }
}