<%@ page import="Domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Dell</title>
    <link rel="stylesheet" type="text/css" href="/css/normalize.css">
  </head>
  <body>

<form action="submit" method="get">
    <input hidden name="action" value="getUser">
    <input name="user_id">
    <input type="submit">
</form>

  <%= "<p>" + request.getAttribute("testHole") + "</p>" %>

  <%= request.getAttribute("userInfo") %>

<form action="submit" method="get">
    <input hidden name="action" value="createProjectRequest">
    <input name="budget">
    <input type="project_body">
    <input type="submit">
</form>

<%= request.getAttribute("submitCheck")%>


    <p>URL is: <%= request.getAttribute("url") %></p>
    <a href="/myaction">Action!</a>

  </body>
</html>
