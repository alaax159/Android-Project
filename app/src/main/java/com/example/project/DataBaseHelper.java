package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database name LibraryDB and version 3
    // Keep constructor signature as you had it
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

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
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        onCreate(db);
    }
    public void registerStudent(String universityId, String firstName, String lastName, String email, String passwordHash, String department, String level){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("university_id", universityId);
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("email", email);
        String hashedPassword = CaeserCipher.encrypt(passwordHash, 5);
        contentValues.put("password_hash", hashedPassword);
        contentValues.put("department", department);
        contentValues.put("level", level);
        db.insert("Students", null, contentValues);

    }
    public boolean checkUniversityId(String universityId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Students WHERE university_id = ?", new String[]{universityId});
        return cursor.getCount() <= 0;

    }
    public Cursor checkInformations(String emailOrUniversityId, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String HashedPassword = CaeserCipher.encrypt(password, 5);
        if(emailOrUniversityId.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@university.edu$") || emailOrUniversityId.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@library.edu$")){
            Cursor cursor = db.rawQuery("SELECT * FROM Students WHERE email = ? AND password_hash = ?", new String[]{emailOrUniversityId, HashedPassword});
            return cursor;
        }else{
            Cursor cursor = db.rawQuery("SELECT * FROM Students WHERE university_id = ? AND password_hash = ?", new String[]{emailOrUniversityId, HashedPassword});
            return cursor;
        }

    }
    public Cursor getBooks() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                "b.title AS title, " +
                "b.author AS author, " +
                "r.reservation_date AS reservation_date, " +
                "r.due_date AS due_date, " +
                "r.status AS status, " +
                "r.return_date AS return_date " +
                "FROM Reservations r " +
                "JOIN Books b ON r.book_id = b.id";

        return db.rawQuery(query, null);
    }
}

