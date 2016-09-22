package com.workfront.internship.business;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class BasketManagerImpl implements BasketManager {


    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private BasketDao basketDao;


    @Override
    public int createNewBasket(Basket basket) {
        if (basket == null)
            throw new RuntimeException("invalid basket!");
        int id = basketDao.insertBasket(basket);
        return id;


    }

    @Override
    public int addToBasket(User user, Product product, String sizeOption, int quantity) {
        if (user == null || product == null || quantity <= 0 || sizeOption == null || sizeOption == "")
            throw new RuntimeException("invalid entry!");
        if (user.getBasket() == null) {
            Basket basket = getCurrentBasket(user);
            user.setBasket(basket);
        }
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setProduct(product).setBasketID(user.getBasket().getBasketID()).setQuantity(quantity).setSizeOption(sizeOption);
        OrderItem oldOrderItem = orderItemDao.getOrderItemByProductIDBasketIDSizeOption(product.getProductID(), user.getBasket().getBasketID(), sizeOption);
        if (oldOrderItem == null) {
            orderItemDao.insertOrderItem(newOrderItem);
            user.setBasket(getCurrentBasket(user));
        }

        else {
            oldOrderItem.setQuantity(oldOrderItem.getQuantity()+quantity);
            orderItemDao.updateOrderItem(oldOrderItem);
            user.setBasket(getCurrentBasket(user));
        }
return newOrderItem.getOrderItemID();
    }

    @Override
    public List<OrderItem> showItemsInCurrentBasket(User user) {
        if (user == null)
            throw new RuntimeException("invalid user");
        List<OrderItem> orderItems = new ArrayList<>();
        //if (user.getBasket() == null) {
            Basket basket = basketDao.getCurrentBasket(user.getUserID());
            if (basket == null)
                return orderItems;
            else {
                orderItems = orderItemDao.getOrderItemByBasketID(basket.getBasketID());
                basket.setOrderItems(orderItems);
                user.setBasket(basket);
                return orderItems;
            }
       // } else{

           // return user.getBasket().getOrderItems();
       // }
            
    }
    @Override
    public List<OrderItem> getOrderItemsByBasketId(int basketId){
        if(basketId <= 0)
            throw new RuntimeException("invalid id");
        return orderItemDao.getOrderItemByBasketID(basketId);

    }

    @Override
    public OrderItem getOrderItemByItemID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");
        return orderItemDao.getOrderItemByItemID(id);
    }


    @Override
    public Basket getCurrentBasket(User user) {
        if (user == null)
            throw new RuntimeException("invalid user");
        Basket basket = basketDao.getCurrentBasket(user.getUserID());
        if (basket == null) {
            basket = new Basket();
            basket.setUserID(user.getUserID()).setTotalPrice(0.0).setBasketStatus("current").setOrderItems(new ArrayList<OrderItem>());
            basketDao.insertBasket(basket);
        }
        return basket;
    }
    //TODO test for this function
    @Override
    public Basket getBasket(int basketId){
        if(basketId <= 0)
            throw new RuntimeException("invalid id");
        Basket basket = basketDao.getBasket(basketId);
        return basket;

    }
    @Override
    public void deleteFromBasket(User user, int itemId) {
        if (user == null || itemId <= 0)
            throw new RuntimeException("invalid entry");
        OrderItem orderItem = orderItemDao.getOrderItemByItemID(itemId);
        orderItemDao.deleteOrderItemByItemID(orderItem.getOrderItemID());
        //get new basket and set to the user...
        Basket newBasket = basketDao.getBasket(orderItem.getBasketID());
        user.setBasket(newBasket);
    }

    @Override
    public void updateBasket(User user,OrderItem orderItem) {
        if (orderItem == null)
            throw new RuntimeException("invalid entry!");
        orderItemDao.updateOrderItem(orderItem);
        user.getBasket().setOrderItems(orderItemDao.getOrderItemByBasketID(user.getBasket().getBasketID()));
    }


}
