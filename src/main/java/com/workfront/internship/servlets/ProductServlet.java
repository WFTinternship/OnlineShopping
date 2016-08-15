package com.workfront.internship.servlets;

import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.ProductManagerImpl;
import com.workfront.internship.common.Product;

import com.workfront.internship.dao.DataSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Anna Asmangulyan on 8/10/2016.
 */
public class ProductServlet extends HttpServlet {
   private DataSource dataSource;
    private ProductManager productManager;

    public ProductServlet() throws IOException, SQLException {
        super();
        dataSource = DataSource.getInstance();
        productManager = new ProductManagerImpl(dataSource);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId=Integer.parseInt(request.getParameter("id"));
        System.out.println("productid issssssss" + productId);

        Product product = productManager.getProduct(productId);

        request.setAttribute("product", product);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/productPage.jsp");
        dispatcher.forward(request, response);
    }
}
