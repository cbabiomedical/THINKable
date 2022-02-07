package com.example.thinkableproject.pianotiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String MAXTILES = "MaxTiles";
    FirebaseFirestore database;
    User user;
    FirebaseUser mUser;
    int updatedCoins;
    int points;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main4);
        Intent get = getIntent();
        database = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        final int maxtiles = get.getIntExtra(Main4Activity.TILES, 0);
        System.out.println(maxtiles);
//        Intent i = new Intent(this,Main2Activity.class);
//        startActivity(i);
        Random r = new Random(SystemClock.uptimeMillis());
        final int a1 = r.nextInt(10000000) % 4;
        final int a2 = r.nextInt(10000000) % 4;
        final int a3 = r.nextInt(10000000) % 4;
        final int a4 = r.nextInt(10000000) % 4;
        final AppCompatButton b1, b2, b3, b4, b6, b7, b8, b9, b11, b12, b13, b14, b16, b17, b18, b19;
        b1 = (AppCompatButton) findViewById(R.id.btn1);
        b2 = (AppCompatButton) findViewById(R.id.btn2);
        b3 = (AppCompatButton) findViewById(R.id.btn3);
        b4 = (AppCompatButton) findViewById(R.id.btn4);
        b6 = (AppCompatButton) findViewById(R.id.btn6);
        b7 = (AppCompatButton) findViewById(R.id.btn7);
        b8 = (AppCompatButton) findViewById(R.id.btn8);
        b9 = (AppCompatButton) findViewById(R.id.btn9);
        b11 = (AppCompatButton) findViewById(R.id.btn11);
        b12 = (AppCompatButton) findViewById(R.id.btn12);
        b13 = (AppCompatButton) findViewById(R.id.btn13);
        b14 = (AppCompatButton) findViewById(R.id.btn14);
        b16 = (AppCompatButton) findViewById(R.id.btn16);
        b17 = (AppCompatButton) findViewById(R.id.btn17);
        b18 = (AppCompatButton) findViewById(R.id.btn18);
        b19 = (AppCompatButton) findViewById(R.id.btn19);
        switch (a1) {
            case 0:
                b1.setBackgroundResource(R.color.Black);
                break;
            case 1:
                b2.setBackgroundResource(R.color.Black);
                break;
            case 2:
                b3.setBackgroundResource(R.color.Black);
                break;
            case 3:
                b4.setBackgroundResource(R.color.Black);
                break;
        }
        switch (a2) {
            case 0:
                b6.setBackgroundResource(R.color.Black);
                break;
            case 1:
                b7.setBackgroundResource(R.color.Black);
                break;
            case 2:
                b8.setBackgroundResource(R.color.Black);
                break;
            case 3:
                b9.setBackgroundResource(R.color.Black);
                break;
        }
        switch (a3) {
            case 0:
                b11.setBackgroundResource(R.color.Black);
                b11.setText("START");
                break;
            case 1:
                b12.setBackgroundResource(R.color.Black);
                b12.setText("START");
                break;
            case 2:
                b13.setBackgroundResource(R.color.Black);
                b13.setText("START");
                break;
            case 3:
                b14.setBackgroundResource(R.color.Black);
                b14.setText("START");
                break;
        }

        SharedPreferences prefsCountPiano = getSharedPreferences("prefsCountPiano", MODE_PRIVATE);
        int firstStartCountPiano = prefsCountPiano.getInt("firstStartCountPiano", 0);


        SharedPreferences sh = getSharedPreferences("prefsCountPiano", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        a = sh.getInt("firstStartCountPiano", 0);

// We can then use the data
        Log.d("A Count", String.valueOf(a));

        int b = a + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsCountPiano", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartCountPiano", b);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsCountPiano", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int c = sha.getInt("firstStartCountPiano", 0);


        Log.d("A Count2", String.valueOf(a1));

//

        final Intent i = new Intent(this, Main2Activity.class);
        View.OnClickListener clickListener = new View.OnClickListener() {
            int c1 = a1, c2 = a2, c3 = a3, c4 = a4;
            Random r = new Random(SystemClock.uptimeMillis());
            Double score;
            int tiles = -1;
            double startTime = 0;


            @Override
            public void onClick(View v) {

                if (tiles == -1) {
                    ((Button) v).setText("");
                    startTime = SystemClock.uptimeMillis();
                }
                score = (SystemClock.uptimeMillis() - startTime) / 1000;
                tiles++;
                if (tiles == maxtiles) {
                    i.putExtra("SCORE", String.valueOf(score));
                    i.putExtra(MAXTILES, String.valueOf(maxtiles));
                    Log.d("Complete", String.valueOf(score));

                    if (maxtiles == 50) {
                        if (score < 15) {
                            points = 50;
                        } else if (score < 25) {
                            points = 30;
                        } else if (points < 40) {
                            points = 15;
                        } else {
                            points = 5;
                        }
                    } else if (maxtiles == 100) {
                        if (score < 35) {
                            points = 50;
                        } else if (score < 50) {
                            points = 30;
                        } else if (points < 60) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 150) {
                        if (score < 50) {
                            points = 50;
                        } else if (score < 65) {
                            points = 30;
                        } else if (points < 75) {
                            points = 15;
                        } else {
                            points = 5;
                        }
                    } else if (maxtiles == 200) {
                        if (score < 70) {
                            points = 50;
                        } else if (score < 100) {
                            points = 30;
                        } else if (points < 120) {
                            points = 15;
                        } else {
                            points = 5;
                        }
                    } else if (maxtiles == 250) {
                        if (score < 100) {
                            points = 50;
                        } else if (score < 130) {
                            points = 30;
                        } else if (points < 150) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 300) {
                        if (score < 110) {
                            points = 50;
                        } else if (score < 140) {
                            points = 30;
                        } else if (points < 170) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 350) {
                        if (score < 130) {
                            points = 50;
                        } else if (score < 160) {
                            points = 30;
                        } else if (points < 180) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 400) {
                        if (score < 150) {
                            points = 50;
                        } else if (score < 190) {
                            points = 30;
                        } else if (points < 210) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 450) {
                        if (score < 170) {
                            points = 50;
                        } else if (score < 200) {
                            points = 30;
                        } else if (points < 220) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    } else if (maxtiles == 100) {
                        if (score < 200) {
                            points = 50;
                        } else if (score < 240) {
                            points = 30;
                        } else if (points < 260) {
                            points = 15;
                        } else {
                            points = 5;
                        }

                    }
                    startActivity(i);
                    database.collection("users")
                            .document(mUser.getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                            Log.d("Current Coins", String.valueOf(user.getCoins()));
                            Log.d("High Score Inside", String.valueOf(points));
                            updatedCoins = (int) (user.getCoins() + points);
                            Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                            database.collection("users").document(mUser.getUid())
                                    .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                        Toast.makeText(ColorPatternGame.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
//

                }
                //Log.d(TAG, "onClick: has been called");
                switch (v.getId()) {
                    case R.id.btn11:
                        switch (c3) {
                            case 0:
                                v.setBackgroundResource(R.color.Grey);
                                c4 = c3;
                                c3 = c2;
                                c2 = c1;
                                if (tiles <= maxtiles - 2) {
                                    c1 = r.nextInt((int) SystemClock.uptimeMillis()) % 4;
                                } else {
                                    c1 = 4;
                                }
                                switch (c1) {
                                    case 0:
                                        b1.setBackgroundResource(R.drawable.con1);
                                        b2.setBackgroundResource(R.drawable.con2);
                                        b3.setBackgroundResource(R.drawable.con3);
                                        b4.setBackgroundResource(R.drawable.con4);
                                        break;
                                    case 1:
                                        b2.setBackgroundResource(R.drawable.con46);
                                        b1.setBackgroundResource(R.drawable.con5);
                                        b3.setBackgroundResource(R.drawable.con6);
                                        b4.setBackgroundResource(R.drawable.con7);
                                        break;
                                    case 2:
                                        b3.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con8);
                                        b1.setBackgroundResource(R.drawable.con9);
                                        b4.setBackgroundResource(R.drawable.con10);
                                        break;
                                    case 3:
                                        b4.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con11);
                                        b3.setBackgroundResource(R.drawable.con12);
                                        b1.setBackgroundResource(R.drawable.con13);
                                        break;
                                    case 4:
                                        b4.setBackgroundResource(R.drawable.con14);
                                        b2.setBackgroundResource(R.drawable.con15);
                                        b3.setBackgroundResource(R.drawable.con16);
                                        b1.setBackgroundResource(R.drawable.con17);
                                        break;
                                }
                                switch (c2) {
                                    case 0:
                                        b6.setBackgroundResource(R.drawable.con46);
                                        b7.setBackgroundResource(R.drawable.con18);
                                        b8.setBackgroundResource(R.drawable.con19);
                                        b9.setBackgroundResource(R.drawable.con20);
                                        break;
                                    case 1:
                                        b7.setBackgroundResource(R.drawable.con46);
                                        b8.setBackgroundResource(R.drawable.con21);
                                        b9.setBackgroundResource(R.drawable.con22);
                                        b6.setBackgroundResource(R.drawable.con23);
                                        break;
                                    case 2:
                                        b8.setBackgroundResource(R.drawable.con46);
                                        b9.setBackgroundResource(R.drawable.con24);
                                        b6.setBackgroundResource(R.drawable.con25);
                                        b7.setBackgroundResource(R.drawable.con26);
                                        break;
                                    case 3:
                                        b9.setBackgroundResource(R.drawable.con46);
                                        b6.setBackgroundResource(R.drawable.con27);
                                        b7.setBackgroundResource(R.drawable.con28);
                                        b8.setBackgroundResource(R.drawable.con29);
                                        break;
                                    case 4:
                                        b9.setBackgroundResource(R.drawable.con30);
                                        b6.setBackgroundResource(R.drawable.con31);
                                        b7.setBackgroundResource(R.drawable.con32);
                                        b8.setBackgroundResource(R.drawable.con33);
                                        break;
                                }
                                switch (c3) {
                                    case 0:
                                        b11.setBackgroundResource(R.drawable.con46);
                                        b12.setBackgroundResource(R.drawable.con34);
                                        b13.setBackgroundResource(R.drawable.con35);
                                        b14.setBackgroundResource(R.drawable.con36);
                                        break;
                                    case 1:
                                        b11.setBackgroundResource(R.drawable.con37);
                                        b12.setBackgroundResource(R.drawable.con46);
                                        b13.setBackgroundResource(R.drawable.con38);
                                        b14.setBackgroundResource(R.drawable.con39);
                                        break;
                                    case 2:
                                        b11.setBackgroundResource(R.drawable.con40);
                                        b12.setBackgroundResource(R.drawable.con41);
                                        b13.setBackgroundResource(R.drawable.con46);
                                        b14.setBackgroundResource(R.drawable.con42);
                                        break;
                                    case 3:
                                        b11.setBackgroundResource(R.drawable.con43);
                                        b12.setBackgroundResource(R.drawable.con44);
                                        b13.setBackgroundResource(R.drawable.con45);
                                        b14.setBackgroundResource(R.drawable.con46);
                                        break;
                                    case 4:
                                        b11.setBackgroundResource(R.drawable.con2);
                                        b12.setBackgroundResource(R.drawable.con3);
                                        b13.setBackgroundResource(R.drawable.con4);
                                        b14.setBackgroundResource(R.drawable.con5);
                                        break;
                                }
                                switch (c4) {
                                    case 0:
                                        b16.setBackgroundResource(R.color.Grey);
                                        b17.setBackgroundResource(R.drawable.con6);
                                        b18.setBackgroundResource(R.drawable.con7);
                                        b19.setBackgroundResource(R.drawable.con8);
                                        break;
                                    case 1:
                                        b17.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con9);
                                        b18.setBackgroundResource(R.drawable.con10);
                                        b19.setBackgroundResource(R.drawable.con11);
                                        break;
                                    case 2:
                                        b18.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con12);
                                        b17.setBackgroundResource(R.drawable.con13);
                                        b19.setBackgroundResource(R.drawable.con14);
                                        break;
                                    case 3:
                                        b19.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con20);
                                        b17.setBackgroundResource(R.drawable.con21);
                                        b18.setBackgroundResource(R.drawable.con22);
                                        break;
                                    case 4:
                                        b19.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con23);
                                        b17.setBackgroundResource(R.drawable.con24);
                                        b18.setBackgroundResource(R.drawable.con25);
                                        break;
                                }
                                break;
                            default:
                                break;
                            case 1:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 2:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 3:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                        }
                        break;
                    case R.id.btn12:
                        switch (c3) {
                            case 1:
                                c4 = c3;
                                c3 = c2;
                                c2 = c1;
                                if (tiles <= maxtiles - 2) {
                                    c1 = r.nextInt((int) SystemClock.uptimeMillis()) % 4;
                                } else {
                                    c1 = 4;
                                }
                                switch (c1) {
                                    case 0:
                                        b1.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con1);
                                        b3.setBackgroundResource(R.drawable.con2);
                                        b4.setBackgroundResource(R.drawable.con3);
                                        break;
                                    case 1:
                                        b2.setBackgroundResource(R.drawable.con46);
                                        b1.setBackgroundResource(R.drawable.con4);
                                        b3.setBackgroundResource(R.drawable.con5);
                                        b4.setBackgroundResource(R.drawable.con6);
                                        break;
                                    case 2:
                                        b3.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con7);
                                        b1.setBackgroundResource(R.drawable.con8);
                                        b4.setBackgroundResource(R.drawable.con9);
                                        break;
                                    case 3:
                                        b4.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con10);
                                        b3.setBackgroundResource(R.drawable.con11);
                                        b1.setBackgroundResource(R.drawable.con12);
                                        break;
                                    case 4:
                                        b4.setBackgroundResource(R.drawable.con13);
                                        b2.setBackgroundResource(R.drawable.con14);
                                        b3.setBackgroundResource(R.drawable.con15);
                                        b1.setBackgroundResource(R.drawable.con16);
                                        break;
                                }
                                switch (c2) {
                                    case 0:
                                        b6.setBackgroundResource(R.drawable.con46);
                                        b7.setBackgroundResource(R.drawable.con17);
                                        b8.setBackgroundResource(R.drawable.con18);
                                        b9.setBackgroundResource(R.drawable.con19);

                                        break;
                                    case 1:
                                        b7.setBackgroundResource(R.drawable.con46);
                                        b8.setBackgroundResource(R.drawable.con20);
                                        b9.setBackgroundResource(R.drawable.con21);
                                        b6.setBackgroundResource(R.drawable.con22);
                                        break;
                                    case 2:
                                        b8.setBackgroundResource(R.drawable.con46);
                                        b9.setBackgroundResource(R.drawable.con23);
                                        b6.setBackgroundResource(R.drawable.con24);
                                        b7.setBackgroundResource(R.drawable.con25);
                                        break;
                                    case 3:
                                        b9.setBackgroundResource(R.drawable.con46);
                                        b6.setBackgroundResource(R.drawable.con26);
                                        b7.setBackgroundResource(R.drawable.con27);
                                        b8.setBackgroundResource(R.drawable.con28);
                                        break;
                                    case 4:
                                        b9.setBackgroundResource(R.drawable.con29);
                                        b6.setBackgroundResource(R.drawable.con30);
                                        b7.setBackgroundResource(R.drawable.con31);
                                        b8.setBackgroundResource(R.drawable.con32);
                                        break;
                                }
                                switch (c3) {
                                    case 0:
                                        b11.setBackgroundResource(R.drawable.con46);
                                        b12.setBackgroundResource(R.drawable.con33);
                                        b13.setBackgroundResource(R.drawable.con34);
                                        b14.setBackgroundResource(R.drawable.con35);
                                        break;
                                    case 1:
                                        b11.setBackgroundResource(R.drawable.con36);
                                        b12.setBackgroundResource(R.drawable.con46);
                                        b13.setBackgroundResource(R.drawable.con37);
                                        b14.setBackgroundResource(R.drawable.con38);
                                        break;
                                    case 2:
                                        b11.setBackgroundResource(R.drawable.con39);
                                        b12.setBackgroundResource(R.drawable.con40);
                                        b13.setBackgroundResource(R.drawable.con46);
                                        b14.setBackgroundResource(R.drawable.con41);
                                        break;
                                    case 3:
                                        b11.setBackgroundResource(R.drawable.con42);
                                        b12.setBackgroundResource(R.drawable.con43);
                                        b13.setBackgroundResource(R.drawable.con44);
                                        b14.setBackgroundResource(R.drawable.con46);
                                        break;
                                    case 4:
                                        b11.setBackgroundResource(R.drawable.con45);
                                        b12.setBackgroundResource(R.drawable.con2);
                                        b13.setBackgroundResource(R.drawable.con3);
                                        b14.setBackgroundResource(R.drawable.con4);
                                        break;
                                }
                                switch (c4) {
                                    case 0:
                                        b16.setBackgroundResource(R.color.Grey);
                                        b17.setBackgroundResource(R.drawable.con5);
                                        b18.setBackgroundResource(R.drawable.con6);
                                        b19.setBackgroundResource(R.drawable.con7);
                                        break;
                                    case 1:
                                        b17.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con8);
                                        b18.setBackgroundResource(R.drawable.con9);
                                        b19.setBackgroundResource(R.drawable.con10);
                                        break;
                                    case 2:
                                        b18.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con11);
                                        b17.setBackgroundResource(R.drawable.con12);
                                        b19.setBackgroundResource(R.drawable.con13);
                                        break;
                                    case 3:
                                        b19.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con20);
                                        b17.setBackgroundResource(R.drawable.con21);
                                        b18.setBackgroundResource(R.drawable.con22);
                                        break;
                                }
                                break;
                            default:
                                break;
                            case 0:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 2:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 3:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                        }
                        break;
                    case R.id.btn13:
                        switch (c3) {
                            case 2:
                                c4 = c3;
                                c3 = c2;
                                c2 = c1;
                                if (tiles <= maxtiles - 2) {
                                    c1 = r.nextInt((int) SystemClock.uptimeMillis()) % 4;
                                } else {
                                    c1 = 4;
                                }
                                switch (c1) {
                                    case 0:
                                        b1.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con1);
                                        b3.setBackgroundResource(R.drawable.con2);
                                        b4.setBackgroundResource(R.drawable.con3);
                                        break;
                                    case 1:
                                        b2.setBackgroundResource(R.drawable.con46);
                                        b1.setBackgroundResource(R.drawable.con4);
                                        b3.setBackgroundResource(R.drawable.con5);
                                        b4.setBackgroundResource(R.drawable.con6);
                                        break;
                                    case 2:
                                        b3.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con7);
                                        b1.setBackgroundResource(R.drawable.con8);
                                        b4.setBackgroundResource(R.drawable.con9);
                                        break;
                                    case 3:
                                        b4.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con10);
                                        b3.setBackgroundResource(R.drawable.con11);
                                        b1.setBackgroundResource(R.drawable.con12);
                                        break;
                                    case 4:
                                        b4.setBackgroundResource(R.drawable.con13);
                                        b2.setBackgroundResource(R.drawable.con14);
                                        b3.setBackgroundResource(R.drawable.con15);
                                        b1.setBackgroundResource(R.drawable.con16);
                                        break;
                                }
                                switch (c2) {
                                    case 0:
                                        b6.setBackgroundResource(R.drawable.con46);
                                        b7.setBackgroundResource(R.drawable.con17);
                                        b8.setBackgroundResource(R.drawable.con18);
                                        b9.setBackgroundResource(R.drawable.con19);
                                        break;
                                    case 1:
                                        b7.setBackgroundResource(R.drawable.con46);
                                        b8.setBackgroundResource(R.drawable.con20);
                                        b9.setBackgroundResource(R.drawable.con21);
                                        b6.setBackgroundResource(R.drawable.con22);
                                        break;
                                    case 2:
                                        b8.setBackgroundResource(R.drawable.con46);
                                        b9.setBackgroundResource(R.drawable.con23);
                                        b6.setBackgroundResource(R.drawable.con24);
                                        b7.setBackgroundResource(R.drawable.con25);
                                        break;
                                    case 3:
                                        b9.setBackgroundResource(R.drawable.con46);
                                        b6.setBackgroundResource(R.drawable.con26);
                                        b7.setBackgroundResource(R.drawable.con27);
                                        b8.setBackgroundResource(R.drawable.con28);
                                        break;
                                    case 4:
                                        b9.setBackgroundResource(R.drawable.con29);
                                        b6.setBackgroundResource(R.drawable.con30);
                                        b7.setBackgroundResource(R.drawable.con31);
                                        b8.setBackgroundResource(R.drawable.con32);
                                        break;
                                }
                                switch (c3) {
                                    case 0:
                                        b11.setBackgroundResource(R.drawable.con46);
                                        b12.setBackgroundResource(R.drawable.con33);
                                        b13.setBackgroundResource(R.drawable.con34);
                                        b14.setBackgroundResource(R.drawable.con35);
                                        break;
                                    case 1:
                                        b11.setBackgroundResource(R.drawable.con36);
                                        b12.setBackgroundResource(R.drawable.con46);
                                        b13.setBackgroundResource(R.drawable.con37);
                                        b14.setBackgroundResource(R.drawable.con38);
                                        break;
                                    case 2:
                                        b11.setBackgroundResource(R.drawable.con39);
                                        b12.setBackgroundResource(R.drawable.con40);
                                        b13.setBackgroundResource(R.drawable.con46);
                                        b14.setBackgroundResource(R.drawable.con41);
                                        break;
                                    case 3:
                                        b11.setBackgroundResource(R.drawable.con42);
                                        b12.setBackgroundResource(R.drawable.con43);
                                        b13.setBackgroundResource(R.drawable.con44);
                                        b14.setBackgroundResource(R.drawable.con46);
                                        break;
                                    case 4:
                                        b11.setBackgroundResource(R.drawable.con45);
                                        b12.setBackgroundResource(R.drawable.con2);
                                        b13.setBackgroundResource(R.drawable.con3);
                                        b14.setBackgroundResource(R.drawable.con4);
                                        break;
                                }
                                switch (c4) {
                                    case 0:
                                        b16.setBackgroundResource(R.color.Grey);
                                        b17.setBackgroundResource(R.drawable.con5);
                                        b18.setBackgroundResource(R.drawable.con6);
                                        b19.setBackgroundResource(R.drawable.con7);
                                        break;
                                    case 1:
                                        b17.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con8);
                                        b18.setBackgroundResource(R.drawable.con9);
                                        b19.setBackgroundResource(R.drawable.con10);
                                        break;
                                    case 2:
                                        b18.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con20);
                                        b17.setBackgroundResource(R.drawable.con21);
                                        b19.setBackgroundResource(R.drawable.con22);
                                        break;
                                    case 3:
                                        b19.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con23);
                                        b17.setBackgroundResource(R.drawable.con24);
                                        b18.setBackgroundResource(R.drawable.con25);
                                        break;
                                }
                                break;
                            default:
                                break;
                            case 1:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 0:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 3:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                        }
                        break;
                    case R.id.btn14:
                        switch (c3) {
                            case 3:
                                c4 = c3;
                                c3 = c2;
                                c2 = c1;
                                if (tiles <= maxtiles - 2) {
                                    c1 = r.nextInt((int) SystemClock.uptimeMillis()) % 4;
                                } else {
                                    c1 = 4;
                                }
                                switch (c1) {
                                    case 0:
                                        b1.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con1);
                                        b3.setBackgroundResource(R.drawable.con2);
                                        b4.setBackgroundResource(R.drawable.con3);
                                        break;
                                    case 1:
                                        b2.setBackgroundResource(R.drawable.con46);
                                        b1.setBackgroundResource(R.drawable.con4);
                                        b3.setBackgroundResource(R.drawable.con5);
                                        b4.setBackgroundResource(R.drawable.con6);
                                        break;
                                    case 2:
                                        b3.setBackgroundResource(R.drawable.con46);
                                        b2.setBackgroundResource(R.drawable.con7);
                                        b1.setBackgroundResource(R.drawable.con8);
                                        b4.setBackgroundResource(R.drawable.con9);
                                        break;
                                    case 3:
                                        b4.setBackgroundResource(R.color.Black);
                                        b2.setBackgroundResource(R.drawable.con10);
                                        b3.setBackgroundResource(R.drawable.con11);
                                        b1.setBackgroundResource(R.drawable.con12);
                                        break;
                                    case 4:
                                        b4.setBackgroundResource(R.drawable.con13);
                                        b2.setBackgroundResource(R.drawable.con14);
                                        b3.setBackgroundResource(R.drawable.con15);
                                        b1.setBackgroundResource(R.drawable.con16);
                                        break;
                                }
                                switch (c2) {
                                    case 0:
                                        b6.setBackgroundResource(R.drawable.con46);
                                        b7.setBackgroundResource(R.drawable.con17);
                                        b8.setBackgroundResource(R.drawable.con18);
                                        b9.setBackgroundResource(R.drawable.con19);
                                        break;
                                    case 1:
                                        b7.setBackgroundResource(R.drawable.con46);
                                        b8.setBackgroundResource(R.drawable.con20);
                                        b9.setBackgroundResource(R.drawable.con21);
                                        b6.setBackgroundResource(R.drawable.con22);
                                        break;
                                    case 2:
                                        b8.setBackgroundResource(R.drawable.con46);
                                        b9.setBackgroundResource(R.drawable.con23);
                                        b6.setBackgroundResource(R.drawable.con24);
                                        b7.setBackgroundResource(R.drawable.con25);
                                        break;
                                    case 3:
                                        b9.setBackgroundResource(R.drawable.con46);
                                        b6.setBackgroundResource(R.drawable.con26);
                                        b7.setBackgroundResource(R.drawable.con27);
                                        b8.setBackgroundResource(R.drawable.con28);
                                        break;
                                    case 4:
                                        b9.setBackgroundResource(R.drawable.con29);
                                        b6.setBackgroundResource(R.drawable.con30);
                                        b7.setBackgroundResource(R.drawable.con31);
                                        b8.setBackgroundResource(R.drawable.con32);
                                        break;
                                    default:
                                        break;
                                }
                                switch (c3) {
                                    case 0:
                                        b11.setBackgroundResource(R.drawable.con46);
                                        b12.setBackgroundResource(R.drawable.con33);
                                        b13.setBackgroundResource(R.drawable.con34);
                                        b14.setBackgroundResource(R.drawable.con35);
                                        break;
                                    case 1:
                                        b11.setBackgroundResource(R.drawable.con36);
                                        b12.setBackgroundResource(R.drawable.con46);
                                        b13.setBackgroundResource(R.drawable.con37);
                                        b14.setBackgroundResource(R.drawable.con38);
                                        break;
                                    case 2:
                                        b11.setBackgroundResource(R.drawable.con39);
                                        b12.setBackgroundResource(R.drawable.con40);
                                        b13.setBackgroundResource(R.drawable.con46);
                                        b14.setBackgroundResource(R.drawable.con41);
                                        break;
                                    case 3:
                                        b11.setBackgroundResource(R.drawable.con42);
                                        b12.setBackgroundResource(R.drawable.con43);
                                        b13.setBackgroundResource(R.drawable.con41);
                                        b14.setBackgroundResource(R.drawable.con46);
                                        break;
                                    case 4:
                                        b11.setBackgroundResource(R.drawable.con45);
                                        b12.setBackgroundResource(R.drawable.con2);
                                        b13.setBackgroundResource(R.drawable.con3);
                                        b14.setBackgroundResource(R.drawable.con4);
                                        break;
                                    default:
                                        break;
                                }
                                switch (c4) {
                                    case 0:
                                        b16.setBackgroundResource(R.color.Grey);
                                        b17.setBackgroundResource(R.drawable.con5);
                                        b18.setBackgroundResource(R.drawable.con6);
                                        b19.setBackgroundResource(R.drawable.con7);
                                        break;
                                    case 1:
                                        b17.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con8);
                                        b18.setBackgroundResource(R.drawable.con9);
                                        b19.setBackgroundResource(R.drawable.con10);
                                        break;
                                    case 2:
                                        b18.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con11);
                                        b17.setBackgroundResource(R.drawable.con12);
                                        b19.setBackgroundResource(R.drawable.con13);
                                        break;
                                    case 3:
                                        b19.setBackgroundResource(R.color.Grey);
                                        b16.setBackgroundResource(R.drawable.con20);
                                        b17.setBackgroundResource(R.drawable.con21);
                                        b18.setBackgroundResource(R.drawable.con22);
                                        break;
                                }
                                break;
                            default:
                                break;
                            case 1:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);

                                break;
                            case 2:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                            case 0:
                                i.putExtra("SCORE", "FAILED");
                                startActivity(i);
                                break;
                        }
                        break;
                }
            }
        };
        b11.setOnClickListener(clickListener);
        b12.setOnClickListener(clickListener);
        b13.setOnClickListener(clickListener);
        b14.setOnClickListener(clickListener);
    }
}


