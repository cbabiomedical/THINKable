package com.example.thinkableproject.IHaveToFly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinkableproject.R;

public class IHaveToFlyMainActivity extends AppCompatActivity {

    private boolean isMute;
    ImageView ihavetoinfo;
    Dialog dialogIhaveto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ihave_to_fly_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IHaveToFlyMainActivity.this, GameActivity.class));
            }
        });

        ihavetoinfo = findViewById(R.id.ihavetoInfo);
        dialogIhaveto = new Dialog(this);




        ihavetoinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayIhavetoPop();
                Log.d("State","Clicked");
            }

        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();

            }
        });

    }

    private void displayIhavetoPop() {

        Button ok;

        dialogIhaveto.setContentView(R.layout.havetofly_instructions);
        dialogIhaveto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = (Button) dialogIhaveto.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIhaveto.dismiss();


            }
        });
        dialogIhaveto.show();

        SharedPreferences prefsColIn = getSharedPreferences("prefsColIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsColIn.edit();
        editor.putBoolean("firstStartColIn", false);
        editor.apply();
    }
}
