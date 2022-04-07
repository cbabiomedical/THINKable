package com.example.thinkableproject.pianotiles;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thinkableproject.R;

public class Main3Activity extends AppCompatActivity {
    ImageView pianoInfo;
    Dialog dialogPiano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main6);
        final Intent i=new Intent(this,Main4Activity.class);
        Button b=(Button)findViewById(R.id.btnplay);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });


        SharedPreferences prefsCount = getSharedPreferences("prefsCount", MODE_PRIVATE);
        int firstStartCount = prefsCount.getInt("firstStartCount", 0);

        SharedPreferences prefsCountMem = getSharedPreferences("prefsCountMem", MODE_PRIVATE);
        int firstStartCountMem = prefsCountMem.getInt("firstStartCountMem", 0);

        dialogPiano = new Dialog(this);
        pianoInfo = findViewById(R.id.pianoInfo);



        pianoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPianoPop();
                Log.d("State","Clicked");
            }

        });

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        boolean firstStartColIn = prefsColIn.getBoolean("firstStartColIn", true);

        if (firstStartColIn) {
            displayPianoPop();
        }


    }

    private void displayPianoPop() {

        Button ok;

        dialogPiano.setContentView(R.layout.pianotiles_instructions);
        dialogPiano.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) dialogPiano.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPiano.dismiss();


            }
        });
        dialogPiano.show();

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsColIn.edit();
        editor.putBoolean("firstStartColIn", false);
        editor.apply();
    }
}
