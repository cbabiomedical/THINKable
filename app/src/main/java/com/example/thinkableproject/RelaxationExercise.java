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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.adapters.VideoAdapter;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.example.thinkableproject.sample.VideoModelClass;
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

import pl.droidsonroids.gif.GifImageView;

public class RelaxationExercise extends AppCompatActivity implements MusicAdapter.OnNoteListner, MeditationAdapter.OnNoteListner,VideoAdapter.OnNoteListner {
    ImageView concentrationBtn;
    RecyclerView musicRecyclerView, meditationRecyclerView, videoRecyclerview;
    MusicAdapter musicAdapter;
    MeditationAdapter meditationAdapter;
    VideoAdapter videoAdapter;
    TextView music, meditation, video;
    View c1, c2;
    ImageView relaxationInfo;
    GifImageView c1gif, c2gif;
    int color;
    FirebaseUser mUser;
    Dialog dialogRel;

    ArrayList<MusicModelClass> musicList;
    ArrayList<MeditationModelClass> meditationModelClassArrayList;
    ArrayList<VideoModelClass> videoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_exercise);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        meditationRecyclerView = findViewById(R.id.meditationRecyclerView);
        concentrationBtn = findViewById(R.id.concentration);
        music = findViewById(R.id.musicTitle);
        meditation = findViewById(R.id.meditationTitle);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        dialogRel = new Dialog(this);
        relaxationInfo = findViewById(R.id.relaxationInfo);
        videoRecyclerview = findViewById(R.id.videoRecyclerView);

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

        videoArrayList = new ArrayList<>();

        SharedPreferences prefsRelEx = getSharedPreferences("prefsRelEx", MODE_PRIVATE);
        boolean firstStartRelEx = prefsRelEx.getBoolean("firstStartRelEx", true);

        if (firstStartRelEx) {
            displayRelInstruction();
        }


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeditationExercise.class));
            }
        });
        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Exercise.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        relaxationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRelInstruction();
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
        meditationModelClassArrayList = new ArrayList<>();
        musicList = new ArrayList<>();

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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Meditation_Admin");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MeditationModelClass post = dataSnapshot.getValue(MeditationModelClass.class);
                    meditationModelClassArrayList.add(post);
                    Log.d("MeditationPost", String.valueOf(post));
                }
                meditationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        meditationAdapter = new MeditationAdapter(meditationModelClassArrayList, getApplicationContext(), this::onNoteClickMeditation);
        meditationRecyclerView.setAdapter(meditationAdapter);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Video_Admin");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VideoModelClass post = dataSnapshot.getValue(VideoModelClass.class);
                    videoArrayList.add(post);
                    Log.d("VideoPost", String.valueOf(post));
                }
                videoRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        videoAdapter = new VideoAdapter( videoArrayList,getApplicationContext(), this::onNoteClickVid);
        videoRecyclerview.setAdapter(videoAdapter);
    }

    @Override
    public void onNoteClickMeditation(int position) {
        meditationModelClassArrayList.get(position);
        String songName = meditationModelClassArrayList.get(position).getMeditateName();
        String url = meditationModelClassArrayList.get(position).getUrl();
        String image = meditationModelClassArrayList.get(position).getMeditateImage();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), PlayMeditation.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }

    public void mem2(View view) {
        Intent intentcd = new Intent(RelaxationExercise.this, MemoryExercise.class);

        startActivity(intentcd);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onNoteClick(int position) {
        musicList.get(position);
        String url = musicList.get(position).getSongTitle1();
        String name = musicList.get(position).getName();
        String image = musicList.get(position).getImageUrl();
        String id = musicList.get(position).getId();
        startActivity(new Intent(getApplicationContext(), MusicPlayer.class).putExtra("name", name).putExtra("url", url).putExtra("image", image).putExtra("id", id));

    }

    @Override
    protected void onStart() {
        super.onStart();

//        displayRelInstruction();
    }

    private void displayRelInstruction() {
        Button ok;
        View c1, c2;


        dialogRel.setContentView(R.layout.relaxation_training_popup);
        dialogRel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ok = (Button) dialogRel.findViewById(R.id.clickRel);
//        c1 = (View) dialogRel.findViewById(R.id.c1);
//        c2 = (View) dialogRel.findViewById(R.id.c2);
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
                dialogRel.dismiss();
            }
        });
        dialogRel.show();

        SharedPreferences prefsRelEx = getSharedPreferences("prefsRelEx", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsRelEx.edit();
        editor.putBoolean("firstStartRelEx", false);
        editor.apply();

    }

    @Override
    public void onNoteClickVid(int position) {
        videoArrayList.get(position);
        String url = videoArrayList.get(position).getVideoUrl();
        String name = videoArrayList.get(position).getVideoName();
        String image = videoArrayList.get(position).getVideoImage();
        String id = videoArrayList.get(position).getVideoId();
        startActivity(new Intent(getApplicationContext(), PlayVideo.class).putExtra("name", name).putExtra("url", url).putExtra("image", image).putExtra("id", id));

    }
}