package com.workfront.internship.common;

/**
 * Created by Administrator on 04.07.2016.
 */
public class CreditCard {
    private int cardID;
    private String billingAddress;
    private double balance;
    private String cardNumber;
    private int cvc;

    public double getBalance() {
        return balance;
    }

    public CreditCard setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public CreditCard setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public int getCardID() {
        return cardID;
    }

    public CreditCard setCardID(int cardID) {
        this.cardID = cardID;
        return this;
    }


    public int getCvc() {
        return cvc;
    }

    public CreditCard setCvc(int cvc) {
        this.cvc = cvc;
        return this;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CreditCard setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return  this;
    }
}
