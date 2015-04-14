<%@ page import="Domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <title>Dell</title>
    <meta name="description" content="Dell campaign management system">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,500&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link href="css/normalize.css" rel="stylesheet" media="all">
    <link href="css/skeleton.css" rel="stylesheet" media="all">
    <link href="css/style.css" rel="stylesheet" media="all">
</head>
<body>

<div class="header u-full-width">
    <div class="container">
        <a href="/logout">
            <div class="logo u-pull-left">
                <img src="img/small_dell_logo.svg" alt="Dell logo">
                <span>Campaign<br/>management<br/>system</span>
            </div>
        </a>
        <div class="user-label u-pull-right">
            <img src="img/white_dropdown.svg" alt="Logout menu">
        <span><c:out value="${User.getName()}"></c:out></span>
        </div>
        <c:if test="${User.getCompany_id() == 1}">
            <div class="budget-label u-pull-right">
                <span class="big">834 039 DKK</span>
                <span class="desc">is left available in this quarter (1.1.2015 - 31.3.2015)</span>
            </div>
        </c:if>
        <c:if test="${User.getCompany_id() != 1}">
            <div class="u-pull-right">
                <a href="/createProjectRequest" class="btn">+ Create Project Request</a>
            </div>
        </c:if>
    </div>
</div>
