package daoLayerTest;

import com.workfront.internship.common.Sale;

import com.workfront.internship.dao.SaleDao;
import com.workfront.internship.dao.SaleDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 17.07.2016.
 */
public class SaleDaoUnitTest {
    DataSource dataSource;

    SaleDao saleDao;

    @SuppressWarnings("unchecked")
    @Before
    public void beforeTest() throws Exception {
        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        saleDao = new SaleDaoImpl();
        Whitebox.setInternalState(saleDao, "dataSource", dataSource);
    }

    @After
    public void afterTest() {

    }

    @Test(expected = RuntimeException.class)
    public void insertSale_dbError() {
        saleDao.insertSale(new Sale());
    }
    @Test(expected = RuntimeException.class)
    public void getSaleBySaleID_dbError() {
        saleDao.getSaleBySaleID(10);
    }
    @Test(expected = RuntimeException.class)
    public void getSales_dbError() {
        saleDao.getSales(10);
    }


    @Test(expected = RuntimeException.class)
    public void deleteSaleBySaleID_dbError() {
        saleDao.deleteSaleBySaleID(5);
    }
    @Test(expected = RuntimeException.class)
    public void deleteSaleByUserID_dbError() {
        saleDao.deletSaleByUserID(5);
    }
    @Test(expected = RuntimeException.class)
    public void getAllSales_dbError() {
        saleDao.getAllSales();
    }
}
