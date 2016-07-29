package com.workfront.internship.business;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/25/2016.
 */
public class BasketManagerImpl implements BasketManager {

    private DataSource dataSource;
    private UserDao userDao;
    private OrderItemDao orderItemDao;
    private BasketDao basketDao;


    public BasketManagerImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);
        orderItemDao = new OrderItemDaoImpl(dataSource);
        basketDao = new BasketDaoImpl(dataSource);


    }

    @Override
    public int createNewBasket(Basket basket) {
        if (basket == null)
            throw new RuntimeException("invalid basket!");
        int id = basketDao.insertBasket(basket);
        return id;


    }

    @Override
    public void addToBasket(User user, Product product, int quantity) {
        if(user == null || product == null || quantity <= 0)
            throw new RuntimeException("invalid entry!");
        if (user.getBasket() == null) {
            Basket basket = getCurrentBasket(user);
            user.setBasket(basket);
        }
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setProduct(product).setBasketID(user.getBasket().getBasketID()).setQuantity(quantity);
        OrderItem oldOrderItem = orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID());
        if (oldOrderItem == null)
            orderItemDao.insertOrderItem(newOrderItem);
          else
            orderItemDao.updateOrderItem(newOrderItem);

    }

    @Override
    public List<OrderItem> showItemsInBasket(Basket basket) {
        if (basket == null)
            throw new RuntimeException("invalid basket");
        List<OrderItem> orderItems = orderItemDao.getOrderItemByBasketID(basket.getBasketID());
        basket.setOrderItems(orderItems);
        return orderItems;
    }

    @Override
    public Basket getCurrentBasket(User user) {
        if(user == null)
            throw new RuntimeException("invalid user");
        Basket basket = basketDao.getCurrentBasket(user.getUserID());
        if (basket == null) {
            basket = new Basket();
            basket.setUserID(user.getUserID()).setTotalPrice(0.0).setBasketStatus("current");
            basketDao.insertBasket(basket);
        }
        return basket;
    }

    @Override
    public void deleteFromBasket(int itemId) {
        if(itemId <= 0)
            throw new RuntimeException("invalid id");
        orderItemDao.deleteOrderItemByItemID(itemId);
    }

    @Override
    public void updateBasket(OrderItem orderItem) {
        if(orderItem ==null )
            throw new RuntimeException("invalid entry!");
        orderItemDao.updateOrderItem(orderItem);

    }



}
