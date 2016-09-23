package com.workfront.internship.controller;

import com.workfront.internship.business.BasketManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import org.apache.xpath.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna Asmangulyan on 9/12/2016.
 */
@Controller
public class BasketController {
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;


    @RequestMapping("/addToCart")
    @ResponseBody
    public String addToBasket(HttpServletRequest request) {
        //get user from session...
        User user = (User) request.getSession().getAttribute("user");
        /*if(user == null){
            try {
                response.sendRedirect("signin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //getting parameters from request... productId and quantity...
        String productIdStr = request.getParameter("productId");
        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String sizeOption = (request.getParameter("sizeOption"));

        Product product = productManager.getProduct(productId);
        basketManager.addToBasket(user, product, sizeOption, quantity);

        user.setBasket(basketManager.getCurrentBasket(user));

        List<OrderItem> orderItems = basketManager.showItemsInCurrentBasket(user);
        int numberOfItemsInBasket = 0;
        for (OrderItem orderItem : orderItems) {

            numberOfItemsInBasket += orderItem.getQuantity();

        }
        request.getSession().setAttribute("number", numberOfItemsInBasket);

        String str = Integer.toString(numberOfItemsInBasket);

        return str;
    }

    @RequestMapping("/showCartContent")
    public String showCartContent(HttpServletRequest request) {

        //get items in basket...
        List<OrderItem> orderItemList = basketManager.showItemsInCurrentBasket((User) request.getSession().getAttribute("user"));

        //get one media for each product...
        List<Media> medias = new ArrayList<>();

        int productId;
        for (int i = 0; i < orderItemList.size(); i++) {
            productId = orderItemList.get(i).getProduct().getProductID();
            medias = mediaManager.getMediaByProductID(productId);
            request.setAttribute("media" + productId, medias.get(0));
        }

        //set Attributes to request...
        request.setAttribute("orderItemList", orderItemList);

        return "basketContent";
    }

    @RequestMapping("/deleteItemFromBasket")
    public String deleteItemFromBasket(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        int id = Integer.parseInt(request.getParameter("itemId"));

        basketManager.deleteFromBasket(user, id);

        List<OrderItem> orderItemList = basketManager.showItemsInCurrentBasket((User) request.getSession().getAttribute("user"));
        //get one media for each product...
        List<Media> medias = new ArrayList<>();
        int productId;
        for (int i = 0; i < orderItemList.size(); i++) {
            productId = orderItemList.get(i).getProduct().getProductID();
            medias = mediaManager.getMediaByProductID(productId);
            request.setAttribute("media" + productId, medias.get(0));
        }
        //set Attributes to request...
        request.setAttribute("orderItemList", orderItemList);
        request.getSession().setAttribute("number", orderItemList.size());

        return "basketContent";
    }

    @RequestMapping("/updateBasket")
    @ResponseBody
    public String updateBasket(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        int id = Integer.parseInt(request.getParameter("orderItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        //get orderItem by the given id...
        OrderItem orderItem = basketManager.getOrderItemByItemID(id);

        //change quantity...
        int oldQuantity = orderItem.getQuantity();

        orderItem.setQuantity(quantity);
        //update basket...
        basketManager.updateBasket(user, orderItem);


        return Integer.toString(oldQuantity - quantity);

    }
}
