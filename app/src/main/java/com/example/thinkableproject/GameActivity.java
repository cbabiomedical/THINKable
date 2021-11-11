package com.example.thinkableproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.repositories.DownloadMusic;
import com.example.thinkableproject.sample.GameModelClass;
import com.example.thinkableproject.sample.MusicModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity implements GridAdapter.OnNoteListner {
    RecyclerView recyclerView;
    LinearLayout linearLayoutManager;
    ArrayList<GameModelClass> gameList;
    GridAdapter adapter;
    FirebaseUser mUser;
    ArrayList<GameModelClass> downloadGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_excercise);
        recyclerView = findViewById(R.id.gridView);
        //  favouriteBtn = findViewById(R.id.favouritesIcon);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        initData();

        //Calling initRecyclerView function
    }

    private void initData() {
        gameList = new ArrayList<>();
//        //Adding user preferences to arraylist
        gameList.add(new GameModelClass(R.drawable.chess, "Chess", "0", "0"));
        gameList.add(new GameModelClass(R.drawable.images, "Puzzle", "1", "0"));
        gameList.add(new GameModelClass(R.drawable.sudoku, "Sudoku", "2", "0"));
        gameList.add(new GameModelClass(R.drawable.crossword, "CrossWord", "3", "0"));
//////
////        HashMap<String, Object> games=new HashMap<>();
////        games.put("games",gameList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Games").child(mUser.getUid());
        reference.setValue(gameList);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Games").child(mUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postDatasnapshot : snapshot.getChildren()) {
//                    GameModelClass post = postDatasnapshot.getValue(GameModelClass.class);
//                    Log.d("Post", String.valueOf(post));
//                    gameList.add(post);
//                }
//
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        Log.d("List", String.valueOf(gameList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new GridAdapter(gameList, getApplicationContext(), this::onNoteClickGame);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onNoteClickGame(int position) {


        gameList.get(position);
        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();


        GameModelClass gameModelClass = new GameModelClass(gameList.get(position).getImageView(), gameList.get(position).getGameName());

        downloadGames.add(gameModelClass);

        Log.d("Downloads", String.valueOf(downloadGames));
        startActivity(new Intent(getApplicationContext(), MyDownloads.class));

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
        if(launchIntent != null){
            Log.d("Tagopenapp", "---------------------B--------------------------");
            startActivity(launchIntent);
        }else{
            Toast.makeText(GameActivity.this, "There is no package", Toast.LENGTH_LONG).show();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());

        reference.setValue(downloadGames);

    }

}