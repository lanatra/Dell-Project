$('button#submitMessage').click(function (event) {
    var val = $('textarea#message').val();
    val = val.replace(/\r|\n|\r\n/g, '<br>');

    event.preventDefault();
    $.ajax({
        type: "post",
        url: "/api/postMessage", //this is my servlet
        data:   "userId=" +$('input#userId').val()+
                "&projectId="+$('input#projectId').val()+
                "&companyId="+$('input#companyId').val()+
                "&body="+val,
        success: function(msg){
            $('.project-items').append(msg);
            formatDates();
            $('textarea#message').val("");
        }
    });
});