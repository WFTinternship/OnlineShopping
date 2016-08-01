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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Basket basket = (Basket) obj;

        if (getBasketID() != basket.getBasketID()) return false;
        if (getTotalPrice() != basket.getTotalPrice()) return false;
        if (getBasketStatus()!= basket.getBasketStatus()) return false;
        if (getUserID()!= basket.getUserID()) return false;
        if(!getOrderItems().isEmpty() && getOrderItems().size() == basket.getOrderItems().size())
        for(int i = 0; i< getOrderItems().size(); i++)
            if (getOrderItems().get(i) != null ? !getOrderItems().get(i).equals(basket.getOrderItems().get(i)) : basket.getOrderItems().get(i) != null) return false;

        return true;
    }


}

