package com.example.thinkableproject.puzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.example.thinkableproject.puzzle.Class.Cards;
import com.example.thinkableproject.puzzle.Class.DataBase;
import com.example.thinkableproject.puzzle.Class.SlicingImage;
import com.example.thinkableproject.puzzle.Class.Sound;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameActivity9 extends AppCompatActivity {


    private final int N = 3;
    Cards cards;
    int points;
    int a;
    User user;
    FirebaseFirestore database;
    int updatedCoins;
    FirebaseUser mUser;
    public static boolean isStarted=false;
    private ImageButton[][] button;
    private final int[][] BUTTON_ID = {{R.id.b900, R.id.b901, R.id.b902},
            {R.id.b910, R.id.b911, R.id.b912},
            {R.id.b920, R.id.b921, R.id.b922}};
    private final int[] CARDS_ID = {R.drawable.card900, R.drawable.card901, R.drawable.card902,
            R.drawable.card903, R.drawable.card904, R.drawable.card905,
            R.drawable.card906, R.drawable.card907, R.drawable.card908};

    private String whatToShow;
    private TextView scoreTV;
    private int numOfSteps;
    private TextView recordTV;
    private int recordSteps;
    private ImageButton soundBtn;
    private boolean check;

    DataBase dataBase = new DataBase(this);
    Sound sound = new Sound();

    @Override
    protected void onStart() {
        super.onStart();
        if (!Sound.gameMusic.isPlaying())
            sound.switchMusic(Sound.gameMusic, Sound.backgroundMusic);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Sound.activitySwitchFlag)
            Sound.gameMusic.pause();
        else
            sound.switchMusic(Sound.backgroundMusic, Sound.gameMusic);
        Sound.activitySwitchFlag = false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Sound.activitySwitchFlag = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase.setPrefRef("PRESNAME9", "PRESSCORE9");
        setContentView(R.layout.activity_game9);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseFirestore.getInstance();
        isStarted=true;

        whatToShow = getIntent().getStringExtra("whatToShow");
        SharedPreferences prefsCountPuz = getSharedPreferences("prefsCountPuz", MODE_PRIVATE);
        int firstStartCountPuz = prefsCountPuz.getInt("firstStartCountPuz", 0);


        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                button[i][j] = findViewById(BUTTON_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
//        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");

        soundBtn = findViewById(R.id.bSoundOffOn9);
        ImageButton newGameBtn = findViewById(R.id.bNewGame9);
        ImageButton backBtn = findViewById(R.id.bBackMenu9);

        TextView titleTV = findViewById(R.id.gameTitle9);
        TextView textScoreTV = findViewById(R.id.tSScore9);
        scoreTV = findViewById(R.id.tScore9);
        TextView textRecordTV = findViewById(R.id.tBestSScore9);
        recordTV = findViewById(R.id.tBestScore9);
        Button hintBtn = findViewById(R.id.hint);

        AnimationDrawable animationDrawable = (AnimationDrawable) hintBtn.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


//        titleTV.setTypeface(digitalFont);
//        textScoreTV.setTypeface(digitalFont);
//        scoreTV.setTypeface(digitalFont);
//        textRecordTV.setTypeface(digitalFont);
//        recordTV.setTypeface(digitalFont);

        hintBtn.setOnClickListener(navigateBtnsClickListener);
        newGameBtn.setOnClickListener(navigateBtnsClickListener);
        backBtn.setOnClickListener(navigateBtnsClickListener);
        soundBtn.setOnClickListener(navigateBtnsClickListener);

        if (whatToShow.equals("Zoo"))
            hintBtn.setVisibility(View.VISIBLE);
        else
            hintBtn.setVisibility(View.INVISIBLE);

        if (Sound.check)
            soundBtn.setImageResource(R.drawable.soundon);

        cards = new Cards(N, N);

        newGame();

    }

    View.OnClickListener navigateBtnsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bNewGame9:
                    Sound.menuClickSound.start();
                    newGame();
                    break;
                case R.id.bBackMenu9:
                    Sound.activitySwitchFlag = true;
                    Sound.menuClickSound.start();
                    backMenu();
                    break;
                case R.id.bSoundOffOn9:
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
                        if (v.getId() == BUTTON_ID[i][j])
                            buttonFunction(i, j);
            } else
                Toast.makeText(GameActivity9.this, R.string.you_won_toast, Toast.LENGTH_SHORT).show();

        }
    };

    public void buttonFunction(int row, int column) {
        cards.moveCards(row, column); // re arrange the board when clicking button
        if (cards.resultMove()) { // if the move is done correct
            Sound.buttonGameSound.start();
            numOfSteps++;
            showGame(); // place the new images on the buttons
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numOfSteps = 0;
        recordSteps = dataBase.getMaxScore(1, "PRESSCORE9");
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
            for (int j = 0; j < N; j++) {
                if (whatToShow.equals("Zoo")) {
                    if (cards.getValueBoard(i, j) != 0)
                        button[i][j].setImageBitmap(SlicingImage.imageChunksStorageList.get(cards.getValueBoard(i, j)));
                    else
                        button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);
                } else
                    button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);

            }

    }

    public void checkFinish() {
        if (cards.finished(N, N)) {
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
        isStarted=false;
        SharedPreferences sh = getSharedPreferences("prefsCountPuz", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        a = sh.getInt("firstStartCountPuz", 0);

// We can then use the data
        Log.d("Count 1", String.valueOf(a));

        int b = a + 1;
        Log.d("B Val", String.valueOf(b));

        SharedPreferences prefsCount1 = getSharedPreferences("prefsCountPuz", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartCountPuz", b);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsCountPuz", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int a1 = sha.getInt("firstStartCountPuz", 0);

        Log.d("Count 2", String.valueOf(a1));

        final Dialog dialog = new Dialog(GameActivity9.this);
        dialog.setContentView(R.layout.dialog_finished);
        Button finishButton = dialog.findViewById(R.id.finishButton);
        final EditText finishName = dialog.findViewById(R.id.finishName);
        TextView finishSteps = dialog.findViewById(R.id.finishSteps);
        finishSteps.setText(numOfSteps + " " + getString(R.string.finished_steps));
        if (numOfSteps <= 25) {
            points = 50;
        } else if (numOfSteps <= 40) {
            points = 30;
        } else if (numOfSteps <= 60) {
            points = 20;
        } else if (points <= 70) {
            points = 10;
        } else {
            points = 5;
        }

        database.collection("users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(points));
                updatedCoins = (int) (user.getCoins() + points);
                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                database.collection("users").document(mUser.getUid())
                        .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ColorPatternGame.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Puzzles").child(String.valueOf(a));
//        reference.setValue(points);

        dialog.show();
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfScores, checkPlace;
                dataBase.setPrefRef("PRESNAME9", "PRESSCORE9");
                numOfScores = dataBase.preferencesCounter.getInt("game9counter", 0);
                if (numOfScores >= 10) {
                    checkPlace = dataBase.checkIfScoreIsBest("PRESSCORE9", numOfSteps);
                    if (checkPlace != (-1)) {
                        dataBase.changeValues(finishName.getText().toString(), numOfSteps, checkPlace);
                    }
                } else
                    dataBase.setValues(finishName.getText().toString(), numOfSteps, 1);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void openHintDialog() {
        final Dialog dialog = new Dialog(GameActivity9.this);
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


}

