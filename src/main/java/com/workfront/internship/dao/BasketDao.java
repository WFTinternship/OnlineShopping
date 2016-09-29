package com.workfront.internship.dao;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.Product;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface BasketDao {

    int insertBasket(Basket basket);

    int insertBasket(Connection connection, Basket basket);

    Basket getBasket(int basketid);

    Basket getBasketByItemId(int id);

    Basket getCurrentBasket(int userId);

    void updateBasket(Basket basket);

    void updateBasketStatus(Connection connection, int id);

    void updateBasket(Connection connection, Basket basket);

    void deleteBasketByBasketID(int basketid);

    void deleteAllBaskets();

    List<Basket> getAllBaskets();

}
