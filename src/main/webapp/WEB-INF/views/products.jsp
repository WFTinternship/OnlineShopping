<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="java.util.List" %>
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
        <a href="/categories" id="categories">CATEGORIES</a>
        <a href="/products" id="products">PRODUCTS</a>
        <a href="#" id="orders">ORDEERS</a>
    </div>

</div>
<div class="clear"></div>
<form action="/edit" method="post" id="form_id"  name="productForm" >
<div id="icons">
    <a href="/add?option=add" id="add">
        <img src="/resources/image/add.jpg" alt="add" style="height: 40px; width:42px;"></a>

    <button  value="0" class = "littleButtons" onclick="function1();"><img src="/resources/image/edit.jpg" alt="add" style="height: 40px; width:42px;"></button>
    <button  value="0" class = "littleButtons" onclick="function2();"><img src="/resources/image/delete.jpg" alt="add" style="height: 40px; width:42px;"></button>
    <%--<a href="/add" id="add">
            <img src="/resources/image/add.jpg" alt="add" style="height: 40px; width:42px;"></a>--%>

    <%--<a href="/delete" id="delete">
        <img src="/resources/image/delete.jpg" alt="add" style="height: 40px; width:42px;"></a>--%>
</div>
<div class="clear"></div>
<div id="productsList">
    <% List<Product> products = (List<Product>)request.getSession().getAttribute("products"); %>
    <table>
        <thead>
        <tr><th>&nbsp;&nbsp;&nbsp;ID</th> <th>&nbsp;&nbsp;&nbsp;Name</th><th>&nbsp;&nbsp;&nbsp;Price($)</th><th>&nbsp;&nbsp;&nbsp;Category</th></tr>
        </thead>
        <%
            int i=0;
            for(Product product : products){%>
        <tr>
            <td><input type="checkbox" class = "checkBox" name="product" value="<%=product.getProductID()%>"> <%=product.getProductID()%><br></td>
            <td><%=product.getName()%></td>
            <td><%=product.getPrice()%></td>
            <td><%=product.getCategory().getName()%></td>

        </tr>
        <%}
        %>

    </table>
        <%--<a href="" id="edit" onclick="document.getElementById('form_id').submit()">

            <img src="/resources/image/edit.jpg" alt="add" style="height: 40px; width:42px;"></a>--%>
        <input value="0" name="option" type="hidden" id="actionOption">




</form>
<script>
    function function1() {

            var inputElems = document.getElementsByTagName("input"),
                    count = 0;

            for (var i=0; i<inputElems.length; i++) {
                if (inputElems[i].type == "checkbox" && inputElems[i].checked == true){
                    count++;
                }
            }
        if(count == 1) {
            document.productForm.option.value = 'edit';
            document.getElementById("form_id").submit();

        }
        if(count >= 2) {
            alert("choose only one item");
            document.getElementById("form_id").addEventListener("submit", function(event){
                event.preventDefault()
            });
        }
        if(count == 0) {
            alert("choose an item");
            document.getElementById("form_id").addEventListener("submit", function(event){
                event.preventDefault()
            });

        }

        document.productForm.option.value = 'edit';
        return true;
    }
    function function2() {
        var inputElems = document.getElementsByTagName("input"),
                count = 0;

        for (var i=0; i<inputElems.length; i++) {
            if (inputElems[i].type == "checkbox" && inputElems[i].checked == true){
                count++;
            }
        }
        if(count >0) {
            document.getElementById("form_id").submit();

        }

        if(count == 0) {
            alert("choose an item");
            document.getElementById("form_id").addEventListener("submit", function (event) {
                event.preventDefault()
            });
        }
        document.productForm.option.value = 'delete';
        return true;
    }




</script>
</div>

</body>
</html>

