package com.digimasters_revcol.core;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> sms_list = new ArrayList<String>();
    ListView sms_listview;
    ArrayAdapter arrayadpt;
    DatabaseReference transaction_table, payment_histroty_table,T_report_table ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Referencing the transaction table, payment_history and T_report table
        transaction_table = FirebaseDatabase.getInstance().getReference().child("Transactions");
        payment_histroty_table = FirebaseDatabase.getInstance().getReference().child("Payment_History");
        T_report_table = FirebaseDatabase.getInstance().getReference().child("T_Report");



        //Handling the Sms list View
        sms_listview = (ListView) findViewById(R.id.smslist);
        arrayadpt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sms_list);
        sms_listview.setAdapter(arrayadpt);
        sms_listview.setOnItemClickListener(this);
        refresh_smsinbox ();
    }

    private void refresh_smsinbox() {

        ContentResolver contentResolver = getContentResolver();
        Cursor sms_cursor = contentResolver.query(Uri.parse("content://sms/inbox"),null,"address='MobileMoney'",null, null);
        int index_body = sms_cursor.getColumnIndex("body");
        int index_address = sms_cursor.getColumnIndex("address");

        //  String raw_date_string = sms_cursor.getString(raw_date);


        // Long timestamp = Long.parseLong(raw_date);
        // Date date = new Date (raw_date);
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        //String date_in_string =format.format(date) ;

        if (index_body < 0 || !sms_cursor.moveToFirst())
            return;
        arrayadpt.clear();
        do {
            Long raw_date= Long.valueOf(sms_cursor.getString(sms_cursor.getColumnIndex("date")));
            Date date = new Date (raw_date);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            String date_in_string =format.format(date);

            String string = "SMS From: "+ sms_cursor.getString(index_address)+"\n"+"Date: " + date_in_string+ "\n\n"+ sms_cursor.getString(index_body) + "\n";
            arrayadpt.add(string);
            arrayadpt.notifyDataSetChanged();

        }while(sms_cursor.moveToNext());


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {

            String[] smsmessages = sms_list.get(position).split("\n");
            String address = smsmessages[0];
            String date = smsmessages[1];
            String smsmessage = "";

            for (int i=2; i < smsmessages.length;i++){
                smsmessage += smsmessages[i];
            }

            String smsMessage_ = address + "\n"+date+"\n";
            smsMessage_+=smsmessage;

            final TextView test = (TextView) findViewById(R.id.test);
            final String[] sms_data = smsmessage.split(" ");

            //Finding the reference value in the sms body
            int index = -1;

            for (int i = 0;i<sms_data.length;i++){
                if (sms_data[i].equals("Reference:")){
                    index = i;
                    break;
                }

            }
          test.setText(sms_data[index+1]);

            String pay_from_sms = sms_data[5];
            final double final_pay_from_sms = Double.parseDouble(pay_from_sms);

            //Getting date of transaction
            final String pay_date = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());  //date of payment
            final String pay_month_year = new SimpleDateFormat("MMMM-yyyy").format(new java.util.Date());   // month-year date
            final String pay_year = new SimpleDateFormat("yyyy").format(new java.util.Date());     //year of payment



            //Getting Expiry Date - adding 30 days
            Calendar c=new GregorianCalendar();
            c.add(Calendar.DATE, 30);
            Date d=c.getTime();
            final String expiry_date = new SimpleDateFormat("dd/MM/yyyy").format(d);




            //Transaction Table
            //Accounting
            Query queryon_transaction_table = transaction_table.orderByChild("Ref").equalTo(sms_data[index+1]);
            queryon_transaction_table.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot transaction : dataSnapshot.getChildren()) {
                            String push_key= dataSnapshot.getChildren().iterator().next().getKey();
                            String path = "/"+dataSnapshot.getKey()+"/"+push_key;




                            // Getting data used to reconsile account
                            double rate = 20.00;
                            String lpd = transaction.child("Last_pay_date").getValue(String.class);
                            String ed = transaction.child("Expiry_date").getValue(String.class);
                            double bal = transaction.child("Balance_in_account").getValue(double.class);
                            double total_amount_paid = transaction.child("Total_amount_paid").getValue(double.class);
                            //test.setText(lpd);
                            String status="Active";
                            //
                            HashMap<String,Object> result = new HashMap<>();


                            //Scenario
                            //Accounting

                            //Section at other paye periods not first pay
                            if (!TextUtils.equals("n/a",lpd) && !TextUtils.equals("n/a",ed) ) {
                                double total_accu3 = total_amount_paid + final_pay_from_sms;
                                result.put("Last_pay_amount",final_pay_from_sms);
                                result.put("Last_pay_date",pay_date);
                                result.put("Expiry_date",expiry_date);
                                double d_balance_before_expire = bal + final_pay_from_sms;
                                result.put("Balance_in_account",d_balance_before_expire);
                                result.put("Status","Active");
                                result.put("Total_amount_paid",total_accu3);
                                FirebaseDatabase.getInstance().getReference().child(path).updateChildren(result);
                            } else {
                                //Section at First pay

                                if (final_pay_from_sms >= rate) {
                                    double balance_at_first_pay = final_pay_from_sms - rate;
                                    double total_accu1 = total_amount_paid + final_pay_from_sms;

                                    result.put("Last_pay_amount",final_pay_from_sms);
                                    result.put("Last_pay_date",pay_date);
                                    result.put("Expiry_date",expiry_date);
                                    result.put("Balance_in_account",balance_at_first_pay);
                                    result.put("Status",status);
                                    result.put("Total_amount_paid",total_accu1);
                                    FirebaseDatabase.getInstance().getReference().child(path).updateChildren(result);

                                } else if (final_pay_from_sms < rate) {
                                    double total_accu2= total_amount_paid + final_pay_from_sms;
                                    result.put("Last_pay_amount",final_pay_from_sms);
                                    result.put("Last_pay_date",pay_date);
                                    result.put("Expiry_date",expiry_date);
                                    double d_balance_at_first_pay = bal + final_pay_from_sms;
                                    result.put("Balance_in_account",d_balance_at_first_pay);
                                    result.put("Status",status);
                                    result.put("Total_amount_paid",total_accu2);
                                    FirebaseDatabase.getInstance().getReference().child(path).updateChildren(result);

                                }

                            }

                        }

                        }
                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Payment_history
            // Accounting
            Query queryon_paymenth = payment_histroty_table.orderByChild("Ref").equalTo(sms_data[index+1]);
            queryon_paymenth.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot paymentH : dataSnapshot.getChildren() ){
                            //Path Constructions
                            String push_key= dataSnapshot.getChildren().iterator().next().getKey();
                           String monthly_paid_path = "/"+dataSnapshot.getKey()+"/"+push_key+"/monthly_details/"+pay_month_year;
                            String payment_details_path = "/"+dataSnapshot.getKey()+"/"+push_key+"/Payment_details/"+pay_date;

                            //
                            Double owe_rate = -20.00;
                            String month_status = paymentH.child("monthly_details").child(pay_month_year).child("month_status").getValue(String.class);
                            Double total_month_pay = paymentH.child("monthly_details").child(pay_month_year).child("Total_month_pay").getValue(Double.class);
                            Double paid = paymentH.child("Payment_details").child(pay_date).child("Paid").getValue(Double.class);
                            HashMap<String,Object> result ;

                            //Write to payment_details child
                            if (paid==null){
                           result= new HashMap<>();
                            result.put("Paid",final_pay_from_sms);
                            result.put("Expiry_date",expiry_date);
                            FirebaseDatabase.getInstance().getReference().child(payment_details_path).updateChildren(result);
                            }else {
                                result= new HashMap<>();
                                result.put("Paid",paid+final_pay_from_sms);
                                result.put("Expiry_date",expiry_date);
                                FirebaseDatabase.getInstance().getReference().child(payment_details_path).updateChildren(result);
                            }


                            //Write to monthly_paid
                            if (TextUtils.equals("new",month_status)){
                                result= new HashMap<>();
                                result.put("Total_month_pay",final_pay_from_sms);
                                result.put("month_status","old");
                                result.put("Total_month_owe",owe_rate);
                                FirebaseDatabase.getInstance().getReference().child(monthly_paid_path).updateChildren(result);


                            }else {
                                result= new HashMap<>();
                                result.put("Total_month_pay",total_month_pay+final_pay_from_sms);
                                FirebaseDatabase.getInstance().getReference().child(monthly_paid_path).updateChildren(result);


                            }


                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            T_report_table.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){



                            //Path Construction
                            String t_report_path = "/"+dataSnapshot.getKey();

                            //
                           // double realtime_pay = trans_report


                        double realtime_pay = dataSnapshot.child("Realtime_pay").getValue(double.class);
                       
                            HashMap<String,Object> result = new HashMap<>();


                            result.put("Realtime_pay",realtime_pay+final_pay_from_sms);
                            FirebaseDatabase.getInstance().getReference().child(t_report_path).updateChildren(result);


                            //Add date of First transaction to T_report table
                            if ((dataSnapshot.child("First_trans_date").getValue(String.class)==null)){
                                T_report_table.child("First_trans_date").setValue(pay_date);
                            }
                            










                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            Toast.makeText(this,"Processed Transaction Stored",Toast.LENGTH_LONG).show();


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
