<%@ page import="Domain.User" %>
<%@ page import="Domain.DisplayProject" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>

<c:set var="stageIndex" value="0"></c:set>
<c:set var="messageIndex" value="0"></c:set>

<c:set var="lastStage" value="false"></c:set>

<c:forEach items="${stages}" var="stage" varStatus="ite">
    <c:if test="${stage.getType() == 'Waiting Project Verification'}">
        <c:set var="lastWaitingProjectVerification" value="${ite.index}"></c:set>
    </c:if>
</c:forEach>
<c:if test="${project.getMessage() != null}"><c:out value="${project.getMessage()}"></c:out></c:if>

<div class="container project-container">
    <div class="u-pull-right" style="margin-bottom: 20px;">
        <c:if test="${(project.getStatus() != 'Project Finished')}">
            <c:if test="${(project.getStatus() != 'Cancelled')}">
                <div class="cancel-box"><a href="#" class="cancel-button jensabox-trigger">Cancel Project</a></div>
            </c:if>
        </c:if>
        <div class="project-state"><c:out value="${project.getStatus()}" /></div>
        <span class="state">State</span>
    </div>
    <h1><span style="color: #9F9F9F;">#<c:out value="${project.getId()}" /></span> <c:out value="${project.getType()}" /></h1>

    <div class="project-items">
    <c:if test="${(stages.size() + messages.size()) > 0}">
        <c:forEach var="i" begin="0" end="${stages.size() + messages.size() - 1}">
            <c:if test="${stages.size() - 1 == stageIndex}">
                <c:set var="lastStage" value="true"></c:set>
            </c:if>
            <c:choose>
                <c:when test="${stageIndex == -1}">
                    <%@ include file="shortcodes/messageItemByIndex.jsp" %>
                    <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
                </c:when>
                <c:when test="${messageIndex == -1}">
                    <%@ include file="shortcodes/stageItemByIndex.jsp" %>
                    <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
                </c:when>
                <c:otherwise>
                    <c:if test="${stages.size() > 0 && messages.size() > 0}">
                    <c:choose>
                        <c:when test="${stages.get(stageIndex).getDate() < messages.get(messageIndex).getCreation_date_millis()}">
                            <%@ include file="shortcodes/stageItemByIndex.jsp" %>
                            <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
                            <c:if test="${stageIndex == stages.size()}">
                                <c:set var="stageIndex" value="-1"></c:set>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <%@ include file="shortcodes/messageItemByIndex.jsp" %>
                            <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
                            <c:if test="${messageIndex == messages.size()}">
                                <c:set var="messageIndex" value="-1"></c:set>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    </c:if>
                    <c:if test="${messages == null || messages.size() == 0}">
                        <%@ include file="shortcodes/stageItemByIndex.jsp" %>
                        <c:set var="stageIndex" value="${stageIndex + 1}"></c:set>
                    </c:if><c:if test="${stages == null || stages.size() == 0}">
                        <%@ include file="shortcodes/messageItemByIndex.jsp" %>
                        <c:set var="messageIndex" value="${messageIndex + 1}"></c:set>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
        <c:set var="stageIndex" value="${stages.size() - 1}"></c:set>

    <c:if test="${User.getCompany_id() != 1 && (project.getStatus() == 'Project Rejected' || project.getStatus() == 'Project Approved' || project.getStatus() == 'Claim Rejected')}">
        <div class="item pull-right u-full-width">
            <div class="bubble">

                <c:if test="${project.getStatus() == 'Project Approved' || project.getStatus() == 'Claim Rejected'}">
                    <div class="inner-bubble">
                        <h3>Proof Of Execution</h3>
                        <p class="instructions">Upload your images and documents, one by one.</p>
                        <c:forEach items="${poes}" var="poe" varStatus="ite" >
                            <c:if test="${poe.getF_deletion_date() == 0}">
                                <div class="proof-container <c:if test="${poe.getF_date() > stages.get(stageIndex - 1).getDate()}"> new</c:if>">
                                    <c:choose>
                                        <c:when test="${poe.getFiletype() == 'jpg' || poe.getFiletype() == 'png' || poe.getFiletype() == 'jpeg' || poe.getFiletype() == 'gif' || poe.getFiletype() == 'bmp'}">
                                            <div class="proof" style="background-image: url(/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>)">
                                                <a class="fancybox" rel="<c:out value='${poe.getProj_id()}'></c:out>" href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>"><div class="view-image"></div></a>
                                                <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="proof
                                            <c:choose>
                                                <c:when test="${poe.getFiletype() == 'xlsx' || poe.getFiletype() == 'xls' || poe.getFiletype() == 'numbers' || poe.getFiletype() == 'xml'}">
                                                    excel
                                                </c:when>
                                                <c:when test="${poe.getFiletype() == 'zip' || poe.getFiletype() == 'rar' || poe.getFiletype() == 'tar' || poe.getFiletype() == 'dmg'}">
                                                    archive
                                                </c:when>
                                                <c:when test="${poe.getFiletype() == 'mp3' || poe.getFiletype() == 'flac' || poe.getFiletype() == 'm4a' || poe.getFiletype() == 'wav' || poe.getFiletype() == 'flv' || poe.getFiletype() == 'mov' || poe.getFiletype() == 'mp4' || poe.getFiletype() == 'mpeg' || poe.getFiletype() == 'avi' || poe.getFiletype() == 'mkv'}">
                                                    media
                                                </c:when>
                                                <c:otherwise>
                                                    document
                                                </c:otherwise>
                                            </c:choose>
                                            ">
                                                <div class="icon-space"></div>
                                                <div class="download-file"><a href="/resources/<c:out value='${poe.getProj_id()}'></c:out>/<c:out value='${poe.getFilename()}'></c:out>?download=true">Download</a></div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <span class="filename"><c:out value='${poe.getFilename()}'></c:out></span>
                                    <form action="/api/deleteFile" method="post" class="delete-files">
                                        <input type="hidden" name="fileId" value="<c:out value='${poe.getId()}'></c:out>">
                                        <input type="hidden" name="deleteFile" value="<c:out value='${poe.getF_date() > stages.get(stageIndex - 1).getDate() ? "true" : "false"}'></c:out>">
                                        <input type="hidden" name="projectId" value="<c:out value='${project.getId()}'></c:out>">
                                        <input type="hidden" name="fileName" value="<c:out value='${poe.getFilename()}'></c:out>">
                                        <input type="submit" value="" class="delete-icon">
                                    </form>
                                </div>
                            </c:if>
                        </c:forEach>
                        <div class="new-image">
                            <form action="/uploadFile" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="proj_id" value="<c:out value='${project.getId()}'></c:out>">
                                <input type="file" name="file">
                                <input class="button" type="submit" name="submit" value="Upload">
                            </form>
                        </div>
                    </div>
                </c:if>

                <form method="post" action="/api/changeProjectStatus">
                    <input type="hidden" name="currentType" value="${project.getStatus()}">
                    <input type="hidden" name="projectId" value="${project.getId()}">

                    <c:if test="${project.getStatus() == 'Project Rejected'}">
                    <div class="inner-bubble project">
                        <h3>Resubmit Project</h3>
                        <p>The project has been rejected, look in the comments for a reason. Change the values and try again.</p>

                        <span><c:out value="${User.getCompany().getName()}" /> is requesting</span>
                        <div class="amount-box">
                            <span class="small-label">Euro is binding currency</span>
                            <input class="amount" name="budget" type="text" placeholder="Amount"/>
                            <span class="euro-label">&#8364</span>
                        </div>
                        <span>for a</span>
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
                    </div>
                    <div class="stage-actions" style="margin-top: 20px;">
                        <button name="answer" value="resubmit" class="blue">Resubmit project</button>
                    </div>
                    </c:if><c:if test="${project.getStatus() == 'Project Approved'}">
                    <p class="status-message">When the project is finished, upload images and documents as a proof of execution.</p>
                    <div class="stage-actions">
                        <button name="answer" value="approved" class="blue">Send</button>
                    </div>

                </c:if><c:if test="${project.getStatus() == 'Claim Rejected'}">
                    <p class="status-message">Resubmit claim, upload new proof of execution.</p>
                    <div class="stage-actions">
                        <button name="answer" value="approved" class="blue">Resubmit claim</button>
                    </div>
                </c:if>
                </form>
            </div>
        </div>
    </c:if>
    </div>


    <form>
        <input type="hidden" name="userId" id="userId" value="${User.getId()}" />
        <input type="hidden" name="projectId" id="projectId" value="${project.getId()}" />
        <input type="hidden" name="companyId" id="companyId" value="${User.getCompany_id()}" />
        <textarea name="body" id="message" placeholder="Write your message"></textarea>
        <button id="submitMessage" class="submit">Send message</button>
    </form>

</div>

<div class="jensabox">
    <div class="fill-box">
    <div class="content-box">
        <h2>Do you really want to cancel this project?</h2>
        <p>This change will be irreversible.</p>
        <form class="u-pull-left" method="post" action="/api/changeProjectStatus">
            <input type="hidden" name="currentType" value="${project.getStatus()}">
            <input type="hidden" name="projectId" value="${project.getId()}">
            <button class="button button-red" type="submit" name="answer" value="cancelled">Cancel Project</button>
        </form>
        <a href="#" class="button button-cancel">No</a>
    </div>
    </div>
</div>

<script>
    $(".fancybox").fancybox();

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
<script type="text/javascript" src="js/project-functions.js"></script>

</body>
</html>
