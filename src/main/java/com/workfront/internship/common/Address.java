package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */
public class Address {
    private int addressID;
    private String address;
    private int userID;
    private String city;
    private String country;
    private String zipCode;

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }


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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Address address = (Address) obj;

        if (getAddressID() != address.getAddressID()) return false;
        if (getAddress() != null ? !getAddress().equals(address.getAddress()) : address.getAddress() != null)
            return false;
        if (getCity() != null ? !getCity().equals(address.getCity()) : address.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(address.getCountry()) : address.getCountry() != null)
            return false;
        if (getUserID() != address.getUserID()) return false;
        if (getZipCode() != address.getZipCode()) return false;

        return true;
    }
}
