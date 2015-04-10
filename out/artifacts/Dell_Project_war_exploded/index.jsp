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
      <link rel="stylesheet" type="text/css" href="/css/normalize.css">
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

<br><br><br>
Project Request form:
<form action="submit" method="get">
    <input hidden name="action" value="createProjectRequest">
    Budget:<input name="budget">
    Description:<input type="text" name="project_body">
    <input type="submit">
</form>
Request complete:
<%= request.getAttribute("submitCheck")%>

<br><br><br>


Project verification form:
<form action="submit" method="get">
    <input hidden name="action" value="verifyProjectRequestByProjectId">
    Project ID: <input name="project_id">
    <input type="submit">
</form>
Verification complete:
<%= request.getAttribute("verificationCheck")%>

<br><br><br>

    <p>URL is: <%= request.getAttribute("url") %></p>
    <a href="/myaction">Action!</a>

  </body>
</html>
