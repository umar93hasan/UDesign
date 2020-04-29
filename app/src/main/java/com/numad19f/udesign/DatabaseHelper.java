package com.numad19f.udesign;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "DESIGN_IMAGES";
    public static final String _ID = "_id";
    public static final String IMAGE = "image";
    public static final String LIKE_COUNT = "like_count";
    public static final String DB_NAME = "IMG.DB";
    public static final int DB_VERSION = 1;
    //create table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMAGE + " BLOB NOT NULL, " + LIKE_COUNT + " INTEGER DEFAULT 0);";

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}


