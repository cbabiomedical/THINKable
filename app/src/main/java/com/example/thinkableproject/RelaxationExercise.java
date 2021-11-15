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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RelaxationExercise extends AppCompatActivity implements MusicAdapter.OnNoteListner, MeditationAdapter.OnNoteListner {
    ImageButton concentrationBtn;
    RecyclerView musicRecyclerView, meditationRecyclerView;
    MusicAdapter musicAdapter;
    MeditationAdapter meditationAdapter;
    TextView music,meditation;

    ArrayList<MusicModelClass> musicList;
    ArrayList<MeditationModelClass> meditationModelClassArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_exercise);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        meditationRecyclerView = findViewById(R.id.meditationRecyclerView);
        concentrationBtn = findViewById(R.id.concentration);
        music=findViewById(R.id.musicTitle);
        meditation=findViewById(R.id.meditationTitle);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Music.class));
            }
        });
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MeditationExercise.class));
            }
        });
        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Exercise.class));
            }
        });
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
        meditationModelClassArrayList=new ArrayList<>();
        musicList=new ArrayList<>();

        initData();


    }

    private void initData() {
        musicAdapter = new MusicAdapter(musicList, getApplicationContext(), this::onNoteClick);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Music").child("songList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDatasnapshot : snapshot.getChildren()) {
                    MusicModelClass post = postDatasnapshot.getValue(MusicModelClass.class);
                    Log.d("Post", String.valueOf(post));
                    musicList.add(post);
                }
                Log.d("List", String.valueOf(musicList));
                musicRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                musicRecyclerView.setAdapter(musicAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        meditationModelClassArrayList.add(new MeditationModelClass("Mindfulness", R.drawable.mindful, "0", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
        meditationModelClassArrayList.add(new MeditationModelClass("Body Scan", R.drawable.maxresdefault, "1", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
        meditationModelClassArrayList.add(new MeditationModelClass("Loving", R.drawable.love_kind, "2", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
        meditationModelClassArrayList.add(new MeditationModelClass("Transcendental ", R.drawable.transidental, "3", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c", "0"));
//
        meditationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        meditationAdapter = new MeditationAdapter(meditationModelClassArrayList, getApplicationContext(), this::onNoteClickMeditation);
        meditationRecyclerView.setAdapter(meditationAdapter);


    }

    @Override
    public void onNoteClickMeditation(int position) {
        meditationModelClassArrayList.get(position);
        String songName = meditationModelClassArrayList.get(position).getMeditationName();
        String url = meditationModelClassArrayList.get(position).getUrl();
        int image = meditationModelClassArrayList.get(position).getImageView();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), PlayMeditation.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }

    @Override
    public void onNoteClick(int position) {

    }
}