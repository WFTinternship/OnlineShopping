package businessLayerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.CategoryManagerImpl;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.ProductManagerImpl;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 01.08.2016.
 */
public class ProductManagerImplTest {
    private Product product;
    private Category category;
    private ProductManager productManager;
    private CategoryManager categoryManager;
  //  private LegacyDataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
     //   dataSource = LegacyDataSource.getInstance();
        productManager = new ProductManagerImpl();
        categoryManager = new CategoryManagerImpl();

        category = getTestCategory();
        categoryManager.createNewCategory(category);
        product = getTestProduct();


    }
    @After
    public void tearDown()  {
        categoryManager.deleteCategory(category.getCategoryID());
    }
    @Test
    public void createNewProduct(){
        int id = productManager.createNewProduct(product);
        assertFalse(id == 0);
    }

    private Category getTestCategory(){
        category = new Category();
        category.setName("bag");
        return category;
    }
    private Product getTestProduct(){
        product = new Product();
        product.setName("baby bag").
                setShippingPrice(1).
                setPrice(20).
                setDescription("color: white, size: 1").
                setCategory(category).
                setQuantity(50);
        return product;
    }
}
