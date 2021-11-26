package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Notification1 extends AppCompatActivity {

    SwitchCompat sw1, swb, swc, swd;
    Button buttonNO;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification1);

        sw1 = findViewById(R.id.sw1);
        swb = findViewById(R.id.swb);
        swc = findViewById(R.id.swc);
        swd = findViewById(R.id.swd);
        buttonNO = findViewById(R.id.buttonNO);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        createNotificationChannels();
    }

    private void createNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "Popup Notifications",
                    NotificationManager.IMPORTANCE_MIN);
            channel.enableLights(true);
            channel.setDescription("Popup Notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            NotificationChannel channel1 = new NotificationChannel("My Notification1", "High pority Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("High pority Notifications");
            NotificationManager manager1 = getSystemService(NotificationManager.class);
            manager1.createNotificationChannel(channel1);

            NotificationChannel channel2 = new NotificationChannel("My Notification2", "Quote Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel2.setDescription("Quote Notifications");
            NotificationManager manager2 = getSystemService(NotificationManager.class);
            manager2.createNotificationChannel(channel2);
        }


        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (isChecked) {
                    //To change the text near to switch
                    Log.d("You are :", "Checked");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification1.this, "My Notification");
                    builder.setContentTitle("My Title");
                    builder.setContentText("Hellooooo");
                    builder.setSmallIcon(R.drawable.splashlogo);
                    builder.setAutoCancel(true);
                    builder.setPriority(NotificationCompat.PRIORITY_LOW);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);


                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Notification1.this);
                    managerCompat.notify(1, builder.build());


                } else {
                    //To change the text near to switch
                    Log.d("You are :", " Not Checked");
                    Toast.makeText(Notification1.this, "You Turn off Popup Notifications", Toast.LENGTH_LONG)
                            .show();
                }
                //then on another method or where you want


            }
        });

        swb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (isChecked) {
                    //To change the text near to switch
                    Log.d("You are :", "Checked");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification1.this, "My Notification1");
                    builder.setContentTitle("HI THEREEE");
                    builder.setContentText("Hellooooo");
                    builder.setSmallIcon(R.drawable.splashlogo);
                    builder.setAutoCancel(true);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Notification1.this);
                    managerCompat.notify(1, builder.build());


                } else {
                    //To change the text near to switch
                    Log.d("You are :", " Not Checked");
                    Toast.makeText(Notification1.this, "You Turn off High Priority Notifications", Toast.LENGTH_LONG)
                            .show();
                }
                //then on another method or where you want


            }
        });

        swc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (isChecked) {
                    //To change the text near to switch
                    vibrator.vibrate(1000);


                } else {
                    //To change the text near to switch
                    Log.d("You are :", " Not Checked");
                    Toast.makeText(Notification1.this, "You Turn off Vibration", Toast.LENGTH_LONG)
                            .show();
                }
                //then on another method or where you want


            }
        });


        swd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (isChecked) {
                    //To change the text near to switch
                    Log.d("You are :", "Checked");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification1.this, "My Notification2");
                    builder.setContentTitle("HI Prensss");
                    builder.setContentText("Hellooooo");
                    builder.setSmallIcon(R.drawable.splashlogo);
                    builder.setAutoCancel(true);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Notification1.this);
                    managerCompat.notify(1, builder.build());


                } else {
                    //To change the text near to switch
                    Log.d("You are :", " Not Checked");
                    Toast.makeText(Notification1.this, "You Turn off Quote Notifications", Toast.LENGTH_LONG)
                            .show();
                }
                //then on another method or where you want


            }
        });


    }
}