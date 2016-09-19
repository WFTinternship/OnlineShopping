package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


public interface UserManager {

    int createAccount(User user);
    User login(String username, String password);
    void editProfile(User user);
    void deleteAccount(int id);
    List<Product> getList(User user);
    void addToList(User user, Product product);
    void deleteFromList(User user, Product product);
    void deleteAllUsers();
    void editProfileWiyhoutPassword(User user);


}
