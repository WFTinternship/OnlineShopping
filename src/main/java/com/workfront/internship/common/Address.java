package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */
public class Address {
    private int addressID;
    private String address;
    private int userID;

    public int getAddressID() {
        return addressID;
    }

    public Address setAddressID(int addressID) {
        this.addressID = addressID;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Address setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getUserID() {
        return userID;
    }

    public Address setUserID(int userID) {
        this.userID = userID;
        return this;
    }


}
