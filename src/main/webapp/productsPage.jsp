
<%@ page import="com.workfront.internship.common.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="com.workfront.internship.business.*" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/16/2016
  Time: 12:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Box</title>

    <!-- CSS styles for standard search box -->
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">
    <script src="/jquery-1.12.1.min.js"></script>

    <script src="/jquery.elevatezoom.js"></script>
    <script src="/jquery.elevateZoom-3.0.8.min.js"></script>
    <script src="/jquery-1.8.3.min.js"></script>

    <script src="/jzoom.min.js"></script>

</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="logo"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">
        <%

            List<Category> mainCategories = (List<Category>)request.getSession().getAttribute("mainCategories");
            List<String> categoriesName = new ArrayList<>();
            for(int i=0; i<mainCategories.size(); i++)
                categoriesName.add(mainCategories.get(i).getName());

        %>
        <form method="get" action="http://www.google.com"><br><br><br><br>
            <select name="category">
                <option value="all" selected>All</option>
                <% for(int i=0; i<categoriesName.size(); i++){%>
                <option value="cat1"><%=categoriesName.get(i)%>
                </option>
                <%}
                %>

            </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input
                    type="submit" value="search" class="button">

        </form>
        <br>
    </div>
    <div class="some">
        <div class="category">


            <%  List<Category> subCategories;
                for(int j=0; j< mainCategories.size(); j++){%>
            <div class="dropdown">

                <button class="dropbtn" id="dropdown1"><%=mainCategories.get(j).getName()%>
                </button>


                <div class="dropdown-content" id="dropdown-content1">
                    <%
                        subCategories = (List<Category>)request.getSession().getAttribute("subcategories" + j);
                        for(int i=0;  i<subCategories.size(); i++ ){%>

                    <a href="/productsPage?id=<%=subCategories.get(i).getCategoryID()%>"><%=subCategories.get(i).getName()%>
                    </a>
                    <%}
                    %>
                </div>


            </div>
            <%}
            %>


        </div>

        <%
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {

        %>

        <div class="signinRegister">
            <a href="./signin.jsp" class="register" id="login_button">SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/registration.jsp" class="register" id="registration_button">CREATE ACCOUNT</a>
        </div>

        <div class="clear"></div>
    </div>
</div>

<%
    }
%>

<%

    if (user != null) {


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
<%
    }
%>


<div>
    <img src="./image/pic4.jpg" alt="image1" class="backimg1">


    <img src="./image/pic1.jpg" alt="image2" class="backimg2">
</div>
<%
    List<Product> products = (List<Product>)request.getSession().getAttribute("products");
    List<Media> medias;
    for(int i=0; i<products.size(); i++){
        medias = (List<Media>)request.getSession().getAttribute("medias" + i);
        int productId = products.get(i).getProductID();

%>
<div class="image">
    <a href="/productPage?id=<%=productId %>" id="productHref">
        <img src="/image/index.png" alt="index" class="index" style="width:80px;">
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
    <p><%=products.get(i).getName()%>
    </p>
    <p>$<%=products.get(i).getPrice()%>
    </p>
</div>

<%
    }
%>


</body>
</html>
