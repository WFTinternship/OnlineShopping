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

public class UserManagerImpl implements UserManager {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private DataSource dataSource;
    private UserDao userDao;
    AddressDao addressDao;

    public UserManagerImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);
        addressDao = new AddressDaoImpl(dataSource);

    }

    public int createAccount(User user) {
        if (!validateUser(user)) {
            throw new RuntimeException("Invalid user");

        }
        String hashOfPassword = HashManager.getHash(user.getPassword());
        user.setPassword(hashOfPassword);
        int id = userDao.insertUser(user);
        if (id > 0) {
            if (!user.getShippingAddresses().isEmpty()) {
                for (int i = 0; i < user.getShippingAddresses().size(); i++) {
                    user.getShippingAddresses().get(i).setUserID(id);
                    addressDao.insertAddress(user.getShippingAddresses().get(i));
                }
            }
            EmailManager.sendVerificationEmail(user);
        }

        return id;
    }

    public User login(String username, String password) {
        if (!validString(username) || !validString(password)) {
            throw new RuntimeException("Invalid username or password");
        }
            User user = userDao.getUserByUsername(username);
            if (user != null && user.getPassword().equals(HashManager.getHash(password))) {
                return user;
            } else {
                System.out.println(user.getPassword() +  "    " + HashManager.getHash(password));
                throw new RuntimeException("Invalid user");
            }
    }
    public void editProfile(User user) {
        if(!validateUser(user)) {
            throw new RuntimeException("Can not update");

        }
        userDao.updateUser(user);
        List<Address> oldAddresses = addressDao.getShippingAddressByUserID(user.getUserID());
        List<Address> newAddresses = user.getShippingAddresses();
        for (int i = 0; i < newAddresses.size(); i++)
            if (!oldAddresses.contains(newAddresses.get(i)))
                addressDao.insertAddress(newAddresses.get(i));
        for (int i = 0; i < oldAddresses.size(); i++)
            if (!newAddresses.contains(oldAddresses.get(i)))
                addressDao.deleteAddressesByAddressID(oldAddresses.get(i).getAddressID());
    }

    @Override
    public void deleteAccount(User user) {
        if(!validateUser(user)) {
            throw new RuntimeException("Can not delete");
        }
        userDao.deleteUser(user.getUserID());

    }


    @Override
    public void addToList(User user, Product product) {
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

    }
    @Override
    public List<Product> getList(User user) {
        List<Product> wishlist = userDao.getWishlist(user.getUserID());
        user.setWishList(wishlist);
        return wishlist;
    }

    @Override
    public void deleteFromList(User user, Product product) {
        userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());

    }

    private boolean validateUser(User user) {
        if (user != null && (user.getFirstname() != null) &&
                (user.getLastname() != null) &&
                (user.getUsername() != null) &&
                (user.getPassword() != null) &&
                (user.getEmail() != null) &&
                (validateEmail(user.getEmail())))
            return true;
        return false;
    }
    private boolean validString(String string){
        if(string !=null && string != "")
            return true;
        return false;

    }
    private boolean validateEmail(String email) {

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }



}