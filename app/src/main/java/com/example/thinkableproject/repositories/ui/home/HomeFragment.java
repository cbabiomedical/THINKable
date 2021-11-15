package com.example.thinkableproject.repositories.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.thinkableproject.adapters.DownloadMeditationAdapter;
import com.example.thinkableproject.adapters.FavouriteAdapter;
import com.example.thinkableproject.repositories.FavDB;
import com.example.thinkableproject.sample.DownloadGameModelClass;
import com.example.thinkableproject.sample.DownloadMeditationClass;
import com.example.thinkableproject.sample.FavouriteModelClass;
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
    private ArrayList<DownloadGameModelClass> downloadGameList = new ArrayList<>();
    private DownloadGameModelAdapter downloadGameModelAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_down, container, false);

        recyclerView = root.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        // add item touch helper

        loadData();

        return root;
    }
    private void loadData() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DownloadGameModelClass post = dataSnapshot.getValue(DownloadGameModelClass.class);
                    Log.d("Post", String.valueOf(post));
                    downloadGameList.add(post);
                    Log.d("GameList", String.valueOf(downloadGameList));

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                downloadGameModelAdapter= new DownloadGameModelAdapter(getActivity(), downloadGameList);
                recyclerView.setAdapter(downloadGameModelAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}