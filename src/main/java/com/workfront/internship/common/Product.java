package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */

import java.util.List;

/**
 * Created by Administrator on 29.06.2016.
 */
public class Product {
    private int productID;
    private String name;
    private Category category;
    private double price;
    private double shippingPrice;
    private String description;
    private int quantity;
    private List<Media> medias;

    public int getProductID() {

        return productID;
    }

    public Product setProductID(int productID) {
        this.productID = productID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {

        return category;
    }

    public Product setCategory(Category category) {

        this.category = category;
        return this;
    }

    public double getPrice() {

        return price;
    }

    public Product setPrice(double price) {

        this.price = price;
        return this;
    }

    public double getShippingPrice() {

        return shippingPrice;
    }

    public Product setShippingPrice(double shippingPrice) {

        this.shippingPrice = shippingPrice;
        return this;
    }

    public String getDescription() {

        return description;
    }

    public Product setDescription(String description) {

        this.description = description;
        return this;
    }

    public int getQuantity() {

        return quantity;
    }

    public Product setQuantity(int quantity) {

        this.quantity = quantity;
        return this;
    }

    public List<Media> getMedias() {

        return medias;
    }

    public Product setMedias(List<Media> medias) {

        this.medias = medias;
        return this;
    }


}
