<%@ page import="com.workfront.internship.common.User" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="com.workfront.internship.dao.MediaDao" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.business.*" %>
<%@ page import="com.workfront.internship.common.Category" %><%--
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

			List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");
			String saleDone = (String) request.getAttribute("saleDone");
			String errorString = (String) request.getAttribute("errorString");
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
			<a href="/getSaledProducts" class = "saleId">SALE</a>
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

		<%
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {

		%>

		<div class="signinRegister">
			<a href="/login" class="register" id="login_button">SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="/createaccount" class="register" id="registration_button">CREATE ACCOUNT</a>
		</div>

		<div class="clear"></div>
	</div>
</div>

<%
	}
%>

<%

	if (user != null) {
		int number = (Integer) request.getSession().getAttribute("number");

%>
<div id="container">
	<a href="/showCartContent" class="cart" id="infoi">
		<img src="/resources/image/cart.PNG" class="cart" alt="cart image">
	</a>
	<div id="navi"><%=number%>
	</div>
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
<%
	}
%>


<div>
	<img src="/resources/image/pic4.jpg" alt="image1" class="backimg1">


	<img src="/resources/image/pic1.jpg" alt="image2" class="backimg2">
</div>
<%if (saleDone != null) {%>
<script>
	alert("your sale is successfully done!")
</script>
<%}%>
<%if (errorString != null) {%>
<script>
	alert("Sorry! One of the items may be finished! Check your Cart!");
</script>
<%}%>
<div id="productsForPage">
	<%

		List<Product> products = (List<Product>) request.getSession().getAttribute("products");
		List<Media> medias;
		for (int i = 0; i < products.size(); i++) {
			medias = (List<Media>) request.getSession().getAttribute("medias" + i);
			int productId = products.get(i).getProductID();

	%>
	<div class="image">

		<a href="/productPage?id=<%=productId %>" id="productHref">
			<img src="/resources/image/index.png" alt="index" class="index" style="width:80px;">
			<%for (int k = 0; k < medias.size(); k++) {%>
			<img src="<%=medias.get(k).getMediaPath()%>" class="img<%=k+1%>" alt="cart image">

			<%}%>

		</a>

		<%-- <script>var img1 = document.getElementById("productImage");</script>
		 <img src="<%=medias.get(1).getMediaPath()%>" id="productImage2" alt="cart image">
		 <script>   img2 = document.getElementById("productImage2");

			 img1.onmouseover = function(){
				 img2.style.display = "block";
			 }

			 img1.onmouseout = function(){
				 img2.style.display = "none";
			 }
		 </script>--%>
		<p><%=products.get(i).getName()%>
		</p>
		<p>$<%=products.get(i).getPrice()%>
		</p>
	</div>

	<%
		}
	%>
		<script>
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

