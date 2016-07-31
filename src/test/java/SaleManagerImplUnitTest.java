import com.workfront.internship.business.SalesManager;
import com.workfront.internship.business.SalesManagerImpl;
import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.Sale;
import com.workfront.internship.dao.DataSource;
import com.workfront.internship.dao.SaleDao;
import com.workfront.internship.dao.SaleDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 31.07.2016.
 */
public class SaleManagerImplUnitTest {
    private Sale sale;
    private SalesManager salesManager;
    DataSource dataSource;
    SaleDao saleDao;

    @Before
    public void setUP() throws IOException, SQLException {
        // dataSource = DataSource.getInstance();
        sale = getTestSale();
        salesManager = new SalesManagerImpl(dataSource);
        saleDao = Mockito.mock(SaleDaoImpl.class);
        Whitebox.setInternalState(salesManager, "saleDao", saleDao);

    }
    @After
    public void tearDown() {

    }

    private Sale getTestSale(){
        Sale sale = new Sale();
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
        sale.setBasket(basket).setUserID(1000).setSaleID(50).setAddressID(30).setCreditCard(70);
        return sale;
    }
}
