<%@ page import="Domain.User" %>
<%@ page import="Domain.Project" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
