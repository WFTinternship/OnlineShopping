<%--
  Created by IntelliJ IDEA.
  User: Workfront
  Date: 8/24/2016
  Time: 2:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Uploading Form</title>
</head>
<body>
<h3>File Upload:</h3>
Select a file to upload: <br />
<form action="UploadServlet" method="post"
      enctype="multipart/form-data">
    <input type="file" name="file1" size="50" multiple />

    <br />
    <input type="submit" value="Upload File" />
</form>
</body>
</html>
