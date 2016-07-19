package com.workfront.internship.common;

import java.util.Date;


/**
 * Created by Administrator on 01.07.2016.
 */

public class Sale {
    private int saleID;
    private int addressID;
    private Date date;
    private Basket basket;
    private int userID;
    private int creditCardID;


    public int getCreditCardID() {

        return creditCardID;
    }

    public Sale setCreditCard(int cardid) {

        creditCardID = cardid;
        return this;
    }


    public int getSaleID() {
        return saleID;
    }

    public Sale setSaleID(int saleID) {

        this.saleID = saleID;
        return this;
    }

    public int getAddressID() {

        return addressID;
    }

    public Sale setAddressID(int addressID) {
        this.addressID = addressID;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Sale setDate(Date date) {
        this.date = date;
        return this;
    }

    public Basket getBasket() {
        return basket;
    }

    public Sale setBasket(Basket basket) {

        this.basket = basket;
        return this;
    }

    public int getUserID() {
        return userID;
    }

    public Sale setUserID(int id) {
        this.userID = id;
        return this;
    }


}


