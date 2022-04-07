package com.example.thinkableproject.ninjadarts;

import androidx.appcompat.app.AppCompatActivity;

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

public class ninjaStart extends AppCompatActivity {

    ImageView ninjaInfo;
    Dialog dialogNinja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja_start);

        SharedPreferences prefsCount = getSharedPreferences("prefsCount", MODE_PRIVATE);
        int firstStartCount = prefsCount.getInt("firstStartCount", 0);

        SharedPreferences prefsCountMem = getSharedPreferences("prefsCountMem", MODE_PRIVATE);
        int firstStartCountMem = prefsCountMem.getInt("firstStartCountMem", 0);

        dialogNinja = new Dialog(this);
        ninjaInfo = findViewById(R.id.ninjaInfo);



        ninjaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNinjaPop();
                Log.d("State","Clicked");
            }

        });

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        boolean firstStartColIn = prefsColIn.getBoolean("firstStartColIn", true);

        if (firstStartColIn) {
            displayNinjaPop();
        }

    }

    private void displayNinjaPop() {
        Button ok;

        dialogNinja.setContentView(R.layout.colorpattern_instructions);
        dialogNinja.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) dialogNinja.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNinja.dismiss();


            }
        });
        dialogNinja.show();

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsColIn.edit();
        editor.putBoolean("firstStartColIn", false);
        editor.apply();
    }

    public void startGameninja(View view) {
        Intent intentVerifyNum = new Intent(ninjaStart.this, NinjaDartsMainActivity.class);
        startActivity(intentVerifyNum);
    }
}