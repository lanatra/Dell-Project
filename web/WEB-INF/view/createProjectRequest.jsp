<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%@ include file="header.jsp" %>

<form>
    <label for="type">Type</label>
    <select id="type" name="type">
        <option>Web Advertisement</option>
        <option>Web Advertisement</option>
        <option>Web Advertisement</option>
        <option>Web Advertisement</option>
        <option>Other</option>
    </select>
    <!-- if select is other:
     <input type="text" name="type">
     -->
    <input type="text" name="">
</form>

</body>
</html>
