package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import com.example.thinkableproject.adapters.Adapter;
import com.example.thinkableproject.sample.ModelClass;
import com.example.thinkableproject.sample.MyItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreferencesSecPage extends AppCompatActivity {

    private AppCompatButton done;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ModelClass> userList;
    FirebaseUser mUser;
    Adapter adapter;
    Animation scaleUp, scaleDown;



    HashMap<String, Object> preference = new HashMap<>();  // Creating hashmap to store user preference values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_sec_page);
        recyclerView = findViewById(R.id.recycler_view);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        done = findViewById(R.id.done);
//
        //onClick function of doen button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling putInDatabase function
                putDataInDatabase();

            }
        });

        done.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    done.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    done.startAnimation(scaleDown);
                }

                return false;
            }
        });

//        mUser=mAuth.getCurrentUser();
//         onlineUserId = mUser.getUid();
//         Log.d("User")
        //Calling init Data function
        initData();
        //Calling initRecyclerView function
        initRecyclerView();
    }

    private void initData() {
        userList = new ArrayList<>();
        //Adding user preferences to arraylist
        userList.add(new ModelClass(R.drawable.video, "Videos"));
        userList.add(new ModelClass(R.drawable.music, "Music"));
        userList.add(new ModelClass(R.drawable.meditation, "Meditation"));
        userList.add(new ModelClass(R.drawable.controller, "Games"));
        userList.add(new ModelClass(R.drawable.binaural, "Bineural Waves"));
        userList.add(new ModelClass(R.drawable.playtime, "Kids"));
        userList.add(new ModelClass(R.drawable.book, "Sleep Stories"));
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        //Initializing liner layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //Setting layout of recylcerview
        recyclerView.setLayoutManager(linearLayoutManager);
        //Initializing adapter
        adapter = new Adapter(userList);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //Calling set method of TouchHelper variable
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mUser = FirebaseAuth.getInstance().getCurrentUser();


        Log.d("Preference", String.valueOf(preference));


    }


    private void putDataInDatabase() {
        preference.put("preferences", userList); // adding user preference list to hashmap
        // Giving path of user variable to update user information
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).updateChildren(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Display Toast on successful update functionality
                Toast.makeText(PreferencesSecPage.this, "Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PreferencesSecPage.this, EnterPhoneActivity.class);
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

        Intent intentGotoSI = new Intent(PreferencesSecPage.this, SignInActivity.class);
        startActivity(intentGotoSI);

    }

    public void gotoPrefer(View view) {
    }
}
