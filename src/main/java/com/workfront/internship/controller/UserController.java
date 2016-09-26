package com.workfront.internship.controller;

import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
@Controller
public class UserController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private BasketManager basketManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;

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
            List<OrderItem> orderItems = basketManager.showItemsInCurrentBasket(user);
            int numberOfItemsInBasket = 0;
            for (OrderItem orderItem : orderItems) {

                numberOfItemsInBasket += orderItem.getQuantity();

            }
            request.getSession().setAttribute("number", numberOfItemsInBasket);
            request.getSession().setAttribute("user", user);
            return "index";
        }
    }

    @RequestMapping("/registration")
    public String registration(HttpServletRequest request) {
        String errorString = null;
        //get parameters for the registration...
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repeatpassword");
        //create new user and set params...
        User user = new User();
        user.setFirstname(firstname).setLastname(lastname).setUsername(username).setEmail(email).setPassword(password).setAccessPrivilege("user").setConfirmationStatus(true);
        int id = userManager.createAccount(user);
        user.setUserID(id);

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

    @RequestMapping("/editAccount")
    public String editAccount(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        user.setShippingAddresses(addressManager.getShippingAddressByUserID(user.getUserID()));
        request.getSession().setAttribute("user", user);

        return "editAccount";


    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {

        request.getSession().setAttribute("user", null);
        return "index";
    }

    @RequestMapping("/deleteAddress")
    public String deleteAddress(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("addressId"));

        //delete address by given id...
        addressManager.deleteAddressesByAddressID(id);

        return "editAccount";
    }

    @RequestMapping("/saveEditedAccount")
    public String saveEditedAccount(HttpServletRequest request) {

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("oldPassword");
        String newpassword = request.getParameter("newPassword");
        String repeadPassword = request.getParameter("repeatPassword");

        if (!newpassword.equals(repeadPassword)) {
            String errorString = "Passwords are not equal";
            request.setAttribute("errorString", errorString);
            return "editAccount";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (!HashManager.getHash(password).equals(user.getPassword())) {
            String errorPass = "incorrect password";

            request.setAttribute("errorPass", errorPass);
            return "editAccount";

        }

        user.setFirstname(firstname).setLastname(lastname).setUsername(username).setEmail(email);

        if (!newpassword.equals("")) {
            user.setPassword(newpassword);
            //update user...
            userManager.editProfile(user);

            return "index";
        } else {
            userManager.editProfileWiyhoutPassword(user);
            return "index";
        }
    }
    @RequestMapping("/addToList")
    @ResponseBody
    public String addToList(HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");

        int productId = Integer.parseInt(request.getParameter("productId"));

        int id = userManager.addToList(user, productManager.getProduct(productId));

        String str;

        if(id == -1){
            str = "the item is already in your wishlist";

        }
        else{
            str = "the item is added to your wishlist";
        }
        request.setAttribute("str", str);

        return str;
    }
    @RequestMapping("/showWishlistContent")
    public String showWishlistContent(HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");

        List<Product> productList = userManager.getList(user);

        List<Media> medias = new ArrayList<>();
        //get medias for the given productList for wishlist...
        int productId;
        for (int i = 0; i < productList.size(); i++) {
            productId = productList.get(i).getProductID();
            medias = mediaManager.getMediaByProductID(productId);
            request.setAttribute("media" + productId, medias.get(0));
        }

        request.setAttribute("productList", productList);

        return "wishList";
    }
    @RequestMapping("/deleteFromList")
    @ResponseBody
    public String deleteFromList(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");

        int id = Integer.parseInt(request.getParameter("productId"));

        userManager.deleteFromList(user, id);

        return "deleted";
    }


}
