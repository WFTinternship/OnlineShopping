package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import com.workfront.internship.spring.TestConfiguration;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Created by Anna Asmangulyan on 01.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class SaleManagerImplTest {
    private Basket basket;
    private User user;
    private Address address;

    private CreditCard creditCard;
    private Sale sale;
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private CreditcardManager creditcardManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SalesManager salesManager;
    @Autowired
    private AddressManager addressManager;



    @Before
    public void setUP() throws IOException, SQLException {

        user = getTestUser();
        userManager.createAccount(user);

        address = getTestAddress();
        addressManager.insertAddress(address);

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        user.setShippingAddresses(addresses);





        basket = getTestBasket();
        creditCard = getTestCreditCard();


        basketManager.createNewBasket(basket);
        creditcardManager.createCreditCard(creditCard);
        sale = getTestSale();

    }
    @After
    public void tearDown(){
        salesManager.deleteSaleBySaleID(sale.getSaleID());
        userManager.deleteAccount(user.getUserID());
        creditcardManager.deleteCreditCard(creditCard.getCardID());


    }
    @Test
    public void makeNewSale(){
        int id = salesManager.makeNewSale(sale);
        assertFalse(id == 0);
    }
    private Basket getTestBasket() {
        Basket basket = new Basket();
        basket.setUserID(user.getUserID()).setTotalPrice(100).setBasketStatus("current");
        return basket;
    }
    private User getTestUser() {
        user = new User();
        user.setFirstname("Anahit").
                setLastname("galstyan").
                setUsername("anigal").
                setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").
                setConfirmationStatus(true).
                setAccessPrivilege("user");

        return user;
    }

    private CreditCard getTestCreditCard(){
        creditCard = new CreditCard();
        creditCard.setBalance(1000).setBillingAddress("someAddress");
        return creditCard;
    }
    private Sale getTestSale(){
        sale = new Sale();
        Date date = new Date();
        sale.setUserID(user.getUserID()).
                setBasket(basket).
                setCreditCard(creditCard.getCardID()).
                setAddressID(user.getShippingAddresses().get(0).getAddressID()).
                setDate(new Timestamp(date.getTime()));
        return sale;

    }
    private Address getTestAddress(){
        address = new Address();
        address.setUserID(user.getUserID()).setAddress("someaddress");
        return address;
    }
}
