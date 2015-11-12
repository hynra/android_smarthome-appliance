package com.example.bluetoothreceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Start extends Activity {
	
	TextView client;
	TextView appliance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		client = (TextView)findViewById(R.id.client);
		appliance = (TextView)findViewById(R.id.appliance);
		
		client.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  Intent i = new Intent(Start.this, Client.class);
					startActivity(i); 
	            }
	        });
		
		appliance.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  Intent i = new Intent(Start.this, Main.class);
					startActivity(i); 
	            }
	        });
	}

}
