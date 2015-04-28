<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="js/createcompany-functions.js"></script>

<div class="container">
<div class="big-paper">

        <h2> Register new user </h2>
        <form action="/createUser" method="post">
            <div class="input-group">
                <span>Select partner</span>
                <select name="selectedCompany">
                    <c:forEach var="companies" items="${companies}">
                        <option value="<c:out value='${companies.getId()}'></c:out>"><c:out value='${companies.getName()}'></c:out></option>
                    </c:forEach>
                </select>
            </div>
            <div class="input-group">
                <span>Full name</span>
                <input type="text" name="userName">
            </div>
            <div class="input-group">
                <span>Password</span>
                <input type="password" name="password">
            </div>
            <div class="input-group">
                <span>Email</span>
                <input type="email" name="userEmail">
            </div>
            <div class="input-group">
                <span>User type</span>
                <select name="role">
                    <option value="Partner">Partner</option>
                    <option value="Dell">Dell</option>
                </select>
            </div>

            <input class="button" type="submit" value="Register User">
        </form>

    </div>
</div>




</body>
</html>
