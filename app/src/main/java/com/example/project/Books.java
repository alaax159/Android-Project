package com.example.project;
public class Books {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int availability;
    private String cover_url;
    private int publication_year;

    public Books() {
    }

    public Books(String title, int id, String author, int availability) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.availability = availability;
    }

    public Books(int publication_year, String cover_url, int availability, String category, String isbn, String author, String title, int id) {
        this.publication_year = publication_year;
        this.cover_url = cover_url;
        this.availability = availability;
        this.category = category;
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", category='" + category + '\'' +
                ", availability=" + availability +
                ", cover_url='" + cover_url + '\'' +
                ", publication_year=" + publication_year +
                '}';
    }

}
