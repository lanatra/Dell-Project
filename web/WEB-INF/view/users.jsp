<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <a href="/create-user" class="button-clear u-pull-right">Add user</a>
      <h3 style="margin-bottom: 1rem;">Users</h3>
      <p style="color: #9F9F9F;">Click on user to get to the user's details.</p>

      <div class="table-head">
          <span class="id">ID</span>
          <span class="user-name">Name</span>
          <span class="email">Email</span>
      </div>
      <c:forEach var="user" items="${users}">
          <a href="/user?id=<c:out value='${user.getId()}' />">
              <div class="project-item">
                  <span class="id"><strong>#</strong><c:out value="${user.getId()}" /></span>
                  <span class="user-name"><c:out value="${user.getName()}" /></span>
                  <span class="email"><c:out value="${user.getEmail()}" /></span>
              </div>
          </a>
      </c:forEach>
  </div>

  </body>
</html>