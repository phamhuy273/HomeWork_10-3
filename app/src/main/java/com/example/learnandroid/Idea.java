package com.example.learnandroid;

public class Idea {
    private String author;
    private String date;
    private String content;

    public Idea(String author, String date, String content) {
        this.author = author;
        this.date = date;
        this.content = content;
    }

    // Các hàm Getters để lấy dữ liệu
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getContent() { return content; }
}