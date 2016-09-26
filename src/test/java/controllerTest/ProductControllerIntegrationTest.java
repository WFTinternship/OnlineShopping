package controllerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.HomePageController;
import com.workfront.internship.controller.ProductController;
import com.workfront.internship.spring.TestConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;
import static controllerTest.TestHelper.getTestCategory;
import static controllerTest.TestHelper.getTestMedia;
import static controllerTest.TestHelper.getTestProduct;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 9/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {
    @Autowired
    private ProductController productController;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private MediaManager mediaManager;

    private HttpServletRequestMock testRequest;
    private Product testProduct;
    private Media testMedia;
    private Category testCategory;
    private Category testParentCategory;

    @Before
    public void setUp() {
        testRequest = new HttpServletRequestMock();
        testProduct = getTestProduct();
        testMedia = getTestMedia();
        testCategory = getTestCategory();



        categoryManager.createNewCategory(testCategory);

        testProduct.setCategory(testCategory);
        productManager.createNewProduct(testProduct);
        testMedia.setProductID(testProduct.getProductID());
        mediaManager.insertMedia(testMedia);


    }

    @After
    public void tearDown() {
        productManager.deleteProduct(testProduct.getProductID());
        categoryManager.deleteCategory(testCategory.getCategoryID());
        categoryManager.deleteCategory(testParentCategory.getCategoryID());
    }
    @Test
    public void getProducts(){
        testParentCategory = new Category();
        testParentCategory.setParentID(0).setCategoryID(testCategory.getParentID()).setName("parentCategoryName");
        categoryManager.createNewCategory(testParentCategory);
        testRequest.setParameter("id", Integer.toString(testCategory.getCategoryID()));
        //testing method...
        String result = productController.getProducts(testRequest);

        Object object1 = testRequest.getAttribute("products");
        Object object = testRequest.getAttribute("medias0");


        assertNotNull(object);
        assertNotNull(object1);

        Assert.assertEquals("did not get a right page", result, "productsPage");
    }
}