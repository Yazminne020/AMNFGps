package com.example.amfgps.tmpdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper dbHelper;

    private final Context context;

    private SQLiteDatabase db;

    public DBManager(Context c) {
        context = c;
    }

    public void open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteAll() {
        db.delete(DatabaseHelper.TABLE_NAME, null, null);
    }

    public Cursor retriveUser() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.PASSWD, DatabaseHelper.COMPANY};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public void insert(String name, String passwd, String company) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.PASSWD, passwd);
        contentValue.put(DatabaseHelper.COMPANY, company);
        db.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

}
