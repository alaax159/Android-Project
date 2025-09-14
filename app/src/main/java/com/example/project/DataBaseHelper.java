package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Keep constructor signature as you had it
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        // You can pass "BookDB" and 2 from the Activity to force a rebuild
        super(context, name, factory, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT," +
                "isbn TEXT UNIQUE," +
                "category TEXT," +
                "availability INTEGER DEFAULT 1," +
                "cover_url TEXT," +
                "publication_year INTEGER)");

        // Students Table
        db.execSQL("CREATE TABLE IF NOT EXISTS Students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "university_id TEXT UNIQUE NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password_hash TEXT NOT NULL," +
                "department TEXT," +
                "level TEXT)");

        // Reservations Table
        db.execSQL("CREATE TABLE IF NOT EXISTS Reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER NOT NULL," +
                "book_id INTEGER NOT NULL," +
                "reservation_date TEXT NOT NULL," +
                "due_date TEXT," +
                "status TEXT," +
                "FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE," +
                "FOREIGN KEY (book_id) REFERENCES Books(id) ON DELETE CASCADE)");

        // Reading_List Table
        db.execSQL("CREATE TABLE IF NOT EXISTS Reading_List (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER NOT NULL," +
                "book_id INTEGER NOT NULL," +
                "added_date TEXT," +
                "FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE," +
                "FOREIGN KEY (book_id) REFERENCES Books(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple reset strategy (drop & recreate)
        db.execSQL("DROP TABLE IF EXISTS Books");
        db.execSQL("DROP TABLE IF EXISTS Students");
        db.execSQL("DROP TABLE IF EXISTS Reservations");
        db.execSQL("DROP TABLE IF EXISTS Reading_List");
        onCreate(db);
    }
}

