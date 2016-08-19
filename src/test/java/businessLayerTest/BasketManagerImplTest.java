package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import com.workfront.internship.dao.LegacyDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by Workfront on 8/1/2016.
 */
public class BasketManagerImplTest {
    private Basket basket;
    private User user;
    private Product product;
    private Category category;
    private BasketManager basketManager;
    private UserManager userManager;
    private CategoryManager categoryManager;
    private ProductManager productManager;
    LegacyDataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = LegacyDataSource.getInstance();
        basketManager = new BasketManagerImpl(dataSource);
        userManager = new UserManagerImpl();
        Whitebox.setInternalState(userManager, "dataSource", dataSource);
        categoryManager = new CategoryManagerImpl(dataSource);
        productManager = new ProductManagerImpl(dataSource);

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
