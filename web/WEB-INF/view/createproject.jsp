<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />

<div class="container project" style="margin-top: 60px;">
  <H1>Project request</H1>

  <form action="request">
    <span>Fona company is requesting</span>
    <input class="amount" name="amount" type="text" value="$"/>
    <span>for advertisement described below.</span>
    <textarea name="description" id="description" placeholder="Describe your project here."></textarea>
    <button type="submit" class="button">Send request</button>
  </form>
</div>

</body>
</html>