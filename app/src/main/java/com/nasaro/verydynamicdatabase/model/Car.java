package com.nasaro.verydynamicdatabase.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Car {
    public int id;
    public String make;
    public String model;
    public int year;
    public long userId;

    public static String tableName = "cars";

    public static String create = "CREATE TABLE " + tableName + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "make TEXT, " +
            "model TEXT, " +
            "year INTEGER, " +
            "user_id INTEGER, " +
            "FOREIGN KEY (user_id) REFERENCES " + User.tableName + "(id))";

    public Car() {
    }

    public Car(String make, String model, int year, long userId) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.userId = userId;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("make", make);
        values.put("model", model);
        values.put("year", year);
        values.put("user_id", userId);
        return values;
    }

    public static Car fromCursor(Cursor cursor) {
        Car car = new Car();
        car.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        car.make = cursor.getString(cursor.getColumnIndexOrThrow("make"));
        car.model = cursor.getString(cursor.getColumnIndexOrThrow("model"));
        car.year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        car.userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        return car;
    }
} 