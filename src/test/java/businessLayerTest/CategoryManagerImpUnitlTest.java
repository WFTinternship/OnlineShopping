package businessLayerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.CategoryManagerImpl;
import com.workfront.internship.common.Category;
import com.workfront.internship.dao.CategoryDao;
import com.workfront.internship.dao.CategoryDaoImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Workfront on 7/29/2016.
 */
public class CategoryManagerImpUnitlTest {
    private Category category;
    private CategoryManager categoryManager;
 //   LegacyDataSource dataSource;
    CategoryDao categoryDao;

    @Before
    public void setUP() throws IOException, SQLException {
        category = getTestCategory();
        categoryManager = new CategoryManagerImpl();
        categoryDao = Mockito.mock(CategoryDaoImpl.class);
        Whitebox.setInternalState(categoryManager, "categoryDao", categoryDao);

    }
    @After
    public void tearDown() {
        category = null;
        categoryManager = null;

    }
    @Test
    public void createNewCategory_valid_category(){

        categoryManager.createNewCategory(category);
        Mockito.verify(categoryDao).insertCategory(category);

    }
    @Test(expected = RuntimeException.class)
    public void createNewCategory_not_valid_category() {

        Category category1 = new Category();
        categoryManager.createNewCategory(category1);
    }
    @Test
    public void getCategory_validID(){

        when(categoryDao.getCategoryByID(category.getCategoryID())).thenReturn(category);

        Category category1 = categoryManager.getCategoryByID(category.getCategoryID());
        doAssertion(category,category1);


    }
    @Test
    public void getCategoriesByParentId(){
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        when(categoryDao.getCategoriesByParentID(category.getParentID())).thenReturn(categories);

        List<Category> categories1 = categoryManager.getCategoriesByParentID(category.getParentID());
        doAssertion(categories.get(0), categories1.get(0));

    }
    @Test(expected = RuntimeException.class)
    public void getCategory_not_validID(){

        categoryManager.getCategoryByID(-5);

    }
    @Test
    public void deleteCategory_validId(){
        categoryManager.deleteCategory(category.getCategoryID());
        Mockito.verify(categoryDao).deleteCategoryByID(category.getCategoryID());
    }
    @Test(expected = RuntimeException.class)
    public void deleteCategory_invalidId(){
        categoryManager.deleteCategory(-5);
    }
    @Test
    public void getAllCategories(){
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryDao.getAllCategories()).thenReturn(categories);
        List<Category> categories1 = categoryManager.getAllCategories();
        doAssertion(categories.get(0), categories1.get(0));
    }
    @Test
    public void updateCategory(){
        categoryManager.updateCategory(category);
        Mockito.verify(categoryDao).updateCategory(category);
    }
    @Test(expected = RuntimeException.class)
    public void updateCategory_invalidCategory(){
        Category category1 = new Category();
        categoryManager.updateCategory(category1);
    }


    private Category getTestCategory() {
        Random random = new Random();
        int x = random.nextInt(100000);
        Category category = new Category();
        category.setName("oldCategory" + x).setCategoryID(x).setParentID(1);
        return category;
    }
    private void doAssertion(Category category, Category category1){
        assertEquals(category.getCategoryID(), category1.getCategoryID());
        assertEquals(category.getName(), category1.getName());
    }
}
