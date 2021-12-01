package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.thinkableproject.adapters.BineuralAdapter;
import com.example.thinkableproject.sample.BineuralModelClass;
import java.util.ArrayList;
import java.util.List;

public class BineuralAcivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    List<BineuralModelClass> bineralList;
    BineuralAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bineural_acivity);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);

        initData();
        //Calling initRecyclerView function
        initRecyclerView();


    }

    private void initData() {
        bineralList = new ArrayList<>();
        //Adding user preferences to arraylist
        bineralList.add(new BineuralModelClass(R.drawable.bineural, "Bineural 1", R.drawable.ic_favorite));
        bineralList.add(new BineuralModelClass(R.drawable.bineural1, "Bineural 2", R.drawable.ic_favorite));
        bineralList.add(new BineuralModelClass(R.drawable.bineural2, "Bineural 3", R.drawable.ic_favorite));
        bineralList.add(new BineuralModelClass(R.drawable.bineural3, "Bineural 4", R.drawable.ic_favorite));

    }

    private void initRecyclerView() {
        //Initializing liner layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BineuralAdapter(bineralList);
        recyclerView.setAdapter(adapter);
    }
}