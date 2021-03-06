<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <a href="/create-company" class="button-clear u-pull-right">Add partner</a>
      <h3 style="margin-bottom: 1rem;">Partners</h3>
      <p style="color: #9F9F9F;">Click on partner to get to partner's page.</p>

      <div class="table-head">
          <span class="id">ID</span>
          <span class="partner-name">Company name</span>
          <span class="country">Country</span>
      </div>

      <c:forEach var="partner" items="${partners}">
          <a href="partner?id=<c:out value='${partner.getId()}' />" />
          <div class="project-item">
              <span class="id"><strong>#</strong><c:out value="${partner.getId()}" /></span>
              <span class="partner-name"><c:out value="${partner.getName()}" /></span>
              <span class="country small"><c:out value="${partner.country_code()}" /></span>
          </div>
          </a>
      </c:forEach>
  </div>

  </body>
</html>