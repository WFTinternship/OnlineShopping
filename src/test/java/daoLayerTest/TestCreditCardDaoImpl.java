package daoLayerTest;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

public class TestCreditCardDaoImpl {
    DataSource  dataSource;
    CreditCard creditCard = null;
    int lastInsertedIndex = 0;
    CreditCardDao creditCardDao;

    @Before
    public void setUpDB()  throws SQLException, IOException{
        dataSource = DataSource.getInstance();
        creditCardDao = new CreditCardDaoImpl(dataSource);
        creditCard = getRandomCreditCard();
        lastInsertedIndex = creditCardDao.insertCreditCard(creditCard);

    }

    @After
    public void tearDown() {
        creditCardDao.deleteAllCreditCards();
    }

    @Test
    public void insertCreditCard() {

        CreditCard creditCard1 = getRandomCreditCard();
        creditCard1.setBillingAddress("newRandomBillinAddress");
        int insertindex = creditCardDao.insertCreditCard(creditCard1);

        CreditCard creditCard2 = creditCardDao.getCreditCardByCardID(insertindex);

        doAssertion(creditCard2, creditCard1);

    }

    @Test
    public void getCreditCardByCardID() {

        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);

        doAssertion(creditCard1, creditCard);
    }

    @Test
    public void updateCreditCard() {


        creditCard.setBalance(creditCard.getBalance()+1000);

        creditCardDao.updateCreditCard(creditCard);

        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);

        doAssertion(creditCard1, creditCard);

    }
    @Test
    public void deleteAllCreditCards(){
        creditCardDao.deleteAllCreditCards();

        List<CreditCard> creditCards = creditCardDao.getAllCreditCards();

        assertEquals(true, creditCards.isEmpty());
    }
    @Test
    public void getAllCreditCards(){

        creditCardDao.deleteAllCreditCards();

        List<CreditCard> creditCards = new ArrayList<>();


        CreditCard creditCard = getRandomCreditCard();
        creditCardDao.insertCreditCard(creditCard);
        creditCards.add(creditCard);

        CreditCard creditCard1 = getRandomCreditCard();
        creditCard1.setBillingAddress("newBillingAddress");
        creditCardDao.insertCreditCard(creditCard1);
        creditCards.add(creditCard1);

        List<CreditCard> creditCards1 = creditCardDao.getAllCreditCards();

        for (int i = 0; i < creditCards1.size(); i++) {
            doAssertion(creditCards.get(i), creditCards1.get(i));
        }
    }
    @Test
    public void deleteCreditCard() {

        creditCardDao.deleteCreditCard(lastInsertedIndex);
        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);
        assertNull(creditCard1);
    }

    private CreditCard getRandomCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setBillingAddress("randomBillingAddress").setBalance(1000);
        return creditCard;
    }
    private void doAssertion(CreditCard creditCard, CreditCard creditCard1){
        assertEquals(creditCard.getCardID(), creditCard1.getCardID());
        assertEquals(creditCard.getBillingAddress(), creditCard1.getBillingAddress());
        assertEquals(creditCard.getBalance(), creditCard1.getBalance());
    }

}

