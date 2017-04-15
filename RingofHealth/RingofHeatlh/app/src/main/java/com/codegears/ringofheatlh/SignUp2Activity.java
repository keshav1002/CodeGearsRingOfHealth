package com.codegears.ringofheatlh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.codegears.ringofheatlh.R.id.toolbar2;

public class SignUp2Activity extends AppCompatActivity {

    EditText txtUsrName;
    EditText txtPWord;
    EditText txtDocId;

    Button btnSubmit;

    Toolbar toolbar;

    boolean usrExists;
    boolean docExists;

    String fullName, dob, address, gender, mobileNo, emgNameOne, emgNumOne, emgNameTwo, emgNumTwo, emgNameThree, emgNumThree;
    String usrName;
    String pWord;
    String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        toolbar = (Toolbar) findViewById(toolbar2);
        setSupportActionBar(toolbar);

        fullName = getIntent().getStringExtra("FULL_NAME");
        dob = getIntent().getStringExtra("DOB");
        address = getIntent().getStringExtra("ADDRESS");
        gender = getIntent().getStringExtra("GENDER");
        mobileNo = getIntent().getStringExtra("MOBILE_NO");
        emgNameOne = getIntent().getStringExtra("EMG_NAME_ONE");
        emgNumOne = getIntent().getStringExtra("EMG_NUM_ONE");
        emgNameTwo = getIntent().getStringExtra("EMG_NAME_TWO");
        emgNumTwo = getIntent().getStringExtra("EMG_NUM_TWO");
        emgNameThree = getIntent().getStringExtra("EMG_NAME_THREE");
        emgNumThree = getIntent().getStringExtra("EMG_NUM_THREE");


        txtUsrName = (EditText) findViewById(R.id.txtUsrName);
        txtPWord = (EditText) findViewById(R.id.txtPWord);
        txtDocId = (EditText) findViewById(R.id.txtDocId);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users");
        final DatabaseReference myRef2 = database.getReference().child("Doctors");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrName = txtUsrName.getText().toString();
                pWord = txtPWord.getText().toString();
                docId = txtDocId.getText().toString();

                if (usrName.equals("") || pWord.equals("") || docId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d("Name", usrName);
                    Log.d("pWord", pWord);
                    Log.d("docId", docId);

                    final Query queryRef = myRef.child(usrName);

                    usrExists = false;
                    docExists = false;

                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                            Log.d("myTag",dataSnapshot.getValue(String.class));
                                usrExists = true;
                                Toast.makeText(getApplicationContext(), "Username already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    final Query queryRef2 = myRef2.child(docId);

                    queryRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
//                            Log.d("myTag",dataSnapshot.getValue(String.class));
                                docExists = true;
                                Toast.makeText(getApplicationContext(), "Doctor doesn't Exist", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!usrExists) {
                                    DatabaseReference newRef = myRef.child(usrName);
                                    newRef.child("Name").setValue(fullName);
                                    newRef.child("DoB").setValue(dob);
                                    newRef.child("Address").setValue(address);
                                    newRef.child("Gender").setValue(gender);
                                    newRef.child("TelNo").setValue(mobileNo);
                                    newRef.child("emergencyContact1").setValue(emgNameOne);
                                    newRef.child("emergencyNo1").setValue(emgNumOne);
                                    newRef.child("emergencyContact2").setValue(emgNameTwo);
                                    newRef.child("emergencyNo2").setValue(emgNumTwo);
                                    newRef.child("emergencyContact3").setValue(emgNameThree);
                                    newRef.child("emergencyNo3").setValue(emgNumThree);
                                    newRef.child("doctorId").setValue(docId);
                                    newRef.child("password").setValue(pWord);
                                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp2Activity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

    }
}
