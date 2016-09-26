<%@ page import="com.workfront.internship.common.Sale" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.workfront.internship.common.OrderItem" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/26/2016
  Time: 2:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<!DOCTYPE html>
<html>
<head>
    <title>All Orders</title>

    <!-- CSS styles for standard search box -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/adminMain.css" />">
    <script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>



</head>
<body>
<!-- HTML for SEARCH BAR -->

<div class="wrapper">
    <img src="/resources/image/logo3.PNG" width="140px;" alt="logo" id="logo">
    <div class="buttons">
        <a href="/categories" id="categories">CATEGORIES</a>
        <a href="/products" id="products">PRODUCTS</a>
        <a href="/allOrders">ORDEERS</a>
        <a href="/sale" id="sale">SALE</a>
    </div>
</div>
<% Map<Integer, Media> productIdMedia= new HashMap();
    int productId;


    Sale sale = (Sale)request.getAttribute("sale");

    List<OrderItem> orderItemList = sale.getBasket().getOrderItems();

    int number = (Integer)request.getSession().getAttribute("number");


    for(int i = 0; i<orderItemList.size(); i++){
        productId = orderItemList.get(i).getProduct().getProductID();
        productIdMedia.put(productId, (Media)request.getAttribute("media"+productId));
    }
%>
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
    <div id="totalPrice">
        <p>Total Price:&nbsp;<%= sale.getBasket().getTotalPrice()%> </p>
    </div>
</div>
</body>
</html>

