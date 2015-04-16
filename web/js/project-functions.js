$('button#submitMessage').click(function () {
    $.ajax({
        type: "post",
        url: "/api/postMessage", //this is my servlet
        data:   "userId=" +$('input#userId').val()+
                "&projectId="+$('input#projectId').val()+
                "&body="+$('textarea#message').val(),
        success: function(msg){
            $('.project-items').append(msg);
            $('textarea#message').val("");
        }
    });
});