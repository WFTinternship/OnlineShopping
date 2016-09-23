package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import com.workfront.internship.controller.ProductController;
import com.workfront.internship.controller.SaleController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controllerTest.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by annaasmangulyan on 9/22/16.
 */
public class SaleControllerUnitTest {

    private CreditcardManager creditcardManager;
    private SalesManager salesManager;
    private AddressManager addressManager;
    private MediaManager mediaManager;
    private BasketManager basketManager;
    private ProductManager productManager;

    private SaleController saleController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private User testUser;
    private Product testProduct;
    private OrderItem testOrderItem;
    private CreditCard testCreditCard;
    private Basket testBasket;
    private Address testAddress;
    private Sale testSale;


    @Before
    public void setUp() {
        saleController = new SaleController();


        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);
        addressManager = mock(AddressManager.class);
        basketManager = mock(BasketManager.class);
        salesManager = mock(SalesManager.class);
        creditcardManager = mock(CreditcardManager.class);

        Whitebox.setInternalState(saleController, "productManager", productManager);
        Whitebox.setInternalState(saleController, "mediaManager", mediaManager);
        Whitebox.setInternalState(saleController, "addressManager", addressManager);
        Whitebox.setInternalState(saleController, "basketManager", basketManager);
        Whitebox.setInternalState(saleController, "salesManager", salesManager);
        Whitebox.setInternalState(saleController, "creditcardManager", creditcardManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);


        when(testRequest.getSession()).thenReturn(testSession);

        testProduct = getTestProduct();
        testUser = getTestUser();
        testOrderItem = getTestOrderItem();
        testCreditCard = getTestCreditCard();
        testBasket = getTestBasket();
        testAddress = getTestAddress();
        testUser.setBasket(testBasket);
        testSale = getTestSale();

        when(creditcardManager.getCreditCardByCardNumber(testCreditCard.getCardNumber())).thenReturn(testCreditCard);
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(testSession.getAttribute("address")).thenReturn(testAddress);
        when(productManager.getProduct(testProduct.getProductID())).thenReturn(testProduct);


    }
    @Test
    public void getCheckoutInfoPage(){

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(testOrderItem);
        when(testSession.getAttribute("user")).thenReturn(testUser);

        when(basketManager.showItemsInCurrentBasket(testUser)).thenReturn(orderItems);

        String result = saleController.getCheckoutInfoPage(testRequest);

        int number = 0;
        for (OrderItem orderItem : orderItems) {

            number += orderItem.getQuantity();

        }

        verify(testSession).setAttribute("number", number);

        assertEquals("did not get checkoutPage page", result, "checkoutPage");
    }
    @Test
    public void getInfoFromCheckoutPage(){
        when(testRequest.getParameter("addressOption")).thenReturn(null);
        when(testSession.getAttribute("user")).thenReturn(testUser);

        String result = saleController.getInfoFromCheckoutPage(testRequest);

        verify(testRequest).getParameter("address");
        verify(testRequest).getParameter("city");
        verify(testRequest).getParameter("country");
        verify(testRequest).getParameter("zip");

        verify(testSession).getAttribute("user");
        verify(addressManager).insertAddress(any(Address.class));


        assertEquals("did not get cartInfo", result, "cartInfo");
    }
    @Test
    public void makeNewSale_success() {

        when(testRequest.getParameter("cvc")).thenReturn(Integer.toString(testCreditCard.getCvc()));
        when(testRequest.getParameter("cartNumber")).thenReturn(testCreditCard.getCardNumber());


        when(salesManager.makeNewSale(any(Sale.class))).thenReturn(5);

        //checking if updateProductSizeAfterSale is called by setting quantity of some sizeOption of some product becomes 0 after sale...
        //giving conditions for that...
        when(productManager.getQuantity(any(Integer.class), any(String.class))).thenReturn(0);

        String result = saleController.makeNewSale(testRequest);


        verify(testRequest).getParameter("month");
        verify(testRequest).getParameter("year");
        verify(testRequest).setAttribute("saleDone", "Your sale is successfully done");
        verify(testSession).setAttribute("number", 0);
        verify(testRequest).getParameter("year");
        verify(productManager).deleteProductFromProductSizeTable(any(Integer.class), any(String.class));
        verify(productManager, never()).deleteProduct(testProduct.getProductID());

        assertEquals("did not get index page", result, "index");

    }
    @Test
    public void makeNewSale_fail_invalid_card_cvc(){
        when(testRequest.getParameter("cvc")).thenReturn(Integer.toString(11));
        when(testRequest.getParameter("cartNumber")).thenReturn(testCreditCard.getCardNumber());


        String result = saleController.makeNewSale(testRequest);

        verify(testRequest).setAttribute("errorCvc", "wrong cvc");

        assertEquals("did not get cartinfo page", result, "cartInfo");

    }
    @Test
    public void makeNewSale_fail_not_enough_balance(){
        testCreditCard.setBalance(0);
        when(testRequest.getParameter("cvc")).thenReturn(Integer.toString(testCreditCard.getCvc()));
        when(testRequest.getParameter("cartNumber")).thenReturn(testCreditCard.getCardNumber());


        String result = saleController.makeNewSale(testRequest);

        verify(testRequest).setAttribute("errorBalance", "not enough balance");



        assertEquals("did not get cartinfo page", result, "cartInfo");

    }
    @Test
    public void makeNewSale_fail_couldnot_insert_sale(){
        when(testRequest.getParameter("cvc")).thenReturn(Integer.toString(testCreditCard.getCvc()));
        when(testRequest.getParameter("cartNumber")).thenReturn(testCreditCard.getCardNumber());


        when(salesManager.makeNewSale(any(Sale.class))).thenReturn(0);

        String result = saleController.makeNewSale(testRequest);

        verify(testRequest).setAttribute("errorString", "Sorry! One of the items may be out of stock! Check your Cart!");

        assertEquals("did not get index page", result, "index");

    }
    @Test
    public void getOrders(){

        String result = saleController.getOrders(testRequest);

        verify(testSession).getAttribute("user");
        verify(salesManager).getSales(testUser);

        assertEquals("did not get orders page", result, "orders");
    }

    @Test
    public void showOrderInfo(){

        when(testRequest.getParameter("saleId")).thenReturn(Integer.toString(testSale.getSaleID()));
        when(salesManager.getSaleBySaleID(testSale.getSaleID())).thenReturn(testSale);
        when(basketManager.getOrderItemsByBasketId(testSale.getBasket().getBasketID())).thenReturn(testBasket.getOrderItems());
        when(mediaManager.getMediaByProductID(testProduct.getProductID())).thenReturn(testProduct.getMedias());

        String result = saleController.showOrderInfo(testRequest);

        verify(testRequest).setAttribute("media" + testProduct.getProductID(), testProduct.getMedias().get(0));
        verify(testRequest).setAttribute("sale", testSale);

        assertEquals("did not get orderPage", result, "orderPage");

    }

    @After
    public void tearDown() {

    }
}
