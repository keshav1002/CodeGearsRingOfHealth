package com.codegears.ringofheatlh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.codegears.ringofheatlh.R.id.toolbar2;

public class SignUpActivity extends AppCompatActivity {

    EditText fullName, dob, address, gender, mobNo, emgName1, emgNum1, emgName2, emgNum2, emgName3, emgNum3;
    Button btnContinue;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(toolbar2);
        setSupportActionBar(toolbar);

        fullName = (EditText) findViewById(R.id.txtName);
        dob = (EditText) findViewById(R.id.txtDoB);
        address = (EditText) findViewById(R.id.txtAddress);
        gender = (EditText) findViewById(R.id.txtGender);
        mobNo = (EditText) findViewById(R.id.txtMobNo);
        emgName1 = (EditText) findViewById(R.id.txtEmgNameOne);
        emgNum1 = (EditText) findViewById(R.id.txtEmgNumOne);
        emgName2 = (EditText) findViewById(R.id.txtEmgNameTwo);
        emgNum2 = (EditText) findViewById(R.id.txtEmgNumTwo);
        emgName3 = (EditText) findViewById(R.id.txtEmgNameThree);
        emgNum3 = (EditText) findViewById(R.id.txtEmgNumThree);


        btnContinue = (Button) findViewById(R.id.btn_Cont);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName2 = fullName.getText().toString();
                String dob2 = dob.getText().toString();
                String address2 = address.getText().toString();
                String gender2 = gender.getText().toString();
                String mobNo2 = mobNo.getText().toString();
                String emrgName1 = emgName1.getText().toString();
                String emrgNum1 = emgNum1.getText().toString();
                String emrgName2 = emgName2.getText().toString();
                String emrgNum2 = emgNum2.getText().toString();
                String emrgName3 = emgName3.getText().toString();
                String emrgNum3 = emgNum3.getText().toString();

                if (fullName2.equals("") || dob2.equals("") || address2.equals("") || gender2.equals("") || mobNo2.equals("") ||
                        emrgName1.equals("") || emrgNum1.equals("") || emrgName2.equals("") || emrgNum2.equals("") ||
                        emrgName3.equals("") || emrgNum3.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields and try again", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                    intent.putExtra("FULL_NAME", fullName2);
                    intent.putExtra("DOB", dob2);
                    intent.putExtra("ADDRESS", address2);
                    intent.putExtra("GENDER", gender2);
                    intent.putExtra("MOBILE_NO", mobNo2);
                    intent.putExtra("EMG_NAME_ONE", emrgName1);
                    intent.putExtra("EMG_NUM_ONE", emrgNum1);
                    intent.putExtra("EMG_NAME_TWO", emrgName2);
                    intent.putExtra("EMG_NUM_TWO", emrgNum2);
                    intent.putExtra("EMG_NAME_THREE", emrgName3);
                    intent.putExtra("EMG_NUM_THREE", emrgNum3);
                    startActivity(intent);
                }

            }
        });


    }
}
