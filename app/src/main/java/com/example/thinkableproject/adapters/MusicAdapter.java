package com.example.thinkableproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.repositories.FavMusicDB;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.example.thinkableproject.sample.MusicModelClass;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<MusicModelClass> musicList;
    private Context context;
    private OnNoteListner onNoteListner;
    private FavMusicDB favDB;


    public MusicAdapter( ArrayList<MusicModelClass> musicList,Context context,OnNoteListner onNoteListner) {
        this.musicList = musicList;
        this.context = context;
        this.onNoteListner=onNoteListner;
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

        return new ViewHolder(view,onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MusicModelClass coffeeItem = musicList.get(position);

        readCursorDataMed(coffeeItem, holder);
        holder.imageView.setImageResource(musicList.get(position).getImageView());
        holder.title.setText(musicList.get(position).getSongName());
        String[] time= new String[]{"1 min","1.5 min","2 min","2.5 min","3 min"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, time);

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView title;
        AppCompatButton favBtn;
        ImageView timing;
        OnNoteListner onNoteListner;
        TextView time;
        AppCompatButton download;

        public ViewHolder(@NonNull View itemView,OnNoteListner onNoteListner) {
            super(itemView);
            imageView=itemView.findViewById(R.id.gridImage);
            title=itemView.findViewById(R.id.item_name);
            favBtn=itemView.findViewById(R.id.favouritesIcon3);
            timing=itemView.findViewById(R.id.occupation);
            time=itemView.findViewById(R.id.time);
            download=itemView.findViewById(R.id.download);
            this.onNoteListner=onNoteListner;
            itemView.setOnClickListener(this);
            timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu=new PopupMenu(context,v);
                    popupMenu.inflate(R.menu.pop_up_menu_time);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.onemin:
                                    time.setText("1 min");

                                    return true;
                                case R.id.onehalfmin:
                                    time.setText("1.5 min");
                                    return true;
                                case R.id.twomin:
                                    time.setText("2 min");
                                    return true;
                                case R.id.twohalfmin:
                                    time.setText("2.5 min");
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

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
    private void popUpMenu(View view){

    }

    private void timepopUpMenu(View view){
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


    public interface OnNoteListner{
        void onNoteClick(int position);
    }
}
