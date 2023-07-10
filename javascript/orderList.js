$('#dateRangeFilter').change(function() {
	console.log("error here");
  applyFilters();
});

/*function showSpinner() {
  var spinnerContainer = document.getElementById('spinner-container');
  spinnerContainer.style.display = 'flex';
}

function hideSpinner() {
  var spinnerContainer = document.getElementById('spinner-container');
  spinnerContainer.style.display = 'none';
}*/


var tokenElement = document.getElementById("tokenHolder");
var token = tokenElement.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token: " + token);


var pageElement = document.getElementById("pageHolder");
var page = pageElement.innerText;
// Use the "page" variable in your JavaScript code
console.log("page: " + page);

$(document).on('click', '#red-button', function(event) {
	    event.preventDefault();
		var orderId = $(this).data('order-id'); 
	    
	    console.log('Clicked button with order ID:', orderId);
	   processOrder(orderId,token,page);
	   });
	 
	 function processOrder(orderId,token,page){
	    toastr.options = {
		  "closeButton": false,
		  "debug": false,
		  "newestOnTop": false,
		  "progressBar": false,
		  "positionClass": "toast-bottom-right",
		  "preventDuplicates": false,
		  "onclick": null,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "5000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}; 
	 //showSpinner();
	 	 $.ajax({ 	 
		      url: "processOrders",
		      method: 'GET',
		      data: { orderId: orderId,
		    	  adminId:1,
		    	  token:token,
		    	  page:page
		    	 },
		      success: function(response) {
		     // //hideSpinner();
		    	  $('#content').html(response);
		    	  toastr.success('Order has been processed');
		      },
		      error: function(xhr, status, error) {
		        console.log('AJAX Error: ' + error);
		      }
		    });
	 	}
	 
	 




function applyFilters() {
  var dateRangeFilter = $('#dateRangeFilter').val();

  $('.order-row').each(function() {
    var orderRow = $(this);
    var orderDate = orderRow.find('td:eq(3)').text();
    //var processedStatus = orderRow.find('td:eq(12) button').text();

    var showRow = true;

    if (dateRangeFilter !== 'All') {
      var currentDate = new Date();
      var orderDateObj = new Date(orderDate);

      if (dateRangeFilter === 'daily' && !isSameDate(currentDate, orderDateObj)) {
        showRow = false;
      } else if (dateRangeFilter === 'weekly' && !isSameWeek(currentDate, orderDateObj)) {
        showRow = false;
      } else if (dateRangeFilter === 'monthly' && !isSameMonth(currentDate, orderDateObj)) {
        showRow = false;
      }
    }

    if (showRow) {
      orderRow.show();
    } else {
      orderRow.hide();
    }
  });
}

function isSameDate(date1, date2) {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getDate() === date2.getDate()
  );
}

function isSameWeek(date1, date2) {
  var oneDay = 24 * 60 * 60 * 1000;
  var firstDayOfWeek = new Date(date1.getFullYear(), date1.getMonth(), date1.getDate() - date1.getDay());
  var firstDayOfNextWeek = new Date(firstDayOfWeek.getTime() + 7 * oneDay);
  return date2 >= firstDayOfWeek && date2 < firstDayOfNextWeek;
}

function isSameMonth(date1, date2) {
  return date1.getFullYear() === date2.getFullYear() && date1.getMonth() === date2.getMonth();
}

function processedStatusFilter() {
  var processedStatusFilter = $('#processedStatusFilter').val();
	console.log('im here in status filtering');
  if (processedStatusFilter === 'unprocessed') {
    getUnprocessedOrders();
  } else if (processedStatusFilter === 'processed') {
    getProcessedOnlyOrders();
  } else {
    getAllOrders();
  }
}


function getAllOrders() {
//showSpinner();
  // Send an AJAX request to retrieve unprocessed orders
  $.ajax({
    url: 'listOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}

function getUnprocessedOrders() {
//showSpinner();
  // Send an AJAX request to retrieve unprocessed orders
  $.ajax({
    url: 'unprocessedOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}



function getProcessedOnlyOrders() {
//showSpinner();
  // Send an AJAX request to retrieve processed orders
  $.ajax({
    url: 'processedOnlyOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
      //hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}

var tokenElement = document.getElementById("tokenHolder");
var token = tokenElement.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token: " + token);


var pageElement = document.getElementById("pageHolder");
var page = pageElement.innerText;
// Use the "page" variable in your JavaScript code
console.log("page: " + page);



function navigateToPage(page) {
//showSpinner();
  var nextPage = parseInt(page) + 1;
  nextPage -= 1;
  console.log('in navigation next', nextPage);
  /*window.location.href = "/listOrders?page=" + nextPage;*/

  $.ajax({
    url: "listOrdersForPagination",
    method: 'GET',
    data: { nextPage: nextPage,
    token:token },
    success: function(response) {
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}


