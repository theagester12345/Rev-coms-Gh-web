

 
 /* function signin(){
      $("#prgbar").show();
      var email=$("#loginemail").val();
      var password = $("#loginpassword").val();
     // var error = document.getElementById("error");
      
 
     

      firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
         


        //$("#error").show().text(error.message);
        var error = document.getElementById("error");
        error.innerText = error.message;


       
      });

      }
*/
    $(document).ready(function (){


      



      







      // on Login Page form submit
      $("#loginform").submit(function(e){

       
        var email=$("#loginemail").val();
      var password = $("#loginpassword").val();

        

        firebase.auth().signInWithEmailAndPassword(email, password).then(function(error){
          var errorcode = error.code;

          if(errorcode==null){
            $("#prgbar").show();
            firebase.auth().onAuthStateChanged(function(user) {
              if (user) {
                // User is signed in.
                window.location ="overview.html";
              } else {
                // No user is signed in.d
                
              }
            });

          }


        }).catch(function(error) {
         


          $("#error").show().text(error.message);
          
  
  
         
        });

       
        //Prevents form from refreshing for some apparent reason
        return false;
   
      });
  
    });
  
