package com.workfront.internship.servlets;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.User;
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
public class RegistrationServlet extends HttpServlet {
    private DataSource dataSource;
    private UserManager userManager;

    public RegistrationServlet() throws IOException, SQLException {
        super();
        dataSource = DataSource.getInstance();
        userManager = new UserManagerImpl(dataSource);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repeat password");
        User user = new User();
        user.setFirstname(firstname).setLastname(lastname).setUsername(username).setEmail(email).setPassword(password).setAccessPrivilege("user").setConfirmationStatus(true);
        int id = userManager.createAccount(user);

        request.setAttribute("user", user);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/userPage.jsp");
        dispatcher.forward(request, response);
    }
}
