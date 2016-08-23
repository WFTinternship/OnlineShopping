/*
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

*/
/**
 * Created by Anna Asmangulyan on 8/10/2016.
 *//*

public class ProductServlet extends HttpServlet {

    private ProductManager productManager;


    @Override
    public void init() throws ServletException {
        super.init();
        productManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(ProductManager.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));

        Product product = productManager.getProduct(productId);

        request.setAttribute("product", product);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/productPage.jsp");
        dispatcher.forward(request, response);


}
}
*/
