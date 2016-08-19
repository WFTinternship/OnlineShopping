package com.workfront.internship.servlets;

import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Product;
import com.workfront.internship.spring.OnlineShopApplication;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Anna Asmangulyan on 8/16/2016.
 */
public class ProductsServlet extends HttpServlet {

    private ProductManager productManager;

    @Override
    public void init() throws ServletException {
        super.init();
        productManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(ProductManager.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        System.out.println("productid issssssss" + categoryId);
        List<Product> products = productManager.getProdactsByCategoryID(categoryId);


        request.setAttribute("products", products);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/productsPage.jsp");
        dispatcher.forward(request, response);
    }
}
