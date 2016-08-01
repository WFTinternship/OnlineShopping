package businessLayerTest;

import com.workfront.internship.business.CreditcardManager;
import com.workfront.internship.business.CreditcardManagerImpl;
import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 01.08.2016.
 */
public class CreditCardManagerImplTest {
    private CreditCard creditCard;
    private CreditcardManager creditcardManager;
    DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        creditCard = getTestCreditCard();
        creditcardManager = new CreditcardManagerImpl(dataSource);

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
