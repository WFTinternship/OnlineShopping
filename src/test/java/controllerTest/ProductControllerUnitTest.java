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
import static org.mockito.Mockito.*;

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
        when(testRequest.getParameter("id")).thenReturn(Integer.toString(testProduct.getProductID()));
        when(productManager.getProduct(testProduct.getProductID())).thenReturn(testProduct);

        //testing method...
        String result = productController.getProductDescription(testRequest);

        verify(productManager).getSizeOptionQuantityMap(testProduct.getProductID());
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

        verify(testRequest).setAttribute("medias0", medias);


    }
    @Test
    public void getSaledProducts(){
        List<Product> products = new ArrayList<>();
        testProduct.setSaled(20);
        products.add(testProduct);

        List<Media> medias = new ArrayList<>();
        when(productManager.getSaledProducts()).thenReturn(products);
        when(mediaManager.getMediaByProductID(products.get(0).getProductID())).thenReturn(medias);
        //testing method...
        productController.getSaledProducts(testRequest);

        verify(testRequest).setAttribute("products", products);
        verify(testRequest).setAttribute("medias0", medias);

    }
    @Test
    public void getLikeProducts(){
        when(testRequest.getParameter("category")).thenReturn(Integer.toString(testCategory.getParentID()));
        when(testRequest.getParameter("searchKey")).thenReturn("str");

        productController.getLikeProducts(testRequest);

        verify(productManager).getLikeStringsByCategory(testCategory.getParentID(), "str");
    }
    @Test
    public void getProductsBySearch(){
        Category childCategory = TestHelper.getTestCategory();
        childCategory.setParentID(testCategory.getCategoryID());
        List<Category> childCategories = new ArrayList<>();

        childCategories.add(childCategory);

        when(testRequest.getParameter("category")).thenReturn(Integer.toString(testCategory.getCategoryID()));
        when(testRequest.getParameter("productName")).thenReturn("str");
        when(categoryManager.getChildCategories(testCategory.getCategoryID())).thenReturn(childCategories);
        when(categoryManager.getCategoryByParentIDANDCategoryName(testCategory.getCategoryID(), "str")).thenReturn(null);

        productController.getProductsBySearch(testRequest);

        verify(productManager, never()).getProdactsByCategoryID(any(Integer.class));
        verify(productManager).getProducts(childCategories.get(0).getCategoryID(), "str");
        verify(testRequest).setAttribute(eq("products"), anyList());
        verify(testRequest).setAttribute(eq("categories"), anyList());


    }

}
