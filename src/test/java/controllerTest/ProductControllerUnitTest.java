package controllerTest;



import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.ProductController;
import com.workfront.internship.controller.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controllerTest.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class ProductControllerUnitTest {

    private ProductManager productManager;
    private MediaManager mediaManager;
    private CategoryManager categoryManager;

    private ProductController productController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private Product testProduct;
    private Media testMedia;
    private Category testCategory;


    @Before
    public void setUp() {
        productController = new ProductController();


        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);
        categoryManager = mock(CategoryManager.class);

        Whitebox.setInternalState(productController, "productManager", productManager);
        Whitebox.setInternalState(productController, "mediaManager", mediaManager);
        Whitebox.setInternalState(productController, "categoryManager", categoryManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getSession()).thenReturn(testSession);

        testProduct = getTestProduct();
        testMedia = getTestMedia();
        testCategory = getTestCategory();

    }


    @After
    public void tearDown() {

    }

    @Test
    public void getProductDescription() {
        when(testRequest.getParameter("id")).thenReturn("5");
        //testing method...
        String result = productController.getProductDescription(testRequest);

        verify(productManager).getProduct(5);
        verify(testRequest).setAttribute(any(String.class), any(Product.class));

        assertEquals(result, "productPage");
    }

    @Test
    public void getProductsForProductPage() {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        List<Media> medias = new ArrayList<>();
        when(testRequest.getParameter("id")).thenReturn(Integer.toString(testCategory.getCategoryID()));
        when(productManager.getProdactsByCategoryID(testCategory.getCategoryID())).thenReturn(products);
        when(mediaManager.getMediaByProductID(products.get(0).getProductID())).thenReturn(medias);
        //testing method... returns products for the productPage...
        productController.getProductsForProductPage(testRequest);

        verify(testRequest).setAttribute("products", products);
        verify(mediaManager).getMediaByProductID(products.get(0).getProductID());

        verify(testRequest).setAttribute("medias0", medias);


    }

}
