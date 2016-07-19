import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;
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

/**
 * Created by Administrator on 08.07.2016.
 */
public class TestAddressDaoImpl{
    DataSource dataSource;
    Address address = null;
    User user = null;
    int lastInsertedIndex = 0;
    AddressDao addressDao;
    UserDao userDao;

    @Before
    public void setUpDB() throws SQLException, IOException{
        dataSource = DataSource.getInstance();

        addressDao = new AddressDaoImpl(dataSource);

        userDao = new UserDaoImpl(dataSource);
        user = getRandomUser();
        userDao.insertUser(user);

        address = getRandomAddress();
        lastInsertedIndex = addressDao.insertAddress(address);
        address.setAddressID(lastInsertedIndex);
    }

    @After
    public void tearDown() {

        addressDao.deleteAllAddresses();
        userDao.deleteAllUsers();
    }

    @Test
    public void deleteAddressesByUserID(){

        addressDao.deleteAddressesByUserID(address.getUserID());
        List<Address> addresses1 = addressDao.getShippingAddressByUserID(lastInsertedIndex);
        assertEquals(true, addresses1.isEmpty());
    }

    @Test
    public void insertAddress() {

        Address address1 = getRandomAddress();
        int insertindex = addressDao.insertAddress(address1);

        Address address2 = addressDao.getAddressByID(insertindex);

        doAssertion(address2, address1);

    }

    @Test
    public void getAddressByID() {

        Address address1 = addressDao.getAddressByID(lastInsertedIndex);

        doAssertion(address1, address);

    }
    @Test
    public void getShippingAddressByUserID(){
        addressDao.deleteAllAddresses();

        List<Address> addresses = new ArrayList<>();


        Address address = getRandomAddress();
        addressDao.insertAddress(address);
        addresses.add(address);

        Address address1 = getRandomAddress();
        address1.setAddress("newShippingAddress1");
        addressDao.insertAddress(address1);
        addresses.add(address1);

        List<Address> addresses1 = addressDao.getShippingAddressByUserID(user.getUserID());

        for (int i = 0; i < addresses1.size(); i++) {
            doAssertion(addresses.get(i), addresses1.get(i));
        }
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
    public void insertAddress_duplicate(){
        Address address1 = getRandomAddress();
        addressDao.insertAddress(address1);
    }

    @Test(expected = RuntimeException.class)
    public void updateAddress_dulicate(){
        Address address1 = getRandomAddress();
        address1.setAddress("NewAddress");
        addressDao.insertAddress(address1);
        address1.setAddress(address.getAddress());
        addressDao.updateAddress(address1);
    }
    @Test
    public void deleteAllAddresses(){
        addressDao.deleteAllAddresses();

        List<Address> addresses = addressDao.getAllAddresses();

        assertEquals(true, addresses.isEmpty());
    }
    @Test
    public void getAllAddresses(){

        addressDao.deleteAllAddresses();

        List<Address> addresses = new ArrayList<>();


        Address address = getRandomAddress();
        addressDao.insertAddress(address);
        addresses.add(address);

        Address address1 = getRandomAddress();
        address1.setAddress("newShippingAddress1");
        addressDao.insertAddress(address1);
        addresses.add(address1);

        List<Address> addresses1 = addressDao.getAllAddresses();

        for (int i = 0; i < addresses1.size(); i++) {
            doAssertion(addresses.get(i), addresses1.get(i));
        }
    }

    private Address getRandomAddress() {
        Address address = new Address();
        address.setUserID(user.getUserID()).setAddress("randomAddress");
        return address;
    }
    private User getRandomUser() {
        user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal").setPassword("anahitgal85").setEmail("galstyan@gmailgom").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }
    private void doAssertion(Address address, Address address1){
        assertEquals(address.getUserID(), address1.getUserID());
        assertEquals(address.getAddress(), address1.getAddress());
        assertEquals(address.getUserID(), address1.getUserID());
    }


}
