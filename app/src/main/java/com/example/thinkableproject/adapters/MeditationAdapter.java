package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeditationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MeditationModelClass> meditationList;
    private Context context;
    ArrayList faveList=new ArrayList();
    HashMap<String,Object> fav=new HashMap<>();

    public MeditationAdapter(List<MeditationModelClass> gameList) {
        this.meditationList = gameList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.grid_item_meditation, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((ViewHolder) viewHolder).mName.setText(meditationList.get(i).getMeditationName());
        ((ViewHolder) viewHolder).isFav.setImageResource(meditationList.get(i).getIsFavourite());
//
        ((ViewHolder) viewHolder).mImage.setImageResource(meditationList.get(i).getImageView());
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);
        ((ViewHolder) viewHolder).isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewHolder) viewHolder).mUser= FirebaseAuth.getInstance().getCurrentUser();
                if (((ViewHolder) viewHolder).isFavourite) {
                    ((ViewHolder) viewHolder).isFav.setBackgroundResource(R.drawable.ic_favorite_filled);
                    ((ViewHolder) viewHolder).isFavourite = false;

                    faveList.add(meditationList.get(i));
//                    faveList.addAll((Collection) gameList.get(i));


                    fav.put("favourites",faveList);

//                    FirebaseDatabase.getInstance().getReference().child("Users").child(((ViewHolder) viewHolder).mUser.getUid()).updateChildren(fav).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // Display Toast on successful update functionality
//
//                        }
//                    });

                } else {
                    ((ViewHolder) viewHolder).isFav.setBackgroundResource(R.drawable.ic_favorite);
                    ((ViewHolder) viewHolder).isFavourite = true;
                    faveList.remove(meditationList.get(i));
                    fav.put("favourites",faveList);
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(((ViewHolder) viewHolder).mUser.getUid()).updateChildren(fav).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // Display Toast on successful update functionality
//
//                        }
//                    });


                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return meditationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mName;
        ImageView isFav;
        boolean isFavourite = false;
        DatabaseReference reference;
        FirebaseUser mUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.gridImage);
            mName = itemView.findViewById(R.id.item_name);
            isFav = itemView.findViewById(R.id.favouritesIcon);
            mUser = FirebaseAuth.getInstance().getCurrentUser();


        }

    }

}
