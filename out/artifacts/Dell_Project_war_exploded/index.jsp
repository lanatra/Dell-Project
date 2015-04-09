<%@ page import="Domain.User" %>
<%--
  Created by IntelliJ IDEA.
  User: Andreas Poulsen
  Date: 08-Apr-15
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
hest

<form action="submit" method="get">
    <input hidden name="action" value="getUser">
    <input name="user_id">
    <input type="submit">
</form>
  <%= "<p>" + request.getAttribute("testHole") + "</p>" %>

  <%= request.getAttribute("userInfo") %>


  </body>
</html>
