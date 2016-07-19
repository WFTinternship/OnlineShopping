import com.workfront.internship.common.Product;
import com.workfront.internship.dao.DataSource;
import com.workfront.internship.dao.ProductDao;
import com.workfront.internship.dao.ProductDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


/**
 * Created by Administrator on 17.07.2016.
 */
public class ProductDaoUnitTest {
    DataSource dataSource;

    ProductDao productDao;

    @SuppressWarnings("unchecked")
    @Before
    public void beforeTest() throws Exception {
        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        productDao = new ProductDaoImpl(dataSource);
    }

    @After
    public void afterTest() {

    }

    @Test(expected = RuntimeException.class)
    public void insertProduct_dbError() {
        productDao.insertProduct(new Product());
    }
    @Test(expected = RuntimeException.class)
    public void getProduct_dbError() {
        productDao.getProductByID(10);
    }
    @Test(expected = RuntimeException.class)
    public void updateProduct_dbError() {
        productDao.updateProduct(new Product());
    }
    @Test(expected = RuntimeException.class)
    public void deleteProduct_dbError() {
        productDao.deleteAllProducts();
    }
    @Test(expected = RuntimeException.class)
    public void deleteProductByID_dbError() {
        productDao.deleteProductByID(10);
    }
    @Test(expected = RuntimeException.class)
    public void getAllProducts_dbError() {
        productDao.getAllProducts();
    }
}
