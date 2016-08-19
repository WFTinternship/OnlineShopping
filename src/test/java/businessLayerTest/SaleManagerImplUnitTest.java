package businessLayerTest;

import com.workfront.internship.business.BasketManager;
import com.workfront.internship.business.BasketManagerImpl;
import com.workfront.internship.business.SalesManager;
import com.workfront.internship.business.SalesManagerImpl;
import com.workfront.internship.common.*;
import com.workfront.internship.dao.LegacyDataSource;
import com.workfront.internship.dao.SaleDao;
import com.workfront.internship.dao.SaleDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class SaleManagerImplUnitTest {
    private Sale sale;
    private SalesManager salesManager;
    private BasketManager basketManager;
    LegacyDataSource dataSource;
    SaleDao saleDao;

    @Before
    public void setUP() throws IOException, SQLException {
        sale = getTestSale();
        salesManager = new SalesManagerImpl(dataSource);
        saleDao = Mockito.mock(SaleDaoImpl.class);
        basketManager = Mockito.mock(BasketManagerImpl.class);
        Whitebox.setInternalState(salesManager, "saleDao", saleDao);
        Whitebox.setInternalState(salesManager, "basketManager", basketManager);

    }
    @After
    public void tearDown() {
        sale = null;
        salesManager = null;
        basketManager = null;
        saleDao = null;

    }
    @Test
    public void makeNewSale(){
        when(saleDao.insertSale(sale)).thenReturn(sale.getSaleID());

        int result = salesManager.makeNewSale(sale);

        assertEquals("sale was not inserted", result, sale.getSaleID());
    }
    @Test(expected = RuntimeException.class)
    public void makeNewSale_invalid_sale(){
        salesManager.makeNewSale(null);
    }
    @Test
    public void getSaleByID(){
        when(saleDao.getSaleBySaleID(sale.getSaleID())).thenReturn(sale);

        Sale sale1 = salesManager.getSaleBySaleID(sale.getSaleID());

        doAssertion(sale, sale1);
    }
    @Test(expected = RuntimeException.class)
    public void getSaleByID_invalidID(){
        salesManager.getSaleBySaleID(-1);
    }
    @Test
    public void getSales(){
        Sale sale1 = getTestSale();
        sale1.setSaleID(60);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);

        User user = new User();
        user.setUserID(100).setRecords(sales);

        when(saleDao.getSales(user.getUserID())).thenReturn(sales);


        List<Sale> sales1 = salesManager.getSales(user);

        doAssertion(sales.get(0), sales1.get(0));
        doAssertion(sales.get(1), sales1.get(1));
    }
    @Test(expected = RuntimeException.class)
    public void getSales_invalid_user(){
        salesManager.getSales(null);
    }
    @Test
    public void getSalesDetailedInfo(){
        Basket basket = new Basket();
        basket.setBasketID(100);
        sale.setBasket(basket);

        Basket basket1 = getTestBasket();

        when(basketManager.getBasket(basket.getBasketID())).thenReturn(basket1);
        Sale sale1 = salesManager.getSalesDetailedInfo(sale);

        doAssertion(basket1.getOrderItems().get(0), sale1.getBasket().getOrderItems().get(0));
        doAssertion(basket1.getOrderItems().get(1), sale1.getBasket().getOrderItems().get(1));
    }
    @Test(expected = RuntimeException.class)
    public void getSalesDetailedInfo_invalid_sale(){
        salesManager.getSalesDetailedInfo(null);
    }
    @Test
    public void deleteSaleByUserID(){
        salesManager.deleteSaleByUserID(1);
        Mockito.verify(saleDao).deletSaleByUserID(1);
    }
    @Test(expected = RuntimeException.class)
    public void deleteSaleByUserID_invalid_userId(){
        salesManager.deleteSaleByUserID(-1);
    }
    @Test
    public void deleteSaleBySaleID(){
        salesManager.deleteSaleBySaleID(1);
        Mockito.verify(saleDao).deleteSaleBySaleID(1);
    }
    @Test(expected = RuntimeException.class)
    public void deleteSaleBySaleID_invalid_userId(){
        salesManager.deleteSaleByUserID(-1);
    }
    @Test
    public void getAllSales(){
        Sale sale1 = getTestSale();
        sale1.setSaleID(60);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);

        when(saleDao.getAllSales()).thenReturn(sales);
        List<Sale> sales1 = salesManager.getAllSales();

        doAssertion(sales.get(0), sales1.get(0));
        doAssertion(sales.get(1), sales1.get(1));

    }
    private Sale getTestSale(){
        Sale sale = new Sale();
        Date date = new Date();
        Basket basket = getTestBasket();
        sale.setBasket(basket).setUserID(1000).setSaleID(50).setAddressID(30).setCreditCard(70).setDate(new Timestamp(date.getTime()));
        return sale;
    }
    private Basket getTestBasket(){
        Basket basket = new Basket();
        basket.setBasketID(100);
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = new Product();
        Product product1 = new Product();
        OrderItem orderItem = new OrderItem();
        OrderItem orderItem1 = new OrderItem();
        product.setProductID(1).setName("hat").setPrice(10).setShippingPrice(1);
        product1.setProductID(2).setName("shoes").setPrice(50).setShippingPrice(1);
        orderItem.setOrderItemID(10).setProduct(product).setBasketID(100).setQuantity(2);
        orderItem1.setOrderItemID(20).setProduct(product1).setBasketID(100).setQuantity(3);
        orderItems.add(orderItem);
        orderItems.add(orderItem1);
        basket.setBasketStatus("saled").setOrderItems(orderItems);
        return basket;
    }
    private void doAssertion(Sale sale, Sale sale1){

        assertEquals("did not get a right saleid", sale.getSaleID(), sale1.getSaleID());
        assertEquals("did not get a right basketid", sale.getBasket().getBasketID(), sale1.getBasket().getBasketID());
        assertEquals("did not get a right cartid", sale.getCreditCardID(), sale1.getCreditCardID());
        assertEquals("did not get a right userid", sale.getUserID(), sale1.getUserID());
        // assertEquals(sale.getDate(), sale1.getDate());
        assertEquals("did not get a right addressid", sale.getAddressID(), sale1.getAddressID());

    }
    private void doAssertion(OrderItem orderItem, OrderItem orderItem1){

        assertEquals(orderItem.getOrderItemID(), orderItem1.getOrderItemID());
        assertEquals(orderItem.getQuantity(), orderItem1.getQuantity());
        assertEquals(orderItem.getProduct().getProductID(), orderItem1.getProduct().getProductID());
        assertEquals(orderItem.getBasketID(), orderItem1.getBasketID());

    }
}
