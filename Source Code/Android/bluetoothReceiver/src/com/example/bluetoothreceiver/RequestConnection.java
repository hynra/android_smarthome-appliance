package com.example.bluetoothreceiver;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class RequestConnection extends ConnectionHandler {

    protected static AsyncHttpClient mClient = new AsyncHttpClient();
    
    private String TAG_CAPTURE 	= "[Capture] Capturing";


    public RequestConnection(Context context, IConnectionResponseHandler handler) {
        this.mContext = context;
        this.responseHandler = handler;
    }

    @Override
    public String getAbsoluteUrl(String relativeUrl) {
        return mContext.getResources().getString(R.string.http_address) + relativeUrl;
    }





    public void capture(){
    	
    	mClient.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        RequestParams params = new RequestParams();

        params.put("param", "capture");

        System.setProperty("http.keepAlive", "false");
        post("api.php", params, new JsonHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG_CAPTURE, "Sending request");
                dialog = ProgressDialog.show(mContext, "Connecting", "Capturing ...", true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.i(TAG_CAPTURE, "Success");
                responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                Log.e(TAG_CAPTURE, "Failed");
                responseHandler.onFailure(responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG_CAPTURE, "Disconnected");
                dialog.dismiss();
            }

        }, mClient);
    }

    
    
    
    
    
    
    public void turnOnAlarm(){
    	mClient.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        RequestParams params = new RequestParams();

        params.put("param", "alarm_on");

        System.setProperty("http.keepAlive", "false");
        post("api.php", params, new JsonHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG_CAPTURE, "Sending request");
                dialog = ProgressDialog.show(mContext, "Connecting", "Capturing ...", true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.i(TAG_CAPTURE, "Success");
                responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                Log.e(TAG_CAPTURE, "Failed");
                responseHandler.onFailure(responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG_CAPTURE, "Disconnected");
                dialog.dismiss();
            }

        }, mClient);
    }
    
    
    
    public void turnOffAlarm(){
    	mClient.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        RequestParams params = new RequestParams();

        params.put("param", "alarm_off");

        System.setProperty("http.keepAlive", "false");
        post("api.php", params, new JsonHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG_CAPTURE, "Sending request");
                dialog = ProgressDialog.show(mContext, "Connecting", "Capturing ...", true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.i(TAG_CAPTURE, "Success");
                responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                Log.e(TAG_CAPTURE, "Failed");
                responseHandler.onFailure(responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG_CAPTURE, "Disconnected");
                dialog.dismiss();
            }

        }, mClient);
    }
    

}
