/*
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

*/
/**
 * Created by Anna Asmangulyan on 8/10/2016.
 *//*

public class RegistrationServlet extends HttpServlet {

    private UserManager userManager;

    @Override
    public void init() throws ServletException {
        super.init();
        userManager = OnlineShopApplication.getApplicationContext(getServletContext()).getBean(UserManager.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorString = null;
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repeatpassword");
        User user = new User();
        user.setFirstname(firstname).setLastname(lastname).setUsername(username).setEmail(email).setPassword(password).setAccessPrivilege("user").setConfirmationStatus(true);
        int id = userManager.createAccount(user);
        if (id == 0) {

            errorString = "User with that username already exists";


            // If error, forward to /registration.jsp

            */
/*user = new User();
            user.setUsername(username);
            user.setPassword(password);*//*



            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            // request.setAttribute("user", user);


            // Forward to/signin.jsp
            RequestDispatcher dispatcher //
                    = this.getServletContext().getRequestDispatcher("/registration.jsp");

            dispatcher.forward(request, response);
        } else {

            request.getSession().setAttribute("user", user);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }
}
*/
