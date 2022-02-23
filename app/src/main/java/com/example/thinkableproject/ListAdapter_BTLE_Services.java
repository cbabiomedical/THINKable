package com.example.thinkableproject;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kelvin on 5/8/16.
 */
public class ListAdapter_BTLE_Services extends BaseExpandableListAdapter {
    private Activity activity;
    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMap;
    ArrayList dataValues = new ArrayList();
    BluetoothGattCallback gattCallback;
    BluetoothDevice device;
    Context context;

    File fileName;

    public ListAdapter_BTLE_Services(Activity activity, ArrayList<BluetoothGattService> listDataHeader,
                                     HashMap<String, ArrayList<BluetoothGattCharacteristic>> listChildData) {

        this.activity = activity;
        this.services_ArrayList = listDataHeader;
        this.characteristics_HashMap = listChildData;
    }
    public ListAdapter_BTLE_Services( Context context, ArrayList<BluetoothGattService> listDataHeader,
                                      HashMap<String, ArrayList<BluetoothGattCharacteristic>> listChildData) {

        this.context = context;
        this.services_ArrayList = listDataHeader;
        this.characteristics_HashMap = listChildData;
    }

    public ListAdapter_BTLE_Services() {
    }

    public ArrayList<BluetoothGattService> getServices_ArrayList() {
        return services_ArrayList;
    }

    public void setServices_ArrayList(ArrayList<BluetoothGattService> services_ArrayList) {
        this.services_ArrayList = services_ArrayList;
    }

    public HashMap<String, ArrayList<BluetoothGattCharacteristic>> getCharacteristics_HashMap() {
        return characteristics_HashMap;
    }

    public void setCharacteristics_HashMap(HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMap) {
        this.characteristics_HashMap = characteristics_HashMap;
    }

    //    BluetoothGatt  bluetoothGatt= device.connectGatt(context, false, gattCallback);


    @Override

    public int getGroupCount() {
        return services_ArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return characteristics_HashMap.get(
                services_ArrayList.get(groupPosition).getUuid().toString()).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return services_ArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return characteristics_HashMap.get(
                services_ArrayList.get(groupPosition).getUuid().toString()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

//    public List<BluetoothGattService> getSupportedGattServices() {
//        if (bluetoothGatt == null) return null;
//        Log.d("Get Services", String.valueOf(bluetoothGatt.getServices()));
//        return bluetoothGatt.getServices();
//    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        BluetoothGattService bluetoothGattService = (BluetoothGattService) getGroup(groupPosition);

        String serviceUUID = bluetoothGattService.getUuid().toString();
        ArrayList<BluetoothGattService> bluetoothGattServices;
        Log.d("Service UUID", serviceUUID);
        Log.d("Map", String.valueOf(characteristics_HashMap));
        Log.d("Service List", String.valueOf(services_ArrayList));
        for (int i = 0; i < services_ArrayList.size(); i++) {
            String uuid = String.valueOf((services_ArrayList.get(i).getUuid()));
            if (uuid.equals("4fafc201-1fb5-459e-8fcc-c5c9c331914b")) {
                Log.d("Check for Char", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(1).getUuid()));
                Log.d("Bluetooth Value", String.valueOf(services_ArrayList.get(i).getCharacteristics().get(1).getValue()));
            }


        }

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.btle_service_list_item, null);
        }

        TextView tv_service = (TextView) convertView.findViewById(R.id.tv_service_uuid);
        tv_service.setText("S: " + serviceUUID);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) getChild(groupPosition, childPosition);
        Log.d("Group Position", String.valueOf(groupPosition));
        Log.d("Child Position", String.valueOf(childPosition));

        String characteristicUUID = bluetoothGattCharacteristic.getUuid().toString();
        Log.d("Characteristic UUID", characteristicUUID);
        Log.d("CHAR", String.valueOf(bluetoothGattCharacteristic.getValue()));
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.btle_characteristics_list_item, null);
        }

        TextView tv_service = (TextView) convertView.findViewById(R.id.tv_characteristic_uuid);
        tv_service.setText("C: " + characteristicUUID);

        int properties = bluetoothGattCharacteristic.getProperties();

        TextView tv_property = (TextView) convertView.findViewById(R.id.tv_properties);
        StringBuilder sb = new StringBuilder();

        if (Utils.hasReadProperty(properties) != 0) {
            sb.append("R");
        }

        if (Utils.hasWriteProperty(properties) != 0) {
            sb.append("W");
        }

        if (Utils.hasNotifyProperty(properties) != 0) {
            sb.append("N");
        }


        tv_property.setText(sb.toString());

        TextView tv_value = (TextView) convertView.findViewById(R.id.tv_value);

        byte[] data = bluetoothGattCharacteristic.getValue();
        if (data != null) {
            tv_value.setText("Value: " + Utils.hexToString(data));
            dataValues.add(Utils.hexToString(data));

            try {
                fileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/values.txt");
                System.out.println("Path" + Environment.getExternalStorageDirectory().getAbsolutePath());
                String line = "";
                FileWriter fwa;
                fwa = new FileWriter(fileName);
                BufferedWriter outputa = new BufferedWriter(fwa);
                int size = dataValues.size();
                for (int i = 0; i < size; i++) {
                    outputa.write(dataValues.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
                }
                Log.d("Array", String.valueOf(dataValues));
                outputa.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
//            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference("BLE Values");
//            try {
//                StorageReference mountainsRef = storageReference1.child("values.txt");
//                InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
//                UploadTask uploadTask = mountainsRef.putStream(stream);
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }


        } else {
            tv_value.setText("Value: ---");
        }

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
