<c:set var="dellAndLatest" value="${User.getCompany_id() == 1 && lastStage}"></c:set>
<c:set var="partnerAndLatest" value="${User.getCompany_id() != 1 && lastStage}"></c:set>

<div class="item<c:if test='${stages.get(stageIndex).getUser().getId() == User.getId()}'> pull-right</c:if>">
    <span class="user-data"><c:out value="${stages.get(stageIndex).getUser().getName()}"></c:out> - <c:out value="${project.getCompanyName()}"></c:out></span>
    <span class="date isDate"><c:out value="${stages.get(stageIndex).getDate()}"></c:out></span>
    <div class="bubble">
        <div class="inner-bubble">

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Project Verification'}">
            <h3><c:out value="${project.getCompanyName()}"></c:out> is requesting <c:out value="${project.getBudget()}"></c:out> for a <c:out value="${project.getType()}"></c:out></h3>
            <p><c:out value="${project.getBody()}"></c:out> </p>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Review the project; approve if satisfactory or reject if not.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Dell will now review your request.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Rejected'}">
            <h3>Project has been rejected</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Add a comment to explain your decision of rejecting the project.</span>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <span class="status-message">Dell has rejected your request. Change your request to match Dell�s comment.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Approved'}">
            <h3>Project has been Approved</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">You have now approved the project and partner is working on executing the project. A claim request will be made once executed.</span>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <span class="status-message">You are now allowed to execute the project. When executed, submit a claim with proper proof of execution.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Claim Verification'}">
            <h3>Waiting claim verification</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Review the claim and the attached proofs.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Dell will now review your claim request.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Claim Rejected'}">
            <h3>Claim rejected</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Add a comment to explain your decision of rejecting the claim.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Dell has rejected your claim. Look in the comments for an explanation.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Finished'}">
            <h3>Project finished!</h3>
            </div>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Project is finished and now awaiting reimbursement.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Claim has been approved and the project is now finished. Reimbursement is processing.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Cancelled'}">
            <h3>Project has been cancelled</h3>
            </div>
            <span class="status-message">Project has been cancelled, look in the comments for further information.</span>
        </c:if>

        <c:if test="${dellAndLatest &&
                     (project.getStatus() == 'Waiting Project Verification' || project.getStatus() == 'Waiting Claim Verification')}">
            <form method="post" action="/api/changeProjectStatus">
                <input type="hidden" name="currentType" value="${project.getStatus()}">
                <input type="hidden" name="projectId" value="${project.getId()}">
                <button name="answer" value="approved" class="green">Approve</button>
                <button name="answer" value="denied" class="red">Reject</button>
            </form>
        </c:if>

    </div>
</div>