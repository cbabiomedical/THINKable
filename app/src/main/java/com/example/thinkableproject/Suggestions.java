package com.example.thinkableproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Suggestions extends AppCompatActivity {

    GridView coursesGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        coursesGV = findViewById(R.id.idGVcourses);

        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
        courseModelArrayList.add(new CourseModel("Anxiety Reducing", R.drawable.calmit));
        courseModelArrayList.add(new CourseModel("Stress Reducing", R.drawable.wav));
        courseModelArrayList.add(new CourseModel("Sleep Better", R.drawable.moono));
        courseModelArrayList.add(new CourseModel("Focus Improvement", R.drawable.tar));
        courseModelArrayList.add(new CourseModel("Increase Concentration", R.drawable.medi));
        courseModelArrayList.add(new CourseModel("Increase Happiness", R.drawable.aff));

        CourseGVAdapter adapter = new CourseGVAdapter(this, courseModelArrayList);
        coursesGV.setAdapter(adapter);
    }

    public void gotoPrefTwo(View view) {
        Intent intentgotoPreTwo = new Intent(Suggestions.this,PreferencesSecPage.class);


        startActivity(intentgotoPreTwo);

    }
}
