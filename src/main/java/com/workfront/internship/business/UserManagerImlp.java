package com.workfront.internship.business;

import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;

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

    public int registration(User user)  {
        int index = 0;
        index = userDao.insertUser(user);
        try {
            BasketDao basketDao = new BasketDaoImpl(dataSource);
            AddressDao addressDao = new AddressDaoImpl(dataSource);
            basketDao.insertBasket(user.getBasket());
            List<Address> addresses = user.getShippingAddresses();
            if(!addresses.isEmpty())
                for(int i = 0; i<addresses.size(); i++)
                    addressDao.insertAddress(addresses.get(i));

        } catch (IOException |SQLException e) {
            e.printStackTrace();
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }

     return index;
    }
}