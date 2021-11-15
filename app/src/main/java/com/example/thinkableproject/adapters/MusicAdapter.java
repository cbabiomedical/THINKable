package com.example.thinkableproject.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.DownloadMusic;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<MusicModelClass> musicList;
    private Context context;
    private OnNoteListner onNoteListner;
    private FavMusicDB favDB;
    public static ViewHolder viewHolder;

    public static ViewHolder getViewHolder() {
        Log.d("Time", String.valueOf(viewHolder.timeOfMusic));
        return viewHolder;

    }

    public static void setViewHolder(ViewHolder viewHolder) {
        MusicAdapter.viewHolder = viewHolder;

    }

    ArrayList<DownloadMusicModelClass> downoadSong = new ArrayList<>();
    FirebaseUser mUser;

    public MusicAdapter() {
    }

    public MusicAdapter(ArrayList<MusicModelClass> musicList, Context context, OnNoteListner onNoteListner) {
        this.musicList = musicList;
        this.context = context;
        this.onNoteListner = onNoteListner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavMusicDB(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_music,
                parent, false);
        View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);


        return new ViewHolder(view, onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MusicModelClass coffeeItem = musicList.get(position);

        readCursorDataMed(coffeeItem, holder);
        holder.imageView.setImageResource(musicList.get(position).getImageView());
        holder.title.setText(musicList.get(position).getSongName());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checking if file is already downloaded

                DownloadMusicModelClass musicModelClass = new DownloadMusicModelClass(musicList.get(position).getSongName(), musicList.get(position).getImageView());
                downloadFile(context, musicList.get(position).getSongName(), ".mp3", DIRECTORY_DOWNLOADS, musicList.get(position).getUrl());
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                downoadSong.add(musicModelClass);
                Log.d("Downloaded Music", String.valueOf(musicModelClass));
                Log.d("Download List", String.valueOf(downoadSong));

                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Downloads").child(mUser.getUid());
                database.setValue(downoadSong);

                holder.download.setEnabled(false);

            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView title;
        AppCompatButton favBtn;
        ImageView timing;
        OnNoteListner onNoteListner;
        TextView time;
        AppCompatButton download;
        int timeOfMusic = 60000;


        public int getTimeOfMusic() {
            return timeOfMusic;
        }

        public void setTimeOfMusic(int timeOfMusic) {
            this.timeOfMusic = timeOfMusic;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridImage);
            title = itemView.findViewById(R.id.item_name);
            favBtn = itemView.findViewById(R.id.favouritesIcon3);
            time = itemView.findViewById(R.id.time);
            download = itemView.findViewById(R.id.download);
            this.onNoteListner = onNoteListner;
            itemView.setOnClickListener(this);


            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    MusicModelClass gameModelClass = musicList.get(position);
                    if (gameModelClass.getIsFav().equals("0")) {
                        gameModelClass.setIsFav("1");
                        favDB.insertIntoTheDatabaseMusic(gameModelClass.getSongName(), gameModelClass.getImageView(), gameModelClass.getId(), gameModelClass.getIsFav());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    } else {
                        gameModelClass.setIsFav("0");
                        favDB.remove_fav_mus(gameModelClass.getId());
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
//    public class  ViewHolder1 extends RecyclerView.ViewHolder{
//
//        ImageView imageViewEx;
//        TextView titleEx;
//        AppCompatButton favBtnEx;
//        AppCompatButton downloadEx;
//        public ViewHolder1(@NonNull View itemView) {
//            super(itemView);
//            imageViewEx=itemView.findViewById(R.id.gridImageExercise);
//            titleEx=itemView.findViewById(R.id.item_name_exercise);
//            favBtnEx=itemView.findViewById(R.id.favouritesIconExercise);
//            downloadEx=itemView.findViewById(R.id.downloadExercise);
//        }
//    }


    private void timepopUpMenu(View view) {
//        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
//        popupMenu.inflate(R.menu.pop_up_menu_time);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.show();
    }

    private void createTableOnFirstStart() {
        favDB.insertEmptyMusic();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorDataMed(MusicModelClass coffeeItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data_mus(coffeeItem.getId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavMeditationDB.FAVORITE_STATUSMED));
                coffeeItem.setIsFav(item_fav_status);

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


    public interface OnNoteListner {
        void onNoteClick(int position);
    }


}
