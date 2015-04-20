<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />

<div class="container project" style="margin-top: 60px;">
  <H1>Project request</H1>

  <form action="/project-request" method="post">
    <span><c:out value="${User.getCompany().getName()}" /> is requesting</span>
    <input class="amount" name="budget" type="text" placeholder="Amount"/>
    <span>for</span>
    <select name="type" id="type">
      <option value="Web Campaign">Web campaign</option>
      <option value="Billboard ad">Billboard ad</option>
      <option value="TV Promotion">TV promotion</option>
    </select>
    <span style="clear: left;">With execution scheduled</span>
    <select name="execution_year" id="year">
      <option value="2015">2015</option>
      <option value="2016">2016</option>
    </select>
    <select name="execution_month" id="month">
      <option value="1">January</option>
      <option value="2">February</option>
      <option value="3">March</option>
      <option value="4">April</option>
      <option value="5">May</option>
      <option value="6">June</option>
      <option value="7">July</option>
      <option value="8">August</option>
      <option value="9">September</option>
      <option value="10">October</option>
      <option value="11">November</option>
      <option value="12">December</option>
    </select>
    <span class="add_day">Add day</span>
    <select name="execution_day" id="day"style="display: none">
      <option value='0'>0</option>
    </select>
    <textarea name="body" id="description" placeholder="Describe your project here."></textarea>
    <button type="submit" class="button">Send request</button>
  </form>
</div>


<!-- testing upload functionality:-->
<form action="/uploadFile" method="post" enctype="multipart/form-data">
  <input type="file" name="file" width="100">
  <input type="submit" name="submit" value="Submit">
</form>

<a href="C:\Users\Lasse\Desktop\File Storage Dell\1" download="ugeseddel1.pdf">click me </a>

<form action="/downloadFile" method="post">
<input type="submit" value="Submit">
</form>

<br>

<script>
  $('span.add_day').click(function() {
    setDays();
    $('span.add_day').css('display', 'none');
    $('select#day').css('display', 'block');
    $('select#day').addClass('visible');
  });

  $('select#month').change(function() {
    if($('select#day').hasClass('visible')){
      setDays();
    }
  });

  function setDays() {
    var days = 31;
    var e = document.getElementById("month");
    var strMonth = e.options[e.selectedIndex].value;

    if(strMonth == "1" || strMonth == "3" || strMonth == "5" ||
            strMonth == "7" || strMonth == "8" || strMonth == "10" || strMonth == "12") {
      days = 31;
    } else if(strMonth == "4" || strMonth == "6" || strMonth == "9" || strMonth == "11") {
      days = 30;
    } else if(strMonth == "2") {
      days = 28;
    }

    $('select#day').html("");
    for(var i=1; i<=days; i++) {
      $('select#day').append("<option value='" + i + "'>" + i +".</option>");
    }
  }
</script>

</body>
</html>