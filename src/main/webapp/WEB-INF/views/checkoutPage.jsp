<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.common.Category" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.Address" %>
<%--
  Created by IntelliJ IDEA.
  User: annaasmangulyan
  Date: 9/15/16
  Time: 1:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            User user = (User) request.getSession().getAttribute("user");

            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");

            List<List<Category>> listofCategoriesList = new ArrayList<List<Category>>();

            int number = (Integer)request.getSession().getAttribute("number");


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
<div id="checkoutForm">
    <form action="/checkout" method="post" onsubmit="return validateAddressField()">
        <p style="font-size:25px;">Fill required information to continue checkout</p><br>

        <%if (!user.getShippingAddresses().isEmpty()) {%>
        Select from existing addresses:<br>
        <select name="addressOption" class="addressOption" id="addressOption">
            <option>Select</option>
            <%for (Address address : user.getShippingAddresses()) {%>
            <option><%=address.getAddress() + ", " + address.getCity() + ", " + address.getCountry() + ", " + address.getZipCode()%>
            </option>
            <%}%>
        </select><br>
        Add new address:<br>
        <input type="text" name="newAddress" id="newAddress"><br><br>
        <%} else {%>
        Address:<br>
        <input type="text" name="address" id="address"><br><br>
        <%}%>
        City:<br>
        <input type="text" name="city" id = "city" ><br><br>
        Country:<br>
        <input type="text" name="country" id = "country" ><br><br>
        Zip:<br>
        <input type="text" name="zip" id = "zip" ><br><br>
        <button style="border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px; width: 120px; height: 35px" class="button" id="signinButton">Continue
        </button>


    </form>
    <script>
        function validateAddressField() {
            if (document.getElementById("addressOption") == null) {
                if (document.getElementById("address").value == "" || document.getElementById("city").value == ""
                        || document.getElementById("country").value == "" || document.getElementById("zip").value == "") {
                    alert("fill all required fields");
                    return false;
                }
            }

            if (document.getElementById("addressOption").value == "Select") {
                if (document.getElementById("newAddress").value == "" || document.getElementById("city").value == ""
                        || document.getElementById("country").value == "" || document.getElementById("zip").value == "") {
                    alert("At least one address information must be filled");
                    return false;
                }
            }
            if (document.getElementById("addressOption").value != "Select") {
                if (document.getElementById("newAddress").value != "") {
                    alert("Only one address option must be filled");
                    return false;
                }
            }

            return true;
        }
    </script>
</div>
</body>
</html>
