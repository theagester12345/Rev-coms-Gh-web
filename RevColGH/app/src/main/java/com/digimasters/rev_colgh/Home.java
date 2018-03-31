package com.digimasters.rev_colgh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener {
    private CardView newentry,sms,report,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Mapping Cardviews
        newentry = (CardView) findViewById(R.id.new_entry);
        report = (CardView) findViewById(R.id.Report);
        logout = (CardView) findViewById(R.id.exist);

        //Add Onclick listener to Cardviews
        newentry.setOnClickListener(this);
        report.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()){
            case R.id.new_entry : i = new Intent(this,MainActivity.class);
            startActivity(i);
            break;
            case R.id.Report : i = new Intent(this,report.class);
            startActivity(i);
            break;
            case R.id.exist :
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent (this,Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();

        }

    }
}
