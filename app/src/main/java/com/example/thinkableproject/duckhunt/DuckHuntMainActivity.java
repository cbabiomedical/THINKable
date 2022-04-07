package com.example.thinkableproject.duckhunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thinkableproject.R;

public class DuckHuntMainActivity extends AppCompatActivity {


    ImageView duckInfo1;
    Dialog dialogDuck;
    ConstraintLayout mainConstraint2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duck_hunt_main);
        SharedPreferences prefsCount = getSharedPreferences("prefsCount", MODE_PRIVATE);
        int firstStartCount = prefsCount.getInt("firstStartCount", 0);

        SharedPreferences prefsCountMem = getSharedPreferences("prefsCountMem", MODE_PRIVATE);
        int firstStartCountMem = prefsCountMem.getInt("firstStartCountMem", 0);

        dialogDuck = new Dialog(this);
        duckInfo1 = findViewById(R.id.duckInfo);



        duckInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDuckPop();
                Log.d("State","Clicked");
            }

        });

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        boolean firstStartColIn = prefsColIn.getBoolean("firstStartColIn", true);

        if (firstStartColIn) {
            displayDuckPop();
        }
    }

    private void displayDuckPop() {
        Button ok;

        dialogDuck.setContentView(R.layout.duckhunt_instructions);
        dialogDuck.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) dialogDuck.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDuck.dismiss();


            }
        });
        dialogDuck.show();

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsColIn.edit();
        editor.putBoolean("firstStartColIn", false);
        editor.apply();


    }

    public void startGame(View view){

        //Log.i("ImageButton", "Clicked");
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
        finish();

    }
}