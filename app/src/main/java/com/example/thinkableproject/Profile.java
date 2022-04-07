package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    ImageView editProfile;
    RelativeLayout mainLayout;
    CircleImageView profilePic;
    TextView userName;
    FirebaseUser mUser;
    StorageReference storageReference;
    AppCompatButton calendar, aboutApp;
    AppCompatButton myFavourites, myDownloads, share;
    ImageView faceBook, instagram, twitter, youtube;
    Animation scaleUp, scaleDown;


    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editProfile = findViewById(R.id.edit);
        userName = findViewById(R.id.user);
        profilePic = findViewById(R.id.picture);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        calendar = findViewById(R.id.cal_reminder);
        myFavourites = findViewById(R.id.favourites);
        aboutApp = findViewById(R.id.about);
        myDownloads = findViewById(R.id.downloads);
        mainLayout=findViewById(R.id.mainLayout);

        share = findViewById(R.id.share);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {
                    mainLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.lightblue));


                } else {

                    mainLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.darkblue));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download this app";
                String sub = "https://www.facebook.com/login/";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.putExtra(Intent.EXTRA_TEXT, sub);
                startActivity(intent.createChooser(intent, "Share using"));
            }
        });

        share.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    share.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    share.startAnimation(scaleDown);
                }

                return false;
            }
        });
//        faceBook=findViewById(R.id.facebook);
//        instagram=findViewById(R.id.insta);
//        twitter=findViewById(R.id.twitter);
//        youtube=findViewById(R.id.youtube);


        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutApp.class));
            }
        });

        aboutApp.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    aboutApp.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    aboutApp.startAnimation(scaleDown);
                }

                return false;
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDetails.class);
                startActivity(intent);
            }
        });

        editProfile.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    editProfile.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    editProfile.startAnimation(scaleDown);
                }

                return false;
            }
        });
//        faceBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotUrl("https://www.facebook.com/login/");
//            }
//        });
        myFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
        myFavourites.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    myFavourites.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    myFavourites.startAnimation(scaleDown);
                }

                return false;
            }
        });
//        instagram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotUrlInstagram("https://www.instagram.com/?hl=en");
//            }
//        });
//        twitter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goUrlTwitter("https://twitter.com/i/flow/login");
//            }
//        });


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child(mUser.getUid()).child("profilePic.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    userName.setText(String.valueOf(dataSnapshot.child("userName").getValue()));
                }
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcalen = new Intent(getApplicationContext(), CalenderAndNotification.class);
                startActivity(intentcalen);
            }
        });
        calendar.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    calendar.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    calendar.startAnimation(scaleDown);
                }

                return false;
            }
        });
        myDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Downloads.class));
            }
        });
        myDownloads.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    myDownloads.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    myDownloads.startAnimation(scaleDown);
                }

                return false;
            }
        });
//        youtube.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                goUrlYoutube("https://www.youtube.com/");
//
//            }
//        });

    }

    private void goUrlYoutube(String s) {
        Uri uriYoutube = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uriYoutube));
    }

    private void goUrlTwitter(String s) {
        Uri uriTwitter = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uriTwitter));
    }


    private void gotUrlInstagram(String s) {
        Uri uriIsta = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uriIsta));
    }


    private void gotUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}