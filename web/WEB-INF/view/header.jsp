<%@ page import="Domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Dell</title>
  <meta name="description" content="Dell campaign management system">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="apple-touch-icon" sizes="57x57" href="favicon/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="favicon/apple-touch-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="favicon/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="favicon/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="favicon/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="favicon/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="favicon/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="favicon/apple-touch-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="favicon/apple-touch-icon-180x180.png">
    <link rel="icon" type="image/png" href="favicon/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon/favicon-194x194.png" sizes="194x194">
    <link rel="icon" type="image/png" href="favicon/favicon-96x96.png" sizes="96x96">
    <link rel="icon" type="image/png" href="favicon/android-chrome-192x192.png" sizes="192x192">
    <link rel="icon" type="image/png" href="favicon/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="favicon/manifest.json">
    <meta name="msapplication-TileColor" content="#2d89ef">
    <meta name="msapplication-TileImage" content="favicon/mstile-144x144.png">
    <meta name="theme-color" content="#00a3ff">

  <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,500&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
  <link rel="stylesheet" href="css/jquery.fancybox.css" type="text/css" media="screen" />
  <script type="text/javascript" src="js/jquery.fancybox.pack.js"></script>
  <script type="text/javascript" src="js/moment.js"></script>
  <script type="text/javascript" src="js/typeahead.bundle.min.js"></script>
  <script type="text/javascript" src="js/jensabox.js"></script>
  <script type="text/javascript" src="https://www.google.com/jsapi"></script>
  <link href="css/normalize.css" rel="stylesheet" media="all">
  <link href="css/skeleton.css" rel="stylesheet" media="all">
  <link href="css/style.css" rel="stylesheet" media="all">

  <script>
    function formatDates() {
      $('span.isDate').each(function() {
        var milis = parseInt($(this).text());
        var time = moment(milis).format('Do MMMM YYYY, H:mm');
        $(this).text(time);

        $(this).removeClass('isDate');
      });
        $('.isShortDate').each(function() {
            var millis = parseInt($(this).text());
            var m = millis.toString()
            if(millis == 0)
                $(this).text("N/A");
            else if(m.substring(m.length - 4, m.length) == 1000)
                $(this).text(moment(millis).format('MMM YYYY'));
            else
                $(this).text(moment(millis).format('MMM D[.] YYYY'));
        })
    }

    function highlightInputs() {
        if($('.notification.error').length > 0) {
            var $this = $('.notification.error');
            var inputErrorField = $this.text().split("|")[1].split(",");
            console.log("errorInputFields: " + inputErrorField);
            $this.text($this.text().substring(0, $this.text().indexOf("|")));

            for(var i = 0; i < inputErrorField.length; i++) {
                $("input[name=" + inputErrorField[i] + "]").addClass("error");
                $("textarea[name=" + inputErrorField[i] + "]").addClass("error");
                $("select[name=" + inputErrorField[i] + "]").addClass("error");
            }
        }
    }


    $(document).ready(function() {
      formatDates();
      highlightInputs();

        <c:if test="${formData != null}">
        var formData = [
            <c:forEach items="${formData}" var="d">
            ['<c:out value="${d[0]}" />','<c:out value="${d[1]}" escapeXml="false" />'],
            </c:forEach>
        ];
        for(var i = 0; i <formData.length; i++) {
            $('input[name=' + formData[i][0] + ']').val(formData[i][1]);
            $('textarea[name=' + formData[i][0] + ']').val(formData[i][1]);
            $('select[name=' + formData[i][0] + ']').val(formData[i][1]);
        }
        </c:if>

        var toggles = document.querySelectorAll(".cmn-toggle-switch");

        for (var i = toggles.length - 1; i >= 0; i--) {
            var toggle = toggles[i];
            toggleHandler(toggle);
        };

        function toggleHandler(toggle) {
            toggle.addEventListener( "click", function(e) {
                e.preventDefault();
                (this.classList.contains("active") === true) ? this.classList.remove("active") : this.classList.add("active");
            });
        }
    });
  </script>
</head>
<body onload="document.body.setAttribute('class','loaded')">
<c:set var="uri" value="${pageContext.request.requestURI}" />
<c:if test="${errorMessage != null}">
    <div class="notification error"><c:out value="${sessionScope.errorMessage}"></c:out></div>
    <c:if test="${sessionScope.deleteE != null}">
        <c:remove var="formData" scope="session" />
        <c:remove var="errorMessage" scope="session" />
    </c:if>
    <c:if test="${sessionScope.errorMessage != null}">
        <c:set var="deleteE" scope="session" value="true"></c:set>
    </c:if>
</c:if><c:if test="${message != null}">
    <div class="notification message"><c:out value="${sessionScope.message}"></c:out></div>
    <c:if test="${sessionScope.deleteM != null}">
        <c:remove var="message" scope="session" />
    </c:if>
    <c:if test="${sessionScope.message != null}">
        <c:set var="deleteM" scope="session" value="true"></c:set>
    </c:if>
</c:if>

<div class="header u-full-width">
  <div class="container">
    <a href="/dashboard">
      <div class="logo u-pull-left">
        <img src="img/small_dell_logo.svg" alt="Dell logo">
        <span>Campaign<br/>management<br/>system</span>
      </div>
    </a>
    <div class="user-label u-pull-right">
      <img src="img/white_dropdown.svg" alt="Logout menu">
      <span><c:out value="${User.getName()}"></c:out></span>
      <ul class="submenu">
        <li><a href="/logout">Logout</a></li>
      </ul>
    </div>
    <c:if test="${User.getCompany_id() == 1}">
        <div class="mobile u-pull-right" style="margin-top: 23px;">
            <button class="cmn-toggle-switch cmn-toggle-switch__htx">
                <span>toggle menu</span>
            </button>
        </div>
        <div class="u-pull-right desktop">
          <a href="/budgets" class="head-button <c:if test="${fn:contains(uri, 'budget')}">active</c:if>">Budgets</a>
          <a href="/users" class="head-button <c:if test="${fn:contains(uri, 'user')}">active</c:if>">Users</a>
          <a href="/partners" class="head-button <c:if test="${fn:contains(uri, 'partner')}">active</c:if>">Partners</a>
          <a href="/dashboard" class="head-button <c:if test="${fn:contains(uri, 'index')}">active</c:if>">Dashboard</a>
        </div>
        <div class="budget-label u-pull-left">

            <% if (request.getAttribute("activeBudget") != null) { %>
            <span class="big"><c:out value="${activeBudget.get(0).getLeftAvailable()}"></c:out>&#8364 <strong>(<c:out value="${activeBudget.get(0).getReserved()}"></c:out>&#8364 reserved)</strong></span>
            <span class="desc">is left available in this quarter</span>
            <% } else { %>
            <span class="big"> <a href="/budget_view">Set budget</a> </span>
            <span class="desc">No budget available</span>
            <% } %>

        </div>
    </c:if>
    <c:if test="${User.getCompany_id() != 1}">
    <div class="u-pull-right">
      <a href="/project-request" class="project-request button">Project request</a>
    </div>
    </c:if>

  </div>
</div>