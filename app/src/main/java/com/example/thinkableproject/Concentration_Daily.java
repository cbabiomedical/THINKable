package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.io.*;
import java.util.*;

import pl.droidsonroids.gif.GifImageView;

public class Concentration_Daily extends AppCompatActivity {
    Dialog dialogcd;
    BarChart barChartdaily;
    View c1,c2;
    GifImageView c1gif,c2gif;
    TextView day, night;
    ImageView games, relaxationBtn, memory, landingtwo, landingtwoday, musicNight, gameNight;
    AppCompatButton monthly, yearly, weekly;

    ImageView music;
    FirebaseUser mUser;
    String text;


    LottieAnimationView anim;
    File localFile, fileName;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    int color;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__daily);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        barChartdaily = (BarChart) findViewById(R.id.barChartDaily);
        monthly = findViewById(R.id.monthly);
        games = findViewById(R.id.game);
        yearly = findViewById(R.id.yearly);
        weekly = findViewById(R.id.weekly);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        music = findViewById(R.id.music);
        landingtwo = findViewById(R.id.landingtwo);
        landingtwoday = findViewById(R.id.landingtwoday);
        musicNight = findViewById(R.id.music1);
        gameNight = findViewById(R.id.game1);
        relaxationBtn = findViewById(R.id.relaxation);
        anim = findViewById(R.id.animation);
        List<BarEntry> entries = new ArrayList<>();
        dialogcd = new Dialog(this);
        memory = findViewById(R.id.memory);
        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);

        mUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference colorreference= FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color= (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                 if(color==2){
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                }else{
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c1gif.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Memory_Daily.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });

        //Initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
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
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //Initializing arraylist and storing data in arraylist
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));

        //  Creating txt file and writing data in array list to file
        try {
            fileName = new File(getCacheDir() + "/daily.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj.size();
            for (int i = 0; i < size; i++) {
                output.write(obj.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();

        // Uploading saved data containing file to firebase storage
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1.child("daily.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Concentration_Daily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Concentration_Daily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Providing handler to delay the process of displaying
        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/daily.txt");
                //Downloading file from firebase and storing data into a tempFile in cache memory
                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Concentration_Daily.this, "Success", Toast.LENGTH_SHORT).show();

                            // reading data from the tempFile and storing in array list

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                Log.d("FileName", localFile.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list));
                                //Converting string arraylist to float array list
                                for (int i = 0; i < list.size(); i++) {
                                    floatList.add(Float.parseFloat(list.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("floatListTest", String.valueOf(floatList));
                            String[] days = new String[]{"M", "Tu", "W", "Th", "F", "Sa", "Su"};
                            ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 10f, 15f, 85f));

                            for (int j = 0; j < floatList.size(); ++j) {
                                entries.add(new BarEntry(j, floatList.get(j)));
                            }


                            float textSize = 16f;
                            //Initializing object of MyBarDataset class and passing th arraylist to y axis of chart
                            MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.8f);
                            barChartdaily.setNoDataText("Data Loading Please Wait....");
                            barChartdaily.setData(data);
                            barChartdaily.setFitBars(true);
                            barChartdaily.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));
                            barChartdaily.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartdaily.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            barChartdaily.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            barChartdaily.getXAxis().setTextSize(textSize);
                            barChartdaily.getAxisLeft().setTextSize(textSize);
                            barChartdaily.setExtraBottomOffset(10f);

                            barChartdaily.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("Chart Loading");
                            desc.setEnabled(false);
                            desc.setTextAlign(Paint.Align.CENTER);
                            barChartdaily.setDescription(desc);
                            barChartdaily.getLegend().setEnabled(false);
                            barChartdaily.getXAxis().setDrawGridLines(false);
                            barChartdaily.getAxisLeft().setDrawGridLines(false);
                            barChartdaily.setNoDataTextColor(R.color.white);
                            barChartdaily.setNoDataText("Chart Loading Please Wait...");
                            barChartdaily.animateXY(3000, 3000);
                            barChartdaily.invalidate();
                            barChartdaily.getXAxis().setTextColor(getResources().getColor(R.color.white));


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Concentration_Daily.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay);

        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Monthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Yearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Daily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of real time indication button

        anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });

    }

    // Popup window method for suggestions to improve concentration
    public void gotoPopup1(View view) {
//        Intent intentgp1 = new Intent(Concentration_Daily.this, Concentration_popup.class);
//
//        startActivity(intentgp1);
        ImageButton cancelcon, games, music1;


        dialogcd.setContentView(R.layout.activity_concentration_popup);

        games = (ImageButton) dialogcd.findViewById(R.id.gamespop1);
        music1 = (ImageButton) dialogcd.findViewById(R.id.musicpop1);

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Music.class);
                startActivity(intent);
            }
        });

        cancelcon = (ImageButton) dialogcd.findViewById(R.id.canclepop1);
        cancelcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcd.dismiss();
            }
        });
        dialogcd.show();

    }


    public void calidaily(View view) {
        Intent intentcd = new Intent(Concentration_Daily.this, Calibration.class);

        startActivity(intentcd);
    }

    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, ArrayList<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        @Override
        public int getColor(int index) {
            float c = credits.get(index);

            if (c > 80) {
                return mColors.get(0);
            } else if (c > 60) {
                return mColors.get(1);
            } else if (c > 40) {
                return mColors.get(2);
            } else if (c > 20) {
                return mColors.get(3);
            } else {
                return mColors.get(4);
            }

        }
    }

    private class MyXAxisValueFormatter extends ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
            Log.d("Tag", "monthly clicked hi");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }


    }


    public void monthly(View v) {
        Intent intent2 = new Intent(this, Concentration_Monthly.class);
        startActivity(intent2);

    }

    public void yearly(View view) {
        Intent intent2 = new Intent(this, Concentration_Yearly.class);
        startActivity(intent2);
    }

    public void weekly(View view) {
        Intent intent2 = new Intent(this, Concentration_Weekly.class);
        startActivity(intent2);
    }

//    @Override
//    protected void onDestroy() {
//        Log.d("ONDESTROY  ","Cache  ");
//        super.onDestroy();
//        try {
//            trimCache(this);
//            Log.d("DELETE ","Cache Deleted");
//            // Toast.makeText(this,"onDestroy " ,Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Log.d("NOT DELETE ","Cache NOT Deleted");
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }


}