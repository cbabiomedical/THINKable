package com.example.thinkableproject.duckhunt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.thinkableproject.R;

public class Duck2 extends Duck1{

    Bitmap[] duck = new Bitmap[9];

    public Duck2(Context context) {
        super(context);
        duck[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck9);
        duck[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck10);
        duck[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck11);
        duck[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck12);
        duck[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck13);
        duck[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck14);
        duck[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck15);
        duck[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck16);
        duck[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.duck17);
        resetPosition();
    }

    @Override
    public Bitmap getBitMap() {
        return duck[duckFrame];
    }

    @Override
    public int getWidth() {
        return duck[0].getWidth();
    }

    @Override
    public int getHeight() {
        return duck[0].getHeight();
    }

    @Override
    public void resetPosition() {
        duckX = GameView.dWidth + random.nextInt(1500);
        duckY = random.nextInt(400);
        velocity = 5 + random.nextInt(19);
    }

}
