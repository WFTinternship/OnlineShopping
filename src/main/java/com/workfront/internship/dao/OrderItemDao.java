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
    void deleteOrderItemByProductID(int productid);
    void updateOrderItem(OrderItem orderItem);
    int insertOrderItem(OrderItem orderItem);
    void deleteAllOrderItems();
    List<OrderItem> getAllOrderItems();
}
