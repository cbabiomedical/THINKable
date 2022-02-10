package com.example.thinkableproject.puzzle.Class;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class SlicingImage {
    public static ArrayList<Bitmap> imageChunksStorageList = new ArrayList<>();
    public static Bitmap hint;

    public SlicingImage(){}


    public void splitImage(Drawable image, int rows, int columns, int imageSize, int pieaceSize) {
        imageChunksStorageList.clear();
        int chunkHeight, chunkWidth;
        BitmapDrawable drawable = (BitmapDrawable) image;
        Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);
        hint=scaledBitmap;
        chunkHeight = pieaceSize;
        chunkWidth = pieaceSize;

        int y = 0;
        int x = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Bitmap slicedImage;
                slicedImage = Bitmap.createBitmap(scaledBitmap, x, y, chunkWidth, chunkHeight);
                imageChunksStorageList.add(slicedImage);
                x += chunkWidth;
            }
            y +=+ chunkHeight;
            x = 0;
        }
    }
}
