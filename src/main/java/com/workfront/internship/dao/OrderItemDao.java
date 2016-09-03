package com.workfront.internship.dao;

import com.workfront.internship.common.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface OrderItemDao {

    List<OrderItem> getOrderItemByBasketID(int basketid);

    OrderItem getOrderItemByItemID(int id);

    void deleteOrderItemByItemID(int itemid);

    OrderItem getOrderItemByProductID(int id);

    void deleteOrderItemByProductID(int productid);

    OrderItem getOrderItemByProductAndBasketID(int productId, int basketId);

    void updateOrderItem(OrderItem orderItem);

    int insertOrderItem(OrderItem orderItem);

    void deleteAllOrderItems();

    List<OrderItem> getAllOrderItems();
}
