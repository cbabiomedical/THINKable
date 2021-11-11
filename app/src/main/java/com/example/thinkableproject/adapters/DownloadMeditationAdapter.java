package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.DownloadMeditationClass;
import com.example.thinkableproject.sample.DownloadMusicModelClass;

import java.util.ArrayList;

public class DownloadMeditationAdapter extends RecyclerView.Adapter<DownloadMeditationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DownloadMeditationClass> downloadMusic;

    public DownloadMeditationAdapter(Context context, ArrayList<DownloadMeditationClass> downloadMusic) {
        this.context = context;
        this.downloadMusic = downloadMusic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_download_meditation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(downloadMusic.get(position).getItem_image());
        holder.songTitle.setText(downloadMusic.get(position).getItem_title());

    }

    @Override
    public int getItemCount() {
        return downloadMusic.size();
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
