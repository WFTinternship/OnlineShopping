package businessLayerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.SizeManager;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class SizeManagerImplIntegrationTest {
    @Autowired
    private SizeManager sizeManager;
    @Autowired
    private CategoryManager categoryManager;
    private Category category;
    private Size size;

    @Before
    public void setUpDB() throws SQLException, IOException {

        category = TestHelper.getTestCategory();
        categoryManager.createNewCategory(category);
        size = TestHelper.getTestSize();
        size.setCategoryId(category.getCategoryID());
        sizeManager.insertSize(size);
    }

    @After
    public void tearDown() {

        categoryManager.deleteAllCategories();
    }
    @Test
    public void getSizesByCategoryId(){
        //testing method...
        List<Size> sizes = sizeManager.getSizesByCategoryId(category.getCategoryID());

        doAssertion(size, sizes.get(0));

    }
    @Test(expected = RuntimeException.class)
    public void getSizesByCategoryId_invalid_id(){

        sizeManager.getSizesByCategoryId(0);
    }
    @Test(expected = RuntimeException.class)
    public void insertSize(){

        sizeManager.insertSize(null);
    }

    private void doAssertion(Size size, Size size1){

        assertEquals(size.getSizeId(), size.getSizeId());
        assertEquals(size.getCategoryId(), size1.getCategoryId());
        assertEquals(size.getSizeOption(), size1.getSizeOption());
    }
}
