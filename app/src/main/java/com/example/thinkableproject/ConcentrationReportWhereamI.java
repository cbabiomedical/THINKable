package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class ConcentrationReportWhereamI extends AppCompatActivity {
    AppCompatButton progressConcentration;
    File fileName, fileNamea, fileNamem, fileNamea2, fileNamen, fileNamea3, fileNameo, fileNamea4;
    FirebaseUser mUser;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    Long average1, average2, average3, average4, average5, average6, average7;
    Long average11, average12, average13, average14, average15, average16, average17;
    Long average21, average22, average23, average24, average25, average26, average27;
    Long average31, average32, average33, average34, average35, average36, average37;
    Long average41, average42, average43, average44, average45, average46, average47;
    Long average51, average52, average53, average54, average55, average56, average57;
    Long average61, average62, average63, average64, average65, average66, average67;
    Long average71, average72, average73, average74, average75, average76, average77;
    int sum1, sum2, sum3, sum4, sum5, sum6, sum7;
    int sum11, sum12, sum13, sum14, sum15, sum16, sum17;
    int sum21, sum22, sum23, sum24, sum25, sum26, sum27;
    int sum31, sum32, sum33, sum34, sum35, sum36, sum37;
    int sum41, sum42, sum43, sum44, sum45, sum46, sum47;
    int sum51, sum52, sum53, sum54, sum55, sum56, sum57;
    int sum61, sum62, sum63, sum64, sum65, sum66, sum67;
    int sum71, sum72, sum73, sum74, sum75, sum76, sum77;
    String occupation;
    ImageView relaxationBtn, memory;
    Animation scaleUp, scaleDown;

    File localFile, localFilea, localFilem, localFilea2, localFilen, localFilea3, localFileo, localFile4a;
    String text, texta, textm, texta2, textn, texta3, texto, text4a;

    //for chart 1
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> lista = new ArrayList<>();
    ArrayList<Float> floatLista = new ArrayList<>();
    //for chart 2
    ArrayList<String> listm = new ArrayList<>();
    ArrayList<Float> floatListm = new ArrayList<>();
    ArrayList<String> lista1 = new ArrayList<>();
    ArrayList<Float> floatLista1 = new ArrayList<>();
    //for chart 3
    ArrayList<String> listn = new ArrayList<>();
    ArrayList<Float> floatListn = new ArrayList<>();
    ArrayList<String> lista2 = new ArrayList<>();
    ArrayList<Float> floatLista2 = new ArrayList<>();
    //for chart 4
    ArrayList<String> listo = new ArrayList<>();
    ArrayList<Float> floatListo = new ArrayList<>();
    ArrayList<String> lista3 = new ArrayList<>();
    ArrayList<Float> floatLista3 = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSets = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetsm = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetn = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSeto = new ArrayList<>();

    private ScatterChart chart, chart1, chart2, chart3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_wheream_i);
        progressConcentration = findViewById(R.id.progress);
        relaxationBtn = findViewById(R.id.relaxation);
        memory = findViewById(R.id.memory);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c1gif.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                        c1gif.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.INVISIBLE);


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        memory.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    memory.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    memory.startAnimation(scaleDown);
                }

                return false;
            }
        });

        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        relaxationBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    relaxationBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    relaxationBtn.startAnimation(scaleDown);
                }

                return false;
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

        progressConcentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        progressConcentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    progressConcentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    progressConcentration.startAnimation(scaleDown);
                }

                return false;
            }
        });

        //1----> chart
        //1 -----> chart
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileName = new File(getCacheDir() + "/reportConcenWhereaiTR_job.txt");  //Writing data to file
            String line = "";
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea = new File(getCacheDir() + "/reportConcenWhereamiTR_jobAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa;
            fwa = new FileWriter(fileNamea);
            BufferedWriter outputa = new BufferedWriter(fwa);
            int size = obja.size();
            for (int i = 0; i < size; i++) {
                outputa.write(obja.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1.child("reportConcenWhereamiTR_job.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReference1a = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1a.child("reportConcenWhereamiTR_jobAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        final int delay = 5000;
//            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Teacher")
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
                Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
                Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
                Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
                Format f = new SimpleDateFormat("EEEE");
                String str = f.format(new Date());
                int month = now.get(Calendar.MONTH) + 1;
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_job.txt");
                StorageReference storageReferencea = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_jobAvg.txt");
                //download and read the file

                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    localFilea = File.createTempFile("tempFilea", ".txt");

                    text = localFile.getAbsolutePath();
                    texta = localFilea.getAbsolutePath();

                    Log.d("Bitmap", text);
                    Log.d("Bitmap", texta);

                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

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

                                for (int i = 0; i < list.size(); i++) {
                                    floatList.add(Float.parseFloat(list.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                    ArrayList sumElement = new ArrayList();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                    sum1 += av1;
                                    sumElement.add(av1);
                                }
                            }
                            if (sum1 != 0.0) {
                                Log.d("SUMSUNTTC", String.valueOf(sum1));
                                average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                                Log.d("AVERAGESUNTTC", String.valueOf(average1));
                            } else {
                                average1 = 0L;
                            }


                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                            ArrayList sumElement = new ArrayList();
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                            sum2 += av1;
                                            sumElement.add(av1);
                                        }
                                    }
                                    if (sum2 != 0.0) {
                                        Log.d("SUMMONTTC", String.valueOf(sum2));
                                        average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                        Log.d("AVERAGEMONTTC", String.valueOf(average2));
                                    } else {
                                        average2 = 0L;
                                    }
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                    ArrayList sumElement = new ArrayList();
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                    sum3 += av1;
                                                    sumElement.add(av1);
                                                }
                                            }
                                            if (sum3 != 0.0) {
                                                Log.d("SUMTUETTC", String.valueOf(sum3));
                                                average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                                Log.d("AVERAGETUETTC", String.valueOf(average3));
                                            } else {
                                                average3 = 0L;
                                            }

                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                            ArrayList sumElement = new ArrayList();
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                            sum4 += av1;
                                                            sumElement.add(av1);
                                                        }
                                                    }
                                                    if (sum4 != 0.0) {
                                                        Log.d("SUMWEDTTC", String.valueOf(sum4));
                                                        average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                        Log.d("AVERAGEWEDTTC", String.valueOf(average4));
                                                    } else {
                                                        average4 = 0L;
                                                    }
                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                    ArrayList sumElement = new ArrayList();
                                                    reference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                    sum5 += av1;
                                                                    sumElement.add(av1);
                                                                }
                                                            }
                                                            if (sum5 != 0.0) {
                                                                Log.d("SUMTHUTTC", String.valueOf(sum5));
                                                                average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                Log.d("AVERAGETHUTTC", String.valueOf(average5));
                                                            } else {
                                                                average5 = 0L;
                                                            }
                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                            ArrayList sumElement = new ArrayList();
                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                            sum6 += av1;
                                                                            sumElement.add(av1);
                                                                        }
                                                                    }
                                                                    if (sum6 != 0.0) {
                                                                        Log.d("SUMFRITTC", String.valueOf(sum6));
                                                                        average6 = sum6 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                        Log.d("AVERAGEFRITTC", String.valueOf(average6));
                                                                    } else {
                                                                        average6 = 0L;
                                                                    }

                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                    ArrayList sumElement = new ArrayList();
                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                    sum7 += av1;
                                                                                    sumElement.add(av1);
                                                                                }
                                                                            }
                                                                            if (sum7 != 0.0) {
                                                                                Log.d("SUMSATTTC", String.valueOf(sum7));
                                                                                average7 = sum7 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                Log.d("AVERAGESATTTC", String.valueOf(average7));
                                                                            } else {
                                                                                average7 = 0L;
                                                                            }

                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                            ArrayList sumElement = new ArrayList();
                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                            sum11 += av1;
                                                                                            sumElement.add(av1);
                                                                                        }
                                                                                    }
                                                                                    if (sum11 != 0.0) {
                                                                                        Log.d("SUMSUNTTC", String.valueOf(sum11));
                                                                                        average11 = sum11 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                        Log.d("AVERAGESUNTTC", String.valueOf(average11));
                                                                                    } else {
                                                                                        average11 = 0L;
                                                                                    }


                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                    ArrayList sumElement = new ArrayList();
                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                    sum12 += av1;
                                                                                                    sumElement.add(av1);
                                                                                                }
                                                                                            }
                                                                                            if (sum12 != 0.0) {
                                                                                                Log.d("SUMMONTTC", String.valueOf(sum12));
                                                                                                average12 = sum12 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                Log.d("AVERAGEMONTTC", String.valueOf(average12));
                                                                                            } else {
                                                                                                average12 = 0L;
                                                                                            }
                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                            ArrayList sumElement = new ArrayList();
                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                            sum13 += av1;
                                                                                                            sumElement.add(av1);
                                                                                                        }
                                                                                                    }
                                                                                                    if (sum13 != 0.0) {
                                                                                                        Log.d("SUMTUETTC", String.valueOf(sum13));
                                                                                                        average13 = sum13 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                        Log.d("AVERAGETUETTC", String.valueOf(average13));
                                                                                                    } else {
                                                                                                        average13 = 0L;
                                                                                                    }

                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                    sum14 += av1;
                                                                                                                    sumElement.add(av1);
                                                                                                                }
                                                                                                            }
                                                                                                            if (sum14 != 0.0) {
                                                                                                                Log.d("SUMWEDTTC", String.valueOf(sum14));
                                                                                                                average14 = sum14 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                Log.d("AVERAGEWEDTTC", String.valueOf(average14));
                                                                                                            } else {
                                                                                                                average14 = 0L;
                                                                                                            }
                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                            sum15 += av1;
                                                                                                                            sumElement.add(av1);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (sum15 != 0.0) {
                                                                                                                        Log.d("SUMTHUTTC", String.valueOf(sum15));
                                                                                                                        average15 = sum15 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                        Log.d("AVERAGETHUTTC", String.valueOf(average15));
                                                                                                                    } else {
                                                                                                                        average15 = 0L;
                                                                                                                    }
                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                        @Override
                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                    sum16 += av1;
                                                                                                                                    sumElement.add(av1);
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (sum16 != 0.0) {
                                                                                                                                Log.d("SUMFRITTC", String.valueOf(sum16));
                                                                                                                                average16 = sum16 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                Log.d("AVERAGEFRITTC", String.valueOf(average16));
                                                                                                                            } else {
                                                                                                                                average16 = 0L;
                                                                                                                            }

                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                            sum17 += av1;
                                                                                                                                            sumElement.add(av1);
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    if (sum17 != 0.0) {
                                                                                                                                        Log.d("SUMSATTTC", String.valueOf(sum17));
                                                                                                                                        average17 = sum17 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                        Log.d("AVERAGESATTTC", String.valueOf(average17));
                                                                                                                                    } else {
                                                                                                                                        average17 = 0L;
                                                                                                                                    }
                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                    sum21 += av1;
                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            if (sum21 != 0.0) {
                                                                                                                                                Log.d("SUMSUNTTC", String.valueOf(sum21));
                                                                                                                                                average21 = sum21 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                Log.d("AVERAGESUNTTC", String.valueOf(average21));
                                                                                                                                            } else {
                                                                                                                                                average21 = 0L;
                                                                                                                                            }


                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                            sum22 += av1;
                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if (sum22 != 0.0) {
                                                                                                                                                        Log.d("SUMMONTTC", String.valueOf(sum22));
                                                                                                                                                        average22 = sum22 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                        Log.d("AVERAGEMONTTC", String.valueOf(average22));
                                                                                                                                                    } else {
                                                                                                                                                        average22 = 0L;
                                                                                                                                                    }
                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                    sum23 += av1;
                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            if (sum23 != 0.0) {
                                                                                                                                                                Log.d("SUMTUETTC", String.valueOf(sum23));
                                                                                                                                                                average23 = sum23 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                Log.d("AVERAGETUETTC", String.valueOf(average23));
                                                                                                                                                            } else {
                                                                                                                                                                average23 = 0L;
                                                                                                                                                            }

                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                            sum24 += av1;
                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                    if (sum24 != 0.0) {
                                                                                                                                                                        Log.d("SUMWEDTTC", String.valueOf(sum24));
                                                                                                                                                                        average24 = sum24 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                        Log.d("AVERAGEWEDTTC", String.valueOf(average24));
                                                                                                                                                                    } else {
                                                                                                                                                                        average24 = 0L;
                                                                                                                                                                    }
                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                    sum25 += av1;
                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                            if (sum25 != 0.0) {
                                                                                                                                                                                Log.d("SUMTHUTTC", String.valueOf(sum25));
                                                                                                                                                                                average25 = sum25 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                Log.d("AVERAGETHUTTC", String.valueOf(average25));
                                                                                                                                                                            } else {
                                                                                                                                                                                average25 = 0L;
                                                                                                                                                                            }
                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                            sum26 += av1;
                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                    if (sum26 != 0.0) {
                                                                                                                                                                                        Log.d("SUMFRITTC", String.valueOf(sum26));
                                                                                                                                                                                        average26 = sum26 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                        Log.d("AVERAGEFRITTC", String.valueOf(average26));
                                                                                                                                                                                    } else {
                                                                                                                                                                                        average26 = 0L;
                                                                                                                                                                                    }

                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                        @Override
                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                    sum27 += av1;
                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                            if (sum27 != 0.0) {
                                                                                                                                                                                                Log.d("SUMSATTTC", String.valueOf(sum27));
                                                                                                                                                                                                average27 = sum27 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                Log.d("AVERAGESATTTC", String.valueOf(average27));
                                                                                                                                                                                            } else {
                                                                                                                                                                                                average27 = 0L;
                                                                                                                                                                                            }

                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                @Override
                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                            sum31 += av1;
                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (sum31 != 0.0) {
                                                                                                                                                                                                        Log.d("SUMSUNTTC", String.valueOf(sum31));
                                                                                                                                                                                                        average31 = sum31 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                        Log.d("AVERAGESUNTTC", String.valueOf(average31));
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        average31 = 0L;
                                                                                                                                                                                                    }


                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                    sum32 += av1;
                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (sum32 != 0.0) {
                                                                                                                                                                                                                Log.d("SUMMONTTC", String.valueOf(sum32));
                                                                                                                                                                                                                average32 = sum32 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                Log.d("AVERAGEMONTTC", String.valueOf(average32));
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                average32 = 0L;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                            sum33 += av1;
                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    if (sum33 != 0.0) {
                                                                                                                                                                                                                        Log.d("SUMTUETTC", String.valueOf(sum33));
                                                                                                                                                                                                                        average33 = sum33 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                        Log.d("AVERAGETUETTC", String.valueOf(average33));
                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                        average33 = 0L;
                                                                                                                                                                                                                    }

                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                    sum34 += av1;
                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (sum34 != 0.0) {
                                                                                                                                                                                                                                Log.d("SUMWEDTTC", String.valueOf(sum34));
                                                                                                                                                                                                                                average34 = sum34 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                Log.d("AVERAGEWEDTTC", String.valueOf(average34));
                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                average34 = 0L;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                            sum35 += av1;
                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (sum35 != 0.0) {
                                                                                                                                                                                                                                        Log.d("SUMTHUTTC", String.valueOf(sum35));
                                                                                                                                                                                                                                        average35 = sum35 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                        Log.d("AVERAGETHUTTC", String.valueOf(average35));
                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                        average35 = 0L;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                    sum36 += av1;
                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (sum36 != 0.0) {
                                                                                                                                                                                                                                                Log.d("SUMFRITTC", String.valueOf(sum36));
                                                                                                                                                                                                                                                average36 = sum36 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                Log.d("AVERAGEFRITTC", String.valueOf(average36));
                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                average36 = 0L;
                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                            sum37 += av1;
                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    if (sum37 != 0.0) {
                                                                                                                                                                                                                                                        Log.d("SUMSATTTC", String.valueOf(sum37));
                                                                                                                                                                                                                                                        average37 = sum37 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                        Log.d("AVERAGESATTTC", String.valueOf(average37));
                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                        average37 = 0L;
                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                    sum41 += av1;
                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            if (sum41 != 0.0) {
                                                                                                                                                                                                                                                                Log.d("SUMSUNTTC", String.valueOf(sum41));
                                                                                                                                                                                                                                                                average41 = sum41 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                Log.d("AVERAGESUNTTC", String.valueOf(average41));
                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                average41 = 0L;
                                                                                                                                                                                                                                                            }


                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                            sum42 += av1;
                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    if (sum42 != 0.0) {
                                                                                                                                                                                                                                                                        Log.d("SUMMONTTC", String.valueOf(sum42));
                                                                                                                                                                                                                                                                        average42 = sum42 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                        Log.d("AVERAGEMONTTC", String.valueOf(average42));
                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                        average42 = 0L;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                    sum43 += av1;
                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            if (sum43 != 0.0) {
                                                                                                                                                                                                                                                                                Log.d("SUMTUETTC", String.valueOf(sum43));
                                                                                                                                                                                                                                                                                average43 = sum43 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                Log.d("AVERAGETUETTC", String.valueOf(average43));
                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                average43 = 0L;
                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                            sum44 += av1;
                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    if (sum44 != 0.0) {
                                                                                                                                                                                                                                                                                        Log.d("SUMWEDTTC", String.valueOf(sum44));
                                                                                                                                                                                                                                                                                        average44 = sum44 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                        Log.d("AVERAGEWEDTTC", String.valueOf(average44));
                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                        average44 = 0L;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                    sum45 += av1;
                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            if (sum45 != 0.0) {
                                                                                                                                                                                                                                                                                                Log.d("SUMTHUTTC", String.valueOf(sum45));
                                                                                                                                                                                                                                                                                                average45 = sum45 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                Log.d("AVERAGETHUTTC", String.valueOf(average45));
                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                average45 = 0L;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                            sum46 += av1;
                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    if (sum46 != 0.0) {
                                                                                                                                                                                                                                                                                                        Log.d("SUMFRITTC", String.valueOf(sum46));
                                                                                                                                                                                                                                                                                                        average46 = sum46 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                        Log.d("AVERAGEFRITTC", String.valueOf(average46));
                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                        average46 = 0L;
                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                    sum47 += av1;
                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            if (sum47 != 0.0) {
                                                                                                                                                                                                                                                                                                                Log.d("SUMSATTTC", String.valueOf(sum47));
                                                                                                                                                                                                                                                                                                                average47 = sum47 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                Log.d("AVERAGESATTTC", String.valueOf(average47));
                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                average47 = 0L;
                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                            sum51 += av1;
                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    if (sum51 != 0.0) {
                                                                                                                                                                                                                                                                                                                        Log.d("SUMSUNTTC", String.valueOf(sum51));
                                                                                                                                                                                                                                                                                                                        average51 = sum51 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGESUNTTC", String.valueOf(average51));
                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                        average51 = 0L;
                                                                                                                                                                                                                                                                                                                    }


                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                    sum52 += av1;
                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            if (sum52 != 0.0) {
                                                                                                                                                                                                                                                                                                                                Log.d("SUMMONTTC", String.valueOf(sum52));
                                                                                                                                                                                                                                                                                                                                average52 = sum52 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEMONTTC", String.valueOf(average52));
                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                average52 = 0L;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                            sum53 += av1;
                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                    if (sum53 != 0.0) {
                                                                                                                                                                                                                                                                                                                                        Log.d("SUMTUETTC", String.valueOf(sum53));
                                                                                                                                                                                                                                                                                                                                        average53 = sum53 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGETUETTC", String.valueOf(average43));
                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                        average53 = 0L;
                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                    sum54 += av1;
                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            if (sum54 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                Log.d("SUMWEDTTC", String.valueOf(sum54));
                                                                                                                                                                                                                                                                                                                                                average54 = sum54 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEWEDTTC", String.valueOf(average54));
                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                average54 = 0L;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                            sum55 += av1;
                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    if (sum55 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMTHUTTC", String.valueOf(sum55));
                                                                                                                                                                                                                                                                                                                                                        average55 = sum55 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGETHUTTC", String.valueOf(average55));
                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                        average55 = 0L;
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                    sum56 += av1;
                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                            if (sum56 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMFRITTC", String.valueOf(sum56));
                                                                                                                                                                                                                                                                                                                                                                average56 = sum56 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEFRITTC", String.valueOf(average56));
                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                average56 = 0L;
                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                            sum57 += av1;
                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    if (sum57 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMSATTTC", String.valueOf(sum57));
                                                                                                                                                                                                                                                                                                                                                                        average57 = sum57 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGESATTTC", String.valueOf(average57));
                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                        average57 = 0L;
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                    sum61 += av1;
                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                            if (sum61 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMSUNTTC", String.valueOf(sum61));
                                                                                                                                                                                                                                                                                                                                                                                average61 = sum61 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGESUNTTC", String.valueOf(average61));
                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                average61 = 0L;
                                                                                                                                                                                                                                                                                                                                                                            }


                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                            sum62 += av1;
                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                    if (sum62 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMMONTTC", String.valueOf(sum62));
                                                                                                                                                                                                                                                                                                                                                                                        average62 = sum62 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGEMONTTC", String.valueOf(average62));
                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                        average62 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                    sum63 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                            if (sum63 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMTUETTC", String.valueOf(sum63));
                                                                                                                                                                                                                                                                                                                                                                                                average63 = sum63 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGETUETTC", String.valueOf(average63));
                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                average63 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                            sum64 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                    if (sum64 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMWEDTTC", String.valueOf(sum64));
                                                                                                                                                                                                                                                                                                                                                                                                        average64 = sum64 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGEWEDTTC", String.valueOf(average64));
                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                        average64 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                    sum65 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                            if (sum65 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMTHUTTC", String.valueOf(sum65));
                                                                                                                                                                                                                                                                                                                                                                                                                average65 = sum65 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGETHUTTC", String.valueOf(average65));
                                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                                average65 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                            sum66 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                    if (sum66 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMFRITTC", String.valueOf(sum66));
                                                                                                                                                                                                                                                                                                                                                                                                                        average66 = sum66 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGEFRITTC", String.valueOf(average66));
                                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                                        average66 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                    sum67 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                            if (sum67 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMSATTTC", String.valueOf(sum67));
                                                                                                                                                                                                                                                                                                                                                                                                                                average67 = sum67 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGESATTTC", String.valueOf(average67));
                                                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                average67 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");
                                                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                            sum71 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                    if (sum71 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMSUNTTC", String.valueOf(sum71));
                                                                                                                                                                                                                                                                                                                                                                                                                                        average71 = sum71 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGESUNTTC", String.valueOf(average71));
                                                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                        average71 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                    }


                                                                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                    sum72 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                            if (sum72 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMMONTTC", String.valueOf(sum72));
                                                                                                                                                                                                                                                                                                                                                                                                                                                average72 = sum72 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEMONTTC", String.valueOf(average72));
                                                                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                average72 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");
                                                                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                            sum73 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                    if (sum73 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMTUETTC", String.valueOf(sum73));
                                                                                                                                                                                                                                                                                                                                                                                                                                                        average73 = sum73 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGETUETTC", String.valueOf(average73));
                                                                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        average73 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");
                                                                                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sum74 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                            if (sum74 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMWEDTTC", String.valueOf(sum74));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                average74 = sum74 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEWEDTTC", String.valueOf(average74));
                                                                                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                average74 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");
                                                                                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            sum75 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    if (sum75 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMTHUTTC", String.valueOf(sum75));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        average75 = sum75 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGETHUTTC", String.valueOf(average75));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        average75 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sum76 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            if (sum76 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("SUMFRITTC", String.valueOf(sum76));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                average76 = sum76 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Log.d("AVERAGEFRITTC", String.valueOf(average76));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                average76 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            reference.addValueEventListener(new ValueEventListener() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            sum77 += av1;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            sumElement.add(av1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    if (sum77 != 0.0) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("SUMSATTTC", String.valueOf(sum77));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        average77 = sum77 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Log.d("AVERAGESATTTC", String.valueOf(average77));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        average77 = 0L;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(0, average1));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(1, average2));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(2, average3));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(3, average4));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(4, average5));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(5, average6));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries.add(new Entry(6, average7));


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries1 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(0, average11));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(1, average12));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(2, average13));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(3, average14));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(4, average15));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(5, average16));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries1.add(new Entry(6, average17));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries2 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(0, average21));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(1, average22));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(2, average23));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(3, average24));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(4, average25));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(5, average26));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries2.add(new Entry(6, average27));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries3 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(0, average31));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(1, average32));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(2, average33));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(3, average34));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(4, average35));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(5, average36));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries3.add(new Entry(6, average37));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries4 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(0, average41));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(1, average42));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(2, average43));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(3, average44));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(4, average45));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(5, average46));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries4.add(new Entry(6, average47));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries5 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(0, average51));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(1, average52));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(2, average53));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(3, average54));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(4, average55));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(5, average56));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries5.add(new Entry(6, average57));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries6 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(0, average61));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(1, average62));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(2, average63));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(3, average64));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(4, average65));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(5, average66));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries6.add(new Entry(6, average67));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    List<Entry> scatterEntries7 = new ArrayList<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(0, average71));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(1, average72));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(2, average73));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(3, average74));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(4, average75));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(5, average76));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    scatterEntries7.add(new Entry(6, average77));
//                    scatterEntries.add(new Entry(7, average6));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1 = findViewById(R.id.chart3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getDescription().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            setEnabled(false);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setDrawGridBackground(false);
//                            chart.setBackgroundColor(getResources().getColor(R.color.t));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setTouchEnabled(true);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setMaxHighlightDistance(50f);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setDragEnabled(true);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setScaleEnabled(true);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setMaxVisibleValueCount(200);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setPinchZoom(true);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getXAxis().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            setTextColor(getResources().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    getColor(R.color.white));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getAxisLeft().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            setTextColor(getResources().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    getColor(R.color.white));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Legend l = chart1.getLegend();

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    YAxis yl = chart1.getAxisLeft();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    yl.setAxisMinimum(0f);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getAxisRight().

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            setEnabled(false);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    XAxis xl = chart1.getXAxis();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    xl.setDrawGridLines(false);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    String[] daysS = new String[]{"Su", "Mn", "Tu", "We", "Th", "Fr", "Sa"};
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    XAxis xAxis = chart1.getXAxis();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    xAxis.setValueFormatter(new

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            IndexAxisValueFormatter(daysS));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    xAxis.setGranularity(1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    xAxis.setCenterAxisLabels(true);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setNoDataText("Data Loading Please Wait....");

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    l.setDrawInside(false);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    l.setXOffset(4f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set1 = new ScatterDataSet(scatterEntries, "10-20");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set1.setColor(ColorTemplate.COLORFUL_COLORS[1]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set1.setValueTextColor(Color.rgb(255, 255, 255));
//
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set1.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set2 = new ScatterDataSet(scatterEntries1, "20-30");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set2.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set2.setValueTextColor(Color.rgb(255, 255, 255));

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set3 = new ScatterDataSet(scatterEntries2, "30-40");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set3.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set3.setColor(ColorTemplate.COLORFUL_COLORS[2]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set3.setValueTextColor(Color.rgb(255, 255, 255));
//
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set3.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set4 = new ScatterDataSet(scatterEntries3, "40-50");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set4.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set4.setColor(Color.BLUE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set4.setValueTextColor(Color.rgb(255, 255, 255));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set4.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set5 = new ScatterDataSet(scatterEntries4, "50-60");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set5.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set5.setColor(Color.GREEN);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set5.setValueTextColor(Color.rgb(255, 255, 255));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set5.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set6 = new ScatterDataSet(scatterEntries5, "60-70");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set6.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set6.setColor(Color.RED);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set6.setValueTextColor(Color.rgb(255, 255, 255));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set6.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set7 = new ScatterDataSet(scatterEntries6, "70-80");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set7.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set7.setColor(Color.LTGRAY);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set7.setValueTextColor(Color.rgb(255, 255, 255));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set7.setScatterShapeSize(20f);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterDataSet set8 = new ScatterDataSet(scatterEntries6, "80-90");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set8.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set8.setColor(Color.WHITE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set8.setValueTextColor(Color.rgb(255, 255, 255));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set8.setScatterShapeSize(20f);


//
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    set2.setScatterShapeSize(20f);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set1);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set2); // add the data sets
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set3);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set4);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set5);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set6);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set7);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    dataSets.add(set8);

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ScatterData data = new ScatterData(dataSets);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.setData(data);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getLegend().setTextColor(Color.WHITE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.getDescription().setTextColor(Color.WHITE);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    chart1.invalidate();

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            });

                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                    });

                                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            });
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    });
                                                                                                                                                                                                                }

                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                                }
                                                                                                                                                                                                            });
                                                                                                                                                                                                        }

                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                        }
                                                                                                                                                                                                    });
                                                                                                                                                                                                }

                                                                                                                                                                                                @Override
                                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                                }
                                                                                                                                                                                            });
                                                                                                                                                                                        }

                                                                                                                                                                                        @Override
                                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                        }
                                                                                                                                                                                    });
                                                                                                                                                                                }

                                                                                                                                                                                @Override
                                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                                }
                                                                                                                                                                            });
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                                }

                                                                                                                                                                @Override
                                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                                }
                                                                                                                                                            });
                                                                                                                                                        }

                                                                                                                                                        @Override
                                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                }

                                                                                                                                                @Override
                                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                                }
                                                                                                                            });


                                                                                                                        }

                                                                                                                        @Override
                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                        }
                                                                                                                    });
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                }
                                                                                                            });
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//


//                            for (int j = 0; j < floatList.size(); ++j) {
//                                scatterEntries.add(new Entry(j, floatList.get(j)));
//                                            }


                    storageReferencea.getFile(localFilea).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadera = new InputStreamReader(new FileInputStream(localFilea.getAbsolutePath()));

                                Log.d("FileName", localFilea.getAbsolutePath());

                                BufferedReader bufferedReadera = new BufferedReader(inputStreamReadera);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadera.readLine()) != null) {
                                    lista.add(line);
                                }
                                while ((line = bufferedReadera.readLine()) != null) {

                                    lista.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(lista));

                                for (int i = 0; i < lista.size(); i++) {
                                    floatLista.add(Float.parseFloat(lista.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatLista));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


//                            List<Entry> scatterEntriesa = new ArrayList<>();
//                            for (int j = 0; j < floatLista.size(); ++j) {
//                                scatterEntriesa.add(new Entry(j, floatLista.get(j)));
//                            }

//

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (
                        IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay);


        //2 ------> chart

        ArrayList<Float> objm = new ArrayList<>(
                Arrays.asList(80f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja1 = new ArrayList<>(
                Arrays.asList(25f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamem = new File(getCacheDir() + "/reportConcenWhereaiTR_age.txt");  //Writing data to file
            String line = "";
            FileWriter fwm;
            fwm = new FileWriter(fileNamem);
            BufferedWriter outputm = new BufferedWriter(fwm);
            int size = objm.size();
            for (int i = 0; i < size; i++) {
                outputm.write(objm.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputm.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea2 = new File(getCacheDir() + "/reportConcenWhereamiTR_ageAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa2;
            fwa2 = new FileWriter(fileNamea2);
            BufferedWriter outputa2 = new BufferedWriter(fwa2);
            int size = obja1.size();
            for (int i = 0; i < size; i++) {
                outputa2.write(obja1.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa2.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference2.child("reportConcenWhereamiTR_age.txt");
            InputStream stream = new FileInputStream(new File(fileNamem.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea2.child("reportConcenWhereamiTR_ageAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea2.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler1 = new Handler();
        final int delay1 = 5000;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        int month = now.get(Calendar.MONTH) + 1;
        handler1.postDelayed(new

                                     Runnable() {

                                         @Override
                                         public void run() {
                                             StorageReference storageReferencem = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_age.txt");
                                             StorageReference storageReferencea2 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_ageAvg.txt");
                                             //download and read the file

                                             try {
                                                 localFilem = File.createTempFile("tempFilem", ".txt");
                                                 localFilea2 = File.createTempFile("tempFilea2", ".txt");

                                                 textm = localFilem.getAbsolutePath();
                                                 texta2 = localFilea2.getAbsolutePath();

                                                 Log.d("Bitmap", textm);
                                                 Log.d("Bitmap", texta2);

                                                 storageReferencem.getFile(localFilem).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                         Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReaderm = new InputStreamReader(new FileInputStream(localFilem.getAbsolutePath()));

                                                             Log.d("FileName", localFilem.getAbsolutePath());

                                                             BufferedReader bufferedReaderm = new BufferedReader(inputStreamReaderm);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReaderm.readLine()) != null) {
                                                                 listm.add(line);
                                                             }
                                                             while ((line = bufferedReaderm.readLine()) != null) {

                                                                 listm.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(listm));

                                                             for (int i = 0; i < listm.size(); i++) {
                                                                 floatListm.add(Float.parseFloat(listm.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatListm));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time to Concentrate").child("Law and Law Enforcement and Security").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");
                                                         reference.addValueEventListener(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                 for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                     for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                         Long av1 = Long.parseLong(String.valueOf(dataSnapshot1.getValue()));
                                                                         Log.d("AV1MONOCC", String.valueOf(dataSnapshot1.getValue()));
                                                                     }
                                                                 }
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError error) {

                                                             }
                                                         });

                                                         chart = findViewById(R.id.chart1);
                                                         chart.getDescription().setEnabled(false);
                                                         chart.setDrawGridBackground(true);
                                                         chart.setBackgroundColor(getResources().getColor(R.color.background));
                                                         chart.setTouchEnabled(true);
                                                         chart.setMaxHighlightDistance(50f);
                                                         chart.setDragEnabled(true);
                                                         chart.setScaleEnabled(true);
                                                         chart.setMaxVisibleValueCount(200);
                                                         chart.setNoDataText("");
                                                         chart.setPinchZoom(true);
                                                         Legend l1 = chart.getLegend();
                                                         chart.setNoDataText("Data Loading Please Wait....");
                                                         chart.setDescription(null);

                                                         YAxis yl1 = chart.getAxisLeft();
                                                         yl1.setAxisMinimum(0f);
                                                         chart.getAxisRight().setEnabled(false);
                                                         chart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                         chart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                         XAxis xl1 = chart.getXAxis();
                                                         xl1.setDrawGridLines(false);
                                                         String[] daysS1 = new String[]{"Mn", "Tu", "We", "Th", "Fr", "Sa", "Su"};
                                                         XAxis xAxis1 = chart.getXAxis();
                                                         xAxis1.setValueFormatter(new IndexAxisValueFormatter(daysS1));
                                                         xAxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                                         xAxis1.setGranularity(1);
                                                         xAxis1.setCenterAxisLabels(true);

                                                         l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                                                         l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                                                         l1.setOrientation(Legend.LegendOrientation.VERTICAL);
                                                         l1.setDrawInside(false);
                                                         l1.setXOffset(5f);

//                                                         ScatterDataSet setm = new ScatterDataSet(scatterEntriesm, "You");
//                                                         setm.setScatterShape(ScatterChart.ScatterShape.SQUARE);
//                                                         setm.setColor(ColorTemplate.COLORFUL_COLORS[0]);
//
//
//                                                         setm.setScatterShapeSize(8f);

//                                                         dataSetsm.add(setm); // add the data sets

                                                         ScatterData datam = new ScatterData(dataSetsm);
                                                         chart.setData(datam);
                                                         chart.invalidate();


                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                                 storageReferencea2.getFile(localFilea2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReadera2 = new InputStreamReader(new FileInputStream(localFilea2.getAbsolutePath()));

                                                             Log.d("FileName", localFilea2.getAbsolutePath());

                                                             BufferedReader bufferedReadera2 = new BufferedReader(inputStreamReadera2);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReadera2.readLine()) != null) {
                                                                 lista2.add(line);
                                                             }
                                                             while ((line = bufferedReadera2.readLine()) != null) {

                                                                 lista2.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(lista2));

                                                             for (int i = 0; i < lista2.size(); i++) {
                                                                 floatLista2.add(Float.parseFloat(lista2.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatLista2));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         List<Entry> scatterEntriesa2 = new ArrayList<>();
                                                         for (int j = 0; j < floatLista2.size(); ++j) {
                                                             scatterEntriesa2.add(new Entry(j, floatLista2.get(j)));
                                                         }

                                                         ScatterDataSet seta2 = new ScatterDataSet(scatterEntriesa2, "Other");
                                                         seta2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                         seta2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                                                         seta2.setScatterShapeHoleRadius(3f);

                                                         seta2.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                                                         seta2.setScatterShapeSize(8f);

                                                         dataSetsm.add(seta2);

                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                             } catch (IOException exception) {
                                                 exception.printStackTrace();
                                             }
                                         }

                                         //Downloading file and displaying chart
                                     }, delay1);


        //3 -----> chart
        ArrayList<Float> objn = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja2 = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamen = new File(getCacheDir() + "/reportConcenWhereaiTSC_job.txt");  //Writing data to file
            String line = "";
            FileWriter fwn;
            fwn = new FileWriter(fileNamen);
            BufferedWriter outputn = new BufferedWriter(fwn);
            int size = objn.size();
            for (int i = 0; i < size; i++) {
                outputn.write(objn.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputn.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea3 = new File(getCacheDir() + "/reportConcenWhereamiTSC_jobAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa3;
            fwa3 = new FileWriter(fileNamea3);
            BufferedWriter outputa3 = new BufferedWriter(fwa3);
            int size = obja2.size();
            for (int i = 0; i < size; i++) {
                outputa3.write(obja2.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa3.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferencen = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencen.child("reportConcenWhereamiTSC_job.txt");
            InputStream stream = new FileInputStream(new File(fileNamen.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea3 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea3.child("reportConcenWhereamiTSC_jobAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea3.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler3 = new Handler();
        final int delay3 = 5000;

        handler3.postDelayed(new

                                     Runnable() {

                                         @Override
                                         public void run() {
                                             StorageReference storageReferencen = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTSC_job.txt");
                                             StorageReference storageReferencea3 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTSC_jobAvg.txt");
                                             //download and read the file

                                             try {
                                                 localFilen = File.createTempFile("tempFilen", ".txt");
                                                 localFilea3 = File.createTempFile("tempFilea3", ".txt");

                                                 textn = localFilen.getAbsolutePath();
                                                 texta3 = localFilea3.getAbsolutePath();

                                                 Log.d("Bitmap", textn);
                                                 Log.d("Bitmap", texta3);

                                                 storageReferencen.getFile(localFilen).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReadern = new InputStreamReader(new FileInputStream(localFilen.getAbsolutePath()));

                                                             Log.d("FileName", localFilen.getAbsolutePath());

                                                             BufferedReader bufferedReadern = new BufferedReader(inputStreamReadern);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReadern.readLine()) != null) {
                                                                 listn.add(line);
                                                             }
                                                             while ((line = bufferedReadern.readLine()) != null) {

                                                                 listn.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(listn));

                                                             for (int i = 0; i < listn.size(); i++) {
                                                                 floatListn.add(Float.parseFloat(listn.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatListn));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         List<Entry> scatterEntriesn = new ArrayList<>();
                                                         for (int j = 0; j < floatListn.size(); ++j) {
                                                             scatterEntriesn.add(new Entry(j, floatListn.get(j)));
                                                         }


                                                         chart2 = findViewById(R.id.chart2);
                                                         chart2.getDescription().setEnabled(false);
                                                         chart2.setDrawGridBackground(true);
                                                         chart2.setBackgroundColor(getResources().getColor(R.color.background));

                                                         chart2.setTouchEnabled(true);
                                                         chart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                         chart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                         chart2.setMaxHighlightDistance(50f);
                                                         chart2.setDragEnabled(true);
                                                         chart2.setScaleEnabled(true);
                                                         chart2.setMaxVisibleValueCount(200);
                                                         chart2.setPinchZoom(true);
                                                         Legend l3 = chart2.getLegend();

                                                         YAxis yl3 = chart2.getAxisLeft();
                                                         yl3.setAxisMinimum(0f);
                                                         chart2.getAxisRight().setEnabled(false);
                                                         XAxis xl3 = chart2.getXAxis();
                                                         xl3.setDrawGridLines(false);
                                                         String[] daysS3 = new String[]{"Mn", "Tu", "We", "Th", "Fr", "Sa", "Su"};
                                                         XAxis xAxis3 = chart2.getXAxis();
                                                         xAxis3.setValueFormatter(new IndexAxisValueFormatter(daysS3));
                                                         xAxis3.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                                         xAxis3.setGranularity(1);
                                                         xAxis3.setCenterAxisLabels(true);

                                                         l3.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                                                         l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                                                         l3.setOrientation(Legend.LegendOrientation.VERTICAL);
                                                         l3.setDrawInside(false);
                                                         l3.setXOffset(5f);

                                                         ScatterDataSet setn = new ScatterDataSet(scatterEntriesn, "You");
                                                         setn.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                                                         setn.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                                                         setn.setScatterShapeHoleRadius(4f);


                                                         setn.setScatterShapeSize(10f);

                                                         dataSetn.add(setn); // add the data sets

                                                         ScatterData datan = new ScatterData(dataSetn);
                                                         chart2.setData(datan);
                                                         chart2.invalidate();


                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                                 storageReferencea3.getFile(localFilea3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReadera3 = new InputStreamReader(new FileInputStream(localFilea3.getAbsolutePath()));

                                                             Log.d("FileName", localFilea3.getAbsolutePath());

                                                             BufferedReader bufferedReadera3 = new BufferedReader(inputStreamReadera3);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReadera3.readLine()) != null) {
                                                                 lista3.add(line);
                                                             }
                                                             while ((line = bufferedReadera3.readLine()) != null) {

                                                                 lista3.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(lista3));

                                                             for (int i = 0; i < lista3.size(); i++) {
                                                                 floatLista3.add(Float.parseFloat(lista3.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatLista3));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         List<Entry> scatterEntriesa3 = new ArrayList<>();
                                                         for (int j = 0; j < floatLista3.size(); ++j) {
                                                             scatterEntriesa3.add(new Entry(j, floatLista3.get(j)));
                                                         }

                                                         ScatterDataSet seta3 = new ScatterDataSet(scatterEntriesa3, "Other");
                                                         seta3.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                         seta3.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                                                         seta3.setScatterShapeHoleRadius(3f);


                                                         seta3.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                                                         seta3.setScatterShapeSize(10f);

                                                         dataSetn.add(seta3);

                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                             } catch (IOException exception) {
                                                 exception.printStackTrace();
                                             }
                                         }

                                         //Downloading file and displaying chart
                                     }, delay3);


        //4 -----> chart
        ArrayList<Float> objo = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja3 = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNameo = new File(getCacheDir() + "/reportConcenWhereaiTSC_age.txt");  //Writing data to file
            String line = "";
            FileWriter fwo;
            fwo = new FileWriter(fileNameo);
            BufferedWriter outputo = new BufferedWriter(fwo);
            int size = objo.size();
            for (int i = 0; i < size; i++) {
                outputo.write(objo.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputo.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea4 = new File(getCacheDir() + "/reportConcenWhereaiTCS_ageAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa4;
            fwa4 = new FileWriter(fileNamea4);
            BufferedWriter outputa4 = new BufferedWriter(fwa4);
            int size = obja3.size();
            for (int i = 0; i < size; i++) {
                outputa4.write(obja3.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa4.close();
        } catch (
                IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().

                getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferenceo = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferenceo.child("reportConcenWhereaiTCS_age.txt");
            InputStream stream = new FileInputStream(new File(fileNameo.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea4 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea4.child("reportConcenWhereaiTCS_ageAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea4.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler4 = new Handler();
        final int delay4 = 5000;

        handler4.postDelayed(new

                                     Runnable() {

                                         @Override
                                         public void run() {
                                             StorageReference storageReferenceo = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereaiTCS_age.txt");
                                             StorageReference storageReferencea4 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereaiTCS_ageAvg.txt");
                                             //download and read the file

                                             try {
                                                 localFileo = File.createTempFile("tempFileo", ".txt");
                                                 localFile4a = File.createTempFile("tempFile4a", ".txt");

                                                 texto = localFileo.getAbsolutePath();
                                                 text4a = localFile4a.getAbsolutePath();

                                                 Log.d("Bitmap", texto);
                                                 Log.d("Bitmap", text4a);

                                                 storageReferenceo.getFile(localFileo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReadero = new InputStreamReader(new FileInputStream(localFileo.getAbsolutePath()));

                                                             Log.d("FileName", localFileo.getAbsolutePath());

                                                             BufferedReader bufferedReadero = new BufferedReader(inputStreamReadero);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReadero.readLine()) != null) {
                                                                 listo.add(line);
                                                             }
                                                             while ((line = bufferedReadero.readLine()) != null) {

                                                                 listo.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(listo));

                                                             for (int i = 0; i < listo.size(); i++) {
                                                                 floatListo.add(Float.parseFloat(listo.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatListo));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         List<Entry> scatterEntrieso = new ArrayList<>();
                                                         for (int j = 0; j < floatListo.size(); ++j) {
                                                             scatterEntrieso.add(new Entry(j, floatListo.get(j)));
                                                         }


                                                         chart3 = findViewById(R.id.chart4);
                                                         chart3.getDescription().setEnabled(false);
                                                         chart3.setDrawGridBackground(true);
                                                         chart3.setBackgroundColor(getResources().getColor(R.color.background));

                                                         chart3.setTouchEnabled(true);
                                                         chart3.setMaxHighlightDistance(50f);
                                                         chart3.setDragEnabled(true);
                                                         chart3.setScaleEnabled(true);
                                                         chart3.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                         chart3.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                         chart3.setMaxVisibleValueCount(200);
                                                         chart3.setPinchZoom(true);
                                                         Legend l4 = chart3.getLegend();

                                                         YAxis yl4 = chart3.getAxisLeft();
                                                         yl4.setAxisMinimum(0f);
                                                         chart3.getAxisRight().setEnabled(false);
                                                         XAxis xl4 = chart3.getXAxis();
                                                         xl4.setDrawGridLines(false);
                                                         String[] daysS4 = new String[]{"Mn", "Tu", "We", "Th", "Fr", "Sa", "Su"};
                                                         XAxis xAxis4 = chart3.getXAxis();
                                                         xAxis4.setValueFormatter(new IndexAxisValueFormatter(daysS4));
                                                         xAxis4.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                                         xAxis4.setGranularity(1);
                                                         xAxis4.setCenterAxisLabels(true);

                                                         l4.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                                                         l4.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                                                         l4.setOrientation(Legend.LegendOrientation.VERTICAL);
                                                         l4.setDrawInside(false);
                                                         l4.setXOffset(5f);

                                                         ScatterDataSet seto = new ScatterDataSet(scatterEntrieso, "You");
                                                         seto.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                                                         seto.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                                                         seto.setScatterShapeSize(8f);

                                                         dataSeto.add(seto); // add the data sets

                                                         ScatterData datao = new ScatterData(dataSeto);
                                                         chart3.setData(datao);
                                                         chart3.invalidate();


                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                                 storageReferencea4.getFile(localFile4a).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                         Toast.makeText(ConcentrationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                                                         try {
                                                             InputStreamReader inputStreamReadera4 = new InputStreamReader(new FileInputStream(localFile4a.getAbsolutePath()));

                                                             Log.d("FileName", localFile4a.getAbsolutePath());

                                                             BufferedReader bufferedReadera4 = new BufferedReader(inputStreamReadera4);
                                                             String line = "";

                                                             Log.d("First", line);
                                                             if ((line = bufferedReadera4.readLine()) != null) {
                                                                 lista3.add(line);
                                                             }
                                                             while ((line = bufferedReadera4.readLine()) != null) {

                                                                 lista3.add(line);
                                                                 Log.d("Line", line);
                                                             }

                                                             Log.d("List", String.valueOf(lista3));

                                                             for (int i = 0; i < lista3.size(); i++) {
                                                                 floatLista3.add(Float.parseFloat(lista3.get(i)));
                                                                 Log.d("FloatArrayList", String.valueOf(floatLista3));
                                                             }
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }


                                                         List<Entry> scatterEntriesa4 = new ArrayList<>();
                                                         for (int j = 0; j < floatLista3.size(); ++j) {
                                                             scatterEntriesa4.add(new Entry(j, floatLista3.get(j)));
                                                         }

                                                         ScatterDataSet seta4 = new ScatterDataSet(scatterEntriesa4, "Other");
                                                         seta4.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                                                         seta4.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                                                         seta4.setScatterShapeHoleRadius(3f);

                                                         seta4.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                                                         seta4.setScatterShapeSize(8f);

                                                         dataSeto.add(seta4);

                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });


                                             } catch (IOException exception) {
                                                 exception.printStackTrace();
                                             }
                                         }

                                         //Downloading file and displaying chart
                                     }, delay4);


    }

    public void relaxCQ(View view) {
        Intent intentrelaxCQ = new Intent(getApplicationContext(), RelaxationReportWhereamI.class);
        startActivity(intentrelaxCQ);
    }
}