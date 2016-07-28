package com.workfront.internship.business;

import com.workfront.internship.common.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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


    //TODO tests and correct implementations
    List<Product> getList(User user);
    void addToList(User user, Product product);
    void deleteFromList(User user, Product product);

}
