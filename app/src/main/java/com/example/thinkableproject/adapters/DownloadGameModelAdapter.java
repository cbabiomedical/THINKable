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
import com.example.thinkableproject.sample.DownloadGameModelClass;

import java.util.ArrayList;

public class DownloadGameModelAdapter extends RecyclerView.Adapter<DownloadGameModelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DownloadGameModelClass> downloadGameList;

    public DownloadGameModelAdapter(Context context, ArrayList<DownloadGameModelClass> gameModelClassArrayList) {
        this.context = context;
        this.downloadGameList = gameModelClassArrayList;
    }

    @NonNull
    @Override
    public DownloadGameModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_download_games, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadGameModelAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(downloadGameList.get(position).getImageView());
        holder.gameTitle.setText(downloadGameList.get(position).getGameTitle());
    }

    @Override
    public int getItemCount() {
        return downloadGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView gameTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.downloadImageGame);
            gameTitle = itemView.findViewById(R.id.downloadTitleGame);


        }
    }
}
