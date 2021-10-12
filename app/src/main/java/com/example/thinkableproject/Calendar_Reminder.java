package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

public class Calendar_Reminder extends AppCompatActivity {

    CustomCalendar customCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__reminder);
        customCalendar = findViewById(R.id.customCalendar);

        HashMap<Object, Property> descHashMap = new HashMap<>();
        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;

        defaultProperty.dateTextViewResource = R.id.textView1;
        descHashMap.put("default", defaultProperty);

        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.textView1;
        descHashMap.put("current", currentProperty);

        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.textView1;
        descHashMap.put("present", presentProperty);

        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.textView1;
        descHashMap.put("absent", absentProperty);
        customCalendar.setMapDescToProp(descHashMap);

        HashMap<Integer, Object> dateHashMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
        dateHashMap.put(1, "present");
        dateHashMap.put(2, "absent");
        dateHashMap.put(3, "present");
        dateHashMap.put(4, "absent");
        dateHashMap.put(20, "present");
        dateHashMap.put(30, "absent");

        customCalendar.setDate(calendar);
//        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
//                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" +
//                        (selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.YEAR);
////                Toast.makeText(getApplicationContext(),sDate,Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}