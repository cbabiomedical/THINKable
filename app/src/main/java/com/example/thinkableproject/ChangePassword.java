package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText new_password;
    EditText confirm_Password;
    Button resetPassword;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        new_password = (EditText) findViewById(R.id.new_password);
        confirm_Password = (EditText) findViewById(R.id.confirm_Password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        resetPassword = (Button) findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_password.getText().toString().isEmpty()){
                    new_password.setText("Required Field");
                    return;
                }

                if(confirm_Password.getText().toString().isEmpty()){
                    confirm_Password.setError("Required Field");
                    return;
                }

                if(!new_password.getText().toString().equals(confirm_Password.getText().toString())){
                    new_password.setError("Password Do not Match");
                }

                user.updatePassword(new_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LandingPage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}