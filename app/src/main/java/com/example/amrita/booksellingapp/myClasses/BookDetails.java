package com.example.amrita.booksellingapp.myClasses;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BookDetails {
    private String bookname;
    private String author;
    private String sem;
    private String category;
    private String img;
    private String userId;

    private @ServerTimestamp Date timestamp;
    private int mrp;
    public BookDetails(){}

    public BookDetails(String bookname, String author, String sem, String category, int mrp,String img,String userId,Date timestamp) {
        this.bookname = bookname;
        this.author = author;
        this.sem = sem;
        this.category = category;
        this.mrp = mrp;
        this.img=img;
        this.userId=userId;
        this.timestamp=timestamp;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
