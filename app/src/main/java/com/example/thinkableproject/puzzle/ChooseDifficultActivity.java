package com.example.thinkableproject.puzzle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.SlicingImage;
import com.example.thinkableproject.puzzle.Class.Sound;
import com.google.android.material.chip.ChipGroup;

public class ChooseDifficultActivity extends AppCompatActivity {
    private ImageButton soundBtn;
    Sound sound = new Sound();
    ImageButton[] zooImages;
    int[] imagesDraw = {R.drawable.lion, R.drawable.dolphin, R.drawable.bird, R.drawable.elephant, R.drawable.dog, R.drawable.horse};
    Drawable imageChoose;
    SlicingImage slicingImage = new SlicingImage();
    private ImageView card24, card15, card9;
    private ImageButton infoBtn;

    @Override
    protected void onStart() {
        super.onStart();
        if (!Sound.backFromGame) {
            if (!Sound.backgroundMusic.isPlaying())
                Sound.backgroundMusic.start();
        }
        Sound.backFromGame = false;


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Sound.activitySwitchFlag)
            Sound.backgroundMusic.pause();
        Sound.activitySwitchFlag = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosse_difficult);



        Button game9Btn = findViewById(R.id.btn1);
        Button game15Btn = findViewById(R.id.btn2);
        Button game24Btn = findViewById(R.id.btn3);
        Button gameZoo = findViewById(R.id.btn4);

        AnimationDrawable animationDrawable = (AnimationDrawable) gameZoo.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        final TextView puzzelTextChoose = findViewById(R.id.Choose);
        final TextView puzzelextLevels = findViewById(R.id.Levels);


        infoBtn = findViewById(R.id.howToPlayBtn);
        ImageButton backBtn = findViewById(R.id.levelBackMenu);
        soundBtn = findViewById(R.id.bSoundOffOnChoose);

//        card9 = findViewById(R.id.card908);
//        card15 = findViewById(R.id.card1515);
//        card24 = findViewById(R.id.card2424);



        game9Btn.setOnClickListener(onClickListener);
        game15Btn.setOnClickListener(onClickListener);
        game24Btn.setOnClickListener(onClickListener);
        gameZoo.setOnClickListener(onClickListener);

        soundBtn.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);

        if (Sound.check)
            soundBtn.setImageResource(R.drawable.soundon);
        else
            soundBtn.setImageResource(R.drawable.soundoff);


        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sound.menuClickSound.start();
                openDialog();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                puzzelTextChoose.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight)
                        .duration(2000)
                        .repeat(0)
                        .playOn(puzzelTextChoose);
                puzzelextLevels.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft)
                        .duration(2000)
                        .repeat(0)
                        .playOn(puzzelextLevels);
                infoBtn.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn)
                        .duration(3000)
                        .repeat(0)
                        .playOn(infoBtn);
                YoYo.with(Techniques.Bounce)
                        .duration(3000)
                        .repeat(-1)
                        .playOn(infoBtn);



            }
        }, 1000);
        //play24();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    Sound.menuClickSound.start();
                    Sound.activitySwitchFlag = true;
                    sound.switchMusic(Sound.gameMusic, Sound.backgroundMusic);
                    newGame(3, "Cards");
                    break;
                case R.id.btn2:
                    Sound.menuClickSound.start();
                    sound.switchMusic(Sound.gameMusic, Sound.backgroundMusic);
                    newGame(4, "Cards");
                    break;
                case R.id.btn3:
                    Sound.menuClickSound.start();
                    sound.switchMusic(Sound.gameMusic, Sound.backgroundMusic);
                    newGame(5, "Cards");
                    break;
                case R.id.btn4:
                    Sound.menuClickSound.start();
                    openZooDialog();
                    break;
                case R.id.levelBackMenu:
                    Sound.menuClickSound.start();
                    back();
                    break;
                case R.id.bSoundOffOnChoose:
                    soundOffOn();
                    Sound.menuClickSound.start();
                    break;
            }
        }
    };


    private void newGame(int level, String isImageChoosen) {
        Intent gameChooserIntent = new Intent();
        switch (level) {
            case 3:
                gameChooserIntent = new Intent(ChooseDifficultActivity.this, GameActivity9.class);
                gameChooserIntent.putExtra("whatToShow", isImageChoosen);
                break;
            case 4:
                gameChooserIntent = new Intent(ChooseDifficultActivity.this, GameActivity15.class);
                gameChooserIntent.putExtra("whatToShow", isImageChoosen);
                break;
            case 5:
                gameChooserIntent = new Intent(ChooseDifficultActivity.this, GameActivity24.class);
                gameChooserIntent.putExtra("whatToShow", isImageChoosen);
                break;
        }
        Sound.activitySwitchFlag = true;
        Sound.backFromGame = true;
        startActivity(gameChooserIntent);
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }

    public void back() {

        finish();
    }

    public void openDialog() {
        final Dialog infoDialog = new Dialog(ChooseDifficultActivity.this);
        infoDialog.setContentView(R.layout.dialog_info);
        Button okBtn = infoDialog.findViewById(R.id.gotItButton);
        final ImageView imageViewCard1=infoDialog.findViewById(R.id.card1);
        final ImageView imageViewCard2=infoDialog.findViewById(R.id.card2);
        final ImageView imageViewCard3=infoDialog.findViewById(R.id.card3);
        final ImageView imageViewCard4=infoDialog.findViewById(R.id.card4);
        new Handler().postDelayed(new Runnable() {
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



        infoDialog.show();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sound.buttonGameSound.start();
                infoDialog.dismiss();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void openZooDialog() {
        final Dialog zooDialog = new Dialog(ChooseDifficultActivity.this);
        zooDialog.setContentView(R.layout.dialog_zoo);

        final int[] levelChoosed = {3, 100, 300};
        imageChoose = getResources().getDrawable(imagesDraw[1]);
        zooImages = new ImageButton[6];
        Button startBtn = zooDialog.findViewById(R.id.startButton);
        zooImages[0] = zooDialog.findViewById(R.id.lion);
        zooImages[1] = zooDialog.findViewById(R.id.dolphin);
        zooImages[2] = zooDialog.findViewById(R.id.bird);
        zooImages[3] = zooDialog.findViewById(R.id.elephant);
        zooImages[4] = zooDialog.findViewById(R.id.dog);
        zooImages[5] = zooDialog.findViewById(R.id.horse);

        for (int i = 0; i < 6; i++) {
            zooImages[i].setOnClickListener(zooClickListener);
            BitmapDrawable drawable = (BitmapDrawable) zooImages[i].getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 135, 135, true);
            zooImages[i].setImageBitmap(scaledBitmap);
        }
        final ChipGroup level = zooDialog.findViewById(R.id.levelChipGroup);
        level.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.level3:
                        levelChoosed[0] = 3;
                        levelChoosed[1] = 100;
                        levelChoosed[2] = 300;
                        break;
                    case R.id.level4:
                        levelChoosed[0] = 4;
                        levelChoosed[1] = 80;
                        levelChoosed[2] = 320;
                        break;
                    case R.id.level5:
                        levelChoosed[0] = 5;
                        levelChoosed[1] = 60;
                        levelChoosed[2] = 300;
                        break;
                }
            }
        });

        zooDialog.show();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sound.buttonGameSound.start();
                zooDialog.dismiss();
                slicingImage.splitImage(imageChoose, levelChoosed[0], levelChoosed[0], levelChoosed[2], levelChoosed[1]);
                newGame(levelChoosed[0], "Zoo");
            }
        });

    }

    View.OnClickListener zooClickListener = new View.OnClickListener() {
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            int i = 0;
            while (zooImages[i].getId() != v.getId()) {
                i++;
            }
            imageChoose = getResources().getDrawable(imagesDraw[i]);
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void soundOffOn() {
        if (Sound.check) {
            Sound.check = false;
            soundBtn.setImageResource(R.drawable.soundoff);
        } else {
            Sound.check = true;
            soundBtn.setImageResource(R.drawable.soundon);
        }
        sound.setSounds();
        sound.setMusic();
    }


//    private void play9() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card9.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideOutUp)
//                        .duration(20000)
//                        .repeat(0)
//                        .interpolate(new AccelerateDecelerateInterpolator())
//                        .interpolate(new BounceInterpolator())
//                        .withListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animator) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animator) {
//                                card9.setVisibility(View.INVISIBLE);
//                                play15();
//
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animator) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animator) {
//
//                            }
//                        })
//                        .playOn(card9);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(20000)
//                        .repeat(10)
//                        .playOn(card9);
//            }
//        }, 100);
//    }
//    private void play15() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card15.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideOutRight)
//                        .duration(15000)
//                        .repeat(0)
//                        .withListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animator) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animator) {
//                                card15.setVisibility(View.INVISIBLE);
//                                play24();
//
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animator) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animator) {
//
//                            }
//                        })
//                        .playOn(card15);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(15000)
//                        .repeat(10)
//                        .playOn(card15);
//            }
//        }, 100);
//    }
//    private  void play24()
//    {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card24.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideOutDown)
//                        .duration(20000)
//                        .repeat(0)
//                        .interpolate(new AccelerateDecelerateInterpolator())
//                        .interpolate(new BounceInterpolator())
//                        .withListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animator) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animator) {
//                                card24.setVisibility(View.INVISIBLE);
//                                play9();
//
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animator) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animator) {
//
//                            }
//                        })
//                        .playOn(card24);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(20000)
//                        .repeat(10)
//                        .playOn(card24);
//            }
//        },100);
//    }
}
