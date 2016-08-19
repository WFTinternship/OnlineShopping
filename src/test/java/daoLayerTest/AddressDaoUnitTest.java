package daoLayerTest;

import com.workfront.internship.common.Address;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class AddressDaoUnitTest {

    LegacyDataSource dataSource;

    AddressDao addressDao;

   // @SuppressWarnings("unchecked")
    @Before
    public void beforeTest() throws Exception {

        dataSource = Mockito.mock(LegacyDataSource.class);

        Connection connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        addressDao = new AddressDaoImpl(dataSource);
    }

    @After
    public void afterTest() {

    }

    @Test(expected = RuntimeException.class)
    public void insertAddress_dbError() {
        addressDao.insertAddress(new Address());
    }
    @Test(expected = RuntimeException.class)
    public void getAddress_dbError() {
        addressDao.getAddressByID(8);
    }
    @Test(expected = RuntimeException.class)
    public void updateAddress_dbError() {
        addressDao.updateAddress(new Address());
    }
    @Test(expected = RuntimeException.class)
    public void deleteAddress_dbError() {
        addressDao.deleteAllAddresses();
    }
    @Test(expected = RuntimeException.class)
    public void deleteAddressByAddressID_dbError() {
        addressDao.deleteAddressesByAddressID(8);
    }
    @Test(expected = RuntimeException.class)
    public void deleteAddressByUserID_dbError() {
        addressDao.deleteAddressesByUserID(10);
    }
    @Test(expected = RuntimeException.class)
    public void getAllAddresses_dbError() {
        addressDao.getAllAddresses();
    }
    @Test(expected = RuntimeException.class)
    public void getShippingAddresses_dbError() {
        addressDao.getShippingAddressByUserID(8);
    }
}
