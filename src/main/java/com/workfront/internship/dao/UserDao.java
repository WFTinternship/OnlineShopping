package com.workfront.internship.dao;
import com.workfront.internship.common.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


/**
 * Created by Workfront on 7/1/2016.
 */
public interface UserDao {

    int insertUser(User user);

    User getUserByID(int userid);

    User getUserByUsername(String username);

    void updateUser(User user);

    void updateUserWiyhoutPassword(User user);

    void updateUserStatus(int id);

    void deleteUser(int userid);

    List<Product> getWishlist(int userid);

    void deleteWishlistByUserID(int userid);

    void deleteFromWishlistByUserIDAndProductID(int userid, int productid);

    void insertIntoWishlist(int userID, int productID);

    boolean isInWishList(int userID, int productID);

    void deleteAllUsers();

    List<User> getAllUsers();

}
