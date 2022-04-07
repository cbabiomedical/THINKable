package com.example.thinkableproject;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.thinkableproject.adapters.SuggestionsAdapter;
import com.example.thinkableproject.sample.MyItemTouchHelper;
import com.example.thinkableproject.sample.SuggestionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Suggestions extends AppCompatActivity {

    //    private static final String TAG = "MainActivity";
    private Button done;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<SuggestionsModel> userList;
    FirebaseUser mUser;
    Animation scaleUp, scaleDown;

    SuggestionsAdapter adapter;
    FirebaseAuth mAuth;
    String onlineUserId;

    //suggestions hashmap
    HashMap<String, Object> suggestions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        recyclerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);



        //store data in database
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        initData();
        initRecyclerView();
    }

    //set data in recycler view imageview and textview
    private void initData() {
        userList = new ArrayList<>();
        userList.add(new SuggestionsModel(R.drawable.calmit, "Anxiety Reading"));
        userList.add(new SuggestionsModel(R.drawable.wav, "Stress Reducing"));
        userList.add(new SuggestionsModel(R.drawable.moono, "Sleep Better"));
        userList.add(new SuggestionsModel(R.drawable.tar, "Focus Improvement"));
        userList.add(new SuggestionsModel(R.drawable.medi, "Increase Concentration"));
        userList.add(new SuggestionsModel(R.drawable.aff, "Increase Happiness"));
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SuggestionsAdapter(userList);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //get adapter to set data change
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //user authentication instance
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("suggestions", String.valueOf(suggestions));

    }

    private void putDataInDatabase() {
        //put suggestions into user profile as a child branch
        suggestions.put("suggestions", userList);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).updateChildren(suggestions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Suggestions.this, "Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Suggestions.this, PreferencesSecPage.class);
                startActivity(intent);
            }
        });
        Log.d("User", mUser.getUid());
//

    }


    public void gotoPrefTwo(View view) {
        Intent intentgotoPreTwo = new Intent(Suggestions.this, PreferencesSecPage.class);


        startActivity(intentgotoPreTwo);

    }

    public void gotoPrefer(View view) {
        //go to preferences page
        Intent intentGotoSI = new Intent(Suggestions.this, PreferencesSecPage.class);
        startActivity(intentGotoSI);
    }
}
