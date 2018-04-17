package com.digimasters.rev_colgh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class summary extends AppCompatActivity {
    TextView GPC,Customer_name,address,region,email,date_ob,district,mobile_no,id_type,id_no,suburb,profile_type,profiletype_no,tele,pf_day,pf_time,comment;
    String  Ghana_pc ,customer_n,property_address,property_type,Region,Email,dob,District,MmNo;
    Button submit;
    private DatabaseReference customer_details,transaction,payment_h;
    private DatabaseReference newpost;

    private ProgressDialog spinner;
    Intent s ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Getting intent on oncreate
        s=getIntent();

        //Initializing the Database References
        customer_details= FirebaseDatabase.getInstance().getReference().child("Customer_Details");
        transaction = FirebaseDatabase.getInstance().getReference().child("Transactions");
        payment_h= FirebaseDatabase.getInstance().getReference().child("Payment_History");

        //Initializing ProgressDialog
        spinner = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);


        //Mapping TxtViews
        GPC = (TextView)findViewById(R.id.sum_GPC);
        Customer_name = (TextView)findViewById(R.id.sum_customer_n);
        id_type = (TextView)findViewById(R.id.sum_id_type);
        id_no = (TextView)findViewById(R.id.sum_id_no);
        suburb = (TextView)findViewById(R.id.sum_suburb);
        profile_type = (TextView)findViewById(R.id.sum_profile_type);
        profiletype_no = (TextView)findViewById(R.id.sum_profile_no);
        tele = (TextView)findViewById(R.id.sum_tele);
        pf_day = (TextView)findViewById(R.id.sum_pf_day);
        pf_time = (TextView)findViewById(R.id.sum_pf_time);
        comment = (TextView)findViewById(R.id.sum_comments);
        address = (TextView)findViewById(R.id.sum_address);
        date_ob= (TextView)findViewById(R.id.sum_dob);
        region = (TextView)findViewById(R.id.sum_region);
        email= (TextView)findViewById(R.id.sum_email);
        district = (TextView)findViewById(R.id.sum_district);
        mobile_no = (TextView)findViewById(R.id.sum_mmno);




        //Passing the values from main activity
        Ghana_pc = s.getExtras().getString("Gpc");
        customer_n = s.getExtras().getString("customer_name");
        property_address = s.getExtras().getString("address");
        Region = s.getExtras().getString("region");
        Email = s.getExtras().getString("email");
        District = s.getExtras().getString("district");
        dob = s.getExtras().getString("date_of_birth");
        MmNo = s.getExtras().getString("mobile_no");


        //Mapping the back and submit buttons
        submit = (Button) findViewById(R.id.submit);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting intent on submit button click

                spinner.setMessage("Submitting to Database...");
                spinner.setCanceledOnTouchOutside(false);
                spinner.setCancelable(false);
                spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                spinner.show();
                //Writing to Database

                //Generating timestamp
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());

                // Customer_details Branch
                //Generating id
                long currentid = System.currentTimeMillis();

                 newpost= customer_details.push();
                newpost.child("Ghana_Post_Code").setValue(Ghana_pc);
                newpost.child("id").setValue(Long.toString(currentid));
                newpost.child("Reg_date").setValue(timeStamp);
                newpost.child("Customer_Name").setValue(customer_n);
                newpost.child("National_ID_Type").setValue(s.getExtras().getString("Nation_id_type"));
                newpost.child("National_ID_No").setValue(s.getExtras().getString("Nation_id_no"));
                newpost.child("Date_Of_Birth").setValue(dob);
                newpost.child("Address").setValue(property_address);
                newpost.child("Region").setValue(Region);
                newpost.child("District").setValue(District);
                newpost.child("Suburb").setValue(s.getExtras().getString("suburb"));
                newpost.child("Profile_type").setValue(s.getExtras().getString("profile_type"));
                if (!TextUtils.isEmpty(s.getExtras().getString("Apt_store_No"))){
                    newpost.child("Apt_Store_No").setValue(s.getExtras().getString("Apt_store_No"));
                }
                newpost.child("Mobile_Money_No").setValue(MmNo);
                newpost.child("Telephone").setValue(s.getExtras().getString("Telephone"));
                newpost.child("Email").setValue(Email);
                newpost.child("Preferred_Pickup_Day").setValue(s.getExtras().getString("pf_day"));
                newpost.child("Preferred_Pickup_Time").setValue(s.getExtras().getString("pf_time"));
                newpost.child("Comments").setValue(s.getExtras().getString("Comment"));

                //Transaction

                newpost = transaction.push();
                newpost.child("id").setValue(Long.toString(currentid));
                newpost.child("Mobile_Money_No").setValue(MmNo);
                newpost.child("Balance_in_account").setValue(0);
                newpost.child("Total_amount_paid").setValue(0);
                newpost.child("Last_pay_amount").setValue(0);
                newpost.child("Last_pay_date").setValue("n/a");
                newpost.child("Expiry_date").setValue("n/a");
                newpost.child("Status").setValue("Not Active");




                //Payment_history

                //Getting 12 months from reg_date
                //Getting Expiry Date - adding 336 days for one year
                Calendar c=new GregorianCalendar();
                c.add(Calendar.DATE, 336);
                Date d=c.getTime();
                final String reg_expiry_date = new SimpleDateFormat("MMMM-yyyy").format(d);


                newpost = payment_h.push();
                newpost.child("id").setValue(Long.toString(currentid));
                newpost.child("reg_expiry_date").setValue(reg_expiry_date);
                //DatabaseReference payment_details;

                //for loop for the next 12 months
                for (int i=0;i<=336;i+=28){

                    Calendar calendar=new GregorianCalendar();
                    calendar.add(Calendar.DATE, i);
                    long month_timestamp = calendar.getTimeInMillis();
                    Date date=calendar.getTime();
                    final String pay_month_year = new SimpleDateFormat("MMMM-yyyy").format(date);
                    newpost.child("monthly_details").child(pay_month_year).child("month_status").setValue("new");
                    newpost.child("monthly_details").child(pay_month_year).child("month_timestamp").setValue(month_timestamp);
                    newpost.child("monthly_details").child(pay_month_year).child("Total_month_pay").setValue(0.00);
                }



                spinner.dismiss();

                Toast.makeText(getApplicationContext(),"Fields Successfully Added To Database",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //Setting the TextViews
        GPC.setText(Ghana_pc);
        Customer_name.setText(customer_n);
        id_no.setText(s.getExtras().getString("Nation_id_no"));
        id_type.setText(s.getExtras().getString("Nation_id_type"));
        suburb.setText(s.getExtras().getString("suburb"));
        profile_type.setText(s.getExtras().getString("profile_type"));
        profiletype_no.setText(s.getExtras().getString("Apt_store_No"));
        tele.setText(s.getExtras().getString("Telephone"));
        pf_day.setText(s.getExtras().getString("pf_day"));
        pf_time.setText(s.getExtras().getString("pf_time"));
        comment.setText(s.getExtras().getString("Comment"));
        address.setText(property_address);
        date_ob.setText(dob);
        region.setText(Region);
        email.setText(Email);
        district.setText(District);
        mobile_no.setText(MmNo);



    }
}
