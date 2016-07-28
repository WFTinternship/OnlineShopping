package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public interface BasketManager {

    Basket getBasket(User user);
    void addToBasket(User user, Product product, int quantity);
    void deleteFromBasket(OrderItem orderItem);
    void updateBasket(User user, OrderItem orderItem, int quantity);

}
