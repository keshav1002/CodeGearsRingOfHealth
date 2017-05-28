package codegears.ringofhealthfordoctors;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PatientListActivity extends AppCompatActivity {

    String userName;
    List<String> patientList = new ArrayList<String>();
    ;

    private ListView patientListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        userName = getIntent().getStringExtra("USER_NAME");

        patientListView = (ListView) findViewById(R.id.patientListView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientList);

        patientListView.setAdapter(arrayAdapter);

        Log.d("myTag23", userName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientList.clear();

                GenericTypeIndicator<Map<String, Map<String, String>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Map<String, String>>>() {
                };
                Map<String, Map<String, String>> map = dataSnapshot.getValue(genericTypeIndicator);

                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Map<String, String> map2 = (Map<String, String>) pair.getValue();
                    Iterator it2 = map2.entrySet().iterator();
                    while (it2.hasNext()) {
                        Map.Entry pair2 = (Map.Entry) it2.next();
                        if (pair2.getValue().equals(userName)) {
                            patientList.add((String) pair.getKey());
                        }
                    }

//                    Log.d("myTag", pair.getKey() + " = " + pair.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }

                arrayAdapter.notifyDataSetChanged();


                patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < patientList.size(); i++) {
                            if (position == i) {
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                intent.putExtra("USER_NAME",patientList.get(i));
                                intent.putExtra("DOCTOR_USER_NAME",userName);
                                startActivity(intent);
                            }
                        }
                    }
                });

                Log.d("myTag2", Arrays.toString(patientList.toArray()));
                Log.d("myTag", "Data : " + map.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Log.d("myTag2", Arrays.toString(myList.toArray()));
    }

    public void deleteProfile(){
        AlertDialog.Builder alert = new AlertDialog.Builder(PatientListActivity.this);
        alert.setTitle("Delete Profile");
        alert.setMessage("Are you sure you want to delete the profile?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Doctors").child(userName);
                myRef.setValue(null);
                dialog.dismiss();
                Intent intent = new Intent(PatientListActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(PatientListActivity.this,EditActivity.class);
                intent.putExtra("DOCTOR_USER_NAME",userName);
                startActivity(intent);
                return true;
            case R.id.delete_profile:
                deleteProfile();
                return true;
            case R.id.logout:
                Intent intent2 = new Intent(PatientListActivity.this,HomePageActivity.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
