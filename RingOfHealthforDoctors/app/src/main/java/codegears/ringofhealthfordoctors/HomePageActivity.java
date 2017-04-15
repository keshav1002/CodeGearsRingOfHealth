package codegears.ringofhealthfordoctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
