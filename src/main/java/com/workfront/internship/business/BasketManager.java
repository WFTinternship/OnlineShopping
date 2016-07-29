package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public interface BasketManager {
    List<OrderItem> showItemsInBasket(Basket basket);
    int createNewBasket(Basket basket);
    Basket getCurrentBasket(User user);
    void addToBasket(User user, Product product, int quantity);
    void deleteFromBasket(int itemId);
    void updateBasket(OrderItem orderItem);

}
