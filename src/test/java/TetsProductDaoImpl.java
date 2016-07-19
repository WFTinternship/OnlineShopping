
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;


public class TetsProductDaoImpl{
    DataSource dataSource;
    Product product = null;
    Category category = null;
    int lastInsertedIndex = 0;
    ProductDao productDao;
    CategoryDao categoryDao;

    @Before
    public void setUpDB() throws SQLException, IOException{
        dataSource = DataSource.getInstance();

        productDao = new ProductDaoImpl(dataSource);

        categoryDao = new CategoryDaoImpl(dataSource);
        category = getRandomCategory();
        categoryDao.insertCategory(category);

        product = getRandomProduct();
        lastInsertedIndex = productDao.insertProduct(product);

    }

    @After
    public void tearDown() {

        productDao.deleteAllProducts();
        categoryDao.deleteAllCategories();
    }

    @Test(expected = RuntimeException.class)
    public void insertProduct_duplicate() {
        productDao.insertProduct(product);
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

        Product product1 = getRandomProduct();
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


        product.setQuantity(product.getQuantity() + 3000);

        productDao.updateProduct(product);

        Product product1 = productDao.getProductByID(lastInsertedIndex);

        doAssertion(product, product1);

    }
    @Test(expected = RuntimeException.class)
    public void updateProduct_duplicate(){

        Product product1 = getRandomProduct();
        product1.setName("newName");
        System.out.println("product1Name   "  +  product1.getName());
        productDao.insertProduct(product1);
        product1.setName(product.getName());
        System.out.println("product1Name   "  +  product1.getName());
        productDao.updateProduct(product1);

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


        Product product = getRandomProduct();
        productDao.insertProduct(product);
        products.add(product);

        Product product1 = getRandomProduct();
        product1.setName("newProductName");
        productDao.insertProduct(product1);
        products.add(product1);

        List<Product> products1 = productDao.getAllProducts();

        for (int i = 0; i < products1.size(); i++) {
            doAssertion(products.get(i), products1.get(i));
        }
    }

    private Category getRandomCategory(){
        category = new Category();
        category.setName("hat");
        return category;
    }

    private Product getRandomProduct(){
        Product product1 = new Product();
        product1.setName("baby hat").setPrice(50).setDescription("color:white").setShippingPrice(1).setQuantity(50).setCategory(category);
        return product1;
    }
    private void doAssertion(Product product, Product product1){
        assertEquals(product.getProductID(), product1.getProductID());
        assertEquals(product.getName(), product1.getName());
        assertEquals(product.getPrice(), product1.getPrice());
        assertEquals(product.getShippingPrice(), product1.getShippingPrice());
        assertEquals(product.getQuantity(), product1.getQuantity());
        assertEquals(product.getCategory().getCategoryID(), product1.getCategory().getCategoryID());
        assertEquals(product.getDescription(), product1.getDescription());
    }



}
