package com.example.lenovo.cruciada256;

public class Event {

    public String date, hour, image, name;

    public Event(){

    }

    public Event(String image, String date, String hour, String name) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}