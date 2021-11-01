package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView getStarted;
    private TextView getStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getStarted = findViewById(R.id.getStart);

        Log.d("USERLOGIN", "----------------------E----------------------------");
        Log.d("USERLOGIN", "----------------------F----------------------------");
        Log.d("USERLOGIN", "----------------------G----------------------------");
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USERLOGIN", "----------------------E----------------------------");
                Log.d("USERLOGIN", "----------------------F----------------------------");
                Log.d("USERLOGIN", "----------------------G----------------------------");
//                Intent intent=new Intent(WelcomeActivity.this,SignInActivity.class);
//                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            trimCache(this);
            Log.d("DELETE ","Cache Deleted onStop");
            // Toast.makeText(this,"onDestroy " ,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("NOT DELETE ","Cache NOT Deleted");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}