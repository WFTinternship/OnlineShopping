package com.workfront.internship.controller;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/")
    public String getHomePage(HttpServletRequest request) {

        getProductsForHomePage(request);

        getCategories(request);

        return "index";
    }
    public void getProductsForHomePage(HttpServletRequest request){

        int productId = 0;
        List<Product> products = productManager.getLimitedNumberOfProducts();
        request.getSession().setAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        List<Media> ms=null;
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            ms = mediaManager.getMediaByProductID(productId);
            medias.add(ms);
            products.get(i).setMedias(ms);
            request.getSession().setAttribute("medias" + i, medias.get(i));


        }

    }
    public void getCategories(HttpServletRequest request){
        List<List<Category>> categories = new ArrayList<List<Category>>();
        List<Category> mainCategories = categoryManager.getCategoriesByParentID(0);

        for (int i = 0; i < mainCategories.size(); i++) {
            categories.add(categoryManager.getCategoriesByParentID(mainCategories.get(i).getCategoryID()));

            request.getSession().setAttribute("subcategories" + i, categories.get(i));
        }
        request.getSession().setAttribute("mainCategories", mainCategories);

    }
}
