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
    void deleteAccount(int userId);
    String getHash(String str) throws NoSuchAlgorithmException;
    boolean validateUser(User user);
    boolean validateEmail(String email);
  //  List<Address> getListOfShippingAddresses(User user)throws IOException, SQLException;

}
