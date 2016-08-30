package com.workfront.internship.controller;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
@Controller
public class UserController{

        @Autowired
        private UserManager userManager;

    @RequestMapping("/signin")
    public String login(HttpServletRequest request) {
        String errorString = null;


        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");

        User user = userManager.login(username, password);
        if (user == null) {

            errorString = "Username or password invalid";


            user = new User();
            user.setUsername(username);
            user.setPassword(password);


            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            request.getSession().setAttribute("user", user);


            // Forward to/signin.jsp

            return "signin";
        } else {
            request.getSession().setAttribute("user", user);
            return "index";
        }
    }
    @RequestMapping("/registration")
    public String registration(HttpServletRequest request) {
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

            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            // request.setAttribute("user", user);


            // Forward to/signin.jsp
            return ("registration");
        } else {

            request.getSession().setAttribute("user", user);
            return ("index");
        }

    }

    @RequestMapping("/login")
    public String getLoginPage() {

        return "signin";
    }

    @RequestMapping("/createaccount")
    public String getRegistrationPage() {

        return "registration";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute("user", null);
        return "index";
    }

}
