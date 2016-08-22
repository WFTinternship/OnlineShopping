package daoLayerTest;

import com.workfront.internship.common.*;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

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
public class TestSaleDaoImpl {
    LegacyDataSource dataSource;
    User user = null;
    Basket basket = null;
    CreditCard creditCard = null;
    Address address = null;
    Sale sale = null;
    int lastInsertedIndex = 0;
    UserDao userDao;
    BasketDao basketDao;
    CreditCardDao creditCardDao;
    AddressDao addressDao;
    SaleDao saleDao;

    @Before
    public void setUpDB() throws SQLException, IOException {
        dataSource = LegacyDataSource.getInstance();
        userDao = new UserDaoImpl();
        Whitebox.setInternalState(userDao, "dataSource", dataSource);
        basketDao = new BasketDaoImpl();
        Whitebox.setInternalState(basketDao, "dataSource", dataSource);
        creditCardDao = new CreditCardDaoImpl();
        Whitebox.setInternalState(creditCardDao, "dataSource", dataSource);

        addressDao = new AddressDaoImpl();
        Whitebox.setInternalState(addressDao, "dataSource", dataSource);
        saleDao = new SaleDaoImpl();

        Whitebox.setInternalState(saleDao, "dataSource", dataSource);
        user = getRandomUser();
        userDao.insertUser(user);

        sale = getRandomSale();
        System.out.println(sale.getUserID() + " aaaaaaaaa " + sale.getAddressID());
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
    public void insertBasket() {
        Basket basket1 = getRandomBasket();
        basketDao.insertBasket(basket1);

        Sale sale1 = getRandomSale();

        sale1.setBasket(basket1);

        lastInsertedIndex = saleDao.insertSale(sale1);

        Sale sale2 = saleDao.getSaleBySaleID(lastInsertedIndex);
        doAssertion(sale2, sale1);

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
    public void updateSale() {

        Basket basket2 = getRandomBasket();
        basketDao.insertBasket(basket2);


        sale.setBasket(basket2);
        saleDao.updateSale(sale);

        Sale sale1 = saleDao.getSaleBySaleID(lastInsertedIndex);

        doAssertion(sale, sale1);

    }
    @Test
    public void getSales(){

        saleDao.deleteAllSales();

        Sale sale = getRandomSale();
        Sale sale1 = getRandomSale();
        saleDao.insertSale(sale);
        saleDao.insertSale(sale1);
        List<Sale> sales = saleDao.getSales(user.getUserID());

        doAssertion(sale, sales.get(0));
        doAssertion(sale1, sales.get(1));


    }
    @Test
    public void getAllSales(){

        saleDao.deleteAllSales();

        List<Sale> sales = new ArrayList<>();

        Sale sale = getRandomSale();
        Sale sale1 = getRandomSale();
        saleDao.insertSale(sale);
        saleDao.insertSale(sale1);
        sales = saleDao.getAllSales();

        doAssertion(sale, sales.get(0));
        doAssertion(sale1, sales.get(1));

    }
    private Basket getRandomBasket() {
        Basket basket = new Basket();
        basket.setUserID(user.getUserID()).setTotalPrice(100).setBasketStatus("current");
        return basket;
    }
    private User getRandomUser() {
        Random random = new Random();
        int x = random.nextInt(100000);
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal" + x).setPassword("anahitgal85").setEmail("galstyan" + x + "@gmailgom").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }

    private Sale getRandomSale(){
        java.util.Date date= new java.util.Date();
        sale = new Sale();

        basket = getRandomBasket();
        basketDao.insertBasket(basket);
        creditCard = getRandomCreditCard();
        creditCardDao.insertCreditCard(creditCard);
        address = getRandomAddress();
        address.setUserID(user.getUserID());
        addressDao.insertAddress(address);
        sale.setBasket(basket).setAddressID(address.getAddressID()).
                setCreditCard(creditCard.getCardID()).
                setUserID(user.getUserID()).
                setDate(new Timestamp(date.getTime()));
        return  sale;
    }
    private CreditCard getRandomCreditCard() {
        Random ran = new Random();
        int x = ran.nextInt(1000) + 10;
        CreditCard creditCard = new CreditCard();
        creditCard.setBillingAddress("randomBillingAddress" + x).setBalance(1000);
        return creditCard;
    }
    private Address getRandomAddress() {
        Random ran = new Random();
        int x = ran.nextInt(1000) + 10;
        Address address = new Address();
        address.setAddress("randomAddress" + x);
        return address;
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
