package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.dao.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class CategoryDaoUnitTest {
    DataSource dataSource;
    CategoryDao categoryDao;

    @Before
    public void beforeTest() throws IOException, SQLException{

        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);


        categoryDao = new CategoryDaoImpl();
        Whitebox.setInternalState(categoryDao, "dataSource", dataSource);
    }
    @Test(expected = RuntimeException.class)
    public void insertCategory_dbError() {
        categoryDao.insertCategory(new Category());
    }

    @Test(expected = RuntimeException.class)
    public void getCategory_dbError() {
        categoryDao.getCategoryByID(8);
    }

    @Test(expected = RuntimeException.class)
    public void getAllCategories_dbError() {
        categoryDao.getAllCategories();
    }

    @Test(expected = RuntimeException.class)
    public void updatCategory_dbError() {
        categoryDao.updateCategory(new Category());
    }

    @Test(expected = RuntimeException.class)
    public void deleteBasketByUserID_dbError() {
        categoryDao.deleteCategoryByID(2);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllCategories_dbError() {
        categoryDao.deleteAllCategories();
    }
}
