<div class="item push-left">
    <span class="date"><c:out value="${stages.get(stageIndex).getDate()}"></c:out>, <c:out value="${stages.get(stageIndex).getUser().getName()}"></c:out></span>
    <div class="bubble">
        <h3><c:out value="${stages.get(stageIndex).getType()}"></c:out></h3>
        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Project Verification'}">
            <p><c:out value="${project.getBody()}"></c:out></p>
            <button class="green">Approve</button>
            <button class="red">Reject</button>
        </c:if>
    </div>
</div>