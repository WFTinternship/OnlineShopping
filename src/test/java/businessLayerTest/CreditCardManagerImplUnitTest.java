package businessLayerTest;

import com.workfront.internship.business.CreditcardManager;
import com.workfront.internship.business.CreditcardManagerImpl;
import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.CreditCardDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 31.07.2016.
 */
public class CreditCardManagerImplUnitTest {
    private CreditCard creditCard;
    private CreditcardManager creditcardManager;
    @Mock
    CreditCardDao creditCardDao;

    @Before
    public void setUP() throws IOException, SQLException {
        creditCard = getTestCreditCard();
        creditcardManager = new CreditcardManagerImpl();
        MockitoAnnotations.initMocks(this);

    }
    @After
    public void tearDown() {
        creditCard = null;
        creditcardManager = null;
        creditCardDao = null;

    }
    @Test
    public void createCreditcard(){
        when(creditCardDao.insertCreditCard(creditCard)).thenReturn(1);

        int result = creditcardManager.createCreditCard(creditCard);

        assertEquals(1, result);

    }
    @Test(expected = RuntimeException.class)
    public void createCreditcard_invalid_card(){
        creditcardManager.createCreditCard(null);

    }
    @Test
    public void getCreditcard(){
        when(creditCardDao.getCreditCardByCardID(creditCard.getCardID())).thenReturn(creditCard);

        CreditCard creditCard1 = creditcardManager.getCreditCardByCardID(creditCard.getCardID());

        doAssertion(creditCard1, creditCard);
    }
    @Test(expected = RuntimeException.class)
    public void getCreditcard_invalidID(){
        creditcardManager.getCreditCardByCardID(-3);
    }
    private CreditCard getTestCreditCard(){
        CreditCard creditCard = new CreditCard();
        creditCard.setCardID(1).setBillingAddress("some billing address").setBalance(1000);
        return creditCard;
    }
    @Test
    public void updateCreditCard(){

        creditcardManager.updateCreditCard(creditCard);

        Mockito.verify(creditCardDao).updateCreditCard(creditCard);

    }
    @Test(expected = RuntimeException.class)
    public void updateCreditCard_invalid_card(){
        creditcardManager.updateCreditCard(null);
    }
    @Test
    public void deleteCreditcard(){

        creditcardManager.deleteCreditCard(creditCard.getCardID());

        Mockito.verify(creditCardDao).deleteCreditCard(creditCard.getCardID());


    }
    @Test(expected = RuntimeException.class)
    public void deleteCreditcard_invalidID(){
        creditcardManager.deleteCreditCard(-2);

    }
    private void doAssertion(CreditCard creditCard, CreditCard creditCard1){
        assertEquals("card was not found", creditCard.getCardID(), creditCard1.getCardID());
        assertEquals("card was not found", creditCard.getBillingAddress(), creditCard1.getBillingAddress());
        assertEquals("card was not found", creditCard.getBalance(), creditCard1.getBalance());
    }
}


