package com.workfront.internship.business;

import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserManagerImpl implements UserManager {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private EmailManager emailManager;

    public int createAccount(User user) {
        if (!validateUser(user)) {
            throw new RuntimeException("Invalid user");

        }
        int id = 0;
        String hashOfPassword = HashManager.getHash(user.getPassword());
        user.setPassword(hashOfPassword);
        try {
            id = userDao.insertUser(user);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Duplicate entry!"))
                return 0;
        }

        if (id > 0) {
           /* if (user.getShippingAddresses() != null && !user.getShippingAddresses().isEmpty()) {
                for (int i = 0; i < user.getShippingAddresses().size(); i++) {
                    user.getShippingAddresses().get(i).setUserID(id);
                    addressDao.insertAddress(user.getShippingAddresses().get(i));
                }
            }*/
            emailManager.sendVerificationEmail(user);
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
            return null;
        }
    }

    public void editProfile(User user) {
        if (!validateUser(user)) {
            throw new RuntimeException("Can not update");

        }
        String hashOfPassword = HashManager.getHash(user.getPassword());
        user.setPassword(hashOfPassword);
        userDao.updateUser(user);

    }

    @Override
    public void editProfileWiyhoutPassword(User user) {
        if (!validateUser(user)) {
            throw new RuntimeException("Can not update");

        }
        userDao.updateUserWiyhoutPassword(user);
    }

    @Override
    public void deleteAccount(int id) {
        if (id <= 0) {
            throw new RuntimeException("Can not delete");
        }
        userDao.deleteUser(id);

    }

    @Override
    public int addToList(User user, Product product) {
        if (user == null || product == null)
            throw new RuntimeException("invalid entry!");
        getList(user);
        int id = 0;

        try {
            id = userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Duplicate entry!"))
                return -1;
        }

        user.setWishList(userDao.getWishlist(user.getUserID()));
        return id;

    }

    @Override
    public List<Product> getList(User user) {
        if (user == null)
            throw new RuntimeException("invalid user");
        List<Product> wishlist = user.getWishList();
        if (user.getWishList() != null)
            return wishlist;
        wishlist = userDao.getWishlist(user.getUserID());
        if (wishlist.isEmpty())
            return wishlist;
        user.setWishList(wishlist);
        return wishlist;
    }

    @Override
    public void deleteFromList(User user, int id) {
        if (user == null || id <= 0)
            throw new RuntimeException("invalid entry");
        userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), id);
        user.setWishList(userDao.getWishlist(user.getUserID()));

    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAllUsers();
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

    private boolean validString(String string) {
        if (string != null && string != "")
            return true;
        return false;

    }

    private boolean validateEmail(String email) {

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}