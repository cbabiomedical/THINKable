package com.example.thinkableproject.repositories.ui.dashboard;

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
import com.example.thinkableproject.adapters.DownloadMeditationAdapter;
import com.example.thinkableproject.adapters.DownloadMusicAdapter;
import com.example.thinkableproject.adapters.FavouriteMeditationAdapter;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.sample.DownloadMeditationClass;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.example.thinkableproject.sample.FavouriteModelMeditationClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    FirebaseUser mUser;

    private ArrayList<DownloadMeditationClass> downloadMeditationClassArrayList = new ArrayList<>();
    private DownloadMeditationAdapter downloadMeditationAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_down, container, false);


        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUser= FirebaseAuth.getInstance().getCurrentUser();

        // add item touch helper

        loadData();

        return root;
    }

    private void loadData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DownloadsMeditation").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DownloadMeditationClass post = dataSnapshot.getValue(DownloadMeditationClass.class);
                    downloadMeditationClassArrayList.add(post);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                downloadMeditationAdapter= new DownloadMeditationAdapter(getActivity(), downloadMeditationClassArrayList);
                recyclerView.setAdapter(downloadMeditationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}