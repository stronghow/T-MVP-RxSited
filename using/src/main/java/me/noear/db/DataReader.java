package me.noear.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yuety on 15/3/13.
 */
public class DataReader {
    private Cursor cursor;
    private SQLiteDatabase db;

    public DataReader(SQLiteDatabase db, Cursor cursor)
    {
        this.cursor = cursor;
        this.db     = db;
    }

    public boolean read()
    {
        return cursor.moveToNext();
    }

    public long getLong(String colName)
    {
        return cursor.getLong(cursor.getColumnIndex(colName));
    }

    public int getInt(String colName)
    {
        return cursor.getInt(cursor.getColumnIndex(colName));
    }

    public String getString(String colName)
    {
        return cursor.getString(cursor.getColumnIndex(colName));
    }

    public void close()
    {
        cursor.close();
        db.close();
    }
}
