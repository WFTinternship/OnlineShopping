package businessLayerTest;

import com.workfront.internship.business.HashManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.*;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class UserManagerImplUnitTest {
    private User user;
    private Product product;

    private UserDao userDao;

    private AddressDao addressDao;

    private UserManager userManager;


    @Before
    public void setUP() throws IOException, SQLException {

        user = getTestUser();
        product = getTestProduct();
        userManager = new UserManagerImpl();
        addressDao = Mockito.mock(AddressDaoImpl.class);
        userDao = Mockito.mock(UserDaoImpl.class);

        Whitebox.setInternalState(userManager, "addressDao", addressDao);
        Whitebox.setInternalState(userManager, "userDao", userDao);

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
    public void createAccount_validUser(){
        userManager.createAccount(user);
        //testing method... inserting a valid user
        Mockito.verify(userDao).insertUser(user);


    }

    @Test(expected = RuntimeException.class)
    public void createAccount_invalidUser() {
        user.setEmail("invalidEmail");
        //testing method... user has invalid email
        userManager.createAccount(user);
    }

    @Test
    public void login_validUsername_validPassword_returned_null_user() {
        when(userDao.getUserByUsername(any(String.class))).thenReturn(null);
        // testing method... when can't get user from db... returned user is null
        User user1 = userManager.login(user.getUsername(), user.getPassword());
        assertNull(user1);


    }

    @Test
    public void login_validUsername_validPassword_returned_notNull_user() {

        String notHashedPassword = user.getPassword();

        user.setPassword(HashManager.getHash(user.getPassword()));

        when(userDao.getUserByUsername(any(String.class))).thenReturn(user);
        //testing method... user is returned from db
        User user1 = userManager.login(user.getUsername(), notHashedPassword);

        doAssertion(user, user1);


    }

    @Test(expected = RuntimeException.class)
    public void login_notValid_username_or_password() {
        User user1 = new User();
        //testing method... ehwn username or password is invalid
        userManager.login(user1.getUsername(), user1.getPassword());
    }

    @Test
    public void editProfile_updateUser_and_getShippingAddressByUserID_is_called() {

        userManager.editProfile(user);
        //testing method...
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
        //testing method... when new shipping addresses are added
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
        //testing method... when some shipping addresses are removed
        userManager.editProfile(updatedUser);

        Mockito.verify(addressDao, Mockito.never()).insertAddress(any(Address.class));
        Mockito.verify(addressDao).deleteAddressesByAddressID(any(Integer.class));

    }

    @Test(expected = RuntimeException.class)
    public void editProfile_notValidUser() {
        User user1 = new User();
        //testing method... invalid user
        userManager.editProfile(user1);
    }

    @Test
    public void deleteAccount_validUserID() {
        //testing method...
        userManager.deleteAccount(user.getUserID());

        Mockito.verify(userDao).deleteUser(any(Integer.class));
    }

    @Test(expected = RuntimeException.class)
    public void deleteAccount_notValidUserID() {
        //testing method... invalid id
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
        //testing method... inserting a product into a wishlist
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
