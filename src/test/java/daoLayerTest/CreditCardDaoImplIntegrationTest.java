package daoLayerTest;

import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.*;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class CreditCardDaoImplIntegrationTest {

    @Autowired
    CreditCardDao creditCardDao;

    CreditCard creditCard;
    int lastInsertedIndex;

    @Before
    public void setUpDB() throws SQLException, IOException {

        creditCard = TestHelper.getTestCreditCard();
        lastInsertedIndex = creditCardDao.insertCreditCard(creditCard);

    }

    @After
    public void tearDown() {
        creditCardDao.deleteAllCreditCards();
    }

    @Test
    public void insertCreditCard() {

        CreditCard creditCard2 = creditCardDao.getCreditCardByCardID(creditCard.getCardID());

        doAssertion(creditCard2, creditCard);

    }

    @Test
    public void getCreditCardByCardID() {

        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);

        doAssertion(creditCard1, creditCard);
    }

    @Test
    public void updateCreditCard() {


        creditCard.setBalance(creditCard.getBalance() + 1000);

        creditCardDao.updateCreditCard(creditCard);

        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);

        doAssertion(creditCard1, creditCard);

    }

    @Test
    public void deleteAllCreditCards() {

        creditCardDao.deleteAllCreditCards();

        List<CreditCard> creditCards = creditCardDao.getAllCreditCards();

        assertEquals(true, creditCards.isEmpty());
    }

    @Test
    public void getAllCreditCards() {

        List<CreditCard> creditCards1 = creditCardDao.getAllCreditCards();

        doAssertion(creditCards1.get(0), creditCard);

    }

    @Test
    public void deleteCreditCard() {

        creditCardDao.deleteCreditCard(lastInsertedIndex);

        CreditCard creditCard1 = creditCardDao.getCreditCardByCardID(lastInsertedIndex);

        assertNull(creditCard1);
    }

    private void doAssertion(CreditCard creditCard, CreditCard creditCard1) {

        assertEquals(creditCard.getCardID(), creditCard1.getCardID());
        assertEquals(creditCard.getBillingAddress(), creditCard1.getBillingAddress());
        assertEquals(creditCard.getBalance(), creditCard1.getBalance());
    }

}

