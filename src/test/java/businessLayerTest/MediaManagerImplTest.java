package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;

import com.workfront.internship.spring.TestConfiguration;
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

import static org.junit.Assert.assertFalse;

/**
 * Created by Anna Asmangulyan on 01.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class MediaManagerImplTest {
    private Media media;
    private Product product;
    private Category category;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private CategoryManager categoryManager;


    @Before
    public void setUP() throws IOException, SQLException {

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
        category.setName("bag").setParentID(1);
        return category;
    }
    private Product getTestProduct(){
        product = new Product();
        product.setName("baby bag").
                setShippingPrice(1).
                setPrice(20).
                setDescription("color: white, size: 1").
                setCategory(category);
             //   setQuantity(50);
        return product;
    }
}
