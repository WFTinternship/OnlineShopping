import com.workfront.internship.business.BasketManager;
import com.workfront.internship.business.BasketManagerImpl;
import com.workfront.internship.common.Basket;

import com.workfront.internship.dao.BasketDao;
import com.workfront.internship.dao.BasketDaoImpl;

import com.workfront.internship.dao.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BasketManagerUnitTest {

    private Basket basket;
    private BasketManager basketManager;
    DataSource dataSource;
    BasketDao basketDao;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        basket = getRandomBasket();
        basketManager = new BasketManagerImpl(dataSource);
        basketDao = Mockito.mock(BasketDaoImpl.class);
        Whitebox.setInternalState(basketManager, "basketDao", basketDao);

    }
    @After
    public void tearDown() {

    }
    @Test
    public void createNewBasket(){
        when(basketDao.insertBasket(basket)).thenReturn(10);
        int result = basketManager.createNewBasket(basket);
        assertEquals("basket was not created", 10, result);
    }
    @Test(expected = RuntimeException.class)
    public void createNewBasket_nullEntry(){
        basketManager.createNewBasket(null);
    }









    private Basket getRandomBasket() {
        Basket basket = new Basket();
        basket.setTotalPrice(100).setBasketStatus("current").setBasketID(10);
        return basket;
    }
    private void doAssertion(Basket basket, Basket basket1){
        assertEquals(basket.getUserID(), basket1.getUserID());
        assertEquals(basket.getBasketID(), basket1.getBasketID());
        assertEquals(basket.getTotalPrice(), basket1.getTotalPrice());
        assertEquals(basket.getBasketStatus(), basket1.getBasketStatus());

    }
}
