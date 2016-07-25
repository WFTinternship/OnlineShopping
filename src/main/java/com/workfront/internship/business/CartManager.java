package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public interface CartManager {

    Basket getCart(User user);
    void addToCart(User user, Product product, int quantity);
    void deleteFromCart(OrderItem orderItem);
    void updateCart(User user, OrderItem orderItem, int quantity);
    List<Product> getList(User user);
    void addToList(User user, Product product);
    void deleteFromList(User user, Product product);
}
