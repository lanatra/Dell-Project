<div class="item <c:if test="${stages.get(stageIndex).getUser().getId() == User.getId()}">pull-right</c:if>">
    <span class="date"><c:out value="${stages.get(stageIndex).getDate()}"></c:out>, <c:out value="${stages.get(stageIndex).getUser().getName()}"></c:out></span>
    <div class="bubble">
        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Project Verification'}">
            <h3><c:out value="${project.getCompanyName()}"></c:out> is requesting <c:out value="${project.getBudget()}"></c:out> in this project.</h3>
            <p><c:out value="${project.getBody()}"></c:out></p>
            <c:if test="${User.getCompany_id() == 1 && stages.get(stageIndex).getType() == project.getStatus()}">
            <form method="post" action="/api/changeProjectStatus">
                <input type="hidden" name="currentType" value="${project.getStatus()}">
                <input type="hidden" name="projectId" value="${project.getId()}">
                <button name="answer" value="approved" class="green">Approve</button>
                <button name="answer" value="denied" class="red">Reject</button>
            </form>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Approved'}">
            <h3>Project was approved by Dell.</h3>
        </c:if>
    </div>
</div>