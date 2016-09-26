<%@ page import="com.workfront.internship.common.Category" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.common.Product" %>
<%@ page import="java.util.*" %>
<%@ page import="com.workfront.internship.common.Size" %>
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
    <script src="<c:url value="/resources/js/jquery-1.12.1.min.js" />"></script>


</head>
<body>


<div class="wrapper">
    <img src="/resources/image/logo3.PNG" width="140px;" alt="logo" id="logo">
    <div class="buttons">
        <a href="/categories" id="categories">CATEGORIES</a>
        <a href="/products" id="products">PRODUCTS</a>
        <a href="/allOrders" id="orders">ORDEERS</a>
        <a href="/sale" id="sale">SALE</a>
    </div>

</div>
<div class="clear"></div>


<div>
    <% List<Category> categories = (List<Category>) request.getAttribute("categories");
        Category category0 = (Category) request.getSession().getAttribute("category");
        String option = (String) request.getSession().getAttribute("option");
    %>
    <form action="/saveCategory" method="post" name="myForm"
          enctype="multipart/form-data" id="addProductForm" onsubmit="return validateForm()">

        Category name<br>
        <input type="text" name="categoryName" value="<%=category0.getName()%>" id="catName" required><br><br>
        Choose Parent Category:<br>


            <%List<List<Category>> listofCategoriesList = new ArrayList<List<Category>>();


    int j=0;
    for(Category category : categories) {

        if (category.getParentID() == 0) {
        for(Category category1 : categories){
        if(category1.getParentID() == category.getCategoryID()){

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
        <select name="category" id="currentCategory">

            <% Map map = new HashMap();
                int optionNumber = 0;
                for (int i = 0; i < listofCategoriesList.size(); i++) {%>

            <option value="<%=listofCategoriesList.get(i).get(0).getCategoryID()%>"
                    selected><%=listofCategoriesList.get(i).get(0).getName()%>
            </option>

            <%
                map.put(listofCategoriesList.get(i).get(0).getCategoryID(), optionNumber++);
            %>
            <% for (int l = 1; l < listofCategoriesList.get(i).size(); l++) {%>
            <option value="<%=listofCategoriesList.get(i).get(l).getCategoryID()%>"><%="-" + listofCategoriesList.get(i).get(l).getName()%>
            </option>
            <%
                map.put(listofCategoriesList.get(i).get(l).getCategoryID(), optionNumber++);
                System.out.println(listofCategoriesList.get(i).get(l).getCategoryID() + "  " + listofCategoriesList.get(i).get(l).getName());
            %>
            <%
                }}

            %>
        </select><br>


        <script>
            <%
            if(!option.equals("add")){
            optionNumber=(Integer)map.get(category0.getParentID());
           %>
            document.getElementById('currentCategory').getElementsByTagName('option')[<%=optionNumber%>].selected = 'selected';

            <%}%>
        </script>
        <br><br>


</div>
<div class="uploadImage">
    <input type="submit" value="Save" id="uploadfile"/>
</div>

<script>
    function validateForm() {

        if (document.getElementById("catName").value == " ") {

            alert("Name must be filled out");
            return false;
        }
        return true;
    }
</script>
</form>
</div>
</body>
</html>

