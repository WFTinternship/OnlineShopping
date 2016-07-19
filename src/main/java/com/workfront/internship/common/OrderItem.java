package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */
public class OrderItem {
    private int basketID;
    private int orderItemID;
    private Product product;
    private int quantity;

    public int getOrderItemID() {
        return orderItemID;
    }

    public OrderItem setOrderItemID(int orderItemID) {

        this.orderItemID = orderItemID;
        return this;
    }

    public Product getProduct() {

        return product;
    }

    public OrderItem setProduct(Product product) {

        this.product = product;
        return this;
    }

    public int getQuantity() {

        return quantity;
    }

    public OrderItem setQuantity(int quantity)

    {
        this.quantity = quantity;
        return this;
    }

    public int getBasketID() {

        return basketID;
    }

    public OrderItem setBasketID(int basketID) {

        this.basketID = basketID;
        return this;
    }


}
