package com.nasaro.verydynamicdatabase.model;

import android.content.ContentValues;
import android.database.Cursor;

public class User {
    public int id;
    public String name;
    public String email;

    public static String tableName = "users";

    public static String create = "CREATE TABLE " + tableName + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "email TEXT)";

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        return values;
    }

    public static User fromCursor(Cursor cursor) {
        User user = new User();
        user.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        user.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        user.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        return user;
    }
} 