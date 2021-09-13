package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText newPassword;
    EditText confirmPassword;
    Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        newPassword=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        resetPassword=findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}