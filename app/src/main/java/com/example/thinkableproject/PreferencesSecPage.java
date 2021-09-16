package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thinkableproject.adapters.RecyclerAdaptor;
import com.example.thinkableproject.sample.MyItemTouchHelper;
import com.example.thinkableproject.sample.UserPreferences;
import com.example.thinkableproject.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PreferencesSecPage extends AppCompatActivity {

private static final String TAG = "MainActivity";
    private FloatingActionButton mFab;
    private RecyclerView mRecylerView;
    private RecyclerAdaptor mAdaptor;
    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_sec_page);
        mRecylerView = findViewById(R.id.recycler_view);
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();
        mMainActivityViewModel.getNicePlaces().observe(this, new Observer<List<UserPreferences>>() {
            @Override
            public void onChanged(List<UserPreferences> nicePlaces) {
                mAdaptor.notifyDataSetChanged();
            }
        });
        mMainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                }else{
                    mRecylerView.smoothScrollToPosition(mMainActivityViewModel.getNicePlaces().getValue().size()-1);
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdaptor = new RecyclerAdaptor(this, mMainActivityViewModel.getNicePlaces().getValue());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecylerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback=new MyItemTouchHelper(mAdaptor);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        mAdaptor.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecylerView);
        RecyclerAdaptor recyclerAdaptor=new RecyclerAdaptor();
        mRecylerView.setAdapter(mAdaptor);


    }


}