package com.nasaro.verydynamicdatabase.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nasaro.verydynamicdatabase.model.Car;
import com.nasaro.verydynamicdatabase.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context ctx) {
        super(ctx, "database.db", null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.create);
        db.execSQL(Car.create);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.tableName);
        db.execSQL("DROP TABLE IF EXISTS " + Car.tableName);
        onCreate(db);
    }

    public long insert(String table, ContentValues values) {
        return getWritableDatabase().insert(table, null, values);
    }

    public Cursor query(String table, String selection, String[] args) {
        return getReadableDatabase().query(table, null, selection, args, null, null, null);
    }

    public Cursor getAll(String table) {
        return getReadableDatabase().query(table, null, null, null, null, null, null);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getWritableDatabase().update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return getWritableDatabase().delete(table, whereClause, whereArgs);
    }
} 