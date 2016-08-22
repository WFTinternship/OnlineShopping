package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 01.08.2016.
 */
public class SaleManagerImplTest {
    private Basket basket;
    private User user;

    private CreditCard creditCard;
    private Sale sale;
    private BasketManager basketManager;
    private CreditcardManager creditcardManager;
    private UserManager userManager;

    private SalesManager salesManager;
    private LegacyDataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = LegacyDataSource.getInstance();
        basketManager = new BasketManagerImpl();
        Whitebox.setInternalState(basketManager, "dataSource", dataSource);
        userManager = new UserManagerImpl();
        Whitebox.setInternalState(userManager, "dataSource", dataSource);
        creditcardManager = new CreditcardManagerImpl();
        Whitebox.setInternalState(creditcardManager, "dataSource", dataSource);
        salesManager = new SalesManagerImpl();
        Whitebox.setInternalState(salesManager, "dataSource", dataSource);

        user = getTestUser();
        userManager.createAccount(user);

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
        Address address = new Address();
        address.setAddress("someAddress");
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        user = new User();
        user.setFirstname("Anahit").
                setLastname("galstyan").
                setUsername("anigal").
                setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").
                setConfirmationStatus(true).
                setAccessPrivilege("user").
                setShippingAddresses(addresses);
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
}
