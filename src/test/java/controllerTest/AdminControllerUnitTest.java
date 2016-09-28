package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import com.workfront.internship.controller.AdminController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;
import static controllerTest.TestHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;



public class AdminControllerUnitTest {

    private AdminController adminController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;
    private MockMultipartFile testMultipartFile;

    private CategoryManager categoryManager;
    private ProductManager productManager;
    private MediaManager mediaManager;
    private SizeManager sizeManager;
    private SalesManager salesManager;

    private Product testProduct;
    private Media testMedia;
    private Category testCategory;
    private Size testSize;
    private List<Size> sizes;
    private List<Product> products;
    private List<Category> categories;
    private ServletContext mockServletContext;



    @Before
    public void setUp(){
        adminController = new AdminController();

        categoryManager = mock(CategoryManager.class);
        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);
        sizeManager = mock(SizeManager.class);
        mockServletContext = mock(ServletContext.class);
        salesManager = mock(SalesManager.class);

        Whitebox.setInternalState(adminController, "categoryManager", categoryManager);
        Whitebox.setInternalState(adminController, "productManager", productManager);
        Whitebox.setInternalState(adminController, "mediaManager", mediaManager);
        Whitebox.setInternalState(adminController, "sizeManager", sizeManager);
        Whitebox.setInternalState(adminController, "salesManager", salesManager);

        testRequest = mock(HttpServletRequest.class);
        testMultipartFile = mock(MockMultipartFile.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getSession()).thenReturn(testSession);

        testProduct = getTestProduct();
        testMedia = getTestMedia();
        testCategory=getTestCategory();
        testSize = getTestSize();
        sizes = new ArrayList<>();
        sizes.add(testSize);
        products = new ArrayList<>();
        products.add(testProduct);
        categories = new ArrayList<>();
        categories.add(testCategory);

        when(testSession.getAttribute("product")).thenReturn(testProduct);
        when(productManager.getAllProducts()).thenReturn(products);

        when(categoryManager.getAllCategories()).thenReturn(categories);
        when(testSession.getAttribute("category")).thenReturn(testCategory);

    }


    @After
    public void tearDown() {

    }
    @Test
    public void getaddProductPage(){
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);

        Category parentCategory = getTestCategory();
        parentCategory.setParentID(0).setCategoryID(testCategory.getParentID());


        when(categoryManager.getAllCategories()).thenReturn(categories);
        when(sizeManager.getSizesByCategoryId(testCategory.getCategoryID())).thenReturn(sizes);
        when(categoryManager.getCategoryByID(testCategory.getParentID())).thenReturn(parentCategory);

        when(testRequest.getParameter("option")).thenReturn("add");
        //testing method... gets addProduct page fields filled an appropriate way depending on the option value
        String result = adminController.getaddProductPage(testRequest);


        verify(testRequest).getParameter("option");
        verify(testSession).setAttribute(eq("product"), any(Product.class));
        verify(testSession).setAttribute("option", "add");
        verify(testRequest).setAttribute("categories", categories);
        verify(testRequest).setAttribute(eq("categoryMap"), anyMap());
        verify(testRequest).setAttribute(eq("sizeMap"), anyMap());

        Assert.assertEquals("did not get addProduct page", result, "addProduct");

    }
    @Test
    public void getEditProductPage_delete_option(){
        //List<Category> categories = new ArrayList<>();
       // categories.add(testCategory);
        String[] chekedValues = {Integer.toString(testProduct.getProductID())};

        List<Product> products1 = new ArrayList<>();

       // when(categoryManager.getAllCategories()).thenReturn(categories);
        when(testRequest.getParameterValues("product")).thenReturn(chekedValues);
        when(testRequest.getParameter("option")).thenReturn("delete");
        when(productManager.getAllProducts()).thenReturn(products1);

        String result = adminController.getEditProductPage(testRequest);


        verify(testRequest).setAttribute("products", products1);
        verify(productManager).deleteProduct(Integer.parseInt(chekedValues[0]));
        assertEquals(result, "products");
    }
    @Test
    public void getEditProductPage_edit_option(){
        String[] chekedValues = {Integer.toString(testProduct.getProductID())};


        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);
        List<Size> sizes = new ArrayList<>();
        sizes.add(testSize);
        Category parentCategory = getTestCategory();
        parentCategory.setParentID(0).setCategoryID(testCategory.getParentID());


        when(categoryManager.getAllCategories()).thenReturn(categories);
        when(testRequest.getParameterValues("product")).thenReturn(chekedValues);
        when(sizeManager.getSizesByCategoryId(testCategory.getCategoryID())).thenReturn(sizes);
        when(categoryManager.getCategoryByID(testCategory.getParentID())).thenReturn(parentCategory);
        when(productManager.getProduct(Integer.parseInt(chekedValues[0]))).thenReturn(testProduct);

        when(testRequest.getParameter("option")).thenReturn("edit");
        //testing method... gets addProduct page fields filled an appropriate way depending on the option value
        String result = adminController.getEditProductPage(testRequest);


        verify(testRequest).getParameter("option");
        verify(testSession).setAttribute("product", testProduct);
        verify(testSession).setAttribute("option", "edit");
        verify(testRequest).setAttribute("categories", categories);
        verify(testRequest).setAttribute(eq("categoryMap"), anyMap());
        verify(testRequest).setAttribute(eq("sizeMap"), anyMap());

        Assert.assertEquals("did not get addProduct page", result, "addProduct");

    }
    @Test
    public void saveProduct_edit_option() throws IOException {
        MultipartFile[] multipartFile = new MockMultipartFile[1];
        multipartFile[0] = testMultipartFile;
        Category testParentCategory = new Category();
        testParentCategory.setCategoryID(testCategory.getParentID());

        when(testSession.getAttribute("option")).thenReturn("edit");
        when(testSession.getServletContext()).thenReturn(mockServletContext);
        when(mockServletContext.getRealPath("/resources/image")).thenReturn("somePath");

        when(categoryManager.getCategoryByID(testCategory.getCategoryID())).thenReturn(testCategory);
        when(categoryManager.getCategoryByID(testCategory.getParentID())).thenReturn(testParentCategory);
        when(sizeManager.getSizesByCategoryId(testParentCategory.getParentID())).thenReturn(sizes);
        when(testRequest.getParameter("quantity0")).thenReturn("10");
        when(testRequest.getParameter("price")).thenReturn("50");
        when(testRequest.getParameter("shippingPrice")).thenReturn("1");
        when(testRequest.getParameter("category")).thenReturn(Integer.toString(testCategory.getCategoryID()));

        //testing method... in option edit...
        String result = adminController.saveProduct(testRequest, multipartFile);

        verify(productManager).updateProduct(any(Product.class));
        verify(testRequest).getParameter("productName");

        verify(testRequest).getParameter("sizeoption0");
        verify(testRequest, times(2)).getParameter("quantity0");
        verify(testRequest).setAttribute("products", products);


        Assert.assertEquals("did not get products page", result, "products");
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

        Assert.assertEquals("did not get products page", result, "products");

    }
    @Test
    public void getAllCategories(){

        //testing method...
        String result = adminController.getAllCategories(testRequest);

        verify(testRequest).setAttribute("categories", categories);

        Assert.assertEquals("did not get products page", result, "categories");

    }
    @Test
    public void getaddCategoryPage(){
        when(testRequest.getParameter("option")).thenReturn("add");

        String result = adminController.getaddCategoryPage(testRequest);


        verify(testRequest).getParameter("option");

        verify(testSession).setAttribute(eq("category"), any(Category.class));
        verify(testRequest).setAttribute("categories", categories);
        verify(testSession).setAttribute("option", "add");


        Assert.assertEquals("did not get addCategory page", result, "addCategory");

    }
    @Test
    public void saveCategory_add(){

        when(testSession.getAttribute("option")).thenReturn("add");
        when(testRequest.getParameter("category")).thenReturn("10");
        //testing method...
        String result = adminController.saveCategory(testRequest);

        verify(categoryManager).createNewCategory(any(Category.class));
        verify(categoryManager, never()).updateCategory(any(Category.class));
        Assert.assertEquals("did not get categories page", result, "categories");

    }
    @Test
    public void saveCategory_edit(){

        when(testSession.getAttribute("option")).thenReturn("edit");
        when(testRequest.getParameter("category")).thenReturn("10");
        //testing method...
        String result = adminController.saveCategory(testRequest);

        verify(categoryManager, never()).createNewCategory(any(Category.class));
        verify(categoryManager).updateCategory(any(Category.class));
        Assert.assertEquals("did not get categories page", result, "categories");

    }
    @Test
    public void editCategory_edit(){

        String[] checkedValues = {Integer.toString(testCategory.getCategoryID())};
        when(testRequest.getParameterValues("category")).thenReturn(checkedValues);
        when(testRequest.getParameter("option")).thenReturn("edit");
        when(categoryManager.getCategoryByID(Integer.parseInt(checkedValues[0]))).thenReturn(testCategory);

        String result = adminController.getEditCategoryPage(testRequest);

        verify(testSession).setAttribute("category", testCategory);
        verify(testRequest).setAttribute("categories", categories);
        verify(testSession).setAttribute("option", "edit");
        verify(categoryManager, never()).deleteCategory(any(Integer.class));

        Assert.assertEquals("did not get addCategory page", result, "addCategory");
    }
    @Test
    public void editCategory_delete(){
        List<Category> categories1 = new ArrayList<>();

        String[] checkedValues = {Integer.toString(testCategory.getCategoryID())};
        when(testRequest.getParameterValues("category")).thenReturn(checkedValues);
        when(testRequest.getParameter("option")).thenReturn("delete");
        when(categoryManager.getCategoryByID(Integer.parseInt(checkedValues[0]))).thenReturn(testCategory);
        when(categoryManager.getAllCategories()).thenReturn(categories1);

        String result = adminController.getEditCategoryPage(testRequest);

        verify(testRequest).setAttribute("categories", categories1);
        verify(categoryManager).deleteCategory(Integer.parseInt(checkedValues[0]));

        Assert.assertEquals("did not get ategories page", result, "categories");
    }
    @Test
    public void getAllOrders(){

        Sale sale = getTestSale();
        List<Sale> sales = new ArrayList<>();

        sales.add(sale);

        when(salesManager.getAllSales()).thenReturn(sales);

        //testing method...
        String result = adminController.getAllOrders(testRequest);

        verify(testRequest).setAttribute("orders", sales);
        Assert.assertEquals("did not get allOrders page", result, "allOrders");

    }
    @Test
    public void changeStatus(){

        when(testRequest.getParameter("saleId")).thenReturn("10");
        when(testRequest.getParameter("status")).thenReturn("delivered");

        String result = adminController.changeStatus(testRequest);

        verify(salesManager).updateSaleStatus(10, "delivered");
        Assert.assertEquals("status is not changed", result, "status is changed");
    }
    @Test
    public void makeDiscount(){

        String[] checkedValues = {Integer.toString(testProduct.getProductID())};
        when(testSession.getAttribute("checkedValues")).thenReturn(checkedValues);
        when(testRequest.getParameter("discount")).thenReturn("20");
        when(productManager.getSaledProducts()).thenReturn(products);

        String result = adminController.makeDiscount(testRequest);

        verify(productManager).updateSaleField(Integer.parseInt(checkedValues[0]), 20);
        verify(testRequest).setAttribute("products", products);

        Assert.assertEquals("can not get saledProducts page", result, "saledProducts");

    }
    @Test
    public void sale(){

        when(productManager.getSaledProducts()).thenReturn(products);

        String  result = adminController.sale(testRequest);

        verify(testRequest).setAttribute("products", products);

        Assert.assertEquals("can not get saledProducts page", result, "saledProducts");


    }
}
