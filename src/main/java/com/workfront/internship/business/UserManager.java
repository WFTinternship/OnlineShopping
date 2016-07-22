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
    void deleteAccount(User user);
    List<Product> getWishlist(int id);
    Basket getBasket(int id)throws IOException, SQLException;
    List<Address> getListOfShippingAddresses(int id)throws IOException, SQLException;
    List<Sale> getRecords(int id)throws IOException, SQLException;
}
