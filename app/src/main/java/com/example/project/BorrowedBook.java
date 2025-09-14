package com.example.project;

public class BorrowedBook {
    public final String title, author, reservationDate, dueDate, returnDate, status, fine;
    public BorrowedBook(String title, String author, String reservationDate,
                        String dueDate, String returnDate, String status, String fine) {
        this.title = title;
        this.author = author;
        this.reservationDate = reservationDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
    }
}