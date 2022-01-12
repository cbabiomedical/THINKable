package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thinkableproject.adapters.MusicAdapter;
import com.example.thinkableproject.sample.MusicModelClass;
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
import com.google.firebase.messaging.FirebaseMessaging;;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Music extends AppCompatActivity implements MusicAdapter.OnNoteListner {
    RecyclerView recyclerView;
    ArrayList<MusicModelClass> musicList;
    MusicAdapter adapter;
    Dialog dialogmusic;
    String selected_time;
    LinearLayoutManager layoutManager;
    //    int time;
    FirebaseUser mUser;
    ImageView information;
    View c1, c2;
    int color;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mRequestQue = Volley.newRequestQueue(this);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        dialogmusic = new Dialog(this);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        information = findViewById(R.id.musicInfo);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        sendNotification();
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


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);


                } else {
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
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstructionsPopUp();
            }
        });
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
           openInstructionsPopUp();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.exercise);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Relaxation_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
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
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

//        Log.d("Time of Music",String.valueOf(MusicAdapter.getTimeOfmusic()));
        recyclerView = findViewById(R.id.recycler_view);
//        Spinner dropdown_time = (Spinner) findViewById(R.id.spinner2);
//        String[] items = new String[]{"Audio track duration is: 1 min", "Audio track duration is: 1.5 min", "Audio track duration is: 2 min", "Audio track duration is: 2.5 min", "Audio track duration is: 3 min"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown_time.setAdapter(adapter1);

//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
//                new IntentFilter("time"));
//        dropdown_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selected_time = parent.getItemAtPosition(position).toString();
//                if (position == 0) {
//                    time = 60000;
//                    Log.d("TIME", String.valueOf(time));
//                } else if (position == 1) {
//                    time = 90000;
//                    Log.d("TIME", String.valueOf(time));
//                } else if (position == 2) {
//                    time = 120000;
//                } else if (position == 3) {
//                    time = 150000;
//                } else {
//                    time = 180000;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        MusicAdapter musicAdapter=new MusicAdapter();
//        final int timeOfMus= MusicAdapter.getViewHolder().getTimeOfMusic();
//        Log.d("Time", String.valueOf(timeOfMus));


        musicList = new ArrayList<>();
//        musicList.add(new MusicModelClass(R.drawable.music1, "Chilled Acoustic", "1", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/chilled-acoustic-indie-folk-instrumental-background-music-for-videos-5720.mp3?alt=media&token=c61afc5b-1833-47a0-af5c-645872eae852"));
//        musicList.add(new MusicModelClass(R.drawable.music1, "Melody Of Nature", "2", "https://firebasestorage.googleapis.com/v0/b/thinkableproject-15f91.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=241ad528-0581-44ec-b415-93684ebcee9c"));
//        HashMap<String, Object> songs = new HashMap<>();
//        songs.put("songList", musicList);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Music");
//        reference.setValue(songs);
        FirebaseMessaging.getInstance().subscribeToTopic("Music")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("TAG", msg);
                        Toast.makeText(Music.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        initData();


    }

    private void showStartDialog() {
        new AlertDialog.Builder(this).setTitle("One Time Dialog").setMessage("This should appear only once").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

//    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String ItemName = intent.getStringExtra("pass");
//            Log.d("time", ItemName);
//            Toast.makeText(Music.this, ItemName + " ", Toast.LENGTH_SHORT).show();
//        }
//    };

    private void initData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs_Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDatasnapshot : snapshot.getChildren()) {
                    MusicModelClass post = postDatasnapshot.getValue(MusicModelClass.class);
                    Log.d("Post", String.valueOf(post));
                    musicList.add(post);

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                Log.d("List", String.valueOf(musicList));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        adapter = new MusicAdapter(musicList, getApplicationContext(), this::onNoteClick);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onNoteClick(int position) {

        musicList.get(position);
        String songName = musicList.get(position).getName();
        Log.d("Name", songName);
        String url = musicList.get(position).getSongTitle1();
        Log.d("SongURL", url);
        String image = musicList.get(position).getImageUrl();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), MusicPlayer.class).putExtra("url", url).putExtra("name", songName).putExtra("image", image));
    }

    private void sendNotification() {
        JSONObject mainobj = new JSONObject();
        try {
            mainobj.put("to", "/topics/" + "news");
            JSONObject notificatioinObj = new JSONObject();
            notificatioinObj.put("title", "Music");
            notificatioinObj.put("body", "New music track was added!!");
            mainobj.put("notification", notificatioinObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainobj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }


            }

            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization",
                            "key=AAAAYTGeHmQ:APA91bGcM2zXzcA0hv5ssW8kE4BTBmlsK5uhHz6UQm2Ur8vuqUoZDErFKUvX7m_-S9m7cmIe5picpG79jk4z1UfB4YuZ3shx1fpEAmh0YzA4L1x85pOlgfNP4bPorTE70ORGqbXdemLq");
                    return header;
                }
            };
            mRequestQue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        openInstructionsPopUp();
    }

    private void openInstructionsPopUp() {
        Button ok;
        View c1, c2;
//        ImageView musicAudioInstructions;
        dialogmusic.setContentView(R.layout.music_activity_popup);
        ok = (Button) dialogmusic.findViewById(R.id.click);
        c1=(View)dialogmusic.findViewById(R.id.c1);
        c2=(View)dialogmusic.findViewById(R.id.c2);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor PopUp", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {  //light theme
                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
                    c2.setVisibility(View.VISIBLE);
                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);


                } else {
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
//        musicAudioInstructions=(ImageView) dialogmusic.findViewById(R.id.playAudio);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogmusic.dismiss();
            }
        });

//        musicAudioInstructions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Music.this,"Play Audio Button was clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

        dialogmusic.show();
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();


    }
}