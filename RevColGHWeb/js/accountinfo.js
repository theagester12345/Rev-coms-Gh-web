
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

// Obtaining Specific Account Information and Details 
var getUrlParameter = function getUrlParameter(sParam) {
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

var tele = getUrlParameter("tel");






var customer_detail_ref = firebase.database().ref().child("Customer_Details");  // Reference to the Customer Details table 
var queryon_customer_details = customer_detail_ref.orderByChild("Mobile_Money_No").equalTo(tele);  // Query on customer details table where telephone matches val

queryon_customer_details.on("child_added",snap=>{
  var c_name= snap.child("Customer_Name").val();
  var mobile_money = snap.child("Mobile_Money_No").val();
   
  $("#c_name").text(c_name);  //Append the customer name to account info page
  $("#tel").text(mobile_money);       //Append the Telephone to account info page

var transactionRef = firebase.database().ref().child("Transactions"); // Reference to the Transaction Table
var queryon_transaction = transactionRef.orderByChild("Mobile_Money_No").equalTo(mobile_money);  // Query on Transaction Table 
 
queryon_transaction.on("child_added",snap=>{

  if (snap.val()!=null){

    var stat = snap.child("Status").val();
    var acc = snap.child("id").val();
  
    $("#account_id").text(acc);
    var last_pay = snap.child("Last_pay_amount").val();
    var expiry_date = snap.child("Expiry_date").val();
    var bal = snap.child("Balance_in_account").val();
    var last_pay_date = snap.child("Last_pay_date").val();
    if (stat=="Active"){
       $("#accountinfoTB").append("<tr><td class='text-center'>"+'GHs'+last_pay+"</td><td class='text-center'>"+last_pay_date+"</td><td class='text-center'>"+expiry_date+"</td><td class='text-center'>"+'GHs'+bal+"</td><td class='bg-dark text-white text-center'>"+stat+"</td></tr>");
    }else if (stat == "Not Active"){
      $("#accountinfoTB").append("<tr><td class='text-center'>"+'GHs'+last_pay+"</td><td class='text-center'>"+last_pay_date+"</td><td class='text-center'>"+expiry_date+"</td><td class='text-center'>"+'GHs'+bal+"</td><td class='bg-danger text-white text-center'>"+stat+"</td></tr>");
    }else{
      alert("Problem With Account");
    }


  }else {

  }
 


});


  $("#spinner_accountinfo").hide();   // hiding the spinner
  $(".accountinfo_section").show();  // shwoing the account info page
});  
  



    });
  
