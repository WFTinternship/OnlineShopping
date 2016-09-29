package daoLayerTest;

import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;
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
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class AddressDaoImplIntegrationTest {

    @Autowired
    AddressDao addressDao;
    @Autowired
    UserDao userDao;
    Address address;
    User user;
    int lastInsertedIndex;


    @Before
    public void setUpDB() throws SQLException, IOException {

        user = TestHelper.getTestUser();
        userDao.insertUser(user);

        address = TestHelper.getTestAddress();
        address.setUserID(user.getUserID());

        lastInsertedIndex = addressDao.insertAddress(address);
        address.setAddressID(lastInsertedIndex);
    }

    @After
    public void tearDown() {

        addressDao.deleteAllAddresses();
        userDao.deleteAllUsers();
    }

    @Test
    public void deleteAddressesByUserID() {

        addressDao.deleteAddressesByUserID(address.getUserID());
        List<Address> addresses1 = addressDao.getShippingAddressByUserID(lastInsertedIndex);
        assertEquals(true, addresses1.isEmpty());
    }

    @Test
    public void insertAddress() {

        Address address2 = addressDao.getAddressByID(lastInsertedIndex);

        doAssertion(address2, address);

    }

    @Test
    public void getAddressByID() {

        Address address1 = addressDao.getAddressByID(lastInsertedIndex);

        doAssertion(address1, address);

    }

    @Test
    public void getShippingAddressByUserID() {

        List<Address> addresses1 = addressDao.getShippingAddressByUserID(user.getUserID());

        doAssertion(addresses1.get(0), address);

    }

    @Test
    public void updateAddress() {

        address.setAddress("newShippingAddress");

        addressDao.updateAddress(address);

        Address address1 = addressDao.getAddressByID(lastInsertedIndex);

        doAssertion(address, address1);

    }

    @Test
    public void deleteAddressesByAddressID() {

        addressDao.deleteAddressesByAddressID(lastInsertedIndex);

        Address address1 = addressDao.getAddressByID(lastInsertedIndex);

        assertNull(address1);
    }

    @Test(expected = RuntimeException.class)
    public void updateAddress_dulicate() {

        Address address1 = TestHelper.getTestAddress();
        address1.setAddress("NewAddress");

        addressDao.insertAddress(address1);

        address1.setAddress(address.getAddress());
        addressDao.updateAddress(address1);
    }

    @Test
    public void deleteAllAddresses() {
        addressDao.deleteAllAddresses();

        List<Address> addresses = addressDao.getAllAddresses();

        assertEquals(true, addresses.isEmpty());
    }

    @Test
    public void getAllAddresses() {

        List<Address> addresses1 = addressDao.getAllAddresses();

        doAssertion(addresses1.get(0), address);

    }

    private void doAssertion(Address address, Address address1) {
        assertEquals(address.getUserID(), address1.getUserID());
        assertEquals(address.getAddress(), address1.getAddress());
        assertEquals(address.getUserID(), address1.getUserID());
    }


}
