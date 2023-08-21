package com.example.backend.Newspapers;

public class newspaper {
    private String code;
    private String category;
    private int hour;
    private int minute;
    private int second;
    private int day;
    private int month;
    private int year;
    private String heading;
    private String description;
    private String img;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public static newspaper build(
            String code,
            String category,
            int hour,
            int minute,
            int second,
            int day,
            int month,
            int year,
            String heading,
            String description,
            String img){
        newspaper item = new newspaper();
        item.code = code;
        item.category = category;
        item.hour = hour;
        item.minute = minute;
        item.second = second;
        item.day = day;
        item.month = month;
        item.year = year;
        item.heading = heading;
        item.description = description;
        item.img = img;
        return item;
    }

}
