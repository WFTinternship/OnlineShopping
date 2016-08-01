package daoLayerTest;

import com.workfront.internship.common.*;
import com.workfront.internship.dao.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestForTransaction {

  /*  public static void main(String[] args) throws IOException, SQLException {
        DataSource dataSource = DataSource.getInstance();
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        Category category = new Category();
        category.setCategoryID(44).setName("hat");
        product.setName("baby hat").setProductID(5).setCategory(category).setDescription("color:white").setPrice(50).setShippingPrice(1).setQuantity(40);
        Basket basket = new Basket();
        basket.setBasketID(9).setTotalPrice(100).setBasketStatus("saled").setUserID(10);
        orderItem.setProduct(product).setBasketID(9).setQuantity(5);
       // orderItemDao.insertOrderItem(orderItem);
        SaleDao saleDao = new SaleDaoImpl(dataSource);

        Sale sale = new Sale();
        sale.setAddressID(1).setBasket(basket).setCreditCard(1).setUserID(10);
        saleDao.insertSale(sale);



    }*/
}
