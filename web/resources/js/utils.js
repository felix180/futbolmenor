// This function is specific to a particular ID,
// but the rest of the functions are generic and reusable.
// Experienced JavaScript programmers could just
// make an anonymous function (closure) on the main page that
// captures the id and calls showIndicatorRegion.

function showWorkingIndicator(data) {
  showIndicatorRegion(data, "workingIndicator");
}

// Show an HTML element, fire off the Ajax request,
// then hide the element once the response comes back.
// In real life I would probably use jQuery and
// $("#workingIndicator").hide() and ...show()
// But I wanted to keep this as simple as possible
// for folks that don't know jQuery.

function showIndicatorRegion(data, regionId) {
  if (data.status == "begin") {
    showElement(regionId);
  } else if (data.status == "success") {
    hideElement(regionId);
  } 
}

function showElement(id) {
  document.getElementById(id).style.display 
    = "inline";
}

function hideElement(id) {
  document.getElementById(id).style.display 
    = "none";
}

