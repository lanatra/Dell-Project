<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="js/createcompany-functions.js"></script>

<div class="container">
<div class="big-paper">
    <h2>Register new partner</h2>
    <form action="/api/createCompany" method="post">
        <div class="input-group">
            <span>Name of the partner</span>
            <input type="text" id="companyName" name="companyName">
        </div>
        <div class="input-group">
            <span>Select country</span>
            <select id="countryCode" name="countryCode">
                <option value="DK">Denmark</option>
                <option value="SE">Sweden</option>
                <option value="NO">Norway</option>
                <option value="DE">Germany</option>
                <option value="PL">Poland</option>
                <option value="CZ">Czech</option>
            </select>
        </div>

        <div class="logo-box" style="float: left; clear: left;">
            <span>Select logo</span>
            <div id="logo-results"></div>
            <div id="branding"  style="float: left;"></div><br>
            <span>Or upload your own</span>
            <input type="file" name="logo">
            <input type="hidden" name="logoUrl" id="logo-url">
        </div>
        <input class="button" type="submit" value="Create Company">
    </form>

    </div>
</div>

</body>
</html>
