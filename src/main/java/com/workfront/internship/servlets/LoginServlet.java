package com.workfront.internship.servlets;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.common.User;
import com.workfront.internship.spring.OnlineShopApplication;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Anna Asmangulyan on 8/9/2016.
 */
public class LoginServlet extends HttpServlet {

    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        userManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(UserManager.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String errorString = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();

        user = userManager.login(username, password);
        if (user == null) {

            errorString = "Username or password invalid";


            user = new User();
            user.setUsername(username);
            user.setPassword(password);


            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user);


            // Forward to/signin.jsp
            RequestDispatcher dispatcher //
                    = this.getServletContext().getRequestDispatcher("/signin.jsp");

            dispatcher.forward(request, response);
        } else {
            request.getSession().setAttribute("user", user);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }
}
