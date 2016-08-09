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
 * Created by Anna Asmangulyan on 8/9/2016.
 */
public class LoginServlet extends HttpServlet {
    private DataSource dataSource;
    private UserManager userManager;

    public LoginServlet() throws IOException, SQLException {
        super();
        dataSource = DataSource.getInstance();
        userManager = new UserManagerImpl(dataSource);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userManager.login(username,password);

        request.setAttribute("user", user);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/userPage.jsp");
        dispatcher.forward(request, response);
    }
}
