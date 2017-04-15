package codegears.ringofhealthfordoctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    Toolbar toolbar;
    boolean user = false;
    String pass;
    String username2;
    String password2;
    EditText username;
    EditText password;
    Button btnLogin;

    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = (EditText) findViewById(R.id.txtUserName);
        password = (EditText) findViewById(R.id.txtPassword);

        btnLogin = (Button) findViewById(R.id.btnSignIn);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Doctors");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username2 = username.getText().toString();
                password2 = password.getText().toString();

                if (username2.equals("") || password2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields and try again", Toast.LENGTH_SHORT).show();
                } else {

                    final Query queryRef = myRef.child(username2);

                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                };
                                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                                pass = map.get("password");
                                if (password2.equals(pass)) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignInActivity.this, PatientListActivity.class);
                                    intent.putExtra("USER_NAME", username2);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Password, Try again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid Username, Try again", Toast.LENGTH_SHORT).show();
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
