<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <%@ include file="header.jsp" %>


  <div class="container" style="margin-top: 40px;">
    <a href="/dashboard"">
      <div class="filter">
        <div class="circle waiting"><c:out value="${statusCount[0]}" /> </div>
        <span>Waiting<br/>for action</span>
      </div>
    </a>
    <a href="?state=In Execution">
      <div class="filter">
        <div class="circle execution"><c:out value="${statusCount[1]}" /></div>
        <span>In execution</span>
      </div>
    </a>
    <a href="?state=Claim approved">
      <div class="filter">
        <div class="circle finished"><c:out value="${statusCount[2]}" /></div>
        <span>Finished</span>
      </div>
    </a>
  </div>

  <div class="container" style="margin-top: 30px;">

      <div class="table-head">
          <span class="id">ID</span>
          <span class="partner">Partner</span>
          <span class="type">Type</span>
          <span class="state">State</span>
          <span class="execution-date">Execution date</span>
      </div>

      <c:forEach var="project" items="${projects}">

          <a href="#">
              <div class="project-item
              <c:if test="${User.getCompany_id() == 1}">
                <c:if test="${project.isUnread_admin()}">unread</c:if>
              </c:if>
              <c:if test="${User.getCompany_id() != 1}">
                <c:if test="${project.isUnread_partner()}">unread</c:if>
              </c:if>

              "><a href="/project?id=<c:out value="${project.getId()}" />">
                  <span class="id"><strong>#</strong><c:out value="${project.getId()}" /></span>
                  <span class="partner"><c:out value="${project.getCompanyName()}" /></span>
                  <span class="type"><c:out value="${project.getType()}" /></span>
                  <span class="notification small"><c:out value="${project.getNotification()}" /></span>
                  <span class="state small"><c:out value="${project.getStatus()}" /></span>


                  <span class="execution-date small">Jan. 12 2014</span>
              </a></div>
          </a>
      </c:forEach>
  </div>




  </body>
</html>