package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    ImageView editProfile;
    CircleImageView profilePic;
    TextView userName;
    FirebaseUser mUser;
    StorageReference storageReference;
    AppCompatButton calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editProfile=findViewById(R.id.edit);
        userName=findViewById(R.id.user);
        profilePic=findViewById(R.id.picture);
        calendar=findViewById(R.id.cal_reminder);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UserDetails.class);
                startActivity(intent);
            }
        });


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child(mUser.getUid()).child("profilePic.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot=task.getResult();
                    userName.setText(String.valueOf(dataSnapshot.child("userName").getValue()));
                }
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Calendar_Reminder.class);
                startActivity(intent);
            }
        });
    }
}