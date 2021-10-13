package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
    }

    public void btnCurrentLocation(View view) {

        startActivity(new Intent(this, MapsActivity.class));
    }

    public void btnRetrieveLocation (View view) {

        startActivity(new Intent(getApplicationContext(), RetrieveMapsActivity.class));

    }
}