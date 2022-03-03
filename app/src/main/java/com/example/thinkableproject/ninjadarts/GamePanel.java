package com.example.thinkableproject.ninjadarts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.thinkableproject.GameActivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Player user;
    private Point userPoint;
    private FruitManager fruitManager;
    FirebaseFirestore database;
    FirebaseUser mUser;
    User pUser;
    int updatedCoins;
    private Context context;
    int a;
    int a1;
    int g;
    Dialog dialog;
    long seconds;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();
    long startTime, endTime;
    public static boolean isStarted = false;


    private int highScore = Constants.PREF.getInt("key", 0);

    private boolean gameOver = false;
//    private Context context;

    public GamePanel(Context context) {

        super(context);
        startTime = System.currentTimeMillis();
        isStarted = true;
        Log.d("StartTime", String.valueOf(startTime));
        dialog = new Dialog(context);
        SharedPreferences prefsCount1 = context.getSharedPreferences("prefsCountNinja", MODE_PRIVATE);
        int firstStartCount = prefsCount1.getInt("firstStartCountNinja", 0);


        getHolder().addCallback(this);
//

        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        //Instantiate player
        user = new Player(new Rect(100, 100, 200, 200), Color.argb(0, 0, 0, 0));

        //Instantiate location of the player
        userPoint = new Point(150, 150);
        user.update(userPoint);

        //Instantiate the fruit-managing class
        fruitManager = new FruitManager(200, 200, 325, Color.argb(0, 255, 255, 255));
        Log.d("XX", String.valueOf(fruitManager.getScore()));
        setFocusable(true);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        while (retry) {

            try {

                thread.setRunning(false);
                thread.join();

            } catch (Exception e) {

                e.printStackTrace();

            }

            retry = false;


        }

    }

    //Creates a new fruitManager and resets the location of the user
    public void resetGame() {

        userPoint = new Point(150, 150);
        user.update(userPoint);

        fruitManager = new FruitManager(200, 200, 325, Color.argb(0, 255, 255, 255));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            //User has tapped
            case MotionEvent.ACTION_DOWN:

                //Reset the game if ended
                if (gameOver) {

                    resetGame();
                    gameOver = false;

                }

                break;

            //User has moved their finger, update the location of the rect
            case MotionEvent.ACTION_MOVE:

                userPoint.set((int) event.getX(), (int) event.getY());

        }

        return true;

    }

    public void update() throws InterruptedException {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);


        //Game is continuing
        if (!gameOver) {

            //Move the user to the new point
            user.update(userPoint);

            //Update the fruitManager
            boolean x = fruitManager.update();

            //Check if three fruits have been missed


            if (x) {
                gameOver = true;
                isStarted = false;
                Log.d("X Over", "True");
                thread.sleep(200);
                Intent intent = new Intent(getContext(), NinjaDartLineChart.class);
                getContext().startActivity(intent);

//                openPopUpIntervention();


                Log.d("CHECK", String.valueOf(fruitManager.getScore()));
                database.collection("users")
                        .document(mUser.getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pUser = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                        Log.d("Current Coins", String.valueOf(pUser.getCoins()));
                        Log.d("High Score Inside", String.valueOf(fruitManager.getScore()));
                        updatedCoins = (int) (pUser.getCoins() + fruitManager.getScore());
                        mUser = FirebaseAuth.getInstance().getCurrentUser();

                        endTime = System.currentTimeMillis();
                        Log.d("End", String.valueOf(endTime));
                        seconds = (endTime - startTime) / 1000;
                        Log.d("ASec", String.valueOf(seconds));

                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Sec", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
                        myEdit.putInt("sec", (int) seconds);
                        Log.d("MyEdit", String.valueOf(myEdit));


// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
                        myEdit.commit();


                        mUser = FirebaseAuth.getInstance().getCurrentUser();

//


                        Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                        database = FirebaseFirestore.getInstance();
                        database.collection("users").document(mUser.getUid())
                                .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                        Toast.makeText( "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
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


            }

            boolean y = fruitManager.collisionDetection(user);

            //Check if bomb has been hit
            if (y) {

                gameOver = true;
                isStarted = false;
                thread.sleep(200);
                Intent intent = new Intent(getContext(), NinjaDartLineChart.class);
                getContext().startActivity(intent);
//                openPopUpIntervention();
                database = FirebaseFirestore.getInstance();

                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pUser = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                        Log.d("Current Coins", String.valueOf(pUser.getCoins()));
                        Log.d("High Score Inside", String.valueOf(fruitManager.getScore()));
                        updatedCoins = (int) (pUser.getCoins() + fruitManager.getScore());
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                        database.collection("users").document(mUser.getUid())
                                .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                        Toast.makeText( "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
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
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                endTime = System.currentTimeMillis();
                Log.d("End", String.valueOf(endTime));
                seconds = (endTime - startTime) / 1000;
                Log.d("ASec", String.valueOf(seconds));


            }
            Log.d("CHECKOUT", String.valueOf(fruitManager.getScore()));

        } else {

        }


    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

//        Log.d("Seconds Ninja", String.valueOf(seconds));

        canvas.drawColor(Color.rgb(232, 200, 179));

        user.draw(canvas);

        fruitManager.draw(canvas);

        //Set gameover screen
        if (gameOver) {
            isStarted = false;

//            Intent    intent = new Intent(getContext(), GameActivity.class);
//            context.startActivity(intent);


            Log.d("STOP", String.valueOf(fruitManager.getScore()));
            database = FirebaseFirestore.getInstance();
            mUser = FirebaseAuth.getInstance().getCurrentUser();


            BitmapFactory bf = new BitmapFactory();

            Bitmap gOverImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover);

//            long seconds=()

            if (highScore < fruitManager.getScore()) {

                SharedPreferences.Editor editor = Constants.PREF.edit();
                editor.putInt("key", fruitManager.getScore());
                editor.commit();

                highScore = fruitManager.getScore();


            }

            canvas.drawColor(Color.BLACK);

            Paint p = new Paint();
            p.setColor(Color.GREEN);
            p.setTextSize(150);

            Rect img = new Rect(Constants.SCREEN_WIDTH / 2 - 250, 0, Constants.SCREEN_WIDTH / 2 + 250, 500);
            canvas.drawBitmap(gOverImg, null, img, null);

            Rect bounds = new Rect();

            String text1 = "Game Over! Tap to Restart";
            p.getTextBounds(text1, 0, text1.length(), bounds);
            int x1 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y1 = (canvas.getHeight() / 2) - (bounds.height() / 2);
            Log.d("SecNin", String.valueOf(seconds));
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Sec", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
            myEdit.putInt("sec", (int) seconds);
            myEdit.commit();
            String text2 = "Score: " + fruitManager.getScore();
            // Storing data into SharedPreferences
            SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("Score", MODE_PRIVATE);
            SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
            myEdit1.putString("name", String.valueOf(fruitManager.getScore()));
            myEdit1.commit();

            Log.d("Print", text2);

            p.getTextBounds(text2, 0, text2.length(), bounds);
            int x2 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y2 = (canvas.getHeight() / 2) - (bounds.height() / 2);

            String text3 = "High Score: " + highScore;
            SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("HighScore", MODE_PRIVATE);
            SharedPreferences.Editor myEdit2 = sharedPreferences2.edit();
            myEdit2.putString("highscore", String.valueOf(highScore));
            myEdit2.commit();

            p.getTextBounds(text3, 0, text3.length(), bounds);
            int x3 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y3 = (canvas.getHeight() / 2) - (bounds.height() / 2);


            canvas.drawText(text1, x1, y1, p);
            canvas.drawText(text2, x2, y2 + 200, p);
            canvas.drawText(text3, x3, y3 + 400, p);


        }


    }

    private void openPopUpIntervention() {
//        Button ok;
//        LineChart lineChart;
//
//        dialog.setContentView(R.layout.game_intervention_popup);
//        ok = (Button) dialog.findViewById(R.id.ok);
//        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
//
//        lineEntries = new ArrayList();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Ninja Dart");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Log.d("XVAL", dataSnapshot.getKey());
//                    float xxVal = (Float.parseFloat(dataSnapshot.getKey()));
//
//                    Log.d("XArrayList", String.valueOf(xVal));
//                    float yyVal = (Float.parseFloat(String.valueOf((Long) dataSnapshot.getValue())));
//                    Log.d("YVAL", String.valueOf(yyVal));
//                    Log.d("YArrayList", String.valueOf(yVal));
//                    lineEntries.add(new Entry(xxVal, yyVal));
//
//                    lineDataSet = new LineDataSet(lineEntries, "Memory Progress");
//                    lineData = new LineData(lineDataSet);
//                    lineChart.setData(lineData);
//
//                    lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                    lineDataSet.setValueTextColor(Color.WHITE);
//                    lineDataSet.setValueTextSize(10f);
//
//                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                    lineChart.setBorderColor(Color.TRANSPARENT);
//                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//                    lineChart.getAxisLeft().setDrawGridLines(false);
//                    lineChart.getXAxis().setDrawGridLines(false);
//                    lineChart.getAxisRight().setDrawGridLines(false);
//                    lineChart.getXAxis().setTextColor(R.color.white);
//                    lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
//                    lineChart.getDescription().setTextColor(R.color.white);
//                    lineChart.invalidate();
//                    lineChart.refreshDrawableState();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
    }
}


