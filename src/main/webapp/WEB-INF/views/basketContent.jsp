<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.common.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="com.workfront.internship.common.OrderItem" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
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
    <script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>

    <script>


        function changeNumberOnCart(orderItemId, elem) {

            var quantity = elem.options[elem.selectedIndex].value;


            $.get("/updateBasket?orderItemId=" + orderItemId + "&quantity=" + quantity, function (data) {

                var delta = parseInt(data);

                var oldQuantity = document.getElementById("navi").textContent;

                var newQuantity = parseInt(oldQuantity) - parseInt(data);

                document.getElementById("navi").innerHTML = newQuantity;

                var oldPrice = document.getElementById("total").textContent;
                alert(oldPrice);

                var singlePrice = document.getElementById("price").textContent;
                alert(singlePrice);
                var newPrice = parseFloat(oldPrice) - parseFloat(singlePrice)*delta;
                alert(newPrice);
                document.getElementById("total").innerHTML = newPrice;
                document.getElementById("error").style.display = "none";




            });


        }

    </script>
</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="logo"><img src="/resources/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">

        <% Map<Integer, Media> productIdMedia= new HashMap();
            int productId;
            User user = (User) request.getSession().getAttribute("user");
            List<OrderItem> orderItemList = (List<OrderItem>) request.getAttribute("orderItemList");
            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");

            int number = (Integer)request.getSession().getAttribute("number");

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
        <div id="navi" value ="<%=number%>"><%=number%></div>
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
<div id="basketContent">
    <%int id = 0;
        Media media;
        for(int k=0; k<orderItemList.size(); k++){%>
    <div id="productdiv<%=(k+1)%>">
<%  id = orderItemList.get(k).getProduct().getProductID();
    media = (Media)request.getAttribute("media"+id);%>
        <div class="iconImage">
    <img src="<%=media.getMediaPath()%>"  alt="image">
        </div>
        <div class = "productInfo">
       <p style="font-size: 20px;">Name:&nbsp;<%= orderItemList.get(k).getProduct().getName()%></p>
       <p class="inline" style="font-style: italic; ">Price:&nbsp;$&nbsp;</p><span id= "price"><%= orderItemList.get(k).getProduct().getPrice()%></span>
       <p style="font-style: italic; ">Shipping Price:&nbsp;$&nbsp;<%= orderItemList.get(k).getProduct().getShippingPrice()%></p>
       <p >Size:&nbsp;<%= orderItemList.get(k).getSizeOption()%></p>
            <% int productQuantity = orderItemList.get(k).getProduct().getSizeOptionQuantity().get(orderItemList.get(k).getSizeOption());

                    if(productQuantity < orderItemList.get(k).getQuantity()){
            %>
            <p style="font-size:12px; color:red" id="error">the product number you have chosen is not available in store</p>


            <%}%>

            <p class="quantity" >Quantity: </p><select id = "selectQuantity<%=k%>" name = "quantity" onchange="changeNumberOnCart(<%=orderItemList.get(k).getOrderItemID()%>, this)">
                <option>Select</option>
                <%for(int i =1; i< productQuantity +1; i++){%>


            <script>
                var str = "<option ";
               <% if(i==orderItemList.get(k).getQuantity()){%>
                    str += "selected";
               <% }%>
                str += " >" + <%=i%> + "</option>"

                document.write(str);


            </script>
<%}%>
        </select>
        </div><br>
        <a href="/deleteItemFromBasket?itemId=<%=orderItemList.get(k).getOrderItemID()%>" class="littleAnchor">delete</a><br><br>

    </div>
    <%}%>

    <div class = "clear"></div>
</div>
<%if(!orderItemList.isEmpty()){%>
<div id="checkout">
    <p class="inline">Total Price:&nbsp;$</p><span id ="total"><%=user.getBasket().getTotalPrice()%></span><br><br><br>
    <a href="/infoForSale" class="button" role="button" onclick="return validate(<%=orderItemList.size()%>)">Procced to checkout</a>

</div>
<%}%>
<script>
    function validate(size){
        alert(size);
        for(var i = 0; i < parseInt(size); i ++) {
            if (document.getElementById("selectQuantity" + i).value == "Select"){
                alert("chose quantity");
                return false;
            }
        }

    }


</script>
<div class = "clear"></div>
</body>
</html>
