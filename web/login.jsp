<%--
  Created by IntelliJ IDEA.
  User: Andreas Poulsen
  Date: 09-Apr-15
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form action="/login">
    <input type="hidden" name="action" value="login">
    <input name="email" placeholder="email">
    <input type="password" name="password" placeholder="password">
    <input type="submit" value="login">
</form>
</body>
</html>
