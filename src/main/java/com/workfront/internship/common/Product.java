package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */

import java.util.List;
import java.util.Map;

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
    private List<Media> medias;
    private Map<String, Integer> sizeOptionQuantity;
    private int saled;

    public Map<String, Integer> getSizeOptionQuantity() {
        return sizeOptionQuantity;
    }

    public Product setSizeOptionQuantity( Map<String, Integer> sizeOptionQuantity) {
        this.sizeOptionQuantity = sizeOptionQuantity;
        return this;
    }



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

    public List<Media> getMedias() {

        return medias;
    }

    public Product setMedias(List<Media> medias) {

        this.medias = medias;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;

        if (getProductID() != product.getProductID()) return false;
        if (!getName().equals(product.getName())) return false;
        if (!getDescription().equals(product.getDescription())) return false;
        if (getPrice() != product.getPrice()) return false;
        if (getShippingPrice() != product.getShippingPrice()) return false;
        if (getCategory() != null ? !getCategory().equals(product.getCategory()) : product.getCategory() != null) return false;
        List<Media> medias = product.getMedias();
        if(getMedias().size() != medias.size()) return false;
        for(int i = 0; i< medias.size(); i++)
            if(getMedias().get(i) != null ? getMedias().get(i).equals(medias.get(i)) : medias.get(i) != null) return false;

        return true;

    }

    public int getSaled() {
        return saled;
    }

    public Product setSaled(int saled) {
        this.saled = saled;
        return this;
    }
}
