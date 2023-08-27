package com.example.backend.Newspapers;

public class category {
    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return type;
    }

    public void setCategory(String category) {
        this.type = type;
    }
    public static category build(
            int id,
            String type){
        category item = new category();
        item.id = id;
        item.type = type;
        return item;
    }

}
