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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManagerImlp implements UserManager {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
            if (validateUser(user)) {
                try {
                    hashOfPassword = getHash(user.getPassword());
                    user.setPassword(hashOfPassword);
                    index = userDao.insertUser(user);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    throw new RuntimeException("System error");
                }
                if(index > 0 && !(user.getShippingAddresses().isEmpty()))
                    for(int i = 0; i < user.getShippingAddresses().size(); i++)
                    addressDao.insertAddress(user.getShippingAddresses().get(i));

            }
            else
                throw new RuntimeException("Invalid user");
        }
        return index;
    }

    public User login(String username, String password) {

        User user = userDao.getUserByUsername(username);
        if(user!=null) {

            try {
                if (user.getPassword() == getHash(password))
                    return user;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException("System error");
            }

        }
         throw new RuntimeException("Invalid user");

    }

    /*public List<Address> getListOfShippingAddresses(User user) {

        List<Address> addresses = addressDao.getShippingAddressByUserID(user.getUserID());
        user.setShippingAddresses(addresses);
        return addresses;
    }*/

    public void editProfile(User user) {

        userDao.updateUser(user);

        List<Address> oldAddresses = addressDao.getShippingAddressByUserID(user.getUserID());
        List<Address> newAddresses = user.getShippingAddresses();
        for(int i = 0; i < newAddresses.size(); i ++)
            if(!oldAddresses.contains(newAddresses.get(i)))
                addressDao.insertAddress(newAddresses.get(i));
        for(int i = 0; i < oldAddresses.size(); i ++)
            if(!newAddresses.contains(oldAddresses.get(i)))
                addressDao.deleteAddressesByAddressID(oldAddresses.get(i).getAddressID());

    }


    @Override
    public void deleteAccount(int id) {
        userDao.deleteUser(id);

    }
    public String getHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] result = messageDigest.digest(str.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            stringBuffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuffer.toString();


    }
    public boolean validateUser(User user){
              if(user.getFirstname() != null &&
                user.getLastname() != null &&
                user.getUsername() != null &&
                user.getPassword() != null &&
                user.getEmail() != null && validateEmail(user.getEmail()))
                  return true;
        return false;
    }

    public boolean validateEmail(String email){

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

}