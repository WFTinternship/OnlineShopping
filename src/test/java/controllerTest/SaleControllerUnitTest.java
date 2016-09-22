package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.User;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private OrderItem testOrderItem;

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

        //testProduct = getTestProduct();
        //testMedia = getTestMedia();
        testUser = getTestUser();
        testOrderItem = getTestOrderItem();


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


    @After
    public void tearDown() {

    }
}
