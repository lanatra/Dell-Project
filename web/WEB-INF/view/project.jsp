<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%@ include file="header.jsp" %>
<h1>Project #<c:out value="${project.getId()}" /></h1>
<h2><c:out value="${project.getCompanyName()}" /></h2>
<c:set var="stageIndex" value="0"></c:set>
<c:set var="messageIndex" value="0"></c:set>
<c:forEach var="i" begin="0" end="${stages.size() + messages.size() - 1}">
    <c:choose>
        <c:when test="${stageIndex == -1}">
            <%@ include file="shortcodes/messageItemByIndex.jsp" %>
            <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
        </c:when>
        <c:when test="${messageIndex == -1}">
            <%@ include file="shortcodes/stageItemByIndex.jsp" %>
            <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${stages.get(stageIndex).getDate() < messages.get(messageIndex).getCreation_date_millis()}">
                    <%@ include file="shortcodes/stageItemByIndex.jsp" %>
                    <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
                    <c:if test="${stageIndex == stages.size()}">
                        <c:set var="stageIndex" value="-1"></c:set>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <%@ include file="shortcodes/messageItemByIndex.jsp" %>
                    <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
                    <c:if test="${messageIndex == messages.size()}">
                        <c:set var="messageIndex" value="-1"></c:set>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</c:forEach>



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

<!-- Buttons for admin -->
<c:if test="${User.getCompany_id() == 1}">
    <c:if test="${project.getStatus() == 'Waiting Project Verification'}">
        <button>Approve Project</button>
        <button>Deny Project</button>
    </c:if>
    <c:if test="${project.getStatus() == 'Waiting Claim Verification'}">
        <button>Approve and pay project</button>
        <button>Deny claim</button>
    </c:if>
</c:if>
<!-- Buttons for partner -->
<c:if test="${User.getCompany_id() != 1}">
    <button>Cancel Project</button>
    <c:if test="${project.getStatus() == 'Project Denied'}">
        <button>Request new project approval</button>
    </c:if>
    <c:if test="${project.getStatus() == 'Project Verified'}">
        <button>Submit claim</button>
    </c:if>
</c:if>

<form method="post" action="/api/postMessage">
    <input type="hidden" name="userId" value="${User.getId()}" />
    <input type="hidden" name="projectId" value="${project.getId()}" />
    <textarea name="body" placeholder="Write your message.."></textarea>
    <input type="submit">
</form>
</body>
</html>
