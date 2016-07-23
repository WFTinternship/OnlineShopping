package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Created by Workfront on 7/21/2016.
 */
public interface UserManager {
    int createAccount(User user);
    User login(String username, String password);
    void editProfile(User user);
    void deleteAccount(int userId);
    List<Product> getWishlist(User user);
    Basket getBasket(User user)throws IOException, SQLException;
    List<Address> getListOfShippingAddresses(User user)throws IOException, SQLException;
    int addShippingAddress(Address address)throws IOException, SQLException;
    List<Sale> getRecords(User user)throws IOException, SQLException;
    void addToCart(User user, Product product, int quantity) throws IOException, SQLException;
    void deleteFromCart(OrderItem orderItem)throws IOException, SQLException;
    void updateCart(User user, OrderItem orderItem, int quantity)throws IOException, SQLException;
    void addToList(User user, Product product);
    void deleteFromList(User user, Product product);
    void makeNewSale(Sale sale) throws IOException, SQLException;
}
