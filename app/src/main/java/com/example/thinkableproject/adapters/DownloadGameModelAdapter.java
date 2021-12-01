package com.example.thinkableproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.DownloadGameModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DownloadGameModelAdapter extends RecyclerView.Adapter<DownloadGameModelAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DownloadGameModelClass> downloadGames;
    FirebaseUser mUser;

    //constructor
    public DownloadGameModelAdapter(Context context, ArrayList<DownloadGameModelClass> downloadMusic) {
        this.context = context;
        this.downloadGames = downloadMusic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_download_games, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //getting current user
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Picasso.get().load(downloadGames.get(position).getGameImage()).into(holder.imageView);
        holder.songTitle.setText(downloadGames.get(position).getGameName());
        //delete function of MyDownloads Games
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DownloadGameModelClass post = dataSnapshot.getValue(DownloadGameModelClass.class);
                            Log.d("DisplayPost", String.valueOf(post));
                            assert post != null;
                            if (post.getGameName().equals(downloadGames.get(position).getGameName())) {
                                Log.d("ClickedValue", String.valueOf(post));
                                //removing selected data from firebase
                                reference.child(post.getGameName()).removeValue();
                            }
//
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadGames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songTitle;
        ImageView deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.downloadImageGame);
            songTitle = itemView.findViewById(R.id.downloadTitleGame);
            deleteBtn = itemView.findViewById(R.id.deleteDownloadGame);

        }
    }


}
