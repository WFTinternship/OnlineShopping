package controllerTest;

import com.workfront.internship.common.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class TestHelper {


    public static Category getTestCategory() {
        Category category = new Category();
        category.setName("oldCategory" ).setCategoryID(1).setParentID(2);
        return category;
    }

    public static Media getTestMedia(){

        Random random = new Random();
        int x = random.nextInt(100000);
        Media media = new Media();
        media.setMediaID(x).setMediaPath("some path" + x).setProductID(1);
        return media;
    }
    public static Product getTestProduct(){
        Product product = new Product();
        List<Media> medias = new ArrayList<>();
        medias.add(getTestMedia());
        Map<String, Integer> sizeQuantity = new HashMap<>();
        sizeQuantity.put("3M", 5);
        sizeQuantity.put("9M", 0);
        product.setName("baby hat").
                setPrice(50).
                setDescription("color:white").
                setShippingPrice(1).
                setCategory(getTestCategory()).
                setProductID(2).setSizeOptionQuantity(sizeQuantity).setMedias(medias).setSaled(0);
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
    public static CreditCard getTestCreditCard(){
        CreditCard creditCard = new CreditCard();

        creditCard.setCardID(1).setCardNumber("1234").setCvc(111).setBalance(1000);

        return creditCard;
    }
    public static Address getTestAddress(){
        Address address = new Address();

        address.setAddress("testAddress").
                setCity("Yerevan").setCountry("Armenia").
                setZipCode("0026").setUserID(getTestUser().getUserID()).setAddressID(1);
        return address;
    }
    public static Sale getTestSale(){
        Sale sale = new Sale();
        sale.setUserID(getTestUser().getUserID()).setSaleID(1).
                setCreditCard(getTestCreditCard().getCardID()).
                setBasket(getTestBasket()).setAddressID(getTestAddress().getAddressID());
        return sale;
    }
    public static Size getTestSize(){
        Size size = new Size();
        size.setSizeId(5).setCategoryId(16).setSizeOption("12M");
        return  size;
    }

}
