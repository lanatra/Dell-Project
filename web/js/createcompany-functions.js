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