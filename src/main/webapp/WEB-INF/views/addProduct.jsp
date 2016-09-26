<%@ page import="com.workfront.internship.common.Category" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <% List<Category> categories = (List<Category>)request.getAttribute("categories");
        Product product = (Product)request.getSession().getAttribute("product");
        String option = (String)request.getSession().getAttribute("option");
        Map categoryMap = (Map)request.getAttribute("categoryMap");
        Map sizeOptionQuantity = (Map)request.getAttribute("sizeOptionQuantity");
        Map sizeMap = (Map)request.getAttribute("sizeMap");

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
        <select name="category" id="currentCategory" onchange="getSizeOptions()" >

<% Map map = new HashMap();
    int optionNumber = 0;
    for(int i=0; i<listofCategoriesList.size(); i++){%>

    <option value="<%=listofCategoriesList.get(i).get(0).getCategoryID()%>" selected><%=listofCategoriesList.get(i).get(0).getName()%></option>

         <%map.put(listofCategoriesList.get(i).get(0).getCategoryID(), optionNumber++);
            %>
          <% for(int l=1; l<listofCategoriesList.get(i).size(); l++) {%>
            <option value="<%=listofCategoriesList.get(i).get(l).getCategoryID()%>"><%="-" + listofCategoriesList.get(i).get(l).getName()%></option>
            <%map.put(listofCategoriesList.get(i).get(l).getCategoryID(), optionNumber++);
                System.out.println(listofCategoriesList.get(i).get(l).getCategoryID() +  "  "+ listofCategoriesList.get(i).get(l).getName());%>
            <%}

%>
        <%}

        %>
        </select><br>

       <%
       List<List<Size>> listOfSizeLists= new ArrayList();

       Set<Map.Entry<Integer, List<Size>>> set = sizeMap.entrySet();
       for (Map.Entry<Integer, List<Size>> entry : set)  {
       List<Size> list = new ArrayList<Size>();
            list = ((List<Size>)entry.getValue());
            listOfSizeLists.add(list);
       } %>
        <%for(int k = 0; k< listOfSizeLists.size(); k++){%>
        <div id="sizeVersion<%=k%>" style="display: none;" class="size">
        <%for(int l = 0; l<listOfSizeLists.get(k).size(); l++){%>


            Size&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantity<br>

            <select id="sizeOption" name = "sizeoption<%=l%>">
               <% for(int m = 0; m < listOfSizeLists.get(k).size(); m++){
           %>
                <option value="<%= listOfSizeLists.get(k).get(m).getSizeOption()%>" id = "sizeVersion<%=l + listOfSizeLists.get(k).get(m).getSizeOption()%>" name = "<%=l+listOfSizeLists.get(k).get(m).getSizeOption()%>"><%=listOfSizeLists.get(k).get(m).getSizeOption()%></option>

                <%}%>
            </select>&nbsp;&nbsp;&nbsp;<input id="quantityOption<%=l%>" name="quantity<%=l%>">

                    </input><br>
            <%}%>
        </div>
<%}%>
        <script>
           <%
           if(!option.equals("add")){
           optionNumber=(Integer)map.get(product.getCategory().getCategoryID());
          %>
             document.getElementById('currentCategory').getElementsByTagName('option')[<%=optionNumber%>].selected='selected';
           <%
                       Set<Map.Entry<Integer, Integer>> set1 = categoryMap.entrySet();
                       for (Map.Entry<Integer, Integer> entry1 : set1)  {%>

           if(document.getElementById("currentCategory").value == "<%=entry1.getKey()%>"){

               <% int versionNum = 0;
               for (Map.Entry<Integer, List<Size>> entry2 : set)  {%>

               <%if(entry1.getValue() == entry2.getKey()){%>

               document.getElementById("sizeVersion<%=versionNum%>").style.display = "block";
               <%Set<Map.Entry<String, Integer>> set3 = sizeOptionQuantity.entrySet();
int l = 0;
for (Map.Entry<String, Integer> entry3 : set3){%>
document.getElementById("sizeVersion<%=l + entry3.getKey()%>").selected = 'selected'; 
               document.getElementById("quantityOption<%=l%>").value = <%=entry3.getValue()%>;
<% l++;}
               %>


               <%}
               versionNum++;}%>
           }

           <%}%>


       <%}
       %>
        </script><br><br>



        </div>
<div class="uploadImage">
        <input type="file" name="file" value = "image" size="50" multiple id = "choosefile"/>
        <br />
        <input type="submit" value="Save" id = "uploadfile"/>
</div>
        <script>function getSizeOptions() {
            document.getElementById("sizeVersion0").style.display = "none";
            document.getElementById("sizeVersion1").style.display = "none";
            document.getElementById("sizeVersion2").style.display = "none";
            document.getElementById("sizeVersion3").style.display = "none";
           <%
            Set<Map.Entry<Integer, Integer>> set1 = categoryMap.entrySet();
            for (Map.Entry<Integer, Integer> entry1 : set1)  {%>

            if(document.getElementById("currentCategory").value == "<%=entry1.getKey()%>"){

                <% int versionNum = 0;
                for (Map.Entry<Integer, List<Size>> entry2 : set)  {%>

                <%if(entry1.getValue() == entry2.getKey()){%>

                document.getElementById("sizeVersion<%=versionNum%>").style.display = "block";
                <%}
                versionNum++;}%>
            }

            <%}%>



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

