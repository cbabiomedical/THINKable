package com.example.thinkableproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.IHaveToFly.IHaveToFlyMainActivity;
import com.example.thinkableproject.WordMatching.MainMenu;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.duckhunt.StartGame;
import com.example.thinkableproject.ninjadarts.NinjaDartsMainActivity;
import com.example.thinkableproject.pianotiles.Main3Activity;
import com.example.thinkableproject.puzzle.PuzzleMainActivity;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.example.thinkableproject.spaceshooter.StartUp;
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
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class MemoryExercise extends AppCompatActivity implements MusicAdapter.OnNoteListner, GridAdapter.OnNoteListner, MeditationAdapter.OnNoteListner {
    ImageView relaxation, concentration;
    TextView music, games, meditation;
    RecyclerView musicRecyclerView, gameRecyclerView, meditationRecyclerview;
    MusicAdapter musicAdapter;
    GridAdapter gameAdapter;
    MeditationAdapter meditationAdapter;
    int color;
    Dialog dialogMem;
    View c1, c2;
    ImageView memoryInfo;
    GifImageView c1gif, c2gif;
    ArrayList<MusicModelClass> musicList;
    ArrayList<GameModelClass> gameList;
    ArrayList<MeditationModelClass> meditationList;
    HashMap<String, Object> gamesHashMap = new HashMap<>();
    ArrayList<GameModelClass> downloadGames = new ArrayList<>();
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_exercise);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        gameRecyclerView = findViewById(R.id.gamesRecyclerView);
        meditationRecyclerview = findViewById(R.id.meditationRecyclerView);
        relaxation = findViewById(R.id.relaxation);
        concentration = findViewById(R.id.concentration);
        music = findViewById(R.id.musicTitle);
        games = findViewById(R.id.gameTitle);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        dialogMem = new Dialog(this);
        meditation = findViewById(R.id.meditationTitle);
        memoryInfo = findViewById(R.id.memoryInfo);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c1gif.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//light theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                        c1gif.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.INVISIBLE);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SharedPreferences prefsMemEx = getSharedPreferences("prefsMemEx", MODE_PRIVATE);
        boolean firstStartMemEx = prefsMemEx.getBoolean("firstStartMemEx", true);

        if (firstStartMemEx) {
            displayMemInstructions();
        }


        memoryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMemInstructions();
            }
        });


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

        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Exercise.class));
            }
        });
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeditationExercise.class));
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

        meditationList = new ArrayList<>();
        initData();

    }

    private void initData() {
        musicAdapter = new MusicAdapter(musicList, getApplicationContext(), this::onNoteClick);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs_Admin").child("Songs_Memory");
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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Games_Admin").child("Games_Memory");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GameModelClass post = dataSnapshot.getValue(GameModelClass.class);
                    gameList.add(post);

                }
                gameRecyclerView.setLayoutManager(new LinearLayoutManager(MemoryExercise.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameAdapter = new GridAdapter(gameList, getApplicationContext(), this::onNoteClickGame);
        gameRecyclerView.setAdapter(gameAdapter);

        DatabaseReference meditationReference = FirebaseDatabase.getInstance().getReference("Meditation_Admin").child("Meditation_Memory");
        meditationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MeditationModelClass medipost = dataSnapshot.getValue(MeditationModelClass.class);
                    Log.d("MeditationPostMem", String.valueOf(medipost));

                    meditationList.add(medipost);
                }
                meditationRecyclerview.setLayoutManager(new LinearLayoutManager(MemoryExercise.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        meditationAdapter = new MeditationAdapter(meditationList, getApplicationContext(), this::onNoteClickMeditation);
        meditationRecyclerview.setAdapter(meditationAdapter);

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
//        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();
        gameList.get(position);
        if (gameList.get(position).getGameName().equals("Card Memory Game")) {
            startActivity(new Intent(getApplicationContext(), MainActivityK.class));
        } else if (gameList.get(position).getGameName().equals("Color Pattern Game")) {
            startActivity(new Intent(getApplicationContext(), ColorPatternGame.class));
        } else if (gameList.get(position).getGameName().equals("SpaceHooter")) {
            startActivity(new Intent(getApplicationContext(), StartUp.class));
        } else if (gameList.get(position).getGameName().equals("Duck Hunt")) {
            startActivity(new Intent(getApplicationContext(), StartGame.class));
        } else if (gameList.get(position).getGameName().equals("Ninja Dart")) {
            startActivity(new Intent(getApplicationContext(), NinjaDartsMainActivity.class));
        } else if (gameList.get(position).getGameName().equals("Piano Tiles")) {
            startActivity(new Intent(getApplicationContext(), Main3Activity.class));
        } else if (gameList.get(position).getGameName().equals("Puzzles")) {
            startActivity(new Intent(getApplicationContext(), PuzzleMainActivity.class));
        } else if (gameList.get(position).getGameName().equals("Puzzle Advanced")) {
            startActivity(new Intent(getApplicationContext(), com.example.thinkableproject.DragandDropPuzzle.PuzzleMainActivity.class));
        } else if(gameList.get(position).getGameName().equals("I Have to Fly")){
            startActivity(new Intent(getApplicationContext(), IHaveToFlyMainActivity.class));
        } else if(gameList.get(position).getGameName().equals("Word Match")){
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
        }else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
            if (launchIntent != null) {
                Log.d("Tagopenapp", "---------------------B--------------------------");
                startActivity(launchIntent);
            } else {
                Toast.makeText(MemoryExercise.this, "There is no package", Toast.LENGTH_LONG).show();
            }

        }
//        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();


        GameModelClass gameModelClass = new GameModelClass(gameList.get(position).getGameImage(), gameList.get(position).getGameName());

        downloadGames.add(gameModelClass);
        Log.d("DownloadGames", String.valueOf(downloadGames));
//        gamesHashMap.put(downloadGames.get(position).getGameName(), gameModelClass);

        Log.d("CHECK UPLOAD", "-------------------------CHECKING FIREBASE UPLOAD-----------------");
//        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
//        reference1.setValue(gamesHashMap);

        Log.d("CHECK UPLOAD", "------------------------CHECK AFTER UPLOAD------------");

        Log.d("Downloads", String.valueOf(downloadGames));


    }

    @Override
    public void onNoteClickMeditation(int position) {
        meditationList.get(position);
        String meditationName = meditationList.get(position).getMeditateName();
        String url = meditationList.get(position).getUrl();
        String image = musicList.get(position).getImageUrl();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), PlayMeditation.class).putExtra("url", url).putExtra("name", meditationName).putExtra("image", image));

    }

    @Override
    protected void onStart() {
        super.onStart();
//        displayMemInstructions();
    }

    private void displayMemInstructions() {
        Button ok;
        View c1, c2;

        dialogMem.setContentView(R.layout.memorytraining_popup);
        ok = (Button) dialogMem.findViewById(R.id.clickMem);
        dialogMem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        c1 = (View) dialogMem.findViewById(R.id.c1);
//        c2 = (View) dialogMem.findViewById(R.id.c2);

//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//
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
                dialogMem.dismiss();
            }
        });
        dialogMem.show();

        SharedPreferences prefsMemEx = getSharedPreferences("prefsMemEx", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsMemEx.edit();
        editor.putBoolean("firstStartMemEx", false);
        editor.apply();

    }
}