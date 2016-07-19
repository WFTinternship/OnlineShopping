package com.workfront.internship.common;

import java.util.List;

/**
 * Created by Administrator on 01.07.2016.
 */
public class Basket {
    private int basketID;
    private int userID;
    private List<OrderItem> orderItems;
    private double totalPrice;
    private String basketStatus;

    public String getBasketStatus() {
        return basketStatus;
    }

    public Basket setBasketStatus(String status) {
        this.basketStatus = status;
        return this;
    }

    public int getBasketID() {

        return basketID;
    }

    public Basket setBasketID(int basketID) {
        this.basketID = basketID;
        return this;
    }

    public int getUserID() {

        return userID;
    }

    public Basket setUserID(int userID) {

        this.userID = userID;
        return this;
    }

    public List<OrderItem> getOrderItems() {

        return orderItems;
    }

    public Basket setOrderItems(List<OrderItem> orderItems) {

        this.orderItems = orderItems;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Basket setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }


}

