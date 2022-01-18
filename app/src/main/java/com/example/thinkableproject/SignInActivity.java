package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    TextView forgotPassword;
    EditText emailAddress, passwordTxt;
    AppCompatButton signIn, signUp;
    RelativeLayout mainLayout;
    VideoView videoView;
    Dialog dialog;


    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        dialog = new Dialog(this);

        mainLayout = findViewById(R.id.mainLayout);
        videoView = findViewById(R.id.simpleVideo);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set onclick listener for signin button
        signIn = (AppCompatButton) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        //set onclick listener for signup button
        signUp = (AppCompatButton) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        emailAddress = (EditText) findViewById(R.id.username);
        passwordTxt = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //set onclick listener for forgotpassword button
        forgotPassword = findViewById(R.id.forgetPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                //go to sign up page
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.signIn:
//                Log.d("USERLOGIN", "----------------------H----------------------------");
//                Log.d("USERLOGIN", "----------------------I----------------------------");
//                Log.d("USERLOGIN", "----------------------J----------------------------");

                userLogin();


                break;

            case R.id.forgetPassword:
                //go to forget password page
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    private void openInstructionsPopUp() {
        startActivity(new Intent(getApplicationContext(),Video.class));
//        VideoView videoView;
//
//
//        dialog.setContentView(R.layout.activity_video);
//        videoView = (VideoView) dialog.findViewById(R.id.simpleVideo);
//
//        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.roadmap));
//        videoView.start();
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mainLayout.setVisibility(View.VISIBLE);
//                dialog.dismiss();
//                startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
//            }
//        });

        SharedPreferences prefsMap = getSharedPreferences("prefsMap", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsMap.edit();
        editor.putBoolean("firstStartMap", false);
        editor.apply();

//        dialog.show();

    }

    //get & display current user's profile
    @Override
    protected void onStart() {
        super.onStart();

        //signin authentication and redirect to landing page
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Concentration_Daily.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    //get user credentials & convert it back to string

    private void userLogin() {
        //get user email and password
        String email = emailAddress.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        //check email is entered
        if (email.isEmpty()) {
            emailAddress.setError("Email is required");
            emailAddress.requestFocus();
            return;
        }
        //check whether email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Enter a valid email!");
            emailAddress.requestFocus();
            return;
        }
        //check whether password is entered
        if (password.isEmpty()) {
            passwordTxt.setError("Password is Required");
            passwordTxt.requestFocus();
            return;
        }
        //check passoword length is more than 8 characters
        if (password.length() < 8) {
            passwordTxt.setError("Minimum Password length should be 8 characters!");
            passwordTxt.requestFocus();
            return;
        }

        Log.d("USERLOGIN", "----------------------A----------------------------");
        Log.d("USERLOGIN", "----------------------B----------------------------");
        Log.d("USERLOGIN", "----------------------C----------------------------");
        progressBar.setVisibility(View.GONE);
        //signin with email and password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

//                    Log.d("USERLOGIN", "----------------------E----------------------------");
//                    Log.d("USERLOGIN", "----------------------F----------------------------");
//                    Log.d("USERLOGIN", "----------------------G----------------------------");
                    //check whether user details are correct and authenticate with firebase userdata and redirect user to landing page
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    SharedPreferences prefsMap = getSharedPreferences("prefsMap", MODE_PRIVATE);
                    boolean firstStartMap = prefsMap.getBoolean("firstStartMap", true);

                    if (firstStartMap) {
                        openInstructionsPopUp();
                        Log.d("Fisrt SignIn", "Working");

                    } else {
                        startActivity(new Intent(SignInActivity.this, Concentration_Daily.class));
                    }

                     /*if (user.isEmailVerified()) {
                        // redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account",Toast.LENGTH_LONG)
                                .show();
                    }*/

                } else {
                    Toast.makeText(SignInActivity.this, "LogIn Failed! Please check your username or password again", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }
}