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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class OrderItemDaoImplIntegrationTest {

    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BasketDao basketDao;

    OrderItem orderItem;
    Product product;
    Category category;
    User user;
    Basket basket;
    int lastInsertedIndex;
    int lastIsertedProductIndex;

    @Before
    public void setUpDB() throws SQLException, IOException {

        user = TestHelper.getTestUser();
        userDao.insertUser(user);
        basket = TestHelper.getTestBasket();
        basket.setUserID(user.getUserID());
        basketDao.insertBasket(basket);

        category = TestHelper.getTestCategory();
        categoryDao.insertCategory(category);
        product = TestHelper.getTestProduct();
        product.setCategory(category);
        lastIsertedProductIndex = productDao.insertProduct(product);

        orderItem = TestHelper.getTestOrderItem();
        orderItem.setProduct(product);
        orderItem.setBasketID(basket.getBasketID());
        lastInsertedIndex = orderItemDao.insertOrderItem(orderItem);

    }

    @After
    public void tearDown() {

        userDao.deleteAllUsers();
        categoryDao.deleteAllCategories();

    }

    @Test
    public void deleteOrderItemByItemID() {
        //testing method...
        orderItemDao.deleteOrderItemByItemID(lastInsertedIndex);

        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);

        assertNull(orderItem1);
    }

    @Test
    public void deleteOrderItemByProductID() {
        //testing method...
        orderItemDao.deleteOrderItemByProductID(orderItem.getProduct().getProductID());

        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);

        assertNull(orderItem1);
    }

    @Test
    public void insertOrderItem() {
        //testing method...
        OrderItem orderItem2 = orderItemDao.getOrderItemByItemID(orderItem.getOrderItemID());

        doAssertion(orderItem2, orderItem);

    }

//    @Test
//    public void getOrderItemByItemID() {
//        //testing method...
//        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);
//
//        doAssertion(orderItem, orderItem1);
//    }

    @Test
    public void deleteAllOrderItems() {
        //testing method...
        orderItemDao.deleteAllOrderItems();

        List<OrderItem> orderItems = orderItemDao.getAllOrderItems();

        assertEquals(true, orderItems.isEmpty());
    }

    @Test
    public void updateOrderItem() {

        orderItem.setQuantity(orderItem.getQuantity() + 3);
        //testing method...
        orderItemDao.updateOrderItem(orderItem);

        OrderItem orderItem1 = orderItemDao.getOrderItemByItemID(lastInsertedIndex);

        doAssertion(orderItem, orderItem1);

    }

    @Test
    public void getAllOrderItems() {
        //testing method...
        List<OrderItem> orderItems1 = orderItemDao.getAllOrderItems();

        doAssertion(orderItem, orderItems1.get(0));


    }

    @Test
    public void getOrderItemByBasketID() {
        //testing method...
        List<OrderItem> orderItems1 = orderItemDao.getOrderItemByBasketID(orderItem.getBasketID());

        doAssertion(orderItem, orderItems1.get(0));


    }

    private void doAssertion(OrderItem orderItem, OrderItem orderItem1) {

        assertEquals(orderItem.getOrderItemID(), orderItem1.getOrderItemID());
        assertEquals(orderItem.getQuantity(), orderItem1.getQuantity());
        assertEquals(orderItem.getProduct().getProductID(), orderItem1.getProduct().getProductID());
        assertEquals(orderItem.getBasketID(), orderItem1.getBasketID());

    }


}
