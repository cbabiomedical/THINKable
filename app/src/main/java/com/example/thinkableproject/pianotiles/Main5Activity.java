package com.example.thinkableproject.pianotiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.thinkableproject.R;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5);
        final Intent i2;
        i2 = getIntent();

        final Intent i=new Intent(this,Main3Activity.class);
        final Task myTask=new Task();
        myTask.setOnPreExecuteCalledListener(new Task.OnPreExecuteCalledListener() {
            @Override
            public void onpreExecutecalled() {
                if (i2 != null) {
                    if (i2.getBooleanExtra("EXIT", false)) {
                        myTask.cancel(true);
                        finish();
                    }
                }
            }
        });
        myTask.execute(1);
        myTask.setOnPostExecuteDone(new Task.OnPostExecuteDone() {
            @Override
            public void onDone() {
                startActivity(i);
            }
        });
    }
}
