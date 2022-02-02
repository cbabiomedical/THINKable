package com.example.thinkableproject.duckhunt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.thinkableproject.R;

import java.util.Random;

public class Duck1 {

    Bitmap duck[] = new Bitmap[9];
    Random random;
    int duckX, duckY, velocity, duckFrame;

    public Duck1(Context context) {
        duck[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck0);
        duck[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck1);
        duck[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck2);
        duck[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck3);
        duck[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck4);
        duck[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck5);
        duck[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck6);
        duck[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck7);
        duck[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck8);
        random = new Random();
        duckFrame = 0;
        resetPosition();
    }

    public Bitmap getBitMap(){
        return duck[duckFrame];
    }

    public int getWidth(){
        return duck[0].getWidth();
    }

    public int getHeight(){
        return duck[0].getHeight();
    }

    public void resetPosition(){
        duckX = GameView.dWidth + random.nextInt(1200);
        duckY = random.nextInt(300);
        velocity = 14 + random.nextInt(17);
    }
}
