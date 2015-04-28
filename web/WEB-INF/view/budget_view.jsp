<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />




<!-- VIEW BUDGETS -->
<h1> Budget View </h1>
<c:forEach var="budgets" items="${budgetCollection}">
  <c:out value='${budgets.getInitial_budget()}'></c:out>
  <c:out value='${budgets.getYear()}'></c:out>
  <c:out value='${budgets.getQuarter()}'></c:out>
  <c:out value='${budgets.getReserved()}'></c:out>
  <c:out value='${budgets.getReimbursed()}'></c:out>
  <br>
  <br>
</c:forEach>

<!-- CREATE BUDGET (MAY MOVE TO SEPARATE PAGE LATER) -->
<h1> Create Budget </h1>
<form action="/createBudget" method="post">
  <label> Define budget: </label>
  <input type="number" name="initial_budget" min="0" required>
  <label> Select year: </label>
  <select name="year">
    <c:forEach begin="2015" end="2030" varStatus="loop">
    <option value="${loop.index}">${loop.index}</option>
    </c:forEach>
  </select>
  <label> Choose quarter: </label>
  <select name="quarter">
    <c:forEach begin="1" end="4" varStatus="loop">
    <option value="${loop.index}">${loop.index} </option>
    </c:forEach>
  </select>

  <input type="submit" value="Create Budget">
</form>

<!-- MODIFY BUDGET -->
<h1> Modify Budget </h1>
<form action="/modifyBudget" method="post">
  <label> Modify the budget for a chosen quarter: </label>
  <input type="number" name="newBudget" min="0" required>
  <label> Select year: </label>
  <select name="year">
    <c:forEach begin="2015" end="2030" varStatus="loop">
      <option value="${loop.index}">${loop.index}</option>
    </c:forEach>
  </select>
  <label> Choose quarter: </label>
  <select name="quarter">
    <c:forEach begin="1" end="4" varStatus="loop">
      <option value="${loop.index}">${loop.index} </option>
    </c:forEach>
  </select>

  <input type="submit" value="Modify Budget">
</form>




</body>
</html>
