package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;


public class TestCategoryDaoImpl  {
    LegacyDataSource dataSource;
    Category category = null;
    int lastInsertedIndex = 0;
    CategoryDao categoryDao;

    @Before
    public void setUpDB()  throws SQLException, IOException{
        dataSource = LegacyDataSource.getInstance();
        categoryDao = new CategoryDaoImpl();
        Whitebox.setInternalState(categoryDao, "dataSource", dataSource);
        category = getRandomCategory();
        lastInsertedIndex = categoryDao.insertCategory(category);
        category.setCategoryID(lastInsertedIndex);
    }

    @After
    public void tearDown() {

        categoryDao.deleteAllCategories();
    }

    @Test
    public void insertCategory() {

        Category category1 = getRandomCategory();

        int insertindex = categoryDao.insertCategory(category1);


        Category category2 = categoryDao.getCategoryByID(insertindex);

        doAssertion(category2, category1);

    }
    @Test(expected = RuntimeException.class)
    public void insertCategory_duplicate(){
        Category category1 = getRandomCategory();
        category1.setName(category.getName());
        categoryDao.insertCategory(category1);
    }

    @Test(expected = RuntimeException.class)
    public void updateCategory_dulicate(){
        Category category1 = getRandomCategory();

        categoryDao.insertCategory(category1);
        category1.setName(category.getName());
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
    public void deleteAllCategories(){
        categoryDao.deleteAllCategories();

        List<Category> categories = categoryDao.getAllCategories();

        assertEquals(true, categories.isEmpty());
    }
    @Test
    public void getAllCategories(){

            categoryDao.deleteAllCategories();


        List<Category> categories = new ArrayList<>();

        Category category = getRandomCategory();
        Category category1 = getRandomCategory();
        category1.setName("NewName");
        categoryDao.insertCategory(category);
        categoryDao.insertCategory(category1);
        categories = categoryDao.getAllCategories();

        doAssertion(category, categories.get(1));
        doAssertion(category1, categories.get(0));

    }
    @Test
    public void deleteCategoryByID() {

        categoryDao.deleteCategoryByID(lastInsertedIndex);
        Category category1 = categoryDao.getCategoryByID(lastInsertedIndex);
        assertNull(category1);
    }

    private Category getRandomCategory() {
        Random random = new Random();
        int x = random.nextInt(100000);
        Category category = new Category();
        category.setName("oldCategory" + x);
        return category;
    }
    private void doAssertion(Category category, Category category1){
        assertEquals(category.getCategoryID(), category1.getCategoryID());
        assertEquals(category.getName(), category1.getName());
    }
}
