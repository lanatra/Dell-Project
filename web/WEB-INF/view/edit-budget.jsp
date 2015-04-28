<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="js/createcompany-functions.js"></script>

<div class="container">
    <div class="big-paper">
        <h2>Edit budget</h2>
        <form action="/modifyBudget" method="post">
            <div class="input-group">
                <span>Define budget</span>
                <input type="number" name="newBudget" min="0" required value="<c:out value='${initialbudget}' />">
            </div>
            <div class="input-group">
                <span>Select year</span>
                <select name="year">
                <c:forEach begin="2015" end="2020" varStatus="loop">
                    <option value="${loop.index}" <c:if test="${loop.index.equals(year)}">selected</c:if> >${loop.index}</option>
                </c:forEach>
                </select>
            </div>
            <div class="input-group">
                <span>Choose quarter</span>
                <select name="quarter">
                    <c:forEach begin="1" end="4" varStatus="loop">
                        <option value="${loop.index}" <c:if test="${loop.index.equals(quarter)}">selected</c:if> >${loop.index} </option>
                    </c:forEach>
                </select>
            </div>

            <input class="button" type="submit" value="Edit budget">
        </form>

    </div>
</div>

</body>
</html>
