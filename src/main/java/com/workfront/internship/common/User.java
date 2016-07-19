package com.workfront.internship.common;

import java.util.List;

/**
 * Created by Workfront on 7/1/2016.
 */
public class User {
    private int userID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String phone;
    private String email;
    private boolean confirmationStatus;
    private String accessPrivilege;
    private Basket basket;
    private List<Product> wishList;
    private List<Sale> sales;
    private List<Address> shippingAddresses;

    public List<Product> getWishList() {
        return wishList;
    }

    public User setWishList(List<Product> wishList) {
        this.wishList = wishList;
        return this;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public User setSales(List<Sale> sales) {
        this.sales = sales;
        return this;
    }


    public List<Address> getShippingAddresses() {
        return shippingAddresses;
    }

    public User setShippingAddresses(List<Address> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
        return this;
    }


    public int getUserID() {
        return userID;
    }

    public User setUserID(int userID) {

        this.userID = userID;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {

        this.firstname = firstname;
        return this;
    }

    public String getLastname() {

        return lastname;
    }

    public User setLastname(String lastname) {

        this.lastname = lastname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }


    public String getEmail() {

        return email;
    }

    public User setEmail(String email) {

        this.email = email;
        return this;
    }

    public boolean getConfirmationStatus() {

        return confirmationStatus;
    }

    public User setConfirmationStatus(boolean confirmationStatus) {

        this.confirmationStatus = confirmationStatus;
        return this;
    }

    public String getAccessPrivilege() {
        return accessPrivilege;
    }

    public User setAccessPrivilege(String accessPrivilege) {

        this.accessPrivilege = accessPrivilege;
        return this;
    }

    public Basket getBasket() {

        return basket;
    }

    public User setBasket(Basket basket) {
        this.basket = basket;
        return this;
    }

}

