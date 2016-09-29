package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.dao.*;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class CategoryDaoImplIntegrationTest {

    Category category = null;
    int lastInsertedIndex = 0;
    @Autowired
    CategoryDao categoryDao;

    @Before
    public void setUpDB() throws SQLException, IOException {

        category = TestHelper.getTestCategory();
        lastInsertedIndex = categoryDao.insertCategory(category);

        category.setCategoryID(lastInsertedIndex);
    }

    @After
    public void tearDown() {

        categoryDao.deleteAllCategories();
    }

    @Test
    public void insertCategory() {

        Category category2 = categoryDao.getCategoryByID(lastInsertedIndex);

        doAssertion(category2, category);

    }

    @Test(expected = RuntimeException.class)
    public void insertCategory_duplicate() {

        categoryDao.insertCategory(category);
    }

    @Test(expected = RuntimeException.class)
    public void updateCategory_dulicate() {

        Category category1 = TestHelper.getTestCategory();
        category1.setName("newName");

        categoryDao.insertCategory(category1);

        category1.setName(category.getName());
        //testing method...
        categoryDao.updateCategory(category1);
    }

    @Test
    public void getCategoryByID() {

        Category category1 = categoryDao.getCategoryByID(lastInsertedIndex);

        doAssertion(category1, category);
    }

    @Test
    public void updateCategory() {


        category.setName("newCategory");

        categoryDao.updateCategory(category);

        Category category1 = categoryDao.getCategoryByID(lastInsertedIndex);

        doAssertion(category1, category);

    }

    @Test
    public void deleteAllCategories() {
        categoryDao.deleteAllCategories();

        List<Category> categories = categoryDao.getAllCategories();

        assertEquals(true, categories.isEmpty());
    }

    @Test
    public void getAllCategories() {
        //testing method...
        List<Category> categories = categoryDao.getAllCategories();

        doAssertion(category, categories.get(0));


    }

    @Test
    public void deleteCategoryByID() {
        //testing method...
        categoryDao.deleteCategoryByID(lastInsertedIndex);

        Category category1 = categoryDao.getCategoryByID(lastInsertedIndex);
        assertNull(category1);
    }


    private void doAssertion(Category category, Category category1) {
        assertEquals(category.getCategoryID(), category1.getCategoryID());
        assertEquals(category.getName(), category1.getName());
    }
}
