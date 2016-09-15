package com.workfront.internship.business;

import com.workfront.internship.common.CreditCard;

/**
 * Created by Workfront on 7/29/2016.
 */
public interface CreditcardManager {
    CreditCard getCreditCardByCardID(int cardid);
    CreditCard getCreditCardByCardNumber(String  cardNumber);
    int createCreditCard(CreditCard creditCard);
    void updateCreditCard(CreditCard creditCard);
    void deleteCreditCard(int id);
}
