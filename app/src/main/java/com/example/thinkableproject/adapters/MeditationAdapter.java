package com.example.thinkableproject.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMeditationDB;

import com.example.thinkableproject.sample.MeditationModelClass;

import java.util.ArrayList;

public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {
    private ArrayList<MeditationModelClass> coffeeItems;
    private Context context;
   OnNoteListner onNoteListner;
    private FavMeditationDB favDB;

    public MeditationAdapter(ArrayList<MeditationModelClass> coffeeItems, Context context, OnNoteListner onNoteListner) {
        this.coffeeItems = coffeeItems;
        this.context = context;
        this.onNoteListner=onNoteListner;
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
        return new ViewHolder(view,onNoteListner);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MeditationModelClass coffeeItem = coffeeItems.get(position);

        readCursorDataMed(coffeeItem, holder);
        holder.imageView.setImageResource(coffeeItem.getImageView());
        holder.titleTextView.setText(coffeeItem.getMeditationName());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(context,coffeeItems.get(position).getMeditationName(),".mp3", Environment.DIRECTORY_DOWNLOADS,coffeeItems.get(position).getUrl());
            }
        });
    }


    @Override
    public int getItemCount() {
        return coffeeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView titleTextView, likeCountTextView;
        Button favBtn;
        OnNoteListner onNoteListner;
        AppCompatButton download;

        public ViewHolder(@NonNull View itemView,OnNoteListner onNoteListner) {
            super(itemView);

            imageView = itemView.findViewById(R.id.gridImage);
            titleTextView = itemView.findViewById(R.id.item_name);
            favBtn = itemView.findViewById(R.id.favouritesIcon2);
            download=itemView.findViewById(R.id.download);
            this.onNoteListner=onNoteListner;
            itemView.setOnClickListener(this);



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

        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClick(getAdapterPosition());
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
    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    public interface OnNoteListner{
        void onNoteClick(int position);
    }

    // like click
}
