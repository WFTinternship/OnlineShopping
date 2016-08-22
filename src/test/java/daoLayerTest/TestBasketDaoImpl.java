package daoLayerTest;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;


public class TestBasketDaoImpl  {
    LegacyDataSource dataSource;
    Basket basket = null;
    User user = null;
    int lastInsertedIndex = 0;
    BasketDao basketDao;
    UserDao userDao;
    @Before
    public void setUpDB() throws SQLException, IOException{
        dataSource = LegacyDataSource.getInstance();
        user = getRandomUser();
        basketDao = new BasketDaoImpl();
        Whitebox.setInternalState(basketDao, "dataSource", dataSource);
        userDao = new UserDaoImpl();
        Whitebox.setInternalState(userDao, "dataSource", dataSource);
        userDao.insertUser(user);
        basket = getRandomBasket();

        lastInsertedIndex = basketDao.insertBasket(basket);
        basket.setBasketID(lastInsertedIndex);
    }

    @After
    public void tearDown() {

        basketDao.deleteAllBaskets();
        userDao.deleteAllUsers();
    }

    @Test
    public void deleteBasketByUserId(){

        basketDao.deleteBasketByUserId(basket.getUserID());
        Basket basket1 = basketDao.getBasket(lastInsertedIndex);
        assertNull(basket1);
    }

    @Test
    public void insertBasket() {

        Basket basket1 = getRandomBasket();
        int insertindex = basketDao.insertBasket(basket1);
        basket1.setBasketID(insertindex);

        Basket basket2 = basketDao.getBasket(insertindex);

        doAssertion(basket2, basket1);

    }
    @Test
    public void getCurrentBasket() {

        Basket basket1 = basketDao.getCurrentBasket(basket.getUserID());

        doAssertion(basket, basket1);

    }
    @Test
    public void getBasket() {

        Basket basket1 = basketDao.getBasket(lastInsertedIndex);

        doAssertion(basket, basket1);

    }

    @Test
    public void updateBasket() {


        basket.setTotalPrice(basket.getTotalPrice() + 1000).setBasketID(lastInsertedIndex).setBasketStatus(basket.getBasketStatus()).setUserID(basket.getUserID());

        basketDao.updateBasket(basket);

        Basket basket1 = basketDao.getBasket(lastInsertedIndex);

        doAssertion(basket, basket1);

    }

    @Test
    public void deleteBasketByBasketID() {

        basketDao.deleteBasketByBasketID(lastInsertedIndex);
        Basket basket1 = basketDao.getBasket(lastInsertedIndex);
        assertNull(basket1);
    }
    @Test
    public void deleteAllBaskets(){
        basketDao.deleteAllBaskets();

        List<Basket> baskets = basketDao.getAllBaskets();

        assertEquals(true, baskets.isEmpty());
    }
    @Test
    public void getAllBaskets(){

            basketDao.deleteAllBaskets();

            List<Basket> baskets = new ArrayList<>();


            Basket basket = getRandomBasket();
            basketDao.insertBasket(basket);
            baskets.add(basket);

            Basket basket1 = getRandomBasket();
            basket1.setBasketStatus("saled");
            basketDao.insertBasket(basket1);
            baskets.add(basket1);

            List<Basket> baskets1 = basketDao.getAllBaskets();

            for (int i = 0; i < baskets1.size(); i++) {
                doAssertion(baskets.get(i), baskets1.get(i));
            }
    }

    private Basket getRandomBasket() {
        Basket basket = new Basket();
        basket.setUserID(user.getUserID()).setTotalPrice(100).setBasketStatus("current");
        return basket;
    }
    private User getRandomUser() {
        user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal").setPassword("anahitgal85").setEmail("galstyan@gmailgom").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }
    private void doAssertion(Basket basket, Basket basket1){
        assertEquals(basket.getUserID(), basket1.getUserID());
        assertEquals(basket.getBasketID(), basket1.getBasketID());
        assertEquals(basket.getTotalPrice(), basket1.getTotalPrice());
        assertEquals(basket.getBasketStatus(), basket1.getBasketStatus());

    }
}
