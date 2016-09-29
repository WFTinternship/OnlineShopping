package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Size;
import com.workfront.internship.dao.CategoryDao;
import com.workfront.internship.dao.SizeDao;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


/**
 * Created by annaasmangulyan on 9/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class SizeDaoIntegrationTest {
    @Autowired
    private SizeDao sizeDao;
    @Autowired
    private CategoryDao categoryDao;
    private Category category;
    private Size size;

    @Before
    public void setUpDB() throws SQLException, IOException {

        category = TestHelper.getTestCategory();
        categoryDao.insertCategory(category);
        size = TestHelper.getTestSize();
        size.setCategoryId(category.getCategoryID());
        sizeDao.insertSize(size);
    }

    @After
    public void tearDown() {

        categoryDao.deleteAllCategories();
    }
    @Test
    public void getSizesByCategoryId(){
        //testing method...
        List<Size> sizes = sizeDao.getSizesByCategoryId(category.getCategoryID());

        doAssertion(size, sizes.get(0));

    }

    private void doAssertion(Size size, Size size1){

        assertEquals(size.getSizeId(), size.getSizeId());
        assertEquals(size.getCategoryId(), size1.getCategoryId());
        assertEquals(size.getSizeOption(), size1.getSizeOption());
    }

}
