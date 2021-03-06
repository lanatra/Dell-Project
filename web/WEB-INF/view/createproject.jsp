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
    <div class="amount-box">
      <span class="small-label">Euro is binding currency</span>
      <input class="amount" name="budget" type="text" placeholder="Amount"/>
      <span class="euro-label">&#8364</span>
    </div>
    <span>for a </span>
      <input class="amount custom-type" type="text" name="type" id="type">

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


<script>
  $('span.add_day').click(function() {
    setDays();
    $('span.add_day').css('display', 'none');
    $('select#day').css('display', 'block');
    $('select#day').addClass('visible');
  });

  $('select#month option').each(function(){
      var d = new Date();
      var m = d.getMonth();

      if($(this).val() < m+1) {
          $(this).remove();
      }
  });

  var searchNext = true;

  var removeNotMatches = function(list, q, sync) {
          var matches = [];
          var regex = new RegExp(q, "i");
          for (var i = 0; i < list.length; i++) {
              if (regex.test(list[i])) {
                  if (list == pres) {
                      matches.push(list[i])
                  } else if (pres.indexOf(list[i]) < 0) {
                      matches.push(list[i])
          }}}
          return matches;
  }


  var pres = ["Online advertising",
      "Billboard ad",
      "TV Promotion",
      "Face-to-face event",
        "Webinar",
        "Direct mail"];

  var preset = function(q, sync) {
      if(q == '')
          sync(pres)
      else
          sync(removeNotMatches(pres, q));
  }

  var types = [];

  var typesFilter = function(q, sync, async) {
      if(q.length == 3 && searchNext) {
          return $.ajax({
              dataType: "json",
              url: "/getTypes",
              data: {query: q},
              success: function(data) {
                  types = data;
                  async(removeNotMatches(types));
              }
          });
      } else
          return removeNotMatches(types, q, sync);
  }

  $('#type').typeahead({
              hint: false,
              highlight: true,
              minLength: 0
          }, {
              name: 'preset',
              source: preset
          }, {
              name: "type",
              source: typesFilter
          }

  ).change(function() {
              if($(this).val().length < 3)
                  searchNext = true;
              if($(this).val().length > 4)
                  searchNext = false;
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