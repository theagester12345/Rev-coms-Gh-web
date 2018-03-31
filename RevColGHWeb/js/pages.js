

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

//Reconsile Expired Account 
// Query on Transaction Table to find expiry_date == current date
//Obtaining current system data
var rate= 20.00;

var d = new Date();

var month = d.getMonth()+1;
var day = d.getDate();

var date =(day<10 ? '0' : '') + day  + '/' +
    (month<10 ? '0' : '') + month + '/' +
    d.getFullYear();


var transactionref = firebase.database().ref().child("Transactions");
var queryon_transaction_table = transactionref.orderByChild("Expiry_date").equalTo(date);
queryon_transaction_table.on("child_added",function(snapshot){
if (snapshot.val()!=null){
  var id = snapshot.child("id").val();
  
  var push_key=snapshot.key;

firebase.database().ref().child("/Transactions/"+push_key).update({
  Expiry_date:"n/a",
  Status:"Not Active"
});

}else{

}



});




      
//sign up agent button clicked
$("#signupbtn").click(function(){

  window.location="signupag.html";


})
//view agents database button clicked
$("#viewagbtn").click(function(){

  window.location="viewag.html";


})


// on register agent button clicked
$("#signupform").submit(function(e){

  //show Spinner hide form
  $("#spinner").show();
  $("#cardheader").hide();
  $("#signupform").hide();

//Passing to the Database table Agent Details
  var f_name = $("#fn").val();
  var l_name = $("#ln").val();
  var email = $("#email").val();
  var password = $("#password").val();
  var address = $("#address").val();
  var region = $("#region").val();
  var city = $("#city").val();
  var tel = $("#tele").val();
  var agent_details = firebase.database().ref().child("Agent_Details").push();

  //Signup agent 
  firebase.auth().createUserWithEmailAndPassword(email, password).then(function(error){
    var errorCode = error.code;

    if (errorCode==null){
  
      
      
          //parse agents info to database
    agent_details.child("fname").set(f_name);
    agent_details.child("lname").set(l_name);  
    agent_details.child("email").set(email);
    agent_details.child("password").set(password);
    agent_details.child("address").set(address);
    agent_details.child("region").set(region);
    agent_details.child("city").set(city);
    agent_details.child("Telephone").set(tel);
  
    //hide spinner and display success message
    $("#spin").hide();
  
    $("#signuperror").show().text("Successfully Registered Field Agent.");
    $("#proccedbtn").show().click(function(){
  
      window.location="agent.html";
  
    });

  }
}).catch(function(error) {



    $("#signuperror").show().text(error.message+" Refresh the Page.");

  
  });

  

  
  
return false;
});


//Displaying the Agent database
var agent_detailsRef = firebase.database().ref().child("Agent_Details");
agent_detailsRef.on("child_added",snap=>{

  
  var first_name = snap.child("fname").val();
  var last_name = snap.child("lname").val();
  var email_= snap.child("email").val();
  var  city_= snap.child("city").val();
  var tele_ = snap.child("Telephone").val();

  $("#viewagTb").append("<tr><th scope='row'><input type='checkbox'></th><td>"+first_name+"</td><td>"+last_name+"</td><td>"+email_+"</td><td>"+city_+"</td><td>"+tele_+"</td></tr>");

})

//Populating Transaction Table with Customer details
var customer_detailsRef = firebase.database().ref().child("Customer_Details");
customer_detailsRef.on("child_added",snap=>{

var customer_name = snap.child("Customer_Name").val();
var Telephone = snap.child("Mobile_Money_No").val();
var region = snap.child("Region").val();


var transaction_table_ref = firebase.database().ref().child("Transactions"); // Reference to the transaction table 
var queryon_transaction_table = transaction_table_ref.orderByChild("Mobile_Money_No").equalTo(Telephone); // query on transaction table 
queryon_transaction_table.on("child_added",data=>{

  var id_ = data.child("id").val();
  var stats = data.child("Status").val();
  $("#transactTb").append("<tr><th scope='row'><input type='checkbox'></th><td>"+id_+"</td><td>"+customer_name+"</td><td>"+Telephone+"</td><td><span class='status'>"+stats+"</span></td></tr>");
  $("#customerTb").append("<tr><th scope='row'><input type='checkbox'></th><td>"+id_+"</td><td>"+customer_name+"</td><td>"+region+"</td><td><span class='status'>"+stats+"</span></td></tr>");
  
  $("#spinner_transaction").hide();   // hiding the spinner 
  $("#transaction_section").show();   // Showing page main content
  $("#customer_section").show();      // Showing page content in customer page
});

});

//Getting values from Transaction/Customer Account table onclick
$("#transactiontable").on('click','tr',function(e){
    e.preventDefault();
    


    var $row = $(this).closest("tr"),       // Finds the closest row <tr> 
    $tds = $row.find("td");             // Finds all children <td> elements
$.each($tds, function(index) {               // Visits every single <td> element
  if (index==2){
    var tel = $(this).text();

window.location= "accountinfo.html?tel="+tel; // Redirect to account info page
  }
       
});


});




    });
  
