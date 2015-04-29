<%--
  Created by IntelliJ IDEA.
  User: Andreas Poulsen
  Date: 28-Apr-15
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Reset Password</title>
</head>
<body>
<form action="/reset-password" method="post">
    <input type="hidden" name="nonce" value="<c:out value="${param.n}"></c:out>">
    <input type="hidden" name="user_id" value='<c:out value="${userId}"></c:out>'>
    <input type="password" name="pw" placeholder="Enter your new password">
    <input type="submit" value="Change Password">
</form>
<c:out value="${error}"></c:out>
</body>
</html>
