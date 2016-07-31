package com.workfront.internship.dao;

import com.workfront.internship.common.CreditCard;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface CreditCardDao {
    CreditCard getCreditCardByCardID(int cardid);
    int insertCreditCard(CreditCard creditCard);
    void updateCreditCard(CreditCard creditCard);
    void updateCreditCard(Connection connection, CreditCard creditCard);
    void deleteCreditCard(int id);
    void deleteAllCreditCards();
    List<CreditCard> getAllCreditCards();

}
