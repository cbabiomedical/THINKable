package com.example.thinkableproject.repositories.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.adapters.DownloadGameModelAdapter;
import com.example.thinkableproject.sample.DownloadGameModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    FirebaseUser mUser;

    private ArrayList<DownloadGameModelClass> downloadGames = new ArrayList<>();
    private DownloadGameModelAdapter downloadGameAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_down, container, false);


        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // add item touch helper

        loadData();

        return root;
    }

    private void loadData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DownloadGameModelClass post = dataSnapshot.getValue(DownloadGameModelClass.class);
                    downloadGames.add(post);
                    Log.d("Post", String.valueOf(post));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                downloadGameAdapter = new DownloadGameModelAdapter(getActivity(), downloadGames);
                recyclerView.setAdapter(downloadGameAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}