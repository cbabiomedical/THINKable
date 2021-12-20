package com.example.thinkableproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.thinkableproject.adapters.GridAdapter;
import com.example.thinkableproject.sample.GameModelClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements GridAdapter.OnNoteListner {
    RecyclerView recyclerView;
    ArrayList<GameModelClass> gameList;
    GridAdapter adapter;
    FirebaseUser mUser;
    HashMap<String, Object> games = new HashMap<>();
    ArrayList<GameModelClass> downloadGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_excercise);
        recyclerView = findViewById(R.id.gridView);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.exercise);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Relaxation_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        initData();

        //Calling initRecyclerView function
    }

    private void initData() {
        gameList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Games_Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GameModelClass post = dataSnapshot.getValue(GameModelClass.class);
                    gameList.add(post);
                    Log.d("GamePost", String.valueOf(post));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new GridAdapter(gameList, getApplicationContext(), this::onNoteClickGame);
        recyclerView.setAdapter(adapter);
        Log.d("List", String.valueOf(gameList));


    }


    @Override
    public void onNoteClickGame(int position) {


        gameList.get(position);
        if (gameList.get(position).getGameName().equals("Card Memory Game")){
            startActivity(new Intent(getApplicationContext(),MainActivityK.class));
        }
        else{
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
            if (launchIntent != null) {
                Log.d("Tagopenapp", "---------------------B--------------------------");
                startActivity(launchIntent);
            } else {
                Toast.makeText(GameActivity.this, "There is no package", Toast.LENGTH_LONG).show();
            }



        }
        Toast.makeText(getApplicationContext(), "You clicked " + gameList.get(position).getGameName(), Toast.LENGTH_SHORT).show();


        GameModelClass gameModelClass = new GameModelClass(gameList.get(position).getGameImage(), gameList.get(position).getGameName());

        downloadGames.add(gameModelClass);
        Log.d("DownloadGames", String.valueOf(downloadGames));
//        games.put(downloadGames.get(position).getGameName(), gameModelClass);

        Log.d("CHECK UPLOAD", "-------------------------CHECKING FIREBASE UPLOAD-----------------");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UsersGame").child(mUser.getUid());
        reference1.setValue(games);
//        reference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Log.d("CHECK UPLOAD", "------------------------CHECK AFTER UPLOAD------------");

        Log.d("Downloads", String.valueOf(downloadGames));


    }

}