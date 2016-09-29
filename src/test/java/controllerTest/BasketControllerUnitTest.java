package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.controller.BasketController;
import com.workfront.internship.controller.SaleController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static controllerTest.TestHelper.*;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by annaasmangulyan on 9/23/16.
 */
public class BasketControllerUnitTest {

    private BasketManager basketManager;
    private ProductManager productManager;
    private MediaManager mediaManager;

    private BasketController basketController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private User testUser;
    private Product testProduct;
    private OrderItem testOrderItem;
    private Basket testBasket;

    @Before
    public void setUp() {
        basketController = new BasketController();


        productManager = mock(ProductManager.class);
        mediaManager = mock(MediaManager.class);
        basketManager = mock(BasketManager.class);


        Whitebox.setInternalState(basketController, "productManager", productManager);
        Whitebox.setInternalState(basketController, "mediaManager", mediaManager);
        Whitebox.setInternalState(basketController, "basketManager", basketManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);


        when(testRequest.getSession()).thenReturn(testSession);

        testProduct = getTestProduct();
        testUser = getTestUser();
        testOrderItem = getTestOrderItem();
        testBasket = getTestBasket();
        testUser.setBasket(testBasket);


        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(productManager.getProduct(testProduct.getProductID())).thenReturn(testProduct);

    }

    @Test
    public void addToBasket() {
        when(testRequest.getParameter("productId")).thenReturn(Integer.toString(testProduct.getProductID()));
        when(testRequest.getParameter("quantity")).thenReturn(Integer.toString(5));
        when(testRequest.getParameter("sizeOption")).thenReturn("3M");
        when(basketManager.showItemsInCurrentBasket(testUser)).thenReturn(testBasket.getOrderItems());

        String result = basketController.addToBasket(testRequest);


        int number = 0;
        for (OrderItem orderItem : testBasket.getOrderItems()) {

            number += orderItem.getQuantity();

        }

        verify(basketManager).addToBasket(testUser, testProduct, "3M", 5);

        verify(testSession).setAttribute("number", number);

        assertEquals("wrong quantity", result, Integer.toString(number));
    }
    @Test
    public void showCartContent(){
        when(basketManager.showItemsInCurrentBasket(testUser)).thenReturn(testBasket.getOrderItems());
        when(mediaManager.getMediaByProductID(testProduct.getProductID())).thenReturn(testProduct.getMedias());

        String result = basketController.showCartContent(testRequest);

        verify(testRequest).setAttribute("media" + testProduct.getProductID(), testProduct.getMedias().get(0));
        verify(testRequest).setAttribute("orderItemList", testBasket.getOrderItems());

        assertEquals("can not get basketContent page", result, "basketContent");
    }
    @Test
    public void deleteItemFromBasket(){
        when(testRequest.getParameter("itemId")).thenReturn("5");
        when(basketManager.showItemsInCurrentBasket(testUser)).thenReturn(testBasket.getOrderItems());

        String result = basketController.deleteItemFromBasket(testRequest);

        verify(basketManager).deleteFromBasket(testUser, 5);
        verify(testRequest).setAttribute("orderItemList", testBasket.getOrderItems());
        verify(testSession).setAttribute("number", testBasket.getOrderItems().size());
        assertEquals("can not get basketContent page", result, "basketContent");

    }
    @Test
    public void updateBasket(){
        when(testRequest.getParameter("orderItemId")).thenReturn(Integer.toString(testOrderItem.getOrderItemID()));
        when(testRequest.getParameter("quantity")).thenReturn("1");
        when(basketManager.getOrderItemByItemID(testOrderItem.getOrderItemID())).thenReturn(testOrderItem);

        testOrderItem.setQuantity(1);

        String result = basketController.updateBasket(testRequest);

        verify(basketManager).updateBasket(testUser, testOrderItem);
        assertEquals("can not get basketContent page", result, Integer.toString(testOrderItem.getQuantity() - 1));

    }

}
