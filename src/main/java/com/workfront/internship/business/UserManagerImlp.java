package com.workfront.internship.business;

import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserManagerImlp {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class);
    private DataSource dataSource;
    private UserDao userDao;

    public UserManagerImlp(DataSource dataSource) throws IOException, SQLException{
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);

    }
//TODO address hashing
    public int registration(User user)  {
        int index = 0;
        if(!user.getShippingAddresses().isEmpty())
            index = userDao.insertUserWithShippingAddresses(user);
        else
        index = userDao.insertUser(user);

        return index;
    }

    public User login(String username) throws IOException, SQLException {
        AddressDao addressDao = new AddressDaoImpl(dataSource);
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        SaleDao saleDao = new SaleDaoImpl(dataSource);

        User user = userDao.getUserByUsername(username);
        int userID = user.getUserID();

        List<Address> addresses = addressDao.getShippingAddressByUserID(userID);
        Basket basket = basketDao.getCurrentBasket(userID);
        List<Product> wishlist = userDao.getWishlist(userID);
        List<Sale> sales = saleDao.getSales(userID);

        user.setBasket(basket).setWishList(wishlist).setShippingAddresses(addresses).setSales(sales);

        return user;
    }
}