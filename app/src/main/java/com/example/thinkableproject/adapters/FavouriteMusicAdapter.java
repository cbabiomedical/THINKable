package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.FavouriteModelMeditationClass;
import com.example.thinkableproject.sample.FavouriteMusicClass;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMusicAdapter extends RecyclerView.Adapter<FavouriteMusicAdapter.ViewHolder> {
    private Context context;
    private List<FavouriteMusicClass> favItemList;
    private FavMusicDB favDB;

    public FavouriteMusicAdapter(Context context, List<FavouriteMusicClass> favItemList) {
        this.context = context;
        this.favItemList = favItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_meditation,
                parent, false);
        favDB = new FavMusicDB(context);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.favTextView.setText(favItemList.get(position).getItem_title());
        holder.favImageView.setImageResource(favItemList.get(position).getItem_image());

    }

    @Override
    public int getItemCount() {
        return favItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView favTextView;
        Button favBtn;
        ImageView favImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favTextView = itemView.findViewById(R.id.favTextView);
            favBtn = itemView.findViewById(R.id.favBtn2);
            favImageView = itemView.findViewById(R.id.favImageView);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavouriteMusicClass favItem = favItemList.get(position);
                    favDB.remove_fav_mus(favItem.getKey_id());
                    removeItem(position);

                }
            });
        }
    }

    private void removeItem(int position) {
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favItemList.size());
    }
}
