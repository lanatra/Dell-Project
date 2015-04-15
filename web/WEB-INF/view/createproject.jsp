<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />

<div class="container project" style="margin-top: 60px;">
  <H1>Project request</H1>

  <form action="/project-request" method="post">
    <span>__Fona company__ is requesting</span>
    <input class="amount" name="budget" type="text" value="$"/>
    <span>for</span>
    <select name="type" id="type">
      <option value="web-campaign">Web campaign</option>
      <option value="billboard-ad">Billboard ad</option>
      <option value="tv-promotion">TV promotion</option>
    </select>
    <span style="clear: left;">With execution scheduled</span>
    <select name="type" id="year">
      <option value="2015">2015</option>
      <option value="2016">2016</option>
    </select>
    <select name="type" id="month">
      <option value="january">January</option>
      <option value="february">February</option>
      <option value="march">March</option>
      <option value="april">April</option>
      <option value="may">May</option>
      <option value="june">June</option>
      <option value="july">July</option>
      <option value="august">August</option>
      <option value="september">September</option>
      <option value="october">October</option>
      <option value="november">November</option>
      <option value="december">December</option>
    </select>
    <span class="add_day">Add day</span>
    <select name="day" id="day"style="display: none">

    </select>
    <textarea name="project_body" id="description" placeholder="Describe your project here."></textarea>
    <button type="submit" class="button">Send request</button>
  </form>
</div>

<script>
  var days = 31;
  for(var i=1; i<=days; i++) {
    $('select#day').append("<option value='" + i + "'>" + i +".</option>");
  }

  $('span.add_day').click(function() {
    $('span.add_day').css('display', 'none');
    $('select#day').css('display', 'block');
  });

  $('select#month').change(function() {
    var e = document.getElementById("month");
    var strMonth = e.options[e.selectedIndex].value;

    if(strMonth == "january" || strMonth == "march" || strMonth == "may" ||
            strMonth == "july" || strMonth == "august" || strMonth == "october" || strMonth == "december") {
      days = 31;
    } else if(strMonth == "april" || strMonth == "june" || strMonth == "september" || strMonth == "november") {
      days = 30;
    } else if(strMonth == "february") {
      days = 28;
    }

    $('select#day').html("");
    for(var i=1; i<=days; i++) {
      $('select#day').append("<option value='" + i + "'>" + i +".</option>");
    }
  });
</script>

</body>
</html>