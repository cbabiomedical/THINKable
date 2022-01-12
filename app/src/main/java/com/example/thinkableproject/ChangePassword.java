package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {
    EditText new_password;
    EditText confirm_Password;
    Button resetPassword;
    FirebaseUser user;
    View c1, c2;
    int color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        new_password = (EditText) findViewById(R.id.new_password);
        confirm_Password = (EditText) findViewById(R.id.confirm_Password);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.VISIBLE);


                } else {
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        resetPassword = (Button) findViewById(R.id.resetPassword);

        //set onclick listener for resetpassword button to reset the password
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get new password
                if (new_password.getText().toString().isEmpty()) {
                    new_password.setText("Required Field");
                    return;
                }

                //confirm new password
                if (confirm_Password.getText().toString().isEmpty()) {
                    confirm_Password.setError("Required Field");
                    return;
                }

                //check whether password match
                if (!new_password.getText().toString().equals(confirm_Password.getText().toString())) {
                    new_password.setError("Password Do not Match");
                }

                //update user password
                user.updatePassword(new_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //give toast on password update success
                        Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LandingPage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //give toast on password update fail
                        Toast.makeText(ChangePassword.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}