<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="com.workfront.internship.dao.DataSource" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="com.workfront.internship.dao.MediaDao" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.business.*" %>
<%@ page import="com.workfront.internship.common.Category" %><%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/10/2016
  Time: 2:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>
    <title>Search Box</title>

    <!-- CSS styles for standard search box -->
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">

</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="image"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
<div class="wrapper1">
    <form  method="get" action="http://www.google.com"><br><br><br><br>
        <select name="category">
            <option value="all" selected>All</option>
            <option value="hat">Hat</option>
            <option value="shoes">Shoes</option>
            <option value="girl dress">Girl dress</option>
            <option value="boy shirt">Boy shirt</option>
        </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input type="submit" value="search" class="button">

    </form><br>
</div>
    <div class="some">
<div class="category">
    <%
        DataSource dataSource=DataSource.getInstance();
        CategoryManager categoryManager = new CategoryManagerImpl(dataSource);
        List<Category> categories = categoryManager.getAllCategories();
        for(int i=0; i<categories.size(); i++){
            System.out.println(categories.get(0).getName());
    %>

        <a href="./cart.jsp" class="categoryName" alt="category"><%=categories.get(0).getName()%></a>


    <%}
    %>
</div>


<%
    User user = (User)request.getSession().getAttribute("user");
    if(user==null){

%>

    <div class="signinRegister" >
        <a href="/signin.jsp"  class="register" >Sign in</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/registration.jsp" class="register">Create account</a>
    </div>

    <div class="clear"></div>

<%}
%>

    <%

        if(user!=null){


    %>
    <a href="./cart.jsp" class="cart">
        <img src="/image/cart.PNG" alt="cart image">
    </a>
    <div class="dropdown">
        <span class="greeting"><%out.print("Hello," + " " + user.getFirstname());%></span>
        <button class="dropbtn">Your account</button>
        <div class="dropdown-content">
            <a href="#">edit account</a>
            <a href="#">your orders</a>
            <a href="#">your wish list</a>
        </div>
    </div>

    <div class="clear"></div>


    </div>
    <%}
    %>

<div>
    <%

        ProductManager productManager = new ProductManagerImpl(dataSource);
        MediaManager mediaManager = new MediaManagerImpl(dataSource);
        List<Media> medias;
            List<Product> products = productManager.getLimitedNumberOfProducts();
        for(int i=0; i<products.size(); i++){

            medias = products.get(i).setMedias(mediaManager.getMediaByProductID(products.get(i).getProductID())).getMedias();
            String path0 = medias.get(0).getMediaPath();
       // String path1 = medias.get(1).getMediaPath();
        System.out.println(path0);

    %>
    <div class="image">
        <a href="./cart.jsp" >
            <img src="<%=medias.get(0).getMediaPath()%>" class="productImage" alt="cart image">

        </a>
        <p><%=products.get(i).getName()%></p>
        <p>$<%=products.get(i).getPrice()%></p>
    </div>

    <%}
    %>
</div>
</div>
</body>
</html>

