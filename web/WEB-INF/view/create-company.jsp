<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script type="text/javascript">

    google.load('search', '1');

    var imageSearch;

    function addPaginationLinks() {

        // To paginate search results, use the cursor function.
        var cursor = imageSearch.cursor;
        var curPage = cursor.currentPageIndex; // check what page the app is on
        var pagesDiv = document.createElement('div');
        for (var i = 0; i < cursor.pages.length; i++) {
            var page = cursor.pages[i];
            if (curPage == i) {

                // If we are on the current page, then don't make a link.
                var label = document.createTextNode(' ' + page.label + ' ');
                pagesDiv.appendChild(label);
            } else {

                // Create links to other pages using gotoPage() on the searcher.
                var link = document.createElement('a');
                link.href="/image-search/v1/javascript:imageSearch.gotoPage("+i+');';
                link.innerHTML = page.label;
                link.style.marginRight = '2px';
                pagesDiv.appendChild(link);
            }
        }

        var contentDiv = document.getElementById('content');
        contentDiv.appendChild(pagesDiv);
    }

    function searchComplete() {

        // Check that we got results
        if (imageSearch.results && imageSearch.results.length > 0) {

            // Grab our content div, clear it.
            var contentDiv = document.getElementById('content');
            contentDiv.innerHTML = '';

            // Loop through our results, printing them to the page.
            var results = imageSearch.results;
            for (var i = 0; i < results.length; i++) {
                // For each result write it's title and image to the screen
                var result = results[i];
                var imgContainer = document.createElement('div');
                var title = document.createElement('div');

                // We use titleNoFormatting so that no HTML tags are left in the
                // title
                title.innerHTML = result.titleNoFormatting;
                var newImg = document.createElement('img');

                // There is also a result.url property which has the escaped version
                newImg.src=result.tbUrl;
                imgContainer.appendChild(title);
                imgContainer.appendChild(newImg);

                // Put our title + image in the content
                contentDiv.appendChild(imgContainer);
            }

            // Now add links to additional pages of search results.
            addPaginationLinks(imageSearch);
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

</script>

<input id="companyName" name="companyName" >


</body>
</html>
