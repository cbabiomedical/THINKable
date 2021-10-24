package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.thinkableproject.adapters.FavouriteAdapter;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.sample.FavouriteModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager linearLayoutManager;
    List<FavouriteModelClass> favouriteList=new ArrayList<FavouriteModelClass>();
    FavouriteAdapter adapter;
    int image;
    int fav;
    String name;
    FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("favourites");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
            FavouriteModelClass post = postSnapshot.getValue(FavouriteModelClass.class);
            Log.d("Post", String.valueOf(post));
                    favouriteList.add(post);
                }

//                FavouriteModelClass favouriteModelClass=new FavouriteModelClass();
//
//                 favouriteModelClass= (FavouriteModelClass) snapshot.getValue();
//
//                 Log.d("Image", String.valueOf(image));
//                name= (String) snapshot.child("favName").getValue();
//                Log.d("Name",name);
////                 fav= (int)snapshot.child("isFave").getValue();
//                 Log.d("isFav", String.valueOf(fav));


                Log.d("List", String.valueOf(favouriteList));
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                adapter = new FavouriteAdapter((ArrayList<FavouriteModelClass>) favouriteList);
                recyclerView.setAdapter(adapter);

//                int img=(int)image;
//                int favs=(int)fav;


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("List", String.valueOf(favouriteList));


    }
    private void initRecyclerView() {

    }

}