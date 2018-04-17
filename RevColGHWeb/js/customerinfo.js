
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



//Obtaining Customer Details from Database

var customer_detailsRef = firebase.database().ref().child("Customer_Details"); //Reference to the Customer details table
var queryon_customer_details = customer_detailsRef.orderByChild("Mobile_Money_No").equalTo(tele);  //Query on Customer details table
queryon_customer_details.on("child_added",snap=>{
  if (snap.val()!=null){
    var customer_name = snap.child("Customer_Name").val();
    var address= snap.child("Address").val();
    var comments= snap.child("comments").val();
    var dob= snap.child("Date_Of_Birth").val();
    var district= snap.child("District").val();
    var email = snap.child("Email").val();
    var ghana_post_code= snap.child("Ghana_Post_Code").val();
    //var Mmno= snap.child("Mobile_Money_No").val();
    var national_id= snap.child("Nation_ID_Type").val();
    var national_id_no= snap.child("National_ID_No").val();
    var ppd= snap.child("Preferred_Pickup_Day").val();
    var ppt= snap.child("Preferred_Pickup_Time").val();
    var profile_type= snap.child("Profile_type").val();
    var reg_date= snap.child("Reg_date").val();
    var region= snap.child("Region").val();
    var sub= snap.child("Suburb").val();
    var tel= snap.child("Telephone").val();

    //Setting date to customer info page

    $("#name").text(customer_name);
    $("#email").text(email);
    $("#dob").text(dob);
    $("#nid").text(national_id);
    $("#nid_no").text(national_id_no);
    $("#Mmno").text(tele);
    $("#tel").text(tel);
    $("#gpa").text(ghana_post_code);
    $("#address").text(address);
    $("#region").text(region);
    $("#district").text(district);
    $("#sub").text(sub);
    $("#apt_no").text("null");
    $("#pt").text(profile_type);
    $("#ppd").text(ppd);
    $("#ppt").text(ppt);
    $("#reg_date").text(reg_date);

    $("#spinner_customerinfo").hide();  // hide spinner when data is present
    $(".hidden_section").show();  // showing main contents












  }else{
    alert("Problem Occured");
  }
});






  



    });
  
