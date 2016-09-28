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
    <script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>
    <script>
        function searchFunction() {

            var category = $('#category').val();

            var searchKey = $('#searchKey').val();

            if (searchKey == "") {
                document.getElementById("searchResult").innerHTML = "";
                searchFunction().abort();

            }
            document.getElementById("searchKey").value = document.getElementById("searchKey").value.trim();

            $.get("/getLikeProducts?searchKey=" + searchKey + "&category=" + category, function (data) {

                var str = "";
                for (var i = 0; i < data.length; i++) {
                    str += "<div class = 'subcategory'>" + data[i] + "</div>"
                    //console.log('data', data[i]);
                }

                document.getElementById("searchResult").innerHTML = str;

            });
        }
        $(document).ready(function () {

            $('#searchResult').on('click', '.subcategory', function() {

                var category = $('#category').val();

                var subCategory = $(this).text();
                document.getElementById("searchKey").value = subCategory;
                $('#searchResult').hide();

            });
        })
    </script>

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
        <form method="get" action="/getProductsBySearch" onsubmit="return validate()"><br><br><br><br>
            <select name="category" class="searchCategory" id="category">

                <%for (int i = 0; i < listofCategoriesList.size(); i++) {%>

                <option value="<%=listofCategoriesList.get(i).get(0).getCategoryID()%>"
                       ><%=listofCategoriesList.get(i).get(0).getName()%>
                </option>

                <%
                    }
                %>

            </select><input type="search" class="textinput" name="productName" id="searchKey" size="60" maxlength="120"
                            onkeyup="searchFunction()"><input
                type="submit" value="search" class="button">
            <div id="searchResult">

            </div>

        </form>
        <br>
    </div>
    <div class="some">
        <div class="category">
            <a href = "/getSaledProducts" class = "saleId">SALE</a>
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
                <a href="/editAccount">edit account</a>
                <a href="/getOrders">your orders</a>
                <a href="/showWishlistContent">your wish list</a>

                <a href="/logout" id="logout_button">logout</a>
            </div>
        </div>

        <div class="clear"></div>


    </div>
</div>
<div id="checkoutForm">
    <form action="/checkout" method="post" onsubmit="return validateForm()">

        <p style="font-size:25px;">Fill required information to continue checkout</p><br>
        Full name:<br>
        <input type="text" name="fullName" id="fullName" required><br><br>

        <%if (!user.getShippingAddresses().isEmpty()) {%>
        Select from existing addresses:<br>
        <select name="addressOption" class="addressOption" id="addressOption">
            <option>Select</option>
            <%for (Address address : user.getShippingAddresses()) {%>
            <option value="<%=address.getAddressID()%>"><%=address.getAddress() + ", " + address.getCity() + ", " + address.getCountry() + ", " + address.getZipCode()%>
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
        <script>
            String.prototype.trim = function()
            {
                // Replace leading and trailing spaces with the empty string
                return this.replace(/(^\s*)|(\s*$)/g, "");
            }
            document.getElementById("newAddress").value = document.getElementById("newAddress").value.trim();
            document.getElementById("address").value = document.getElementById("address").value.trim();
            document.getElementById("city").value = document.getElementById("city").value.trim();
            document.getElementById("country").value = document.getElementById("country").value.trim();
            document.getElementById("zip").value = document.getElementById("zip").value.trim();
            document.getElementById("fullName").value = document.getElementById("fullName").value.trim();
        </script>

    </form>
    <script>

        function validateForm() {



            if(document.getElementById("fullName").value == ""){
                alert("fill name field");
                return false;
            }
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
                    alert("at least one address information must be filled");
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

        String.prototype.trim = function()
        {
            // Replace leading and trailing spaces with the empty string
            return this.replace(/(^\s*)|(\s*$)/g, "");
        }
        function validate(){
            if(document.getElementById("searchKey").value != "") {
                if(document.getElementById("searchKey").value.trim() == "")
                    return false;
                else{
                    document.getElementById("searchKey").value = document.getElementById("searchKey").value.trim();
                    return true;
                }
            }
            else return false;
        }


    </script>
</div>
</body>
</html>
