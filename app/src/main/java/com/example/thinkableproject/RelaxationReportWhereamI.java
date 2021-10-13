package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class RelaxationReportWhereamI extends AppCompatActivity {
    AppCompatButton relaxationProgress;

    File fileNamer, fileNamear, fileNamemr, fileNamea2r, fileNamenr, fileNamea3r, fileNameor, fileNamea4r;
    FirebaseUser mUser;
    File localFiler, localFilear, localFilemr, localFilea2r,localFilenr, localFilea3r, localFileor, localFile4ar;
    String textr, textar, textmr, texta2r, textnr, texta3r, textor, text4ar;

    //for chart4aa 1
    ArrayList<String> listr = new ArrayList<>();
    ArrayList<Float> floatListr = new ArrayList<>();
    ArrayList<String> listar = new ArrayList<>();
    ArrayList<Float> floatListar = new ArrayList<>();
    //for chart4aa 2
    ArrayList<String> listmr = new ArrayList<>();
    ArrayList<Float> floatListmr = new ArrayList<>();

    //for chart4aa 3
    ArrayList<String> listnr = new ArrayList<>();
    ArrayList<Float> floatListnr = new ArrayList<>();
    ArrayList<String> lista2r = new ArrayList<>();
    ArrayList<Float> floatLista2r = new ArrayList<>();
    //for chart4aa 4
    ArrayList<String> listor = new ArrayList<>();
    ArrayList<Float> floatListor = new ArrayList<>();
    ArrayList<String> lista3r = new ArrayList<>();
    ArrayList<Float> floatLista3r = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetsr = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetsmr = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetnr = new ArrayList<>();

    ArrayList<IScatterDataSet> dataSetor = new ArrayList<>();

    private ScatterChart chart4aa, chart5, chart6, chart7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_wheream_i);
        relaxationProgress=findViewById(R.id.progressrel);

        relaxationProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RelaxationReportDaily.class);
                startActivity(intent);
            }
        });

//        1----> chart4aa
//        1 -----> chart4aa
        ArrayList<Float> objr = new ArrayList<>(
                Arrays.asList(30f, 86f, 40f, 50f, 20f, 60f, 80f));

        ArrayList<Float> objar = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamer = new File(getCacheDir() + "/reportRelaxWhereamiTR_job.txt");  //Writing data to file
            String line = "";
            FileWriter fwr;
            fwr = new FileWriter(fileNamer);
            BufferedWriter outputr = new BufferedWriter(fwr);
            int size = objr.size();
            for (int i = 0; i < size; i++) {
                outputr.write(objr.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputr.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamear = new File(getCacheDir() + "/reportRelaxWhereamiTR_jobAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwar;
            fwar = new FileWriter(fileNamear);
            BufferedWriter outputar = new BufferedWriter(fwar);
            int size = objar.size();
            for (int i = 0; i < size; i++) {
                outputar.write(objar.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputar.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference1r = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1r.child("reportRelaxWhereamiTR_job.txt");
            InputStream stream = new FileInputStream(new File(fileNamer.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReference1ar = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1ar.child("reportRelaxWhereamiTR_jobAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamear.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handlerr = new Handler();
        final int delayr = 5000;

        handlerr.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferencer = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTR_job.txt");
                StorageReference storageReferencear = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTR_jobAvg.txt");
                //download and read the file

                try {
                    localFiler = File.createTempFile("tempFiler", ".txt");
                    localFilear = File.createTempFile("tempFilear", ".txt");

                    textr = localFiler.getAbsolutePath();
                    textar = localFilear.getAbsolutePath();

                    Log.d("Bitmap", textr);
                    Log.d("Bitmap", textar);

                    storageReferencer.getFile(localFiler).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReaderr = new InputStreamReader(new FileInputStream(localFiler.getAbsolutePath()));

                                Log.d("FileName", localFiler.getAbsolutePath());

                                BufferedReader bufferedReaderr = new BufferedReader(inputStreamReaderr);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReaderr.readLine()) != null) {
                                    listr.add(line);
                                }
                                while ((line = bufferedReaderr.readLine()) != null) {

                                    listr.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(listr));

                                for (int i = 0; i < listr.size(); i++) {
                                    floatListr.add(Float.parseFloat(listr.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatListr));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            List<Entry> scatterEntries = new ArrayList<>();
                            for (int j = 0; j < floatListr.size(); ++j) {
                                scatterEntries.add(new Entry(j, floatListr.get(j)));
                            }


                            chart4aa = findViewById(R.id.chartw);
                            Description descChartDescription = new Description();
                            descChartDescription.setEnabled(true);
                            chart4aa.setDescription(descChartDescription);
                            chart4aa.setDrawGridBackground(false);
                            chart4aa.setTouchEnabled(true);
                            chart4aa.setMaxHighlightDistance(50f);
                            chart4aa.setDragEnabled(true);
                            chart4aa.setScaleEnabled(true);
                            chart4aa.setMaxVisibleValueCount(200);
                            chart4aa.setPinchZoom(true);
                            Legend lr = chart4aa.getLegend();

                            YAxis ylr = chart4aa.getAxisLeft();
                            ylr.setAxisMinimum(0f);
                            chart4aa.getAxisRight().setEnabled(false);
                            XAxis xlr = chart4aa.getXAxis();
                            xlr.setDrawGridLines(false);
                            String[] daysS = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                            XAxis xAxisr = chart4aa.getXAxis();
                            xAxisr.setValueFormatter(new IndexAxisValueFormatter(daysS));
                            xAxisr.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxisr.setGranularity(1);
                            xAxisr.setCenterAxisLabels(true);

                            lr.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            lr.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            lr.setOrientation(Legend.LegendOrientation.VERTICAL);
                            lr.setDrawInside(false);
                            lr.setXOffset(5f);

                            ScatterDataSet set1r = new ScatterDataSet(scatterEntries, "You");
                            set1r.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            set1r.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            set1r.setScatterShapeSize(8f);

                            dataSetsr.add(set1r); // add the datar sets

                            ScatterData datar = new ScatterData(dataSetsr);
                            chart4aa.setData(datar);
                            chart4aa.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                    storageReferencear.getFile(localFilear).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReaderar = new InputStreamReader(new FileInputStream(localFilear.getAbsolutePath()));

                                Log.d("FileName", localFilear.getAbsolutePath());

                                BufferedReader bufferedReaderar = new BufferedReader(inputStreamReaderar);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReaderar.readLine()) != null) {
                                    listar.add(line);
                                }
                                while ((line = bufferedReaderar.readLine()) != null) {

                                    listar.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(listar));

                                for (int i = 0; i < listar.size(); i++) {
                                    floatListar.add(Float.parseFloat(listar.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatListar));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            List<Entry> scatterEntriesar = new ArrayList<>();
                            for (int j = 0; j < floatListar.size(); ++j) {
                                scatterEntriesar.add(new Entry(j, floatListar.get(j)));
                            }

                            ScatterDataSet setar = new ScatterDataSet(scatterEntriesar, "Other");
                            setar.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                            setar.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                            setar.setScatterShapeHoleRadius(3f);

                            setar.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                            setar.setScatterShapeSize(8f);

                            dataSetsr.add(setar);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart4aa
        }, delayr);


        //2 ------> chart5

        ArrayList<Float> objmr = new ArrayList<>(
                Arrays.asList(80f, 86f, 10f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja1r = new ArrayList<>(
                Arrays.asList(25f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamemr = new File(getCacheDir() + "/reportRelaxWhereaiTR_age.txt");  //Writing data to file
            String line = "";
            FileWriter fwmr;
            fwmr = new FileWriter(fileNamemr);
            BufferedWriter outputmr = new BufferedWriter(fwmr);
            int size = objmr.size();
            for (int i = 0; i < size; i++) {
                outputmr.write(objmr.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputmr.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea2r = new File(getCacheDir() + "/reportRelaxWhereamiTR_ageAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa2r;
            fwa2r = new FileWriter(fileNamea2r);
            BufferedWriter outputa2r = new BufferedWriter(fwa2r);
            int size = obja1r.size();
            for (int i = 0; i < size; i++) {
                outputa2r.write(obja1r.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa2r.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference2r = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference2r.child("reportRelaxWhereamiTR_age.txt");
            InputStream stream = new FileInputStream(new File(fileNamemr.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea2r = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea2r.child("reportRelaxWhereamiTR_ageAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea2r.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler1r = new Handler();
        final int delay1r = 5000;

        handler1r.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferencemr = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_age.txt");
                StorageReference storageReferencea2r = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportConcenWhereamiTR_ageAvg.txt");
                //download and read the file

                try {
                    localFilemr = File.createTempFile("tempFilemr", ".txt");
                    localFilea2r = File.createTempFile("tempFilea2r", ".txt");

                    textmr = localFilemr.getAbsolutePath();
                    texta2r = localFilea2r.getAbsolutePath();

                    Log.d("Bitmap", textmr);
                    Log.d("Bitmap", texta2r);

                    storageReferencemr.getFile(localFilemr).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadermr = new InputStreamReader(new FileInputStream(localFilemr.getAbsolutePath()));

                                Log.d("FileName", localFilemr.getAbsolutePath());

                                BufferedReader bufferedReadermr = new BufferedReader(inputStreamReadermr);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadermr.readLine()) != null) {
                                    listmr.add(line);
                                }
                                while ((line = bufferedReadermr.readLine()) != null) {

                                    listmr.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(listmr));

                                for (int i = 0; i < listmr.size(); i++) {
                                    floatListmr.add(Float.parseFloat(listmr.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatListmr));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            List<Entry> scatterEntriesmr = new ArrayList<>();
                            for (int j = 0; j < floatListmr.size(); ++j) {
                                scatterEntriesmr.add(new Entry(j, floatListmr.get(j)));
                            }


                            chart5 = findViewById(R.id.chartx);
                            chart5.getDescription().setEnabled(false);
                            chart5.setDrawGridBackground(false);
                            chart5.setTouchEnabled(true);
                            chart5.setMaxHighlightDistance(50f);
                            chart5.setDragEnabled(true);
                            chart5.setScaleEnabled(true);
                            chart5.setMaxVisibleValueCount(200);
                            chart5.setPinchZoom(true);
                            Legend l1r = chart5.getLegend();

                            YAxis yl1r = chart5.getAxisLeft();
                            yl1r.setAxisMinimum(0f);
                            chart5.getAxisRight().setEnabled(false);
                            XAxis xl1r = chart5.getXAxis();
                            xl1r.setDrawGridLines(false);
                            String[] daysS1r = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
                            XAxis xAxis1r = chart5.getXAxis();
                            xAxis1r.setValueFormatter(new IndexAxisValueFormatter(daysS1r));
                            xAxis1r.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis1r.setGranularity(1);
                            xAxis1r.setCenterAxisLabels(true);

                            l1r.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l1r.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            l1r.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l1r.setDrawInside(false);
                            l1r.setXOffset(5f);

                            ScatterDataSet setmr = new ScatterDataSet(scatterEntriesmr, "You");
                            setmr.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            setmr.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            setmr.setScatterShapeSize(8f);

                            dataSetsmr.add(setmr); // add the data sets

                            ScatterData datamr = new ScatterData(dataSetsmr);
                            chart5.setData(datamr);
                            chart5.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                    storageReferencea2r.getFile(localFilea2r).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadera2r = new InputStreamReader(new FileInputStream(localFilea2r.getAbsolutePath()));

                                Log.d("FileName", localFilea2r.getAbsolutePath());

                                BufferedReader bufferedReadera2r = new BufferedReader(inputStreamReadera2r);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadera2r.readLine()) != null) {
                                    lista2r.add(line);
                                }
                                while ((line = bufferedReadera2r.readLine()) != null) {

                                    lista2r.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(lista2r));

                                for (int i = 0; i < lista2r.size(); i++) {
                                    floatLista2r.add(Float.parseFloat(lista2r.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatLista2r));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            List<Entry> scatterEntriesa2r = new ArrayList<>();
                            for (int j = 0; j < floatLista2r.size(); ++j) {
                                scatterEntriesa2r.add(new Entry(j, floatLista2r.get(j)));
                            }

                            ScatterDataSet seta2r = new ScatterDataSet(scatterEntriesa2r, "Other");
                            seta2r.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                            seta2r.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                            seta2r.setScatterShapeHoleRadius(3f);

                            seta2r.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                            seta2r.setScatterShapeSize(8f);

                            dataSetsmr.add(seta2r);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay1r);

        //3 -----> chart4aa
        ArrayList<Float> objnr = new ArrayList<>(
                Arrays.asList(30f, 86f, 40f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja2r = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNamenr = new File(getCacheDir() + "/reportRelaxWhereamiTCS_job.txt");  //Writing data to file
            String line = "";
            FileWriter fwnr;
            fwnr = new FileWriter(fileNamenr);
            BufferedWriter outputnr = new BufferedWriter(fwnr);
            int size = objnr.size();
            for (int i = 0; i < size; i++) {
                outputnr.write(objnr.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputnr.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea3r = new File(getCacheDir() + "/reportRelaxWhereamiTCS_jobAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa3r;
            fwa3r = new FileWriter(fileNamea3r);
            BufferedWriter outputa3r = new BufferedWriter(fwa3r);
            int size = obja2r.size();
            for (int i = 0; i < size; i++) {
                outputa3r.write(obja2r.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa3r.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferencenr = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencenr.child("reportRelaxWhereamiTCS_job.txt");
            InputStream stream = new FileInputStream(new File(fileNamenr.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea3r = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea3r.child("reportRelaxWhereamiTCS_jobAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea3r.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler3r = new Handler();
        final int delay3r = 5000;

        handler3r.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferencenr = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTCS_job.txt");
                StorageReference storageReferencea3r = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTCS_jobAvg.txt");
                //download and read the file

                try {
                    localFilenr = File.createTempFile("tempFilenr", ".txt");
                    localFilea3r = File.createTempFile("tempFilea3r", ".txt");

                    textnr = localFilenr.getAbsolutePath();
                    texta3r = localFilea3r.getAbsolutePath();

                    Log.d("Bitmap", textnr);
                    Log.d("Bitmap", texta3r);

                    storageReferencenr.getFile(localFilenr).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadernr = new InputStreamReader(new FileInputStream(localFilenr.getAbsolutePath()));

                                Log.d("FileName", localFilenr.getAbsolutePath());

                                BufferedReader bufferedReadernr = new BufferedReader(inputStreamReadernr);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadernr.readLine()) != null) {
                                    listnr.add(line);
                                }
                                while ((line = bufferedReadernr.readLine()) != null) {

                                    listnr.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(listnr));

                                for (int i = 0; i < listnr.size(); i++) {
                                    floatListnr.add(Float.parseFloat(listnr.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatListnr));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            List<Entry> scatterEntriesnr = new ArrayList<>();
                            for (int j = 0; j < floatListnr.size(); ++j) {
                                scatterEntriesnr.add(new Entry(j, floatListnr.get(j)));
                            }


                            chart6 = findViewById(R.id.charty);
                            Description descChartDescription = new Description();
                            descChartDescription.setEnabled(true);
                            chart6.setDescription(descChartDescription);
                            chart6.setDrawGridBackground(false);
                            chart6.setTouchEnabled(true);
                            chart6.setMaxHighlightDistance(50f);
                            chart6.setDragEnabled(true);
                            chart6.setScaleEnabled(true);
                            chart6.setMaxVisibleValueCount(200);
                            chart6.setPinchZoom(true);
                            Legend l3r = chart6.getLegend();

                            YAxis yl3r = chart6.getAxisLeft();
                            yl3r.setAxisMinimum(0f);
                            chart6.getAxisRight().setEnabled(false);
                            XAxis xl3r = chart6.getXAxis();
                            xl3r.setDrawGridLines(false);
                            String[] daysS3r = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
                            XAxis xAxis3r = chart6.getXAxis();
                            xAxis3r.setValueFormatter(new IndexAxisValueFormatter(daysS3r));
                            xAxis3r.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis3r.setGranularity(1);
                            xAxis3r.setCenterAxisLabels(true);

                            l3r.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l3r.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            l3r.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l3r.setDrawInside(false);
                            l3r.setXOffset(5f);

                            ScatterDataSet setnr = new ScatterDataSet(scatterEntriesnr, "You");
                            setnr.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            setnr.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            setnr.setScatterShapeSize(8f);

                            dataSetnr.add(setnr); // add the data sets

                            ScatterData datanr = new ScatterData(dataSetnr);
                            chart6.setData(datanr);
                            chart6.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                    storageReferencea3r.getFile(localFilea3r).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadera3r = new InputStreamReader(new FileInputStream(localFilea3r.getAbsolutePath()));

                                Log.d("FileName", localFilea3r.getAbsolutePath());

                                BufferedReader bufferedReadera3r = new BufferedReader(inputStreamReadera3r);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadera3r.readLine()) != null) {
                                    lista3r.add(line);
                                }
                                while ((line = bufferedReadera3r.readLine()) != null) {

                                    lista3r.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(lista3r));

                                for (int i = 0; i < lista3r.size(); i++) {
                                    floatLista3r.add(Float.parseFloat(lista3r.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatLista3r));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            List<Entry> scatterEntriesa3r = new ArrayList<>();
                            for (int j = 0; j < floatLista3r.size(); ++j) {
                                scatterEntriesa3r.add(new Entry(j, floatLista3r.get(j)));
                            }

                            ScatterDataSet seta3r = new ScatterDataSet(scatterEntriesa3r, "Other");
                            seta3r.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                            seta3r.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                            seta3r.setScatterShapeHoleRadius(3f);

                            seta3r.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                            seta3r.setScatterShapeSize(8f);

                            dataSetnr.add(seta3r);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart4aa
        }, delay3r);

        //4 -----> chart4aa
        ArrayList<Float> objor = new ArrayList<>(
                Arrays.asList(30f, 86f, 40f, 50f, 20f, 60f, 80f));

        ArrayList<Float> obja3r = new ArrayList<>(
                Arrays.asList(50f, 56f, 20f, 40f, 50f, 40f, 89f));// Avearage Array listr to write data to file

        try {
            fileNameor = new File(getCacheDir() + "/reportRelaxWhereamiTCS_job.txt");  //Writing data to file
            String line = "";
            FileWriter fwor;
            fwor = new FileWriter(fileNameor);
            BufferedWriter outputor = new BufferedWriter(fwor);
            int size = objor.size();
            for (int i = 0; i < size; i++) {
                outputor.write(objor.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputor.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //Avg
        try {
            fileNamea4r = new File(getCacheDir() + "/reportRelaxWhereamiTCS_jobAvg.txt");  //Writing data to file
            String line = "";
            FileWriter fwa4r;
            fwa4r = new FileWriter(fileNamea4r);
            BufferedWriter outputa4r = new BufferedWriter(fwa4r);
            int size = obja3r.size();
            for (int i = 0; i < size; i++) {
                outputa4r.write(obja3r.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            outputa4r.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReferenceor = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferenceor.child("reportRelaxWhereamiTCS_job.txt");
            InputStream stream = new FileInputStream(new File(fileNameor.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Avg

        StorageReference storageReferencea4r = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReferencea4r.child("reportRelaxWhereamiTCS_jobAvg.txt");
            InputStream stream = new FileInputStream(new File(fileNamea4r.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWhereamI.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler4r = new Handler();
        final int delay4r = 1000;

        handler4r.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReferenceor = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTCS_job.txt");
                StorageReference storageReferencea4r = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportRelaxWhereamiTCS_jobAvg.txt");
                //download and read the file

                try {
                    localFileor = File.createTempFile("tempFileor", ".txt");
                    localFile4ar = File.createTempFile("tempFile4ar", ".txt");

                    textor = localFileor.getAbsolutePath();
                    text4ar = localFile4ar.getAbsolutePath();

                    Log.d("Bitmap", textor);
                    Log.d("Bitmap", text4ar);

                    storageReferenceor.getFile(localFileor).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReaderor = new InputStreamReader(new FileInputStream(localFileor.getAbsolutePath()));

                                Log.d("FileName", localFileor.getAbsolutePath());

                                BufferedReader bufferedReaderor = new BufferedReader(inputStreamReaderor);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReaderor.readLine()) != null) {
                                    listor.add(line);
                                }
                                while ((line = bufferedReaderor.readLine()) != null) {

                                    listor.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(listor));

                                for (int i = 0; i < listor.size(); i++) {
                                    floatListor.add(Float.parseFloat(listor.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatListor));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            List<Entry> scatterEntriesor = new ArrayList<>();
                            for (int j = 0; j < floatListor.size(); ++j) {
                                scatterEntriesor.add(new Entry(j, floatListor.get(j)));
                            }


                            chart7 = findViewById(R.id.chartz);
                            Description descChartDescription = new Description();
                            descChartDescription.setEnabled(true);
                            chart7.setDescription(descChartDescription);
                            chart7.setDrawGridBackground(false);
                            chart7.setTouchEnabled(true);
                            chart7.setMaxHighlightDistance(50f);
                            chart7.setDragEnabled(true);
                            chart7.setScaleEnabled(true);
                            chart7.setMaxVisibleValueCount(200);
                            chart7.setPinchZoom(true);
                            Legend l4r = chart7.getLegend();

                            YAxis yl4r = chart7.getAxisLeft();
                            yl4r.setAxisMinimum(0f);
                            chart7.getAxisRight().setEnabled(false);
                            XAxis xl4r = chart7.getXAxis();
                            xl4r.setDrawGridLines(false);
                            String[] daysS4r = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                            XAxis xAxis4r = chart7.getXAxis();
                            xAxis4r.setValueFormatter(new IndexAxisValueFormatter(daysS4r));
                            xAxis4r.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            xAxis4r.setGranularity(1);
                            xAxis4r.setCenterAxisLabels(true);

                            l4r.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l4r.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                            l4r.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l4r.setDrawInside(false);
                            l4r.setXOffset(5f);

                            ScatterDataSet setor = new ScatterDataSet(scatterEntriesor, "You");
                            setor.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                            setor.setColor(ColorTemplate.COLORFUL_COLORS[0]);


                            setor.setScatterShapeSize(8f);

                            dataSetor.add(setor); // add the data sets

                            ScatterData dataor = new ScatterData(dataSetor);
                            chart7.setData(dataor);
                            chart7.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                    storageReferencea4r.getFile(localFile4ar).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReadera4r = new InputStreamReader(new FileInputStream(localFile4ar.getAbsolutePath()));

                                Log.d("FileName", localFile4ar.getAbsolutePath());

                                BufferedReader bufferedReadera4r = new BufferedReader(inputStreamReadera4r);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReadera4r.readLine()) != null) {
                                    lista3r.add(line);
                                }
                                while ((line = bufferedReadera4r.readLine()) != null) {

                                    lista3r.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(lista3r));

                                for (int i = 0; i < lista3r.size(); i++) {
                                    floatLista3r.add(Float.parseFloat(lista3r.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatLista3r));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            List<Entry> scatterEntriesa4r = new ArrayList<>();
                            for (int j = 0; j < floatLista3r.size(); ++j) {
                                scatterEntriesa4r.add(new Entry(j, floatLista3r.get(j)));
                            }

                            ScatterDataSet seta4r = new ScatterDataSet(scatterEntriesa4r, "Other");
                            seta4r.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                            seta4r.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);
                            seta4r.setScatterShapeHoleRadius(3f);

                            seta4r.setColor(ColorTemplate.COLORFUL_COLORS[1]);

                            seta4r.setScatterShapeSize(8f);

                            dataSetor.add(seta4r);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWhereamI.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart4aa
        }, delay4r);


















    }

    public void concq(View view) {
        Intent intentconcq = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
        startActivity(intentconcq);
    }
}