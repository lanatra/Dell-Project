<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <% if (request.getSession().getAttribute("User") == null) {response.sendRedirect("login.jsp");} %>
  <head>
    <meta charset="utf-8">
    <title>Dell</title>
    <meta name="description" content="Dell campaign management system">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,500&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link href="css/normalize.css" rel="stylesheet" media="all">
    <link href="css/skeleton.css" rel="stylesheet" media="all">
    <link href="css/style.css" rel="stylesheet" media="all">
  </head>
  <body>

  <div class="header u-full-width">
    <div class="container">
      <a href="/logout">
        <div class="logo u-pull-left">
          <img src="img/small_dell_logo.svg" alt="Dell logo">
          <span>Campaign<br/>management<br/>system</span>
        </div>
      </a>
      <div class="user-label u-pull-right">
        <img src="img/white_dropdown.svg" alt="Logout menu">
        <span><% User user = (User) request.getSession().getAttribute("User");
          out.print(user.name); %></span>
      </div>
      <div class="budget-label u-pull-right">
        <span class="big">834 039 DKK</span>
        <span class="desc">is left available in this quarter (1.1.2015 - 31.3.2015)</span>
      </div>
    </div>
  </div>


  <div class="container" style="margin-top: 40px;">
    <a href="/"">
      <div class="filter">
        <div class="circle waiting"><c:out value="${statusCount[0]}" /> </div>
        <span>Waiting<br/>for action</span>
      </div>
    </a>
    <a href="/?state=In Execution">
      <div class="filter">
        <div class="circle execution"><c:out value="${statusCount[1]}" /></div>
        <span>In execution</span>
      </div>
    </a>
    <a href="/?state=Claim approved">
      <div class="filter">
        <div class="circle finished"><c:out value="${statusCount[2]}" /></div>
        <span>Finished</span>
      </div>
    </a>
  </div>
  <div class="project-list">
      <ul>
          <c:forEach var="project" items="${projects}">
              <li class='
              <%
              if(user.getCompany_id() == 1) {%>
              <c:if test="${project.isUnread_admin()}">unread</c:if>
              <%} else {%>
              <c:if test="${project.isUnread_partner()}">unread</c:if>
              <%}%>
              '>
                  <div><c:out value="${project.getId()}" /></div>
                  <div><c:out value="${project.getCompanyName()}" /></div>
                  <div><c:out value="${project.getBudget()}" /></div>
                  <div><c:out value="${project.getStatus()}" /></div>
              </li>
          </c:forEach>

      </ul>
  </div>

  <!-- <form action="submit" method="get">
      <input hidden name="action" value="getUser">
      <input name="user_id">
      <input type="submit">
  </form>
  <form action="submit" method="get">
      <input hidden name="action" value="createProjectRequest">
      <input name="budget">
      <input type="project_body">
      <input type="submit">
  </form>
  null
  /
  null -->

  </body>
</html>