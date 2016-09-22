<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.workfront.internship.common.*" %><%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 9/13/2016
  Time: 10:29 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        <% Map<Integer, Media> productIdMedia= new HashMap();
            int productId;
            User user = (User) request.getSession().getAttribute("user");

            Sale sale = (Sale)request.getAttribute("sale");

            List<OrderItem> orderItemList = sale.getBasket().getOrderItems();

            int number = (Integer)request.getSession().getAttribute("number");

            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");
            for(int i = 0; i<orderItemList.size(); i++){
                productId = orderItemList.get(i).getProduct().getProductID();
                productIdMedia.put(productId, (Media)request.getAttribute("media"+productId));
            }

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
            <select name="category" class = "searchCategory">
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
        <div id="container">
        <a href="/showCartContent" class="cart" id="infoi">
            <img src="/resources/image/cart.PNG" class="cart" alt="cart image">
        </a>
        <div id="navi"><%=number%></div>
        </div>
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
<div id="orderContent">
    <%int id = 0;
        Media media;
        for(OrderItem orderItem : orderItemList){%>
    <div>
        <p class="saleInfo">Sale ID:&nbsp;<%=sale.getSaleID()%></p>
        <p class="saleInfo">Date:&nbsp;<%=sale.getDate()%></p>
        <%  id = orderItem.getProduct().getProductID();
            media = (Media)request.getAttribute("media"+id);%>
        <div class="iconImage">
            <img src="<%=media.getMediaPath()%>"  alt="image">
        </div>
        <div class = "productInfo">
            <p style="font-size: 20px;">Name:&nbsp;<%= orderItem.getProduct().getName()%></p>
            <p style="font-style: italic; ">Price:&nbsp;$&nbsp;<%= orderItem.getProduct().getPrice()%></p>
            <p style="font-style: italic; ">Shipping Price:&nbsp;$&nbsp;<%= orderItem.getProduct().getShippingPrice()%></p>
            <p >Size:&nbsp;<%= orderItem.getSizeOption()%></p>


            <p class="quantity">Quantity:&nbsp;<%= orderItem.getQuantity()%> </p>
        </div>
    </div>
    <%}%>

    <div class = "clear"></div>
</div>

    <div class = "clear"></div>
</body>
</html>
