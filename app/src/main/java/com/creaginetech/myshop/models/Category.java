package com.creaginetech.myshop.models;

public class Category {

    String nameCategory,shopId,image_url;

    public Category() {
    }

    public Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Category(String nameCategory, String shopId) {
        this.nameCategory = nameCategory;
        this.shopId = shopId;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
