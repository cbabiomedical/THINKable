package com.example.thinkableproject.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.example.thinkableproject.sample.VideoModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private ArrayList<VideoModelClass> videoModelClassArrayList;
    private Context context;
    private OnNoteListner onNoteListner;

    public static MusicAdapter.ViewHolder viewHolder;
    HashMap<String, Object> music = new HashMap<>();


    ArrayList<DownloadMusicModelClass> downoadSong = new ArrayList<>();
    FirebaseUser mUser;


    // Constructor
    public VideoAdapter(ArrayList<VideoModelClass> videoModelClassArrayList, Context context, OnNoteListner onNoteListner) {
        this.videoModelClassArrayList = videoModelClassArrayList;
        this.context = context;
        this.onNoteListner = onNoteListner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //Setting View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_video_list,
                parent, false);


        return new ViewHolder(view, onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Setting values to variables in arraylist
        final VideoModelClass coffeeItem = videoModelClassArrayList.get(position);
        //Reading all data
//        readCursorDataMed(coffeeItem, holder);
        Picasso.get().load(videoModelClassArrayList.get(position).getVideoImage()).into(holder.imageView);
        holder.title.setText(videoModelClassArrayList.get(position).getVideoName());
        //OnClick Listener for download buttonn in music
//        holder.download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Checking if file is already downloaded
//                DownloadMusicModelClass musicModelClass = new DownloadMusicModelClass(musicList.get(position).getName(), musicList.get(position).getImageUrl());
//                //Calling download function
//                Log.d("ImageUrl", musicList.get(position).getImageUrl());
//                downloadFile(context, musicList.get(position).getName(), ".mp3", DIRECTORY_DOWNLOADS, musicList.get(position).getSongTitle1());
//                mUser = FirebaseAuth.getInstance().getCurrentUser();
//                downoadSong.add(musicModelClass);
//                music.put(downoadSong.get(position).getItem_title(), musicModelClass);
//                Log.d("Downloaded Music", String.valueOf(musicModelClass));
//                Log.d("Download List", String.valueOf(downoadSong));
//
//                holder.download.setEnabled(false);
//                //Saving downloaded music data to firebase
//                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Downloads").child(mUser.getUid());
//                database.setValue(music);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        //returning size of arraylist
        return videoModelClassArrayList.size();
    }

    //ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView title;
        //        AppCompatButton favBtn;
        OnNoteListner onNoteListner;
//        TextView time;
//        AppCompatButton download;
//        int timeOfMusic = 60000;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridImage);
            title = itemView.findViewById(R.id.item_name);
//            favBtn = itemView.findViewById(R.id.favouritesIcon3);
//            time = itemView.findViewById(R.id.time);
//            download = itemView.findViewById(R.id.download);
            this.onNoteListner = onNoteListner;
            itemView.setOnClickListener(this);

//
//            favBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    MusicModelClass gameModelClass = musicList.get(position);
//                    if (gameModelClass.getIsFav().equals("0")) {
//                        gameModelClass.setIsFav("1");
//                        favDB.insertIntoTheDatabaseMusic(gameModelClass.getName(), gameModelClass.getImageUrl(), gameModelClass.getId(), gameModelClass.getIsFav());
//                        favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
//                    } else {
//                        gameModelClass.setIsFav("0");
//                        favDB.remove_fav_mus(gameModelClass.getId());
//                        favBtn.setBackgroundResource(R.drawable.ic_favorite);
//                    }
//                }
//
//            });
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

//    private void createTableOnFirstStart() {
//        favDB.insertEmptyMusic();
//
//        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("firstStart", false);
//        editor.apply();
//    }

//    private void readCursorDataMed(MusicModelClass coffeeItem, MusicAdapter.ViewHolder viewHolder) {
//        Cursor cursor = favDB.read_all_data_mus(coffeeItem.getId());
//        SQLiteDatabase db = favDB.getReadableDatabase();
//        try {
//            while (cursor.moveToNext()) {
//                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavMeditationDB.FAVORITE_STATUSMED));
//                coffeeItem.setIsFav(item_fav_status);
//
//                //check fav status
//                if (item_fav_status != null && item_fav_status.equals("1")) {
//                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
//                } else if (item_fav_status != null && item_fav_status.equals("0")) {
//                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite);
//                }
//            }
//        } finally {
//            if (cursor != null && cursor.isClosed())
//                cursor.close();
//            db.close();
//        }
//
//    }

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
