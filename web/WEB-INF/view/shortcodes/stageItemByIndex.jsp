<c:set var="dellAndLatest" value="${User.getCompany_id() == 1 && stages.get(stageIndex).getType() == project.getStatus()}"></c:set>
<c:set var="partnerAndLatest" value="${User.getCompany_id() != 1 && stages.get(stageIndex).getType() == project.getStatus()}"></c:set>

<div class="item<c:if test='${stages.get(stageIndex).getUser().getId() == User.getId()}'> pull-right</c:if><c:if test='${stages.get(stageIndex).getType() == project.getStatus()}'> latest-stage</c:if>">
    <span class="user-data"><c:out value="${stages.get(stageIndex).getUser().getName()}"></c:out> - <c:out value="${project.getCompanyName()}"></c:out></span>
    <span class="date isDate"><c:out value="${stages.get(stageIndex).getDate()}"></c:out></span>
    <div class="bubble">

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Project Verification'}">
            <h3><c:out value="${project.getCompanyName()}"></c:out> is requesting <c:out value="${project.getBudget()}"></c:out> for a <c:out value="${project.getType()}"></c:out></h3>
            <c:if test="${dellAndLatest}">
                <p><c:out value="${project.getBody()}"></c:out></p>
                <span class="status-message">Review the project; approve if satisfactory or reject if not.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <p><c:out value="${project.getBody()}"></c:out></p>
                <span class="status-message">Dell will now review your request.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Rejected'}">
            <h3>Project has been rejected</h3>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Add a comment to explain your decision of rejecting the project.</span>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <span class="status-message">Dell has rejected your request. Change your request to match Dell�s comment.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Approved'}">
            <h3>Project has been Approved</h3>
            <c:if test="${dellAndLatest}">
                <span class="status-message">You have now approved the project and partner is working on executing the project. A claim request will be made once executed.</span>
            </c:if>
            <c:if test="${partnerAndLatest}">
                <span class="status-message">You are now allowed to execute the project. When executed, submit a claim with proper proof of execution.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Waiting Claim Verification'}">
            <h3>Waiting claim verification</h3>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Review the claim and the attached proofs.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Dell will now review your claim request.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Claim Rejected'}">
            <h3>Claim rejected</h3>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Add a comment to explain your decision of rejecting the claim.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Dell has rejected your claim. Look in the comments for an explanation.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Project Finished'}">
            <h3>Project finished!</h3>
            <c:if test="${dellAndLatest}">
                <span class="status-message">Project is finished and now awaiting reimbursement.</span>
            </c:if><c:if test="${partnerAndLatest}">
                <span class="status-message">Claim has been approved and the project is now finished. Reimbursement is processing.</span>
            </c:if>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == 'Cancelled'}">
            <h3>Project has been cancelled</h3>
            <span class="status-message">Project has been cancelled, look in the comments for further information.</span>
        </c:if>

        <c:if test="${stages.get(stageIndex).getType() == project.getStatus() && User.getCompany_id() == 1 &&
                     (project.getStatus() == 'Waiting Project Verification' || project.getStatus() == 'Waiting Claim Verification')}">
            <form method="post" action="/api/changeProjectStatus">
                <input type="hidden" name="currentType" value="${project.getStatus()}">
                <input type="hidden" name="projectId" value="${project.getId()}">
                <button name="answer" value="approved" class="green">Approve</button>
                <button name="answer" value="denied" class="red">Reject</button>
            </form>
        </c:if>
    </div>
</div>

<!-- New bubble for next step as partner -->
<c:if test="${stages.get(stageIndex).getType() == project.getStatus() &&
                     (User.getCompany_id() != 1 && (project.getStatus() == 'Project Rejected' || project.getStatus() == 'Project Approved' || project.getStatus() == 'Claim Rejecetd'))}">
<div class="item u-full-width">
    <div class="bubble">

            <form method="post" action="/api/changeProjectStatus">
                <input type="hidden" name="currentType" value="${project.getStatus()}">
                <input type="hidden" name="projectId" value="${project.getId()}">

                <c:if test="${project.getStatus() == 'Project Rejected'}">
                    <h3>Resubmit Project</h3>
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


                    <button name="answer" value="approved" class="green">Resubmit project</button>
                </c:if><c:if test="${project.getStatus() == 'Project Approved'}">
                    <h3>Submit claim</h3>
                    <label>Upload proof of execution</label>
                    <input type="file">
                    <button name="answer" value="approved" class="green">Submit claim</button>
                </c:if><c:if test="${project.getStatus() == 'Claim Rejected'}">
                    <h3>Resubmit claim</h3>
                    <label>Upload proof of execution</label>
                    <input type="file">
                    <button name="answer" value="approved" class="green">Resubmit claim</button>
                </c:if>
            </form>
    </div>
</div>
</c:if>