package com.digimasters.rev_colgh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText username,password;
    private Button login;
    private FirebaseAuth fbauth;
  //  private DatabaseReference mdatabase;
    private ProgressDialog spinner;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mapping the username,password and login button
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        //Initialization of Firebase Auth
        fbauth = FirebaseAuth.getInstance();

        //Initializing the Progress Dialog
        spinner = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);



        //Checking login status
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(Login.this,Home.class));
                }
            }
        };


        
        
        //Setting Onclick listener on login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checklogin();
            }
        });
    }

    private void checklogin() {

        String user_name = username.getText().toString().trim();
        String password_ = password.getText().toString().trim();

        if(!TextUtils.isEmpty(user_name)&& !TextUtils.isEmpty(password_)){
            spinner.setMessage("Logging In...");
            spinner.setCanceledOnTouchOutside(false);
            spinner.setCancelable(false);
            spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            spinner.show();
            fbauth.signInWithEmailAndPassword(user_name,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        spinner.dismiss();
                        // Open the main activity
                        Intent main = new Intent(Login.this,Home.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main);


                    }else {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(),"Login Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Please Provide Email and Password",Toast.LENGTH_SHORT).show();

        }
    }

    /*private void checkuserExist() {
        final String user_id = fbauth.getCurrentUser().getUid();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){
                    Intent CollectionAct = new Intent(Login.this,MainActivity.class);
                    CollectionAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(CollectionAct);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        fbauth.addAuthStateListener(authStateListener);
    }
}
