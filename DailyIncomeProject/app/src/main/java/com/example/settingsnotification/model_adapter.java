package com.example.settingsnotification;

public class model_adapter {
    private int id;
    private String item;
    private float price;

    public model_adapter(int id, String item, float price) {
        this.id = id;
        this.item = item;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return item;
    }

    public void setItemName(String item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
