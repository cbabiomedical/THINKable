package com.example.thinkableproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.sample.GameModelClass;

import java.util.ArrayList;
import java.util.List;

public class ConcentrationExcercise extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    List<GameModelClass> gameList;
    GridAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_excercise);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);

        initData();
        //Calling initRecyclerView function
        initRecyclerView();

    }
    private void initData() {
        gameList = new ArrayList<>();
        //Adding user preferences to arraylist
        gameList.add(new GameModelClass(R.drawable.chess, "Chess",R.drawable.ic_favorite));
        gameList.add(new GameModelClass(R.drawable.images, "Puzzle",R.drawable.ic_favorite));
        gameList.add(new GameModelClass(R.drawable.sudoku, "Sudoku",R.drawable.ic_favorite));
        gameList.add(new GameModelClass(R.drawable.crossword, "CrossWord",R.drawable.ic_favorite));

    }
    private void initRecyclerView() {
        //Initializing liner layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new GridAdapter(gameList);
        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //Initializing adapter
//        adapter = new GridAdapter(gameList);
//        recyclerView.setAdapter(adapter);

    }


}