<%--
  Created by IntelliJ IDEA.
  User: Lasse
  Date: 10-04-2015
  Time: 12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
submit project jsp -test
<form action="submit">

  <input hidden name="action" value="createProjectRequest">
  <input name="budget">
  <input type="project_body">
  <input type="submit">

</form>

<%= request.getAttribute("submitCheck")%>


</body>
</html>
