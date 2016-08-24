<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/9/2016
  Time: 2:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign in Page</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />">

</head>

<body class="registration">
<div class="logo"><img src="/resources/image/logo3.PNG" width="140px;" alt="logo"></div>

<form method="post" action="/signin" id="signinPage">
    <p style="font-size:30px;">Sign In</p><br>
    <% String string = (String) request.getAttribute("errorString");
        if (string != null) {%>
    <p style="font-size:12px; color:red"><%=request.getAttribute("errorString")%>
    </p><br>
    <%
        }
    %>
    Username
    <input type="text" name="username" required><br><br>
    Password<br>
    <input type="password" pattern=".{6,}" name="password"><br><br>
    <button style="border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px; width: 255px; height: 35px" class="button" id="signinButton">Sign in
    </button>
    <br><br>
    <p>Not a customer?
    <p>
        <a href="/createaccount" style="font-size: 12px;" class="register">Start here</a>
</form>
<div class="clear"></div>

</body>
</html>
