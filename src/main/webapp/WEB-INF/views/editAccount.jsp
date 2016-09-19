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

            String errorString = (String)request.getAttribute("errorString");

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
    <form action="/saveEditedAccount" method="post" onsubmit="return enableFields()">
        <p style="font-size:25px;">Edit account</p><br>

        First name:<br>
        <input type="text" id="firstname" name="firstname" required disabled="disabled"
               value="<%=user.getFirstname()%>"><a href="#" class="littleAnchor"
                                                   onclick="enableFName()">edit</a><br><br>
        Last name:<br>
        <input type="text" id="lastname" name="lastname" required disabled="disabled" value="<%=user.getLastname()%>"><a
            href="#" class="littleAnchor" onclick="enableLName()">edit</a><br><br>
        Username:<br>
        <input type="text" id="username" name="username" required disabled="disabled" value="<%=user.getUsername()%>"><a
            href="#" class="littleAnchor" onclick="enableUName()">edit</a><br><br>
        Email:<br>
        <input type="email" id="email" name="email" required disabled="disabled" value="<%=user.getEmail()%>"><a
            href="#" class="littleAnchor" onclick="enableEmail()">edit</a><br><br>

        <%
            List<Address> addresses = user.getShippingAddresses();
            System.out.println("aaaaaaaaaaaaaaa" + addresses.size());
            if (!addresses.isEmpty()) {
        %>

        <%for (int i = 0; i < addresses.size(); i++) {%>
        <div id="addressdiv<%=(i+1)%>">
        Address<%=" " + (i + 1)%>:<br>
        <input type="text" id="address<%=(i+1)%>" name="address<%=i%>" required disabled="disabled"
             value="<%=addresses.get(i).getAddress()%>" ><a href="#" class="littleAnchor"
                                                                                             onclick="deleteAddress<%=(i+1)%>(<%=addresses.get(i).getAddressID()%>)">delete</a><br><br>
        <%}%>
</div>
        <%}%>
<%--
        <%for (int k = 0; k < 3 - addresses.size(); k++) {%>
        New address line<%=" " + (k + 1)%>:<br>
        <input type="text" name="newaddress<%=k%>"><br><br>
        <%}%>
--%>

        <a href="#" id = "changePass" onclick="enablePass()">Change password</a><br><br>
        <div id="password" style="display: none;">

            Old password:<br>
            <% String string1 = (String) request.getAttribute("errorPass");
                if (string1 != null) {%>
            <p style="font-size:12px; color:red"><%=request.getAttribute("errorPass")%>
            </p><br>
            <script>
                document.getElementById("password").style.display = "block";
            </script>
            <%
                }
            %>
            <input type="password" name="oldPassword" required><br><br>
            <% String string = (String) request.getAttribute("errorString");
                if (string != null) {%>
            <p style="font-size:12px; color:red"><%=request.getAttribute("errorString")%>
            </p><br>
            <script>
                document.getElementById("password").style.display = "block";
            </script>
            <%
                }
            %>
            New password:<br>
            <input type="password" name="newPassword" required><br><br>

            Repeat password:<br>
            <input type="password" name="repeatPassword" required><br><br>
        </div>
        <button type = "submit" style="border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px; width: 120px; height: 35px" class="button" id="signinButton">Save
        </button>


    </form>
    <script>
        function enableFName() {

            document.getElementById("firstname").disabled = false;

        }

        function enableLName() {

            document.getElementById("lastname").disabled = false;


        }
        function enableUName() {

            document.getElementById("username").disabled = false;


        }
        function enableEmail() {

            document.getElementById("email").disabled = false;


        }

        function enablePass() {

            document.getElementById("password").style.display = "block";
        }
        function enableFields(){
            document.getElementById("firstname").disabled = false;
            document.getElementById("lastname").disabled = false;
            document.getElementById("username").disabled = false;
            document.getElementById("email").disabled = false;
        }
        <%for(int l =0; l<addresses.size(); l++){%>
        function deleteAddress<%=l+1%>(addressId) {
            alert("kuku");
            alert("kukula");
            var addressId1 = <%=addresses.get(l).getAddressID()%>;
            alert("kuku1");
            alert("lala" + addressId1);


            $.get("/deleteAddress?addressId=" + addressId1, function (data) {
                alert("lalala");
            });

            $( "#addressdiv<%=(l+1)%>" ).remove();

        }
        <%}%>

    </script>
</div>
</body>
</html>
