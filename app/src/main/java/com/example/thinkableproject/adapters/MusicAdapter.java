package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.MusicModelClass;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<MusicModelClass> musicList;
    private Context context;
    private OnNoteListner onNoteListner;


    public MusicAdapter( ArrayList<MusicModelClass> musicList,Context context,OnNoteListner onNoteListner) {
        this.musicList = musicList;
        this.context = context;
        this.onNoteListner=onNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_music,
                parent, false);
        return new ViewHolder(view,onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(musicList.get(position).getImageView());
        holder.title.setText(musicList.get(position).getSongName());

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView title;
        AppCompatButton favBtn;
        OnNoteListner onNoteListner;

        public ViewHolder(@NonNull View itemView,OnNoteListner onNoteListner) {
            super(itemView);
            imageView=itemView.findViewById(R.id.gridImage);
            title=itemView.findViewById(R.id.item_name);
            favBtn=itemView.findViewById(R.id.favouritesIcon2);
            this.onNoteListner=onNoteListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClick(getAdapterPosition());

        }
    }

    public interface OnNoteListner{
        void onNoteClick(int position);
    }
}
