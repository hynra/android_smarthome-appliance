package com.example.bluetoothreceiver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {
	
	
    // Debugging for LOGCAT
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    
  
    // declare button for launching website and textview for connection status
    Button tlbutton;
    
    TextView textView1;
    Button scan;
    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		checkBTState();

    	textView1 = (TextView) findViewById(R.id.connecting);
    	textView1.setTextSize(40);
    	textView1.setText(" ");
    	scan = (Button) findViewById(R.id.scan);
    	scan.setOnClickListener(btnScanDeviceOnClickListener);
    	mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
    	
    	
	}
	
	
    private Button.OnClickListener btnScanDeviceOnClickListener
    = new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// Initialize array adapter for paired devices
	    	
			mPairedDevicesArrayAdapter.clear();
	    	// Find and set up the ListView for paired devices
	    	ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
	    	pairedListView.setAdapter(mPairedDevicesArrayAdapter);
	    	pairedListView.setOnItemClickListener(mDeviceClickListener);

	    	// Get the local Bluetooth adapter
	    	mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	    	// Get a set of currently paired devices and append to 'pairedDevices'
	    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

	    	// Add previosuly paired devices to the array
	    	if (pairedDevices.size() > 0) {
	    		// findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
	    		for (BluetoothDevice device : pairedDevices) {
	    			mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	    		}
	    	} else {
	    		String noDevices = "No devices";
	    		mPairedDevicesArrayAdapter.add(noDevices);
	    	}
			
		}};
		
		
		
		
	    private Button.OnClickListener btnClient
	    = new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// Initialize array adapter for paired devices
		    	
				Intent i = new Intent(Main.this, Client.class);
				startActivity(i); 
				
			}};
	
	
	
    // Set up on-click listener for the list (nicked this - unsure)
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity while taking an extra which is the MAC address.
			Intent i = new Intent(Main.this, SensorActivity.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
			startActivity(i);   
        }
    };
    
    
    
    
    
    
    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
    	 mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) { 
        	Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
          if (mBtAdapter.isEnabled()) {
            Log.d(TAG, "...Bluetooth ON...");
          } else {
            //Prompt user to turn on Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
 
            }
          }
        }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
