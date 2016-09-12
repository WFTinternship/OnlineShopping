package com.workfront.internship.controller;

import com.workfront.internship.business.BasketManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anna Asmangulyan on 9/12/2016.
 */
@Controller
public class BasketController {
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private ProductManager productManager;

    @RequestMapping("/addToCart")
    @ResponseBody
    public String getHomePage(HttpServletRequest request) {
        //get user from session...
        User user = (User) request.getSession().getAttribute("user");
        //getting parameters from request... productId and quantity...
        String productIdStr = request.getParameter("productId");
        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String sizeOption = (request.getParameter("sizeOption"));

        Product product = productManager.getProduct(productId);

        basketManager.addToBasket(user, product, sizeOption, quantity);


        return "index";
    }
}
