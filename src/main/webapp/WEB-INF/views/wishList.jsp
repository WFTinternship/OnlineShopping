<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.common.*" %><%--
  Created by IntelliJ IDEA.
  User: annaasmangulyan
  Date: 9/24/16
  Time: 4:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Search Box</title>

<!-- CSS styles for standard search box -->

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />">
<script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>
<script>
    function deleteFromList(productId){


        $.get("/deleteFromList?productId=" + productId, function (data) {
            document.getElementById("productdiv" + productId).remove();

        });
        }

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
<div class="logo"><img src="/resources/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">

        <% Map<Integer, Media> productIdMedia= new HashMap();
            int productId;
            User user = (User) request.getSession().getAttribute("user");
            List<Product> productList = (List<Product>) request.getAttribute("productList");
            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");

            int number = (Integer)request.getSession().getAttribute("number");

            for(int i = 0; i<productList.size(); i++){
                productId = productList.get(i).getProductID();
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
            <a href = "/getSaledProducts">SALE</a>
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
                <a href="/editAccount">edit account</a>
                <a href="/getOrders">your orders</a>
                <a href="#">your wish list</a>

                <a href="/logout" id="logout_button">logout</a>
            </div>
        </div>

        <div class="clear"></div>


    </div>
</div>




<div id="wishlist">
    <%int id = 0;
        Media media;
        for(int k=0; k<productList.size(); k++){%>
    <div id="productdiv<%=productList.get(k).getProductID()%>">
        <%  id = productList.get(k).getProductID();
            media = (Media)request.getAttribute("media"+id);%>
        <div class="iconImage">
            <a href="/productPage?id=<%=id %>" id="productHref">
            <img src="<%=media.getMediaPath()%>"  alt="image">
                </a>
        </div>
        <div class = "productInfo">
            <p style="font-size: 20px;">Name:&nbsp;<%= productList.get(k).getName()%></p>
            <p class="inline" style="font-style: italic; ">Price:&nbsp;$&nbsp;</p><span id= "price"><%= productList.get(k).getPrice()%></span>
            <p style="font-style: italic; ">Shipping Price:&nbsp;$&nbsp;<%= productList.get(k).getShippingPrice()%></p><br>


            <a href="#" onclick="deleteFromList(<%=productList.get(k).getProductID()%>)" class="littleAnchor">delete</a><br><br>

        </div><br>


    </div>
    <%}%>

    <div class = "clear"></div>
</div>
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



</body>
</html>
