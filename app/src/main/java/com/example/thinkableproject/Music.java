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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

public class Music extends AppCompatActivity implements MusicAdapter.OnNoteListner {
    RecyclerView recyclerView;
    LinearLayout linearLayoutManager;
    ArrayList<MusicModelClass> musicList;
   MusicAdapter adapter;
   FirebaseUser mUser;
   String songName;
    String id;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.exercise);
        recyclerView = findViewById(R.id.recycler_view);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        musicList=new ArrayList<>();
//        musicList.add(new MusicModelClass(R.drawable.music1,"Chilled Acoustic","1","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/chilled-acoustic-indie-folk-instrumental-background-music-for-videos-5720.mp3?alt=media&token=c61afc5b-1833-47a0-af5c-645872eae852"));
//        musicList.add(new MusicModelClass(R.drawable.music1,"Melody Of Nature","2","https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c"));
//        HashMap<String,Object> songs=new HashMap<>();
//        songs.put("songList",musicList);
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Music");
//        reference.setValue(songs);



        initData();


    }

    private void initData() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Music").child("songList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDatasnapshot:snapshot.getChildren()){
                    MusicModelClass post=postDatasnapshot.getValue(MusicModelClass.class);
                    Log.d("Post", String.valueOf(post));
                    musicList.add(post);
                }
                Log.d("List", String.valueOf(musicList));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MusicAdapter(musicList,getApplicationContext(),this::onNoteClick);
        recyclerView.setAdapter(adapter);
//



    }

    @Override
    public void onNoteClick(int position) {
        musicList.get(position);
        String songName=musicList.get(position).getSongName();
        String url=musicList.get(position).getUrl();
        int image=musicList.get(position).getImageView();
        Log.d("Url",url);
        startActivity(new Intent(getApplicationContext(),MusicPlayer.class).putExtra("url",url).putExtra("name",songName).putExtra("image",image).putExtra("pos",position));
    }
}