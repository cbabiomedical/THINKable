package com.example.thinkableproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.adapters.Adapter;
import com.example.thinkableproject.adapters.SuggestionsAdapter;
import com.example.thinkableproject.sample.ModelClass;
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

    private static final String TAG = "MainActivity";
    private Button done;
    private Button next;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<SuggestionsModel> userList;
    FirebaseUser mUser;
    SuggestionsAdapter adapter;
    FirebaseAuth mAuth;
    String onlineUserId;

    HashMap<String, Object> suggestions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        recyclerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);
        next = findViewById(R.id.next);

//        coursesGV = findViewById(R.id.idGVcourses);
//
//        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
//        courseModelArrayList.add(new CourseModel("Anxiety Reducing", R.drawable.calmit));
//        courseModelArrayList.add(new CourseModel("Stress Reducing", R.drawable.wav));
//        courseModelArrayList.add(new CourseModel("Sleep Better", R.drawable.moono));
//        courseModelArrayList.add(new CourseModel("Focus Improvement", R.drawable.tar));
//        courseModelArrayList.add(new CourseModel("Increase Concentration", R.drawable.medi));
//        courseModelArrayList.add(new CourseModel("Increase Happiness", R.drawable.aff));
//
//        CourseGVAdapter adapter = new CourseGVAdapter(this, courseModelArrayList);
//        coursesGV.setAdapter(adapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDataInDatabase();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Suggestions.this, PreferencesSecPage.class);
                startActivity(intent);
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
        userList.add(new SuggestionsModel(R.drawable.calmit,"Anxiety Reading"));
        userList.add(new SuggestionsModel(R.drawable.wav,"Stress Reducing"));
        userList.add(new SuggestionsModel(R.drawable.moono,"Sleep Better"));
        userList.add(new SuggestionsModel(R.drawable.tar,"Focus Improvement"));
        userList.add(new SuggestionsModel(R.drawable.medi,"Increase Concentration"));
        userList.add(new SuggestionsModel(R.drawable.aff,"Increase Happiness"));
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.recycler_view);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new SuggestionsAdapter(userList);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("suggestions", String.valueOf(suggestions));

    }

    private void putDataInDatabase() {
        suggestions.put("suggestions", userList);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).updateChildren(suggestions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Suggestions.this,"Successful",Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("User", mUser.getUid());
//

    }


    public void gotoPrefTwo(View view) {
        Intent intentgotoPreTwo = new Intent(Suggestions.this,PreferencesSecPage.class);


        startActivity(intentgotoPreTwo);

    }
}
