<%@ page import="Domain.User" %>
<%@ page import="Domain.Project" %>
<%@ page import="java.util.ArrayList" %>

<%--
  Created by IntelliJ IDEA.
  User: Andreas Poulsen
  Date: 08-Apr-15
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<form action="submit" method="get">
    <input hidden name="action" value="createProjectRequest">
    <input name="budget">
    <input type="project_body">
    <input type="submit">
</form>

<%= request.getAttribute("submitCheck")%>


    <p>URL is: <%= request.getAttribute("url") %></p>
    <a href="/myaction">Action!</a>

<form action="getProjects" method="get">
    <input hidden name="action" value="getProjectsByState">
    <input name="state">
    <input type="submit">
</form>
<table>
    <tr>
        <th>ID</th>
        <th>Company</th>
        <th>Budget</th>
        <th>Body</th>
        <th>Status</th>
    </tr>

<c:forEach var="project" items="${projects}">
    <tr>
        <td><c:out value="${project.getId()}" /></td>
        <td><c:out value="${project.getCompany_id()}" /></td>
        <td><c:out value="${project.getBudget()}" /></td>
        <td><c:out value="${project.getBody()}" /></td>
        <td><c:out value="${project.getStatus()}" /></td>
    </tr>
</c:forEach>
</table>
  </body>
</html>
