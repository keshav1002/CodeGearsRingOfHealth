package codegears.ringofhealthfordoctors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
}
