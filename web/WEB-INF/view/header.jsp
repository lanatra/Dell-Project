<%@ page import="Domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Dell</title>
  <meta name="description" content="Dell campaign management system">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,500&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
  <link rel="stylesheet" href="css/jquery.fancybox.css" type="text/css" media="screen" />
  <script type="text/javascript" src="js/jquery.fancybox.pack.js"></script>
  <script type="text/javascript" src="js/moment.js"></script>
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

    $(document).ready(function() {
      formatDates();
    });
  </script>
</head>
<body>

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
        <div class="budget-label u-pull-right">
            <span class="big">834 039 DKK</span>
            <span class="desc">is left available in this quarter (1.1.2015 - 31.3.2015)</span>
        </div>
    </c:if>
    <c:if test="${User.getCompany_id() != 1}">
    <div class="u-pull-right">
      <a href="/project-request" class="project-request button">Project request</a>
    </div>
    </c:if>

  </div>
</div>