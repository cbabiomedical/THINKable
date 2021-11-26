package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.widget.Switch;

import com.example.thinkableproject.adapters.ExampleAdapter;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


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
    }


}