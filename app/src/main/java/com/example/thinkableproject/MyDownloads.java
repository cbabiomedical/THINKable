package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thinkableproject.adapters.DownloadMusicAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.repositories.DownloadMusic;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDownloads extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DownloadMusicModelClass> downloadMusic;
    DownloadMusicAdapter adapter;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);
        recyclerView=findViewById(R.id.recycler_view);

        downloadMusic=new ArrayList<>();
        mUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Downloads").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DownloadMusicModelClass post=dataSnapshot.getValue(DownloadMusicModelClass.class);
                    downloadMusic.add(post);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new DownloadMusicAdapter( getApplicationContext(),downloadMusic);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}