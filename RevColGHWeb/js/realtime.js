
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




// Target Chart 

google.charts.load('current', {'packages':['bar']});    // Google Charts call 
google.charts.setOnLoadCallback(drawChart);

function drawChart(){

  var t_report = firebase.database().ref().child("T_Report");   //Reference to the T_report table
  t_report.on("value",snap=>{
  var pay = snap.child("Realtime_pay").val();
  var date = snap.child("First_trans_date").val();  //date of first transaction 
  var percent = ((pay/20000)*100).toFixed(0);  //Percent to 0 decimal place
  
  $("#total_s").text("GHS"+pay) //Set total sales 

 
 
  $("#prog").empty().append("<div class='progress-bar1' data-percent='"+percent+"'  data-duration='1000' data-color='#ccc,yellow'></div>"); // Append progress bar
  $(".progress-bar1").loading();    // Load Progress Bar


  // $("#prog").append("<div class='progress-bar1' data-percent='"+percent+"' data-duration='1000' data-color='#ccc,yellow'></div>");
 // $("#prog").data("percent",percent).empty();
  var data = google.visualization.arrayToDataTable([   // Data for chart
    ['Start Date', 'Sales'],
    ['From '+date, pay]
  
  ]);

  var options = {
    chart: {
      title: 'DigiMasters Performance',
      subtitle: 'Sales for:',
    },
    bars: 'horizontal' // Required for Material Bar Charts.
  };

  var chart = new google.charts.Bar(document.getElementById('barchat'));

  chart.draw(data, google.charts.Bar.convertOptions(options));

  
  });

  
}








    });
  
