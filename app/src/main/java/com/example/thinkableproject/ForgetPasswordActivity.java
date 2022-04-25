package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private AppCompatButton resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText = (EditText) findViewById(R.id.email);
        resetPasswordButton = (AppCompatButton) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //get instance of firebase user authentication
        auth = FirebaseAuth.getInstance();

        //onclick listener for resetPasswordButton to run resetPassword() method
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    //resetting password
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        //check email is entered or not
        if (email.isEmpty()) {
            emailEditText.setError("Email is Required");
            emailEditText.requestFocus();
            return;
        }

        //check if a valid email is entered
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        //after user is authenticated send reset password mail to user email via otp
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //if task is successful show the toast
                    Toast.makeText(ForgetPasswordActivity.this, "Check your email to reset your password & Log in again to continue!", Toast.LENGTH_LONG)
                            .show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //after user change password via otp redirect user to signin page to signin with new password
                            startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
                        }
                    }, 8000);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Something went wrong! Try again", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }
}