package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    TextView forgotPassword;
     EditText emailAddress, passwordTxt;
    Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button) findViewById(R.id.signIn);
//        signIn.setOnClickListener(this);

        emailAddress = (EditText) findViewById(R.id.email);
        passwordTxt = (EditText) findViewById(R.id.password);

        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgetPassword);
//        forgotPassword.setOnClickListener(this);

    }

    //get & display current user's profile
    @Override
    protected void onStart() {
        super.onStart();

    }

    //get user credentials & convert it back to string

    private void userLogin() {
        String email = emailAddress.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        if(email.isEmpty()){
            emailAddress.setError("Email is required");
            emailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAddress.setError("Enter a valid email!");
            emailAddress.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordTxt.setError("Password is Required");
            passwordTxt.requestFocus();
            return;
        }
        if(password.length() < 8){
            passwordTxt.setError("Minimum Password length should be 8 characters!");
            passwordTxt.requestFocus();
            return;
        }
        progressBar.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(SignInActivity.this, LandingPage.class));

                     /*if (user.isEmailVerified()) {
                        // redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account",Toast.LENGTH_LONG)
                                .show();
                    }*/

                }else {
                    Toast.makeText(SignInActivity.this,"LogIn Failed! Please check your username or password again",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }
}