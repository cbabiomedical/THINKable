package com.example.thinkableproject.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavMeditationDB extends SQLiteOpenHelper {

    private static int DB_VERSIONMED = 1;
    private static String DATABASE_NAMEMED = "MeditationDB";
    private static String TABLE_NAMEMED = "favoriteTableMed";
    public static String KEY_IDMED = "id";
    public static String ITEM_TITLEMED = "itemTitle";
    public static String ITEM_IMAGEMED = "itemImage";
    public static String FAVORITE_STATUSMED = "fStatus";
    // dont forget write this spaces
    private static String CREATE_TABLEMED = "CREATE TABLE " + TABLE_NAMEMED + "("
            + KEY_IDMED + " TEXT," + ITEM_TITLEMED + " TEXT,"
            + ITEM_IMAGEMED + " TEXT," + FAVORITE_STATUSMED + " TEXT)";

    public FavMeditationDB(Context context) {
        super(context, DATABASE_NAMEMED, null, DB_VERSIONMED);
    }
    //create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLEMED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // create empty table
    public void insertEmptyMed() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // enter your value
        for (int x = 1; x < 11; x++) {
            cv.put(KEY_IDMED, x);
            cv.put(FAVORITE_STATUSMED, "0");

            db.insert(TABLE_NAMEMED, null, cv);
        }
    }

    // insert data into database
    public void insertIntoTheDatabaseMed(String item_title, String item_image, String id, String fav_status) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLEMED, item_title);
        cv.put(ITEM_IMAGEMED, item_image);
        cv.put(KEY_IDMED, id);
        cv.put(FAVORITE_STATUSMED, fav_status);
        db.insert(TABLE_NAMEMED, null, cv);
        Log.d("FavDB Status", item_title + ", favstatus - " + fav_status + " - . " + cv);
    }

    //    // read all data
    public Cursor read_all_data_med(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAMEMED + " WHERE " + KEY_IDMED + "=" + id + "";
        return db.rawQuery(sql, null, null);
    }

    //    // remove line from database
    public void remove_fav_med(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAMEMED + " SET  " + FAVORITE_STATUSMED + " ='0' WHERE " + KEY_IDMED + "=" + id + "";
        db.execSQL(sql);
        Log.d("remove", id.toString());

    }

//    // select all favorite list

    public Cursor select_all_favorite_list_med() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAMEMED + " WHERE " + FAVORITE_STATUSMED + " ='1'";
        return db.rawQuery(sql, null, null);
    }

}
