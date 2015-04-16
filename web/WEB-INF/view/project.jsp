<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>

<c:set var="stageIndex" value="0"></c:set>
<c:set var="messageIndex" value="0"></c:set>

<c:if test="${project.getMessage() != null}"><c:out value="${project.getMessage()}"></c:out></c:if>

<div class="container project-container">
    <h1><c:out value="${project.getType()}" /></h1>
    <div class="project-state"><c:out value="${project.getStatus()}" /></div>
    <span class="state">State</span>

    <div class="project-items">
    <c:if test="${(stages.size() + messages.size()) > 0}">
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
                    <c:if test="${stages.size() > 0 && messages.size() > 0}">
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
                    </c:if>
                    <c:if test="${messages == null || messages.size() == 0}">
                        <%@ include file="shortcodes/stageItemByIndex.jsp" %>
                        <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
                    </c:if><c:if test="${stages == null || stages.size() == 0}">
                        <%@ include file="shortcodes/messageItemByIndex.jsp" %>
                        <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
    </div>

    <form method="post" action="/api/postMessage">
        <input type="hidden" name="userId" id="userId" value="${User.getId()}" />
        <input type="hidden" name="projectId" id="projectId" value="${project.getId()}" />
        <input type="hidden" name="companyId" value="${User.getCompany_id()}" />
        <textarea name="body" id="message" placeholder="Write your message"></textarea>
        <button id="submitMessage" class="submit">Send message</button>
    </form>


</div>

<!-- Buttons for admin -->
<!--
<c:if test="${User.getCompany_id() == 1}">
    <c:if test="${project.getStatus() == 'Waiting Project Verification'}">
        <button>Approve Project</button>
        <button>Deny Project</button>
    </c:if>
    <c:if test="${project.getStatus() == 'Waiting Claim Verification'}">
        <button>Approve and pay project</button>
        <button>Deny claim</button>
    </c:if>
</c:if>-->
<!-- Buttons for partner -->
<!--
<c:if test="${User.getCompany_id() != 1}">
    <button>Cancel Project</button>
    <c:if test="${project.getStatus() == 'Project Denied'}">
        <button>Request new project approval</button>
    </c:if>
    <c:if test="${project.getStatus() == 'Project Verified'}">
        <button>Submit claim</button>
    </c:if>
</c:if>-->

<script type="text/javascript" src="js/project-functions.js"></script>

</body>
</html>
