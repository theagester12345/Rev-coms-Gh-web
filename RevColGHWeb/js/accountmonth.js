
    $(document).ready(function (){

      //if user not logged on 
//redirect to the index page
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
     //Logout link clicked
     function signout(){
      firebase.auth().signOut().then(function() {
        window.location="index.html";
      }.catch(function(error) {
        window.alert("Signout Error");
      }) );
    }

  } else {
    // No user is signed in.
    window.location="index.html";
  }
}); 


//Obtain Payment History of Account

var getUrlParameter = function getUrlParameter(sParam) {                //Capture id from url
  var sPageURL = decodeURIComponent(window.location.search.substring(1)),
      sURLVariables = sPageURL.split('&'),
      sParameterName,
      i;

  for (i = 0; i < sURLVariables.length; i++) {
      sParameterName = sURLVariables[i].split('=');

      if (sParameterName[0] === sParam) {
          return sParameterName[1] === undefined ? true : sParameterName[1];
      }
  }
};

var id = getUrlParameter("id");





var paymenh = firebase.database().ref().child("Payment_History");  //Reference to payment history table
var queyon_paymenth = paymenh.orderByChild("id").equalTo(id);     // Query on payment history table
queyon_paymenth.on("child_added",snap=>{

  

  snap.child("monthly_details").forEach(child=>{    // loop through each child in monthly details
var status = child.val().month_status;
var month = child.key;
var paid = child.val().Total_month_pay;
var owe = child.val().Total_month_owe;
var m_timestamp =child.val().month_timestamp;
var current_date = new Date().getTime();

    if (current_date>m_timestamp){     //obtain months prior to current date 
      if (status=="new"){             //obtain months with no trnasaction 

        $("#page").append("<div class='card  mb-5 p-3'><div><p><h3>"+month+"</h3></p><p class='text-center p-3'>No Transaction </p></div></div>");         //append no transaction in month 

      }else {

        var transaction = firebase.database().ref().child("Transactions");   //Refernece to the Transaction table
  var queryon_trans = transaction.orderByChild("id").equalTo(id);     // Query on the transaction table
  queryon_trans.on("child_added",snap=>{
    var bal = snap.child("Balance_in_account").val();      //Obtain balance and expiry dates
    var exp_date = snap.child("Expiry_date").val();
    $("#page").append("<div class='card text-left mb-5 p-3'><div><p><h3>"+month+"</h3></p><dl class='row'><p><dt class='col-sm-3'>Paid</dt><dd class='col-sm-9' >GHS "+paid+"</dd></p><p><dt class='col-sm-3'>Owe</dt><dd class='col-sm-9' >GHS "+owe+"</dd></p><p><dt class='col-sm-3'>Balance In Account</dt><dd class='col-sm-9' >GHS "+bal+"</dd></p><p><dt class='col-sm-3'>Expiry Date</dt><dd class='col-sm-9' >"+exp_date+"</dd></p></dl></div></div>");    //append the monthly account transaction card to page

  });
      }

    }
    $("#spinner_accountinfo").hide();   // hiding the spinner
      $(".hidden_section").show();  // showing the account info page

  });

});



    });
  
