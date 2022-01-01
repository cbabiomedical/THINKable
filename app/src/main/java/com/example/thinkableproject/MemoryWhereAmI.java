package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MemoryWhereAmI extends AppCompatActivity {

    AppCompatButton progressConcentration;
    File fileName, fileNamea, fileNamem, fileNamea2, fileNamen, fileNamea3, fileNameo, fileNamea4;
    FirebaseUser mUser;
    File localFile, localFilea, localFilem, localFilea2, localFilen, localFilea3, localFileo, localFile4a;
    String text, texta, textm, texta2, textn, texta3, texto, text4a;
    ImageView relaxationBtn, concentrationBtn;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;

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
        setContentView(R.layout.activity_memory_where_am_i);
        progressConcentration = findViewById(R.id.progress);

        relaxationBtn = findViewById(R.id.relaxation);
        concentrationBtn = findViewById(R.id.concentration);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                } else {
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

        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportYearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConcentrationReportYearly.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        progressConcentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportDaily.class);
                startActivity(intent);
            }
        });

        //1----> chart
        //1 -----> chart
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileName = new File(getCacheDir() + "/reportMemWhereaiTR_job.txt");  //Writing data to file
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
            fileNamea = new File(getCacheDir() + "/reportMemWhereamiTR_jobAvg.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageReference1.child("reportMemWhereamiTR_job.txt");
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
            StorageReference mountainsRef = storageReference1a.child("reportMemWhereamiTR_jobAvg.txt");
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

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTR_job.txt");
                StorageReference storageReferencea = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTR_jobAvg.txt");
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


                            List<Entry> scatterEntries = new ArrayList<>();
                            for (int j = 0; j < floatList.size(); ++j) {
                                scatterEntries.add(new Entry(j, floatList.get(j)));
                            }


                            chart = findViewById(R.id.chart1);
                            chart.getDescription().setEnabled(false);
                            chart.setDrawGridBackground(true);
                            chart.setBackgroundColor(getResources().getColor(R.color.background));

                            chart.setTouchEnabled(true);
                            chart.setMaxHighlightDistance(50f);
                            chart.setDragEnabled(true);
                            chart.setScaleEnabled(true);
                            chart.setMaxVisibleValueCount(200);
                            chart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            chart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            chart.setPinchZoom(true);
                            chart.setNoDataText("Data Loading Please Wait...");
                            Legend l = chart.getLegend();

                            YAxis yl = chart.getAxisLeft();
                            yl.setAxisMinimum(0f);
                            chart.getAxisRight().setEnabled(false);
                            XAxis xl = chart.getXAxis();
                            xl.setDrawGridLines(false);
                            String[] daysS = new String[]{"M", "Tu", "W", "Th", "F", "Sa", "Su"};
                            XAxis xAxis = chart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(daysS));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis.setGranularity(1);
                            xAxis.setCenterAxisLabels(true);

                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setDrawInside(false);
                            l.setXOffset(5f);

                            ScatterDataSet set1 = new ScatterDataSet(scatterEntries, "You");
                            set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            set1.setScatterShapeSize(8f);

                            dataSets.add(set1); // add the data sets

                            ScatterData data = new ScatterData(dataSets);
                            chart.setData(data);
                            chart.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


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


                            List<Entry> scatterEntriesa = new ArrayList<>();
                            for (int j = 0; j < floatLista.size(); ++j) {
                                scatterEntriesa.add(new Entry(j, floatLista.get(j)));
                            }

                            ScatterDataSet seta = new ScatterDataSet(scatterEntriesa, "Other");
                            seta.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                            seta.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                            seta.setScatterShapeHoleRadius(3f);

                            seta.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                            seta.setScatterShapeSize(8f);

                            dataSets.add(seta);

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
        }, delay);


        //2 ------> chart

        ArrayList<Float> objm = new ArrayList<>(
                Arrays.asList(80f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja1 = new ArrayList<>(
                Arrays.asList(25f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamem = new File(getCacheDir() + "/reportMemWhereaiTR_age.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea2 = new File(getCacheDir() + "/reportMemWhereamiTR_ageAvg.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference2.child("reportMemWhereamiTR_age.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea2.child("reportMemWhereamiTR_ageAvg.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler1 = new Handler();
        final int delay1 = 5000;

        handler1.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferencem = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTR_age.txt");
                StorageReference storageReferencea2 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTR_ageAvg.txt");
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
                            Toast.makeText(MemoryWhereAmI.this, "Success", Toast.LENGTH_SHORT).show();

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


                            List<Entry> scatterEntriesm = new ArrayList<>();
                            for (int j = 0; j < floatListm.size(); ++j) {
                                scatterEntriesm.add(new Entry(j, floatListm.get(j)));
                            }


                            chart1 = findViewById(R.id.chart3);
                            chart1.getDescription().setEnabled(false);
                            chart1.setDrawGridBackground(true);
                            chart1.setBackgroundColor(getResources().getColor(R.color.background));

                            chart1.setTouchEnabled(true);
                            chart1.setMaxHighlightDistance(50f);
                            chart1.setDragEnabled(true);
                            chart1.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            chart1.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            chart1.setScaleEnabled(true);
                            chart1.setMaxVisibleValueCount(200);
                            chart1.setPinchZoom(true);
                            chart1.setNoDataText("Data Loading Please Wait");
                            Legend l1 = chart1.getLegend();

                            YAxis yl1 = chart1.getAxisLeft();
                            yl1.setAxisMinimum(0f);
                            chart1.getAxisRight().setEnabled(false);
                            XAxis xl1 = chart1.getXAxis();
                            xl1.setDrawGridLines(false);
                            String[] daysS1 = new String[]{"M", "Tu", "W", "Th", "F", "Sa", "Su"};
                            XAxis xAxis1 = chart1.getXAxis();
                            xAxis1.setValueFormatter(new IndexAxisValueFormatter(daysS1));
                            xAxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis1.setGranularity(1);
                            xAxis1.setCenterAxisLabels(true);

                            l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            l1.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l1.setDrawInside(false);
                            l1.setXOffset(5f);

                            ScatterDataSet setm = new ScatterDataSet(scatterEntriesm, "You");
                            setm.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            setm.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            setm.setScatterShapeSize(8f);

                            dataSetsm.add(setm); // add the data sets

                            ScatterData datam = new ScatterData(dataSetsm);
                            chart1.setData(datam);
                            chart1.invalidate();


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
            fileNamen = new File(getCacheDir() + "/reportMemWhereaiTSC_job.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea3 = new File(getCacheDir() + "/reportMemWhereamiTSC_jobAvg.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferencen = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencen.child("reportMemWhereamiTSC_job.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea3 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea3.child("reportMemWhereamiTSC_jobAvg.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler3 = new Handler();
        final int delay3 = 5000;

        handler3.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferencen = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTSC_job.txt");
                StorageReference storageReferencea3 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereamiTSC_jobAvg.txt");
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
                            chart2.setMaxHighlightDistance(50f);
                            chart2.setDragEnabled(true);
                            chart2.setScaleEnabled(true);
                            chart2.setMaxVisibleValueCount(200);
                            chart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            chart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            chart2.setPinchZoom(true);
                            Legend l3 = chart2.getLegend();

                            YAxis yl3 = chart2.getAxisLeft();
                            yl3.setAxisMinimum(0f);
                            chart2.getAxisRight().setEnabled(false);
                            XAxis xl3 = chart2.getXAxis();
                            xl3.setDrawGridLines(false);
                            String[] daysS3 = new String[]{"M", "Tu", "W", "Th", "F", "Sa", "Su"};
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


                            setn.setScatterShapeSize(8f);

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

                            seta3.setScatterShapeSize(8f);

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
            fileNameo = new File(getCacheDir() + "/reportMemWhereaiTSC_age.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea4 = new File(getCacheDir() + "/reportMemWhereaiTCS_ageAvg.txt");  //Writing data to file
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferenceo = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferenceo.child("reportMemWhereaiTCS_age.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea4 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea4.child("reportMemWhereaiTCS_ageAvg.txt");
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler4 = new Handler();
        final int delay4 = 5000;

        handler4.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferenceo = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereaiTCS_age.txt");
                StorageReference storageReferencea4 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMemWhereaiTCS_ageAvg.txt");
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
                            chart3.setTouchEnabled(true);
                            chart3.setMaxHighlightDistance(50f);
                            chart3.setBackgroundColor(getResources().getColor(R.color.background));

                            chart3.setDragEnabled(true);
                            chart3.setScaleEnabled(true);
                            chart3.setMaxVisibleValueCount(200);
                            chart3.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            chart3.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            chart3.setPinchZoom(true);
                            Legend l4 = chart3.getLegend();

                            YAxis yl4 = chart3.getAxisLeft();
                            yl4.setAxisMinimum(0f);
                            chart3.getAxisRight().setEnabled(false);
                            XAxis xl4 = chart3.getXAxis();
                            xl4.setDrawGridLines(false);
                            String[] daysS4 = new String[]{"M", "Tu", "W", "Th", "F", "Sa", "Su"};
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
                            Toast.makeText(MemoryWhereAmI.this, "Success", Toast.LENGTH_SHORT).show();

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