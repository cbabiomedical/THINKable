package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class ConcentrationReportMonthly extends AppCompatActivity {
    BarChart barChart, barChart2;
    private Context context;
    File fileName, localFile, fileName1, localFile1;
    String text;
    AppCompatButton daily, weekly, yearly, whereAmI;
    FirebaseUser mUser;
    ImageView relaxationBtn, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<Float> floatList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration_report_monthly);
// Adding identifier to front end elements
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        yearly = findViewById(R.id.yearly);
        barChart = (BarChart) findViewById(R.id.barChartMonthly);
        barChart2 = findViewById(R.id.barChartMonthly2);
        relaxationBtn = findViewById(R.id.relaxation);
        whereAmI = findViewById(R.id.whereAmI);
        memory = findViewById(R.id.memory);

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoryReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.reports);

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
        //Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 85f, 10f, 50f, 20f, 60f, 80f, 43f, 23f, 70f, 73f, 10f));  //Array list to write data to file
        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/reportMonthly.txt");
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
        // Uploading file created to firebase storage
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1.child("reportMonthly.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        final int delay = 7000;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMonthly.txt");
                //downloading the uploaded file and storing in arraylist
                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportMonthly.this, "Success", Toast.LENGTH_SHORT).show();

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

                            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                            List<Float> credits = new ArrayList<>(Arrays.asList(90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f));
                            float[] strength = new float[]{90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f};

                            List<BarEntry> entries = new ArrayList<>();
                            for (int j = 0; j < floatList.size(); ++j) {
                                entries.add(new BarEntry(j, floatList.get(j)));
                            }

                            float textSize = 16f;
                            //Initializing object of MyBarDataset class
                            MyBarDataset dataSet = new MyBarDataset(entries, "data", credits);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.9f);

                            barChart.setData(data);
                            barChart.setFitBars(true);
                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart.getXAxis().setTextSize(textSize);
                            barChart.getAxisLeft().setTextSize(textSize);
                            barChart.setExtraBottomOffset(10f);

                            barChart.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("");
                            barChart.setDescription(desc);
                            barChart.getLegend().setEnabled(false);
                            barChart.getXAxis().setDrawGridLines(false);
                            barChart.getAxisLeft().setDrawGridLines(false);
                            barChart.setNoDataText("Data Loading Please Wait....");

                            barChart.invalidate();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportMonthly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }, delay);
        //Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj1 = new ArrayList<>(
                Arrays.asList(30f, 10f, 60f, 40f, 20f, 60f, 10f, 45f, 25f, 70f, 75f, 35f));  //Array list to write data to file
        //Write input data to file
        try {
            fileName1 = new File(getCacheDir() + "/reportMonthly2.txt");  //Writing data to file
            String line = "";
            FileWriter fw;
            fw = new FileWriter(fileName1);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj1.size();
            for (int i = 0; i < size; i++) {
                output.write(obj1.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            //downloading the uploaded file and storing in arraylist
            StorageReference mountainsRef = storageReference1.child("reportMonthly2.txt");
            InputStream stream = new FileInputStream(new File(fileName1.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
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
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/reportMonthly2.txt");

                try {
                    localFile1 = File.createTempFile("tempFile", ".txt");
                    text = localFile1.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ConcentrationReportMonthly.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReader1 = new InputStreamReader(new FileInputStream(localFile1.getAbsolutePath()));

                                Log.d("FileName", localFile1.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader1);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list1.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list1.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list1));

                                for (int i = 0; i < list1.size(); i++) {
                                    floatList1.add(Float.parseFloat(list1.get(i)));
                                    Log.d("FloatArrayList2", String.valueOf(floatList1));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String[] months1 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                            List<Float> credits1 = new ArrayList<>(Arrays.asList(90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f));
                            float[] strength = new float[]{90f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 15f, 85f, 30f};

                            List<BarEntry> entries2 = new ArrayList<>();
                            for (int j = 0; j < floatList1.size(); ++j) {
                                entries2.add(new BarEntry(j, floatList1.get(j)));
                            }
                            float textSize = 16f;
                            MyBarDataset dataSet1 = new MyBarDataset(entries2, "data", credits1);
                            dataSet1.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data1 = new BarData(dataSet1);
                            data1.setDrawValues(false);
                            data1.setBarWidth(0.9f);

                            barChart2.setData(data1);
                            barChart2.setFitBars(true);
                            barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months1));
                            barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart2.getXAxis().setTextSize(textSize);
                            barChart2.getAxisLeft().setTextSize(textSize);
                            barChart2.setExtraBottomOffset(10f);

                            barChart2.getAxisRight().setEnabled(false);
                            Description desc1 = new Description();
                            desc1.setText("");
                            barChart2.setDescription(desc1);
                            barChart2.getLegend().setEnabled(false);
                            barChart2.getXAxis().setDrawGridLines(false);
                            barChart2.getAxisLeft().setDrawGridLines(false);
                            barChart2.setNoDataText("Data Loading Please Wait...");
                            barChart2.invalidate();

//
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ConcentrationReportMonthly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            //Downloading file and displaying chart
        }, delay);
        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportYearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportMonthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of where am i toggle button
        whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
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
}