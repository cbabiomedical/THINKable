package com.example.thinkableproject.puzzle.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.Sound;

public class ViewPager extends AppCompatActivity implements View.OnClickListener{
    TextView tvLeft;
    TextView tvRight;
    ImageButton leftNavigate,rightNavigate,backBtn;
    androidx.viewpager.widget.ViewPager viewPager;
    ViewPageFragmentCollectionAdapter adapter;
    String temp;
    int lastScore;

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
            Sound.backgroundMusic.pause();
        Sound.activitySwitchFlag = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        leftNavigate = this.findViewById(R.id.leftBtn);
        rightNavigate = this.findViewById(R.id.rightBtn);
        backBtn = this.findViewById(R.id.bBackMenuPager);

        leftNavigate.setOnClickListener(onClickListener);
        rightNavigate.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);



        viewPager =findViewById(R.id.ViewPager);
        adapter = new ViewPageFragmentCollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        viewPager.arrowScroll(View.FOCUS_LEFT);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tab;
            switch (v.getId()){
                case R.id.rightBtn:
                    tab = viewPager.getCurrentItem();
                    tab++;
                    viewPager.setCurrentItem(tab);
                    Sound.menuClickSound.start();
                    break;
                case R.id.leftBtn:
                    tab = viewPager.getCurrentItem();
                    tab--;
                    viewPager.setCurrentItem(tab);
                    Sound.menuClickSound.start();
                    break;
                case R.id.bBackMenuPager:
                    Sound.activitySwitchFlag=true;
                    Sound.menuClickSound.start();
                    finish();
                    break;


            }
        }
    };
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}

