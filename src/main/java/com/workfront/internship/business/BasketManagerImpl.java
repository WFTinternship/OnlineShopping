package com.workfront.internship.business;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;

import java.io.IOException;
import java.sql.SQLException;

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
        public void addToBasket(User user, Product product, int quantity) {
            if (user.getBasket() == null) {
                Basket basket = getBasket(user);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product).setBasketID(user.getBasket().getBasketID()).setQuantity(quantity);
            if (orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID()) == null)
                orderItemDao.insertOrderItem(orderItem);
            else
                orderItemDao.updateOrderItem(orderItem);
        }
        @Override
        public Basket getBasket(User user){

        Basket basket = basketDao.getCurrentBasket(user.getUserID());
        user.setBasket(basket);
        return basket;
    }

        @Override
        public void deleteFromBasket(OrderItem orderItem) {
            orderItemDao.deleteOrderItemByItemID(orderItem.getOrderItemID());
        }

        @Override
        public void updateBasket(User user, OrderItem orderItem, int quantity)  {
            orderItem.setQuantity(quantity);
            orderItemDao.updateOrderItem(orderItem);

        }


}
