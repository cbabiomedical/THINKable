package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.thinkableproject.databinding.ActivityMainBinding;
import com.example.thinkableproject.databinding.ActivityUserDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails extends AppCompatActivity {
    FirebaseUser mUser;
    CircleImageView profilePicture;
    EditText username,emailAddress,occupation,dateOfBirth;
    AppCompatButton update;
    RadioButton male,female;
    String gender="";
    ActivityUserDetailsBinding binding;
    DatabaseReference reference;
    boolean isMale;
    boolean isFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profilePicture=findViewById(R.id.profilePic);
        username=findViewById(R.id.userName);
        emailAddress=findViewById(R.id.email);
        occupation=findViewById(R.id.occupation);
        dateOfBirth=findViewById(R.id.dob);
        male=findViewById(R.id.radio_male);
        female=findViewById(R.id.radio_female);
        update=findViewById(R.id.update);
        isMale=male.isChecked();
        isFemale=female.isChecked();
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        readData();
    }
    private void readData(){
        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(UserDetails.this,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot=task.getResult();
                        username.setHint(String.valueOf(dataSnapshot.child("userName").getValue()));
                        emailAddress.setHint(String.valueOf(dataSnapshot.child("email").getValue()));
                        gender=String.valueOf(dataSnapshot.child("gender").getValue());
                        if(gender.equals("Male")){
                            male.setChecked(true);
                        }else{
                            female.setChecked(true);
                        }
                        occupation.setHint(String.valueOf(dataSnapshot.child("occupation").getValue()));
                        dateOfBirth.setHint(String.valueOf(dataSnapshot.child("dob").getValue()));

                    }else{
                        Toast.makeText(UserDetails.this,"User does not exist",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(UserDetails.this,"Read Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}