/*
package com.workfront.internship.servlets;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.spring.OnlineShopApplication;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Anna Asmangulyan on 8/16/2016.
 *//*

public class ProductsServlet extends HttpServlet {

    private ProductManager productManager;
    private CategoryManager categoryManager;
    private MediaManager mediaManager;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(CategoryManager.class);
        productManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(ProductManager.class);
        mediaManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(MediaManager.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
       // String name = request.getParameter("name");
        System.out.println("productid issssssss" + categoryId);



        int productId = 0;
        List<Product> products = productManager.getProdactsByCategoryID(categoryId);
        request.getSession().setAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            medias.add(mediaManager.getMediaByProductID(productId));
            products.get(i).setMedias(mediaManager.getMediaByProductID(productId));
            request.getSession().setAttribute("medias" + i, medias.get(i));


        }
        List<List<Category>> categories = new ArrayList<List<Category>>();
        List<Category> mainCategories = categoryManager.getCategoriesByParentID(0);

        for(int i=0; i<mainCategories.size(); i++) {
            categories.add(categoryManager.getCategoriesByParentID(mainCategories.get(i).getCategoryID()));

            request.getSession().setAttribute("subcategories" + i, categories.get(i));
        }

        request.getSession().setAttribute("mainCategories", mainCategories);


        request.setAttribute("products", products);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/productsPage.jsp");
        dispatcher.forward(request, response);
    }
}
*/
