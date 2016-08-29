package businessLayerTest;

import com.workfront.internship.business.CreditcardManager;
import com.workfront.internship.business.CreditcardManagerImpl;
import com.workfront.internship.common.CreditCard;

import com.workfront.internship.spring.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by Anna Asmangulyan on 01.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class CreditCardManagerImplTest {
    private CreditCard creditCard;
    @Autowired
    private CreditcardManager creditcardManager;

    @Before
    public void setUP() throws IOException, SQLException {

        creditCard = getTestCreditCard();
    }
    @After
    public void tearDown()  {
        creditcardManager.deleteCreditCard(creditCard.getCardID());
    }
    @Test
    public void createCreditCard(){
        int id = creditcardManager.createCreditCard(creditCard);
        assertFalse(id == 0);
    }
    private CreditCard getTestCreditCard(){
        creditCard = new CreditCard();
        creditCard.setBalance(1000).setBillingAddress("someAddress");
        return creditCard;
    }
}
