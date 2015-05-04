<%--
  Created by IntelliJ IDEA.
  User: Andreas Poulsen
  Date: 28-Apr-15
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Dell - Reset password</title>
    <meta name="description" content="Dell campaign management system">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,500&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link href="/css/normalize.css" rel="stylesheet" media="all">
    <link href="/css/skeleton.css" rel="stylesheet" media="all">
    <link href="/css/style.css" rel="stylesheet" media="all">
</head>
<body>
<div class="login-background u-pull-left">

    <div class="container" style="margin-top: 200px;">
        <div class="row">
            <div class="eight columns" style="padding-right: 30px;">
                <h1>Set password</h1>
                <p>Please set the password you will use to access <br/>Dell campaign management system.</p>
            </div>
            <div class="four columns login-form">
                <div class="message u-full-width u-pull-left">
                    <p><c:out value="${error}"></c:out></p>
                </div>
                <form action="/reset-password" method="post" class="u-full-width">
                    <input type="hidden" name="nonce" value="<c:out value="${param.n}"></c:out>">
                    <input type="hidden" name="user_id" value='<c:out value="${userId}"></c:out>'>
                    <input type="password" class="u-full-width just-line" name="pw" placeholder="Enter your new password">
                    <input type="submit" class="u-full-width button submit" value="Change Password">
                </form>
            </div>
        </div>
    </div>

</div>

</body>
</html>