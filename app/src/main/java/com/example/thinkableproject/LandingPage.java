package com.example.thinkableproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class LandingPage extends AppCompatActivity {
    Button changepassword, logout, relaxationLanding, concentrationLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        concentrationLanding=findViewById(R.id.concentrationDaily);
        concentrationLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Concentration_Daily.class);
                startActivity(intent);
            }
        });
//      /  BottomNavigationView navView = findViewById(R.id.nav_view);
        relaxationLanding=findViewById(R.id.relaxationLanding);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        changepassword = findViewById(R.id.changepassword);
        logout = findViewById(R.id.logout);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(intent);
            }
        });
relaxationLanding.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Relaxation_Daily.class);
        startActivity(intent);
    }
});
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LandingPage.this,SignInActivity.class));
            }
        });
    }


    public void logoutfrmGgl(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intentSignotGgl=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intentSignotGgl);
    }
}