package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Music extends AppCompatActivity implements MusicAdapter.OnNoteListner {
    RecyclerView recyclerView;
    MusicAdapter.ViewHolder musicAdapter;
    LinearLayout linearLayoutManager;
    ArrayList<MusicModelClass> musicList;
    MusicAdapter adapter;
    String selected_time;
    int time;
    StorageReference reference;

    String songName;
    String id;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

//        Log.d("Time of Music",String.valueOf(MusicAdapter.getTimeOfmusic()));
        recyclerView = findViewById(R.id.recycler_view);
        Spinner dropdown_time = (Spinner) findViewById(R.id.spinner2);
        String[] items = new String[]{"1 min", "1.5 min", "2 min", "2.5 min", "3 min"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown_time.setAdapter(adapter1);

//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
//                new IntentFilter("time"));
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
//        MusicAdapter musicAdapter=new MusicAdapter();
//        final int timeOfMus= MusicAdapter.getViewHolder().getTimeOfMusic();
//        Log.d("Time", String.valueOf(timeOfMus));


        musicList = new ArrayList<>();
//        musicList.add(new MusicModelClass(R.drawable.music1, "Chilled Acoustic", "1", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/chilled-acoustic-indie-folk-instrumental-background-music-for-videos-5720.mp3?alt=media&token=c61afc5b-1833-47a0-af5c-645872eae852"));
//        musicList.add(new MusicModelClass(R.drawable.music1, "Melody Of Nature", "2", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c"));
        HashMap<String, Object> songs = new HashMap<>();
        songs.put("songList", musicList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Music");
        reference.setValue(songs);


        initData();


    }

//    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String ItemName = intent.getStringExtra("pass");
//            Log.d("time", ItemName);
//            Toast.makeText(Music.this, ItemName + " ", Toast.LENGTH_SHORT).show();
//        }
//    };

    private void initData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs_Admin").child("songList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDatasnapshot : snapshot.getChildren()) {
                    MusicModelClass post = postDatasnapshot.getValue(MusicModelClass.class);
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
        adapter = new MusicAdapter(musicList, getApplicationContext(), this::onNoteClick);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onNoteClick(int position) {

        musicList.get(position);
        String songName = musicList.get(position).getSongTitle1();
        String url = musicList.get(position).getName();
        String image = musicList.get(position).getImageUrl();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), MusicPlayer.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image).putExtra("time", time));
    }
}