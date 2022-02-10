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

public class GameActivity15 extends AppCompatActivity {

    private final int N = 4;
    Cards cards;
    private ImageButton[][] button;
    private final int[][] BUTTON_ID = {{R.id.b1500, R.id.b1501, R.id.b1502, R.id.b1503},
            {R.id.b1510, R.id.b1511, R.id.b1512, R.id.b1513},
            {R.id.b1520, R.id.b1521, R.id.b1522, R.id.b1523},
            {R.id.b1530, R.id.b1531, R.id.b1532, R.id.b1533}};
    private final int[] CARDS_ID = {R.drawable.card1500, R.drawable.card1501, R.drawable.card1502, R.drawable.card1503,
            R.drawable.card1504, R.drawable.card1505, R.drawable.card1506, R.drawable.card1507,
            R.drawable.card1508, R.drawable.card1509, R.drawable.card1510, R.drawable.card1511,
            R.drawable.card1512, R.drawable.card1513, R.drawable.card1514, R.drawable.card1515};

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
        dataBase.setPrefRef("PRESNAME15","PRESSCORE15");
        setContentView(R.layout.activity_game15);
        whatToShow = getIntent().getStringExtra("whatToShow");



        button = new ImageButton[N][N];
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTON_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");


        ImageButton newGameBtn =findViewById(R.id.bNewGame15);
        ImageButton backBtn =findViewById(R.id.bBackMenu15);
        soundBtn = findViewById(R.id.bSoundOffOn15);


        TextView titleTV = findViewById(R.id.gameTitle15);
        TextView textScoreTV =findViewById(R.id.tSScore15);
        scoreTV = findViewById(R.id.tScore15);
        TextView textRecordTV =findViewById(R.id.textBestScore15);
        recordTV =findViewById(R.id.tBestScore15);

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
        newGameBtn.setOnClickListener(navigateBtnsClickListener);
        backBtn.setOnClickListener(navigateBtnsClickListener);
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
            switch(v.getId()) {
                case R.id.bNewGame15:
                    Sound.menuClickSound.start();
                    newGame();
                    break;
                case R.id.bBackMenu15:
                    Sound.menuClickSound.start();
                    Sound.activitySwitchFlag=true;
                    backMenu();
                    break;
                case R.id.bSoundOffOn15:
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
            if(!check) {
                for (int i = 0; i < N; i++)
                    for (int j = 0; j < N; j++)
                        if (v.getId() == BUTTON_ID[i][j])
                            buttonFunction(i, j);
            }
            else
                Toast.makeText(GameActivity15.this, R.string.you_won_toast, Toast.LENGTH_SHORT).show();


        }
    };

    public void buttonFunction(int row, int column) {
        cards.moveCards(row, column);
        if(cards.resultMove()) {
            Sound.buttonGameSound.start();
            numOfSteps++;
            showGame();
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numOfSteps = 0;
        recordSteps=dataBase.getMaxScore(2,"PRESSCORE15");
        recordTV.setText(Integer.toString(recordSteps));
        showGame();
        check = false;
    }

    public void backMenu() {
        finish();
    }

    public void showGame() {
        scoreTV.setText(Integer.toString(numOfSteps));

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
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
        final Dialog dialog = new Dialog(GameActivity15.this);
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
                dataBase.setPrefRef("PRESNAME15","PRESSCORE15");
                numOfScores = dataBase.preferencesCounter.getInt("game15counter",0);
                if(numOfScores>=10) {
                    checkPlace = dataBase.checkIfScoreIsBest("PRESSCORE15", numOfSteps);
                    if (checkPlace!=(-1)) {
                        dataBase.changeValues(finishName.getText().toString(),numOfSteps,checkPlace);
                    }
                }
                else
                    dataBase.setValues(finishName.getText().toString(),numOfSteps,2);

                dialog.dismiss();

            }
        });
    }

    private void openHintDialog(){
        final Dialog dialog = new Dialog(GameActivity15.this);
        dialog.setContentView(R.layout.dialog_hint);
        ImageButton imageButton = dialog.findViewById(R.id.hintImage);
        imageButton.setImageBitmap(SlicingImage.hint);
        imageButton.setOnClickListener(new View.OnClickListener() {
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
