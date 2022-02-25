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

import com.example.thinkableproject.sample.JsonPlaceHolder;
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
    ArrayList dataValues = new ArrayList();
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
    ArrayList spacehooter;
    ArrayList doubleValues = new ArrayList();
    Button testActivity;
    FirebaseUser mUser;
    private String name;
    String dateTime;
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
        StartUp startUp=new StartUp();
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
//
                    dataValues.add(Utils.hexToString(data));
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

                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.101:5000/").client(client)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
                        ArrayList<Float> listRel = new ArrayList(Arrays.asList(225646.0, 234306.0, 234849.0, 230242.0, 225590.0, 229498.0, 234651.0, 234635.0, 229451.0, 225371.0, 228420.0, 229014.0, 225213.0, 234150.0, 230061.0, 225406.0, 234475.0, 225580.0, 231192.0, 234641.0, 225105.0, 228938.0, 224959.0, 228676.0, 224620.0, 228491.0, 233822.0, 233613.0, 227686.0, 227194.0, -4194304.0, 233232.0, 226263.0, 232887.0, 226550.0, 230482.0, 234149.0, 227693.0, 225040.0, 230572.0, 227484.0, 224816.0, 230051.0, 226919.0, 224542.0, 229744.0, 233711.0, 232410.0, 225919.0, 230208.0, 233829.0, 226116.0, 224424.0, 229931.0, 233753.0, 232404.0, 225949.0, 224528.0, 230014.0, 226262.0, 224108.0, 228726.0, 233508.0, 231988.0, 225461.0, 874.0, 228101.0, 233110.0, 225554.0, 224065.0, 229544.0, 233394.0, 231913.0, 225439.0, 224086.0, 229422.0, 233420.0, 231781.0, 225293.0, 224093.0, 229408.0, 233373.0, 231883.0, 225373.0, 223964.0, 229249.0, 231584.0, 225254.0, 225003.0, 231445.0, 225191.0, 224498.0, 230549.0, 224184.0, 229573.0, 233584.0, 232121.0, 224133.0, 229471.0, 224813.0, 231207.0, 224752.0, 231167.0, 224128.0, 229935.0, 224566.0, 224451.0, 230750.0, 233158.0, 230524.0, 224277.0, 223651.0, 229138.0, 232937.0, 230191.0, 224096.0, 224431.0, 224484.0, 224235.0, 230449.0, 230420.0, 224178.0, 224050.0, 230074.0, 223621.0, 223726.0, 229816.0, 232873.0, 230341.0, 224166.0, 224975.0, 231443.0, 224213.0, 224548.0, 231023.0, 224275.0, 224363.0, 230721.0, 224051.0, 224614.0, 231095.0, 224831.0, 231337.0, 223921.0, 225433.0, 231834.0, 223796.0, 224138.0, 230308.0, 232675.0, 228845.0, 223321.0, 223758.0, 230059.0, 232575.0, 228758.0, 223306.0, 223781.0, 230099.0, 232609.0, 228814.0, 223364.0, 225133.0, 231541.0, 232688.0, 228864.0, 223466.0, 224534.0, 231022.0, 223617.0, 225388.0, 223733.0, 225544.0, 233054.0, 225403.0, 235736.0, 185823.0, 104903.0, 222643.0, 235269.0, 225654.0, 184728.0, 103385.0, 222339.0, 224815.0, 104610.0, 222634.0, 234173.0, 224685.0, 183009.0, 104787.0, 179141.0, 222803.0, 233487.0, 224128.0, 178793.0, 101741.0, 222598.0, 233236.0, 223753.0, 178873.0, 102742.0, 222831.0, 98300.0, 221898.0, 223614.0, 175255.0, 98969.0, 222389.0, 232691.0, 223363.0, 175547.0, 98568.0, 221978.0, 232415.0, 245760.0, 222443.0, 232250.0, 223007.0, 172189.0, 98438.0, 222536.0, 231898.0, 222601.0, 171519.0, 98403.0, 222578.0, 231931.0, 222938.0, 904.0, 222541.0, 169613.0, 97898.0, 222579.0, 230918.0, 221703.0, 168716.0, 97943.0, 222321.0, 230392.0, 220876.0, 167960.0, 222943.0, 222241.0, 165636.0, 96649.0, 222819.0, 230577.0, 221433.0, 222977.0, 230247.0, 221019.0, 165089.0, 98278.0, 222957.0, 220781.0, 160919.0, 96746.0, 223496.0, 230108.0, 221231.0, 162389.0, 97916.0, 223689.0, 230131.0, 221403.0, 162885.0, 98624.0, 224192.0, 159261.0, 221352.0, 158168.0, 98790.0, 225176.0, 229950.0, 221728.0, 158160.0, 100411.0, 225878.0, 221766.0, 157616.0, 100924.0, 226107.0, 229829.0, 221523.0, 100494.0, 226352.0, 230618.0, 222954.0, 156119.0, 101961.0, 226925.0, 230651.0, 222725.0, 155440.0, 102846.0, 227448.0, 230648.0, 222814.0, 222991.0, 101646.0, 227913.0, 231253.0, 223811.0, 153201.0, 103135.0, 228528.0, 231343.0, 223653.0, 152273.0, 103209.0, 228616.0, 231243.0, 152525.0, 227984.0, 229890.0, 222229.0, 149834.0, 102885.0, 227002.0, 229076.0, 221894.0, 149686.0, 103488.0, 227250.0, 228804.0, 221266.0, 180224.0, 230873.0, 223736.0, 147211.0, 104911.0, 229701.0, 230612.0, 223185.0, 147644.0, 105599.0, 229721.0, 230272.0, 222744.0, 146869.0, 106201.0, 231097.0, 223647.0, 105553.0, 230434.0, 230974.0, 223174.0, 143461.0, 105112.0, 230545.0, 230626.0, 222421.0, 141093.0, 230046.0, 222061.0, 139394.0, 103836.0, 230571.0, 222626.0, 138659.0, 230959.0, 230129.0, 222017.0, 231847.0, 222581.0, 136126.0, 906.0, 230833.0, 222442.0, 135426.0, 232274.0, 230878.0, 223106.0, 107504.0, 231579.0, 108983.0, 233248.0, 231521.0, 223706.0, 133324.0, 109546.0, 233411.0, 231379.0, 223640.0, 134396.0, 111227.0, 233693.0, 230942.0, 112123.0, 231686.0, 129026.0, 112833.0, 234559.0, 231264.0, 129261.0, 112795.0, 234406.0, 231212.0, 235689.0, 232409.0, 224278.0, 126874.0, 115489.0, 236026.0, 124878.0, 116034.0, 235595.0, 231669.0, 223709.0, 124429.0, 117014.0, 235679.0, 231658.0, 223734.0, 123778.0, 116907.0, 235792.0, 231391.0, 223016.0, 122648.0, 117433.0, 235712.0, 119233.0, 117564.0, 235575.0, 230509.0, 221833.0, 119226.0, 118108.0, 917504.0, 234940.0, 229439.0, 220649.0, 115834.0, 119512.0, 234960.0, 229383.0, 220038.0, 120391.0, 235076.0, 229281.0, 220456.0, 115158.0, 121153.0, 122035.0, 230054.0, 220653.0, 113076.0, 122908.0, 230214.0, 221015.0, 112598.0, 123638.0, 236546.0, 230163.0, 220688.0, 112278.0, 124476.0, 236566.0, 230430.0, 110466.0, 125578.0, 237417.0, 230876.0, 220969.0, 110464.0, 125518.0, 236563.0, 230028.0, 220636.0, 110149.0, 126997.0, 237607.0, 220272.0, 108020.0, 126844.0, 237014.0, 219657.0, 107343.0, 128905.0, 237133.0, 217973.0, 106265.0, 128743.0, 236441.0, 114688.0, 129677.0, 236465.0, 219016.0, 104475.0, 130337.0, 236683.0, 229982.0, 218499.0, 103058.0, 131836.0, 237601.0, 229146.0, 217850.0));
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

                        ArrayList<Float> listCon = new ArrayList(Arrays.asList(225646.0, 234306.0, 234849.0, 230242.0, 225590.0, 229498.0, 234651.0, 234635.0, 229451.0, 225371.0, 228420.0, 229014.0, 225213.0, 234150.0, 230061.0, 225406.0, 234475.0, 225580.0, 231192.0, 234641.0, 225105.0, 228938.0, 224959.0, 228676.0, 224620.0, 228491.0, 233822.0, 233613.0, 227686.0, 227194.0, -4194304.0, 233232.0, 226263.0, 232887.0, 226550.0, 230482.0, 234149.0, 227693.0, 225040.0, 230572.0, 227484.0, 224816.0, 230051.0, 226919.0, 224542.0, 229744.0, 233711.0, 232410.0, 225919.0, 230208.0, 233829.0, 226116.0, 224424.0, 229931.0, 233753.0, 232404.0, 225949.0, 224528.0, 230014.0, 226262.0, 224108.0, 228726.0, 233508.0, 231988.0, 225461.0, 874.0, 228101.0, 233110.0, 225554.0, 224065.0, 229544.0, 233394.0, 231913.0, 225439.0, 224086.0, 229422.0, 233420.0, 231781.0, 225293.0, 224093.0, 229408.0, 233373.0, 231883.0, 225373.0, 223964.0, 229249.0, 231584.0, 225254.0, 225003.0, 231445.0, 225191.0, 224498.0, 230549.0, 224184.0, 229573.0, 233584.0, 232121.0, 224133.0, 229471.0, 224813.0, 231207.0, 224752.0, 231167.0, 224128.0, 229935.0, 224566.0, 224451.0, 230750.0, 233158.0, 230524.0, 224277.0, 223651.0, 229138.0, 232937.0, 230191.0, 224096.0, 224431.0, 224484.0, 224235.0, 230449.0, 230420.0, 224178.0, 224050.0, 230074.0, 223621.0, 223726.0, 229816.0, 232873.0, 230341.0, 224166.0, 224975.0, 231443.0, 224213.0, 224548.0, 231023.0, 224275.0, 224363.0, 230721.0, 224051.0, 224614.0, 231095.0, 224831.0, 231337.0, 223921.0, 225433.0, 231834.0, 223796.0, 224138.0, 230308.0, 232675.0, 228845.0, 223321.0, 223758.0, 230059.0, 232575.0, 228758.0, 223306.0, 223781.0, 230099.0, 232609.0, 228814.0, 223364.0, 225133.0, 231541.0, 232688.0, 228864.0, 223466.0, 224534.0, 231022.0, 223617.0, 225388.0, 223733.0, 225544.0, 233054.0, 225403.0, 235736.0, 185823.0, 104903.0, 222643.0, 235269.0, 225654.0, 184728.0, 103385.0, 222339.0, 224815.0, 104610.0, 222634.0, 234173.0, 224685.0, 183009.0, 104787.0, 179141.0, 222803.0, 233487.0, 224128.0, 178793.0, 101741.0, 222598.0, 233236.0, 223753.0, 178873.0, 102742.0, 222831.0, 98300.0, 221898.0, 223614.0, 175255.0, 98969.0, 222389.0, 232691.0, 223363.0, 175547.0, 98568.0, 221978.0, 232415.0, 245760.0, 222443.0, 232250.0, 223007.0, 172189.0, 98438.0, 222536.0, 231898.0, 222601.0, 171519.0, 98403.0, 222578.0, 231931.0, 222938.0, 904.0, 222541.0, 169613.0, 97898.0, 222579.0, 230918.0, 221703.0, 168716.0, 97943.0, 222321.0, 230392.0, 220876.0, 167960.0, 222943.0, 222241.0, 165636.0, 96649.0, 222819.0, 230577.0, 221433.0, 222977.0, 230247.0, 221019.0, 165089.0, 98278.0, 222957.0, 220781.0, 160919.0, 96746.0, 223496.0, 230108.0, 221231.0, 162389.0, 97916.0, 223689.0, 230131.0, 221403.0, 162885.0, 98624.0, 224192.0, 159261.0, 221352.0, 158168.0, 98790.0, 225176.0, 229950.0, 221728.0, 158160.0, 100411.0, 225878.0, 221766.0, 157616.0, 100924.0, 226107.0, 229829.0, 221523.0, 100494.0, 226352.0, 230618.0, 222954.0, 156119.0, 101961.0, 226925.0, 230651.0, 222725.0, 155440.0, 102846.0, 227448.0, 230648.0, 222814.0, 222991.0, 101646.0, 227913.0, 231253.0, 223811.0, 153201.0, 103135.0, 228528.0, 231343.0, 223653.0, 152273.0, 103209.0, 228616.0, 231243.0, 152525.0, 227984.0, 229890.0, 222229.0, 149834.0, 102885.0, 227002.0, 229076.0, 221894.0, 149686.0, 103488.0, 227250.0, 228804.0, 221266.0, 180224.0, 230873.0, 223736.0, 147211.0, 104911.0, 229701.0, 230612.0, 223185.0, 147644.0, 105599.0, 229721.0, 230272.0, 222744.0, 146869.0, 106201.0, 231097.0, 223647.0, 105553.0, 230434.0, 230974.0, 223174.0, 143461.0, 105112.0, 230545.0, 230626.0, 222421.0, 141093.0, 230046.0, 222061.0, 139394.0, 103836.0, 230571.0, 222626.0, 138659.0, 230959.0, 230129.0, 222017.0, 231847.0, 222581.0, 136126.0, 906.0, 230833.0, 222442.0, 135426.0, 232274.0, 230878.0, 223106.0, 107504.0, 231579.0, 108983.0, 233248.0, 231521.0, 223706.0, 133324.0, 109546.0, 233411.0, 231379.0, 223640.0, 134396.0, 111227.0, 233693.0, 230942.0, 112123.0, 231686.0, 129026.0, 112833.0, 234559.0, 231264.0, 129261.0, 112795.0, 234406.0, 231212.0, 235689.0, 232409.0, 224278.0, 126874.0, 115489.0, 236026.0, 124878.0, 116034.0, 235595.0, 231669.0, 223709.0, 124429.0, 117014.0, 235679.0, 231658.0, 223734.0, 123778.0, 116907.0, 235792.0, 231391.0, 223016.0, 122648.0, 117433.0, 235712.0, 119233.0, 117564.0, 235575.0, 230509.0, 221833.0, 119226.0, 118108.0, 917504.0, 234940.0, 229439.0, 220649.0, 115834.0, 119512.0, 234960.0, 229383.0, 220038.0, 120391.0, 235076.0, 229281.0, 220456.0, 115158.0, 121153.0, 122035.0, 230054.0, 220653.0, 113076.0, 122908.0, 230214.0, 221015.0, 112598.0, 123638.0, 236546.0, 230163.0, 220688.0, 112278.0, 124476.0, 236566.0, 230430.0, 110466.0, 125578.0, 237417.0, 230876.0, 220969.0, 110464.0, 125518.0, 236563.0, 230028.0, 220636.0, 110149.0, 126997.0, 237607.0, 220272.0, 108020.0, 126844.0, 237014.0, 219657.0, 107343.0, 128905.0, 237133.0, 217973.0, 106265.0, 128743.0, 236441.0, 114688.0, 129677.0, 236465.0, 219016.0, 104475.0, 130337.0, 236683.0, 229982.0, 218499.0, 103058.0, 131836.0, 237601.0, 229146.0, 217850.0));
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
                    if (context.toString()==s1) {
                        Log.d("ColorPattern", s1);
                    }


// We can then use the data



                    Log.d("Thread DATA", String.valueOf(dataValues));

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
