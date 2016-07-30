package com.workfront.internship.business;

import com.workfront.internship.common.CreditCard;

/**
 * Created by Workfront on 7/29/2016.
 */
public interface CreditcardManager {
    CreditCard getCreditCardByCardID(int cardid);
    int insertCreditCard(CreditCard creditCard);
    void updateCreditCard(CreditCard creditCard);
    void deleteCreditCard(int id);
}
