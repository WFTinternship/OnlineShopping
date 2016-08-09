<%@ page import="com.workfront.internship.common.User" %><%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/9/2016
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/reset.css">
    <link rel="stylesheet" type="text/css" href="/css/main.css">

</head>
<body class="registration">
<div class="image"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<div class="userInfo">
    <%
        User user = (User)request.getAttribute("user");
        out.print("Hello" + " " + user.getFirstname());

    %>
</div>
</body>
</html>
