<%@ page import="com.workfront.internship.common.User" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />">

</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="logo"><img src="/resources/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">

        <%

            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");
            List<List<Category>> listofCategoriesList = new ArrayList<List<Category>>();


            int j = 0;
            for (Category category : categories) {

                if (category.getParentID() == 0) {
                    for (Category category1 : categories) {
                        if (category1.getParentID() == category.getCategoryID()) {

                            List<Category> list = new ArrayList<Category>();
                            list.add(category1);
                            listofCategoriesList.add(list);
                        }
                    }
                }
            }
            for (int k = 0; k < listofCategoriesList.size(); k++) {
                for (Category category1 : categories) {
                    if (category1.getParentID() == listofCategoriesList.get(k).get(0).getCategoryID())
                        listofCategoriesList.get(k).add(category1);
                }
            }


        %>
        <form method="get" action="http://www.google.com"><br><br><br><br>
            <select name="category">
                <option value="all" selected>All</option>
                <%for (int i = 0; i < listofCategoriesList.size(); i++) {%>

                <option value="<%=listofCategoriesList.get(i).get(0).getCategoryID()%>"
                        selected><%=listofCategoriesList.get(i).get(0).getName()%>
                </option>

                <%
                    }
                %>

            </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input
                    type="submit" value="search" class="button">

        </form>
        <br>
    </div>
    <div class="some">
        <div class="category">

            <%
                for (int i = 0; i < listofCategoriesList.size(); i++) {%>
            <div class="dropdown">

                <button class="dropbtn" id="dropdown1"><%=listofCategoriesList.get(i).get(0).getName()%>
                </button>

                <div class="dropdown-content" id="dropdown-content1">

                    <% for (int l = 1; l < listofCategoriesList.get(i).size(); l++) {%>

                    <a href="/productsPage?id=<%=listofCategoriesList.get(i).get(l).getCategoryID()%>"><%=listofCategoriesList.get(i).get(l).getName()%>
                    </a>
                    <%
                        }
                    %>
                </div>


            </div>
            <%
                }
            %>

        </div>

        <%
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {

        %>

        <div class="signinRegister">
            <a href="/login" class="register" id="login_button">SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/createaccount" class="register" id="registration_button">CREATE ACCOUNT</a>
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
    <img src="/resources/image/cart.PNG" class="cart" alt="cart image">
</a>
<div class="dropdown">
    <span class="greeting"><%out.print("Hello," + " " + user.getFirstname());%></span>
    <button class="dropbtn" id="your_account">YOUR ACCOUNT</button>
    <div class="dropdown-content">
        <a href="#">edit account</a>
        <a href="#">your orders</a>
        <a href="#">your wish list</a>

        <a href="/logout" id="logout_button">logout</a>
    </div>
</div>

<div class="clear"></div>


</div>
</div>
<%
    }
%>


<div>
    <img src="/resources/image/pic4.jpg" alt="image1" class="backimg1">


    <img src="/resources/image/pic1.jpg" alt="image2" class="backimg2">
</div>
<%

    List<Product> products = (List<Product>) request.getSession().getAttribute("products");
    List<Media> medias;
    for (int i = 0; i < products.size(); i++) {
        medias = (List<Media>) request.getSession().getAttribute("medias" + i);
        int productId = products.get(i).getProductID();

%>
<div class="image">

    <a href="/productPage?id=<%=productId %>" id="productHref">
        <img src="/resources/image/index.png" alt="index" class="index" style="width:80px;">
        <%if (medias.size() == 2) {%>
        <img src="<%=medias.get(0).getMediaPath()%>" class="img1" alt="cart image">
        <img src="<%=medias.get(1).getMediaPath()%>" class="img2" alt="cart image">
        <%}%>
        <%if (medias.size() == 3) {%>
        <img src="<%=medias.get(0).getMediaPath()%>" class="img1" alt="cart image">
        <img src="<%=medias.get(1).getMediaPath()%>" class="img2" alt="cart image">
        <img src="<%=medias.get(2).getMediaPath()%>" class="img3" alt="cart image">
        <%}%>
        <%if (medias.size() == 1) {%>
        <img src="<%=medias.get(0).getMediaPath()%>" alt="cart image">
        <%}%>
        <%if (medias.size() == 0) {%>
        <img src="/resources/image/index.png" alt="cart image">
        <%}%>
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

