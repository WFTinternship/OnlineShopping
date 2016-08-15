<!DOCTYPE html>
<html>
<head>
    <title>Registration Page</title>
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">
</head>
<body class="registration">
<div class="logo"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<form method="post" action="/registration" id="registrationPage">
    <p style="font-size:30px;">Create account</p><br>
    <%  String string = (String)request.getAttribute("errorString");
        if(string!=null){%>
    <p style="font-size:12px; color:red"><%=request.getAttribute("errorString")%></p><br>
    <%}
    %>
    First name<br>
    <input type="text" name="firstname" required><br><br>
    Last name<br>
    <input type="text" name="lastname" required><br><br>
    Email<br>
    <input type="email" name="email" required><br><br>
    Username<br>
    <input type="text" name="username" required><br><br>
    Password<br>

    <input type="password" pattern=".{6,}" name="password"><br><br>

    Repeat password<br>
    <input type="password" pattern=".{6,}" name="repeat password"><br><br>
    <%--<% Boolean equal = true; %>
    <script>var fistInput = document.getElementsByName("password").value;
    var secondInput = document.getElementsByName("repeat password").value;


    if(fistInput != secondInput)
    {
    equal=false;
    }</script>--%>
    <button style="border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px; width: 255px; height: 35px" class="button" id="createAccountButton">Create account</button><br><br>
    <p>Already have an account?<p>
    <a href="/signin.jsp" style="font-size: 12px;" class="register">Sign in</a>
</form>
<div class="clear"></div>
</body>
</html>