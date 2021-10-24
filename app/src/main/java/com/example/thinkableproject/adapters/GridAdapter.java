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
import com.example.thinkableproject.sample.FavouriteModelClass;
import com.example.thinkableproject.sample.GameModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


    public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<GameModelClass> gameList;
        private Context context;
        ArrayList <FavouriteModelClass>faveList=new ArrayList();
        HashMap<String,Object> fav=new HashMap<>();

        LayoutInflater inflater;
        public GridAdapter(List<GameModelClass> gameList) {
            this.gameList = gameList;
        }

        @NonNull
        @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.grid_item, viewGroup, false);
                ViewHolder vh = new ViewHolder(view);
                return vh;
            }

            @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                    ((ViewHolder) viewHolder).mName.setText(gameList.get(i).getGameName());
                    ((ViewHolder) viewHolder).isFav.setImageResource(gameList.get(i).getIsFavourite());
//
                    ((ViewHolder) viewHolder).mImage.setImageResource(gameList.get(i).getImageView());
                    RequestOptions defaultOptions = new RequestOptions()
                            .error(R.drawable.ic_launcher_background);
                    ((ViewHolder) viewHolder).isFav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((ViewHolder) viewHolder).mUser=FirebaseAuth.getInstance().getCurrentUser();
                            if (((ViewHolder) viewHolder).isFavourite) {
                                ((ViewHolder) viewHolder).isFav.setBackgroundResource(R.drawable.ic_favorite_filled);
                                ((ViewHolder) viewHolder).isFavourite = false;
                                FavouriteModelClass favouriteModelClass=new FavouriteModelClass();
                                favouriteModelClass.favName=gameList.get(i).getGameName();
                                favouriteModelClass.imageView=gameList.get(i).getImageView();
                                favouriteModelClass.setIsFave(R.drawable.ic_favorite_filled);
                                faveList.add(favouriteModelClass);
//                    faveList.addAll((Collection) gameList.get(i));


                                fav.put("favourites",faveList);

                                FirebaseDatabase.getInstance().getReference().child("Users").child(((ViewHolder) viewHolder).mUser.getUid()).updateChildren(fav).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Display Toast on successful update functionality

                                    }
                                });

                            } else {
                                ((ViewHolder) viewHolder).isFav.setBackgroundResource(R.drawable.ic_favorite);
                                ((ViewHolder) viewHolder).isFavourite = true;
//                                faveList.remove(gameList.get(i));
//                                fav.put("favourites",faveList);
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(((ViewHolder) viewHolder).mUser.getUid()).updateChildren(fav).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            // Display Toast on successful update functionality
//
//                                        }
//                                    });


                                }

                            }

                        });
                    GameModelClass gameModelClass=new GameModelClass();
                    FirebaseDatabase.getInstance().getReference().child("GameModel").child(((ViewHolder) viewHolder).mUser.getUid()).child("games").setValue(gameList);
                    }

                    @Override
                        public int getItemCount() {
                            return gameList.size();
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