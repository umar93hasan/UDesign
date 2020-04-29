package com.numad19f.udesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * SQLLite database class for CRUD operations
 */
public class DatabaseManager {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    /**
     * Constructor
     * @param c context
     */
    public DatabaseManager(Context c)
    {
        context = c;
    }

    /**
     * start database connection
     * @return
     * @throws SQLException
     */
    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Insert values in database-link url and link name
     * @param image
     */
    public void insert(byte[] image, int likes) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.IMAGE, image);
        values.put(DatabaseHelper.LIKE_COUNT, likes);
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    /**
     * get values from database table
     * @return
     */
    public Cursor fetch() {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.IMAGE, DatabaseHelper.LIKE_COUNT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * update values in database
     * @param _id link id
     * @param image link name
     * @return
     */
    public int update(int _id, byte[] image, int likes) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.IMAGE, image);
        values.put(DatabaseHelper.LIKE_COUNT, likes);

        int update = database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper._ID + " = " + _id, null);
        return update;
    }

    /**
     * delete values from database
     * @param _id
     */
    public void delete(int _id)
    {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public Bitmap getImage(int i){

        String qu = "select image  from DESIGN_IMAGES where _id=" + i ;
        Cursor cur = database.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null;
    }
    public int getLikes(int i){

        String qu = "select like_count  from DESIGN_IMAGES where _id=" + i ;
        Cursor cur = database.rawQuery(qu, null);

        if (cur.moveToFirst()){
            int count = cur.getInt(0);
            cur.close();
            return count;
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return 0;
    }

    /**
     * close database connection
     */
    public void close()
    {
        dbHelper.close();
    }
}


