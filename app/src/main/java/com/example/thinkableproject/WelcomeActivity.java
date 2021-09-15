package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView getStarted;
    private TextView getStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted=findViewById(R.id.getStart);

        Log.d("USERLOGIN", "----------------------E----------------------------");
        Log.d("USERLOGIN", "----------------------F----------------------------");
        Log.d("USERLOGIN", "----------------------G----------------------------");
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USERLOGIN", "----------------------E----------------------------");
                Log.d("USERLOGIN", "----------------------F----------------------------");
                Log.d("USERLOGIN", "----------------------G----------------------------");
//                Intent intent=new Intent(WelcomeActivity.this,SignInActivity.class);
//                startActivity(intent);
            }
        });
    }
}