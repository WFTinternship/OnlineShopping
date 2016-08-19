package daoLayerTest;

import com.workfront.internship.common.OrderItem;
import com.workfront.internship.dao.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class OrderItemDaoUnitTest {

    LegacyDataSource dataSource;
    OrderItemDao orderItemDao;

    @Before
    public void beforeTest()throws IOException, SQLException{
        dataSource = Mockito.mock(LegacyDataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        orderItemDao = new OrderItemDaoImpl(dataSource);
    }
    @Test(expected = RuntimeException.class)
    public void insertOrderItem_dbError() {
        orderItemDao.insertOrderItem(new OrderItem());
    }
    @Test(expected = RuntimeException.class)
    public void getOrderItemByItemID_dbError() {
        orderItemDao.getOrderItemByItemID(8);
    }
    @Test(expected = RuntimeException.class)
    public void getOrderItemByBasketID_dbError() {
        orderItemDao.getOrderItemByBasketID(3);
    }
    @Test(expected = RuntimeException.class)
    public void getAllOrderItems_dbError() {
        orderItemDao.getAllOrderItems();
    }
    @Test(expected = RuntimeException.class)
    public void updateOrderItem_dbError() {
        orderItemDao.updateOrderItem(new OrderItem());
    }
    @Test(expected = RuntimeException.class)
    public void deleteOrderItemByItemID_dbError() {
        orderItemDao.deleteOrderItemByItemID(3);
    }
    @Test(expected = RuntimeException.class)
    public void deleteAllOrderItems_dbError() {
        orderItemDao.deleteAllOrderItems();
    }
    @Test(expected = RuntimeException.class)
    public void deleteOrderItemByProductID_dbError() {
        orderItemDao.deleteOrderItemByProductID(3);
    }
}
