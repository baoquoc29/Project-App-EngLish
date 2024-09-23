package com.example.testaudioenglish.Model;

public class EachPartModel {
    private String title;
    private String namePart;
    private int quantity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNamePart() {
        return namePart;
    }

    public void setNamePart(String namePart) {
        this.namePart = namePart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EachPartModel(String title, String namePart, int quantity) {
        this.title = title;
        this.namePart = namePart;
        this.quantity = quantity;
    }
}
