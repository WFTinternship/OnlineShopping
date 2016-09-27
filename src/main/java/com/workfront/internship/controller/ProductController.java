package com.workfront.internship.controller;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Anna Asmangulyan on 8/23/2016.
 */
@Controller
public class ProductController {
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private SizeManager sizeManager;




    @RequestMapping("/productPage")
    public String getProductDescription(HttpServletRequest request) {
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productManager.getProduct(productId);

        //getting and setting sizes...
            Map<String, Integer> sizeOptionQuantityMap = productManager.getSizeOptionQuantityMap(product.getProductID());
            product.setSizeOptionQuantity(sizeOptionQuantityMap);

        request.setAttribute("product", product);
        //request.getSession().setAttribute("categories", categories);

        return "productPage";
    }

    @RequestMapping("/productsPage")
    public String getProducts(HttpServletRequest request) {

        getProductsForProductPage(request);
       // getCategories(request);

        return "productsPage";
    }
    public void getProductsForProductPage(HttpServletRequest request){

        int productId = 0;
        int categoryId = Integer.parseInt(request.getParameter("id"));
        List<Product> products = productManager.getProdactsByCategoryID(categoryId);
        request.setAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        List<Media> medias1 = null;
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            medias1 = mediaManager.getMediaByProductID(productId);
            medias.add(medias1);
            products.get(i).setMedias(medias1);
            request.setAttribute("medias" + i, medias.get(i));



        }

    }
    @RequestMapping("/getSaledProducts")
    public String  getSaledProducts(HttpServletRequest request){
        int productId = 0;

        List<Product> products = productManager.getSaledProducts();
        request.setAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        List<Media> medias1 = null;
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            medias1 = mediaManager.getMediaByProductID(productId);
            medias.add(medias1);
            products.get(i).setMedias(medias1);
            request.setAttribute("medias" + i, medias.get(i));



        }
        return "productsPage";

    }
    @RequestMapping("/getLikeProducts")
    @ResponseBody
    public List<String> getLikeProducts(HttpServletRequest request){

        int parentId = Integer.parseInt(request.getParameter("category"));
        String str = request.getParameter("searchKey");

        return productManager.getLikeStringsByCategory(parentId, str);

    }
    @RequestMapping("/getProductsBySearch")
    public String getProductsBySearch(HttpServletRequest request){
        List<Product> products = new ArrayList<>();
        int productId = 0;

        int categoryId = Integer.parseInt(request.getParameter("category"));
        String searchkey = request.getParameter("productName");

        products = productManager.getProducts(categoryId, searchkey);

        request.setAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        List<Media> medias1 = null;
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            medias1 = mediaManager.getMediaByProductID(productId);
            medias.add(medias1);
            products.get(i).setMedias(medias1);
            request.setAttribute("medias" + i, medias.get(i));
        }
        return "productsPage";
    }


   /* public void getCategories(HttpServletRequest request){

        List<List<Category>> categories = new ArrayList<List<Category>>();
        List<Category> mainCategories = categoryManager.getCategoriesByParentID(0);

        for (int i = 0; i < mainCategories.size(); i++) {
            categories.add(categoryManager.getCategoriesByParentID(mainCategories.get(i).getCategoryID()));

            request.getSession().setAttribute("subcategories" + i, categories.get(i));
        }

        request.getSession().setAttribute("mainCategories", mainCategories);

    }
*/

}




