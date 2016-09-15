package com.workfront.internship.controller;

import com.workfront.internship.business.AddressManager;
import com.workfront.internship.business.CreditcardManager;
import com.workfront.internship.business.SalesManager;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.CreditCard;
import com.workfront.internship.common.Sale;
import com.workfront.internship.common.User;
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

        CreditCard creditCard = creditcardManager.getCreditCardByCardNumber(cartNumber);
        if(creditCard == null){
            String errorNumber = "wrong card number";
            request.setAttribute("errorNumber", errorNumber);
            return "cartInfo";
        }

        if(creditCard.getCvc() != cvc) {
            String errorCvc = "wrong cvc";
            request.setAttribute("errorCvc", errorCvc);
            return "cartInfo";
        }
        //create new sale...
        Sale sale = new Sale();
        //getting address from session...
        Address address = (Address)(request.getSession().getAttribute("address"));

        User user = (User)request.getSession().getAttribute("user");


        sale.setAddressID(address.getAddressID()).
                setBasket(user.getBasket()).
                setCreditCard(creditCard.getCardID()).
                setUserID(user.getUserID()).setDate(new Date());

        salesManager.makeNewSale(sale);

        return "saleDone";
    }
}
