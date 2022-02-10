package com.example.thinkableproject.puzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.Lang;
import com.example.thinkableproject.puzzle.Class.Sound;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {




    SwitchCompat soundSwitch;
    boolean langFlag = false;
    Sound sound=new Sound();
    String tempLang = "";
    Dialog dialog;
    boolean finish = false;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sound.release();
        finish =true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Sound.backgroundMusic.isPlaying())
            Sound.backgroundMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!Sound.activitySwitchFlag)
            if(!finish)
                Sound.backgroundMusic.pause();
        Sound.activitySwitchFlag = false;
        finish=false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ImageButton infoBtn = this.findViewById(R.id.howToPlayBtn);
        Button startButton = findViewById(R.id.start_button);
        Button settingbutton = findViewById(R.id.setting_button);
//        Button leaderBoard = findViewById(R.id.highscore_button);

//        final ImageButton facebook =findViewById(R.id.facebook);
//        final ImageButton instagram =findViewById(R.id.instagram);
//        final ImageButton github =findViewById(R.id.github);
//        final TextView puzzelText = findViewById(R.id.PuzzelIt);
//        github.setOnClickListener(onClickListener);
//        instagram.setOnClickListener(onClickListener);
//        facebook.setOnClickListener(onClickListener);
        settingbutton.setOnClickListener(onClickListener);
        startButton.setOnClickListener(onClickListener);
        //leaderBoard.setOnClickListener(onClickListener);


        Sound.backgroundMusic.start();

        YoYo.with(Techniques.Shake)
                .duration(3000)
                .repeat(-1)
                .playOn(startButton);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                puzzelText.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInDown)
//                        .duration(4000)
//                        .interpolate(new AccelerateDecelerateInterpolator())
//                        .interpolate(new BounceInterpolator())
//                        .repeat(0)
//                        .playOn(puzzelText);
//                facebook.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInRight)
//                        .duration(1000)
//                        .repeat(0)
//                        .playOn( facebook);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(1000)
//                        .repeat(0)
//                        .playOn( facebook);
//                instagram.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInRight)
//                        .duration(2000)
//                        .repeat(0)
//                        .playOn( instagram);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(2000)
//                        .repeat(0)
//                        .playOn( instagram);
//                github.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInRight)
//                        .duration(3000)
//                        .repeat(0)
//                        .playOn( github);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(3000)
//                        .repeat(0)
//                        .playOn( github);
//
//            }
//        },1000);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent;
            switch (v.getId()) {
//                case R.id.facebook:
//                    Sound.buttonGameSound.start();
//                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/gal.tager"));
//                    startActivity(browserIntent);
//                    break;
//                case R.id.instagram:
//                    Sound.buttonGameSound.start();
//                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/tager_gal/"));
//                    startActivity(browserIntent);
//                    break;
//                case R.id.github:
//                    Sound.buttonGameSound.start();
//                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Galtager"));
//                    startActivity(browserIntent);
//                    break;
                case R.id.setting_button:
                    openDialog();
                    Sound.menuClickSound.start();
                    break;
                case R.id.start_button:
                    Sound.menuClickSound.start();
                    Intent chooseDifficultIntent = new Intent(HomeActivity.this, ChooseDifficultActivity.class);
                    startActivity(chooseDifficultIntent);
                    Sound.activitySwitchFlag=true;
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                    break;
//                case R.id.highscore_button:
//                    Sound.menuClickSound.start();
//                    Sound.activitySwitchFlag=true;
//                    Intent leaderBoardIntent = new Intent(HomeActivity.this, ViewPager.class);
//                    startActivity(leaderBoardIntent);
//                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
//                    break;
            }
        }
    };
    public void openDialogHome(){
        final Dialog infoDialog = new Dialog(HomeActivity.this);
        infoDialog.setContentView(R.layout.dialog_info);
        final Button okBtn = infoDialog.findViewById(R.id.gotItButton);
        infoDialog.show();
        final ImageView imageViewCard1=infoDialog.findViewById(R.id.card1);
        final ImageView imageViewCard2=infoDialog.findViewById(R.id.card2);
        final ImageView imageViewCard3=infoDialog.findViewById(R.id.card3);
        final ImageView imageViewCard4=infoDialog.findViewById(R.id.card4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageViewCard1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft)
                        .duration(1000)
                        .repeat(0)
                        .playOn(imageViewCard1);
                imageViewCard2.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight)
                        .duration(3000)
                        .repeat(0)
                        .playOn(imageViewCard2);
                imageViewCard3.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp)
                        .duration(5000)
                        .repeat(0)
                        .playOn(imageViewCard3);
                imageViewCard4.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn)
                        .duration(7000)
                        .repeat(0)
                        .playOn(imageViewCard4);
            }
        },1000);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sound.buttonGameSound.start();
                infoDialog.dismiss();
            }
        });
    }

    public void openDialog(){
        langFlag=false;
        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_setting);
        dialog.show();

//        Chip hebrewChip = dialog.findViewById(R.id.hebrewChip);
//        Chip englishChip = dialog.findViewById(R.id.englishChip);

        soundSwitch = dialog.findViewById(R.id.sound_switch);
        soundSwitch.setChecked(Sound.check);
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sound.check= isChecked;
            }
        });

        Button submitButton = dialog.findViewById(R.id.submitButton);

//        hebrewChip.setOnClickListener(dialogClickListener);
//        englishChip.setOnClickListener(dialogClickListener);
        submitButton.setOnClickListener(dialogClickListener);

    }
    View.OnClickListener dialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
//                case R.id.hebrewChip:
//                    Sound.buttonGameSound.start();
//                    langFlag = true;
//                    tempLang = "iw";
//                    break;
//                case R.id.englishChip:
//                    Sound.buttonGameSound.start();
//                    langFlag=true;
//                    tempLang ="en";
//                    break;
                case R.id.submitButton:
                    Sound.menuClickSound.start();
                    sound.setSounds();
                    sound.setMusic();
                    if(langFlag)
                    {
                        if(!Lang.lang.equals(tempLang))
                        {
                            Lang.lang=tempLang;
                            setAppLocale(Lang.lang);
                            restartActivity();
                        }
                    }
                    dialog.dismiss();
                    break;
            }
        }
    };

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration,displayMetrics);
    }

    private void restartActivity()
    {
        finish();
        startActivity(getIntent());
        Sound.activitySwitchFlag=true;

    }

}