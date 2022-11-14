package com.example.logbookreportfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private static final String DATABASE_NAME  = "Images";

    private static final String DROP = "DROP TABLE IF EXISTS";

    //    Property of trips table
    private static final String TABLE_IMAGES = "images";
    private static final String ID_IMAGE = "image_id";
    private static final String URL_IMAGE = "url";

    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT)"
            , TABLE_IMAGES, ID_IMAGE, URL_IMAGE);

    public long insertImage(String url) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(URL_IMAGE, url);

        return database.insertOrThrow(TABLE_IMAGES, null, rowValues);
    }

    public long DeleteAllImage()
    {
        return database.delete(TABLE_IMAGES,null,null);
    }

    public ArrayList<Image> getImages() {
        Cursor cursor = database.query(TABLE_IMAGES, new String[] {ID_IMAGE, URL_IMAGE},
                null, null, null, null, ID_IMAGE);

        ArrayList<Image> results = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String url = cursor.getString(1);

            Image image = new Image();
            image.setId(id);
            image.setUrl(url);
            results.add(image);
            cursor.moveToNext();
        }
        return results;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1   );
        database = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        onCreate(db);
        Log.w(this.getClass().getName(), DATABASE_NAME + "DATABASE UPGRADE TO VERSION " + newVersion + "- OLD DATA LOST");
    }
}
