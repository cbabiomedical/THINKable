package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class RelaxationReportWeekly extends AppCompatActivity {

    BarChart barChartWeeklytimeto, barChartWeeklytimestayed;
    AppCompatButton daily, yearly, monthly, whereAmI, progress, timetorel, timestayedrel;
    FirebaseUser mUser;
    String text;
    String text2;
    File localFile;
    File localFile2;
    File fileName;
    File fileName2;
    ImageButton concentration, memory;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<Float> floatList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_report_weekly);

        //Initialize buttons
        whereAmI = findViewById(R.id.whereAmI);
        progress = findViewById(R.id.progress);
        timetorel = findViewById(R.id.timetorel);
        timestayedrel = findViewById(R.id.timestayedrel);
        //Initialize bar chart
        barChartWeeklytimeto = findViewById(R.id.barChartWeeklytimeto);
        barChartWeeklytimestayed = findViewById(R.id.barChartWeeklytimestayed);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");
        //Initialize List entries
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        //Initialize buttons
        concentration = findViewById(R.id.concentration);
        daily = findViewById(R.id.daily);
        yearly = findViewById(R.id.yearly);
        monthly = findViewById(R.id.monthly);
        memory = findViewById(R.id.memory);
        //go to relaxation daily page
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportDaily.class);
                startActivity(intentr1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        //go to relaxation monthly page
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportMonthly.class);
                startActivity(intentr1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        //go to relaxation yearly page
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportYearly.class);
                startActivity(intentr1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        //go to RQ Page with comparison to occupation and age factors
        whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentr1 = new Intent(getApplicationContext(), RelaxationReportWhereamI.class);
                startActivity(intentr1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //go to concentration report weekly page
        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //Array list to write data to file
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f));  //Array list to write data to file

        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/relaxationweeklytimeto.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageReference1.child("relaxationweeklytimeto.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        final int delay = 5000;

        //initialize file handler
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Downloading file and displaying chart
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/relaxationweeklytimeto.txt");

                try {
                    localFile = File.createTempFile("tempFile", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWeekly.this, "Success", Toast.LENGTH_SHORT).show();

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
                            Log.d("floatListTest", String.valueOf(floatList));
                            String[] weeks = new String[]{"One", "Two", "Three", "Four"};
                            List<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 10f));
                            float[] strengthWeek = new float[]{90f, 30f, 70f, 10f};

                            for (int j = 0; j < floatList.size(); ++j) {
                                entries.add(new BarEntry(j, floatList.get(j)));
                            }


                            float textSize = 16f;
                            RelaxationReportWeekly.MyBarDataset dataSet = new RelaxationReportWeekly.MyBarDataset(entries, "data", creditsWeek);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.8f);

                            barChartWeeklytimeto.setData(data);
                            barChartWeeklytimeto.setFitBars(true);
                            barChartWeeklytimeto.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weeks));
                            barChartWeeklytimeto.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartWeeklytimeto.getXAxis().setTextSize(textSize);
                            barChartWeeklytimeto.getAxisLeft().setTextSize(textSize);
                            barChartWeeklytimeto.setExtraBottomOffset(10f);

                            barChartWeeklytimeto.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("");
                            barChartWeeklytimeto.setDescription(desc);
                            barChartWeeklytimeto.getLegend().setEnabled(false);
                            barChartWeeklytimeto.getXAxis().setDrawGridLines(false);
                            barChartWeeklytimeto.getAxisLeft().setDrawGridLines(false);
                            barChartWeeklytimeto.setNoDataText("Data Loading Please Wait...");

                            barChartWeeklytimeto.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWeekly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }, delay);


        //Array list to write data to file
        ArrayList<Float> obj2 = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f));  //Array list to write data to file

        //Writing data to file
        try {
            fileName2 = new File(getCacheDir() + "/relaxationweeklytimestayed.txt");  //Writing data to file
            String line = "";
            FileWriter fw;
            fw = new FileWriter(fileName2);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj2.size();
            for (int i = 0; i < size; i++) {
                output.write(obj2.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference2.child("relaxationweeklytimestayed.txt");
            InputStream stream = new FileInputStream(new File(fileName2.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RelaxationReportWeekly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //initialize file handler
        final Handler handler2 = new Handler();
        final int delay2 = 5000;

        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Downloading file and displaying chart
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/relaxationweeklytimestayed.txt");

                try {
                    localFile2 = File.createTempFile("tempFile1", ".txt");
                    text2 = localFile2.getAbsolutePath();
                    Log.d("Bitmap", text2);
                    storageReference.getFile(localFile2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(RelaxationReportWeekly.this, "Success", Toast.LENGTH_SHORT).show();

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile2.getAbsolutePath()));

                                Log.d("FileName", localFile2.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    list2.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    list2.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("List", String.valueOf(list2));

                                for (int i = 0; i < list2.size(); i++) {
                                    floatList2.add(Float.parseFloat(list2.get(i)));
                                    Log.d("FloatArrayList", String.valueOf(floatList2));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("floatListTest", String.valueOf(floatList2));
                            String[] weeks = new String[]{"One", "Two", "Three", "Four"};
                            List<Float> creditsWeek = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 10f));
                            float[] strengthWeek = new float[]{90f, 30f, 70f, 10f};

                            for (int j = 0; j < floatList2.size(); ++j) {
                                entries2.add(new BarEntry(j, floatList2.get(j)));
                            }


                            float textSize = 16f;
                            RelaxationReportWeekly.MyBarDataset dataSet = new RelaxationReportWeekly.MyBarDataset(entries2, "data", creditsWeek);
                            dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                    ContextCompat.getColor(getApplicationContext(), R.color.Ldark),
                                    ContextCompat.getColor(getApplicationContext(), R.color.dark));
                            BarData data = new BarData(dataSet);
                            data.setDrawValues(false);
                            data.setBarWidth(0.8f);

                            barChartWeeklytimestayed.setData(data);
                            barChartWeeklytimestayed.setFitBars(true);
                            barChartWeeklytimestayed.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weeks));
                            barChartWeeklytimestayed.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChartWeeklytimestayed.getXAxis().setTextSize(textSize);
                            barChartWeeklytimestayed.getAxisLeft().setTextSize(textSize);
                            barChartWeeklytimestayed.setExtraBottomOffset(10f);

                            barChartWeeklytimestayed.getAxisRight().setEnabled(false);
                            Description desc = new Description();
                            desc.setText("");
                            barChartWeeklytimestayed.setDescription(desc);
                            barChartWeeklytimestayed.getLegend().setEnabled(false);
                            barChartWeeklytimestayed.getXAxis().setDrawGridLines(false);
                            barChartWeeklytimestayed.getAxisLeft().setDrawGridLines(false);

                            barChartWeeklytimestayed.invalidate();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RelaxationReportWeekly.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }, delay2);

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
    }

    //set up x and y axis data
    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        //set up color of bars on chart
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

    //delete temporary file
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            deleteTempFiles(getCacheDir());
        }
    }

    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }

}