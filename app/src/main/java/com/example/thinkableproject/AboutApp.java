package com.example.thinkableproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        View aboutPage = new AboutPage(this)
                .isRTL(false).setImage(R.drawable.splashlogo1)
                .setDescription(" Welcome to THINKable!" +
                        " We're a small and robust team passionate about increasing concentration and relaxation. This application is developed on purpose to check the concentration relaxation level of users and to provide exercises to improve their concentration and relaxation level. " +
                        "Come and join with us to get a better concentration and relaxation experience in your busy and stressful life...")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("thinkable@gmail.com ")          //email
                .addWebsite("http://www.cba.lk/")         //website
                .addYoutube("https://www.youtube.com/")   //youtube link
                .addPlayStore("com.example.thinkable")     //Play store link
                .addInstagram("thinkable")                 //instagram id
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
        aboutPage.setFocusable(true);
//       aboutPage.setBackground(getResources().getDrawable(R.drawable.about));
//        aboutPage.setBackgroundDrawable(getResources().getDrawable(R.drawable.about));
//        aboutPage.setBackgroundColor(getResources().getColor(R.color.heading_color));
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by CBA", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AboutApp.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}