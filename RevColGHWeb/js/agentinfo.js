
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



//Obtaining Agent details from Database

var agent_detailsRef = firebase.database().ref().child("Agent_Details"); //Reference to the Customer details table
var queryon_agent_details = agent_detailsRef.orderByChild("Telephone").equalTo(tele);  //Query on Customer details table
queryon_agent_details.on("child_added",snap=>{
  if (snap.val()!=null){

    //Setting date to agent info page
    var first_name= snap.child("fname").val();
    $("#name").text(first_name+" "+last_name);
    var last_name= snap.child("lname").val();
    var email= snap.child("email").val();
    $("#email").text(email);
    var pass= snap.child("password").val();
    var region= snap.child("region").val();
    $("#region").text(region);
    var city= snap.child("city").val();
    $("#city").text(city);
    var address= snap.child("address").val();
    $("#address").text(address);
    var district = snap.child("District").val();
    $("#district").text(district);
    $("#tel").text(tele);

    

    $("#spinner_agentinfo").hide();  // hiding spinner on agents page

    $(".hidden_section").show();     // Showing main content on data present



  
   
  }else{
    alert("Problem Occured");
  }
 

});






  



    });
  
