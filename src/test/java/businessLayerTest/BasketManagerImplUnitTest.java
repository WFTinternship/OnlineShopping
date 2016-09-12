package businessLayerTest;

import com.workfront.internship.business.BasketManager;
import com.workfront.internship.business.BasketManagerImpl;
import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class BasketManagerImplUnitTest {
    private User user;
    private Basket basket;
    private OrderItem orderItem;
    Product product;
    private BasketManager basketManager;
   // LegacyDataSource dataSource;
    BasketDao basketDao;
    OrderItemDao orderItemDao;

    @Before
    public void setUP() throws IOException, SQLException {
        product = getTestProduct();
        basket = getTestBasket();
        user = getTestUser();
        orderItem = getTestOrderItem();

        basketManager = new BasketManagerImpl();
        basketDao = Mockito.mock(BasketDaoImpl.class);
        orderItemDao = Mockito.mock(OrderItemDaoImpl.class);
        Whitebox.setInternalState(basketManager, "basketDao", basketDao);
        Whitebox.setInternalState(basketManager, "orderItemDao", orderItemDao);

    }
    @After
    public void tearDown() {
        product = null;
        basket = null;
        orderItem = null;
        user = null;
        basketManager = null;
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
    @Test
    public void addToBasket_already_exists(){

        when(orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID())).thenReturn(orderItem);

      //  basketManager.addToBasket(user, product, 5);
        Mockito.verify(orderItemDao, Mockito.never()).insertOrderItem(any(OrderItem.class));
        Mockito.verify(orderItemDao).updateOrderItem(any(OrderItem.class));

    }

    @Test
    public void addToBasket_does_not_exist(){

        when(orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID())).thenReturn(null);

     //   basketManager.addToBasket(user, product, 5);
        Mockito.verify(orderItemDao).insertOrderItem(any(OrderItem.class));
        Mockito.verify(orderItemDao, Mockito.never()).updateOrderItem(any(OrderItem.class));

    }
    @Test
    public void addToBasket_null_basket(){

        user.setBasket(null);

     //   basketManager.addToBasket(user, product, 5);
        Mockito.verify(basketDao).getCurrentBasket(user.getUserID());
    }
    @Test(expected = RuntimeException.class)
    public void addToBasket_invalid_entry(){
    //    basketManager.addToBasket(user, null, 5);

    }
    @Test
    public void getBasket(){

        when(basketDao.getBasket(basket.getBasketID())).thenReturn(basket);

        Basket basket1 = basketManager.getBasket(basket.getBasketID());

        assertEquals("could not get a basket", basket, basket1);

    }

    @Test
    public void showItemsInBasket_is_not_empty(){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        basket.setOrderItems(orderItems);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(user.getBasket());
        when(orderItemDao.getOrderItemByBasketID(basket.getBasketID())).thenReturn(orderItems);

        orderItems = basketManager.showItemsInCurrentBasket(user);

        assertEquals(orderItem, orderItems.get(0));

    }

    @Test
    public void showItemsInBasket_is_empty(){
        List<OrderItem> orderItems = new ArrayList<>();

        basket.setOrderItems(orderItems);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(null);

        orderItems = basketManager.showItemsInCurrentBasket(user);

        Mockito.verify(orderItemDao, Mockito.never()).getOrderItemByBasketID(any(Integer.class));
        assertEquals(true, orderItems.isEmpty());

    }
    @Test
    public void showItemsInBasket_userBasket_null_current_basket_notNull(){
        user.setBasket(null);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
       // basket.setOrderItems(orderItems);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(basket);

        when(orderItemDao.getOrderItemByBasketID(basket.getBasketID())).thenReturn(orderItems);

        List<OrderItem> orderItems1 = basketManager.showItemsInCurrentBasket(user);

        assertEquals(orderItems.get(0), orderItems1.get(0));



    }
    @Test
    public void showItemsInBasket_userBasket_null_current_basket_Null(){
        user.setBasket(null);

        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(null);


        List<OrderItem> orderItems = basketManager.showItemsInCurrentBasket(user);
        Mockito.verify(orderItemDao, Mockito.never()).getOrderItemByBasketID(any(Integer.class));

        assertTrue(orderItems.isEmpty());



    }
    @Test(expected = RuntimeException.class)
    public void showItemsInBasket_invalid_user(){

        user = null;
        basketManager.showItemsInCurrentBasket(user);

    }
    @Test
    public void getCurrentBasket(){

        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(basket);

        Basket basket1 = basketManager.getCurrentBasket(user);

        assertEquals("could not get current basket", basket, basket1);

    }
    @Test
    public void getCurrentBasket_does_not_exist(){

        Basket emptyBasket = new Basket();
        emptyBasket.setBasketStatus("current").setTotalPrice(0.0).setUserID(user.getUserID()).setBasketID(5).setOrderItems(new ArrayList<OrderItem>());
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(null);

        Basket basket1 = basketManager.getCurrentBasket(user);

        basket1.setBasketID(5);

        Mockito.verify(basketDao).insertBasket(basket1);
        assertEquals("could not get current basket", emptyBasket, basket1);

    }
    @Test(expected = RuntimeException.class)
    public void getCurrentBasket_invalid_user(){
        basketManager.getCurrentBasket(null);
    }
    @Test
    public void deleteFromBasket(){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        user.getBasket().setOrderItems(orderItems);
        when(orderItemDao.getOrderItemByItemID(orderItem.getOrderItemID())).thenReturn(orderItem);

        basketManager.deleteFromBasket(user, orderItem.getOrderItemID());

        Mockito.verify(orderItemDao).deleteOrderItemByItemID(orderItem.getOrderItemID());
    }
    @Test(expected = RuntimeException.class)
    public void deleteFromBasket_invalid_entry(){
        basketManager.deleteFromBasket(null, 0);
    }
    @Test
    public void updateBasket(){
        basketManager.updateBasket(user, orderItem);

        Mockito.verify(orderItemDao).updateOrderItem(orderItem);
        Mockito.verify(orderItemDao).getOrderItemByBasketID(orderItem.getBasketID());

    }
    @Test(expected = RuntimeException.class)
    public void updateBasket_invalid_entry(){

        basketManager.updateBasket(null, orderItem);

    }

    private Basket getTestBasket() {
        Basket basket = new Basket();
        basket.setTotalPrice(100).setBasketStatus("current").setBasketID(10).setOrderItems(new ArrayList<OrderItem>());
        return basket;
    }
    private OrderItem getTestOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(10).setBasketID(basket.getBasketID()).setProduct(product).setOrderItemID(1);
        return  orderItem;
    }
    private Product getTestProduct(){
        product = new Product();
        product.setProductID(5).setName("hat");
        return product;
    }
    private User getTestUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        user.setBasket(basket);
        return user;
    }

}
