$(document).ready(function() {
    var jensabox = $('div.jensabox');

    $('.jensabox-trigger').click(function(){
       //jensabox.css('display', 'table');
       jensabox.css('visibility', 'visible');
       jensabox.addClass('visible');
    });

    $('.jensabox .button-cancel').click(function() {
       //jensabox.css('display', 'none');
        jensabox.removeClass('visible');
        window.setTimeout(function(){
            jensabox.css('visibility', 'hidden');
        },400);
    });
});