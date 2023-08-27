package com.example.backend.Newspapers;

public class Titles {
    private String category;
    private String time;
    private String heading;
    private String subHeading;
    private String content;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public static Titles build(
            String category,
            String time,
            String heading,
            String subHeading,
            String content){
        Titles item = new Titles();
        item.category = category;
        item.time = time;
        item.heading = heading;
        item.subHeading = subHeading;
        item.content = content;
        return item;
    }
}