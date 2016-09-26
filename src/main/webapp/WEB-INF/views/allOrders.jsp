<%@ page import="com.workfront.internship.common.Sale" %>
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
    <title>All Orders</title>

    <!-- CSS styles for standard search box -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/adminMain.css" />">
    <script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>

    <script>
        function changeStatus(saleId){
            debugger;

            var e = document.getElementById("status");
            var status = e.options[e.selectedIndex].value;
            alert(e.selectedIndex);

            alert(status);
            alert("lalala");

            $.get("/changeStatus?saleId=" + saleId + "&status=" + status, function (data) {
                alert(data);


            });
        }
    </script>

</head>
<body>
<!-- HTML for SEARCH BAR -->

<div class="wrapper">
    <img src="/resources/image/logo3.PNG" width="140px;" alt="logo" id="logo">
    <div class="buttons">
        <a href="/categories" id="categories">CATEGORIES</a>
        <a href="/products" id="products">PRODUCTS</a>
        <a href="#">ORDEERS</a>
        <a href="/sale" id="sale">SALE</a>
    </div>
</div>
    <% List<Sale> orders = (List<Sale>) request.getAttribute("orders");%>

    <div class = "tables">
        <table>
            <thead>
            <tr><th>&nbsp;&nbsp;&nbsp;Order ID</th><th>&nbsp;&nbsp;&nbsp;Full Name</th> <th>&nbsp;&nbsp;&nbsp;Date</th><th>&nbsp;&nbsp;&nbsp;Price($)</th><th>&nbsp;&nbsp;&nbsp;Delivery Status</th></tr>
            </thead>
            <%
                int i=0;
                for(Sale sale : orders){%>

                <tr>
                    <td><a href="/showOrderInfo?saleId=<%=sale.getSaleID()%>&admin=admin" class="saleId"><%=sale.getSaleID()%></a></td>
                    <td><a href="/showOrderInfo?saleId=<%=sale.getSaleID()%>&admin=admin" class="saleId"><%=sale.getFullName()%></a></td>
                    <td><a href="/showOrderInfo?saleId=<%=sale.getSaleID()%>&admin=admin" class="saleId"><%=sale.getDate()%></a></td>
                    <td><a href="/showOrderInfo?saleId=<%=sale.getSaleID()%>&admin=admin" class="saleId">$<%=sale.getBasket().getTotalPrice()%></a></td>
                    <td>


                            <% if(sale.getStatus().equals("not delivered")){%>
                        <select id = "status" onchange="changeStatus(<%=sale.getSaleID()%>)">
                            <option>delivered</option>
                            <option selected>not delivered</option>
                            </select>
                            <%}%>
                        <% if(sale.getStatus().equals("delivered")){%>
                        <select id = "status" onchange="changeStatus(<%=sale.getSaleID()%>)">
                            <option selected>delivered</option>
                            <option>not delivered</option>
                        </select>
                        <%}%>
                    </td>

                </tr>

            <%}
            %>

        </table>

    </div>

<div class="clear"></div>
</body>
</html>

