package daoLayerTest;

import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class UserDaoUnitTest {
    DataSource dataSource;

    UserDao userDao;

    @SuppressWarnings("unchecked")
    @Before
    public void beforeTest() throws Exception {
        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        userDao = new UserDaoImpl(dataSource);
    }

    @After
    public void afterTest() {

    }

    @Test(expected = RuntimeException.class)
    public void insertUser_dbError() throws SQLIntegrityConstraintViolationException {
        userDao.insertUser(new User());
    }
    @Test(expected = RuntimeException.class)
    public void getUserByID_dbError() {
        userDao.getUserByID(5);
    }
    @Test(expected = RuntimeException.class)
    public void getUserByUsername_dbError() {
        userDao.getUserByUsername("name");
    }
    @Test(expected = RuntimeException.class)
    public void updateUser_dbError() {
        userDao.updateUser(new User());
    }
    @Test(expected = RuntimeException.class)
    public void getWishlist_dbError() {
        userDao.getWishlist(3);
    }
    @Test(expected = RuntimeException.class)
    public void deleteWishlistByUserID_dbError() {
        userDao.deleteWishlistByUserID(3);
    }
    @Test(expected = RuntimeException.class)
    public void insertIntoWishlist_dbError() {
        userDao.insertIntoWishlist(3, 7);
    }
    @Test(expected = RuntimeException.class)
    public void deleteUser_dbError() {
        userDao.deleteAllUsers();
    }
    @Test(expected = RuntimeException.class)
    public void deleteUserByID_dbError() {
        userDao.deleteUser(10);
    }
    @Test(expected = RuntimeException.class)
    public void getAllUsers_dbError() {
        userDao.getAllUsers();
    }
}
