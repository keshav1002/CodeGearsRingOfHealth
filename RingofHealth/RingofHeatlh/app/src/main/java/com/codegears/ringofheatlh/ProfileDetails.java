package com.codegears.ringofheatlh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.codegears.ringofheatlh.R.id.toolbar2;
import static com.codegears.ringofheatlh.R.id.txtAddress;
import static com.codegears.ringofheatlh.R.id.txtDOB;
import static com.codegears.ringofheatlh.R.id.txtEmergencyContact1;
import static com.codegears.ringofheatlh.R.id.txtEmergencyContact2;
import static com.codegears.ringofheatlh.R.id.txtEmergencyContact3;
import static com.codegears.ringofheatlh.R.id.txtEmergencyNo1;
import static com.codegears.ringofheatlh.R.id.txtEmergencyNo2;
import static com.codegears.ringofheatlh.R.id.txtEmergencyNo3;
import static com.codegears.ringofheatlh.R.id.txtGender;
import static com.codegears.ringofheatlh.R.id.txtDoB;
import static com.codegears.ringofheatlh.R.id.txtTelNo;

public class ProfileDetails extends AppCompatActivity {

    TextView Name,Gender,Address,TelNo,DOB,EC1,EC2,EC3,EN1,EN2,EN3;
    Toolbar toolbar;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        toolbar = (Toolbar)findViewById(toolbar2);
        setSupportActionBar(toolbar);

        Name = (TextView) findViewById(txtDoB);
        Gender = (TextView) findViewById(txtGender);
        Address = (TextView) findViewById(txtAddress);
        TelNo = (TextView) findViewById(txtTelNo);
        DOB = (TextView) findViewById(txtDOB);
        EC1 = (TextView) findViewById(txtEmergencyContact1);
        EC2 = (TextView) findViewById(txtEmergencyContact2);
        EC3 = (TextView) findViewById(txtEmergencyContact3);
        EN1 = (TextView) findViewById(txtEmergencyNo1);
        EN2 = (TextView) findViewById(txtEmergencyNo2);
        EN3 = (TextView) findViewById(txtEmergencyNo3);

        userName = getIntent().getStringExtra("USER_NAME");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(userName);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);


                String name = map.get("Name");
                String gender = map.get("Gender");
                String address = map.get("Address");
                String telNo = map.get("TelNo");
                String DoB = map.get("DoB");

                String emergencyContact1 = map.get("emergencyContact1");
                String emergencyContact2 = map.get("emergencyContact2");
                String emergencyContact3 = map.get("emergencyContact3");
                String emergencyNo1 = map.get("emergencyNo1");
                String emergencyNo2 = map.get("emergencyNo2");
                String emergencyNo3 = map.get("emergencyNo3");

                Name.setText(name);
                Gender.setText(gender);
                Address.setText(address);
                TelNo.setText(telNo);
                DOB.setText(DoB);

                EC1.setText(emergencyContact1);
                EC2.setText(emergencyContact2);
                EC3.setText(emergencyContact3);
                EN1.setText(emergencyNo1);
                EN2.setText(emergencyNo2);
                EN3.setText(emergencyNo3);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("myTag", "Failed to read value.", error.toException());
            }
        });

    }
        // THIS CODE is for the popup menu
//    @Override
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//
//        inflater.inflate(R.menu.menu_main, menu);
//
//        return true;
//
//    }
}
