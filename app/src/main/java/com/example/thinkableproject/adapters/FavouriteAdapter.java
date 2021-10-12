package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.FavouriteActivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.FavouriteModelClass;
import com.example.thinkableproject.sample.GameModelClass;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    public FavouriteAdapter(Context context, List<FavouriteModelClass> favList) {
        this.context = context;
        this.favList = favList;
    }

    private List<FavouriteModelClass> favList;


    public FavouriteAdapter(List<FavouriteModelClass> favList) {
        this.favList = favList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.grid_fav_item, viewGroup, false);
     ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).favName.setText(favList.get(i).getFavoriteName());
        ((ViewHolder) viewHolder).favImage.setImageResource(favList.get(i).getImageView());
//
        ((ViewHolder) viewHolder).isFav.setImageResource(favList.get(i).getIsFavourite());
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView favImage;
        TextView  favName;
        ImageView isFav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favImage = itemView.findViewById(R.id.gridImage);
            favName = itemView.findViewById(R.id.item_name);
            isFav = itemView.findViewById(R.id.favouritesIcon);

        }
    }
}
