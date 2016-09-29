package daoLayerTest;

import com.workfront.internship.common.*;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

/**
 * Created by Administrator on 09.07.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class SaleDaoImplIntegrationTest {

    User user;
    Basket basket;
    CreditCard creditCard;
    Address address;
    Sale sale;
    int lastInsertedIndex = 0;
    @Autowired
    UserDao userDao;
    @Autowired
    BasketDao basketDao;
    @Autowired
    CreditCardDao creditCardDao;
    @Autowired
    AddressDao addressDao;
    @Autowired
    SaleDao saleDao;



    @Before
    public void setUpDB() throws SQLException, IOException {

        user = TestHelper.getTestUser();
        userDao.insertUser(user);

        basket = TestHelper.getTestBasket();
        basket.setUserID(user.getUserID());
        basketDao.insertBasket(basket);


        address = TestHelper.getTestAddress();
        address.setUserID(user.getUserID());
        addressDao.insertAddress(address);

        creditCard = TestHelper.getTestCreditCard();
        creditCardDao.insertCreditCard(creditCard);

        sale = new Sale();

        sale.setAddressID(address.getAddressID()).
                setBasket(basket).
                setCreditCard(creditCard.getCardID()).
                setUserID(user.getUserID()).
                setStatus("not delivered").setFullName(user.getFirstname());

        lastInsertedIndex = saleDao.insertSale(sale);

    }

    @After
    public void tearDown() {

        userDao.deleteAllUsers();
        addressDao.deleteAllAddresses();
        creditCardDao.deleteAllCreditCards();
        basketDao.deleteAllBaskets();
        saleDao.deleteAllSales();

    }

    @Test
    public void deleteSaleBySaleID(){

        saleDao.deleteSaleBySaleID(lastInsertedIndex);
        Sale sale1 = saleDao.getSaleBySaleID(lastInsertedIndex);
        assertNull(sale1);
    }
    @Test
    public void deletSaleByUserID(){

        saleDao.deletSaleByUserID(sale.getUserID());
        List<Sale> sales  = saleDao.getSales(sale.getUserID());
        assertEquals(true, sales.isEmpty());
    }
    @Test
    public void insertSale() {

        Sale sale2 = saleDao.getSaleBySaleID(lastInsertedIndex);

        doAssertion(sale2, sale);

    }
    @Test(expected = RuntimeException.class)
    public void insertSale_duplicate(){

        saleDao.insertSale(sale);
    }

    @Test
    public void getSaleBySaleID() {

        Sale sale1 = saleDao.getSaleBySaleID(lastInsertedIndex);

        doAssertion(sale, sale1);
    }

    @Test
    public void deleteAllSales(){

        saleDao.deleteAllSales();

        List<Sale> sales = saleDao.getAllSales();

        assertEquals(true, sales.isEmpty());
    }

    @Test
    public void updateSaleStatus() {

        sale.setStatus("delivered");
        //testing method
        saleDao.updateSaleStatus(sale.getSaleID(), "delivered");

        saleDao.getSaleBySaleID(sale.getSaleID());

        assertEquals("status is not changed", "delivered", sale.getStatus());


    }
    @Test
    public void getSales(){

        //testing method...
        List<Sale> sales = saleDao.getSales(user.getUserID());

        doAssertion(sale, sales.get(0));

    }
    @Test
    public void getAllSales(){

        //testing method...
        List<Sale> sales = saleDao.getAllSales();

        doAssertion(sale, sales.get(0));

    }

    private void doAssertion(Sale sale, Sale sale1){

        assertEquals(sale.getSaleID(), sale1.getSaleID());
        assertEquals(sale.getBasket().getBasketID(), sale1.getBasket().getBasketID());
        assertEquals(sale.getCreditCardID(), sale1.getCreditCardID());
        assertEquals(sale.getUserID(), sale1.getUserID());
       // assertEquals(sale.getDate(), sale1.getDate());
        assertEquals(sale.getAddressID(), sale1.getAddressID());

    }


    }
