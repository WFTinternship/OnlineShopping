package com.workfront.internship.business;

import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;
import org.apache.log4j.Logger;
import org.mockito.internal.matchers.Or;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserManagerImlp implements UserManager {

    private DataSource dataSource;
    private UserDao userDao;
    AddressDao addressDao;

    public UserManagerImlp(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);
        addressDao = new AddressDaoImpl(dataSource);


    }

    //TODO password hashing
    public int createAccount(User user) {
        String hashOfPassword = "";
        int index = 0;
        if(user!=null) {
            if (user.getFirstname() != null && user.getLastname() != null && user.getUsername() != null && user.getPassword() != null && user.getEmail() != null) {
                try {
                    hashOfPassword = getHash(user.getPassword());
                    user.setPassword(hashOfPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                    index = userDao.insertUser(user);
                if(index > 0 && !(user.getShippingAddresses().isEmpty()))
                    for(int i = 0; i < user.getShippingAddresses().size(); i++)
                    addressDao.insertAddress(user.getShippingAddresses().get(i));

            }
        }
        return index;
    }

    public User login(String username, String password) {

        User user = userDao.getUserByUsername(username);
        if(user!=null) {

            try {
                if (user.getPassword() == getHash(password)) ;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return user;
        }

        return null;
    }

    public List<Address> getListOfShippingAddresses(User user) {

        List<Address> addresses = addressDao.getShippingAddressByUserID(user.getUserID());
        user.setShippingAddresses(addresses);
        return addresses;
    }

    public void editProfile(User user) {
        userDao.updateUser(user);
    }


    @Override
    public void deleteAccount(int id) {
        userDao.deleteUser(id);

    }
    private String getHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] result = messageDigest.digest(str.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            stringBuffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuffer.toString();


    }

}