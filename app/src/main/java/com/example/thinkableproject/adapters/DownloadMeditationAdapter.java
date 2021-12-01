package com.example.thinkableproject.adapters;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.DownloadMeditationClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class DownloadMeditationAdapter extends RecyclerView.Adapter<DownloadMeditationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DownloadMeditationClass> downloadMusic;
    FirebaseUser mUser;

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
        Picasso.get().load(downloadMusic.get(position).getItem_image()).into(holder.imageView);
        holder.songTitle.setText(downloadMusic.get(position).getItem_title());
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DownloadsMeditation").child(mUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DownloadMeditationClass post = dataSnapshot.getValue(DownloadMeditationClass.class);
                            Log.d("DisplayPost", String.valueOf(post));
                            assert post != null;
                            if (post.getItem_title().equals(downloadMusic.get(position).getItem_title())) {
                                Log.d("Clicked", String.valueOf(post));
                                reference.child(post.getItem_title()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                File fdelete = new File(Environment.DIRECTORY_DOWNLOADS + "/" + downloadMusic.get(position).getItem_title());
                Log.d("Path", fdelete.getAbsolutePath());
//                if (fdelete.exists()) {
//                    fdelete.delete();
//                    if (fdelete.delete()) {
//                        System.out.println("file Deleted :" );
//                    } else {
//                        System.out.println("file not Deleted :" );
//                    }
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadMusic.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songTitle;
        ImageView deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.downloadImage);
            songTitle = itemView.findViewById(R.id.downloadTitle);
            deleteBtn = itemView.findViewById(R.id.deleteDownloadMeditate);

        }
    }


}
