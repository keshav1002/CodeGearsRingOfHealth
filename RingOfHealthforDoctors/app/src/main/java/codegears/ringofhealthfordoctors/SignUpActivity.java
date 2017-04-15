package codegears.ringofhealthfordoctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity {

    EditText txtUsrName, txtPWord, txtName, txtPosition, txtHospitalName, txtTelNo;
    String usrName, pWord, name, position, hospName, telNo;

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtUsrName = (EditText) findViewById(R.id.txtUsrName);
        txtPWord = (EditText) findViewById(R.id.txtPWord);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPosition = (EditText) findViewById(R.id.txtPosition);
        txtHospitalName = (EditText) findViewById(R.id.txtHospName);
        txtTelNo = (EditText) findViewById(R.id.txtTelNo);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Doctors");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrName = txtUsrName.getText().toString();
                pWord = txtPWord.getText().toString();
                name = txtName.getText().toString();
                position = txtPosition.getText().toString();
                hospName = txtHospitalName.getText().toString();
                telNo = txtTelNo.getText().toString();

                final Query queryRef = myRef.child(usrName);

                if (usrName.equals("") || pWord.equals("") || name.equals("") || position.equals("") || hospName.equals("") || telNo.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                            Log.d("myTag",dataSnapshot.getValue(String.class));
                                Toast.makeText(getApplicationContext(), "Username already Exists", Toast.LENGTH_SHORT).show();
                            } else {
                                DatabaseReference newRef = myRef.child(usrName);
                                newRef.child("Name").setValue(name);
                                newRef.child("Position").setValue(position);
                                newRef.child("HospitalName").setValue(hospName);
                                newRef.child("TelNo").setValue(telNo);
                                newRef.child("password").setValue(pWord);
                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                startActivity(intent);
                                finish();
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
