package com.example.thinkableproject.ninjadarts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private int highScore = Constants.PREF.getInt("key", 0);

    private boolean gameOver = false;

    public GamePanel(Context context) {

        super(context);

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

    public void update() {

        //Game is continuing
        if (!gameOver) {

            //Move the user to the new point
            user.update(userPoint);

            //Update the fruitManager
            boolean x = fruitManager.update();

            //Check if three fruits have been missed
            if (x) {

                gameOver = true;

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
                mUser=FirebaseAuth.getInstance().getCurrentUser();

//                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Ninja Dart").child()

            }

            boolean y = fruitManager.collisionDetection(user);

            //Check if bomb has been hit
            if (y) {

                gameOver = true;


            }
            Log.d("CHECKOUT", String.valueOf(fruitManager.getScore()));


        } else {

        }


    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        canvas.drawColor(Color.rgb(232, 200, 179));

        user.draw(canvas);

        fruitManager.draw(canvas);

        //Set gameover screen
        if (gameOver) {
//            Log.d("Overc",String.valueOf(fruitManager.getScore()));
            database = FirebaseFirestore.getInstance();
            mUser = FirebaseAuth.getInstance().getCurrentUser();


            BitmapFactory bf = new BitmapFactory();

            Bitmap gOverImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover);

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

            String text2 = "Score: " + fruitManager.getScore();
            Log.d("Print", text2);



            p.getTextBounds(text2, 0, text2.length(), bounds);
            int x2 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y2 = (canvas.getHeight() / 2) - (bounds.height() / 2);

            String text3 = "High Score: " + highScore;
            p.getTextBounds(text3, 0, text3.length(), bounds);
            int x3 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y3 = (canvas.getHeight() / 2) - (bounds.height() / 2);


            canvas.drawText(text1, x1, y1, p);
            canvas.drawText(text2, x2, y2 + 200, p);
            canvas.drawText(text3, x3, y3 + 400, p);

        }

    }
}
