package com.example.thinkableproject;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.sample.GameModelClass;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout linearLayoutManager;
    ArrayList<GameModelClass> gameList;
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
        gameList.add(new GameModelClass(R.drawable.chess, "Chess","0","0"));
        gameList.add(new GameModelClass(R.drawable.images, "Puzzle","1","0"));
        gameList.add(new GameModelClass(R.drawable.sudoku, "Sudoku","2","0"));
        gameList.add(new GameModelClass(R.drawable.crossword, "CrossWord","3","0"));

    }
    private void initRecyclerView() {
        //Initializing liner layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GridAdapter(gameList,getApplicationContext());
        recyclerView.setAdapter(adapter);


    }

}