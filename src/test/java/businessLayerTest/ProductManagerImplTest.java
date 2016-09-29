package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.failSame;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Anna Asmangulyan on 01.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class ProductManagerImplTest {
    private Product product;
    private Category category;
    private Media media;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private MediaManager mediaManager;
    int lastInsertedIndex;


    @Before
    public void setUP() throws IOException, SQLException {

        category = TestHelper.getTestCategory();
        categoryManager.createNewCategory(category);

        product = TestHelper.getTestProduct();
        product.setCategory(category);

        product.setCategory(category);

        lastInsertedIndex = productManager.createNewProduct(product);

    }

    @After
    public void tearDown() {

        categoryManager.deleteAllCategories();
        productManager.deleteAllProducts();
    }

    @Test
    public void deleteProductByID(){

        productManager.deleteProduct(product.getProductID());
        Product product1 = productManager.getProduct(lastInsertedIndex);
        assertNull(product1);
    }


    @Test
    public void insertProduct() {
        //product was inserted in @Before... now we get and doAssertion...
        Product product2 = productManager.getProduct(lastInsertedIndex);

        doAssertion(product2, product);

    }

    @Test
    public void getProductByID() {

        Product product1 = productManager.getProduct(lastInsertedIndex);

        doAssertion(product1, product);

    }
    @Test
    public void updateProduct() {

        productManager.updateProduct(product);

        Product product1 = productManager.getProduct(lastInsertedIndex);

        doAssertion(product, product1);

    }

    @Test
    public void deleteAllProducts(){
        productManager.deleteAllProducts();

        List<Product> products = productManager.getAllProducts();

        assertEquals(true, products.isEmpty());
    }
    @Test
    public void getAllProducts(){

        productManager.deleteAllProducts();

        List<Product> products = new ArrayList<>();

        Product product = TestHelper.getTestProduct();

        product.setCategory(category);
        productManager.createNewProduct(product);
        products.add(product);

        Product product1 = TestHelper.getTestProduct();

        product1.setName("newProductName").setCategory(category);
        productManager.createNewProduct(product1);

        products.add(product1);

        List<Product> products1 = productManager.getAllProducts();

        for (int i = 0; i < products1.size(); i++) {
            doAssertion(products.get(i), products1.get(i));
        }
    }
    @Test
    public void getProductsByCategoryID(){


        List<Product> products = new ArrayList<>();

        products.add(product);

        Product product1 = TestHelper.getTestProduct();

        product1.setCategory(category);
        product1.setName("newProductName");
        productManager.createNewProduct(product1);
        products.add(product1);

        List<Product> products1 = productManager.getProdactsByCategoryID(category.getCategoryID());

        for (int i = 0; i < products1.size(); i++) {
            doAssertion(products.get(i), products1.get(i));
        }
    }
    @Test
    public void getQuantity(){

        productManager.setSizes(product.getProductID(), "3M", 5);

        int quantity = productManager.getQuantity(product.getProductID(), "3M");

        assertEquals("the quantity is not the rigth one", 5, quantity);
    }
    @Test
    public void deleteProductFromProductSizeTable(){
        //testing method...
        productManager.deleteProductFromProductSizeTable(product.getProductID(), "3M");

        int quantity = productManager.getQuantity(product.getProductID(), "3M");

        assertEquals("the item was not deleted", -1, quantity);

    }
    @Test
    public void updateSaledField(){

        productManager.updateSaleField(product.getProductID(), 50);
        //testing method...
        Product product1 = productManager.getProduct(product.getProductID());

        assertEquals("sale is not equal to 50", 50, product1.getSaled());


    }
    @Test
    public void getProducts(){
        //testing method...
        List<Product> products = productManager.getProducts(product.getCategory().getCategoryID(), "baby");

        doAssertion(product, products.get(0));
    }
    @Test
    public void getSaledProducts(){

        productManager.updateSaleField(product.getProductID(), 30);
        //testing method...
        List<Product> products = productManager.getSaledProducts();

        doAssertion(product, products.get(0));
    }
    @Test
    public void getLimitidNumberOfProducts(){

        List<Product> products = productManager.getLimitedNumberOfProducts();

        doAssertion(product, products.get(0));
    }

    @Test
    public void getSizeOptionQuantityMap(){

        Map map = product.getSizeOptionQuantity();

        productManager.setSizes(product.getProductID(), "3M", 5);
        productManager.setSizes(product.getProductID(), "9M", 0);

        Map map1 = productManager.getSizeOptionQuantityMap(product.getProductID());

        assertEquals("quantities are not equal", map.get("3M"), map1.get("3M"));
        assertEquals("quantities are not equal",map.get("9M"), map1.get("9M"));

    }

    private void doAssertion(Product product, Product product1){
        assertEquals(product.getProductID(), product1.getProductID());
        assertEquals(product.getName(), product1.getName());
        assertEquals(product.getPrice(), product1.getPrice());
        assertEquals(product.getShippingPrice(), product1.getShippingPrice());
        // assertEquals(product.getQuantity(), product1.getQuantity());
        assertEquals(product.getCategory().getCategoryID(), product1.getCategory().getCategoryID());
        assertEquals(product.getDescription(), product1.getDescription());
    }

}
