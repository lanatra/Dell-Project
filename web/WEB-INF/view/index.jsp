<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <jsp:include page="header.jsp" />

<div class="error"><c:out value="${error}"></c:out></div>

  <div class="container actions" style="margin-top: 40px;">
      <c:if test="${User.getCompany_id() == 1}">
      <a href="/dashboard">
          <div class="filter">
              <div class="circle waiting <c:if test="${param.state == null}">active</c:if>"><c:out value="${statusCount[0]}" /> </div>
              <span>Waiting<br/>for action</span>
          </div>
      </a>
      <a href="?state=inExecution">
          <div class="filter">
              <div class="circle execution <c:if test="${param.state != null && param.state.equals('inExecution')}">active</c:if>"><c:out value="${statusCount[1]}" /></div>
              <span>In execution</span>
          </div>
      </a>
      </c:if>
      <c:if test="${User.getCompany_id() != 1}">
        <a href="/dashboard">
          <div class="filter">
              <div class="circle execution <c:if test="${param.state == null}">active</c:if>"><c:out value="${statusCount[0]}" /></div>
            <span>Active</span>
          </div>
        </a>
      </c:if>
    <a href="?state=finished">
      <div class="filter">
        <div class="circle finished <c:if test="${param.state != null && param.state.equals('finished')}">active</c:if>"><c:out value="${statusCount[2]}" /></div>
        <span>Finished</span>
      </div>
    </a>
    <div class="searchbox">
        <input type="text" placeholder="Search" class="search"
               <c:if test="${param.type != null}">value="<c:out value="${param.type}"></c:out>"</c:if>
               <c:if test="${param.company != null}">value="<c:out value="${param.company}"></c:out>"</c:if>
                />
    </div>

  </div>

  <div class="container" style="margin-top: 30px; padding-bottom: 30px;">
      <div class="table-head">
          <span class="id">ID</span>
          <span class="partner">Partner</span>
          <span class="type">Type</span>
          <span class="state">State</span>
          <span class="execution-date">Execution date</span>
      </div>

      <c:forEach var="project" items="${projects}">

          <a href="/project?id=<c:out value="${project.getId()}" />">
              <div class="project-item
              <c:if test="${User.getCompany_id() == 1}">
                <c:if test="${project.isUnread_admin()}">unread</c:if>
              </c:if>
              <c:if test="${User.getCompany_id() != 1}">
                <c:if test="${project.isUnread_partner()}">unread</c:if>
              </c:if>
              ">
                  <span class="id"><strong>#</strong><c:out value="${project.getId()}" /></span>
                  <span class="partner"><c:out value="${project.getCompanyName()}" /></span>
                  <span class="type"><c:out value="${project.getType()}" /></span>
                  <c:choose>
                      <c:when test="${project.getNotification() != null && ((User.getCompany_id() == 1 && project.isUnread_admin()) || (User.getCompany_id() != 1 && project.isUnread_partner()))}">
                          <span class="notification"><c:out value="${project.getNotification()}" /></span>
                      </c:when>
                      <c:otherwise>
                          <span class="state small"><c:out value="${project.getStatus()}" /></span>
                      </c:otherwise>
                  </c:choose>
                  <span class="execution-date small isShortDate"><c:out value="${project.getF_execution_date()}"></c:out> </span>
              </div>
          </a>
      </c:forEach>
  </div>
<script type="application/javascript">
   $(document).ready(function() {
       var searchNext = true;

       var removeNotMatches = function(list, q, sync) {
           var matches = [];

           var regex = new RegExp(q, "i");

           for(var i = 0; i < list.length; i++) {
               if(regex.test(list[i]))
                   matches.push(list[i])
           }

           sync(matches);
       }

        var statuses = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.whitespace,
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: ["Waiting Project Verification",
                "Project Rejected",
                "Project Approved",
                "Waiting Claim Verification",
                "Claim Rejected",
                "Project Finished",
                "Cancelled"]
        });

       var types = [];

       var typesFilter = function(q, sync, async) {
           if(q.length == 3 && searchNext) {
               return $.ajax({
                   dataType: "json",
                   url: "/getTypes",
                   data: {query: q},
                   success: function(data) {
                       types = data;
                       async(types);
                   }
               });
           } else
               return removeNotMatches(types, q, sync);
       }


       var companies = [];
       var companyFilter = function(q, sync, async) {
           if(<c:out value="${User.getCompany_id()}"></c:out> === 1) {
               if (q.length == 3 && searchNext) {
                   return $.ajax({
                       dataType: "json",
                       url: "/getCompanyNames",
                       data: {query: q},
                       success: function (data) {
                           companies = data;
                           async(companies);
                           return companies;
                       }
                   });
               } else
                   return removeNotMatches(companies, q, sync);
           }
       }

        $('.search').typeahead({
            hint: false,
            highlight: true,
            minLength: 3
                }, {
            name: 'statuses',
            source: statuses,
            limit: 4,
            templates: {
                header: '<h3 class="typeahead-header">Statuses</h3>'
            }
            }, {
            name: "type",
            source: typesFilter,
            limit: 4,
            templates: {
                header: '<h3 class="typeahead-header">Types</h3>'
            }
            }, {
            name: "companies",
            source: companyFilter,
            limit:4,
            templates: {
                header: '<h3 class="typeahead-header">Companies</h3>'
            }
        }

        ).bind("typeahead:selected", function () {
            var header = $(".searchbox .tt-cursor").parent().find("h3").text(),
            selected = $(".search").val();
            if(header == "Statuses"){
                window.location.href = "/dashboard?state=" + selected;
            }else if(header == "Types") {
                window.location.href = "/dashboard?type=" + selected;
            }else if(header == "Companies")
                window.location.href = "/dashboard?company=" + selected;
        }).keypress(function(e) {
                    if(e.keyCode == 13) { // enter
                        var val = $(".search").val();
                        if(val.substr(0, 1) == "#" && !isNaN(val.substr(1)))
                            window.location.href = "/project?id=" + val.substr(1);
                        else
                            window.location.href = "/search?q=" + val;
                    }
        }).change(function() {
                    if($(this).val().length < 3)
                    searchNext = true;
                    if($(this).val().length > 4)
                    searchNext = false;
                });

       $('span.twitter-typeahead').css('width', '100%');
       $('div.searchbox input').css('position', 'absolute !important');
       //$('div.searchbox input').css('margin-top', '-17px');

    });
</script>

  </body>
</html>