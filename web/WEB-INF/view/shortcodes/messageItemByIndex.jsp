<div class="message-item">
    <div>Author: <c:out value="${messages.get(messageIndex).getUser().getName()}"></c:out> </div>
    <div>Company: <c:out value="${messages.get(messageIndex).getCompany().getName()}"></c:out> </div>
    <div>Body: <c:out value="${messages.get(messageIndex).getBody()}"></c:out> </div>
    <div>Date: <c:out value="${messages.get(messageIndex).getCreation_date_millis()}"></c:out> </div>
</div>