package controllerTest;

import com.workfront.internship.common.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class TestHelper {


    public static Category getTestCategory() {
        Random random = new Random();
        int x = random.nextInt(100000);
        Category category = new Category();
        category.setName("oldCategory" + x).setCategoryID(x).setParentID(2);
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
        Product product = new Product();
        Map<String, Integer> sizeQuantity = new HashMap<>();
        sizeQuantity.put("3M", 5);
        product.setName("baby hat").
                setPrice(50).
                setDescription("color:white").
                setShippingPrice(1).
                setCategory(getTestCategory()).
                setProductID(1).setSizeOptionQuantity(sizeQuantity);
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
    public static OrderItem getTestOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setBasketID(1).setQuantity(2).
                setProduct(getTestProduct()).setSizeOption("3M");
        return orderItem;
    }
    public static  Basket getTestBasket(){
        Basket basket = new Basket();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(getTestOrderItem());

        basket.setBasketID(1).
                setOrderItems(orderItems).
                setTotalPrice(orderItems.get(0).
                        getProduct().getPrice()*2).
                setBasketStatus("current");

        return basket;

    }

}
