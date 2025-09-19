package com.example.project;

public class Rev {
    private int id;
    private int student_id;
    private int book_id;
    private String reservation_date;
    private String due_date;
    private String return_date;
    private String status;

    public Rev() {
    }

    public Rev(int id, String status, String return_date, String due_date, String reservation_date, int book_id, int student_id) {
        this.id = id;
        this.status = status;
        this.return_date = return_date;
        this.due_date = due_date;
        this.reservation_date = reservation_date;
        this.book_id = book_id;
        this.student_id = student_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}
