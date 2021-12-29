package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.thinkableproject.R;
import com.example.thinkableproject.Setting;
import com.example.thinkableproject.adapters.Adapter;
import com.example.thinkableproject.sample.ModelClass;
import com.example.thinkableproject.sample.MyItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsPreference extends AppCompatActivity {
    private Button done;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ModelClass> userList;
    RelativeLayout layoutMain;
    FirebaseUser mUser;
    Adapter adapter;
    int color;


    HashMap<String, Object> preference = new HashMap<>();  // Creating hashmap to store user preference values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_preference);
        recyclerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);
        layoutMain=findViewById(R.id.layoutMain);

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {
                    layoutMain.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.preflight));


                } else {
                    layoutMain.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.prefbg));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
        //onClick function of doen button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling putInDatabase function
                putDataInDatabase();

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
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("preferences");
        Log.d("ReferencePath", String.valueOf(reference));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ModelClass post=dataSnapshot.getValue(ModelClass.class);
                    userList.add(post);
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Toast.makeText(SettingsPreference.this, "Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsPreference.this, Setting.class);
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

        Intent intentGotoSI = new Intent(SettingsPreference.this, Setting.class);
        startActivity(intentGotoSI);

    }
}