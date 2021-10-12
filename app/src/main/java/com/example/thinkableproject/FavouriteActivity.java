package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<FavouriteModelClass> favList;
    FavouriteAdapter adapter;
    DatabaseReference reference;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        recyclerView = findViewById(R.id.gridView);
        initData();

        initRecyclerView();

    }


    private void initData() {

//        favList.add(new FavouriteModelClass(R.drawable.chess,"Chess",R.drawable.ic_favorite_filled));
    }


    private void initRecyclerView() {
//        favList = new ArrayList<FavouriteModelClass>();
////        favList.add(new FavouriteModelClass(R.drawable.chess,"Chess",R.drawable.ic_favorite_filled));
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        Log.d("mUser", mUser.getUid());
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        reference.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().exists()) {
//                        Toast.makeText(FavouriteActivity.this, "Successfully Read", Toast.LENGTH_SHORT).show();
//                        DataSnapshot dataSnapshot = task.getResult();
////                       favList.add(dataSnapshot.child("favourites").getValue());
////                      favList.add(new FavouriteModelClass((Integer)dataSnapshot.child("favourites").child("imageView").getValue(),
////                   String.valueOf(dataSnapshot.child("favourites").child("gameName").getValue()),R.drawable.ic_favorite_filled));
//                        favList.add(new FavouriteModelClass(R.drawable.chess, (String) dataSnapshot.child("favourites").child("gameName").getValue(),R.drawable.ic_favorite_filled));
//                        Log.d("FavList", String.valueOf(favList));
//                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//                      adapter = new FavouriteAdapter(favList);
//                        recyclerView.setAdapter(adapter);
//
//                    } else {
//                        Toast.makeText(FavouriteActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(FavouriteActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//    }
    }
}