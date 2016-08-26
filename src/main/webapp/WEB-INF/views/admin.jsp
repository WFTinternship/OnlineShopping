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
    <title>Search Box</title>

    <!-- CSS styles for standard search box -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/adminMain.css" />">


</head>
<body>
<!-- HTML for SEARCH BAR -->

<div class="wrapper">
    <img src="/resources/image/logo3.PNG" width="140px;" alt="logo" id="logo">
    <div class="buttons">
        <a href="#" id="categories">CATEGORIES</a>
        <a href="#" id="products">PRODUCTS</a>
        <a href="#" id="users">USERS</a>
        <a href="#" id="orders">ORDEERS</a>
    </div>

</div>
<div class="clear"></div>
<div id="icons">
    <a href="/add" id="add">
        <img src="/resources/image/add.jpg" alt="add" style="height: 40px; width:42px;"></a>
    <a href="#" id="edit">
        <img src="/resources/image/edit.jpg" alt="add" style="height: 40px; width:42px;"></a>
    <a href="#" id="delete">
        <img src="/resources/image/delete.jpg" alt="add" style="height: 40px; width:42px;"></a>
</div>
<div class="clear"></div>
</body>
</html>

