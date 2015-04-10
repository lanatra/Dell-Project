<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
  <div class="login-background u-pull-left">

    <div class="container" style="margin-top: 200px;">
      <div class="row">
        <div class="eight columns" style="padding-right: 30px;">
          <h1>Login instructions</h1>
          <p>Please fill-in login credentials. Initial testing login is: <br><br>
          Email: email.dk <br>
          Password: hest</p>
        </div>
        <div class="four columns login-form">
          <div class="message u-full-width u-pull-left">
              <% if (request.getAttribute("message") != null) { %>
                <p><%= request.getAttribute("message") %></p>
              <%};%>
          </div>
          <form action="/login" method="post" class="u-full-width" autocomplete="off">
            <input type="hidden" name="action" value="login">
            <input class="u-full-width just-line" type="text" name="email" placeholder="email">
            <input class="u-full-width just-line" type="password" name="password" placeholder="password">
            <input class="u-full-width button submit" type="submit" value="login">
          </form>
        </div>
      </div>
    </div>

  </div>

</body>
</html>
