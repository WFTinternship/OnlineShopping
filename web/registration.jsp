<!DOCTYPE html>
<html>
<head>
    <title>Registration Page</title>
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">
</head>
<body class="registration">
<div class="image"><img src="/image/logo3.PNG" width="140px;" alt="logo"></div>
<form method="post" action="/registration">
    <p style="font-size:30px;">Create account</p><br>
    First name<br>
    <input type="text" name="firstname"><br><br>
    Last name<br>
    <input type="text" name="lastname"><br><br>
    Email<br>
    <input type="text" name="email"><br><br>
    Username<br>
    <input type="text" name="username"><br><br>
    Password<br>

    <input type="password" pattern=".{6,}" name="password"><br><br>

    Repeat password<br>
    <input type="password" pattern=".{6,}" name="repeat password"><br><br>
    <button style="border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px; width: 255px; height: 35px" class="button">Create account</button><br><br>
    <p>Already have an account?<p>
    <a href="./signin.html" style="font-size: 12px;" class="register">Sign in</a>
</form>
<div class="clear"></div>
</body>
</html>