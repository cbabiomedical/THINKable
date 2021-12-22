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

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Exercise extends AppCompatActivity implements MusicAdapter.OnNoteListner, GridAdapter.OnNoteListner {
    ImageView relaxation;
    TextView music, games;
    RecyclerView musicRecyclerView, gameRecyclerView;
    MusicAdapter musicAdapter;
    GridAdapter gameAdapter;
    ArrayList<MusicModelClass> musicList;
    ArrayList<GameModelClass> gameList;
    HashMap<String, Object> gamesHashMap = new HashMap<>();
    ArrayList<GameModelClass> downloadGames = new ArrayList<>();
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        gameRecyclerView = findViewById(R.id.gamesRecyclerView);
        relaxation = findViewById(R.id.relaxation);
        music = findViewById(R.id.musicTitle);
        games = findViewById(R.id.gameTitle);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
        relaxation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RelaxationExercise.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
        musicList = new ArrayList<>();
//        meditationModelClassArrayList=new ArrayList<>();
        gameList = new ArrayList<>();
        initData();

    }

    private void initData() {
        musicAdapter = new MusicAdapter(musicList, getApplicationContext(), this::onNoteClick);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs_Admin");
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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Games_Admin");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GameModelClass post = dataSnapshot.getValue(GameModelClass.class);
                    gameList.add(post);

                }
                gameRecyclerView.setLayoutManager(new LinearLayoutManager(Exercise.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameAdapter = new GridAdapter(gameList, getApplicationContext(), this::onNoteClickGame);
        gameRecyclerView.setAdapter(gameAdapter);


    }

    @Override
    public void onNoteClick(int position) {
        musicList.get(position);
        String songName = musicList.get(position).getName();
        String url = musicList.get(position).getSongTitle1();
        String image = musicList.get(position).getImageUrl();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), MusicPlayer.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }


    @Override
    public void onNoteClickGame(int position) {
        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();
        gameList.get(position);
        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();


        GameModelClass gameModelClass = new GameModelClass(gameList.get(position).getGameImage(), gameList.get(position).getGameName());

        downloadGames.add(gameModelClass);
        Log.d("DownloadGames", String.valueOf(downloadGames));
        gamesHashMap.put(downloadGames.get(position).getGameName(), gameModelClass);

        Log.d("CHECK UPLOAD", "-------------------------CHECKING FIREBASE UPLOAD-----------------");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
        reference1.setValue(gamesHashMap);

        Log.d("CHECK UPLOAD", "------------------------CHECK AFTER UPLOAD------------");

        Log.d("Downloads", String.valueOf(downloadGames));

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
        if (launchIntent != null) {
            Log.d("Tagopenapp", "---------------------B--------------------------");
            startActivity(launchIntent);
        } else {
            Toast.makeText(Exercise.this, "There is no package", Toast.LENGTH_LONG).show();
        }


    }

}