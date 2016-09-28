package com.workfront.internship.controller;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.SizeManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
@Controller
public class HomePageController {
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private SizeManager sizeManager;

    @RequestMapping("/")
    public String getHomePage(HttpServletRequest request) {

        getProductsForHomePage(request);

        getCategories(request);

        return "index";
    }

    public void getProductsForHomePage(HttpServletRequest request) {
        int productId = 0;

        List<Product> products = productManager.getLimitedNumberOfProducts();



        request.setAttribute("products", products);

        List<List<Media>> medias = new ArrayList<List<Media>>();
        List<Media> ms = null;

        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            ms = mediaManager.getMediaByProductID(productId);
            medias.add(ms);
            products.get(i).setMedias(ms);
            request.getSession().setAttribute("medias" + i, medias.get(i));
        }
    }

    public void getCategories(HttpServletRequest request) {
        //get all categories for menu...
        List<Category> categories = categoryManager.getAllCategories();
        request.getSession().setAttribute("categories", categories);
    }

}
