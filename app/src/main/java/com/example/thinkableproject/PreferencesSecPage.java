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

import com.example.thinkableproject.adapters.RecyclerAdaptor;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreferencesSecPage extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button done;
    private RecyclerView mRecylerView;
    private RecyclerAdaptor mAdaptor;
    private MainActivityViewModel mMainActivityViewModel;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String onlineUserId;
    Intent intent = new Intent();
    String _PREFERENCE = intent.getStringExtra("preference");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_sec_page);
        mRecylerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);
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
                if (aBoolean) {
                } else {
                    mRecylerView.smoothScrollToPosition(mMainActivityViewModel.getNicePlaces().getValue().size() - 1);
                }
            }
        });

//        mUser=mAuth.getCurrentUser();
//         onlineUserId = mUser.getUid();

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdaptor = new RecyclerAdaptor(this, mMainActivityViewModel.getNicePlaces().getValue());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecylerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(mAdaptor);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        mAdaptor.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecylerView);
        RecyclerAdaptor recyclerAdaptor = new RecyclerAdaptor();
        mRecylerView.setAdapter(mAdaptor);

        mUser = FirebaseAuth.getInstance().getCurrentUser();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDataInDatabase();
            }
        });


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            mUser = mAuth.getCurrentUser();
            assert mUser != null;
            onlineUserId = mUser.getUid();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

//    public void updateUser(View view) {
//        if (isPreferenceChanage()) {
//            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
//
//        }
//    }

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getPreferences());

//    private boolean isPreferenceChanage() {
//        if (_PREFERENCE.equals("")) {
//            reference.child(_PREFERENCE)
//        } else {
//            return false;
//        }
//

    private void putDataInDatabase() {
        if (mUser != null) {
            String uId = mUser.getEmail();
            Log.d("TAG", "User is there");
            Log.d("ID", uId);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            DatabaseReference usersRef = ref.child("Users");
            DatabaseReference hopperRef = usersRef.child("N4Aw6EFDjnRWDrUpNdRiKlU2LY13");
//            Map<String, Object> hopperUpdates = new HashMap<>();
//            hopperUpdates.put("preference",mMainActivityViewModel.getNicePlaces().getValue());
            Log.d("Ref", String.valueOf(ref));
            Log.d("hopRef", String.valueOf(hopperRef));
            Log.d("userRef", String.valueOf(usersRef));
            Map<String, User> users = new HashMap<>();
            users.put("preference", new User("Video"));
            Log.d("User", String.valueOf(users));
            hopperRef.setValue(users);

//            ref = FirebaseDatabase.getInstance().getReference("N4Aw6EFDjnRWDrUpNdRiKlU2LY13");
            Log.d("READ", String.valueOf(ref));
//            hopperRef.updateChildren(hopperUpdates);


//            reference.updateChildren(prefMap);

        } else {
            Log.d("TAG", "User is not there");
        }

//        Log.d("ID", onlineUserId);
//
//        Log.d("Path", String.valueOf(reference));


//        reference.child("preference").updateChildren(prefMap);
        Toast.makeText(this, "Your preference arranging update successful", Toast.LENGTH_SHORT).show();
    }


}