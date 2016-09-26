package com.workfront.internship.controller;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by annaasmangulyan on 9/15/16.
 */
@Controller
public class SaleController {
    @Autowired
    private CreditcardManager creditcardManager;
    @Autowired
    private SalesManager salesManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private ProductManager productManager;


    @RequestMapping("/infoForSale")
    public String getCheckoutInfoPage(HttpServletRequest request) {

        User user = (User)request.getSession().getAttribute("user");
        //get number of items in basket...
        List<OrderItem> orderItems = basketManager.showItemsInCurrentBasket(user);
        int number = 0;
        for (OrderItem orderItem : orderItems) {

            number += orderItem.getQuantity();

        }

        request.getSession().setAttribute("number", number);
        return "checkoutPage";
    }

    @RequestMapping("/checkout")
    public String getInfoFromCheckoutPage(HttpServletRequest request) {
        //getting request params...
        String addressOption = request.getParameter("addressOption");
        String fullName = request.getParameter("fullName");
        Address address = new Address();

        if(addressOption !=null && !addressOption.equals("Select")){
            address = addressManager.getAddressByID(Integer.parseInt(addressOption));
            request.getSession().setAttribute("address", address);
            request.getSession().setAttribute("fullName", fullName);
            return "cartInfo";
        }

        else{
            if(addressOption != null) {
                addressOption = request.getParameter("newAddress");
            }
            else {
                addressOption = request.getParameter("address");
            }
            String city = request.getParameter("city");
            String country = request.getParameter("country");
            String zip = request.getParameter("zip");

            //getting user from session...
            User user = (User) request.getSession().getAttribute("user");

            //create address list and set to user...
            List<Address> addresses = new ArrayList<>();

            address.setAddress(addressOption).setCity(city).setCountry(country).setZipCode(zip).setUserID(user.getUserID());

            //save address...
            addressManager.insertAddress(address);
            //set address atribute...
            request.getSession().setAttribute("address", address);
            request.getSession().setAttribute("fullName", fullName);
            return "cartInfo";
            }



    }

    @RequestMapping("/makeSale")
    public String makeNewSale(HttpServletRequest request) {

        String cartNumber = request.getParameter("cartNumber");
        String cvc = request.getParameter("cvc");
        String month = request.getParameter("month");
        String year = request.getParameter("year");

        User user = (User) request.getSession().getAttribute("user");


        CreditCard creditCard = creditcardManager.getCreditCardByCardNumber(cartNumber);
        //credit card validation...
        if(!isCreditCardValid(creditCard,cvc, request)){
            return "cartInfo";
        }

        //create new sale...
        Sale sale = new Sale();
        //getting address from session...
        Address address = (Address) (request.getSession().getAttribute("address"));
        String fullName = (String) (request.getSession().getAttribute("fullName"));

        sale.setAddressID(address.getAddressID()).
                setBasket(user.getBasket()).
                setCreditCard(creditCard.getCardID()).
                setUserID(user.getUserID()).setDate(new Date()).setStatus("not delivered").setFullName(fullName);

        int id = salesManager.makeNewSale(sale);
        if(id == 0){
            String errorString = "Sorry! One of the items may be out of stock! Check your Cart!";

            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            // request.setAttribute("user", user);

            return ("index");
        }
        //update product info if sale is successfully done...
        if (id > 0) {

            updateProductSizeAfterSale(sale.getBasket().getOrderItems());
            System.out.println("" + sale.getBasket().getOrderItems().size());
        }

        String saleDone = "Your sale is successfully done";
        //setting success string attribute...
        request.setAttribute("saleDone", saleDone);
        //make basket empty...
        request.getSession().setAttribute("number", 0);
        user.setBasket(null);
        request.getSession().setAttribute("user", user);

        return "index";
    }

    @RequestMapping("/getOrders")
    public String getOrders(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        //getting all sales of the user...
        List<Sale> orders = salesManager.getSales(user);

        request.setAttribute("orders", orders);

        return "orders";

    }

    @RequestMapping("/showOrderInfo")
    public String showOrderInfo(HttpServletRequest request) {
        int saleId = Integer.parseInt(request.getParameter("saleId"));
        String adminOption = request.getParameter("admin");

        Sale sale = salesManager.getSaleBySaleID(saleId);


        List<OrderItem> orderItemList = basketManager.getOrderItemsByBasketId(sale.getBasket().getBasketID());
        sale.getBasket().setOrderItems(orderItemList);

        List<Media> medias = new ArrayList<>();
        int productId;
        for (int i = 0; i < orderItemList.size(); i++) {
            productId = orderItemList.get(i).getProduct().getProductID();
            medias = mediaManager.getMediaByProductID(productId);
            request.setAttribute("media" + productId, medias.get(0));
        }
        //set Attributes to request...
        request.setAttribute("sale", sale);

        if(adminOption != null){
            return "orderInfo";
        }

        return "orderPage";


    }

    public void updateProductSizeAfterSale(List<OrderItem> orderItems) {
        int quantity;
        int productId;
        Product product;
        int totalQuantity = 0;
        for (OrderItem orderItem : orderItems) {
            productId = orderItem.getProduct().getProductID();
            quantity = productManager.getQuantity(productId, orderItem.getSizeOption());

            //check if quantity is 0 delete from table...
            if (quantity == 0) {
                productManager.deleteProductFromProductSizeTable(productId, orderItem.getSizeOption());
            }
            //check if there remains a product of some size, if no delete from products table...
            product = productManager.getProduct(productId);
            for (Map.Entry<String, Integer> entry : product.getSizeOptionQuantity().entrySet()) {
                totalQuantity += entry.getValue();
            }
            if (totalQuantity == 0){
                productManager.deleteProduct(productId);
            }
        }
    }
    private boolean isCreditCardValid(CreditCard creditCard, String cvc, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (creditCard == null) {
            String errorNumber = "wrong card number";
            request.setAttribute("errorNumber", errorNumber);
            return false;
        }

        if (cvc.equals("") || creditCard.getCvc() != Integer.parseInt(cvc)) {
            String errorCvc = "wrong cvc";
            request.setAttribute("errorCvc", errorCvc);
            return false;
        }
        if (creditCard.getBalance() < user.getBasket().getTotalPrice()) {
            String errorBalance = "not enough balance";
            request.setAttribute("errorBalance", errorBalance);
            return false;
        }
        return true;
    }
}