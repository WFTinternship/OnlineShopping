<%@ page import="com.workfront.internship.dao.DataSource" %>
<%@ page import="com.workfront.internship.business.CategoryManager" %>
<%@ page import="com.workfront.internship.business.CategoryManagerImpl" %>
<%@ page import="com.workfront.internship.common.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.common.User" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="com.workfront.internship.business.ProductManager" %>
<%@ page import="com.workfront.internship.business.ProductManagerImpl" %><%--
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
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">
    <script src="/jquery-1.12.1.min.js"></script>

    <script src="/jquery.elevatezoom.js"></script>
    <script src="/jquery.elevateZoom-3.0.8.min.js"></script>
    <script src="/jquery-1.8.3.min.js"></script>

    <script src="/jzoom.min.js"></script>

</head>
<body>
<!-- HTML for SEARCH BAR -->
<div class="logo"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="wrapper">
    <div class="wrapper1">
        <%
            DataSource dataSource=DataSource.getInstance();
            CategoryManager categoryManager = new CategoryManagerImpl(dataSource);
            List<Category> categories = categoryManager.getAllCategories();


        %>
        <form  method="get" action="http://www.google.com"><br><br><br><br>
            <select name="category">
                <option value="all" selected>All</option>
                <option value="cat1"><%=categories.get(0).getName()%></option>
                <option value="cat2"><%=categories.get(1).getName()%></option>
                <option value="cat3"><%=categories.get(2).getName()%></option>

            </select><input type="text" class="textinput" name="productName" size="60" maxlength="120"><input type="submit" value="search" class="button">

        </form><br>
    </div>
    <div class="some">
        <div class="category">




            <div class="dropdown">
                <button class="dropbtn">BABY GIRL</button>
                <div class="dropdown-content">

                    <a href="#"><%=categories.get(0).getName()%></a>
                    <a href="#"><%=categories.get(1).getName()%></a>
                    <a href="#"><%=categories.get(2).getName()%></a>

                </div>
            </div>
            <div class="dropdown">
                <button class="dropbtn">BABY BOY</button>
                <div class="dropdown-content">

                    <a href="#"><%=categories.get(0).getName()%></a>
                    <a href="#"><%=categories.get(1).getName()%></a>
                    <a href="#"><%=categories.get(2).getName()%></a>

                </div>
            </div>
        </div>

        <%
            User user = (User)request.getSession().getAttribute("user");
            if(user==null){

        %>

        <div class="signinRegister" >
            <a href="/signin.jsp"  class="register" id="login_button" >SIGN IN</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/registration.jsp" class="register" id="registration_button">CREATE ACCOUNT</a>
        </div>

        <div class="clear"></div>
    </div>
</div>

<%}
%>

<%

    if(user!=null){


%>

<a href="./cart.jsp" class="cart">
    <img src="/image/cart.PNG" class="cart" alt="cart image">
</a>
<div class="dropdown">
    <span class="greeting"><%out.print("Hello," + " " + user.getFirstname());%></span>
    <button class="dropbtn" id="your_account">YOUR ACCOUNT</button>
    <div class="dropdown-content">
        <a href="#">edit account</a>
        <a href="#">your orders</a>
        <a href="#">your wish list</a>
        <a href="/index.jsp?user=null" id="logout_button">logout</a>
    </div>
</div>

<div class="clear"></div>


</div>
</div>
<%}
%>




<div>
    <img src="./image/pic4.jpg"  alt="image1" class="backimg1">


    <img src="./image/pic1.jpg"  alt="image2" class="backimg2">
</div>
<div class = "product">
    <%

        Product product = (Product)request.getAttribute("product");
        System.out.println(product.getMedias().get(1).getMediaPath());
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
        <div class="jzoom" id="bigimg2">


            <img src="<%=product.getMedias().get(1).getMediaPath()%>"  alt="cart image"  >


        </div>
        <div class="jzoom" id="bigimg1">

            <img src="<%=product.getMedias().get(0).getMediaPath()%>"  alt="cart image">



        </div>

     <!--   <div  >

            <img src="/image/girldress12.jpg"  alt="cart image"  id="bigimg2">

        </div> -->

    </div>
    <div class="productdesc">
        <h1 class="productName">2-Piece dress & Cardigan Set</h1>
        <p class="price">Price: $20</p>
        <p class="size">Size:&nbsp; &nbsp; &nbsp; <a href="#">NB</a>
            <a href="#">3M</a>
            <a href="#">6M</a>
            <a href="#">9M</a>
            <a href="#">12M</a>
            <a href="#">18M</a>
            <a href="#">24M</a>
        </p>

        <p class="quantity">Quantity: <select>
            <option value='1'>1</option>
            <option value='2'>2</option>
            <option value='3'>3</option>
            <option value='4'>4</option>
            <option value='5'>5</option>
        </select>    </p>
    </div>
    <div class="clear"></div>
</div>


<div class="productButtons">
    <button  id="littleImage1" onclick="myFunction1()"> <img src="<%=product.getMedias().get(0).getMediaPath()%>"  style="height:90px" id="aaa"></button>

    <button  id="littleImage2" onclick="myFunction2()"> <img src="<%=product.getMedias().get(1).getMediaPath()%>" style="height:90px" id="bbb"></button>
  <script>function myFunction2(){
       document.getElementById("bigimg1").style.display="none";

      document.getElementById("bigimg2").style.display="block";
       // document.getElementById("bigimg").src = "/image/girldress12.jpg";
    }

    function myFunction1(){

        document.getElementById("bigimg1").style.display="block";
       document.getElementById("bigimg2").style.display="none";
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


