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
<form action="PresentationServlet" method="get">
    <input hidden name="action" value="getUser">
    <input name="name">
    <input type="submit">
</form>
<%= request.getAttribute("name") %>
  </body>
</html>
