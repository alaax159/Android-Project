package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                "phone_number TEXT," +
                "department TEXT," +
                "level TEXT)");

        // Reservations Table
        db.execSQL("CREATE TABLE IF NOT EXISTS Reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER NOT NULL," +
                "book_id INTEGER NOT NULL," +
                "reservation_date TEXT NOT NULL," +
                "due_date TEXT," +
                "return_date TEXT," +
                "status TEXT," +
                "collection_method TEXT," +
                "special_notes TEXT," +
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
//        if (oldVersion < 4) { // upgrading to version 4
//            db.execSQL("ALTER TABLE Reservations ADD COLUMN return_date TEXT");
//        }
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        onCreate(db);
    }
    public void registerStudent(String universityId, String firstName, String lastName, String email, String passwordHash, String department, String level, String phone_number){
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
        contentValues.put("phone_number", phone_number);
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
    public Cursor getFavorites(int tvId, int acc_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Reading_List WHERE book_id = ? AND student_id = ?", new String[]{String.valueOf(tvId), String.valueOf(acc_id)});
    }
    public void addFavorite(int book_id, int acc_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        contentValues.put("student_id", acc_id);
        contentValues.put("book_id", book_id);
        contentValues.put("added_date",today);
        db.insert("Reading_List", null, contentValues);
    }
    public void removeFavorite(int book_id, int acc_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Reading_List", "book_id = ? AND student_id = ?", new String[]{String.valueOf(book_id), String.valueOf(acc_id)});
    }
    public boolean isReserved(int studentId, int bookId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Reservations WHERE student_id = ? AND book_id = ?",
                new String[]{ String.valueOf(studentId), String.valueOf(bookId) }
        );
        return c.getCount() > 0;
    }

    public Cursor getBooks(String SId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                "b.title AS title, " +
                "b.author AS author, " +
                "r.reservation_date AS reservation_date, " +
                "r.due_date AS due_date, " +
                "r.status AS status " +
                "FROM Reservations r " +
                "JOIN Books b ON r.book_id = b.id " +
                "WHERE r.student_id = ?";

        return db.rawQuery(query, new String[]{SId});
    }

    public Cursor StudentDataID(String universityId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Students WHERE university_id = ?", new String[]{universityId});

    }

    public Cursor getAllBooksReading(String SId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.title, b.author, b.availability, b.id " +
                "FROM Reading_List r " +
                "JOIN Books b ON r.book_id = b.id " +
                "WHERE r.student_id = ?";
        return db.rawQuery(query, new String[]{SId});
    }
    public int removeBookFromReadingList(String studentId, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(
                "Reading_List",
                "student_id = ? AND book_id = ?",
                new String[]{studentId, String.valueOf(bookId)}
        );
    }
    public boolean reserveBook(int bookId, int studentId, int weeks, String method, String notes) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusWeeks(weeks);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("student_id", studentId);
        v.put("book_id", bookId);
        v.put("reservation_date", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        v.put("due_date", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        v.put("status", "Borrowed");
        v.put("collection_method", method);
        v.put("special_notes", notes);

        long rowId = db.insert("Reservations", null, v);
        return rowId != -1;
    }

    public boolean addToBorrowedBooks(int studentId, int bookId, String reservationDate, String dueDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("book_id", bookId);
        values.put("reservation_date", reservationDate);
        values.put("due_date", dueDate);
        values.put("status", status);

        long result = db.insert("Reservations", null, values);

        return result != -1;
    }

    public Cursor BookInfo(String bookId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Books WHERE id = ?", new String[]{bookId});
    }
    public Cursor getAllBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Books", null);
    }

    public void insertDummyBooks() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("title", "Introduction to Algorithms");
        values1.put("author", "Thomas H. Cormen");
        values1.put("isbn", "9780262033848");
        values1.put("category", "Computer Science");
        values1.put("availability", 1);
        values1.put("cover_url", "https://example.com/algorithms.jpg");
        values1.put("publication_year", 2009);
        db.insert("Books", null, values1);

        ContentValues values2 = new ContentValues();
        values2.put("title", "Clean Code");
        values2.put("author", "Robert C. Martin");
        values2.put("isbn", "9780132350884");
        values2.put("category", "Software Engineering");
        values2.put("availability", 1);
        values2.put("cover_url", "https://example.com/cleancode.jpg");
        values2.put("publication_year", 2008);
        db.insert("Books", null, values2);

        ContentValues values3 = new ContentValues();
        values3.put("title", "Artificial Intelligence: A Modern Approach");
        values3.put("author", "Stuart Russell");
        values3.put("isbn", "9780136042594");
        values3.put("category", "AI");
        values3.put("availability", 0);
        values3.put("cover_url", "https://example.com/ai.jpg");
        values3.put("publication_year", 2010);
        db.insert("Books", null, values3);
    }

    public void insertDummyReservations() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues r1 = new ContentValues();
        r1.put("student_id", 1);   // assuming student with id = 1 exists
        r1.put("book_id", 2);      // Clean Code
        r1.put("reservation_date", "2025-09-15");
        r1.put("due_date", "2025-10-15");
        r1.put("status", "Borrowed");
        db.insert("Reservations", null, r1);

        ContentValues r2 = new ContentValues();
        r2.put("student_id", 1);
        r2.put("book_id", 3);      // AI book
        r2.put("reservation_date", "2025-09-10");
        r2.put("due_date", "2025-09-24");
        r2.put("status", "Returned");
        db.insert("Reservations", null, r2);

        // Ahmad Odeh -> Introduction to Algorithms
        ContentValues r3 = new ContentValues();
        r3.put("student_id", 1);
        r3.put("book_id", 1);  // Algorithms
        r3.put("reservation_date", "2025-09-10");
        r3.put("due_date", "2025-09-24");
        r3.put("status", "Borrowed");
        db.insert("Reservations", null, r3);

        // Sara Khalil -> Clean Code
        ContentValues r4 = new ContentValues();
        r4.put("student_id", 2);
        r4.put("book_id", 2);
        r4.put("reservation_date", "2025-09-11");
        r4.put("due_date", "2025-09-25");
        r4.put("status", "Borrowed");
        db.insert("Reservations", null, r4);

        // Omar Nasser -> AI: A Modern Approach
        ContentValues r5 = new ContentValues();
        r5.put("student_id", 3);
        r5.put("book_id", 3);  // AI
        r5.put("reservation_date", "2025-09-01");
        r5.put("due_date", "2025-09-15");
        r5.put("status", "Returned");
        db.insert("Reservations", null, r5);

        // Lina Hassan -> Database System Concepts
        ContentValues r6 = new ContentValues();
        r6.put("student_id", 4);
        r6.put("book_id", 4);  // Databases
        r6.put("reservation_date", "2025-09-05");
        r6.put("due_date", "2025-09-19");
        r6.put("status", "Borrowed");
        db.insert("Reservations", null, r6);

        // Yousef Salem -> Operating System Concepts
        ContentValues r7 = new ContentValues();
        r7.put("student_id", 5);
        r7.put("book_id", 5);  // OS
        r7.put("reservation_date", "2025-09-02");
        r7.put("due_date", "2025-09-16");
        r7.put("status", "Overdue");
        db.insert("Reservations", null, r7);


    }

    public void insertDummyReadingList() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues rl1 = new ContentValues();
        rl1.put("student_id", 1);
        rl1.put("book_id", 1);   // Algorithms
        rl1.put("added_date", "2025-09-12");
        db.insert("Reading_List", null, rl1);

        ContentValues rl2 = new ContentValues();
        rl2.put("student_id", 2);
        rl2.put("book_id", 2);   // Clean Code
        rl2.put("added_date", "2025-09-14");
        db.insert("Reading_List", null, rl2);
    }

    public void insertDummyStudents() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues s1 = new ContentValues();
        s1.put("university_id", "20250001");
        s1.put("first_name", "Ahmad");
        s1.put("last_name", "Odeh");
        s1.put("email", "ahmad.odeh@university.edu");
        s1.put("password_hash", CaeserCipher.encrypt("Pass@123", 5));
        s1.put("department", "Computer Science");
        s1.put("level", "Junior");
        db.insert("Students", null, s1);

        ContentValues s2 = new ContentValues();
        s2.put("university_id", "20250002");
        s2.put("first_name", "Sara");
        s2.put("last_name", "Khalil");
        s2.put("email", "sara.khalil@university.edu");
        s2.put("password_hash", CaeserCipher.encrypt("S@ra4567", 5));
        s2.put("department", "Engineering");
        s2.put("level", "Sophomore");
        db.insert("Students", null, s2);

        ContentValues s3 = new ContentValues();
        s3.put("university_id", "20250003");
        s3.put("first_name", "Omar");
        s3.put("last_name", "Nasser");
        s3.put("email", "omar.nasser@university.edu");
        s3.put("password_hash", CaeserCipher.encrypt("Om@r7890", 5));
        s3.put("department", "Business");
        s3.put("level", "Senior");
        db.insert("Students", null, s3);

        ContentValues s4 = new ContentValues();
        s4.put("university_id", "20250004");
        s4.put("first_name", "Lina");
        s4.put("last_name", "Hassan");
        s4.put("email", "lina.hassan@university.edu");
        s4.put("password_hash", CaeserCipher.encrypt("L!na2025", 5));
        s4.put("department", "Literature");
        s4.put("level", "Freshman");
        db.insert("Students", null, s4);

        ContentValues s5 = new ContentValues();
        s5.put("university_id", "20250005");
        s5.put("first_name", "Yousef");
        s5.put("last_name", "Salem");
        s5.put("email", "yousef.salem@university.edu");
        s5.put("password_hash", CaeserCipher.encrypt("Y0us3f!", 5));
        s5.put("department", "Medicine");
        s5.put("level", "Graduate");
        db.insert("Students", null, s5);
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Reservations", null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Reservations", null);
    }

    public void insertNewBook(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values3 = new ContentValues();
        values3.put("title", "Computer Organization and Architecture: A Modern Approach");
        values3.put("author", "Adnan Odeh");
        values3.put("isbn", "9780136042323");
        values3.put("category", "Hardware Engineering");
        values3.put("availability", 1);
        values3.put("cover_url", "https://example.com/ai.jpg");
        values3.put("publication_year", 2001);
        db.insert("Books", null, values3);
    }
}

