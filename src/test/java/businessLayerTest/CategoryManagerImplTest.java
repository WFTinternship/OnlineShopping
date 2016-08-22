package businessLayerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.CategoryManagerImpl;
import com.workfront.internship.common.Category;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Workfront on 8/1/2016.
 */
public class CategoryManagerImplTest {
    private Category category;
    private CategoryManager categoryManager;
 //   LegacyDataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
      //  dataSource = LegacyDataSource.getInstance();
        category = getTestCategory();
        categoryManager = new CategoryManagerImpl();
    }


    @After
    public void tearDown()  {
       categoryManager.deleteCategory(category.getCategoryID());
    }
    @Test
    public void createNewCategory(){

        int id = categoryManager.createNewCategory(category);
        Category actualCategory = categoryManager.getCategoryByID(id);
        assertNotNull(actualCategory);

    }
    private Category getTestCategory() {
        Random random = new Random();
        int x = random.nextInt(100000);
        Category category = new Category();
        category.setName("oldCategory" + x).setCategoryID(x);
        return category;
    }
}
