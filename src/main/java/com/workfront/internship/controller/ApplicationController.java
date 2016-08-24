package com.workfront.internship.controller;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna Asmangulyan on 8/23/2016.
 */
@Controller
public class ApplicationController {
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private UserManager userManager;

    @RequestMapping("/")
    public String simpleRequest(Model model, HttpServletRequest request){

        int productId = 0;
        List<Product> products = productManager.getLimitedNumberOfProducts();
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

        return "index";
    }

    @RequestMapping("/signin")
    public String simpleRequest(HttpServletRequest request, Model model){
        String errorString = null;

        User user = new User();
        String username = (String)request.getParameter("username");
        String password = (String)request.getParameter("password");

        user = userManager.login(username, password);
        if (user == null) {

            errorString = "Username or password invalid";


            user = new User();
            user.setUsername(username);
            user.setPassword(password);


            // Store information in request attribute, before forward.
            model.addAttribute("errorString", errorString);
            request.getSession().setAttribute("user", user);


            // Forward to/signin.jsp

           return "signin";
        } else {
            request.getSession().setAttribute("user", user);
           return "index";
        }
    }

    @RequestMapping("/productPage")
    public String getProductDescription(HttpServletRequest request, Model model){
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productManager.getProduct(productId);

        model.addAttribute("product", product);
        return "productPage";
    }

    @RequestMapping("/productsPage")
    public String getProducts(HttpServletRequest request, Model model) {

        int productId = 0;
        int categoryId=  Integer.parseInt(request.getParameter("id"));
        List<Product> products = productManager.getProdactsByCategoryID(categoryId);
        model.addAttribute("products", products);
        List<List<Media>> medias = new ArrayList<List<Media>>();
        for (int i = 0; i < products.size(); i++) {
            productId = products.get(i).getProductID();
            medias.add(mediaManager.getMediaByProductID(productId));
            products.get(i).setMedias(mediaManager.getMediaByProductID(productId));
            model.addAttribute("medias" + i, medias.get(i));


        }
        List<List<Category>> categories = new ArrayList<List<Category>>();
        List<Category> mainCategories = categoryManager.getCategoriesByParentID(0);

        for (int i = 0; i < mainCategories.size(); i++) {
            categories.add(categoryManager.getCategoriesByParentID(mainCategories.get(i).getCategoryID()));

            request.getSession().setAttribute("subcategories" + i, categories.get(i));
        }

        request.getSession().setAttribute("mainCategories", mainCategories);


        model.addAttribute("products", products);
        return "productsPage";
    }

    @RequestMapping("/registration")
    public String registration(HttpServletRequest request,Model model) {
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

            /*user = new User();
            user.setUsername(username);
            user.setPassword(password);*/


            // Store information in request attribute, before forward.
            model.addAttribute("errorString", errorString);
            // request.setAttribute("user", user);


            // Forward to/signin.jsp
            return ("registration");
        } else {

            request.getSession().setAttribute("user", user);
            return ("index");
        }

    }
    @RequestMapping("/login")
    public String getLoginPage(){

        return "signin";
    }
    @RequestMapping("/createaccount")
    public String getRegistrationPage(){

        return "registration";
    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
    request.getSession().setAttribute("user", null);
        return "index";
    }

}
