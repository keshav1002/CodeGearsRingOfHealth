package com.codegears.ringofheatlh;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class DeviceListActivity extends Activity {


    TextView txtConnStatus;

    private BluetoothAdapter myBtAdapter; //Adapter to be used to find the paired devices
    private ArrayAdapter<String> myPairedDevicesArrayAdapter; //ArrayAdapter to configure the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        //Asking for the SMS permission if its not provided already
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        checkBTState();

        txtConnStatus = (TextView) findViewById(R.id.connecting);
        txtConnStatus.setTextSize(20);
        txtConnStatus.setText(" ");

        // Initialize array adapter for paired devices
        myPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(myPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        myBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = myBtAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : pairedDevices) {
                myPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            //String noDevices = getResources().getText(R.string.none_paired).toString();
            myPairedDevicesArrayAdapter.add("no devices paired");
        }
    }

    // Set up on-click listener for the list
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            txtConnStatus.setText("Connecting...");

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            String userName = getIntent().getStringExtra("USER_NAME");

            // Make an intent to start next activity while taking an extra which is the MAC address.
            Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
            intent.putExtra("device_address", address);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        }
    };

    private void checkBTState() {
        // Check if the device has bluetooth capabilities and whether it is turned on
        myBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBtAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (myBtAdapter.isEnabled()) {
                Log.d("Checking - ", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}