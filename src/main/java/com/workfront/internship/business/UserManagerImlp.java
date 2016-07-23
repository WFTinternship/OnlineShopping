package com.workfront.internship.business;

import com.workfront.internship.common.*;

import com.workfront.internship.dao.*;
import org.apache.log4j.Logger;
import org.mockito.internal.matchers.Or;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserManagerImlp implements UserManager {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class);
    private DataSource dataSource;
    private UserDao userDao;


    public UserManagerImlp(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        userDao = new UserDaoImpl(dataSource);


    }

    //TODO password hashing
    public int createAccount(User user) {
        int index = 0;
        if (!user.getShippingAddresses().isEmpty())
            index = userDao.insertUserWithShippingAddresses(user);
        else
            index = userDao.insertUser(user);

        return index;
    }

    public User login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        //TODO  hash(password) and compare with the record in DB
        if (user != null && user.getPassword() == password)
            return user;

        return null;
    }

    public List<Product> getWishlist(User user) {
        List<Product> wishlist = userDao.getWishlist(user.getUserID());
        user.setWishList(wishlist);
        return wishlist;
    }

    public Basket getBasket(User user) throws IOException, SQLException {
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        Basket basket = basketDao.getCurrentBasket(user.getUserID());
        user.setBasket(basket);
        return basket;
    }

    public List<Address> getListOfShippingAddresses(User user) throws IOException, SQLException {
        AddressDao addressDao = new AddressDaoImpl(dataSource);
        List<Address> addresses = addressDao.getShippingAddressByUserID(user.getUserID());
        user.setShippingAddresses(addresses);
        return addresses;
    }
    public int addShippingAddress(Address address)throws IOException, SQLException {
        AddressDao addressDao = new AddressDaoImpl(dataSource);
        int index = addressDao.insertAddress(address);
        return index;
    }

    public List<Sale> getRecords(User user) throws IOException, SQLException {
        SaleDao saleDao = new SaleDaoImpl(dataSource);
        List<Sale> records = saleDao.getSales(user.getUserID());
        user.setRecords(records);
        return records;
    }

    public void editProfile(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void addToCart(User user, Product product, int quantity) throws IOException, SQLException {
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
        if (user.getBasket() == null) {
            Basket basket = getBasket(user);
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product).setBasketID(user.getBasket().getBasketID()).setQuantity(quantity);
        if (orderItemDao.getOrderItemByProductAndBasketID(product.getProductID(), user.getBasket().getBasketID()) == null)
            orderItemDao.insertOrderItem(orderItem);
        else
            orderItemDao.updateOrderItem(orderItem);
    }

    @Override
    public void deleteFromCart(User user, OrderItem orderItem) throws IOException, SQLException {
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
        orderItemDao.deleteOrderItemByProducttIDAndByBasketID(orderItem.getProduct().getProductID(), user.getBasket().getBasketID());

    }

    @Override
    public void updateCart(User user, OrderItem orderItem, int quantity) throws IOException, SQLException {
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
        orderItem.setQuantity(quantity);
        orderItemDao.updateOrderItem(orderItem);

    }

    @Override
    public void addToList(User user, Product product) {
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

    }

    @Override
    public void deleteFromList(User user, Product product) {
        userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());

    }

    @Override
    public void deleteAccount(User user) {
        userDao.deleteUser(user.getUserID());

    }
    @Override
    public void makeNewSale(Sale sale) throws IOException, SQLException{
        SaleDao saleDao = new SaleDaoImpl(dataSource);
        saleDao.insertSale(sale);
    }
}