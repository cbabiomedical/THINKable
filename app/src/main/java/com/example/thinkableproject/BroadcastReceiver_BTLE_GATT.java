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
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    Button testActivity;
    FirebaseUser mUser;
    private String name;
    String dateTime;
    private String address;
    File fileName;
    private final static String TAG = Activity_BTLE_Services.class.getSimpleName();
    HashMap hashMap = new HashMap();


    Context context;
    private Activity_BTLE_Services activity;

    public BroadcastReceiver_BTLE_GATT(Activity_BTLE_Services activity) {
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
            if (uuid.equals("6c6f36f5-7601-465f-9421-ce3c46fba8ae")) {
                Log.d("UUID", String.valueOf(activity.getServices_ArrayList().get(i).getUuid()));
                Log.d("Get ", String.valueOf(activity.getServices_ArrayList().get(i).getCharacteristics().get(0).getUuid()));
                Log.d("Get Blue Value", String.valueOf(activity.getServices_ArrayList().get(i).getCharacteristics().get(0).getValue()));
                byte[] data = activity.getServices_ArrayList().get(i).getCharacteristics().get(0).getValue();
                if (data != null) {
//
                    dataValues.add(Utils.hexToString(data));
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
            Utils.toast(activity.getApplicationContext(), "Device Connected");
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
            activity.finish();
        } else if (Service_BTLE_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            activity.updateServices();
        } else if (Service_BTLE_GATT.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

            activity.updateCharacteristic();
        }

        return;
    }
}
