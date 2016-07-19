
import com.workfront.internship.common.*;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;


public class TestOrderItemDaoImpl {
    DataSource dataSource;
    OrderItem orderItem = null;
    Product product = null;
    Category category = null;
    User user = null;
    Basket basket = null;
    int lastInsertedIndex = 0;
    int lastIsertedProductIndex = 0;
    OrderItemDao orderItemDao;
    ProductDao productDao;
    CategoryDao categoryDao;
    UserDao userDao;
    BasketDao basketDao;

    @Before
    public void setUpDB() throws SQLException, IOException {
        dataSource = DataSource.getInstance();
        orderItemDao = new OrderItemDaoImpl(dataSource);
        productDao = new ProductDaoImpl(dataSource);
        categoryDao = new CategoryDaoImpl(dataSource);
        userDao = new UserDaoImpl(dataSource);
        basketDao = new BasketDaoImpl(dataSource);

        user = getRandomUser();
        userDao.insertUser(user);
        basket = getRandomBasket();
        basketDao.insertBasket(basket);

        category = getRandomCategory();
        categoryDao.insertCategory(category);
        product = getRandomProduct();
        lastIsertedProductIndex = productDao.insertProduct(product);

        orderItem = getRandomOrderItem();
        orderItem.setProduct(product);
        lastInsertedIndex = orderItemDao.insertOrderItem(orderItem);

    }

    @After
    public void tearDown() {

      // userDao.deleteAllUsers();
      // categoryDao.deleteAllCategories();
      //  productDao.deleteAllProducts();
      // orderItemDao.deleteAllOrderItems();
    }

    @Test
    public void deleteOrderItemByItemID(){

        orderItemDao.deleteOrderItemByItemID(lastInsertedIndex);
        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);
        assertNull(orderItem1);
    }
    @Test
    public void deleteOrderItemByProductID(){

        orderItemDao.deleteOrderItemByProductID(orderItem.getProduct().getProductID());
        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);
        assertNull(orderItem1);
    }

    @Test
    public void insertOrderItem() {
        Product product1 = getRandomProduct();
        productDao.insertProduct(product1);
        OrderItem orderItem1 = getRandomOrderItem();
        orderItem1.setProduct(product1);

        int insertindex = orderItemDao.insertOrderItem(orderItem1);


        OrderItem orderItem2 = orderItemDao.getOrderItemByItemID(insertindex);
        doAssertion(orderItem2, orderItem1);

    }

    @Test
    public void getOrderItemByItemID() {

        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);

        doAssertion(orderItem, orderItem1);
    }

    @Test
    public void deleteAllOrderItems(){

        orderItemDao.deleteAllOrderItems();

        List<OrderItem> orderItems = orderItemDao.getAllOrderItems();

        assertEquals(true, orderItems.isEmpty());
    }

    @Test
    public void updateOrderItem() {

        orderItem.setQuantity(orderItem.getQuantity() + 3);
        orderItemDao.updateOrderItem(orderItem);

        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);

        doAssertion(orderItem, orderItem1);

    }
    @Test
    public void getAllOrderItems(){

        orderItemDao.deleteAllOrderItems();

        List<OrderItem> orderItems1 = new ArrayList<>();

        OrderItem orderItem = getRandomOrderItem();
        orderItem.setProduct(product);
        orderItemDao.insertOrderItem(orderItem);

        Product product1 = getRandomProduct();
        product1.setName("newProductName1");
        productDao.insertProduct(product1);

        OrderItem orderItem1 = getRandomOrderItem();
        orderItem1.setProduct(product1);
        orderItemDao.insertOrderItem(orderItem1);

        orderItems1 = orderItemDao.getAllOrderItems();

        doAssertion(orderItem, orderItems1.get(0));
        doAssertion(orderItem1, orderItems1.get(1));

    }
    @Test
    public void getOrderItemByBasketID(){

        orderItemDao.deleteAllOrderItems();

        List<OrderItem> orderItems1 = new ArrayList<>();

        OrderItem orderItem = getRandomOrderItem();
        orderItem.setProduct(product);
        orderItemDao.insertOrderItem(orderItem);

        Product product1 = getRandomProduct();
        product1.setName("newProductName2");
        productDao.insertProduct(product1);

        OrderItem orderItem1 = getRandomOrderItem();
        orderItem1.setProduct(product1);
        orderItemDao.insertOrderItem(orderItem1);

        orderItems1 = orderItemDao.getOrderItemByBasketID(orderItem.getBasketID());

        doAssertion(orderItem, orderItems1.get(0));
        doAssertion(orderItem1, orderItems1.get(1));

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
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal" + x).setPassword("anahitgal85").setEmail("galstyan@gmailgom" + x + "@gmailgom").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }
    private OrderItem getRandomOrderItem() {
        orderItem = new OrderItem();
        orderItem.setQuantity(10).setBasketID(basket.getBasketID());
        return  orderItem;
    }

    private Category getRandomCategory(){
        Random random = new Random();
        int x = random.nextInt(100000);
        category = new Category();
        category.setName("hat" +x);
        return category;
    }

    private Product getRandomProduct(){
        Random random = new Random();
        int x = random.nextInt(100000);
        product = new Product();
        product.setName("baby hat" + x).setPrice(50).setDescription("color:white").setShippingPrice(1).setQuantity(50).setCategory(category);
        return product;
    }
    private void doAssertion(OrderItem orderItem, OrderItem orderItem1){

        assertEquals(orderItem.getOrderItemID(), orderItem1.getOrderItemID());
        assertEquals(orderItem.getQuantity(), orderItem1.getQuantity());
        assertEquals(orderItem.getProduct().getProductID(), orderItem1.getProduct().getProductID());
        assertEquals(orderItem.getBasketID(), orderItem1.getBasketID());

    }


}
