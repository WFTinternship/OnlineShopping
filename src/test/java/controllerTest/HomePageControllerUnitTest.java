package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.HomePageController;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static controllerTest.TestHelper.getTestCategory;
import static controllerTest.TestHelper.getTestMedia;
import static controllerTest.TestHelper.getTestProduct;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class HomePageControllerUnitTest {

    private CategoryManager categoryManager;
    private ProductManager productManager;
    private MediaManager mediaManager;

    private  HomePageController homePageController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private Product testProduct;
    private Media testMedia;
    private Category testCategory;


    @Before
    public void setUp(){
        homePageController = new HomePageController();

        categoryManager = mock(CategoryManager.class);
        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);

        Whitebox.setInternalState(homePageController, "categoryManager", categoryManager);
        Whitebox.setInternalState(homePageController, "productManager", productManager);
        Whitebox.setInternalState(homePageController, "mediaManager", mediaManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getSession()).thenReturn(testSession);


        testProduct = getTestProduct();
        testMedia = getTestMedia();
        testCategory=getTestCategory();
    }


    @After
    public void tearDown() {

    }

   @Test
    public void getProductsForHomePage(){
       List<Product> products = new ArrayList<>();
       products.add(testProduct);

       List<Media> medias = new ArrayList<>();

       when(productManager.getLimitedNumberOfProducts()).thenReturn(products);
       when(mediaManager.getMediaByProductID(products.get(0).getProductID())).thenReturn(medias);
       //testing method... returns products for the homePage...
       homePageController.getProductsForHomePage(testRequest);

       verify(testSession).setAttribute("products", products);
       verify(mediaManager).getMediaByProductID(products.get(0).getProductID());

       verify(testSession).setAttribute("medias0", medias);


   }
    @Test
    public void getCategories(){

        List<Category> categories =  new ArrayList<>();
        categories.add(getTestCategory());

        when(categoryManager.getAllCategories()).thenReturn(categories);


        //testing method... getting all categories for homePage menu...
        homePageController.getCategories(testRequest);


        verify(testSession).setAttribute("categories", categories);

    }
    @Test
    public void getHomePage(){
        //testing method... returns homePage named index...
        String result = homePageController.getHomePage(testRequest);

        assertEquals(result, "index");
    }


}
