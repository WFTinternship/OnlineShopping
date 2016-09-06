package controllerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.AdminController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;
import static controllerTest.TestHelper.getTestCategory;
import static controllerTest.TestHelper.getTestMedia;
import static controllerTest.TestHelper.getTestProduct;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


/**
 * Created by Anna Asmangulyan on 8/31/2016.
 */
public class AdminControllerUnitTest {

    private AdminController adminController;
    private HttpServletRequest testRequest;
    private HttpServletResponse testResponce;
    private HttpSession testSession;

    private CategoryManager categoryManager;
    private ProductManager productManager;
    private MediaManager mediaManager;

    private Product testProduct;
    private Media testMedia;
    private Category testCategory;

    @Before
    public void setUp(){
        adminController = new AdminController();

        categoryManager = mock(CategoryManager.class);
        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);

        Whitebox.setInternalState(adminController, "categoryManager", categoryManager);
        Whitebox.setInternalState(adminController, "productManager", productManager);
        Whitebox.setInternalState(adminController, "mediaManager", mediaManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        testResponce = mock(HttpServletResponse.class);

        when(testRequest.getSession()).thenReturn(testSession);


        testProduct = getTestProduct();
        testMedia = getTestMedia();
        testCategory=getTestCategory();
    }


    @After
    public void tearDown() {

    }
    @Test
    public void getaddProductPage(){
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);

        when(categoryManager.getAllCategories()).thenReturn(categories);
        when(testRequest.getParameter("option")).thenReturn("add");
        //testing method... gets addProduct page fields filled an appropriate way depending on the option value
        String result = adminController.getaddProductPage(testRequest);

        verify(testSession).setAttribute("option", "add");
        verify(testSession).setAttribute(eq("product"), any(Product.class));
        verify(testSession).setAttribute("categories", categories);

        assertEquals(result, "addProduct");

    }
    @Test
    public void getEditProductPage_delete_option(){
        //List<Category> categories = new ArrayList<>();
       // categories.add(testCategory);

        List<Product> products = new ArrayList<>();
        products.add(testProduct);

       // when(categoryManager.getAllCategories()).thenReturn(categories);
        when(testRequest.getParameter("product")).thenReturn(Integer.toString(testProduct.getProductID()));
        when(testRequest.getParameter("option")).thenReturn("delete");
        when(testSession.getAttribute("products")).thenReturn(products);

        String result = adminController.getEditProductPage(testRequest);


        verify(testSession).setAttribute("products", products);
        assertEquals(result, "products");
    }
    @Test
    public void getEditProductPage_edit_option(){
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);

        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        when(categoryManager.getAllCategories()).thenReturn(categories);
        when(productManager.getProduct(testProduct.getProductID())).thenReturn(testProduct);
        when(testRequest.getParameter("product")).thenReturn(Integer.toString(testProduct.getProductID()));
        when(testRequest.getParameter("option")).thenReturn("edit");
        when(testSession.getAttribute("products")).thenReturn(products);

        String result = adminController.getEditProductPage(testRequest);

        verify(productManager).getProduct(testProduct.getProductID());
        verify(testSession).setAttribute("product", testProduct);
        verify(testSession).setAttribute("categories", categories);

        assertEquals(result, "addProduct");
    }
    @Test
    public void saveProduct_edit_option() throws IOException {
        when(testSession.getAttribute("option")).thenReturn("edit");
        when(testSession.getAttribute("product")).thenReturn(testProduct);

        //testing method... in option edit...
      //  String result = adminController.saveProduct(testResponce,testRequest);

        verify(productManager).updateProduct(any(Product.class));
     //   assertEquals("admin", result);
    }
    @Test
    public void saveProduct_add_option() throws IOException {
        when(testSession.getAttribute("option")).thenReturn("add");

        //testing method... in option edit...
    //    String result = adminController.saveProduct(testResponce,testRequest);

        verify(productManager).createNewProduct(any(Product.class));
     //   assertEquals("admin", result);
    }
    @Test
    public void getAllProducts(){
        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        when(productManager.getAllProducts()).thenReturn(products);
        when(categoryManager.getCategoryByID(testProduct.getCategory().getParentID())).thenReturn(testCategory);
        //testing method...
        String result = adminController.getAllProducts(testRequest);

        verify(categoryManager, times(products.size())).getCategoryByID(testProduct.getCategory().getParentID());
        verify(testSession).setAttribute("products", products);
        assertEquals("products", result);

    }
    @Test
    public void getAllCategories(){
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);
        when(categoryManager.getAllCategories()).thenReturn(categories);
        //testing method...
        String result = adminController.getAllCategories(testRequest);

        verify(testRequest).setAttribute("categories", categories);
        assertEquals("categories", result);

    }
}
