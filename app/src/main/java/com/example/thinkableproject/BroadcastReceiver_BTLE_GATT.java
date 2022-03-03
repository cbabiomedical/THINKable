package com.example.thinkableproject;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thinkableproject.DragandDropPuzzle.PuzzleActivity;
import com.example.thinkableproject.WordMatching.TwoByFiveGrid;
import com.example.thinkableproject.WordMatching.TwoByFourGrid;
import com.example.thinkableproject.WordMatching.TwoBySixGrid;
import com.example.thinkableproject.WordMatching.TwoByThreeGrid;
import com.example.thinkableproject.WordMatching.TwoByTwoGrid;
import com.example.thinkableproject.duckhunt.GameView;
import com.example.thinkableproject.ninjadarts.GamePanel;
import com.example.thinkableproject.pianotiles.MainActivity;
import com.example.thinkableproject.puzzle.GameActivity15;
import com.example.thinkableproject.puzzle.GameActivity24;
import com.example.thinkableproject.puzzle.GameActivity9;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.example.thinkableproject.spaceshooter.SpaceShooter;
import com.example.thinkableproject.spaceshooter.StartUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

/**
 * Created by Kelvin on 5/8/16.
 */
public class BroadcastReceiver_BTLE_GATT extends BroadcastReceiver {
    private boolean mConnected = false;
    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;
    private ExpandableListView expandableListView;

    public static ArrayList dataValues = new ArrayList();
    public static final String EXTRA_NAME = "android.aviles.bletutorial.Activity_BTLE_Services.NAME";
    public static final String EXTRA_ADDRESS = "android.aviles.bletutorial.Activity_BTLE_Services.ADDRESS";
    private ListAdapter_BTLE_Services expandableListAdapter;
    boolean df = false;

    BroadcastReceiver_BTLE_GATT broadcastReceiver_btle_gatt;
    private Intent mBTLE_Service_Intent;
    private Service_BTLE_GATT mBTLE_Service;
    private boolean mBTLE_Service_Bound;
    private BroadcastReceiver_BTLE_GATT mGattUpdateReceiver;
    BluetoothDevice device;

    BluetoothGattCallback gattCallback;
    Intent intent;
    ArrayList cardGame = new ArrayList();
    ArrayList duckHunt = new ArrayList();
    ArrayList spacehooter = new ArrayList();
    ArrayList pianoTiles = new ArrayList();
    ArrayList color = new ArrayList();
    ArrayList doubleValues = new ArrayList();
    ArrayList ninjaDart = new ArrayList();
    ArrayList puzzles = new ArrayList();
    ArrayList advancedPuzzle = new ArrayList();
    ArrayList iHavetoFly=new ArrayList();
    ArrayList wordMatch = new ArrayList();
    ArrayList musicArray=new ArrayList();
    ArrayList meditationArray=new ArrayList();
    ArrayList videoArray=new ArrayList();
    Button testActivity;
    FirebaseUser mUser;
    private String name;
    String dateTime;
    ColorPatternGame ma;
    int x;
    private String address;
    File fileName;

    JsonPlaceHolder jsonPlaceHolder;
    private final static String TAG = Activity_BTLE_Services.class.getSimpleName();
    HashMap hashMap = new HashMap();


    Context context;
    private Activity_BTLE_Services activity;


    public Activity_BTLE_Services getActivity() {
        return activity;
    }

    public BroadcastReceiver_BTLE_GATT(Activity_BTLE_Services activity) {
        this.activity = activity;
    }


    public void setActivity(Activity_BTLE_Services activity) {
        this.activity = activity;
    }

    private ServiceConnection mBTLE_ServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ma = new ColorPatternGame();

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Service_BTLE_GATT.BTLeServiceBinder binder = (Service_BTLE_GATT.BTLeServiceBinder) service;
            mBTLE_Service = binder.getService();
            Log.d("BroadCast", String.valueOf(mBTLE_Service));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            dateTime = simpleDateFormat.format(calendar.getTime());
            Log.d("Date", dateTime);
            mBTLE_Service_Bound = true;

            if (!mBTLE_Service.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
            }

            mBTLE_Service.connect(address);

            // Automatically connects to the device upon successful start-up initialization.
//            mBTLeService.connect(mBTLeDeviceAddress);

//            mBluetoothGatt = mBTLeService.getmBluetoothGatt();
//            mGattUpdateReceiver.setBluetoothGatt(mBluetoothGatt);
//            mGattUpdateReceiver.setBTLeService(mBTLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBTLE_Service = null;
            mBTLE_Service_Bound = false;

//            mBluetoothGatt = null;
//            mGattUpdateReceiver.setBluetoothGatt(null);
//            mGattUpdateReceiver.setBTLeService(null);
        }
    };


    public boolean ismConnected() {
        return mConnected;

    }

    public void setmConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read or notification operations.

    @Override
    public void onReceive(Context context, Intent intent) {
        StartUp startUp = new StartUp();
        if (context instanceof ColorPatternGame) {
            ColorPatternGame s = (ColorPatternGame) context;

        }
        Log.d("checking", String.valueOf(ColorPatternGame.isGameStarted));
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("countPost", MODE_PRIVATE);
        int firstStartPost = sharedPreferences1.getInt("firstStartPost", 0);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        services_ArrayList = new ArrayList<>();
        characteristics_HashMap = new HashMap<>();
        characteristics_HashMapList = new HashMap<>();
        expandableListAdapter = new ListAdapter_BTLE_Services(
                context, activity.getServices_ArrayList(), characteristics_HashMapList);
//        expandableListView = (ExpandableListView) findViewById(R.id.lv_expandable);
//        expandableListView.setAdapter(expandableListAdapter);
//        expandableListView.setOnChildClickListener(this);
        Log.d("Get Service", String.valueOf(activity.getServices_ArrayList()));
        for (int i = 0; i < activity.getServices_ArrayList().size(); i++) {
            String uuid = String.valueOf((activity.getServices_ArrayList().get(i).getUuid()));
            Log.d("Services", String.valueOf(activity.getServices_ArrayList()));
            if (uuid.equals("4fafc201-1fb5-459e-8fcc-c5c9c331914b")) {
                Log.d("UUID", String.valueOf(activity.getServices_ArrayList().get(i).getUuid()));
                Log.d("Get ", String.valueOf(activity.getServices_ArrayList().get(i).getCharacteristics().get(1).getUuid()));
                Log.d("Get Blue Value", String.valueOf(activity.getServices_ArrayList().get(i).getCharacteristics().get(1).getValue()));
                byte[] data = activity.getServices_ArrayList().get(i).getCharacteristics().get(1).getValue();

                String text1 = null;   // if the charset is UTF-8
                try {
                    text1 = new String(data, "UTF-8");
                    String text2 = new String(data, "ISO-8859-1");   // if the charset is ISO Latin 1
                    char[] chars = text1.toCharArray();
                    Log.d("CHAR", String.valueOf(chars));
                    for (int k = 0; k < chars.length; k++) {
                        char c = chars[k];
                        double d = (double) (c - '0');
                        doubleValues.add(d);
// do some operation
                    }
//                    doubleValues.add(d);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (data != null) {
                    dataValues.add(Utils.hexToString(data));
                    Log.d("DataSize", String.valueOf(dataValues.size()));
                    if (ColorPatternGame.isGameStarted) {
                        color.add(Utils.hexToString(data));
                        Log.d("ColorPatternData", String.valueOf(color));
                    }
                    if (MainActivityK.Companion.isStarted()) {
                        cardGame.add(Utils.hexToString(data));
                        Log.d("CardGame", String.valueOf(cardGame));
                    }
                    if (SpaceShooter.isStarted) {
                        spacehooter.add(Utils.hexToString(data));
                        Log.d("Spacehooter", String.valueOf(spacehooter));
                    }

                    if (GameView.gameState) {
                        duckHunt.add(Utils.hexToString(data));
                        Log.d("DuckHunt", String.valueOf(duckHunt));
                    }
                    if (MainActivity.isStarted) {
                        pianoTiles.add(Utils.hexToString(data));
                        Log.d("PianoTiles", String.valueOf(pianoTiles));
                    }
                    if (GamePanel.isStarted) {
                        ninjaDart.add(Utils.hexToString(data));
                        Log.d("NinjaDart", String.valueOf(ninjaDart));
                    }
                    if (PuzzleActivity.isStarted) {
                        advancedPuzzle.add(Utils.hexToString(data));
                        Log.d("AdvancedPuzzle", String.valueOf(advancedPuzzle));
                    }
                    if (GameActivity9.isStarted) {
                        puzzles.add(Utils.hexToString(data));
                        Log.d("Puzzles", String.valueOf(puzzles));
                    } else if (GameActivity15.isStarted) {
                        puzzles.add(data);
                        Log.d("Puzzles", String.valueOf(puzzles));
                    } else if (GameActivity24.isStarted) {
                        puzzles.add(Utils.hexToString(data));
                        Log.d("Puzzles", String.valueOf(puzzles));
                    }

                    if (com.example.thinkableproject.IHaveToFly.GameView.isStarted) {
                        iHavetoFly.add(Utils.hexToString(data));
                        Log.d("FLY", String.valueOf(iHavetoFly));
                    }
                    if (TwoByTwoGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                    } else if (TwoByThreeGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                    } else if (TwoByFourGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                    } else if (TwoByFiveGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                    } else if (TwoBySixGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                    }
                    if(MusicPlayer.isStarted){
                        musicArray.add(Utils.hexToString(data));
                        Log.d("MusicData", String.valueOf(musicArray));
                    }
                    if(PlayMeditation.isStarted){
                        meditationArray.add(Utils.hexToString(data));
                        Log.d("MeditationData", String.valueOf(meditationArray));
                    }
                    if (PlayVideo.isStarted){
                        videoArray.add(Utils.hexToString(data));
                        Log.d("VideoData", String.valueOf(videoArray));
                    }

//                    Log.d("Boolean",colorPatternGame.name);
//

                    Log.d("Double Value", String.valueOf(doubleValues));


                    Log.d("Data Size", String.valueOf(dataValues.size()));
                    if (dataValues.size() % 80 == 0) {
                        SharedPreferences sh = context.getSharedPreferences("countPost", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

                        x = sh.getInt("firstStartPost", 0);

// We can then use the data
                        Log.d("A Count", String.valueOf(x));

                        int y = x + 1;

                        SharedPreferences prefsCount1 = context.getSharedPreferences("countPost", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefsCount1.edit();
                        editor.putInt("firstStartPost", y);
                        editor.apply();
                        SharedPreferences sha = context.getSharedPreferences("countPost", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

                        int x1 = sha.getInt("firstStartPost", 0);

                        Log.d("A Count2", String.valueOf(x1));
                        Calendar now = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
                        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
                        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
                        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
                        Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

                        int month = now.get(Calendar.MONTH) + 1;
                        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
                        Format f = new SimpleDateFormat("EEEE");
                        String str = f.format(new Date());
//prints day name
                        System.out.println("Day Name: " + str);
                        Log.d("Day Name", str);
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();


                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(120000, TimeUnit.SECONDS)
                                .readTimeout(120000, TimeUnit.SECONDS).build();

                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.119:5000/").client(client)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
                        Call<List> callRel = jsonPlaceHolder.PostRelaxationData(doubleValues);
                        callRel.enqueue(new Callback<List>() {
                            @Override
                            public void onResponse(Call<List> call, Response<List> response) {
                                Toast.makeText(context, "Post Successful", Toast.LENGTH_SHORT).show();
                                Log.d("Response Code Rel", String.valueOf(response.code()));
                                Log.d("Relaxation Res Message", response.message());
                                Log.d("Relaxation Res Body", String.valueOf(response.body()));
                                mUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Relaxation Post").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
                                reference.setValue(response.body());
                                Log.d("Relaxation Res Type", String.valueOf(response.body().getClass().getSimpleName()));


                            }

                            //
                            @Override
                            public void onFailure(Call<List> call, Throwable t) {
                                Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                Log.d("ErrorVal", String.valueOf(t));


                            }
                        });
                        Call<List> callCon = jsonPlaceHolder.PostConcentrationData(doubleValues);
                        callCon.enqueue(new Callback<List>() {
                            @Override
                            public void onResponse(Call<List> call, Response<List> response) {
                                Toast.makeText(context, "Post Successful", Toast.LENGTH_SHORT).show();
                                Log.d("Concentration Code Rel", String.valueOf(response.code()));
                                Log.d("Concentration Res Mess", response.message());
                                Log.d("Concentration Res Body", String.valueOf(response.body()));
                                mUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
                                reference.setValue(response.body());
                                Log.d("Concentration Res Type", String.valueOf(response.body().getClass().getSimpleName()));


                            }

                            //
                            @Override
                            public void onFailure(Call<List> call, Throwable t) {
                                Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                Log.d("ErrorVal", String.valueOf(t));


                            }
                        });
                    }
                    // Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data
                    SharedPreferences sh = context.getSharedPreferences("ColorPattern", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
                    String s1 = sh.getString("name", "");
                    if (context.toString() == s1) {
                        Log.d("ColorPattern", s1);
                    }


// We can then use the data


                    Log.d("Thread DATA", String.valueOf(dataValues));
//                    Log.d("BROADCOL", String.valueOf(color));

                    try {
                        fileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/values.txt");
                        System.out.println("Path" + Environment.getExternalStorageDirectory().getAbsolutePath());
                        String line = "";
                        FileWriter fwa;
                        fwa = new FileWriter(fileName);
                        BufferedWriter outputa = new BufferedWriter(fwa);
                        int size = dataValues.size();
                        for (int j = 0; j < size; j++) {
                            hashMap.put(dateTime, dataValues);
                            Log.d("HashMap", String.valueOf(hashMap));
//                            Log.d()
                            outputa.write(dataValues.get(j).toString() + "\n");
//                            outputa.write(dataValues.get(j).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Array", String.valueOf(dataValues));
                        outputa.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid());
                    try {
                        StorageReference mountainsRef = storageReference1.child("eeg_values.txt");
                        InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
                        UploadTask uploadTask = mountainsRef.putStream(stream);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(activity, "File Uploaded", Toast.LENGTH_SHORT).show();
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

////
                }
            }
        }
        mBTLE_Service_Intent = new Intent(context, Service_BTLE_GATT.class);
        context.bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
        context.startService(mBTLE_Service_Intent);

        final String action = intent.getAction();


        if (Service_BTLE_GATT.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
//            Utils.toast(activity.getApplicationContext(), "Device Connected");
            myEdit.putString("name", "Device Connected");
            mBTLE_Service_Intent = new Intent(context, Service_BTLE_GATT.class);
            context.bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
            context.startService(mBTLE_Service_Intent);
//            int intent = BluetoothProfile.STATE_CONNECTED;
            Log.d("Device Thread", String.valueOf(intent));

            Log.d("Thread", String.valueOf(services_ArrayList));

            Log.d("TAG", "On Pause");

        } else if (Service_BTLE_GATT.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            Utils.toast(activity.getApplicationContext(), "Disconnected From Device");
            myEdit.putString("name", "Device Disconnected");
            activity.finish();

        } else if (Service_BTLE_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            activity.updateServices();
        } else if (Service_BTLE_GATT.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

            activity.updateCharacteristic();

        }
        myEdit.commit();

        return;
    }
}
