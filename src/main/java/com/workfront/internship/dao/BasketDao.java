package com.workfront.internship.dao;

import com.workfront.internship.common.Basket;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface BasketDao {
    int insertBasket(Basket basket);
    int insertBasket(Connection connection, Basket basket);
    Basket getBasket(int basketid);
    Basket getCurrentBasket(int userId);
    void updateBasket(Basket basket);
    void updateBasket(Connection connection, Basket basket);
    void deleteBasketByBasketID(int basketid);
    void deleteBasketByUserId(int userid);
    void deleteAllBaskets();
    List<Basket> getAllBaskets();

}
