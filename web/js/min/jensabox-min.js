$(document).ready(function(){var i=$("div.jensabox");$(".jensabox-trigger").click(function(){i.css("visibility","visible"),i.addClass("visible")}),$(".jensabox .button-cancel").click(function(){i.removeClass("visible"),window.setTimeout(function(){i.css("visibility","hidden")},400)})});