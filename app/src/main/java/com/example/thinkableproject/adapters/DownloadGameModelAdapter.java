package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.DownloadingActivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.DownloadGameModelClass;
import com.example.thinkableproject.sample.DownloadMeditationClass;

import java.util.ArrayList;

public class DownloadGameModelAdapter extends RecyclerView.Adapter<DownloadGameModelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DownloadGameModelClass> downloadGameModelClasses;

    public DownloadGameModelAdapter(Context context, ArrayList<DownloadGameModelClass> downloadMusic) {
        this.context = context;
        this.downloadGameModelClasses = downloadMusic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_download_games, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageView.setImageResource(downloadGameModelClasses.get(position).getItem_image());
        holder.songTitle.setText(downloadGameModelClasses.get(position).getItem_title());

    }

    @Override
    public int getItemCount() {
        return downloadGameModelClasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.downloadImage);
            songTitle = itemView.findViewById(R.id.downloadTitle);

        }
    }

}
