package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.thinkableproject.adapters.FavouriteAdapter;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.repositories.FavDB;
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
    ArrayList<FavouriteModelClass> favouriteList;
    private FavDB favDB;
    FavouriteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        recyclerView = findViewById(R.id.gridView);
        favDB = new FavDB(getApplicationContext());

        loadData();


    }

    private void loadData() {
        favouriteList = new ArrayList<>();
        if (favouriteList != null) {
            favouriteList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_TITLE));
                String id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));
                String image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE));
                FavouriteModelClass favItem = new FavouriteModelClass(title, id, image);
                favouriteList.add(favItem);
                Log.d("Fav List =", String.valueOf(favouriteList));
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouriteAdapter(this, favouriteList);
        recyclerView.setAdapter(adapter);

    }


}