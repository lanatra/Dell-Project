<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script type="text/javascript">

    google.load('search', '1');

    var imageSearch;

    function searchComplete() {

        // Check that we got results
        if (imageSearch.results && imageSearch.results.length > 0) {

            // Grab our content div, clear it.
            var contentDiv = document.getElementById('logo-results');
            contentDiv.innerHTML = '';

            // Loop through our results, printing them to the page.
            var results = imageSearch.results;
            for (var i = 0; i < results.length; i++) {
                var result = results[i];
                var newImg = document.createElement('img');

                // There is also a result.url property which has the escaped version
                newImg.src=result.tbUrl;
                $(newImg).data("full-url", result.url);

                // Put our title + image in the content
                contentDiv.appendChild(newImg);
            }
            selectImage();
            // Now add links to additional pages of search results.
            // addPaginationLinks(imageSearch);
        }
    }

    function OnLoad() {

        // Create an Image Search instance.
        imageSearch = new google.search.ImageSearch();

        // Set searchComplete as the callback function when a search is
        // complete.  The imageSearch object will have results in it.
        imageSearch.setSearchCompleteCallback(this, searchComplete, null);

        // Find me a beautiful car.
        imageSearch.execute($("#companyName").val() + " logo");

        // Include the required Google branding
        google.search.Search.getBranding('branding');
    }
    $(function() {

        $("#companyName").blur(function() {
            console.log("blur");
            OnLoad();
        })
    })

    function selectImage() {
        $("#logo-results img").click(function() {
            if($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                $("#logo-url").val("");
            } else {
                $("#logo-results img").removeClass("selected");
                $(this).addClass("selected");
                $("#logo-url").val($(this).data("full-url"));
            }
        })
    }

</script>
<h2>Create Company</h2>
<form action="/api/createCompany" method="post">
<input id="companyName" name="companyName" placeholder="Company Name">
<select id="countryCode" name="countryCode" >
    <option value="DK">Denmark</option>
    <option value="SE">Sweden</option>
    <option value="NO">Norway</option>
    <option value="DE">Germany</option>
    <option value="PL">Poland</option>
    <option value="CZ">Czech</option>
    </select>
<input id="email" name="email" placeholder="email">
<div>
<h3>Select logo</h3>
    <div  id="logo-results"></div>
    <div id="branding"  style="float: left;"></div></div><br>
    <label>Or upload your own</label>
    <input type="file" name="logo">
    <input type="hidden" name="logoUrl" id="logo-url">
</div>
    <input type="submit" value="Create Company">
</form>
</body>
</html>
