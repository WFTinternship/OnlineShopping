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
    Basket getCurrentBasket(int userId);
    void updateBasket(Basket basket);
    void updateBasket(Connection connection, Basket basket);
    void deleteBasketByBasketID(int basketid);
    void deleteBasketByUserId(int userid);
  //  int insertProductIntoBasket(Basket basket, Product product);
  //  void deleteProductFromBasket(Basket basket, Product product);
    void deleteAllBaskets();
    List<Basket> getAllBaskets();

}
