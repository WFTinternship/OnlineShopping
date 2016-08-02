package businessLayerTest;

import com.workfront.internship.business.HashManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.*;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserManagerImplUnitTest {
    private User user;
    private Product product;
    private UserDao userDao;
    private AddressDao addressDao;
    private UserManager userManager;
    DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {

        user = getTestUser();
        product = getTestProduct();
        userManager = new UserManagerImpl(dataSource);
        userDao = Mockito.mock(UserDaoImpl.class);
        addressDao = Mockito.mock(AddressDaoImpl.class);
        Whitebox.setInternalState(userManager, "userDao", userDao);
        Whitebox.setInternalState(userManager, "addressDao", addressDao);

    }

    @After
    public void tearDown() {
        user = null;
        product = null;
        userManager = null;
        userDao = null;
        addressDao = null;
    }

    @Test
    public void createAccount_ValidUser_getHash() throws NoSuchAlgorithmException {

        String expectedPassword = HashManager.getHash(user.getPassword());

        userManager.createAccount(user);

        String actualPassword = user.getPassword();
        assertEquals("Unable to hash password", actualPassword, expectedPassword);
    }

    @Test
    public void createAccount_validUser_notInserted() {
        userManager.createAccount(user);

        Mockito.verify(userDao).insertUser(user);
        Mockito.verify(addressDao, Mockito.never()).insertAddress(any(Address.class));

    }

    @Test
    public void createAccount_validUser_Inserted() {
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
    public void login_validUsername_validPassword_returned_null_user() {

        user.setPassword(HashManager.getHash(user.getPassword()));

        // testing method
        userManager.login(user.getUsername(), user.getPassword());

        Mockito.verify(userDao).getUserByUsername(user.getUsername());


    }

    @Test
    public void login_validUsername_validPassword_returned_notNull_user() {

        String notHashedPassword = user.getPassword();

        user.setPassword(HashManager.getHash(user.getPassword()));

        when(userDao.getUserByUsername(any(String.class))).thenReturn(user);

        User user1 = userManager.login(user.getUsername(), notHashedPassword);

        doAssertion(user, user1);


    }

    @Test(expected = RuntimeException.class)
    public void login_notValid_username_or_password() {
        User user1 = new User();
        userManager.login(user1.getUsername(), user1.getPassword());
    }

    @Test
    public void editProfile_updateUser_and_getShippingAddressByUserID_is_called() {

        userManager.editProfile(user);

        Mockito.verify(userDao).updateUser(user);
        Mockito.verify(addressDao).getShippingAddressByUserID(user.getUserID());

    }

    @Test
    public void editProfile_shippingAddress_added() {
        User updatedUser = getTestUser();
        Address newAddress = new Address();
        newAddress.setAddress("newAddress");
        updatedUser.getShippingAddresses().add(newAddress);

        when(addressDao.getShippingAddressByUserID(user.getUserID())).thenReturn(user.getShippingAddresses());

        userManager.editProfile(updatedUser);

        Mockito.verify(addressDao).insertAddress(any(Address.class));
        Mockito.verify(addressDao, Mockito.never()).deleteAddressesByAddressID(any(Integer.class));

    }

    @Test
    public void editProfile_shippingAddress_removed() {
        User updatedUser = getTestUser();
        Address newAddress = new Address();
        newAddress.setAddress("newAddress");
        user.getShippingAddresses().add(newAddress);

        when(addressDao.getShippingAddressByUserID(user.getUserID())).thenReturn(user.getShippingAddresses());

        userManager.editProfile(updatedUser);

        Mockito.verify(addressDao, Mockito.never()).insertAddress(any(Address.class));
        Mockito.verify(addressDao).deleteAddressesByAddressID(any(Integer.class));

    }

    @Test(expected = RuntimeException.class)
    public void editProfile_notValidUser() {
        User user1 = new User();
        userManager.editProfile(user1);
    }

    @Test
    public void deleteAccount_validUserID() {

        userManager.deleteAccount(user.getUserID());

        Mockito.verify(userDao).deleteUser(any(Integer.class));
    }

    @Test(expected = RuntimeException.class)
    public void deleteAccount_notValidUserID() {

        userManager.deleteAccount(-1);
    }

    @Test
    public void addToList() {

        Product product1 = getTestProduct();
        product1.setProductID(20);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);

        when(userDao.getWishlist(user.getUserID())).thenReturn(products);
        userManager.addToList(user, product);

        Mockito.verify(userDao).insertIntoWishlist(user.getUserID(), product.getProductID());

        assertEquals(products.get(0), user.getWishList().get(0));
        assertEquals(products.get(1), user.getWishList().get(1));

    }

    @Test(expected = RuntimeException.class)
    public void addToList_notValidUser() {

        userManager.addToList(null, product);
    }
    @Test(expected = RuntimeException.class)
    public void deleteFromList_invalid_entry() {

        userManager.deleteFromList(null, product);
    }

    @Test
    public void getList() {
        Product product1 = getTestProduct();
        product1.setProductID(20);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);

        when(userDao.getWishlist(user.getUserID())).thenReturn(products);

        List<Product> products1 = userManager.getList(user);

        assertEquals(products.get(0), products1.get(0));
        assertEquals(products.get(1), products1.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void getList_notValidUser() {
        userManager.getList(null);
    }

    @Test
    public void deleteFromList() {
        userManager.deleteFromList(user, product);
        verify(userDao).deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());
        verify(userDao).getWishlist(user.getUserID());
    }

    private User getTestUser() {
        User user = new User();
        user.setUserID(5).setFirstname("Anahit").setLastname("galstyan").
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

    private Product getTestProduct() {
        Product product = new Product();
        Category category = new Category();
        category.setCategoryID(1).setName("hat");
        product.setProductID(10).
                setName("baby hat").
                setPrice(50).setDescription("color:white").
                setShippingPrice(1).setQuantity(50).
                setCategory(category);

        return product;
    }

    private void doAssertion(User user, User user1) {
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
