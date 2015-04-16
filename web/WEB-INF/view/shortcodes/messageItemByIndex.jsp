<div class="item <c:if test="${messages.get(messageIndex).getUser().getId() == User.getId()}">pull-right</c:if>">
    <span class="user-data">
        <c:out value="${messages.get(messageIndex).getUser().getName()}"></c:out> -
        <c:out value="${messages.get(messageIndex).getCompany().getName()}"></c:out>
    </span>
    <span class="date" id="<c:out value="${messages.get(messageIndex).getCreation_date_millis()}"></c:out>">
        <script>
            var milis = <c:out value="${messages.get(messageIndex).getCreation_date_millis()}"></c:out>;

            var time = moment(milis).format('Do MMMM YYYY, H:mm');
            $('span#' + milis).html(time);
        </script>
    </span>
    <div class="bubble">
        <p><c:out value="${messages.get(messageIndex).getBody()}"></c:out></p>
    </div>
</div>