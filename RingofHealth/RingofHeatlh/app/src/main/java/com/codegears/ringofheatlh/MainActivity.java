package com.codegears.ringofheatlh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.codegears.ringofheatlh.R.id.txtBPM;

public class MainActivity extends ActionBarActivity {
    int count = 0;
    TextView txtHeartRate;
    TextView txtGreeting;
    Handler bluetoothIn;
    String userName;
    Toolbar toolbar;
    GPSTracker gps;
    String phoneNo = "0752852826";
    String phoneNo2 = "0766707194";
    String phoneNo3 = "0777071229";
    String message = "Patient is under distress. Address: No 57 Ramakrishna Road, Colombo 6";

    final int handlerState = 0;                        //is used to identify the message (from, handler)
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString;

    private ConnectedThread myConnectedThread;

    // A valid SPP UUID service
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC Address
    private static String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        img2.setImageResource(R.mipmap.ic_profile);

        txtHeartRate = (TextView) findViewById(txtBPM);
        txtGreeting = (TextView) findViewById(R.id.txtGreeting);

        userName = getIntent().getStringExtra("USER_NAME");
        txtGreeting.setText("Welcome " + userName);

        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileDetails.class);
                intent.putExtra("USER_NAME", userName);
                startActivity(intent);
            }
        });


        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                message+= address+","+city+" "+country;
                //Toast.makeText(getApplicationContext(), "Your Location is - "+address+" "+city+" "+" "+country+" ", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                try {
                    int bpm = 0;
                    if (msg.what == handlerState) {
                        String readMessage = (String) msg.obj;

                        if (!(readMessage.equals("0") || readMessage.equals("00") || readMessage.equals("000") || readMessage.equals("#"))) {
                            recDataString = new StringBuilder();
                            ImageView img = (ImageView) findViewById(R.id.imageView);
                            img.setImageResource(R.mipmap.heart_on);
                            if (readMessage.charAt(0) == '#') {
                                Log.d("yo1", readMessage);
                                recDataString.append(readMessage);
                                int start = recDataString.indexOf("#");
                                int end = recDataString.indexOf("~");
                                Log.d("yo", String.valueOf(start));
                                Log.d("yo", String.valueOf(end));
                                String dataInPrint = recDataString.substring(1, end);
                                Log.d("yo", dataInPrint);
                                bpm = Integer.parseInt(dataInPrint);
                            } else {
                                Log.d("yo1", readMessage);
                                recDataString.append(readMessage);
                                int start = recDataString.indexOf("#");
                                int end = recDataString.indexOf("~");
                                Log.d("yo", String.valueOf(start));
                                Log.d("yo", String.valueOf(end));
                                String dataInPrint = recDataString.substring(0, end);
                                Log.d("yo", dataInPrint);
                                bpm = Integer.parseInt(dataInPrint);
                            }
                            if (bpm < 40) {
                                count++;
                                if (count > 3) {
                                    Toast.makeText(getApplicationContext(), "Emergency Detected, Message Sent", Toast.LENGTH_LONG).show();
                                    SmsManager.getDefault().sendTextMessage(phoneNo, null, message, null, null);
                                    SmsManager.getDefault().sendTextMessage(phoneNo2, null, message, null, null);
                                    SmsManager.getDefault().sendTextMessage(phoneNo3, null, message, null, null);
                                    count = 0;
                                }
                            }else if(bpm > 120){
                                count++;
                                if(count>3){
                                    Toast.makeText(getApplicationContext(), "Emergency Detected, Message Sent", Toast.LENGTH_LONG).show();
                                    SmsManager.getDefault().sendTextMessage(phoneNo, null, message, null, null);
                                    SmsManager.getDefault().sendTextMessage(phoneNo2, null, message, null, null);
                                    SmsManager.getDefault().sendTextMessage(phoneNo3, null, message, null, null);
                                    count = 0;
                                }
                            }
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("Users").child(userName).child("BPM");
                            myRef.setValue(String.valueOf(bpm));
                            txtHeartRate.setText(String.valueOf(bpm));
                            recDataString.setLength(0);
                        } else {
                            ImageView img = (ImageView) findViewById(R.id.imageView);
                            img.setImageResource(R.mipmap.heart_off);
                        }

                    }

                } catch (NumberFormatException e) {

                } catch (StringIndexOutOfBoundsException e) {

                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get the Bluetooth adapter
        checkBTState(); // check bluetooth state

    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity from devicelist intent
        Intent intent = getIntent();

        //Get the MAC address from the Device List Activty from intent.getStringExtra
        address = intent.getStringExtra("device_address");

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
           } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {

            }
        }
        myConnectedThread = new ConnectedThread(btSocket);
        myConnectedThread.start();


                myConnectedThread.write("x");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {

        }
    }

    //Checks if the mobile has Bluetooth and prompts it to be turned on if it is off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            // loop to continue getting messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from the input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }
}