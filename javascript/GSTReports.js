
function loadOrderProductsContent(orderId) {
  showSpinner();
  console.log("In viewing Order's Products specific to order ID: ", orderId);
  $.ajax({
    url: "displayProcessedOrderProducts",
    method: 'GET',
    data: { orderId: orderId },
    success: function (response) {
      hideSpinner();
      $('#orderproducts-' + orderId).html(response);
    },
    error: function (xhr, status, error) {
      console.log('AJAX Error: ' + error);
      hideSpinner(); // Add this line to hide the spinner in case of an error
    }
  });
}

  
