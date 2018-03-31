package com.digimasters.rev_colgh;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
EditText GPC,c_name,address,email,mobile_no,dob,id_no,profiletype_no,tele,comment;
Spinner region,district,suburb,pf_day,pf_time,profile_type,id_type;
private ArrayAdapter<CharSequence> adapter;

Button next;
String Ghana_pc ,customer_n,Address,Email,MmNo,final_dob;
Calendar mycalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mapping Spinners
        region = (Spinner)findViewById(R.id.region);
        district =(Spinner)findViewById(R.id.district);
        profile_type = (Spinner)findViewById(R.id.profile_type);
        pf_day = (Spinner)findViewById(R.id.pickup_day);
        pf_time = (Spinner)findViewById(R.id.pickup_time);
        suburb = (Spinner)findViewById(R.id.suburb);
        id_type = (Spinner)findViewById(R.id.id_type);





        //Setting apt/store No edittext readonly at oncreate
        final EditText apt_no= (EditText)findViewById(R.id.apt_store_no);
        apt_no.setEnabled(false);

        //Enable edittext on the Apartment selection
       //Setting on item select listener on the profile type spinner
       profile_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (position==1){
                   apt_no.setEnabled(true);
               }else{
                   apt_no.setEnabled(false);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        //Setting on item selected listener on region spinner
        //To populate the district spinner
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.A_district,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==1){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.BA_districts,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==2){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.GA_districts,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                }else if (position==3){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.C_districts,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);

                }else if (position==4){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.E_district,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==5){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.N_districts,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                }else if (position==6){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.W_district,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==7){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.UE_districts,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==8) {
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.UW_districts, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                } else if (position==9) {
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.V_districts, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Setting on item selected listener on district spinner
        // To populate the suburb spinner
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (district.getSelectedItem().equals("Adansi North")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.adansi_north_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);

                } else if (district.getSelectedItem().equals("Afigya-Kwabre")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.afigya_kwabre_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Asunafo North Municipal")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.asunafo_north_muni_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Asunafo South")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.asunafo_south_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("GA Central")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.GA_central_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("Accra Metropolitan")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Accra_Metro_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Ashaiman Municipal")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Ashiaman_Muni_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Abura/Asebu/Kwamankese")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.abura_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Agona East")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.agona_east_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("Jirapa")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.jirapa_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Lambussie Karni")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lambussie_karni_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Bawku Municipal")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.bawku_muni_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Bolgatanga Municipal")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.Bolga_muni_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("Bunkpurugu-Yunyo")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.bunkp_yunyoo_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }   else if (district.getSelectedItem().equals("Central Gonja")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.central_gonja_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("Akuapim South")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.akuapim_south_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }  else if (district.getSelectedItem().equals("Akuapim North")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.akuapim_north_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Ahanta West")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.ahanta_west_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Aowin/Suaman")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.aowin_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Adaklu")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.adaklu_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                } else if (district.getSelectedItem().equals("Afadjato South")){
                    adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.afadjato_suburbs, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    suburb.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Initializing the calendar
        mycalendar = Calendar.getInstance();


        //Date Time picker Dialog
         dob = (EditText) findViewById(R.id.dob);



       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
              final_dob = updatelabel();
            }
        };

        //setting onclick on edittext
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(MainActivity.this,date,mycalendar.get(Calendar.YEAR),mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        Context c =this;


        //Onclick listener for next button
        next = (Button)findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting the date of birth in string


                //Mapping Edittext fields
                GPC = (EditText) findViewById(R.id.GPC);
                c_name= (EditText) findViewById(R.id.c_name);
                address = (EditText) findViewById(R.id.address);
                email = (EditText) findViewById(R.id.email);
                mobile_no = (EditText) findViewById(R.id.Mmno);
                dob = (EditText) findViewById(R.id.dob);
                id_no =(EditText) findViewById(R.id.n_id);
                profiletype_no = (EditText) findViewById(R.id.apt_store_no);
                tele = (EditText) findViewById(R.id.tele2);
                comment = (EditText) findViewById(R.id.comment);

                //Getting Strings from EditText Fields
                Ghana_pc = GPC.getText().toString();
                customer_n= c_name.getText().toString();
                Address= address.getText().toString();
                Email = email.getText().toString();
                MmNo = mobile_no.getText().toString();



                if (TextUtils.isEmpty(Ghana_pc)||TextUtils.isEmpty(customer_n)|| TextUtils.isEmpty(Address)||TextUtils.isEmpty(Email)){

                    Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_LONG).show();
                }else {
                    Intent s = new Intent(v.getContext(), summary.class);
                    s.putExtra("Gpc", Ghana_pc);
                    s.putExtra("customer_name", customer_n);
                    s.putExtra("Nation_id_type", id_type.getSelectedItem().toString());
                    s.putExtra("Nation_id_no", id_no.getText().toString());
                    s.putExtra("address", Address);
                    s.putExtra("region",region.getSelectedItem().toString() );
                    s.putExtra("district", district.getSelectedItem().toString());
                    s.putExtra("suburb", suburb.getSelectedItem().toString());
                    s.putExtra("profile_type", profile_type.getSelectedItem().toString());
                    s.putExtra("Apt_store_No", profiletype_no.getText().toString());
                    s.putExtra("Telephone", tele.getText().toString());
                    s.putExtra("pf_day", pf_day.getSelectedItem().toString());
                    s.putExtra("pf_time", pf_time.getSelectedItem().toString());
                    s.putExtra("Comment", comment.getText().toString());
                    s.putExtra("email", Email);
                    s.putExtra("date_of_birth",dob.getText().toString());
                    s.putExtra("mobile_no", MmNo);

                    startActivity(s);
                    finish();
                }

            }
        });














    }

    private String updatelabel() {
        dob = (EditText) findViewById(R.id.dob);
        //Formatting date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        String final_date = dateFormat.format(mycalendar.getTime());
        dob.setText(final_date);
        return final_date;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
