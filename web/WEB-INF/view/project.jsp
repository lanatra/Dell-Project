<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>
<h1>Project #<c:out value="${project.getId()}" /></h1>
<h2><c:out value="${project.getCompanyName()}" /></h2>
<c:set var="stageIndex" value="0"></c:set>
<c:set var="messageIndex" value="0"></c:set>
<c:if test="${(stages.size() + messages.size() - 1) > 0}">
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
</c:if>

<div class="container project-container">
    <h1>Project name</h1>
    <div class="project-state">Project waiting for approval</div>
    <span class="state">State</span>

    <div class="project-items">
        <div class="item push-left">
            <span class="date">2. July 2015</span>
            <div class="bubble">
                <h3>Fona company is requesting 10 000 DKK for this campaign.</h3>
                <p>Ut molestie venenatis semper. Aliquam erat volutpat. Morbi tincidunt volutpat sapien, sit amet tempus enim vehicula ut.
                    Sed blandit mauris ut tempor ultricies. Donec a molestie purus. Praesent justo metus, porta eu dui et, tempus hendrerit
                    odio. In vitae eros in diam mattis facilisis. Etiam dignissim felis nec leo feugiat volutpat sodales id lorem.
                    Integer et mollis lectus.</p>
                <button class="green">Approve</button>
                <button class="red">Reject</button>
            </div>
        </div>
    </div>

    <form action="project-message">
        <textarea name="message" id="message" placeholder="Start writing a message here…"></textarea>
        <button type="submit" class="submit">Send message</button>
    </form>

</div>

<!--
<h1>Project #<c:out value="${project.getId()}" /></h1>
<h2><c:out value="${project.getCompanyName()}" /></h2>


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
