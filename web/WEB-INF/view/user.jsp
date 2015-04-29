<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <div class="u-pull-right" style="margin-top: 10px;">
          <c:if test="${partner.getImg_filename() != null}" >
              <a href="partner?id=<c:out value='${partner.getId()}' />">
                <img class="company-image" style="margin-right: 0px;" src="/resources/companies/<c:out value='${partner.getId()}' />/<c:out value='${partner.getImg_filename()}' />" />
              </a>
          </c:if>
      </div>

      <h3 class="company-name"><c:out value='${user.getName()}' /></h3>
      <div class="u-pull-left" style="clear: left;">
          <p class="company-desc"><c:out value="${user.getEmail()}" /></p>
          <p class="company-desc">You can see all <c:out value='${user.getName()}' />'s projects below.</p>
      </div>

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
                  <span class="partner"><c:out value='${partner.getName()}' /></span>
                  <span class="type"><c:out value="${project.getType()}" /></span>
                  <span class="state small"><c:out value="${project.getStatus()}" /></span>
                  <span class="execution-date small isShortDate"><c:out value="${project.getExecution_date()}"></c:out></span>
              </div>
          </a>
      </c:forEach>
  </div>

  </body>
</html>