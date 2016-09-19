<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.workfront.internship.business.CategoryManager" %>
<%@ page import="com.workfront.internship.business.CategoryManagerImpl" %>
<%@ page import="com.workfront.internship.common.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="com.workfront.internship.business.ProductManager" %>
<%@ page import="com.workfront.internship.business.ProductManagerImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.common.Media" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %><%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/15/2016
  Time: 10:14 AM
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
    <script src="<c:url value="/resources/js/jquery.elevatezoom.js" />"></script>
    <script src="<c:url value="/resources/js/jquery-1.8.3.min.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>
    <script src="<c:url value="/resources/js/jzoom.min.js" />"></script>

<script>
    function addToCart(productId) {
        var sizeOption = $('#sizeOptions').val();
        if(sizeOption == "Select") {
            alert("chose size");
        }
        var quantity = $('#quantity').val();

        $.get("/addToCart?productId=" + productId + "&sizeOption=" + sizeOption + "&quantity=" + quantity, function (data) {
            alert(data);
            document.getElementById("navi").innerHTML = data;

        });


    }

</script>
</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="logo"><img src="/resources/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">
        <%

            List<Category> categories = (List<Category>) request.getSession().getAttribute("categories");
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
        <div class="category" class = "searchCategory">

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
            <a href="/signin.jsp" class="register" id="login_button">SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/jspgistration.jsp" class="register" id="registration_button">CREATE ACCOUNT</a>
        </div>

        <div class="clear"></div>
    </div>
</div>

<%
    }
%>

<%

    if (user != null) {
        int number = (Integer)request.getSession().getAttribute("number");


%>
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
        <a href="/WEB-INF/views/index.jsp?user=null" id="logout_button">logout</a>
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
<div class="product">
    <%

        Product product = (Product) request.getAttribute("product");

    %>
    <%-- <img id="zoom_09" src="./image/girldress11.jpg" data-zoom-image="./image/girldress11_big.jpg"/>

     <select id="select">
         <option value="1">Front</option>
         <option value="2">Back</option>

     </select>--%>
    <%-- <img id="img_01" src="./image/girldress11.jpg" data-zoom-image="./image/girldress11_big.jpg"/>

     <div id="gal1">

         <a href="#" data-image="./image/girldress11.jpg" data-zoom-image="./image/girldress11_big.jpg">
             <img  src="./image/index.png" />
         </a>

         <a href="#" data-image="/image/girldress12.jpg" data-zoom-image="/image/girldress12_big.jpg">
             <img  src="./image/index.png" />
         </a>
 --%>


</div>
<div class="bigproductImage">
    <% List<Media> medias= product.getMedias();
    for(int i = 0; i<medias.size(); i++)
        {%>
    <div class="jzoom" id="bigimg<%=i+1%>">


        <img src="<%=medias.get(i).getMediaPath()%>" alt="cart image">


    </div>
    <%}%>


    <!--   <div  >

           <img src="/image/girldress12.jpg"  alt="cart image"  id="bigimg2">

       </div> -->

</div>
<div class="productdesc">
    <h1 class="productName"><%=product.getName()%></h1>
    <p class="price">Price: $<%=product.getPrice()%></p>
    <p class="size">Size:&nbsp; &nbsp; &nbsp; <select id="sizeOptions" name = "sizeOption"  onchange="chooseQuantity()">
        <option>Select</option>
        <%Set<Map.Entry<String, Integer>> set = product.getSizeOptionQuantity().entrySet();
        for (Map.Entry<String, Integer> entry : set) {%>
        <option ><%=entry.getKey()%></option>
       <%}%>
            </select></p>

    <script>
        function chooseQuantity(){
            var quantity ;
            <%for (Map.Entry<String, Integer> entry : set){%>
            if( document.getElementById("sizeOptions").value == "<%=entry.getKey()%>") {
                quantity = <%=entry.getValue()%>
            }

            var selectString = " <p class='quantity'>Quantity: </p><select id = 'quantity'>";
                for (var i = 1; i<quantity+1; i++) {
                    selectString +="<option>" + i + "</option>";
                }
            selectString += "</select>";
            document.getElementById("select_quantity").innerHTML = selectString;

            <%
}%>
        }
    </script>
    <div id="select_quantity" >
        <p class="quantity">Quantity: </p><select id = "q" name = "quantity">
                <script>
                    document.write("<option>" + 1 + "</option>");
                </script>

        </select>
    </div>
    <div class = "cartButton" id="cartButton">
        <button onclick="addToCart(<%=product.getProductID()%>)"
                class='button'  role='button'>Add to Cart</button>
    <%--<a href="/addToCart?productId=&sizeOption=" class="button"  role="button">Add to Cart</a>--%>
    </div>

    <div class="listButton">
    <a href="#" class="button" role="button">Add to List</a>

        </div>
</div>
<div class="clear"></div>
</div>


<div class="productButtons">
    <%if(medias.size() == 2){%>
    <button id="littleImage1" onclick="myFunction1()"><img src="<%=product.getMedias().get(0).getMediaPath()%>"
                                                           style="height:90px" ></button>

    <button id="littleImage2" onclick="myFunction2()"><img src="<%=product.getMedias().get(1).getMediaPath()%>"
                                                      style="height:90px" ></button>
            <%} if(medias.size() == 1){%>
    <button id="littleImage1" onclick="myFunction1()"><img src="<%=product.getMedias().get(0).getMediaPath()%>"
                                                           style="height:90px"></button>
<%}if(medias.size() == 0){%>
    <button id="littleImage1" onclick="myFunction1()"><img src="/resources/image/index.png"
                                                           style="height:90px"></button>
    <%}if(medias.size() == 3){%>
    <button id="littleImage1" onclick="myFunction1()"><img src="<%=product.getMedias().get(0).getMediaPath()%>"
                                                           style="height:90px" ></button>

    <button id="littleImage2" onclick="myFunction2()"><img src="<%=product.getMedias().get(1).getMediaPath()%>"
                                                           style="height:90px" ></button>
    <button id="littleImage2" onclick="myFunction3()"><img src="<%=product.getMedias().get(2).getMediaPath()%>"
                                                           style="height:90px" ></button>
    <%}%>
    <script>function myFunction2() {
        document.getElementById("bigimg1").style.display = "none";

        document.getElementById("bigimg2").style.display = "block";
        document.getElementById("bigimg3").style.display = "none";
        // document.getElementById("bigimg").src = "/image/girldress12.jpg";
    }

    function myFunction1() {

        document.getElementById("bigimg1").style.display = "block";
        document.getElementById("bigimg2").style.display = "none";
        document.getElementById("bigimg3").style.display = "none";
        //document.getElementById("bigimg").src = "/image/girldress11.jpg";
    }
    function myFunction3() {

        document.getElementById("bigimg1").style.display = "none";
        document.getElementById("bigimg2").style.display = "none";
        document.getElementById("bigimg3").style.display = "block";
        //document.getElementById("bigimg").src = "/image/girldress11.jpg";

    }


    </script>
    <script>

        $('.jzoom').jzoom();

        // width / height of the magnifying glass

    </script>
</div>

<%--<script>
    //initiate the plugin and pass the id of the div containing gallery images
    $("#zoom_03").elevateZoom({gallery:'gallery_01', cursor: 'pointer', galleryActiveClass: 'active', imageCrossfade: true, loadingIcon: 'http://www.elevateweb.co.uk/spinner.gif'});

    //pass the images to Fancybox
    $("#zoom_03").bind("click", function(e) {
        var ez =   $('#zoom_03').data('elevateZoom');
        $.fancybox(ez.getGalleryList());
        return false;
    });
</script>--%>

<%--<script>
    $("#zoom_09").elevateZoom({
        gallery : "gallery_09",
        galleryActiveClass: "active"
    });


    $("#select").change(function(e){
        var currentValue = $("#select").val();
        if(currentValue == 1){
            smallImage = './image/girldress11.jpg';
            largeImage = './image/girldress11_big.jpg';
        }
        if(currentValue == 2){
            smallImage = './image/girldress12.jpg';
            largeImage = './image/girldress12_big.jpg';
        }

        // Example of using Active Gallery
        $('#gallery_09 a').removeClass('active').eq(currentValue-1).addClass('active');


        var ez =   $('#zoom_09').data('elevateZoom');

        ez.swaptheimage(smallImage, largeImage);

    });
</script>--%>
</body>
</html>


