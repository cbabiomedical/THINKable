package com.example.thinkableproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.FavouriteMusicClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteMusicAdapter extends RecyclerView.Adapter<FavouriteMusicAdapter.ViewHolder> {
    private Context context;
    private List<FavouriteMusicClass> favItemList;
    private FavMusicDB favDB;

    //Constructor
    public FavouriteMusicAdapter(Context context, List<FavouriteMusicClass> favItemList) {
        this.context = context;
        this.favItemList = favItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Setting view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_music,
                parent, false);
        favDB = new FavMusicDB(context);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //storing data values into variables in recyclerview
        holder.favTextView.setText(favItemList.get(position).getItem_title());
        Log.d("FavouriteTitle", favItemList.get(position).getItem_image());
        Picasso.get().load(favItemList.get(position).getItem_image()).into(holder.favImageView);

    }

    @Override
    public int getItemCount() {
        // returning size of arraylist
        return favItemList.size();
    }

    //ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView favTextView;
        Button favBtn;
        ImageView favImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favTextView = itemView.findViewById(R.id.favTextView);
            favBtn = itemView.findViewById(R.id.favBtn2);
            favImageView = itemView.findViewById(R.id.favImageView);
            // onClick Listener to favourites button

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavouriteMusicClass favItem = favItemList.get(position);
                    //Removing item from favourites
                    favDB.remove_fav_mus(favItem.getKey_id());
                    removeItem(position);

                }
            });
        }
    }

    //remove Item method
    private void removeItem(int position) {
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favItemList.size());
    }
}
