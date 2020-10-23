package com.example.lenovo.cruciada256;

public class News {

    private String bookID,userID,tip,date;

    public News()
    {

    }

    public News(String bookID, String userID, String tip, String date) {
        this.bookID = bookID;
        this.userID = userID;
        this.tip = tip;
        this.date = date;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
