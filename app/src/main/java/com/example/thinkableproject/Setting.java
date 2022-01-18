package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.Help;
import com.example.SettingsPreference;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

public class Setting extends AppCompatActivity {
    Dialog dialogcd;
    ImageButton buttonpro, accountsettings1, changepassword1, location2, theme2, preferences1, notifications2, help3, logout, contactus;
    ImageView upgradetopro;
    private Uri filePath;
    private StorageReference storageReference;
    FirebaseUser mUser;
    View c1, c2,c3;
    private int color;


    public int getColor() {
        Log.d("Return Color", String.valueOf(color));
        return color;

    }

    public void setColor(int color) {
        Log.d("Set Color", String.valueOf(color));
        this.color = color;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Initialize and Assign Buttons

        accountsettings1 = findViewById(R.id.accountsettings1);
        changepassword1 = findViewById(R.id.changepassword1);
        location2 = findViewById(R.id.location2);
        theme2 = findViewById(R.id.themebtn);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3=findViewById(R.id.c3);
        contactus = findViewById(R.id.contactus1);
        dialogcd = new Dialog(this);

        preferences1 = findViewById(R.id.preferences1);

        help3 = findViewById(R.id.help3);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logout = findViewById(R.id.logout);
        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {  //light theme
                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
                    c2.setVisibility(View.VISIBLE);
                   


                }  else if (color ==1 ) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                   


                }else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                       
                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                      

                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);



                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), MainActivityK.class);
//                view.getContext().startActivity(intent);;
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Setting.this, SignInActivity.class));
            }
        });


        accountsettings1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);

            }
        });

        changepassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intentr1);
            }
        });

        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), UserLocation.class);
                startActivity(intentr1);
            }
        });


        preferences1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), SettingsPreference.class);
                startActivity(intentr1);
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Help.class));

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ContactUs.class));
            }
        });


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.settings);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });
    }


    public void gotoPopup9(View view) {

        AppCompatButton c1, c2,c3;
        dialogcd.setContentView(R.layout.theme_popup);
        HashMap hashMap = new HashMap();

        c1 = (AppCompatButton) dialogcd.findViewById(R.id.c1);
        c2 = dialogcd.findViewById(R.id.c2);
        c3 = dialogcd.findViewById(R.id.c3);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = 1;
                Log.d("Color", String.valueOf(color));

                hashMap.put("theme", color);
                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(Setting.this, "You changed Theme to Dark Theme", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogcd.cancel();
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = 2;
                Log.d("Color", String.valueOf(color));
                hashMap.put("theme", color);
                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(Setting.this, "You changed Theme to Light Theme", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogcd.cancel();

            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = 3;
                Log.d("Color", String.valueOf(color));
                hashMap.put("theme", color);
                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(Setting.this, "You changed Theme to Auto Theme Mode", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogcd.cancel();

            }
        });


        dialogcd.show();
    }
}