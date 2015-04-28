<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>

<div class="container result-container">
    <h4 class="search-query">Searching for: <c:out value="${param.q}"></c:out></h4>
        <c:forEach items="${results}" var="container">
            <div class="result-category <c:out value="${container.getType()}"></c:out>-category"><h3><c:out value="${container.getType()}"></c:out>s</h3>
            <c:forEach items="${container.getContainer()}" var="result">
                <div class="result-item">
                    <div class="id">
                        <c:if test="${result.getType() == 'User'}">
                            <a href='/user?id=<c:out value="${result.getId()}"></c:out>'><c:out value="${result.getId()}"></c:out></a></div>
                        </c:if><c:if test="${result.getType() != 'User'}">
                            <a href='/project?id=<c:out value="${result.getId()}"></c:out>'><c:out value="${result.getId()}"></c:out></a></div>
                        </c:if>
                    <div class="type"><c:out value="${result.getType()}"></c:out></div>
                    <div class="body"><c:out value="${result.getBody()}"></c:out></div>
                </div>
            </c:forEach>
            </div>
        </c:forEach>


<script type="application/javascript">
    $(function() {
        var q = "<c:out value="${param.q}"></c:out>";
        var regex = new RegExp( '(' + q + ')', 'gi' );
       $(".result-item .body").each(function() {
           //console.log($(this).text().replace( regex, "<strong>$1</strong>" ));
           $(this).html($(this).html().replace( regex, "<strong>$1</strong>" ));
       })
    });
</script>


</div>
</body>
</html>
