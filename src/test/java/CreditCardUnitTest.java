import com.workfront.internship.common.CreditCard;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class CreditCardUnitTest {

    DataSource dataSource;
    CreditCardDao creditCardDao;

    @Before
    public void beforeTest()throws IOException, SQLException{
        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        creditCardDao = new CreditCardDaoImpl(dataSource);
    }
    @Test(expected = RuntimeException.class)
    public void insertCreditCard_dbError() {
        creditCardDao.insertCreditCard(new CreditCard());
    }
    @Test(expected = RuntimeException.class)
    public void getCreditCard_dbError() {
        creditCardDao.getCreditCardByCardID(8);
    }

    @Test(expected = RuntimeException.class)
    public void getAllCreditCards_dbError() {
        creditCardDao.getAllCreditCards();
    }
    @Test(expected = RuntimeException.class)
    public void updateCreditCard_dbError() {
        creditCardDao.updateCreditCard(new CreditCard());
    }
    @Test(expected = RuntimeException.class)
    public void deleteBasketByUserID_dbError() {
        creditCardDao.deleteCreditCard(2);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllCreditCards_dbError() {
        creditCardDao.deleteAllCreditCards();
    }
}
