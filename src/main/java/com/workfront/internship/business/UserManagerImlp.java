package com.workfront.internship.business;

import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;
import org.apache.log4j.Logger;
import org.mockito.internal.matchers.Or;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserManagerImlp implements UserManager{
    private static final Logger LOGGER = Logger.getLogger(UserManager.class);
    private DataSource dataSource;
    private UserDao userDao;
    private User user;

    public UserManagerImlp(DataSource dataSource) throws IOException, SQLException{
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);
        user = new User();

    }
//TODO password hashing
    public int createAccount(User user){
        int index = 0;
            if (!user.getShippingAddresses().isEmpty())
                index = userDao.insertUserWithShippingAddresses(user);
            else
                index = userDao.insertUser(user);

        return index;
    }

    public User login(String username, String password) {
        user = userDao.getUserByUsername(username);
        //TODO  hash(password) and compare with the record in DB
        if(user != null && user.getPassword() == password)
            return user;

        return null;
    }
    public List<Product> getWishlist(int id) {
        List<Product> wishlist = userDao.getWishlist(id);
        user.setWishList(wishlist);
        return wishlist;
    }
    public Basket getBasket(int userid) throws IOException, SQLException{
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        Basket basket = basketDao.getCurrentBasket(userid);
        user.setBasket(basket);
        return basket;
    }
    public List<Address> getListOfShippingAddresses(int id) throws IOException, SQLException{
        AddressDao addressDao = new AddressDaoImpl(dataSource);
        List<Address> addresses = addressDao.getShippingAddressByUserID(id);
        user.setShippingAddresses(addresses);
        return addresses;
    }
    public List<Sale> getRecords(int id) throws IOException, SQLException{
        SaleDao saleDao = new SaleDaoImpl(dataSource);
        List<Sale> records = saleDao.getSales(id);
        user.setRecords(records);
        return records;
    }
    public void editProfile(User user) {
        userDao.updateUser(user);
    }

   /* @Override
    public void addToCart(User user, Product product)throws IOException, SQLException{
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
        Basket basket = getBasket(user.getUserID());
        OrderItem orderItem = orderItemDao.getOrderItemByItemID();
        orderItemDao.get
        if(orderItemDao.getOrderItemByBasketID(user.getBasket().getBasketID())==null)
        orderItem.set


    }
    @Override
    public void deleteFromCart(User user, Product product){

    }
    @Override
    public void addToList(User user, Product product){
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

    }
    @Override
    public void deleteFromList(User user, Product product){
        userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());

    }*/
    @Override
    public void deleteAccount(User user){
        userDao.deleteUser(user.getUserID());

    }

}