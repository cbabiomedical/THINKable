package com.example.thinkableproject.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavMusicDB extends SQLiteOpenHelper {

    private static int DB_VERSIONMUS = 1;
    private static String DATABASE_NAMEMUS = "MusicDB";
    private static String TABLE_NAMEMUS = "favoriteTable";
    public static String KEY_IDMUS = "id";
    public static String ITEM_TITLEMUS = "itemTitle";
    public static String ITEM_IMAGEMUS = "itemImage";
    public static String FAVORITE_STATUSMUS = "fStatus";
    // dont forget write this spaces
    private static String CREATE_TABLEMUS = "CREATE TABLE " + TABLE_NAMEMUS + "("
            + KEY_IDMUS + " TEXT," + ITEM_TITLEMUS + " TEXT,"
            + ITEM_IMAGEMUS + " TEXT," + FAVORITE_STATUSMUS + " TEXT)";

    public FavMusicDB(Context context) {
        super(context, DATABASE_NAMEMUS, null, DB_VERSIONMUS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLEMUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // create empty table
    public void insertEmptyMusic() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // enter your value
        for (int x = 1; x < 11; x++) {
            cv.put(KEY_IDMUS, x);
            cv.put(FAVORITE_STATUSMUS, "0");

            db.insert(TABLE_NAMEMUS, null, cv);
        }
    }

    // insert data into database
    public void insertIntoTheDatabaseMusic(String item_title, String item_image, String id, String fav_status) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLEMUS, item_title);
        cv.put(ITEM_IMAGEMUS, item_image);
        cv.put(KEY_IDMUS, id);
        cv.put(FAVORITE_STATUSMUS, fav_status);
        db.insert(TABLE_NAMEMUS, null, cv);
        Log.d("FavDB Status", item_title + ", favstatus - " + fav_status + " - . " + cv);
    }

    // read all data
    public Cursor read_all_data_mus(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAMEMUS + " where " + KEY_IDMUS + "=" + id + "";
        return db.rawQuery(sql, null, null);
    }

    // remove line from database
    public void remove_fav_mus(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAMEMUS + " SET  " + FAVORITE_STATUSMUS + " ='0' WHERE " + KEY_IDMUS + "=" + id + "";
        db.execSQL(sql);
        Log.d("remove", id.toString());

    }

    // select all favorite list

    public Cursor select_all_favorite_list_mus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAMEMUS + " WHERE " + FAVORITE_STATUSMUS + " ='1'";
        return db.rawQuery(sql, null, null);
    }


}
