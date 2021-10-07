package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.databinding.ActivityConcentrationExcerciseBinding;

public class ConcentrationExcercise extends AppCompatActivity {
    ActivityConcentrationExcerciseBinding binding;
    ImageView favouriteBtn;

    boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConcentrationExcerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //  favouriteBtn = findViewById(R.id.favouritesIcon);

        String[] gameName = {"Puzzles", "Chess", "Sudoku", "CrossWord"};
        int[] gameImages = {
                R.drawable.images, R.drawable.chess, R.drawable.sudoku, R.drawable.crossword
        };


        GridAdapter gridAdapter = new GridAdapter(ConcentrationExcercise.this, gameName, gameImages, isFavourite);
        binding.gridView.setAdapter(gridAdapter);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "You clicked on " + gameName[position], Toast.LENGTH_SHORT).show();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.grid_item, null);
        favouriteBtn = dialogLayout.findViewById(R.id.favouritesIcon);
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isFavourite) {
                    favouriteBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    isFavourite = false;
                    saveStae(isFavourite);

                } else {
                    favouriteBtn.setBackgroundResource(R.drawable.ic_favorite);
                    isFavourite = true;
                    saveStae(isFavourite);

                }

            }
        });

    }

    private void saveStae(boolean isFavourite) {
        SharedPreferences aSharedPreferenes = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferenesEdit = aSharedPreferenes
                .edit();
        aSharedPreferenesEdit.putBoolean("State", isFavourite);
        aSharedPreferenesEdit.apply();
    }

    private boolean readStae() {
        SharedPreferences aSharedPreferenes = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferenes.getBoolean("State", true);
    }
}