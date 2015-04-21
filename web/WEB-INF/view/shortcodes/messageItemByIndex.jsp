<div class="item message <c:if test="${messages.get(messageIndex).getUser().getId() == User.getId()}">pull-right</c:if>">
    <span class="user-data">
        <c:out value="${messages.get(messageIndex).getUser().getName()}"></c:out> -
        <c:out value="${messages.get(messageIndex).getCompany().getName()}"></c:out>
    </span>
    <span class="date isDate">
        <c:out value="${messages.get(messageIndex).getCreation_date_millis()}"></c:out>
    </span>
    <div class="inner-bubble">
        <p><c:out value="${messages.get(messageIndex).getBody()}"></c:out></p>
    </div>
</div>