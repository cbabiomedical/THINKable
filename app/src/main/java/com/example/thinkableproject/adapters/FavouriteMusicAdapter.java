package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.FavouriteMusicClass;

import java.util.ArrayList;

public class FavouriteMusicAdapter extends RecyclerView.Adapter<FavouriteMusicAdapter.ViewHolder> {
    private ArrayList<FavouriteMusicClass> favMusic;
    private Context context;
    private FavMusicDB favMusicDB;

    public FavouriteMusicAdapter(ArrayList<FavouriteMusicClass> favMusic, Context context) {
        this.favMusic = favMusic;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
