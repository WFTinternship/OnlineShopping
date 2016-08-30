package controllerTest;

import com.workfront.internship.common.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class TestHelper {


    public static Category getTestCategory() {
        Random random = new Random();
        int x = random.nextInt(100000);
        Category category = new Category();
        category.setName("oldCategory" + x).setCategoryID(x).setParentID(0);
        return category;
    }

    public static Media getTestMedia(){
        Product testProduct = getTestProduct();
        Random random = new Random();
        int x = random.nextInt(100000);
        Media media = new Media();
        media.setMediaID(x).setMediaPath("some path" + x).setProductID(testProduct.getProductID());
        return media;
    }
    public static Product getTestProduct(){
        Category category = new Category();
        category.setCategoryID(1).setName("hat");
        Product product = new Product();
        product.setName("baby hat").
                setPrice(50).
                setDescription("color:white").
                setShippingPrice(1).
                setQuantity(50).
                setCategory(category).
                setProductID(1);
        return product;
    }
    public static User getTestUser() {
        User user = new User();
        user.setUserID(5).setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        Address address = new Address();
        address.setAddress("someAddress");
        List<Address> addressess = new ArrayList<>();
        addressess.add(address);
        user.setShippingAddresses(addressess);
        return user;
    }
}
