
import com.workfront.internship.common.Category;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;


public class TestCategoryDaoImpl  {
    DataSource  dataSource;
    Category category = null;
    int lastInsertedIndex = 0;
    CategoryDao categoryDao;

    @Before
    public void setUpDB()  throws SQLException, IOException{
        dataSource = DataSource.getInstance();
        categoryDao = new CategoryDaoImpl(dataSource);
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
        category1.setName("NewName");
        int insertindex = categoryDao.insertCategory(category1);
        category1.setCategoryID(insertindex);

        Category category2 = categoryDao.getCategoryByID(insertindex);

        doAssertion(category2, category1);

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
            category.setName("NewName2");
            categoryDao.insertCategory(category);
            categories.add(category);

            Category category1 = getRandomCategory();
            category1.setName("newCategory1");
            categoryDao.insertCategory(category1);
            categories.add(category1);

            List<Category>categories1 = categoryDao.getAllCategories();

            for (int i = 0; i < categories1.size(); i++) {
                doAssertion(categories.get(i), categories1.get(i));
            }
    }
    @Test
    public void deleteCategoryByID() {

        categoryDao.deleteCategoryByID(lastInsertedIndex);
        Category category1 = categoryDao.getCategoryByID(lastInsertedIndex);
        assertNull(category1);
    }

    private Category getRandomCategory() {
        Category category = new Category();
        category.setName("oldCategory");
        return category;
    }
    private void doAssertion(Category category, Category category1){
        assertEquals(category.getCategoryID(), category1.getCategoryID());
        assertEquals(category.getName(), category1.getName());
    }
}
