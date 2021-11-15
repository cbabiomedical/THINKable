package com.example.thinkableproject.repositories.ui.notifications;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.DownloadMusicAdapter;
import com.example.thinkableproject.adapters.FavouriteMusicAdapter;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.repositories.DownloadMusic;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.example.thinkableproject.sample.FavouriteMusicClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    FirebaseUser mUser;

    private RecyclerView recyclerView;
    private ArrayList<DownloadMusicModelClass> downloadItemList = new ArrayList<>();
    private DownloadMusicAdapter musicAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification_down, container, false);
        mUser=FirebaseAuth.getInstance().getCurrentUser();


        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add item touch helper

        loadData();

        return root;
    }

    private void loadData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Downloads").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DownloadMusicModelClass post = dataSnapshot.getValue(DownloadMusicModelClass.class);
                    downloadItemList.add(post);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                musicAdapter = new DownloadMusicAdapter(getActivity(), downloadItemList);
                recyclerView.setAdapter(musicAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}