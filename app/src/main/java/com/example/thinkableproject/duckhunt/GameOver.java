package com.example.thinkableproject.duckhunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameOver extends AppCompatActivity {

    TextView tvScore;
    FirebaseFirestore database;
    User user;
    FirebaseUser mUser;
    int updatedCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dh);
        int score = getIntent().getExtras().getInt("score");
        Log.d("Score", String.valueOf(score));
        tvScore = findViewById(R.id.tvScore);
        tvScore.setText(String.valueOf(score));

        database=FirebaseFirestore.getInstance();
        mUser= FirebaseAuth.getInstance().getCurrentUser();

    database.collection("users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(score));
                updatedCoins = (int) (user.getCoins() + score);
                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                database.collection("users").document(mUser.getUid())
                        .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ColorPatternGame.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, DuckHuntMainActivity.class);
        startActivity(intent);
        finish();
    }
}