package com.example.bluetoothreceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.ShareCompat;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends Activity {
	
	Button capture;
	Button on;
	Button off;
	TextView capt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_client);
		
		capture = (Button)findViewById(R.id.buttonOn);
		on = (Button)findViewById(R.id.alarm_on);
		off = (Button)findViewById(R.id.alarm_off);
		capt = (TextView) findViewById(R.id.capt);
		
		
		
		capture.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
			      
		    	  Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.docs");
		    	  startActivity(launchIntent);
	            }
	        });
		
		
		
		
		
		capt.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
			      
		    	  RequestConnection reqCon = new RequestConnection(Client.this, new IConnectionResponseHandler(){

	                    @Override
	                    public void OnSuccessArray(JSONArray pResult){

	                        Log.e("Test ",  pResult.toString());
	                    }


	                    @Override
	                    public void onSuccessJSONObject(String pResult) {
	                        try {

	                            JSONObject jObject = new JSONObject(pResult);
	                            Log.e("Test ",  pResult);

	                            return;

	                        } catch (JSONException e) {
	                            e.printStackTrace();
	                        }

	                        Toast.makeText(Client.this, "Success",  Toast.LENGTH_SHORT).show();

	                    }

	                    @Override
	                    public void onFailure(String e) {
	                          //  Log.e("Test ",  e);
	                    }

	                    @Override
	                    public void onSuccessJSONArray(String pResult) {
	                        // Ignore and do nothing
	                        Log.e("Test ",  pResult);
	                    }

	                });

	                reqCon.capture();
	            }  
	        });
		
		
		
		
		
		on.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
			      
		    	  RequestConnection reqCon = new RequestConnection(Client.this, new IConnectionResponseHandler(){

	                    @Override
	                    public void OnSuccessArray(JSONArray pResult){

	                        Log.e("Test ",  pResult.toString());
	                    }


	                    @Override
	                    public void onSuccessJSONObject(String pResult) {
	                        try {

	                            JSONObject jObject = new JSONObject(pResult);
	                            Log.e("Test ",  pResult);

	                            return;

	                        } catch (JSONException e) {
	                            e.printStackTrace();
	                        }

	                        Toast.makeText(Client.this, "Success",  Toast.LENGTH_SHORT).show();

	                    }

	                    @Override
	                    public void onFailure(String e) {
	                          //  Log.e("Test ",  e);
	                    }

	                    @Override
	                    public void onSuccessJSONArray(String pResult) {
	                        // Ignore and do nothing
	                        Log.e("Test ",  pResult);
	                    }

	                });

	                reqCon.turnOnAlarm();
	            }
	        });
		
		
		
		
		
		
		off.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
			      
		    	  RequestConnection reqCon = new RequestConnection(Client.this, new IConnectionResponseHandler(){

	                    @Override
	                    public void OnSuccessArray(JSONArray pResult){

	                        Log.e("Test ",  pResult.toString());
	                    }


	                    @Override
	                    public void onSuccessJSONObject(String pResult) {
	                        try {

	                            JSONObject jObject = new JSONObject(pResult);
	                            Log.e("Test ",  pResult);

	                            return;

	                        } catch (JSONException e) {
	                            e.printStackTrace();
	                        }

	                        Toast.makeText(Client.this, "Success",  Toast.LENGTH_SHORT).show();

	                    }

	                    @Override
	                    public void onFailure(String e) {
	                          //  Log.e("Test ",  e);
	                    }

	                    @Override
	                    public void onSuccessJSONArray(String pResult) {
	                        // Ignore and do nothing
	                        Log.e("Test ",  pResult);
	                    }

	                });

	                reqCon.turnOffAlarm();
	            }
	        });
		
		
		
		

		}
	}

