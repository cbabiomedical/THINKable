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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GameModelClass> gameList;
    private Context context;
    ArrayList<FavouriteModelClass> faveList;
    HashMap<String, Object> fav = new HashMap<>();


    public FavouriteAdapter(ArrayList<FavouriteModelClass> faveList) {
        this.faveList = faveList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grid_fav_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder) viewHolder).mName.setText(faveList.get(position).getFavName());
        ((ViewHolder) viewHolder).isFav.setImageResource(faveList.get(position).getIsFave());
//
        ((ViewHolder) viewHolder).mImage.setImageResource(faveList.get(position).getImageView());
    }

    @Override
    public int getItemCount() {
        return faveList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mName;
        ImageView isFav;
        boolean isFavourite = false;
        DatabaseReference favouriteRef;
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        Boolean favChecker=false;
        FavouriteModelClass favouriteModelClass;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.gridImage);
            mName = itemView.findViewById(R.id.item_name);
            isFav = itemView.findViewById(R.id.favouritesIcon);

            favouriteModelClass=new FavouriteModelClass();
            favouriteRef=database.getReference("favourites");



        }

    }

}
