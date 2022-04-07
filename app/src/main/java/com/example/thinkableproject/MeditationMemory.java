package com.example.thinkableproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MeditationMemory extends AppCompatActivity implements MeditationAdapter.OnNoteListner {
    RecyclerView recyclerView;
    ArrayList<MeditationModelClass> meditationList;
    MeditationAdapter adapter;
    LinearLayoutManager layoutManager;
    Dialog dialogmeditation;
    ImageView information;
    //    int time;
    FirebaseUser mUser;
    Animation scaleUp, scaleDown;

    View c1, c2;
    int color;
    String selected_time;

    private static String JSON_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_memory);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dialogmeditation = new Dialog(this);
        information=findViewById(R.id.meditationInfo);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {  //light theme
                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
                    c2.setVisibility(View.VISIBLE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);


                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);


                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SharedPreferences prefsMed = getSharedPreferences("prefsMed", MODE_PRIVATE);
        boolean firstStartMed = prefsMed.getBoolean("firstStartMed", true);

        if (firstStartMed) {
            displayInstructionsPopup();
        }

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

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInstructionsPopup();
            }
        });

        recyclerView = findViewById(R.id.gridView);
//        Spinner dropdown_time = (Spinner) findViewById(R.id.spinner2);
        // String[] items = new String[]{"Audio track duration is: 1 min", "Audio track duration is: 1.5 min", "Audio track duration is: 2 min", "Audio track duration is: 2.5 min", "Audio track duration is: 3 min"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown_time.setAdapter(adapter1);
//
//        dropdown_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selected_time = parent.getItemAtPosition(position).toString();
//                if (position == 0) {
//                    time = 60000;
//                    Log.d("TIME", String.valueOf(time));
//                } else if (position == 1) {
//                    time = 90000;
//                    Log.d("TIME", String.valueOf(time));
//                } else if (position == 2) {
//                    time = 120000;
//                } else if (position == 3) {
//                    time = 150000;
//                } else {
//                    time = 180000;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Meditation_Admin").child("Meditation_Memory");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MeditationModelClass post = dataSnapshot.getValue(MeditationModelClass.class);
                    meditationList.add(post);
                    Log.d("MeditationPost", String.valueOf(post));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
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
        startActivity(new Intent(getApplicationContext(), PlayMeditation.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        displayInstructionsPopup();
    }

    private void displayInstructionsPopup() {
        Button ok;
        View c1, c2;
//        ImageView meditationAudioInstruction;
        dialogmeditation.setContentView(R.layout.meditation_activity_popup);
        dialogmeditation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) dialogmeditation.findViewById(R.id.clickMeditation);
//        c1 = (View) dialogmeditation.findViewById(R.id.c1);
//        c2 = (View) dialogmeditation.findViewById(R.id.c2);


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

//        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
//        colorreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("FirebaseColor PopUp", String.valueOf(snapshot.getValue()));
//                color = (int) snapshot.getValue(Integer.class);
//                Log.d("Color", String.valueOf(color));
//
//                if (color == 2) {  //light theme
//                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
//                    c2.setVisibility(View.VISIBLE);
//                } else if (color == 1) { //light theme
//
//                    c1.setVisibility(View.VISIBLE);
//                    c2.setVisibility(View.INVISIBLE);
//
//
//                } else {
//                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme
//
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
//                        c1.setVisibility(View.VISIBLE);
//                        c2.setVisibility(View.INVISIBLE);
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogmeditation.dismiss();
            }
        });

////        meditationAudioInstruction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MeditationExercise.this, "You clicked Meditation Audio Instruction", Toast.LENGTH_SHORT).show();
//            }
//        });

        dialogmeditation.show();

        SharedPreferences prefsMed=getSharedPreferences("prefsMed",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefsMed.edit();
        editor.putBoolean("firstStartMed",false);
        editor.apply();
    }
}
