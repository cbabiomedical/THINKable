package com.example.thinkableproject.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.thinkableproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class SpaceShooter extends View {
    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    int x;
    boolean paused = false;
    Long startTime, endTime;
    OurSpaceship ourSpaceship;
    EnemySpaceship enemySpaceship;
    Random random;
    public static boolean isStarted;
    ArrayList<Shot> enemyShots, ourShots;
    FirebaseUser mUser;
    Explosion explosion;
    ArrayList<Explosion> explosions;
    boolean enemyShotAction = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        isStarted = true;
        startTime = System.currentTimeMillis();
        ourSpaceship = new OurSpaceship(context);
        enemySpaceship = new EnemySpaceship(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("Pt: " + points, 0, TEXT_SIZE, scorePaint);
        for (int i = life; i >= 1; i--) {
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if (life == 0) {
            SharedPreferences prefsTimeCon = context.getSharedPreferences("prefsTimeConWH", MODE_PRIVATE);
            int firstStartTimeCon = prefsTimeCon.getInt("firstStartTimeConWH", 0);
            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            isStarted = false;
            endTime = System.currentTimeMillis();
            Long seconds = (endTime - startTime) / 1000;
            Calendar now = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
            Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
            Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
            Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
            Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH) + 1;
            Format f = new SimpleDateFormat("EEEE");
            String str = f.format(new Date());

            SharedPreferences sh = context.getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

            x = sh.getInt("firstStartTimeConWH", 0);

// We can then use the data
            Log.d("A Count", String.valueOf(x));

            int y = x + 1;

            SharedPreferences prefsCount1 = context.getSharedPreferences("prefsTimeConWH", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefsCount1.edit();
            editor.putInt("firstStartTimeConWH", y);
            editor.apply();
            SharedPreferences sha = context.getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

            int x1 = sha.getInt("firstStartTimeConWH", 0);

            Log.d("A Count2", String.valueOf(x1));
            DatabaseReference referenceTime = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
            Log.d("ReferencePath", String.valueOf(referenceTime));
            referenceTime.setValue(seconds);

            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        // Move enemySpaceship
        enemySpaceship.ex += enemySpaceship.enemyVelocity;
        // If enemySpaceship collides with right wall, reverse enemyVelocity
        if (enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth) {
            enemySpaceship.enemyVelocity *= -1;
        }
        // If enemySpaceship collides with left wall, again reverse enemyVelocity
        if (enemySpaceship.ex <= 0) {
            enemySpaceship.enemyVelocity *= -1;
        }
        // Till enemyShotAction is false, enemy should fire shots from random travelled distance
        if (enemyShotAction == false) {
            if (enemySpaceship.ex >= 200 + random.nextInt(400)) {
                Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            }
            if (enemySpaceship.ex >= 400 + random.nextInt(800)) {
                Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            } else {
                Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            }
        }
        // Draw the enemy Spaceship
        canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex, enemySpaceship.ey, null);
        // Draw our spaceship between the left and right edge of the screen
        if (ourSpaceship.ox > screenWidth - ourSpaceship.getOurSpaceshipWidth()) {
            ourSpaceship.ox = screenWidth - ourSpaceship.getOurSpaceshipWidth();
        } else if (ourSpaceship.ox < 0) {
            ourSpaceship.ox = 0;
        }
        // Draw our Spaceship
        canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox, ourSpaceship.oy, null);
        // Draw the enemy shot downwards our spaceship and if it's being hit, decrement life, remove
        // the shot object from enemyShots ArrayList and show an explosion.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that enemy
        // can shot.
        for (int i = 0; i < enemyShots.size(); i++) {
            enemyShots.get(i).shy += 15;
            canvas.drawBitmap(enemyShots.get(i).getShot(), enemyShots.get(i).shx, enemyShots.get(i).shy, null);
            if ((enemyShots.get(i).shx >= ourSpaceship.ox)
                    && enemyShots.get(i).shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth()
                    && enemyShots.get(i).shy >= ourSpaceship.oy
                    && enemyShots.get(i).shy <= screenHeight) {
                life--;
                enemyShots.remove(i);
                explosion = new Explosion(context, ourSpaceship.ox, ourSpaceship.oy);
                explosions.add(explosion);
            } else if (enemyShots.get(i).shy >= screenHeight) {
                enemyShots.remove(i);
            }
            if (enemyShots.size() < 1) {
                enemyShotAction = false;
            }
        }
        // Draw our spaceship shots towards the enemy. If there is a collision between our shot and enemy
        // spaceship, increment points, remove the shot from ourShots and create a new Explosion object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for (int i = 0; i < ourShots.size(); i++) {
            ourShots.get(i).shy -= 15;
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx, ourShots.get(i).shy, null);
            if ((ourShots.get(i).shx >= enemySpaceship.ex)
                    && ourShots.get(i).shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth()
                    && ourShots.get(i).shy <= enemySpaceship.getEnemySpaceshipWidth()
                    && ourShots.get(i).shy >= enemySpaceship.ey) {
                points++;
                ourShots.remove(i);
                explosion = new Explosion(context, enemySpaceship.ex, enemySpaceship.ey);
                explosions.add(explosion);
            } else if (ourShots.get(i).shy <= 0) {
                ourShots.remove(i);
            }
        }
        // Do the explosion
        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 8) {
                explosions.remove(i);
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if (!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        // When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        // create a new Shot.
        // This way we restrict ourselves of making just one shot at a time, on the screen.
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (ourShots.size() < 1) {
                Shot ourShot = new Shot(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() / 2, ourSpaceship.oy);
                ourShots.add(ourShot);
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ourSpaceship.ox = touchX;
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ourSpaceship.ox = touchX;
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true;
    }
}
