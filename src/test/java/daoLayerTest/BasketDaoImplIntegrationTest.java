package daoLayerTest;

import com.workfront.internship.common.Basket;
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

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class BasketDaoImplIntegrationTest {

    Basket basket;
    User user;
    int lastInsertedIndex;
    @Autowired
    BasketDao basketDao;
    @Autowired
    UserDao userDao;

    @Before
    public void setUpDB() throws SQLException, IOException{

        user = TestHelper.getTestUser();

        int userId = userDao.insertUser(user);
        user.setUserID(userId);
        basket = TestHelper.getTestBasket();
        basket.setUserID(user.getUserID());

        lastInsertedIndex = basketDao.insertBasket(basket);

    }

    @After
    public void tearDown() {
        basketDao.deleteAllBaskets();
        userDao.deleteAllUsers();
    }


    @Test
    public void insertBasket() {

        Basket basket2 = basketDao.getBasket(basket.getBasketID());

        doAssertion(basket2, basket);

    }
   @Test
    public void getCurrentBasket() {

        Basket basket1 = basketDao.getCurrentBasket(user.getUserID());

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

        List<Basket> baskets1 = basketDao.getAllBaskets();


                doAssertion(baskets1.get(0), basket);

    }

    private void doAssertion(Basket basket, Basket basket1){
        assertEquals(basket.getUserID(), basket1.getUserID());
        assertEquals(basket.getBasketID(), basket1.getBasketID());
        assertEquals(basket.getTotalPrice(), basket1.getTotalPrice());
        assertEquals(basket.getBasketStatus(), basket1.getBasketStatus());

    }
}
