package com.example.thinkableproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.thinkableproject.adapters.ExampleAdapter;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    private static Uri alarmSound;
    private NotificationManager mNotificationManager;
    Button vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        vibrate = findViewById(R.id.vibrate);

        NotificationManager mNotificationManager;
        Switch switchInput = new Switch(this);
        int colorOn = 0xFF323E46;
        int colorOff = 0xFF666666;
        int colorDisabled = 0xFF333333;
        StateListDrawable thumbStates = new StateListDrawable();
        thumbStates.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(colorOn));
        thumbStates.addState(new int[]{-android.R.attr.state_enabled}, new ColorDrawable(colorDisabled));
        thumbStates.addState(new int[]{}, new ColorDrawable(colorOff)); // this one has to come last
        switchInput.setThumbDrawable(thumbStates);

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem("Popup Notifications"));
        exampleList.add(new ExampleItem("User high priority Notifications "));
        exampleList.add(new ExampleItem("Vibrate"));
        exampleList.add(new ExampleItem("Quote Notifications"));


        mRecyclerView = findViewById(R.id.noti);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        vibrate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                buttonVibrate(v);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buttonVibrate(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));

    }


}