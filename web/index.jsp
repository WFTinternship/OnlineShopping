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
<div class="logo"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
<div class="wrapper1">
    <%
        DataSource dataSource=DataSource.getInstance();
        CategoryManager categoryManager = new CategoryManagerImpl(dataSource);
        List<Category> categories = categoryManager.getAllCategories();


    %>
    <form  method="get" action="http://www.google.com"><br><br><br><br>
        <select name="category">
            <option value="all" selected>All</option>
            <option value="cat1"><%=categories.get(0).getName()%></option>
            <option value="cat2"><%=categories.get(1).getName()%></option>
            <option value="cat3"><%=categories.get(2).getName()%></option>

        </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input type="submit" value="search" class="button">

    </form><br>
</div>
    <div class="some">
<div class="category">




       <div class="dropdown">
           <button class="dropbtn">BABY GIRL</button>
           <div class="dropdown-content">

               <a href="#"><%=categories.get(0).getName()%></a>
               <a href="#"><%=categories.get(1).getName()%></a>
               <a href="#"><%=categories.get(2).getName()%></a>

           </div>
       </div>
    <div class="dropdown">
        <button class="dropbtn">BABY BOY</button>
        <div class="dropdown-content">

            <a href="#"><%=categories.get(0).getName()%></a>
            <a href="#"><%=categories.get(1).getName()%></a>
            <a href="#"><%=categories.get(2).getName()%></a>

        </div>
    </div>
</div>

<%
    User user = (User)request.getSession().getAttribute("user");
    if(user==null){

%>

    <div class="signinRegister" >
        <a href="./signin.jsp"  class="register" id="login_button" >SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/registration.jsp" class="register" id="registration_button">CREATE ACCOUNT</a>
    </div>

    <div class="clear"></div>
    </div>
</div>

<%}
%>

    <%

        if(user!=null){


    %>

    <a href="./cart.jsp" class="cart">
        <img src="/image/cart.PNG" class="cart" alt="cart image">
    </a>
    <div class="dropdown">
        <span class="greeting"><%out.print("Hello," + " " + user.getFirstname());%></span>
        <button class="dropbtn" id="your_account">YOUR ACCOUNT</button>
        <div class="dropdown-content">
            <a href="#">edit account</a>
            <a href="#">your orders</a>
            <a href="#">your wish list</a>
            <a href="/index.jsp?user=null" id="logout_button">logout</a>
        </div>
    </div>

    <div class="clear"></div>


    </div>
</div>
    <%}
    %>




<div>
    <img src="./image/pic4.jpg"  alt="image1" class="backimg1">


    <img src="./image/pic1.jpg"  alt="image2" class="backimg2">
</div>
    <%

        ProductManager productManager = new ProductManagerImpl(dataSource);
        MediaManager mediaManager = new MediaManagerImpl(dataSource);
        List<Media> medias;
        int productId = 0;
            List<Product> products = productManager.getLimitedNumberOfProducts();
        for(int i=0; i<products.size(); i++){
            productId = products.get(i).getProductID();
            medias = products.get(i).setMedias(mediaManager.getMediaByProductID(productId)).getMedias();
            String path0 = medias.get(0).getMediaPath();
       // String path1 = medias.get(1).getMediaPath();
        System.out.println(path0);

    %>

    <div class="image">
        <a href="/productPage?id=<%=productId %>" id="productHref">
            <img src="/image/index.png"  alt="index"  class="index" style="width:80px;">
            <img src="<%=medias.get(0).getMediaPath()%>" class="img1" alt="cart image">
            <img src="<%=medias.get(1).getMediaPath()%>" class=img2 alt="cart image">


        </a>

       <%-- <script>var img1 = document.getElementById("productImage");</script>
        <img src="<%=medias.get(1).getMediaPath()%>" id="productImage2" alt="cart image">
        <script>   img2 = document.getElementById("productImage2");

            img1.onmouseover = function(){
                img2.style.display = "block";
            }

            img1.onmouseout = function(){
                img2.style.display = "none";
            }
        </script>--%>
        <p><%=products.get(i).getName()%></p>
        <p>$<%=products.get(i).getPrice()%></p>
    </div>

    <%}
    %>

</body>
</html>

