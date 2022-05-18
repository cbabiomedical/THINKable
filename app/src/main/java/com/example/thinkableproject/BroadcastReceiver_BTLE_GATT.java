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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

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
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;
    private ExpandableListView expandableListView;
    public static ArrayList dataValues = new ArrayList();
    private ListAdapter_BTLE_Services expandableListAdapter;
    boolean df = false;
    int a;
    int c;
    BroadcastReceiver_BTLE_GATT broadcastReceiver_btle_gatt;
    private Intent mBTLE_Service_Intent;
    Double sum = 0.0;
    private Service_BTLE_GATT mBTLE_Service;
    private boolean mBTLE_Service_Bound;
    private BroadcastReceiver_BTLE_GATT mGattUpdateReceiver;
    BluetoothDevice device;

    BluetoothGattCallback gattCallback;
    Intent intent;
    ArrayList cardGame = new ArrayList();
    ArrayList cardGameDouble = new ArrayList();
    ArrayList duckHunt = new ArrayList();
    ArrayList duckHuntDouble = new ArrayList();
    ArrayList spacehooter = new ArrayList();
    ArrayList spacehooterDouble = new ArrayList();
    ArrayList pianoTiles = new ArrayList();
    ArrayList pianoTilesDouble = new ArrayList();
    ArrayList color = new ArrayList();
    ArrayList colorDouble = new ArrayList();
    ArrayList doubleValues = new ArrayList();
    ArrayList ninjaDart = new ArrayList();
    ArrayList ninjaDartDouble = new ArrayList();
    ArrayList puzzles = new ArrayList();
    ArrayList puzzlesDouble = new ArrayList();
    ArrayList advancedPuzzle = new ArrayList();
    ArrayList advancedPuzzleDouble = new ArrayList();
    ArrayList iHavetoFly = new ArrayList();
    ArrayList iHavetoFlyDouble = new ArrayList();
    ArrayList wordMatch = new ArrayList();
    ArrayList wordMatchDouble = new ArrayList();
    ArrayList musicArray = new ArrayList();
    ArrayList musicArrayDouble = new ArrayList();
    ArrayList meditationArray = new ArrayList();
    ArrayList meditationArrayDouble = new ArrayList();
    ArrayList videoArray = new ArrayList();
    ArrayList videoArrayDouble = new ArrayList();
    Button testActivity;
    FirebaseUser mUser;
    private String name;
    String dateTime;
    ColorPatternGame ma;
    int x;
    private String address;
    File fileName;
    public static ArrayList concentration_indexes = new ArrayList();
    public static ArrayList concentration_indexesDH = new ArrayList();
    public static ArrayList concentration_indexesND = new ArrayList();
    public static ArrayList concentration_indexesPT = new ArrayList();
    public static ArrayList concentration_indexesIHF = new ArrayList();
    public static ArrayList relaxation_indexesMus = new ArrayList();
    public static ArrayList relaxation_index_Vid = new ArrayList();
    public static ArrayList relaxation_indexMed = new ArrayList();
    public static ArrayList memoryCarGame_index = new ArrayList();
    public static ArrayList memoryColorPattern = new ArrayList();
    public static ArrayList memoryWordMatch = new ArrayList();
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

    public BroadcastReceiver_BTLE_GATT() {
    }

    public ArrayList getConcentration_indexes() {
        return concentration_indexes;
    }

    public void setConcentration_indexes(ArrayList concentration_indexes) {
        this.concentration_indexes = concentration_indexes;
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
        //===============Shared Preference SpaceHooter
        SharedPreferences prefsCountHoot = context.getSharedPreferences("prefsCountHootWH", MODE_PRIVATE);
        int firstStartCountHoot = prefsCountHoot.getInt("firstStartCountHootWH", 0);
        //==========Shared Preference DuckHunt
        SharedPreferences prefsCountDuck = context.getSharedPreferences("prefsCountDuckWH", MODE_PRIVATE);
        int firstStartCountDuck = prefsCountDuck.getInt("firstStartCountDuckWH", 0);

        // Shared Preference for PianoTiles
        SharedPreferences prefsCountPiano = context.getSharedPreferences("prefsCountPianoWH", MODE_PRIVATE);
        int firstStartCountPiano = prefsCountPiano.getInt("firstStartCountPianoWH", 0);

        // Shared Preference for NinjaDart
        SharedPreferences prefsCount1 = context.getSharedPreferences("prefsCountNinjaWH", MODE_PRIVATE);
        int firstStartCount = prefsCount1.getInt("firstStartCountNinjaWH", 0);

        // Shared Preference for Puzzles


        SharedPreferences prefsCountPuz = context.getSharedPreferences("prefsCountPuzWH", MODE_PRIVATE);
        int firstStartCountPuz = prefsCountPuz.getInt("firstStartCountPuzWH", 0);

//        Shared Preference for Fly
        SharedPreferences prefsCountPuzA = activity.getSharedPreferences("prefsCountFlyWH", Context.MODE_PRIVATE);
        int firstStartCountPuzA = prefsCountPuzA.getInt("firstStartFlyWH", 0);

        //Shared Preference for Advanced Puzzle
        SharedPreferences prefsCountP = context.getSharedPreferences("prefsCountPuzA", MODE_PRIVATE);
        int firstStartCountP = prefsCountP.getInt("firstStartCountPuzA", 0);
        // Shared Preference for Word Match
        SharedPreferences prefsCountWord = context.getSharedPreferences("prefsCountWord", MODE_PRIVATE);
        int firstStartCountWord = prefsCountWord.getInt("firstStartCountWord", 0);

        // Shared Preference for Music
        SharedPreferences prefsTimeRel = context.getSharedPreferences("prefsCountMusic", MODE_PRIVATE);
        int firstStartTimeRel = prefsTimeRel.getInt("firstStartMusic", 0);

        // Shared Preference for Meditation
        SharedPreferences prefsCountMed = context.getSharedPreferences("prefsCountMed", MODE_PRIVATE);
        int firstStartCountMed = prefsCountMed.getInt("firstStartCountMed", 0);

        // Shared Preference for Video
        SharedPreferences prefsCountVid = context.getSharedPreferences("prefsCountVid", MODE_PRIVATE);
        int firstStartCountVid = prefsCountMed.getInt("firstStartCountVid", 0);


// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        services_ArrayList = new ArrayList<>();
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
                        cardGameDouble.add(d);
                        colorDouble.add(d);
                        spacehooterDouble.add(d);
                        duckHuntDouble.add(d);
                        pianoTilesDouble.add(d);
                        ninjaDartDouble.add(d);
                        puzzlesDouble.add(d);
                        advancedPuzzleDouble.add(d);
                        iHavetoFlyDouble.add(d);
                        wordMatchDouble.add(d);
                        musicArrayDouble.add(d);
                        meditationArrayDouble.add(d);
                        videoArrayDouble.add(d);

// do some operation
                    }
//                    doubleValues.add(d);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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

                if (data != null) {
                    dataValues.add(Utils.hexToString(data));
                    Log.d("DataSize", String.valueOf(dataValues.size()));

                    // Posting Color Pattern Data
                    if (ColorPatternGame.isGameStarted) {
                        color.add(Utils.hexToString(data));
                        Log.d("ColorPatternData", String.valueOf(color));

                        if (colorDouble.size() % 30 == 0) {
                            //==========Post COLOR PATTERN DATA FOR MEMORY=================
                            Call<List> callColMem = jsonPlaceHolder.PostColorPatternMemData(colorDouble);
                            callColMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code ColMem", String.valueOf(response.code()));
                                    Log.d("ColMem Res Message", response.message());
                                    Log.d("ColMem Res Body", String.valueOf(response.body()));
                                    Log.d("ColMem Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                    List colorpattindex = new ArrayList();
                                    colorpattindex = response.body();
                                    Log.d("ColorPatternList", String.valueOf(colorpattindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) colorpattindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryColorPattern.add(hashMap.get("index"));
                                    Log.d("MEMORYINDEX", String.valueOf(memoryColorPattern));
                                    Log.d("ShCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                //
                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));


                                }
                            });

                        }
                    }
                    // Posting Card Game Data
                    if (MainActivityK.Companion.isStarted()) {
                        cardGame.add(Utils.hexToString(data));
                        Log.d("CardGame", String.valueOf(cardGame));
                        if (cardGameDouble.size() % 40 == 0) {

                            //==========Post CARD GAME DATA FOR MEMORY=================
                            Call<List> callCrdMem = jsonPlaceHolder.PostCardGamData(cardGameDouble);
                            callCrdMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code CrdMem", String.valueOf(response.code()));
                                    Log.d("CrdMem Res Message", response.message());
                                    Log.d("CrdMem Res Body", String.valueOf(response.body()));
                                    Log.d("CrdMem Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                    List cardgameindex = new ArrayList();
                                    cardgameindex = response.body();
                                    Log.d("CardGameList", String.valueOf(cardgameindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) cardgameindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryCarGame_index.add(hashMap.get("index"));
                                    Log.d("CONCENTRATIONINDEX", String.valueOf(memoryCarGame_index));
                                    Log.d("ShCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));


                                }

                                //
                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));


                                }
                            });

                        }
                    }
                    // Posting Spacehooter Data
                    if (SpaceShooter.isStarted) {
                        spacehooter.add(Utils.hexToString(data));
                        Log.d("Spacehooter", String.valueOf(spacehooter));

                        if (spacehooterDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountHootWH", MODE_APPEND);
                            Log.d("VALINT", String.valueOf(SpaceShooter.a));


                            a = sh.getInt("firstStartCountHootWH", 0);
                            int b = a + 1;
                            SharedPreferences prefsCountHootWH = context.getSharedPreferences("prefsCountHootWH", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountHootWH.edit();
                            editor.putInt("firstStartCountHootWH", b);
                            editor.apply();
                            SharedPreferences sha = context.getSharedPreferences("prefsCountHootWH", MODE_APPEND);

                            int a1 = sha.getInt("firstStartCountWH", 0);

                            Log.d("A Count2", String.valueOf(a1));
                            //======Post SPACEHOOTER DATA FOR CONCENTRATION============
                            Call<List> callShCon = jsonPlaceHolder.PostSpaceHooterData(spacehooterDouble);
                            callShCon.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Space Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code ShCon", String.valueOf(response.code()));
                                    Log.d("ShCon Res Message", response.message());
                                    Log.d("ShCon Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("Spacehooter").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());

//                                    Log.d("ResponceType", response.body().getClass().getSimpleName());
                                    List spacehooterindex = new ArrayList();
                                    spacehooterindex = response.body();
                                    Log.d("SpaceShooterList", String.valueOf(spacehooterindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) spacehooterindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    concentration_indexes.add(hashMap.get("index"));
                                    Log.d("CONCENTRATIONINDEX", String.valueOf(concentration_indexes));
                                    Log.d("ShCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
                    }
                    Log.d("SpaceshooterState", String.valueOf(SpaceShooter.over));


//                    if(SpaceShooter.isStarted=false){
//                        Call<List> callDHCon = jsonPlaceHolder.PostTimeToConcentrate(s);
//                        callDHCon.enqueue(new Callback<List>() {
//                            @Override
//                            public void onResponse(Call<List> call, Response<List> response) {
////                                    Toast.makeText(context, "Post Duck Successful", Toast.LENGTH_SHORT).show();
//                                Log.d("Response Code DHCon", String.valueOf(response.code()));
//                                Log.d("DHCon Res Message", response.message());
//                                Log.d("DHCon Res Body", String.valueOf(response.body()));
//                                mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("DuckHunt").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                reference.setValue(response.body());
//                                Log.d("DHCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//
//                            }

//                            @Override
//                            public void onFailure(Call<List> call, Throwable t) {
////                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
//                                Log.d("ErrorVal", String.valueOf(t));
//                            }
//                        });
//                    }

                    if (GameView.gameState) {
                        duckHunt.add(Utils.hexToString(data));
                        Log.d("DuckHunt", String.valueOf(duckHunt));
                        if (duckHuntDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountDuckWH", MODE_APPEND);

                            a = sh.getInt("firstStartCountDuckWH", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;
                            SharedPreferences prefsCountDuckWH = context.getSharedPreferences("prefsCountDuckWH", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountDuckWH.edit();
                            editor.putInt("firstStartCountDuckWH", b);
                            editor.apply();

                            // ===========Post DUCKHUNT DATA FOR CONCENTRATION=====================
                            Call<List> callDHCon = jsonPlaceHolder.PostDuckHuntData(duckHuntDouble);
                            callDHCon.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Duck Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code DHCon", String.valueOf(response.code()));
                                    Log.d("DHCon Res Message", response.message());
                                    Log.d("DHCon Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    List duckhuntindex = new ArrayList();
                                    duckhuntindex = response.body();
                                    Log.d("DuckHuntList", String.valueOf(duckhuntindex.get(0)));
                                    LinkedTreeMap hashMapD = new LinkedTreeMap();
                                    hashMapD = (LinkedTreeMap) duckhuntindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMapD.get("index")));
//                                    referenceIntervention.setValue(hashMap.get("index"));
                                    concentration_indexesDH.add(hashMapD.get("index"));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("DuckHunt").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());
//                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
//                                    reference1.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
//                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child(String.valueOf(snapshot.getValue())).child("Concentration Index").child(String.valueOf(a));
//                                            reference2.setValue(response.body());
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                    Log.d("DHCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }

                    }
                    if (MainActivity.isStarted) {
                        pianoTiles.add(Utils.hexToString(data));
                        Log.d("PianoTiles", String.valueOf(pianoTiles));
                        if (pianoTilesDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountPianoWH", MODE_APPEND);

                            a = sh.getInt("firstStartCountPianoWH", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;
                            SharedPreferences prefsCountPianoWH = context.getSharedPreferences("prefsCountPianoWH", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountPianoWH.edit();
                            editor.putInt("firstStartCountPianoWH", b);
                            editor.apply();
                            SharedPreferences sha = context.getSharedPreferences("prefsCountPianoWH", MODE_APPEND);
                            int c = sha.getInt("firstStartCountPianoWH", 0);
                            Log.d("A Count2", String.valueOf(c));

                            // Post PIANOTILES DATA FOR CONCENTRATION
                            Call<List> callPTCon = jsonPlaceHolder.PostPianoTilesData(pianoTilesDouble);
                            callPTCon.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Piano Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code PTCon", String.valueOf(response.code()));
                                    Log.d("PTCon Res Message", response.message());
                                    Log.d("PTCon Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    List pianoTilesindex = new ArrayList();
                                    pianoTilesindex = response.body();
                                    Log.d("SpaceShooterList", String.valueOf(pianoTilesindex.get(0)));
                                    LinkedTreeMap hashMapPT = new LinkedTreeMap();
                                    hashMapPT = (LinkedTreeMap) pianoTilesindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMapPT.get("index")));
//                                    referenceIntervention.setValue(hashMap.get("index"));
                                    concentration_indexesPT.add(hashMapPT.get("index"));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("PianoTiles").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());
//                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
//                                    reference1.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
//                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child(String.valueOf(snapshot.getValue())).child("Concentration Index").child(String.valueOf(a));
//                                            reference2.setValue(response.body());
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });

                        }
                    }
                    if (GamePanel.isStarted) {
                        ninjaDart.add(Utils.hexToString(data));
                        Log.d("NinjaDart", String.valueOf(ninjaDart));
                        if (ninjaDartDouble.size() % 40 == 0) {
                            SharedPreferences shh = context.getSharedPreferences("prefsCountNinjaWH", MODE_APPEND);

                            a = shh.getInt("firstStartCountNinjaWH", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;
                            SharedPreferences prefsCount2 = context.getSharedPreferences("prefsCountNinjaWH", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCount2.edit();
                            editor.putInt("firstStartCountNinjaWH", b);
                            editor.apply();
                            SharedPreferences sha1 = context.getSharedPreferences("prefsCountNinjaWH", MODE_APPEND);

                            int a1 = sha1.getInt("firstStartCountNinjaWH", 0);

                            //======== Post NINJADART DATA FOR CONCENTRATION=============
                            Call<List> callNDCon = jsonPlaceHolder.PostNinjaDartData(ninjaDartDouble);
                            callNDCon.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Ninja Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code NDCon", String.valueOf(response.code()));
                                    Log.d("NDCon Res Message", response.message());
                                    Log.d("NDCon Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    List ninjadartindex = new ArrayList();
                                    ninjadartindex = response.body();
                                    Log.d("NinjaDartList", String.valueOf(ninjadartindex.get(0)));
                                    LinkedTreeMap hashMapND = new LinkedTreeMap();
                                    hashMapND = (LinkedTreeMap) ninjadartindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMapND.get("index")));
//                                    referenceIntervention.setValue(hashMap.get("index"));
                                    concentration_indexesND.add(hashMapND.get("index"));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("NinjaDart").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());
//
                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });


                        }
                    }
                    if (PuzzleActivity.isStarted) {
                        advancedPuzzle.add(Utils.hexToString(data));
                        Log.d("AdvancedPuzzle", String.valueOf(advancedPuzzle));
                        if (advancedPuzzleDouble.size() % 80 == 0) {
//                            SharedPreferences sh = context.getSharedPreferences("prefsCountPuzA", MODE_APPEND);
//                            a = sh.getInt("firstStartCountPuzA", 0);
//                            Log.d("A Count", String.valueOf(a));
//                            int b = a + 1;
//                            SharedPreferences prefsCountAP = context.getSharedPreferences("prefsCountPuzA", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefsCountAP.edit();
//                            editor.putInt("firstStartCountPuzA", b);
//                            editor.apply();
//                            SharedPreferences sha = context.getSharedPreferences("prefsCountPuzA", MODE_APPEND);
//                            int a1 = sha.getInt("firstStartCountPuzA", 0);
//
//                            // Post ADVANCED PUZZLE FOR MEMORY
//                            Call<List> callAPCon = jsonPlaceHolder.PostFlyData(iHavetoFlyDouble);
//                            callAPCon.enqueue(new Callback<List>() {
//                                @Override
//                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Adv Successful", Toast.LENGTH_SHORT).show();
//                                    Log.d("Response Code APMem", String.valueOf(response.code()));
//                                    Log.d("APMem Res Message", response.message());
//                                    Log.d("APMem Res Body", String.valueOf(response.body()));
//                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("I Have to Fly").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
////                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
//                                    Log.d("ErrorVal", String.valueOf(t));
//                                }
//                            });

                        }
                    }
                    if (GameActivity9.isStarted) {
                        puzzles.add(Utils.hexToString(data));
                        Log.d("Puzzles", String.valueOf(puzzles));
                        if (puzzlesDouble.size() % 80 == 0) {
//                            SharedPreferences sh = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                            a = sh.getInt("firstStartCountPuzWH", 0);
//                            Log.d("Count 1", String.valueOf(a));
//                            int b = a + 1;
//                            Log.d("B Val", String.valueOf(b));
//                            SharedPreferences prefsCountGM9 = context.getSharedPreferences("prefsCountPuzWH", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefsCountGM9.edit();
//                            editor.putInt("firstStartCountPuzWH", b);
//                            editor.apply();
//                            SharedPreferences sha = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                            int a1 = sha.getInt("firstStartCountPuzWH", 0);
//                            // Post PUZZLE DATA FOR MEMORY
//                            Call<List> callPuzMem = jsonPlaceHolder.PostPuzzlesData(puzzlesDouble);
//                            callPuzMem.enqueue(new Callback<List>() {
//                                @Override
//                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Puzzle Successful", Toast.LENGTH_SHORT).show();
//                                    Log.d("Res Code callPuzMem", String.valueOf(response.code()));
//                                    Log.d("callPuzMem Res Message", response.message());
//                                    Log.d("callPuzMem Res Body", String.valueOf(response.body()));
//                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Puzzles").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
////                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
//                                    Log.d("ErrorVal", String.valueOf(t));
//                                }
//                            });
//                        }
                        }
                    } else if (GameActivity15.isStarted) {
                        puzzles.add(data);
                        Log.d("Puzzles", String.valueOf(puzzles));
                        if (puzzlesDouble.size() % 80 == 0) {
//                            SharedPreferences sh = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                            a = sh.getInt("firstStartCountPuzWH", 0);
//                            Log.d("Count 1", String.valueOf(a));
//                            int b = a + 1;
//                            Log.d("B Val", String.valueOf(b));
//                            SharedPreferences prefsCountGM9 = context.getSharedPreferences("prefsCountPuzWH", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefsCountGM9.edit();
//                            editor.putInt("firstStartCountPuzWH", b);
//                            editor.apply();
//                            SharedPreferences sha = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                            int a1 = sha.getInt("firstStartCountPuzWH", 0);
//                            // Post PUZZLE DATA FOR MEMORY
//                            Call<List> callPuzMem = jsonPlaceHolder.PostPuzzlesData(puzzlesDouble);
//                            callPuzMem.enqueue(new Callback<List>() {
//                                @Override
//                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Puzzle Successful", Toast.LENGTH_SHORT).show();
//                                    Log.d("Res Code callPuzMem", String.valueOf(response.code()));
//                                    Log.d("callPuzMem Res Message", response.message());
//                                    Log.d("callPuzMem Res Body", String.valueOf(response.body()));
//                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Puzzles").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
////                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
//                                    Log.d("ErrorVal", String.valueOf(t));
//                                }
//                            });
//                        }
                        }

                    } else if (GameActivity24.isStarted) {
                        puzzles.add(Utils.hexToString(data));
                        Log.d("Puzzles", String.valueOf(puzzles));
//                                if (puzzlesDouble.size() % 80 == 0) {
//                                    SharedPreferences sh = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                                    a = sh.getInt("firstStartCountPuzWH", 0);
//                                    Log.d("Count 1", String.valueOf(a));
//                                    int b = a + 1;
//                                    Log.d("B Val", String.valueOf(b));
//                                    SharedPreferences prefsCountGM9 = context.getSharedPreferences("prefsCountPuzWH", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefsCountGM9.edit();
//                                    editor.putInt("firstStartCountPuzWH", b);
//                                    editor.apply();
//                                    SharedPreferences sha = context.getSharedPreferences("prefsCountPuzWH", MODE_APPEND);
//                                    int a1 = sha.getInt("firstStartCountPuzWH", 0);
//                                    // Post PUZZLE DATA FOR MEMORY
//                                    Call<List> callPuzMem = jsonPlaceHolder.PostPuzzlesData(puzzlesDouble);
//                                    callPuzMem.enqueue(new Callback<List>() {
//                                        @Override
//                                        public void onResponse(Call<List> call, Response<List> response) {
//                                            Toast.makeText(context, "Post Puzzle Successful", Toast.LENGTH_SHORT).show();
//                                            Log.d("Res Code callPuzMem", String.valueOf(response.code()));
//                                            Log.d("callPuzMem Res Message", response.message());
//                                            Log.d("callPuzMem Res Body", String.valueOf(response.body()));
//                                            mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Puzzles").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                            reference.setValue(response.body());
////                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<List> call, Throwable t) {
//                                            Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
//                                            Log.d("ErrorVal", String.valueOf(t));
//                                        }
//                                    });
//
//
//                                }
                    }

                    if (com.example.thinkableproject.IHaveToFly.GameView.isStarted) {
                        iHavetoFly.add(Utils.hexToString(data));
                        Log.d("FLY", String.valueOf(iHavetoFly));
                        if (iHavetoFlyDouble.size() % 40 == 0) {
                            SharedPreferences sh = activity.getSharedPreferences("prefsCountFlyWH", MODE_APPEND);
                            a = sh.getInt("firstStartCountFlyWH", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;

                            SharedPreferences prefsCount2 = context.getSharedPreferences("prefsCountFlyWH", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = prefsCount2.edit();
                            editor1.putInt("firstStartCountFlyWH", b);
                            editor1.apply();
                            SharedPreferences sha2 = context.getSharedPreferences("prefsCountFlyWH", MODE_APPEND);
                            int a1 = sha2.getInt("firstStartCountPuzWH", 0);
                            Log.d("A Count2", String.valueOf(a1));

                            // Post IHAVETOFLYDATA TO CONCENTRATION
                            Call<List> callIFCon = jsonPlaceHolder.PostFlyData(iHavetoFlyDouble);
                            callIFCon.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
//                                    Toast.makeText(context, "Post Fly Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code IFCon", String.valueOf(response.code()));
                                    Log.d("IFCon Res Message", response.message());
                                    Log.d("IFCon Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    List ihavetoflyindex = new ArrayList();
                                    ihavetoflyindex = response.body();
                                    Log.d("IHavetoFlyList", String.valueOf(ihavetoflyindex.get(0)));
                                    LinkedTreeMap hashMapIHF = new LinkedTreeMap();
                                    hashMapIHF = (LinkedTreeMap) ihavetoflyindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMapIHF.get("index")));
//                                    referenceIntervention.setValue(hashMap.get("index"));
                                    concentration_indexesIHF.add(hashMapIHF.get("index"));
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child("I Have to Fly").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());
//                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
//                                    reference1.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
//                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child(String.valueOf(snapshot.getValue())).child(String.valueOf(a));
//                                            reference2.setValue(response.body());
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });


                        }
                    }

                    // Word Matching Data
                    if (TwoByTwoGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                        if (wordMatchDouble.size() % 20 == 0) {
//                            // Post WORDMATHC DATA FOR MEMORY
//
                            Call<List> callWMMem = jsonPlaceHolder.PostWordMatchData(wordMatchDouble);
                            callWMMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Word Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Res Code cWDMem", String.valueOf(response.code()));
                                    Log.d("cWDMem Res Message", response.message());
                                    Log.d("cWDMem2 Res Body", String.valueOf(response.body()));

                                    List twobytwoindex = new ArrayList();
                                    twobytwoindex = response.body();
                                    Log.d("MeditationList", String.valueOf(twobytwoindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) twobytwoindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryWordMatch.add(hashMap.get("index"));
                                    Log.d("MEMORYINDEX", String.valueOf(memoryWordMatch));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();

//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                //
                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
                    } else if (TwoByThreeGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                        if (wordMatchDouble.size() % 20 == 0) {

                            // Post WORDMATHC DATA FOR MEMORY

                            Call<List> callWMMem = jsonPlaceHolder.PostWordMatchData(wordMatchDouble);
                            callWMMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Word Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Res Code cWDMem", String.valueOf(response.code()));
                                    Log.d("cWDMem Res Message", response.message());
                                    Log.d("cWDMem3 Res Body", String.valueOf(response.body()));

                                    List twobythreeindex = new ArrayList();
                                    twobythreeindex = response.body();
                                    Log.d("MeditationList", String.valueOf(twobythreeindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) twobythreeindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryWordMatch.add(hashMap.get("index"));
                                    Log.d("MEMORYINDEX", String.valueOf(memoryWordMatch));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Word Match").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));


                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
                    } else if (TwoByFourGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                        if (wordMatchDouble.size() % 20 == 0) {

                            // Post WORDMATHC DATA FOR MEMORY

                            Call<List> callWMMem = jsonPlaceHolder.PostWordMatchData(wordMatchDouble);
                            callWMMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Word Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Res Code cWDMem", String.valueOf(response.code()));
                                    Log.d("cWDMem Res Message", response.message());
                                    Log.d("cWDMem4 Res Body", String.valueOf(response.body()));

                                    List twobyfourindex = new ArrayList();
                                    twobyfourindex = response.body();
                                    Log.d("MeditationList", String.valueOf(twobyfourindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) twobyfourindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryWordMatch.add(hashMap.get("index"));
                                    Log.d("MEMORYINDEX", String.valueOf(memoryWordMatch));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Word Match").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
                    } else if (TwoByFiveGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                        if (wordMatchDouble.size() % 20 == 0) {

//                            // Post WORDMATHC DATA FOR MEMORY
//
                            Call<List> callWMMem = jsonPlaceHolder.PostWordMatchData(wordMatchDouble);
                            callWMMem.enqueue(new Callback<List>() {
                                        @Override
                                        public void onResponse(Call<List> call, Response<List> response) {
                                            Toast.makeText(context, "Post Word Successful", Toast.LENGTH_SHORT).show();
                                            Log.d("Res Code cWDMem", String.valueOf(response.code()));
                                            Log.d("cWDMem Res Message", response.message());
                                            Log.d("cWDMem5 Res Body", String.valueOf(response.body()));
                                            mUser = FirebaseAuth.getInstance().getCurrentUser();

                                            List twobyfiveindex = new ArrayList();
                                            twobyfiveindex = response.body();
                                            Log.d("MeditationList", String.valueOf(twobyfiveindex.get(0)));
                                            LinkedTreeMap hashMap = new LinkedTreeMap();
                                            hashMap = (LinkedTreeMap) twobyfiveindex.get(0);
                                            Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                            memoryWordMatch.add(hashMap.get("index"));
                                            Log.d("MEMORYINDEX", String.valueOf(memoryWordMatch));
//                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Word Match").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                            reference.setValue(response.body());
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                        }

                                        @Override
                                        public void onFailure(Call<List> call, Throwable t) {
                                            Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                            Log.d("ErrorVal", String.valueOf(t));
                                        }
                                    });
                        }
                    } else if (TwoBySixGrid.isStarted) {
                        wordMatch.add(Utils.hexToString(data));
                        Log.d("WordMatch", String.valueOf(wordMatch));
                        if (wordMatchDouble.size() % 20 == 0) {

//                            // Post WORDMATHC DATA FOR MEMORY
//
                            Call<List> callWMMem = jsonPlaceHolder.PostWordMatchData(wordMatchDouble);
                            callWMMem.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Word Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Res Code cWDMem", String.valueOf(response.code()));
                                    Log.d("cWDMem Res Message", response.message());
                                    Log.d("cWDMem6 Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();

                                    List twobysixindex = new ArrayList();
                                    twobysixindex = response.body();
                                    Log.d("MeditationList", String.valueOf(twobysixindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) twobysixindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    memoryWordMatch.add(hashMap.get("index"));
                                    Log.d("MEMORYINDEX", String.valueOf(memoryWordMatch));

//                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Memory Post").child("Word Match").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//                                    reference.setValue(response.body());
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
                    }
                    if (MusicPlayer.isStarted) {
                        musicArray.add(Utils.hexToString(data));
                        Log.d("MusicData", String.valueOf(musicArray));
                        if (musicArrayDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountMusic", MODE_APPEND);
                            a = sh.getInt("firstStartCountMusic", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;

                            SharedPreferences prefsCountWord1 = context.getSharedPreferences("prefsCountMusic", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountWord1.edit();
                            editor.putInt("firstStartCountMusic", b);
                            editor.apply();
                            SharedPreferences sha = context.getSharedPreferences("prefsCountMusic", MODE_APPEND);
                            // Post MUSIC DATA FOR RELAXATION
                            Call<List> callMusRel = jsonPlaceHolder.PostMusicData(musicArrayDouble);
                            callMusRel.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Music Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code MusRel", String.valueOf(response.code()));
                                    Log.d("MusRel Res Message", response.message());
                                    Log.d("MusRel Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Relaxation Post").child("Music").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());

                                    Log.d("ResponceType", response.body().getClass().getSimpleName());
                                    List musicindex = new ArrayList();
                                    musicindex = response.body();
                                    Log.d("MusicList", String.valueOf(musicindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) musicindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    relaxation_indexesMus.add(hashMap.get("index"));
                                    Log.d("RELAXATIONINDEX", String.valueOf(relaxation_indexesMus));
//                                    Log.d("ShCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }

                    }
                    if (PlayMeditation.isStarted) {
                        meditationArray.add(Utils.hexToString(data));
                        Log.d("MeditationData", String.valueOf(meditationArray));
                        if (meditationArrayDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountMed", MODE_APPEND);
                            a = sh.getInt("firstStartCountMed", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;

                            SharedPreferences prefsCountWord1 = context.getSharedPreferences("prefsCountMed", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountWord1.edit();
                            editor.putInt("firstStartCountMed", b);
                            editor.apply();
                            SharedPreferences sha = context.getSharedPreferences("prefsCountMed", MODE_APPEND);
                            // Post MEDITATION DATA FOR RELAXATION
                            Call<List> callMedRel = jsonPlaceHolder.PostMeditationData(meditationArrayDouble);
                            callMedRel.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Medi Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code MedRel", String.valueOf(response.code()));
                                    Log.d("MedRel Res Message", response.message());
                                    Log.d("MedRel Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Relaxation Post").child("Meditation").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());
                                    Log.d("ResponceType", response.body().getClass().getSimpleName());

                                    List meditationindex = new ArrayList();
                                    meditationindex = response.body();
                                    Log.d("MeditationList", String.valueOf(meditationindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) meditationindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    relaxation_indexMed.add(hashMap.get("index"));
                                    Log.d("RELAXATIONINDEX", String.valueOf(relaxation_indexMed));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }

                    }
                    if (PlayVideo.isStarted) {
                        videoArray.add(Utils.hexToString(data));
                        Log.d("VideoData", String.valueOf(videoArray));
                        if (videoArrayDouble.size() % 40 == 0) {
                            SharedPreferences sh = context.getSharedPreferences("prefsCountVid", MODE_APPEND);
                            a = sh.getInt("firstStartCountVid", 0);
                            Log.d("A Count", String.valueOf(a));
                            int b = a + 1;

                            SharedPreferences prefsCountWord1 = context.getSharedPreferences("prefsCountVid", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefsCountWord1.edit();
                            editor.putInt("firstStartCountVid", b);
                            editor.apply();
                            SharedPreferences sha = context.getSharedPreferences("prefsCountVid", MODE_APPEND);

                            // Post VIDEO DATA FOR RELAXATION
                            Call<List> callVidRel = jsonPlaceHolder.PostVideoData(videoArrayDouble);
                            callVidRel.enqueue(new Callback<List>() {
                                @Override
                                public void onResponse(Call<List> call, Response<List> response) {
                                    Toast.makeText(context, "Post Video Successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Response Code VidRel", String.valueOf(response.code()));
                                    Log.d("VidRel Res Message", response.message());
                                    Log.d("VidRel Res Body", String.valueOf(response.body()));
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Relaxation Post").child("Video").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                                    reference.setValue(response.body());

                                    Log.d("ResponceType", response.body().getClass().getSimpleName());
                                    List videoindex = new ArrayList();
                                    videoindex = response.body();
                                    Log.d("VideoList", String.valueOf(videoindex.get(0)));
                                    LinkedTreeMap hashMap = new LinkedTreeMap();
                                    hashMap = (LinkedTreeMap) videoindex.get(0);
                                    Log.d("HashMapTree", String.valueOf(hashMap.get("index")));
                                    relaxation_index_Vid.add(hashMap.get("index"));
                                    Log.d("RELAXATIONINDEX", String.valueOf(relaxation_index_Vid));
//                                    Log.d("ShCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));
//                                    Log.d("PTCon Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                                }

                                @Override
                                public void onFailure(Call<List> call, Throwable t) {
                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                    Log.d("ErrorVal", String.valueOf(t));
                                }
                            });
                        }
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

                        SharedPreferences prefsCountDouble = context.getSharedPreferences("countPost", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefsCountDouble.edit();
                        editor.putInt("firstStartPost", y);
                        editor.apply();
                        SharedPreferences sha = context.getSharedPreferences("countPost", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

                        int x1 = sha.getInt("firstStartPost", 0);

                        Log.d("A Count2", String.valueOf(x1));

                        //==========Post Raw EEG VALUES RELAXATION=================
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
//                                Log.d("Relaxation Res Type", String.valueOf(response.body().getClass().getSimpleName()));

                            }

                            //
                            @Override
                            public void onFailure(Call<List> call, Throwable t) {
                                Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                                Log.d("ErrorVal", String.valueOf(t));


                            }
                        });

                        //=========== Posting EEG VALUES for CONCENTRATION==========
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
//                                Log.d("Concentration Res Type", String.valueOf(response.body().getClass().getSimpleName()));

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

        mBTLE_Service_Intent = new

                Intent(context, Service_BTLE_GATT.class);
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
