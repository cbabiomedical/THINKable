package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thinkableproject.adapters.BineuralAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.adapters.VideoAdapter;
import com.example.thinkableproject.sample.BineuralModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.example.thinkableproject.sample.VideoModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VideoInterventionActivity extends AppCompatActivity implements VideoAdapter.OnNoteListner {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<VideoModelClass> videoModelClassList;
    VideoAdapter adapter;
    Dialog videoDialog;
    ImageView information;
    Animation scaleUp, scaleDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intervention);
        recyclerView = findViewById(R.id.recyclerView);
        information = findViewById(R.id.videoInfo);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        videoDialog = new Dialog(this);

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoPopUp();
            }
        });


        SharedPreferences prefsVid = getSharedPreferences("prefsVid", MODE_PRIVATE);
        boolean firstStartVid = prefsVid.getBoolean("firstStartVid", true);

        if (firstStartVid) {
            openVideoPopUp();
        }


        initData();
        //Calling initRecyclerView function
        initRecyclerView();


    }

    private void openVideoPopUp() {

        Button ok;

        videoDialog.setContentView(R.layout.videopopup);
        videoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) videoDialog.findViewById(R.id.click);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog.dismiss();
            }
        });



        videoDialog.show();
        SharedPreferences prefsVid = getSharedPreferences("prefsVid", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsVid.edit();
        editor.putBoolean("firstStartVid", false);
        editor.apply();
    }

    private void initData() {
        videoModelClassList = new ArrayList<>();
        //Adding user preferences to arraylist
//        videoModelClassList.add(new VideoModelClass("1","Calm Meditation",R.drawable.download,R.raw.cardgame));
//        videoModelClassList.add(new VideoModelClass("2","Pathway Meditation",R.drawable.download2,R.raw.cardgame));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos_Admin").child("Videos_Relaxation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDatasnapshot : snapshot.getChildren()) {
                    VideoModelClass post = postDatasnapshot.getValue(VideoModelClass.class);
                    Log.d("Post", String.valueOf(post));
                    videoModelClassList.add(post);

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                Log.d("Video List", String.valueOf(videoModelClassList));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(videoModelClassList, getApplicationContext(), this::onNoteClickVid);
        recyclerView.setAdapter(adapter);


    }

    private void initRecyclerView() {
        //Initializing liner layout manager

    }

    @Override
    public void onNoteClickVid(int position) {
//        Toast.makeText(VideoInterventionActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
        videoModelClassList.get(position);
        String videoName = videoModelClassList.get(position).getVideoName();
        Log.d("Name", videoName);
        String url = videoModelClassList.get(position).getVideoUrl();
        Log.d("SongURL", String.valueOf(url));
        String image = videoModelClassList.get(position).getVideoImage();
        Log.d("Url", String.valueOf(url));
        startActivity(new Intent(getApplicationContext(), PlayVideo.class).putExtra("name", videoName).putExtra("image", image).putExtra("url", url));
    }
}