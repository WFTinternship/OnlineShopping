package businessLayerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.CategoryManagerImpl;
import com.workfront.internship.common.Category;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 8/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ManagerTestConfig.class)
public class CategoryManagerImplTest {
    private Category category;
    @Autowired
    private CategoryManager categoryManager;


    @Before
    public void setUP() throws IOException, SQLException {

        category = getTestCategory();
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
