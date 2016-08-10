<%@ page import="com.workfront.internship.common.User" %><%--
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
    <form  method="get" action="http://www.google.com"><br><br><br><br>
        <select name="category">
            <option value="all" selected>All</option>
            <option value="hat">Hat</option>
            <option value="shoes">Shoes</option>
            <option value="girl dress">Girl dress</option>
            <option value="boy shirt">Boy shirt</option>
        </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input type="submit" value="search" class="button">

    </form>



<%
    User user = (User)request.getSession().getAttribute("user");
    if(user==null){

%>

    <div class="signinRegister" >
        <a href="/signin.jsp"  class="register" >Sign in</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/registration.jsp" class="register">Create account</a></div>
    <div class="clear"></div>
<%}
%>
    <div class="dropdown">
        <span>&nbsp;
    <%

        if(user!=null){
            out.print("Hello," + " " + user.getFirstname());

    %>
    </span><br>
        <button class="dropbtn">Your account</button>
        <div class="dropdown-content">
            <a href="#">edit account</a>
            <a href="#">your orders</a>
            <a href="#">your wish list</a>
        </div>
    </div>
    <a href="./cart.jsp" class="cart">
        <img src="/image/cart.PNG" alt="cart image">
    </a>
    <div class="clear"></div>
    <%}
    %>
</div>
</body>
</html>

