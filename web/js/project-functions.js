$('button#submitMessage').click(function (event) {
    event.preventDefault();

    if($('textarea#message').val() !== "") {
        $.ajax({
            type: "post",
            url: "/api/postMessage", //this is my servlet
            data:   {userId: $('input#userId').val(),
                projectId: $('input#projectId').val(),
                companyId: $('input#companyId').val(),
                body: $('textarea#message').val()},
            success: function(msg){
                $('.project-items').append(msg);
                formatDates();
                $('textarea#message').val("");
            }
        });
    }
});

