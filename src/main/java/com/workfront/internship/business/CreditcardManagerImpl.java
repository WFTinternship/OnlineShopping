package com.workfront.internship.business;

import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.CreditCardDao;
import com.workfront.internship.dao.CreditCardDaoImpl;
import com.workfront.internship.dao.DataSource;

import java.io.IOException;
import java.sql.SQLException;


public class CreditcardManagerImpl implements CreditcardManager {
    private DataSource dataSource;
    private CreditCardDao creditCardDao;

    public CreditcardManagerImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        creditCardDao = new CreditCardDaoImpl(dataSource);
    }

    public CreditCard getCreditCardByCardID(int cardid){
        if(cardid<=0)
            throw new RuntimeException("invalid card id");
        return creditCardDao.getCreditCardByCardID(cardid);

    }
    public int insertCreditCard(CreditCard creditCard){
        if(!validateCard(creditCard))
            throw new RuntimeException("invalid card");
        int result = creditCardDao.

    }
    public void updateCreditCard(CreditCard creditCard){

    }
    public void deleteCreditCard(int id){

    }
}
