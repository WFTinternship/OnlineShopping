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

/**
 * Created by annaasmangulyan on 9/15/16.
 */
@Controller
public class saleController {
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


    @RequestMapping("/infoForSale")
    public String getCheckoutInfoPage() {
        return "checkoutPage";
    }

    @RequestMapping("/checkout")
    public String getInfoFromCheckoutPage(HttpServletRequest request) {
        //getting request params...
        String addressOption = request.getParameter("addressOption");
        if (addressOption == null || addressOption == "") {
            addressOption = request.getParameter("newAddress");
            if (addressOption == null || addressOption == "") {
                addressOption = request.getParameter("address");
            }
        }
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String zip = request.getParameter("zip");
        //getting user from session...
        User user = (User) request.getSession().getAttribute("user");
        //create address list and set to user...
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setAddress(addressOption).setCity(city).setCountry(country).setZipCode(zip).setUserID(user.getUserID());
        //save address...
        addressManager.insertAddress(address);
        addresses.add(address);
        user.setShippingAddresses(addresses);
        //setting address attribute to session...
        request.getSession().setAttribute("address", address);


        return "cartInfo";
    }

    @RequestMapping("/makeSale")
    public String makeNewSale(HttpServletRequest request) {
        String cartNumber = request.getParameter("cartNumber");
        int cvc = Integer.parseInt(request.getParameter("cvc"));
        String month = request.getParameter("month");
        String year = request.getParameter("year");
//TODO make separate validate credit card... check balance as well...
        CreditCard creditCard = creditcardManager.getCreditCardByCardNumber(cartNumber);
        if (creditCard == null) {
            String errorNumber = "wrong card number";
            request.setAttribute("errorNumber", errorNumber);
            return "cartInfo";
        }

        if (creditCard.getCvc() != cvc) {
            String errorCvc = "wrong cvc";
            request.setAttribute("errorCvc", errorCvc);
            return "cartInfo";
        }
        //create new sale...
        Sale sale = new Sale();
        //getting address from session...
        Address address = (Address) (request.getSession().getAttribute("address"));

        User user = (User) request.getSession().getAttribute("user");


        sale.setAddressID(address.getAddressID()).
                setBasket(user.getBasket()).
                setCreditCard(creditCard.getCardID()).
                setUserID(user.getUserID()).setDate(new Date());

        salesManager.makeNewSale(sale);

        String saleDone = "Your sale is successfully done";
        //setting success string attribute...
        request.setAttribute("saleDone", saleDone);

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
    public String showOrderInfo(HttpServletRequest request){
        int saleId = Integer.parseInt(request.getParameter("saleId"));

        Sale sale = salesManager.getSaleBySaleID(saleId);



        List<OrderItem> orderItemList = basketManager.getOrderItemsByBasketId(sale.getBasket().getBasketID());
        sale.getBasket().setOrderItems(orderItemList);

        List<Media> medias = new ArrayList<>();
        int productId;
        for(int i = 0; i< orderItemList.size(); i++){
            productId =orderItemList.get(i).getProduct().getProductID();
            medias = mediaManager.getMediaByProductID(productId);
            request.setAttribute("media"+productId, medias.get(0));
        }
        //set Attributes to request...
        request.setAttribute("sale", sale);

        return "orderPage";


    }
}