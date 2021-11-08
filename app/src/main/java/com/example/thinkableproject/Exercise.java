package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

public class Exercise extends AppCompatActivity implements MusicAdapter.OnNoteListner,MeditationAdapter.OnNoteListner{

    RecyclerView musicRecyclerView,meditationRecyclerView,gameRecyclerView;
    MusicAdapter musicAdapter;
    MeditationAdapter meditationAdapter;
    GridAdapter gameAdapter;
    ArrayList<MusicModelClass> musicList;
    ArrayList<MeditationModelClass> meditationModelClassArrayList;
    ArrayList<GameModelClass> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        musicRecyclerView=findViewById(R.id.musicRecyclerView);
        meditationRecyclerView=findViewById(R.id.meditationRecyclerView);
        gameRecyclerView=findViewById(R.id.gamesRecyclerView);

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), UserProfile1.class));
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
        musicList=new ArrayList<>();
        meditationModelClassArrayList=new ArrayList<>();
        gameList = new ArrayList<>();
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
                musicRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                musicRecyclerView.setAdapter(musicAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        meditationModelClassArrayList.add(new MeditationModelClass( "Mindfulness",R.drawable.mindful, "0","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationModelClassArrayList.add(new MeditationModelClass( "Body Scan",R.drawable.maxresdefault, "1","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationModelClassArrayList.add(new MeditationModelClass( "Loving", R.drawable.love_kind,"2","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));
        meditationModelClassArrayList.add(new MeditationModelClass( "Transcendental ",R.drawable.transidental,"3","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c","0"));

        meditationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        meditationAdapter = new MeditationAdapter(meditationModelClassArrayList,getApplicationContext(),this::onNoteClickMeditation);
        meditationRecyclerView.setAdapter(meditationAdapter);

        gameList.add(new GameModelClass(R.drawable.chess, "Chess","0","0"));
        gameList.add(new GameModelClass(R.drawable.images, "Puzzle","1","0"));
        gameList.add(new GameModelClass(R.drawable.sudoku, "Sudoku","2","0"));
        gameList.add(new GameModelClass(R.drawable.crossword, "CrossWord","3","0"));
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        gameAdapter = new GridAdapter(gameList,getApplicationContext());
        gameRecyclerView.setAdapter(gameAdapter);


    }
    @Override
    public void onNoteClick(int position) {
        musicList.get(position);
        String songName = musicList.get(position).getSongName();
        String url = musicList.get(position).getUrl();
        int image = musicList.get(position).getImageView();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), MusicPlayer.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }

    @Override
    public void onNoteClickMeditation(int position) {
        meditationModelClassArrayList.get(position);
        String songName=meditationModelClassArrayList.get(position).getMeditationName();
        String url=meditationModelClassArrayList.get(position).getUrl();
        int image=meditationModelClassArrayList.get(position).getImageView();
        Log.d("Url",url);
        startActivity(new Intent(getApplicationContext(),PlayMeditation.class).putExtra("url",url).putExtra("name",songName).putExtra("image",image));
    }
}