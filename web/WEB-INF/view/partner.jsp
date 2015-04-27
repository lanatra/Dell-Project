<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <c:if test="${partner.getImg_filename() != null}" >
          <img class="company-image" src="/resources/companies/<c:out value='${partner.getId()}' />/<c:out value='${partner.getImg_filename()}' />" />
      </c:if>
      <h3 class="company-name"><c:out value='${partner.getName()}' /></h3>
      <p class="company-desc">You can see all <c:out value='${partner.getName()}' />'s projects below.</p>

      <div class="table-head">
          <span class="id">ID</span>
          <span class="partner">Partner</span>
          <span class="type">Type</span>
          <span class="state">State</span>
          <span class="execution-date">Execution date</span>
      </div>

      <c:forEach var="project" items="${projects}">
          <a href="/project?id=<c:out value="${project.getId()}" />">
              <div class="project-item">
                  <span class="id"><strong>#</strong><c:out value="${project.getId()}" /></span>
                  <span class="partner"><c:out value="${project.getCompanyName()}" /></span>
                  <span class="type"><c:out value="${project.getType()}" /></span>
                  <span class="state small"><c:out value="${project.getStatus()}" /></span>
                  <span class="execution-date small isShortDate"><c:out value="${project.getF_execution_date()}"></c:out></span>
              </div>
          </a>
      </c:forEach>
  </div>

  </body>
</html>