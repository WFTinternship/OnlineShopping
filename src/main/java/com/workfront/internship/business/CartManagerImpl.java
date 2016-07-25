package com.workfront.internship.business;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/25/2016.
 */
public class CartManagerImpl implements CartManager{

    private DataSource dataSource;
    private UserDao userDao;
    private OrderItemDao orderItemDao;
    private BasketDao basketDao;


    public CartManagerImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);
        orderItemDao = new OrderItemDaoImpl(dataSource);
        basketDao = new BasketDaoImpl(dataSource);


    }

        @Override
        public void addToCart(User user, Product product, int quantity) {
            if (user.getBasket() == null) {
                Basket basket = getCart(user);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product).setBasketID(user.getBasket().getBasketID()).setQuantity(quantity);
            if (orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID()) == null)
                orderItemDao.insertOrderItem(orderItem);
            else
                orderItemDao.updateOrderItem(orderItem);
        }
    @Override
        public Basket getCart(User user){

        Basket basket = basketDao.getCurrentBasket(user.getUserID());
        user.setBasket(basket);
        return basket;
    }

        @Override
        public void deleteFromCart(OrderItem orderItem) {
            orderItemDao.deleteOrderItemByItemID(orderItem.getOrderItemID());
        }

        @Override
        public void updateCart(User user, OrderItem orderItem, int quantity)  {
            orderItem.setQuantity(quantity);
            orderItemDao.updateOrderItem(orderItem);

        }

        @Override
        public void addToList(User user, Product product) {
            userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

        }
        @Override
        public List<Product> getList(User user) {
            List<Product> wishlist = userDao.getWishlist(user.getUserID());
            user.setWishList(wishlist);
            return wishlist;
        }

        @Override
        public void deleteFromList(User user, Product product) {
            userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());

        }

}
