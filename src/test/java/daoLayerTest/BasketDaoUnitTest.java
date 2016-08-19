package daoLayerTest;

import com.workfront.internship.common.Basket;
import com.workfront.internship.dao.*;
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


public class BasketDaoUnitTest {
    LegacyDataSource dataSource;
    BasketDao basketDao;

    @Before
    public void beforeTest() throws IOException, SQLException{

        dataSource = Mockito.mock(LegacyDataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        basketDao = new BasketDaoImpl(dataSource);
    }

    @Test(expected = RuntimeException.class)
    public void insertBasket_dbError() {
        basketDao.insertBasket(new Basket());
    }
    @Test(expected = RuntimeException.class)
    public void getBasket_dbError() {
        basketDao.getBasket(8);
    }
    @Test(expected = RuntimeException.class)
    public void getCurrentBasket_dbError() {
        basketDao.getCurrentBasket(3);
    }
    @Test(expected = RuntimeException.class)
    public void getAllBaskets_dbError() {
        basketDao.getAllBaskets();
    }
    @Test(expected = RuntimeException.class)
    public void updateBasket_dbError() {
        basketDao.updateBasket(new Basket());
    }
    @Test(expected = RuntimeException.class)
    public void deleteBasketByUserID_dbError() {
        basketDao.deleteBasketByUserId(2);
    }
    @Test(expected = RuntimeException.class)
    public void deleteBasketByBasktID_dbError() {
        basketDao.deleteBasketByBasketID(8);
    }
    @Test(expected = RuntimeException.class)
    public void deleteAllBaskets_dbError() {
        basketDao.deleteAllBaskets();
    }

}
