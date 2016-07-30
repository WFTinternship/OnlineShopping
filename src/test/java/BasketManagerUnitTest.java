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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class BasketManagerUnitTest {
    private User user;
    private Basket basket;
    private OrderItem orderItem;
    Product product;
    private BasketManager basketManager;
    DataSource dataSource;
    BasketDao basketDao;
    OrderItemDao orderItemDao;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        product = getRandomProduct();
        basket = getRandomBasket();
        user = getRandomUser();
        orderItem = getRandomOrderItem();

        basketManager = new BasketManagerImpl(dataSource);
        basketDao = Mockito.mock(BasketDaoImpl.class);
        orderItemDao = Mockito.mock(OrderItemDaoImpl.class);
        Whitebox.setInternalState(basketManager, "basketDao", basketDao);
        Whitebox.setInternalState(basketManager, "orderItemDao", orderItemDao);

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
    @Test
    public void addToBasket_already_exists(){

        when(orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID())).thenReturn(orderItem);

        basketManager.addToBasket(user, product, 5);
        Mockito.verify(orderItemDao, Mockito.never()).insertOrderItem(any(OrderItem.class));
        Mockito.verify(orderItemDao).updateOrderItem(any(OrderItem.class));

    }
    @Test
    public void addToBasket_does_not_exist(){

        when(orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID())).thenReturn(null);

        basketManager.addToBasket(user, product, 5);
        Mockito.verify(orderItemDao).insertOrderItem(any(OrderItem.class));
        Mockito.verify(orderItemDao, Mockito.never()).updateOrderItem(any(OrderItem.class));

    }
    @Test
    public void addToBasket_null_basket(){

        user.setBasket(null);

        basketManager.addToBasket(user, product, 5);
        Mockito.verify(basketDao).getCurrentBasket(user.getUserID());
    }
    @Test(expected = RuntimeException.class)
    public void addToBasket_invalid_entry(){
        basketManager.addToBasket(user, null, 5);
    }

    @Test
    public void showItemsInBasket_is_not_empty(){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        basket.setOrderItems(orderItems);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(user.getBasket());
        when(orderItemDao.getOrderItemByBasketID(basket.getBasketID())).thenReturn(orderItems);

        orderItems = basketManager.showItemsInBasket(user);

        doAssertion(orderItem, orderItems.get(0));

    }

    @Test
    public void showItemsInBasket_is_empty(){
        List<OrderItem> orderItems = new ArrayList<>();

        basket.setOrderItems(orderItems);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(null);

        orderItems = basketManager.showItemsInBasket(user);

        Mockito.verify(orderItemDao, Mockito.never()).getOrderItemByBasketID(any(Integer.class));
        assertEquals(true, orderItems.isEmpty());

    }
    @Test(expected = RuntimeException.class)
    public void showItemsInBasket_invalid_user(){

        user = null;
        basketManager.showItemsInBasket(user);

    }
    @Test
    public void getCurrentBasket(){

        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(basket);

        Basket basket1 = basketManager.getCurrentBasket(user);

        doAssertion(basket, basket1);

    }
    @Test
    public void getCurrentBasket_does_not_exist(){

        Basket emptyBasket = new Basket();
        emptyBasket.setBasketStatus("current").setTotalPrice(0.0).setUserID(user.getUserID()).setBasketID(5);
        when(basketDao.getCurrentBasket(user.getUserID())).thenReturn(null);

        Basket basket1 = basketManager.getCurrentBasket(user);
        basket1.setBasketID(5);

        Mockito.verify(basketDao).insertBasket(basket1);
        doAssertion(emptyBasket, basket1);

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

    private Basket getRandomBasket() {
        Basket basket = new Basket();
        basket.setTotalPrice(100).setBasketStatus("current").setBasketID(10);
        return basket;
    }
    private OrderItem getRandomOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(10).setBasketID(basket.getBasketID()).setProduct(product).setOrderItemID(1);
        return  orderItem;
    }
    private Product getRandomProduct(){
        product = new Product();
        product.setProductID(5).setName("hat");
        return product;
    }
    private User getRandomUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        user.setBasket(basket);
        return user;
    }
    private void doAssertion(Basket basket, Basket basket1){
        assertEquals(basket.getUserID(), basket1.getUserID());
        assertEquals(basket.getBasketID(), basket1.getBasketID());
        assertEquals(basket.getTotalPrice(), basket1.getTotalPrice());
        assertEquals(basket.getBasketStatus(), basket1.getBasketStatus());

    }
    private void doAssertion(OrderItem orderItem, OrderItem orderItem1){

        assertEquals(orderItem.getOrderItemID(), orderItem1.getOrderItemID());
        assertEquals(orderItem.getQuantity(), orderItem1.getQuantity());
        assertEquals(orderItem.getProduct().getProductID(), orderItem1.getProduct().getProductID());
        assertEquals(orderItem.getBasketID(), orderItem1.getBasketID());

    }
}
