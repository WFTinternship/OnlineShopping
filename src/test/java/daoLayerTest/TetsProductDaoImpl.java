package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
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


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class TetsProductDaoImpl{

    Product product = null;
    Category category = null;
    int lastInsertedIndex = 0;
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    DataSource dataSource;

    @Before
    public void setUpDB() throws SQLException, IOException{


        category = TestHelper.getTestCategory();
        categoryDao.insertCategory(category);

        product = TestHelper.getTestProduct();
        product.setCategory(category);
        lastInsertedIndex = productDao.insertProduct(product);

    }

    @After
    public void tearDown() {

        productDao.deleteAllProducts();
        categoryDao.deleteAllCategories();
    }

    @Test
    public void deleteProductByID(){

        productDao.deleteProductByID(product.getProductID());
        Product product1 = productDao.getProductByID(lastInsertedIndex);
        assertNull(product1);
    }
    @Test
    public void deleteProductByName(){

        productDao.deleteProductByName(product.getName());
        Product product1 = productDao.getProductByID(lastInsertedIndex);
        assertNull(product1);
    }
    @Test
    public void insertProduct() {

        Product product1 = TestHelper.getTestProduct();
        product1.setCategory(category);
        product1.setName("newName");
        int insertindex = productDao.insertProduct(product1);

        Product product2 = productDao.getProductByID(insertindex);

        doAssertion(product2, product1);

    }

    @Test
    public void getProductByID() {

        Product product1 = productDao.getProductByID(lastInsertedIndex);

        doAssertion(product1, product);

    }
   @Test
    public void updateProduct() {

        productDao.updateProduct(product);

        Product product1 = productDao.getProductByID(lastInsertedIndex);

        doAssertion(product, product1);

    }

    @Test
    public void deleteAllProducts(){
        productDao.deleteAllProducts();

        List<Product> products = productDao.getAllProducts();

        assertEquals(true, products.isEmpty());
    }
    @Test
    public void getAllProducts(){

        productDao.deleteAllProducts();

        List<Product> products = new ArrayList<>();


        Product product = TestHelper.getTestProduct();
        product.setCategory(category);
        productDao.insertProduct(product);
        products.add(product);

        Product product1 = TestHelper.getTestProduct();
        product1.setName("newProductName").setCategory(category);
        productDao.insertProduct(product1);

        products.add(product1);

        List<Product> products1 = productDao.getAllProducts();

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
        productDao.insertProduct(product1);
        products.add(product1);

        List<Product> products1 = productDao.getProdactsByCategoryID(category.getCategoryID());

        for (int i = 0; i < products1.size(); i++) {
            doAssertion(products.get(i), products1.get(i));
        }
    }
    @Test
    public void getQuantity(){

        productDao.setSizes(product.getProductID(), "3M", 5);

        int quantity = productDao.getQuantity(product.getProductID(), "3M");

        assertEquals("the quantity is not the rigth one", 5, quantity);
    }
    @Test
    public void deleteProductFromProductSizeTable(){
        //testing method...
        productDao.deleteProductFromProductSizeTable(product.getProductID(), "3M");

        int quantity = productDao.getQuantity(product.getProductID(), "3M");

        assertEquals("the item was not deleted", -1, quantity);

    }
    @Test
    public void updateSaledField(){

        productDao.updateSaledField(product.getProductID(), 50);
        //testing method...
        Product product1 = productDao.getProductByID(product.getProductID());

        assertEquals("sale is not equal to 50", 50, product1.getSaled());


    }
    @Test
    public void getProducts(){
        //testing method...
        List<Product> products = productDao.getProducts(product.getCategory().getCategoryID(), "baby");

        doAssertion(product, products.get(0));
    }
    @Test
    public void getSaledProducts(){

        productDao.updateSaledField(product.getProductID(), 30);
        //testing method...
        List<Product> products = productDao.getSaledProducts();

        doAssertion(product, products.get(0));
    }
    @Test
    public void getLimitidNumberOfProducts(){

        List<Product> products = productDao.getLimitedNumberOfProducts();

        doAssertion(product, products.get(0));
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
