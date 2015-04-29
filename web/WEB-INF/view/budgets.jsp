<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <a href="/create-budget" class="button-clear u-pull-right">Create budget</a>
      <h3 style="margin-bottom: 1rem;">Budget view</h3>
      <p style="color: #9F9F9F;">Click on budget to edit it.</p>

      <div class="table-head">
          <span class="t-quarter">Quarter</span>
          <span class="t-year">Year</span>
          <span class="t-budget">Budget</span>
          <span class="t-reserved small">Reimbursed</span>
          <span class="t-reserved small">Reserved</span>
          <span class="t-reserved small">Available</span>
      </div>
      <c:forEach var="budget" items="${budgets}">
          <a href="/edit-budget?initialbudget=<c:out value='${budget.getInitial_budget()}' />&year=<c:out value='${budget.getYear()}' />&quarter=<c:out value='${budget.getQuarter()}' />">
              <div class="project-item">
                  <span class="t-quarter"><c:out value="${budget.getQuarter()}" /></span>
                  <span class="t-year"><c:out value="${budget.getYear()}" /></span>
                  <span class="t-budget"><c:out value="${budget.getInitial_budget()}" />&#8364</span>
                  <span class="t-reserved small"><c:out value="${budget.getReimbursed()}" />&#8364</span>
                  <span class="t-reserved small"><c:out value="${budget.getReserved()}" />&#8364</span>
                  <span class="t-reserved small"><c:out value="${budget.getLeftAvailable()}" />&#8364</span>
              </div>
          </a>
      </c:forEach>
  </div>

  </body>
</html>