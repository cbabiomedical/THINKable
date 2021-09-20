package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.adapters.Adapter;
import com.example.thinkableproject.adapters.RecyclerAdaptor;
import com.example.thinkableproject.sample.ModelClass;
import com.example.thinkableproject.sample.MyItemTouchHelper;
import com.example.thinkableproject.sample.UserPreferences;
import com.example.thinkableproject.viewmodels.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreferencesSecPage extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button done;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ModelClass> userList;
    FirebaseUser mUser;
    Adapter adapter;
    FirebaseAuth mAuth;
    String onlineUserId;

    HashMap<String, Object> preference = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_sec_page);
        recyclerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);
//        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        mMainActivityViewModel.init();
//        mMainActivityViewModel.getNicePlaces().observe(this, new Observer<List<UserPreferences>>() {
//            @Override
//            public void onChanged(List<UserPreferences> nicePlaces) {
//                mAdaptor.notifyDataSetChanged();
//            }
//        });
//        mMainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                } else {
//                    mRecylerView.smoothScrollToPosition(mMainActivityViewModel.getNicePlaces().getValue().size() - 1);
//                }
//            }
//        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDataInDatabase();

            }
        });

//        mUser=mAuth.getCurrentUser();
//         onlineUserId = mUser.getUid();
//         Log.d("User")
        initData();
        initRecyclerView();
    }

    private void initData() {
        userList=new ArrayList<>();
        userList.add(new ModelClass(R.drawable.video,"Videos"));
        userList.add(new ModelClass(R.drawable.music,"Music"));
        userList.add(new ModelClass(R.drawable.meditation,"Meditation"));
        userList.add(new ModelClass(R.drawable.controller,"Games"));
        userList.add(new ModelClass(R.drawable.binaural,"Bineural Waves"));
        userList.add(new ModelClass(R.drawable.playtime,"Kids"));
        userList.add(new ModelClass(R.drawable.book,"Sleep Stories"));
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.recycler_view);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new Adapter(userList);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mUser = FirebaseAuth.getInstance().getCurrentUser();



        Log.d("Preference", String.valueOf(preference));



    }



    private void putDataInDatabase() {
        preference.put("preferences", userList);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).updateChildren(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(PreferencesSecPage.this,"Successful",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PreferencesSecPage.this,EnterPhoneActivity.class);
                startActivity(intent);
            }
        });
        Log.d("User", mUser.getUid());
//

    }

    @Override
    protected void onStart() {
        super.onStart();
        preference.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preference.clear();

    }


    public void GOTOSIGNIN(View view) {

            Intent intentGotoSI = new Intent(PreferencesSecPage.this,SignInActivity.class);
            startActivity(intentGotoSI);

    }
}
