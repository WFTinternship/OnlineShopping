package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 01.08.2016.
 */
public class MediaManagerImplTest {
    private Media media;
    private Product product;
    private Category category;
    private MediaManager mediaManager;
    private ProductManager productManager;
    private CategoryManager categoryManager;
    private DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        mediaManager = new MediaManagerImpl(dataSource);
        productManager = new ProductManagerImpl(dataSource);
        categoryManager = new CategoryManagerImpl(dataSource);
        category = getTestCategory();
        categoryManager.createNewCategory(category);
        product = getTestProduct();
        productManager.createNewProduct(product);
        media = getTestMedia();



    }


    @After
    public void tearDown()  {
        mediaManager.deleteMediaByID(media.getMediaID());
        categoryManager.deleteCategory(category.getCategoryID());
    }
    @Test
    public void insertMedia(){
        int id = mediaManager.insertMedia(media);
        assertFalse(id == 0);
    }
    private Media  getTestMedia(){
        media = new Media();
        media.setProductID(product.getProductID()).setMediaPath("somePath");

        return media;
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
