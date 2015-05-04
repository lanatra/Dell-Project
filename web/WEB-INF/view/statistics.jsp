<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />

<script type="text/javascript">

  // Load the Visualization API and the piechart package.
  google.load('visualization', '1.0', {'packages':['corechart']});

  // Set a callback to run when the Google Visualization API is loaded.
  google.setOnLoadCallback(drawChart);

  // Callback that creates and populates a data table,
  // instantiates the pie chart, passes in the data and
  // draws it.
  function drawChart() {

    drawDistinctTypesCounts();
  }
  function drawDistinctTypesCounts() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Type');
    data.addColumn('number', 'Count');

    data.addRows(${distinctTypesCounts});

    // Set chart options
    var options = {'title':'Number of different types',
      'backgroundColor':'transparent',
      'width':400,
      'height':300,
      'pieHole':0.4};

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(document.getElementById('distinctTypesCounts'));
    chart.draw(data, options);
  }
</script>

<div class="container">
  <div id="distinctTypesCounts"></div>

</div>

</body>
</html>