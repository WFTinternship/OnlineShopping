package com.workfront.internship.business;

import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.CreditCardDao;
import com.workfront.internship.dao.CreditCardDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component
public class CreditcardManagerImpl implements CreditcardManager {

    @Autowired
    private CreditCardDao creditCardDao;



    public CreditCard getCreditCardByCardID(int cardid){
        if(cardid<=0)
            throw new RuntimeException("invalid card id");
        return creditCardDao.getCreditCardByCardID(cardid);

    }
    //TODO test
    public CreditCard getCreditCardByCardNumber(String cardNumber){
        if(cardNumber == null)
            throw new RuntimeException("invalid card number");
        return creditCardDao.getCreditCardByCardNumber(cardNumber);

    }
    public int createCreditCard(CreditCard creditCard){
        if(!validateCard(creditCard))
            throw new RuntimeException("invalid card");
        int result = creditCardDao.insertCreditCard(creditCard);
        return result;

    }
    public void updateCreditCard(CreditCard creditCard){
        if(!validateCard(creditCard))
            throw new RuntimeException("invalid card");
        creditCardDao.updateCreditCard(creditCard);


    }
    public void deleteAllCards(){
        creditCardDao.deleteAllCreditCards();

    }
    public void deleteCreditCard(int cardid){
        if(cardid<=0)
            throw new RuntimeException("invalid card id");
        creditCardDao.deleteCreditCard(cardid);
    }
    private boolean validateCard(CreditCard creditCard){
        if(creditCard !=null && creditCard.getBalance() >= 0.0 && creditCard.getBillingAddress() != null)
            return true;
        return false;
    }

}
