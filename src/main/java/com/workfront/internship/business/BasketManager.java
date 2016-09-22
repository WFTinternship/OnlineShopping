package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public interface BasketManager {
    List<OrderItem> showItemsInCurrentBasket(User user);
    List<OrderItem> getOrderItemsByBasketId(int basketId);

    OrderItem getOrderItemByItemID(int id);
    int createNewBasket(Basket basket);
    Basket getCurrentBasket(User user);
    Basket getBasket(int basketId);
    int addToBasket(User user, Product product, String sizeOption, int quantity);
    void deleteFromBasket(User user, int itemId);
    void updateBasket(User user, OrderItem orderItem);


}
