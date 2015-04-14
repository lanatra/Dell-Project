<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%@ include file="header.jsp" %>
<h1>Project #<c:out value="${project.getId()}" /></h1>
<h2><c:out value="${project.getCompanyName()}" /></h2>




    <h2>Messages</h2>
    ${messages.size() == 0 ? '<small>No messages</small>' : '' }
<c:forEach var="message" items="${messages}">
        <div class="message-item">
            <span class="id"><strong>#</strong><c:out value="${message.getId()}" /></span>
            <span class="partner"><c:out value="${message.getUser().getName()}" /></span>
            <span class="partner"><c:out value="${message.getCompany().getName()}" /></span>
            <span class="partner"><c:out value="${message.getCompany().getImg_filename()}" /></span>
            <span class="type"><c:out value="${message.getBody()}" /></span>
            <span class="notification small"><c:out value="${message.getCreation_date_millis()}" /></span>
        </div>
</c:forEach>
<form method="post" action="/api/postMessage">
    <input type="hidden" name="userId" value="${User.getId()}" />
    <input type="hidden" name="projectId" value="${project.getId()}" />
    <textarea name="body" placeholder="Write your message.."></textarea>
    <input type="submit">
</form>
</body>
</html>
