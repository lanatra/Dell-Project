<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>

<c:set var="stageIndex" value="0"></c:set>
<c:set var="messageIndex" value="0"></c:set>

<c:set var="lastStage" value="false"></c:set>

<c:if test="${project.getMessage() != null}"><c:out value="${project.getMessage()}"></c:out></c:if>

<div class="container project-container">
    <h1><span style="color: #9F9F9F;">#<c:out value="${project.getId()}" /></span> <c:out value="${project.getType()}" /></h1>
    <div class="project-state"><c:out value="${project.getStatus()}" /></div>
    <span class="state">State</span>

    <div class="project-items">
    <c:if test="${(stages.size() + messages.size()) > 0}">
        <c:forEach var="i" begin="0" end="${stages.size() + messages.size() - 1}">
            <c:if test="${stages.size() - 1 == stageIndex}">
                <c:set var="lastStage" value="true"></c:set>
            </c:if>
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


    <c:if test="${User.getCompany_id() != 1 && (project.getStatus() == 'Project Rejected' || project.getStatus() == 'Project Approved' || project.getStatus() == 'Claim Rejected')}">
        <div class="item pull-right u-full-width">
            <div class="bubble">

                <c:if test="${project.getStatus() == 'Project Approved' || project.getStatus() == 'Claim Rejected'}">
                    <div class="inner-bubble">
                        <h3>Proof Of Execution</h3>
                        <p class="instructions">Upload your images and documents, one by one.</p>
                        <c:forEach items="${poes}" var="poe" varStatus="ite" >
                            <c:if test="${poe.getF_deletion_date() == 0}">
                            <div class="proof-container<c:if test="${poe.getF_date() > stages.get(stageIndex).getDate()}"> new</c:if>">
                                <c:if test="${poe.getFiletype() == 'jpg' ||
                                    poe.getFiletype() == 'png' ||
                                    poe.getFiletype() == 'jpeg' ||
                                    poe.getFiletype() == 'gif' ||
                                    poe.getFiletype() == 'bmp'}">
                                    <div class="proof" style="background-image: url(/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>)">
                                        <a class="fancybox" rel="<c:out value='${poe.getProj_id()}'></c:out>" href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>"><div class="view-image"></div></a>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:if>
                                <span class="filename"><c:out value='${poe.getFilename()}'></c:out></span>
                                <form action="/api/deleteFile" method="post" class="delete-files">
                                    <input type="hidden" name="fileId" value="<c:out value='${poe.getId()}'></c:out>">
                                    <input type="hidden" name="deleteFile" value="<c:out value='${poe.getF_date() > stages.get(stageIndex - 1).getDate() ? "true" : "false"}'></c:out>">
                                    <input type="hidden" name="projectId" value="<c:out value='${project.getId()}'></c:out>">
                                    <input type="hidden" name="fileName" value="<c:out value='${poe.getFilename()}'></c:out>">
                                    <input type="submit" value="" class="delete-icon">
                                </form>
                            </div>
                            </c:if>
                        </c:forEach>
                        <div class="new-image">
                            <form action="/uploadFile" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="proj_id" value="<c:out value='${project.getId()}'></c:out>">
                                <input type="file" name="file">
                                <input class="button" type="submit" name="submit" value="Upload">
                            </form>
                        </div>
                    </div>
                </c:if>

                <form method="post" action="/api/changeProjectStatus">
                    <input type="hidden" name="currentType" value="${project.getStatus()}">
                    <input type="hidden" name="projectId" value="${project.getId()}">

                    <c:if test="${project.getStatus() == 'Project Rejected'}">
                        <h3>Resubmit Project</h3>
                        <span><c:out value="${User.getCompany().getName()}" /> is requesting</span>
                        <input class="amount" name="budget" type="text" placeholder="Amount"/>
                        <span>for</span>
                        <select name="type" id="type">
                            <option value="Web Campaign">Web campaign</option>
                            <option value="Billboard ad">Billboard ad</option>
                            <option value="TV Promotion">TV promotion</option>
                        </select>
                        <span style="clear: left;">With execution scheduled</span>
                        <select name="execution_year" id="year">
                            <option value="2015">2015</option>
                            <option value="2016">2016</option>
                        </select>
                        <select name="execution_month" id="month">
                            <option value="1">January</option>
                            <option value="2">February</option>
                            <option value="3">March</option>
                            <option value="4">April</option>
                            <option value="5">May</option>
                            <option value="6">June</option>
                            <option value="7">July</option>
                            <option value="8">August</option>
                            <option value="9">September</option>
                            <option value="10">October</option>
                            <option value="11">November</option>
                            <option value="12">December</option>
                        </select>
                        <span class="add_day">Add day</span>
                        <select name="execution_day" id="day"style="display: none">
                            <option value='0'>0</option>
                        </select>
                        <textarea name="body" id="description" placeholder="Describe your project here."></textarea>


                        <button name="answer" value="approved" class="green">Resubmit project</button>
                    </c:if><c:if test="${project.getStatus() == 'Project Approved'}">
                    <p class="status-message">When the project is finished, upload images and documents as a proof of execution.</p>
                    <div class="stage-actions">
                        <button name="answer" value="approved" class="blue">Send</button>
                    </div>

                </c:if><c:if test="${project.getStatus() == 'Claim Rejected'}">
                    <p class="status-message">Resubmit claim, upload new proof of execution.</p>
                    <div class="stage-actions">
                        <button name="answer" value="approved" class="blue">Resubmit claim</button>
                    </div>
                </c:if>
                </form>
            </div>
        </div>
    </c:if>
    </div>


    <form>
        <input type="hidden" name="userId" id="userId" value="${User.getId()}" />
        <input type="hidden" name="projectId" id="projectId" value="${project.getId()}" />
        <input type="hidden" name="companyId" id="companyId" value="${User.getCompany_id()}" />
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

<script>
    $(".fancybox").fancybox();
</script>
<script type="text/javascript" src="js/project-functions.js"></script>

</body>
</html>
