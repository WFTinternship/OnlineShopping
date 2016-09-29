package controllerTest;


import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.HomePageController;
import com.workfront.internship.spring.TestConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static controllerTest.TestHelper.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 9/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class HomePageControllerIntegrationTest {
    @Autowired
    private HomePageController homePageController;
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

        List<Media> medias = new ArrayList<>();
        medias.add(testMedia);
        testProduct.setMedias(medias);



    }
    @After
    public void tearDown(){

        categoryManager.deleteAllCategories();
    }

    @Test
    public void getProductsForHomePage(){
        //testing method...
        homePageController.getProductsForHomePage(testRequest);

        Object object1 = testRequest.getAttribute("products");
        Object object = testRequest.getSession().getAttribute("medias0");

        assertNotNull(object);
        assertNotNull(object1);

    }

}
