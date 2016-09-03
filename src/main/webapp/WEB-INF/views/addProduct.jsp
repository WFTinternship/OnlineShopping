<%@ page import="com.workfront.internship.common.Category" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
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
    <title>Add Product</title>


    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/adminMain.css" />">


</head>
<body>


<div class="wrapper">
    <img src="/resources/image/logo3.PNG" width="140px;" alt="logo" id="logo">
    <div class="buttons">
        <a href="/categories" id="categories">CATEGORIES</a>
        <a href="/products" id="products">PRODUCTS</a>
        <a href="/orders" id="orders">ORDEERS</a>
    </div>

</div>
<div class="clear"></div>

<div class="clear"></div>
<div>
    <% List<Category> categories = (List<Category>)request.getSession().getAttribute("categories");
        Product product = (Product)request.getSession().getAttribute("product");
        String option = (String)request.getSession().getAttribute("option");
        Map categoryMap = (Map)request.getSession().getAttribute("categoryMap");
        Map sizeMap = (Map)request.getSession().getAttribute("sizeMap");

    %>
    <form action="/saveProduct" method="post" name="myForm"
          enctype="multipart/form-data" id="addProductForm" onsubmit = "return validateForm()">

        Product name<br>
        <input type="text" name="productName"  value="<%=product.getName()%>" id = "pName" required><br><br>
        Price<br>
        <input type="text" name="price" id = "pPrice" value="<%=product.getPrice()%>" required><br><br>
        Shipping price<br>
        <input type="text" name="shippingPrice" id = "pshippingPrice" value="<%=product.getShippingPrice()%>" required><br><br>
        Color<br><br>
        <input type="text" name="color" id = "pColor" value="<%=product.getDescription()%>" required><br><br>
        Category<br>

            <%List<List<Category>> listofCategoriesList = new ArrayList<List<Category>>();


    int j=0;
    for(Category category : categories) {

        if (category.getParentID() == 0) {
        for(Category category1 : categories){
        if(category1.getParentID == category.getCategoryID){

            List<Category> list = new ArrayList<Category>();
            list.add(category1);
            listofCategoriesList.add(list);
            }
            }
        }
    }
    for(int k=0; k<listofCategoriesList.size(); k++) {
        for (Category category1 : categories) {
            if(category1.getParentID() == listofCategoriesList.get(k).get(0).getCategoryID())
                listofCategoriesList.get(k).add(category1);
        }
    }
    %>
        <select name="category" id="currentCategory" onchange="getSizeOptions();">

<% Map map = new HashMap();
    int optionNumber = 0;
    for(int i=0; i<listofCategoriesList.size(); i++){%>

    <option value="<%=listofCategoriesList.get(i).get(0).getCategoryID()%>" selected><%=listofCategoriesList.get(i).get(0).getName()%></option>

         <%map.put(listofCategoriesList.get(i).get(0).getCategoryID(), optionNumber++);
             System.out.println(listofCategoriesList.get(i).get(0).getCategoryID() +  "  "+ listofCategoriesList.get(i).get(0).getName());%>
          <% for(int l=1; l<listofCategoriesList.get(i).size(); l++) {%>
            <option value="<%=listofCategoriesList.get(i).get(l).getCategoryID()%>"><%="-" + listofCategoriesList.get(i).get(l).getName()%></option>
            <%map.put(listofCategoriesList.get(i).get(l).getCategoryID(), optionNumber++);
                System.out.println(listofCategoriesList.get(i).get(l).getCategoryID() +  "  "+ listofCategoriesList.get(i).get(l).getName());%>
            <%}

%>
        <%}

        %>
        </select>
        <script>
           <%
           if(!option.equals("add")){
           optionNumber=(Integer)map.get(product.getCategory().getCategoryID());
          %>
            document.getElementById('currentCategory').getElementsByTagName('option')[<%=optionNumber%>].selected='selected';
       <%}
       %>
        </script><br><br>



        </div>

        <input type="file" name="file" size="50" multiple id = "choosefile"/>
        <br />
        <input type="submit" value="Save" id = "uploadfile"/>

        <script>function getSizeOptions() {
            <%for(Map.Entry<Category, Integer> entry : categoryMap.entrySet()){%>
            if(document.getElementById("currentCategory").value == "<%=entry.getKey()%>"){
            <%int mainParentId = (Integer)entry().getValue();
             List<String> sizes = List<String>sizeMap.getValue(mainParentId);%>
             <%for(int i=0; i <sizes.size(); i++){%>
             <div>
                             <select id="3">
                             <%for(int i=0; i<sizes.size(); i++){%>
                                 <option value="ss1" selected><%=sizes.get(i)%></option>
             <%}
             %>
                         </select><select id="4">
                         <%for(int i=0; i<20; i++){%>
                                 <option value="ss1" selected><%=i%></option>
             <%}
                          %>
                           </select>
       </div>
               <%}
               %>

            }
<%}
             %>

        }
            function validateForm(){

                if(document.getElementById("pName").value == " "){

                        alert("Name must be filled out");
                    return false;}

                if(document.getElementById("pPrice").value == "0.0") {
                        alert("Price must be filled out");
                        return false;}

                if(document.getElementById("pshippingPrice").value == " ") {
                            alert("ShippingPrice must be filled out");
                            return false;}

                if(document.getElementById("pColor").value == " "){
                                alert("Color must be filled out");
                                return false;}
            }



        </script>


    </form>
</div>
</body>
</html>

