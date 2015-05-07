<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />

<script type="text/javascript">

  // Load the Visualization API and the piechart package.
  google.load('visualization', '1.0', {'packages':['corechart','line', 'geochart']});

  // Set a callback to run when the Google Visualization API is loaded.
  google.setOnLoadCallback(drawChart);

  // Callback that creates and populates a data table,
  // instantiates the pie chart, passes in the data and
  // draws it.
  function drawChart() {
    var charts = ${statistics};
    for(var i = 0; i < charts.length; i++) {
      if (charts[i][0][1] == 'donut')
        drawDonut(charts[i]);
      else if(charts[i][0][1] == 'line')
        drawLine(charts[i]);
      else if(charts[i][0][1] == 'number')
        drawNumber(charts[i]);
      else if(charts[i][0][1] == 'geomap')
        drawMap(charts[i])
    }

  }
  function drawDonut(options) {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Type');
    data.addColumn('number', 'Count');

    var d = [];
    for(var i = 1; i < options.length; i++) {
      d.push(options[i]);
      console.log(d);
    }
    data.addRows(d);

    // Set chart options
    var o = {'title':options[0][0],
      'backgroundColor':'transparent',
      'width':450,
      'height':300,
      'pieHole':0.4};

    var id = options[0][0].split(" ").join("");

    if(id == 'Typeswithhighestsuccessrate') {
      var formatter = new google.visualization.NumberFormat({pattern:'#%'});
      formatter.format(data, 1);
    }

    // Instantiate and draw our chart, passing in some options.
    var div = document.createElement('div');
    div.id = id;
    document.getElementById('graphs').appendChild(div);
    var chart = new google.visualization.PieChart(document.getElementById(id));
    chart.draw(data, o);
  }

 function drawMap(options) {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Region');
    data.addColumn('number', 'Count');

    var d = [];
    for(var i = 1; i < options.length; i++) {
      d.push(options[i]);
      console.log(d);
    }
    data.addRows(d);

    // Set chart options
    var o = {
      region: 150,
      'width':800,
      'height':500};

    var id = options[0][0].split(" ").join("");


    // Instantiate and draw our chart, passing in some options.
    var div = document.createElement('div');
    div.id = id;
    var title = document.createElement('h5');
    title.innerText = options[0][0];
    document.getElementById('graphs').appendChild(title);
    document.getElementById('graphs').appendChild(div);
    var chart = new google.visualization.GeoChart(document.getElementById(id));
    chart.draw(data, o);
  }

  function drawLine(options) {
    var id = options[0][0].split(" ").join("");

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', 'Budget Expenses');
    data.addColumn('number', 'Budget Projection');

    var d = [];
    var c = 0;

    var m = new Date(options[1][0]).getMonth(),
            q = ((m-1) / 3 ) + 1,
            y = new Date(options[1][0]).getFullYear(),
            sM= (q-1)*(12/4),
            eM= q*(12/4),
            sD= new Date(y, sM, 1),
            eD= new Date(y, eM, 0);
    for(var i = 1; i < options.length; i++) {
      if(id=='BudgetProgression') {
        options[i][0] = new Date(options[i][0]);
        c += options[i][1];
        options[i][1] = c;
        options[i][2] = null;
      }
      d.push(options[i]);
    }
    d.push([sD, null,0]);
    d.push([eD, null,${activeBudget.getInitial_budget()}]);
    console.log(d);
    data.addRows(d);

    // Set chart options
    var o = {
      title: options[0][0],
      backgroundColor: 'transparent',
      width:700,
      height:300,
      hAxis: {
        minValue: sD,
        maxValue: eD,
        format: 'MMM'
      },
      vAxis: {
        maxValue: ${activeBudget.getInitial_budget()}
      },
      series: {
        0: {
          lineWidth: 5
        },
        1: {
          lineWidth: 1,
          color: '#dc3912'
        }
      },
      trendlines: {
        0: {
          type: 'linear',
          color: 'green',
          lineWidth: 0,
          opacity: 0.3,
          visibleInLegend: true,
          labelInLegend: 'Trendline',
          pointSize: 2
        }
      }
    };


    // Instantiate and draw our chart, passing in some options.
    var div = document.createElement('div');
    div.id = id;
    document.getElementById('graphs').appendChild(div);
    var chart = new google.visualization.LineChart(document.getElementById(id));
    chart.draw(data, o);
  }

  function drawNumber(options) {
    console.log(options);

    var tr = document.createElement('tr');
    tr.innerHTML = '<td>' + options[0][0] + '</td><td>' + options[1][0] + '</td>';
    document.getElementById('numbersBody').appendChild(tr);
  }
</script>

<div class="container" style="padding-bottom: 30px;">
<div class="big-paper">

  <div class="u-pull-left">
    <h3>Statistics</h3>
    <p class="company-desc">Select quarter and see the graphical representation of projects and partners.</p>
  </div>

  <div class="quarter-selector">
    <form action="/statistics" method="get">
      <select name="quarter">
        <c:forEach items="${budgets}" var="budget">
          <option value="<c:out value="${budget.getYear()}Q${budget.getQuarter()}" />"><c:out value="${budget.getYear()}Q${budget.getQuarter()}" /></option>
        </c:forEach>
      </select>
      <input type="submit" value="Go!">
    </form>
  </div>

  <div id="graphs">
  </div>
  <div id="numbers">
    <h5>Since the beginning of time</h5>
    <table class="u-full-width">
      <tbody id="numbersBody">
      </tbody>
    </table>
  </div>

</div>
</div>

</body>
</html>