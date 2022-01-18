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

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.data.Result;
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
import java.util.Calendar;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class Exercise extends AppCompatActivity implements MusicAdapter.OnNoteListner, GridAdapter.OnNoteListner {
    ImageView relaxation;
    TextView music, games;
    RecyclerView musicRecyclerView, gameRecyclerView;
    MusicAdapter musicAdapter;
    GridAdapter gameAdapter;
    int color;
    Dialog dialogCon;
    View c1, c2;
    ImageView concentrationInfo;
    GifImageView c1gif, c2gif;
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
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        concentrationInfo = findViewById(R.id.concentrationInfo);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        dialogCon = new Dialog(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);


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

                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
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
        SharedPreferences prefsConEx = getSharedPreferences("prefsConEx", MODE_PRIVATE);
        boolean firstStartConEx = prefsConEx.getBoolean("firstStartConEx", true);

        if (firstStartConEx) {
            displayConTrainingInstructions();
        }

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

        concentrationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConTrainingInstructions();
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

    public void mem(View view) {
        Intent intentcd = new Intent(Exercise.this, MemoryExercise.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        startActivity(intentcd);
    }


    @Override
    public void onNoteClickGame(int position) {
//        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();
        gameList.get(position);
        if (gameList.get(position).getGameName().equals("Card Memory Game")) {
            startActivity(new Intent(getApplicationContext(), MainActivityK.class));
        } else if (gameList.get(position).getGameName().equals("Color Pattern Game")) {
            startActivity(new Intent(getApplicationContext(), ColorPatternGame.class));
        } else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
            if (launchIntent != null) {
                Log.d("Tagopenapp", "---------------------B--------------------------");
                startActivity(launchIntent);
            } else {
                Toast.makeText(Exercise.this, "There is no package", Toast.LENGTH_LONG).show();
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
    protected void onStart() {
        super.onStart();
//        displayConTrainingInstructions();

    }

    private void displayConTrainingInstructions() {
        Button ok;
        View c1, c2;

        dialogCon.setContentView(R.layout.concentration_training_popup);
        dialogCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ok = (Button) dialogCon.findViewById(R.id.clickCon);
//        c1=(View)dialogCon.findViewById(R.id.c1);
//        c2=(View)dialogCon.findViewById(R.id.c2);
//
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
                dialogCon.dismiss();
            }
        });

        dialogCon.show();

        SharedPreferences prefsConEx = getSharedPreferences("prefsConEx", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsConEx.edit();
        editor.putBoolean("firstStartConEx", false);
        editor.apply();


    }
}