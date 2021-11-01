package com.example.thinkableproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.sample.FavouriteModelClass;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {
    private ArrayList<MeditationModelClass> coffeeItems;
    private Context context;
    private FavMeditationDB favDB;

    public MeditationAdapter(ArrayList<MeditationModelClass> coffeeItems, Context context) {
        this.coffeeItems = coffeeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavMeditationDB(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_meditation,
                parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MeditationModelClass coffeeItem = coffeeItems.get(position);

        readCursorDataMed(coffeeItem, holder);
        holder.imageView.setImageResource(coffeeItem.getImageView());
        holder.titleTextView.setText(coffeeItem.getMeditationName());
    }


    @Override
    public int getItemCount() {
        return coffeeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView, likeCountTextView;
        Button favBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.gridImage);
            titleTextView = itemView.findViewById(R.id.item_name);
            favBtn = itemView.findViewById(R.id.favouritesIcon2);


            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    MeditationModelClass gameModelClass = coffeeItems.get(position);
                    if (gameModelClass.getFav().equals("0")) {
                        gameModelClass.setFav("1");
                        favDB.insertIntoTheDatabaseMed(gameModelClass.getMeditationName(), gameModelClass.getImageView(), gameModelClass.getId(), gameModelClass.getFav());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    } else {
                        gameModelClass.setFav("0");
                        favDB.remove_fav_med(gameModelClass.getId());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite);
                    }
                }

            });
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmptyMed();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorDataMed(MeditationModelClass coffeeItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data_med(coffeeItem.getId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavMeditationDB.FAVORITE_STATUSMED));
                coffeeItem.setFav(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

    // like click
}
