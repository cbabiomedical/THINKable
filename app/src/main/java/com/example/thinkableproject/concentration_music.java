package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class concentration_music extends AppCompatActivity {

    FirebaseStorage storage; //for uploading files
    FirebaseDatabase database; // urls of uploaded files
    ProgressDialog progressDialog;
    private Button nextPage;
    private String selected_name;
    private String url;
    private String selected_time;
    private int time;
    Handler handler = new Handler();


    ArrayList<String> imageUrl = new ArrayList<>();
    ArrayList<String> songnames = new ArrayList<>();
    ArrayList<String> songUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_music);
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        Spinner dropdown_time = (Spinner) findViewById(R.id.spinner2);
        nextPage = findViewById(R.id.next_btn);

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> vsonguri = new ArrayList<>();
        String[] items = new String[]{"1 min", "1.5 min", "2 min", "2.5 min", "3 min"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown_time.setAdapter(adapter1);

        dropdown_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_time = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    time = 60000;
                    Log.d("TIME", String.valueOf(time));
                } else if (position == 1) {
                    time = 90000;
                    Log.d("TIME", String.valueOf(time));
                } else if (position == 2) {
                    time = 12000;
                } else if (position == 3) {
                    time = 150000;
                } else {
                    time = 18000;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storage = FirebaseStorage.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        DatabaseReference reference = database.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getKey().toString());
                    songUrl.add(snapshot1.getValue().toString());

                }
                Log.i("TAG", list + "");
                Log.i("TAG", songUrl + "");

                dropdown.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.d("URL", songUrl.toString());
        Log.d("URL", songnames.toString());
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_name = parent.getItemAtPosition(position).toString();
                url = songUrl.get(position);
                Log.d("SELECTED", selected_name);
                //Log.d()
                Log.d("SELECTED", url);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdown.getSelectedItem() != null && dropdown_time.getSelectedItem() != null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MusicPlayer.class).putExtra("time", time + "")
                                    .putExtra("url", songUrl).putExtra("name", selected_name).putExtra("uri", url);
                            Log.d("Passing", url);
                            Log.d("Passing", String.valueOf(time));
                            startActivity(intent);
                        }
                    }, 0);


                } else {
                    Toast.makeText(concentration_music.this, "Please Select an Option from dropdown list", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        FirebaseDatabase.getInstance().getReference().child("Melody Of Nature").setValue("https://firebasestorage.googleapis.com/v0/b/musicapp-3ba45.appspot.com/o/melody-of-nature-main-6672.mp3?alt=media&token=f35d19b0-ba3b-46c7-ba9c-70025fad66b6");

    }
}