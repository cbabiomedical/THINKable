package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.adapters.MeditationAdapter;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;

import java.util.ArrayList;
import java.util.List;

public class MeditationExercise extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    List<MeditationModelClass> meditationList;
    MeditationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_exercise);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);

        initData();
        //Calling initRecyclerView function
        initRecyclerView();
    }
    private void initData() {
        meditationList = new ArrayList<>();
        //Adding user preferences to arraylist
        meditationList.add(new MeditationModelClass(R.drawable.mindful, "Mindfulness",R.drawable.ic_favorite));
        meditationList.add(new MeditationModelClass(R.drawable.maxresdefault, "Body Scan",R.drawable.ic_favorite));
        meditationList.add(new MeditationModelClass(R.drawable.love_kind, "Loving",R.drawable.ic_favorite));
        meditationList.add(new MeditationModelClass(R.drawable.transidental, "transcendental ",R.drawable.ic_favorite));

    }
    private void initRecyclerView() {
        //Initializing liner layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MeditationAdapter(meditationList);
        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //Initializing adapter
//        adapter = new GridAdapter(gameList);
//        recyclerView.setAdapter(adapter);

    }
}