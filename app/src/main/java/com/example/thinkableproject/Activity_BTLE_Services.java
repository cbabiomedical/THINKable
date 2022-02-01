package com.example.thinkableproject;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_BTLE_Services extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    private final static String TAG = Activity_BTLE_Services.class.getSimpleName();

    public static final String EXTRA_NAME = "android.aviles.bletutorial.Activity_BTLE_Services.NAME";
    public static final String EXTRA_ADDRESS = "android.aviles.bletutorial.Activity_BTLE_Services.ADDRESS";

    private ListAdapter_BTLE_Services expandableListAdapter;
    private ExpandableListView expandableListView;
    ArrayList dataValues = new ArrayList();
    boolean df = false;
    BroadcastReceiver_BTLE_GATT broadcastReceiver_btle_gatt;

    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;

    private Intent mBTLE_Service_Intent;
    private Service_BTLE_GATT mBTLE_Service;
    private boolean mBTLE_Service_Bound;
    private BroadcastReceiver_BTLE_GATT mGattUpdateReceiver;
    BluetoothDevice device;
    BluetoothGattCallback gattCallback;
    Intent intent;
    Button testActivity;
    private String name;
    private String address;

    private ServiceConnection mBTLE_ServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Service_BTLE_GATT.BTLeServiceBinder binder = (Service_BTLE_GATT.BTLeServiceBinder) service;
            mBTLE_Service = binder.getService();
            mBTLE_Service_Bound = true;

            if (!mBTLE_Service.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
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

    public ArrayList<BluetoothGattService> getServices_ArrayList() {
        return services_ArrayList;
    }

    public void setServices_ArrayList(ArrayList<BluetoothGattService> services_ArrayList) {
        this.services_ArrayList = services_ArrayList;
    }

    public HashMap<String, BluetoothGattCharacteristic> getCharacteristics_HashMap() {
        return characteristics_HashMap;
    }

    public void setCharacteristics_HashMap(HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap) {
        this.characteristics_HashMap = characteristics_HashMap;
    }

    public HashMap<String, ArrayList<BluetoothGattCharacteristic>> getCharacteristics_HashMapList() {
        return characteristics_HashMapList;
    }

    public void setCharacteristics_HashMapList(HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList) {
        this.characteristics_HashMapList = characteristics_HashMapList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btle_services);

        testActivity = findViewById(R.id.button);

        testActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Concentration_Daily.class));
            }
        });


        Handler handler = new Handler();


//        BluetoothGatt bluetoothGatt= device.connectGatt(Activity_BTLE_Services.this, false, gattCallback);
//        Log.d("Services", String.valueOf(bluetoothGatt.getServices()));

        Intent intent = getIntent();
        name = intent.getStringExtra(Activity_BTLE_Services.EXTRA_NAME);
        address = intent.getStringExtra(Activity_BTLE_Services.EXTRA_ADDRESS);

        services_ArrayList = new ArrayList<>();
        characteristics_HashMap = new HashMap<>();
        characteristics_HashMapList = new HashMap<>();

        expandableListAdapter = new ListAdapter_BTLE_Services(
                this, services_ArrayList, characteristics_HashMapList);

        expandableListView = (ExpandableListView) findViewById(R.id.lv_expandable);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(this);

        ((TextView) findViewById(R.id.tv_name)).setText(name + " Services");
        ((TextView) findViewById(R.id.tv_address)).setText(address);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                while (!df) {
                    int intent = BluetoothProfile.STATE_CONNECTED;
                    Log.d("Device Thread", String.valueOf(intent));
                    Log.d("Thread", String.valueOf(services_ArrayList));
                    for (int i = 0; i < services_ArrayList.size(); i++) {
                        String uuid = String.valueOf((services_ArrayList.get(i).getUuid()));
                        BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//            mBTLE_Service.setCharacteristicNotification(bluetoothGattCharacteristic, true);
                        if (uuid.equals("6c6f36f5-7601-465f-9421-ce3c46fba8ae")) {
                            Log.d("Thread Check for Char", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getUuid()));
                            Log.d("Thread Blue Value", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getValue()));
                            byte[] data = services_ArrayList.get(i).getCharacteristics().get(0).getValue();
                            if (data != null) {

                                dataValues.add(Utils.hexToString(data));
                                Log.d("Thread DATA", String.valueOf(dataValues));
//
                            }
                        }
                    }
                    Log.d("TAG", "On Pause");
                }
            }

        }, 10);


    }

    @Override
    protected void onStart() {
        super.onStart();
        df = true;

//        boolean connected=broadcastReceiver_btle_gatt.ismConnected();
        int intent = BluetoothProfile.STATE_CONNECTED;
        Log.d("Device On Pause", String.valueOf(intent));
        Log.d("OnPause", String.valueOf(services_ArrayList));
        for (int i = 0; i < services_ArrayList.size(); i++) {
            String uuid = String.valueOf((services_ArrayList.get(i).getUuid()));
            BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//            mBTLE_Service.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            if (uuid.equals("6c6f36f5-7601-465f-9421-ce3c46fba8ae")) {
                Log.d("On Start Check for Char", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getUuid()));
                Log.d("On Start Blue Value", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getValue()));
                byte[] data = services_ArrayList.get(i).getCharacteristics().get(0).getValue();
                if (data != null) {

                    dataValues.add(Utils.hexToString(data));
                    Log.d("On Start DATA", String.valueOf(dataValues));
//
                }
            }
        }
        Log.d("TAG", "On Pause");

        mGattUpdateReceiver = new BroadcastReceiver_BTLE_GATT(this);
        registerReceiver(mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter());

        mBTLE_Service_Intent = new Intent(this, Service_BTLE_GATT.class);
        bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
        startService(mBTLE_Service_Intent);
//        Log.d("Services",mBTLE_Service.getPackageName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "On Resume");
        int intent = BluetoothProfile.STATE_CONNECTED;
        Log.d("Device On Resume", String.valueOf(intent));
        Log.d("OnPause", String.valueOf(services_ArrayList));
        for (int i = 0; i < services_ArrayList.size(); i++) {
            String uuid = String.valueOf((services_ArrayList.get(i).getUuid()));
            BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//            mBTLE_Service.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            if (uuid.equals("6c6f36f5-7601-465f-9421-ce3c46fba8ae")) {
                Log.d("On Resume Check Char", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getUuid()));
                Log.d("On Resume Blue Value", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getValue()));
                byte[] data = services_ArrayList.get(i).getCharacteristics().get(0).getValue();
                if (data != null) {

                    dataValues.add(Utils.hexToString(data));
                    Log.d("On Resume DATA", String.valueOf(dataValues));
//
                }
            }
        }
        Log.d("TAG", "On Pause");
    }


    @Override
    protected void onPause() {
        super.onPause();
//        boolean connected=broadcastReceiver_btle_gatt.ismConnected();
        int intent = BluetoothProfile.STATE_CONNECTED;
        Log.d("Device On Pause", String.valueOf(intent));
        Log.d("OnPause", String.valueOf(services_ArrayList));
        for (int i = 0; i < services_ArrayList.size(); i++) {
            String uuid = String.valueOf((services_ArrayList.get(i).getUuid()));
            BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//            mBTLE_Service.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            if (uuid.equals("6c6f36f5-7601-465f-9421-ce3c46fba8ae")) {
                Log.d("On Pause Check for Char", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getUuid()));
                Log.d("On Pause Blue Value", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(0).getValue()));
                byte[] data = services_ArrayList.get(i).getCharacteristics().get(0).getValue();
                if (data != null) {

                    dataValues.add(Utils.hexToString(data));
                    Log.d("On Pause DATA", String.valueOf(dataValues));
//
                }
            }
        }
        Log.d("TAG", "On Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();

//        unregisterReceiver(mGattUpdateReceiver);
//        unbindService(mBTLE_ServiceConnection);
//        mBTLE_Service_Intent = null;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Log.d("Clicked", "This Character was clicked");
//        BluetoothGattCharacteristic characteristic = characteristics_HashMapList.get(
//                services_ArrayList.get(groupPosition).getUuid().toString())
//                .get(childPosition);
//
//        if (Utils.hasWriteProperty(characteristic.getProperties()) != 0) {
//            String uuid = characteristic.getUuid().toString();
//
//            Dialog_BTLE_Characteristic dialog_btle_characteristic = new Dialog_BTLE_Characteristic();
//
//            dialog_btle_characteristic.setTitle(uuid);
//            dialog_btle_characteristic.setService(mBTLE_Service);
//            dialog_btle_characteristic.setCharacteristic(characteristic);
//
//            dialog_btle_characteristic.show(getFragmentManager(), "Dialog_BTLE_Characteristic");
//        } else if (Utils.hasReadProperty(characteristic.getProperties()) != 0) {
//            if (mBTLE_Service != null) {
//                mBTLE_Service.readCharacteristic(characteristic);
//            }
//        } else if (Utils.hasNotifyProperty(characteristic.getProperties()) != 0) {
//            if (mBTLE_Service != null) {
//                mBTLE_Service.setCharacteristicNotification(characteristic, true);
//            }
//        }

        return false;
    }

    public void updateServices() {

        if (mBTLE_Service != null) {

            services_ArrayList.clear();
            characteristics_HashMap.clear();
            characteristics_HashMapList.clear();

            List<BluetoothGattService> servicesList = mBTLE_Service.getSupportedGattServices();

            for (BluetoothGattService service : servicesList) {

                services_ArrayList.add(service);

                List<BluetoothGattCharacteristic> characteristicsList = service.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> newCharacteristicsList = new ArrayList<>();

                for (BluetoothGattCharacteristic characteristic : characteristicsList) {
                    characteristics_HashMap.put(characteristic.getUuid().toString(), characteristic);
                    newCharacteristicsList.add(characteristic);

                    if (Utils.hasNotifyProperty(characteristic.getProperties()) != 0) {
                        mBTLE_Service.setCharacteristicNotification(characteristic, true);
                        Log.d("ST", "ENABLED");
                    }
                }

                characteristics_HashMapList.put(service.getUuid().toString(), newCharacteristicsList);
            }

            if (servicesList != null && servicesList.size() > 0) {
                expandableListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateCharacteristic() {
        expandableListAdapter.notifyDataSetChanged();
    }
}
