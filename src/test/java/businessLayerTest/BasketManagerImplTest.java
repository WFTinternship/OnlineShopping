package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 8/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ManagerTestConfig.class)
public class BasketManagerImplTest {
    private Basket basket;
    private User user;
    private Product product;
    private Category category;
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ProductManager productManager;


    @Before
    public void setUP() throws IOException, SQLException {

        user = getTestUser();
        userManager.createAccount(user);

        basket = getTestBasket();
        category = getTestCategory();
        product = getTestProduct();

        categoryManager.createNewCategory(category);
        productManager.createNewProduct(product);
        basketManager.createNewBasket(basket);
    }


    @After
    public void tearDown()  {
        userManager.deleteAccount(user.getUserID());
        categoryManager.deleteCategory(category.getCategoryID());

    }
    @Test
    public void addToBasket(){
        System.out.println(user.getUsername() + "  " + product.getName());
        int result = 0;
        result = basketManager.addToBasket(user, product, 3);


        assertFalse(result == 0);

    }
    @Test
    public void createBasket(){
        int id = basketManager.createNewBasket(basket);
        assertFalse(id==0);
    }
    @Test
    public void getCurrentBasket(){
        Basket basket = basketManager.getCurrentBasket(user);
        assertNotNull(basket);
    }

    private Basket getTestBasket() {
        Basket basket = new Basket();
        basket.setUserID(user.getUserID()).setTotalPrice(100).setBasketStatus("current");
        return basket;
    }
    private User getTestUser() {
        user = new User();
        user.setFirstname("Anahit").
                setLastname("galstyan").
                setUsername("anigal").
                setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").
                setConfirmationStatus(true).
                setAccessPrivilege("user");
        return user;
    }
    private Category getTestCategory(){
        category = new Category();
        category.setName("bag");
        return category;
    }
    private Product getTestProduct(){
        product = new Product();
        product.setName("baby bag").
                setShippingPrice(1).
                setPrice(20).
                setDescription("color: white, size: 1").
                setCategory(category).
                setQuantity(50);
        return product;
    }
}
