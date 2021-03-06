package com.noobprogaming.warungman.Model;

public class ItemModel {

    private String item_id;
    private String name;
    private String stock;
    private String selling_price;
    private String seller_id;

    public ItemModel(String item_id, String name, String stock, String selling_price, String seller_id) {
        this.item_id = item_id;
        this.name = name;
        this.stock = stock;
        this.selling_price = selling_price;
        this.seller_id = seller_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
}
