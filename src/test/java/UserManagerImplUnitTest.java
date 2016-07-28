import com.workfront.internship.business.HashManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Workfront on 7/27/2016.
 */
public class UserManagerImplUnitTest {
    private User user;
    private UserManager userManager;
    DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        user = getRandomUser();
        userManager = new UserManagerImpl(dataSource);

    }


    @After
    public void tearDown() {

    }

    @Test
    public void createAccount_ValidUser_getHash() throws NoSuchAlgorithmException {

        String expectedPassword = HashManager.getHash(user.getPassword());

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        Whitebox.setInternalState(userManager, "userDao", userDao);

        userManager.createAccount(user);

        String actualPassword = user.getPassword();
        assertEquals("Unable to hash password", actualPassword, expectedPassword);
    }

    @Test
    public void createAccount_validUser_notInserted() {


        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        AddressDao addressDao = Mockito.mock(AddressDaoImpl.class);

        Whitebox.setInternalState(userManager, "userDao", userDao);


        userManager.createAccount(user);


        Mockito.verify(userDao).insertUser(user);
        Mockito.verify(addressDao, Mockito.never()).insertAddress(any(Address.class));

    }

    @Test
    public void createAccount_validUser_Inserted() {


        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        AddressDao addressDao = Mockito.mock(AddressDaoImpl.class);

        Whitebox.setInternalState(userManager, "userDao", userDao);
        Whitebox.setInternalState(userManager, "addressDao", addressDao);
        when(userDao.insertUser(user)).thenReturn(10);
        userManager.createAccount(user);


        Mockito.verify(addressDao).insertAddress(any(Address.class));

    }

    @Test(expected = RuntimeException.class)
    public void createAccount_invalidUser() {

        user.setEmail("invalidEmail");

        userManager.createAccount(user);
    }
    @Test(expected = RuntimeException.class)
    public void login_validUsername_validPassword_returned_null_user(){

        user.setPassword(HashManager.getHash(user.getPassword()));
        UserDao userDao = Mockito.mock(UserDaoImpl.class);


        Whitebox.setInternalState(userManager, "userDao", userDao);


        userManager.login(user.getUsername(), user.getPassword());

        Mockito.verify(userDao).getUserByUsername(user.getUsername());


    }
    @Test
    public void login_validUsername_validPassword_returned_notNull_user(){
        String notHashedPassword = user.getPassword();
        user.setPassword(HashManager.getHash(user.getPassword()));
        System.out.println("pasword   " + user.getPassword());
        System.out.println("pasword   " + notHashedPassword);
        UserDao userDao = Mockito.mock(UserDaoImpl.class);


        Whitebox.setInternalState(userManager, "userDao", userDao);
        when(userDao.getUserByUsername(any(String.class))).thenReturn(user);

        User user1 = userManager.login(user.getUsername(), notHashedPassword);

        doAssertion(user, user1);


    }
    @Test(expected = RuntimeException.class)
    public void login_notValid_username_or_password(){
        User user1 = new User();
        userManager.login(user1.getUsername(), user1.getPassword());
    }
    @Test
    public void editProfile_updateUser_and_getShippingAddressByUserID_is_called(){

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        AddressDao addressDao = Mockito.mock(AddressDaoImpl.class);

        Whitebox.setInternalState(userManager, "userDao", userDao);
        Whitebox.setInternalState(userManager, "addressDao", addressDao);


        userManager.editProfile(user);


        Mockito.verify(userDao).updateUser(user);
        Mockito.verify(addressDao).getShippingAddressByUserID(user.getUserID());

    }

    @Test
    public void editProfile_shippingAddress_added(){
        User updatedUser = getRandomUser();
        Address newAddress = new Address();
        newAddress.setAddress("newAddress");
        updatedUser.getShippingAddresses().add(newAddress);

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        AddressDao addressDao = Mockito.mock(AddressDaoImpl.class);

        Whitebox.setInternalState(userManager, "userDao", userDao);
        Whitebox.setInternalState(userManager, "addressDao", addressDao);
        when(addressDao.getShippingAddressByUserID(user.getUserID())).thenReturn(user.getShippingAddresses());

        userManager.editProfile(updatedUser);


        Mockito.verify(addressDao).insertAddress(any(Address.class));
        Mockito.verify(addressDao, Mockito.never()).deleteAddressesByAddressID(any(Integer.class));

    }
    @Test
    public void editProfile_shippingAddress_removed(){
        User updatedUser = getRandomUser();
        Address newAddress = new Address();
        newAddress.setAddress("newAddress");
        user.getShippingAddresses().add(newAddress);

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        AddressDao addressDao = Mockito.mock(AddressDaoImpl.class);

        Whitebox.setInternalState(userManager, "userDao", userDao);
        Whitebox.setInternalState(userManager, "addressDao", addressDao);
        when(addressDao.getShippingAddressByUserID(user.getUserID())).thenReturn(user.getShippingAddresses());

        userManager.editProfile(updatedUser);


        Mockito.verify(addressDao, Mockito.never()).insertAddress(any(Address.class));
        Mockito.verify(addressDao).deleteAddressesByAddressID(any(Integer.class));

    }
    @Test(expected = RuntimeException.class)
    public void editProfile_notValidUser(){
        User user1 = new User();
        userManager.editProfile(user1);
    }
    @Test
    public void deleteAccount_validUser(){

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        Whitebox.setInternalState(userManager, "userDao", userDao);

        userManager.deleteAccount(user);
        Mockito.verify(userDao).deleteUser(any(Integer.class));
    }
    @Test(expected = RuntimeException.class)
    public void deleteAccount_notValidUser(){
        User user1 = new User();
        userManager.deleteAccount(user1);
    }





    private User getRandomUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        Address address = new Address();
        address.setAddress("someAddress");
        List<Address> addressess = new ArrayList<>();
        addressess.add(address);
        user.setShippingAddresses(addressess);
        return user;
    }
    private void doAssertion(User user, User user1){

        assertEquals(user.getUserID(), user1.getUserID());
        assertEquals(user.getFirstname(), user1.getFirstname());
        assertEquals(user.getLastname(), user1.getLastname());
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(user.getPassword(), user1.getPassword());
        assertEquals(user.getPhone(), user1.getPhone());
        assertEquals(user.getAccessPrivilege(), user1.getAccessPrivilege());
        assertEquals(user.getConfirmationStatus(), user1.getConfirmationStatus());
    }
}
