<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="dellAndLatest" value="${User.getCompany_id() == 1 && lastStage}"></c:set>
<c:set var="partnerAndLatest" value="${User.getCompany_id() != 1 && lastStage}"></c:set>

<div class="item<c:if test='${stages.get(stageIndex).getUser().getId() == User.getId()}'> pull-right</c:if>">
    <span class="user-data"><c:out value="${stages.get(stageIndex).getUser().getName()}"></c:out> - <c:out value="${stages.get(stageIndex).getUser().getCompany().getName()}"></c:out></span>
    <span class="date isDate"><c:out value="${stages.get(stageIndex).getDate()}"></c:out></span>
    <div class="bubble">
        <div class="inner-bubble <c:if test="${stages.get(stageIndex).getType() == 'Project Approved'}">approved</c:if> <c:if test="${stages.get(stageIndex).getType() == 'Project Rejected' || stages.get(stageIndex).getType() == 'Claim Rejected'}">rejected</c:if>">

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Project Verification'}">
            <c:set var="balance" value="${project.getBudget()}" />
            <fmt:formatNumber var="i" type="number" value="${balance}" />
            <h3><c:out value="${project.getCompanyName()}"></c:out> is requesting <c:out value="${i}"></c:out>&#8364 for a <c:out value="${project.getType()}"></c:out></h3>
            <p><c:out value="${project.getBody()}"></c:out></p>
            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">Review the project; approve if satisfactory or reject if not.</p>
            </c:if><c:if test="${partnerAndLatest}">
                <p class="status-message">Dell will now review your request.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Rejected'}">
            <h3>Project has been rejected</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">Add a comment to explain your decision of rejecting the project.</p>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <p class="status-message">Dell has rejected your request. Change your request to match Dell�s comment.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Approved'}">
            <h3>Project has been Approved</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">You have now approved the project and partner is working on executing the project. A claim request will be made once executed.</p>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <p class="status-message">You are now allowed to execute the project. When executed, submit a claim with proper proof of execution.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Claim Verification'}">
            <h3>Waiting claim verification</h3>
            <c:forEach items="${poes}" var="poe" varStatus="ite" >
                <c:if test="${poe.getF_date() < stages.get(stageIndex).getDate() || (poe.getUploaded_on_stage() == stages.get(stageIndex).getId()) }">
                    <c:if test="${poe.getF_deletion_date() == 0 || (poe.getF_deletion_date() != 0 && poe.getF_deletion_date() > stages.get(stageIndex).getDate() && stageIndex + 1 != stages.size())}">
                        <div class="proof-container <c:if test="${poe.getF_date() > stages.get(stageIndex - 1).getDate() && poe.getF_date() < stages.get(stageIndex).getDate()}"> new</c:if>">
                            <c:choose>
                                <c:when test="${poe.getFiletype() == 'jpg' || poe.getFiletype() == 'png' || poe.getFiletype() == 'jpeg' || poe.getFiletype() == 'gif' || poe.getFiletype() == 'bmp'}">
                                    <div class="proof" style="background-image: url(/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>)">
                                        <a class="fancybox" rel="<c:out value='${poe.getProj_id()}'></c:out>" href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>"><div class="view-image"></div></a>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:when>
                                <c:when test="${poe.getFiletype() == 'xlsx' || poe.getFiletype() == 'xls' || poe.getFiletype() == 'numbers' || poe.getFiletype() == 'xml'}">
                                    <div class="proof excel">
                                        <div class="icon-space"></div>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:when>
                                <c:when test="${poe.getFiletype() == 'zip' || poe.getFiletype() == 'rar' || poe.getFiletype() == 'tar' || poe.getFiletype() == 'dmg'}">
                                    <div class="proof archive">
                                        <div class="icon-space"></div>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:when>
                                <c:when test="${poe.getFiletype() == 'mp3' || poe.getFiletype() == 'flac' || poe.getFiletype() == 'm4a' || poe.getFiletype() == 'wav' || poe.getFiletype() == 'flv' || poe.getFiletype() == 'mov' || poe.getFiletype() == 'mp4' || poe.getFiletype() == 'mpeg' || poe.getFiletype() == 'avi' || poe.getFiletype() == 'mkv'}">
                                    <div class="proof media">
                                        <div class="icon-space"></div>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="proof document">
                                        <div class="icon-space"></div>
                                        <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <span class="filename"><c:out value='${poe.getFilename()}'></c:out></span>
                            <c:if test="${partnerAndLatest}">
                                <form action="/api/deleteFile" method="post" class="delete-files">
                                    <input type="hidden" name="fileId" value="<c:out value='${poe.getId()}'></c:out>">
                                    <input type="hidden" name="deleteFile" value="<c:out value='${poe.getF_date() > stages.get(stageIndex - 1).getDate() ? "true" : "false"}'></c:out>">
                                    <input type="hidden" name="projectId" value="<c:out value='${project.getId()}'></c:out>">
                                    <input type="hidden" name="fileName" value="<c:out value='${poe.getFilename()}'></c:out>">
                                    <input type="submit" value="" class="delete-icon">
                                </form>
                            </c:if>

                        </div>
                </c:if></c:if>
            </c:forEach>
            <c:if test="${partnerAndLatest}">
                <div class="new-image">
                    <form action="/uploadFile" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="proj_id" value="<c:out value='${project.getId()}'></c:out>">
                        <input type="file" name="file">
                        <input type="hidden" name="stage" value="<c:out value='${stages.get(stageIndex).getId()}'></c:out>">
                        <input class="button" type="submit" name="submit" value="Upload">
                    </form>
                </div>
            </c:if>

            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">Review the claim and the attached proofs.</p>
            </c:if><c:if test="${partnerAndLatest}">
                <p class="status-message">Dell will now review your claim request.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Claim Rejected'}">
            <h3>Claim rejected</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">Add a comment to explain your decision of rejecting the claim.</p>
            </c:if><c:if test="${partnerAndLatest}">
                <p class="status-message">Dell has rejected your claim. Look in the comments for an explanation.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Finished'}">
            <h3>Project finished!</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <p class="status-message">Project is finished and now awaiting reimbursement.</p>
            </c:if><c:if test="${partnerAndLatest}">
                <p class="status-message">Claim has been approved and the project is now finished. Reimbursement is processing.</p>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Cancelled'}">
            <h3>Project has been cancelled</h3>
            </div>
            <p class="status-message">Project has been cancelled, look in the comments for further information.</p>
        </c:if>

        <c:if test="${dellAndLatest &&
                     (project.getStatus() == 'Waiting Project Verification' || project.getStatus() == 'Waiting Claim Verification')}">
            <form method="post" class="stage-actions" action="/api/changeProjectStatus">
                <input type="hidden" name="currentType" value="${project.getStatus()}">
                <input type="hidden" name="projectId" value="${project.getId()}">
                <button name="answer" value="approved" class="green">Approve</button>
                <button name="answer" value="denied" class="red">Reject</button>
            </form>
        </c:if>
    </div>
</div>
