
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

  var payment_details= snap.child("Payment_details").val();
  
    
  if (payment_details==null){       //Check if payment details child exisit 

   $("#page").append("<div class='card text-center  p-4' ><p>No Payment History.</p></div>");   //append no payment history card
  }else{
    

    snap.child("Payment_details").forEach(child=>{            // for each child in payment details

      
      var date= child.key;
      var pay = child.val().Paid;
      var exp_date = child.val().Expiry_date;

      //var transaction = firebase.database().ref().child("Transactions"); // Obtaining the balance from transaction table

      
      $("#page").append("<div class='card text-left mb-5 p-4'><div><dl class='row'><p><dt class='col-sm-3'>Date</dt><dd class='col-sm-9' id='date'>"+date+"</dd></p><p><dt class='col-sm-3'>Pay</dt><dd class='col-sm-9' id='pay'>GHS "+pay+"</dd></p><p><dt class='col-sm-3'>Expiry Date</dt><dd class='col-sm-9' id='exp_date' >"+exp_date+"</dd></p></dl></div></div>");  // Apppend the payment history
      
    });
    
  }

$("#spinner_accountinfo").hide();   // hiding the spinner
      $(".hidden_section").show();  // showing the account info page

});



    });
  
