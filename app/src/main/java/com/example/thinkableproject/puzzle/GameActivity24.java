package com.example.thinkableproject.puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.Cards;
import com.example.thinkableproject.puzzle.Class.DataBase;
import com.example.thinkableproject.puzzle.Class.SlicingImage;
import com.example.thinkableproject.puzzle.Class.Sound;

public class GameActivity24 extends AppCompatActivity {


    private final int N = 5;
    Cards cards;
    private ImageButton[][] button;
    private final int[][] BUT_ID = {{R.id.b2400, R.id.b2401, R.id.b2402, R.id.b2403, R.id.b2404},
            {R.id.b2410, R.id.b2411, R.id.b2412, R.id.b2413, R.id.b2414},
            {R.id.b2420, R.id.b2421, R.id.b2422, R.id.b2423, R.id.b2424},
            {R.id.b2430, R.id.b2431, R.id.b2432, R.id.b2433, R.id.b2434},
            {R.id.b2440, R.id.b2441, R.id.b2442, R.id.b2443, R.id.b2444}};
    private final int[] CARDS_ID = {R.drawable.card2400, R.drawable.card2401, R.drawable.card2402, R.drawable.card2403, R.drawable.card2404,
            R.drawable.card2405, R.drawable.card2406, R.drawable.card2407, R.drawable.card2408, R.drawable.card2409,
            R.drawable.card2410, R.drawable.card2411, R.drawable.card2412, R.drawable.card2413, R.drawable.card2414,
            R.drawable.card2415, R.drawable.card2416, R.drawable.card2417, R.drawable.card2418, R.drawable.card2419,
            R.drawable.card2420, R.drawable.card2421, R.drawable.card2422, R.drawable.card2423, R.drawable.card2424};

    private TextView scoreTV;
    private int numOfSteps;
    private TextView recordTV;
    private int recordSteps;
    private ImageButton soundBtn;
    private boolean check;
    private String whatToShow;

    DataBase dataBase = new DataBase(this);
    Sound sound=new Sound();

    @Override
    protected void onStart() {
        super.onStart();
        if (!Sound.gameMusic.isPlaying())
            sound.switchMusic(Sound.gameMusic,Sound.backgroundMusic);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!Sound.activitySwitchFlag)
            Sound.gameMusic.pause();
        else
            sound.switchMusic(Sound.backgroundMusic,Sound.gameMusic);
        Sound.activitySwitchFlag = false;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Sound.activitySwitchFlag=true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase.setPrefRef("PRESNAME24","PRESSCORE24");
        setContentView(R.layout.activity_game24);
        whatToShow = getIntent().getStringExtra("whatToShow");


        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUT_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");

        ImageButton newGameBtn = findViewById(R.id.bNewGame24);
        ImageButton backBtn = findViewById(R.id.bBackMenu24);
        soundBtn = findViewById(R.id.bSoundOffOn24);


        TextView titleTV = findViewById(R.id.gameTitle24);
        TextView textScoreTV = findViewById(R.id.tSScore24);
        scoreTV = findViewById(R.id.tScore24);
        TextView textRecordTV = findViewById(R.id.tBestSScore24);
        recordTV =findViewById(R.id.tBestScore24);

        titleTV.setTypeface(digitalFont);
        textScoreTV.setTypeface(digitalFont);
        scoreTV.setTypeface(digitalFont);
        textRecordTV.setTypeface(digitalFont);
        recordTV.setTypeface(digitalFont);
        Button hintBtn = findViewById(R.id.hint);

        AnimationDrawable animationDrawable = (AnimationDrawable)hintBtn.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


        hintBtn.setOnClickListener(navigateBtnsClickListener);
        backBtn.setOnClickListener(navigateBtnsClickListener);
        newGameBtn.setOnClickListener(navigateBtnsClickListener);
        soundBtn.setOnClickListener(navigateBtnsClickListener);

        if(whatToShow.equals("Zoo"))
            hintBtn.setVisibility(View.VISIBLE);
        else
            hintBtn.setVisibility(View.INVISIBLE);


        if(Sound.check)
            soundBtn.setImageResource(R.drawable.soundon);


        cards = new Cards(N, N);
        newGame();
    }
    View.OnClickListener navigateBtnsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bNewGame24:
                    Sound.menuClickSound.start();
                    newGame();
                    break;
                case R.id.bBackMenu24:
                    Sound.activitySwitchFlag=true;
                    Sound.menuClickSound.start();
                    backMenu();
                    break;
                case R.id.bSoundOffOn24:
                    soundOffOn();
                    Sound.menuClickSound.start();
                    break;
                case R.id.hint:
                    openHintDialog();
                    break;

                default:
                    break;
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!check) {
                for (int i = 0; i < N; i++)
                    for (int j = 0; j < N; j++)
                        if (v.getId() == BUT_ID[i][j])
                            butFunc(i, j);
            }
            else
                Toast.makeText(GameActivity24.this, R.string.you_won_24, Toast.LENGTH_SHORT).show();

        }
    };

    public void butFunc(int row, int columb) {
        cards.moveCards(row, columb);
        if (cards.resultMove()) {
            Sound.buttonGameSound.start();
            numOfSteps++;
            showGame();
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numOfSteps = 0;
        recordSteps=dataBase.getMaxScore(3,"PRESSCORE24");
        recordTV.setText(Integer.toString(recordSteps));
        showGame();
        check = false;
    }


    public void backMenu() {
        finish();
    }


    public void showGame() {
        scoreTV.setText(Integer.toString(numOfSteps));

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if(whatToShow.equals("Zoo")){
                    if (cards.getValueBoard(i, j) != 0)
                        button[i][j].setImageBitmap(SlicingImage.imageChunksStorageList.get(cards.getValueBoard(i, j)));
                    else
                        button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);}
                else
                    button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);

    }

    public void checkFinish(){
        if(cards.finished(N, N)){
            showGame();
            Sound.winningSound.start();
            openDialog();
            if ((numOfSteps < recordSteps) || (recordSteps == 0)) {
                recordTV.setText(Integer.toString(numOfSteps));
            }
            check = true;
        }
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(GameActivity24.this);
        dialog.setContentView(R.layout.dialog_finished);


        Button finishButton = dialog.findViewById(R.id.finishButton);
        final EditText finishName = dialog.findViewById(R.id.finishName);
        TextView finishSteps = dialog.findViewById(R.id.finishSteps);
        finishSteps.setText(numOfSteps +" "+getString(R.string.finished_steps));
        dialog.show();
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfScores,checkPlace;
                dataBase.setPrefRef("PRESNAME24","PRESSCORE24");
                numOfScores = dataBase.preferencesCounter.getInt("game24counter",0);
                if(numOfScores>=10) {
                    checkPlace = dataBase.checkIfScoreIsBest("PRESSCORE24", numOfSteps);
                    if (checkPlace!=(-1)) {
                        dataBase.changeValues(finishName.getText().toString(),numOfSteps,checkPlace);
                    }
                }
                else
                    dataBase.setValues(finishName.getText().toString(),numOfSteps,3);

                dialog.dismiss();


            }
        });
    }
    private void openHintDialog(){
        final Dialog dialog = new Dialog(GameActivity24.this);
        dialog.setContentView(R.layout.dialog_hint);
        ImageButton hintImageButton = dialog.findViewById(R.id.hintImage);
        hintImageButton.setImageBitmap(SlicingImage.hint);
        hintImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
    public void soundOffOn(){
        if(Sound.check)
        {
            Sound.check=false;
            soundBtn.setImageResource(R.drawable.soundoff);
        }
        else
        {
            Sound.check=true;
            soundBtn.setImageResource(R.drawable.soundon);
        }
        sound.setSounds();
        sound.setMusic();
    }


}
