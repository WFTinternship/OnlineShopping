package com.workfront.internship.dao;
import com.workfront.internship.common.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


/**
 * Created by Workfront on 7/1/2016.
 */
public interface UserDao {
    int insertUser(User user);
    int insertUserWithShippingAddresses(User user);
    User getUserByID(int userid);
    User getUserByUsername(String username);
    void updateUser(User user);
    void deleteUser(int userid);
    List<Product> getWishlist(int userid);
    void deleteWishlistByUserID(int userid);
    void deleteFromWishlistByUserIDAndProductID(int userid, int productid);
    void insertIntoWishlist(int userID, int productID);
    void deleteAllUsers();
    List<User> getAllUsers();

}
