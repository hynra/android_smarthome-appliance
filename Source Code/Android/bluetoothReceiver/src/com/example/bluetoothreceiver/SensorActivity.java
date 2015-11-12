package com.example.bluetoothreceiver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;




public class SensorActivity extends Activity implements SurfaceHolder.Callback, ConnectionCallbacks,
OnConnectionFailedListener {
	
	ImageView thumb;
	Handler bluetoothIn;
	String sensor0;
	final int handlerState = 0;        				 //used to identify handler message
	SurfaceView surface;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private StringBuilder recDataString = new StringBuilder();
	
	int readBufferPosition;
	volatile boolean stopWorker;
	
	private ConnectedThread mConnectedThread;
	    
	  // SPP UUID service - this should work for most devices
	private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	  
	  // String for MAC address
	private static String address;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	Thread workerThread;
    byte[] readBuffer;
    InputStream mmInputStream;
    Camera camera;
    public boolean states = false;
    TextView myLabel;
    SurfaceHolder surfaceHolder;
    
    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;
    MediaPlayer mPlayer;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;
    Button send;

    String TAG = "Appliance Side";
    String fileName;
    

	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.layout_sensor);
	  
	    //Link the buttons and textViews to respective views             
	    myLabel = (TextView)findViewById(R.id.state);
	    thumb = (ImageView)findViewById(R.id.thumb);
	    surface = (SurfaceView)findViewById(R.id.surfaceView);
	    
	    surfaceHolder = surface.getHolder();
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    mPlayer = MediaPlayer.create(getApplicationContext(),R.raw.siren);
	    mPlayer.setLooping(true);
	    
	    jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream outStream = null;
				try {
					fileName = String.format("/sdcard/DCIM/%d.jpg", System.currentTimeMillis());
					outStream = new FileOutputStream(fileName);
					outStream.write(data);
					outStream.close();
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();
				
				refreshCamera();
				
				File imagefile = new File(fileName);
	    		  FileInputStream fis = null;
	    		  try {
	    		      fis = new FileInputStream(imagefile);
	    		      } catch (FileNotFoundException e) {
	    		      e.printStackTrace();
	    		  }

	    		  mBitmapToSave = BitmapFactory.decodeStream(fis);
	    		  saveFileToDrive();
			}
		};
	    
	    bluetoothIn = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            if (msg.what == handlerState) {								
	            	String readMessage = (String) msg.obj;
	                recDataString.append(readMessage.toString());      	
            		if(readMessage.toString().contains("l")){
            			try {
            				captureImage();
            				mConnectedThread.write("d");
            				myLabel.setText("");
            				
            			} catch (IOException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            		
            		if(readMessage.toString().contains("r")){
            			if(mPlayer.isPlaying() == false){
							mPlayer.start();
						}
            		}
            		
            		else if(readMessage.toString().contains("3")){
            			if(mPlayer.isPlaying() == true){
            				mPlayer.pause();
							mConnectedThread.write("4");
							
						}
            		}
            		
	                int endOfLineIndex = recDataString.indexOf("+");                   
	                if (endOfLineIndex > 0) {                                         
	                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    
	                    if (recDataString.charAt(0) == '#')								
	                    {
	                    	sensor0 = recDataString.substring(1, endOfLineIndex);                   	
	                    	myLabel.setText(sensor0);
	                    }
	                    recDataString.delete(0, endOfLineIndex);
	                    dataInPrint = " ";
	                }            
	            }
	        }
	        
	        
	    };
	      
	    btAdapter = BluetoothAdapter.getDefaultAdapter();  
	    checkBTState();	
	  }
	
	
	  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	      
	      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
	      //creates secure outgoing connecetion with BT device using UUID
	  }
	  
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	          Bundle extras = data.getExtras();
	          Bitmap imageBitmap = (Bitmap) extras.get("data");
	          thumb.setImageBitmap(imageBitmap);
	      }
	      
	      else if(requestCode == 100){
	    	  if (resultCode == Activity.RESULT_OK) {
                  // Store the image data as a bitmap for writing later.
	    		  
	    		  File imagefile = new File(fileName);
	    		  FileInputStream fis = null;
	    		  try {
	    		      fis = new FileInputStream(imagefile);
	    		      } catch (FileNotFoundException e) {
	    		      e.printStackTrace();
	    		  }

	    		  mBitmapToSave = BitmapFactory.decodeStream(fis);
	    		  saveFileToDrive();
	    		  
                 
              }
	      }
	  }
	  
	  @Override
	  public void onResume() {
	    super.onResume();
	    
	    //Get MAC address from DeviceListActivity via intent
	    Intent intent = getIntent();
	    
	    //Get the MAC address from the DeviceListActivty via EXTRA
	    address = intent.getStringExtra(Main.EXTRA_DEVICE_ADDRESS);

	    //create device and set the MAC address
	    BluetoothDevice device = btAdapter.getRemoteDevice(address);
	     
	    try {
	        btSocket = createBluetoothSocket(device);
	        
	    } catch (IOException e) {
	    	Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
	    }  
	    // Establish the Bluetooth socket connection.
	    try 
	    {
	      btSocket.connect();
	    } catch (IOException e) {
	      try 
	      {
	        btSocket.close();
	      } catch (IOException e2) 
	      {
	    	//insert code to deal with this 
	      }
	    } 
	    mConnectedThread = new ConnectedThread(btSocket);
	    mConnectedThread.start();
	    
	    //I send a character when resuming.beginning transmission to check device is connected
	    //If it is not an exception will be thrown in the write method and finish() will be called
	    mConnectedThread.write("x");
	    
	    if (mGoogleApiClient == null) {
    		// Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
    	}
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
	    
	    
	    
	    
	  }
	
	  
	  
	  @Override
	  public void onPause() 
	  {
		 if (mGoogleApiClient != null) {
			 mGoogleApiClient.disconnect();
	     }
		  
	    super.onPause();
	    try
	    {
	    //Don't leave Bluetooth sockets open when leaving activity
	      btSocket.close();
	    } catch (IOException e2) {
	    	//insert code to deal with this 
	    }
	  }

	  
	  
	  
	  //Checks that the Android device Bluetooth is available and prompts to be turned on if off 
	  private void checkBTState() {
	 
	    if(btAdapter==null) { 
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
	            } catch (IOException e) { }
	      
	            mmInStream = tmpIn;
	            mmOutStream = tmpOut;
	        }
	        
	      
	        public void run() {
	            byte[] buffer = new byte[256];  
	            int bytes; 
	 
	            // Keep looping to listen for received messages
	            while (true) {
	                try {
	                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
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
	  
	  
	  
	  
	  
		public void captureImage() throws IOException {
			//take the picture
		//	camera.takePicture(null, null, jpegCallback);
			
			camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}

		public void refreshCamera() {
			if (surfaceHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				camera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here
			// start preview with new settings
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
			} catch (Exception e) {

			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// Now that the size is known, set up the camera parameters and begin
			// the preview.
			refreshCamera();
		}

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				// open the camera
				camera = Camera.open();
				camera.setDisplayOrientation(90);
			} catch (RuntimeException e) {
				// check for exceptions
				System.err.println(e);
				return;
			}
			Camera.Parameters param;
			param = camera.getParameters();

			// modify parameter
			param.setPreviewSize(352, 288);
			camera.setParameters(param);
			try {
				// The Surface has been created, now tell the camera where to draw
				// the preview.
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
			} catch (Exception e) {
				// check for exceptions
				System.err.println(e);
				return;
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// stop preview and release camera
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	  
	  
	  
	  
	  //---------------------------- CODE FOR GDRIVE API --------------------------------//
		
	    private void saveFileToDrive() {
	        // Start by creating a new contents, and setting a callback.
	        Log.i(TAG, "Creating new contents.");
	        final Bitmap image = mBitmapToSave;
	        Drive.DriveApi.newDriveContents(mGoogleApiClient)
	                .setResultCallback(new ResultCallback<DriveContentsResult>() {

	            @Override
	            public void onResult(DriveContentsResult result) {
	                // If the operation was not successful, we cannot do anything
	                // and must
	                // fail.
	                if (!result.getStatus().isSuccess()) {
	                    Log.i(TAG, "Failed to create new contents.");
	                    return;
	                }
	                // Otherwise, we can write our data to the new contents.
	                Log.i(TAG, "New contents created.");
	                // Get an output stream for the contents.
	                OutputStream outputStream = result.getDriveContents().getOutputStream();
	                // Write the bitmap data from it.
	                ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
	                image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
	                try {
	                    outputStream.write(bitmapStream.toByteArray());
	                } catch (IOException e1) {
	                    Log.i(TAG, "Unable to write file contents.");
	                }
	                // Create the initial metadata - MIME type and title.
	                // Note that the user will be able to change the title later.
	                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
	                        .setMimeType("image/jpeg").setTitle("Android Photo.png").build();
	                Drive.DriveApi.getRootFolder(mGoogleApiClient)
	                        .createFile(mGoogleApiClient, metadataChangeSet, result.getDriveContents())
	                        .setResultCallback(fileCallback);
	            }
	        });
	    }
	    
	    
	    
	    final private ResultCallback<DriveFileResult> fileCallback = new
	            ResultCallback<DriveFileResult>() {
	        @Override
	        public void onResult(DriveFileResult result) {
	            if (!result.getStatus().isSuccess()) {
	                Log.i(TAG, "Error while trying to save the file");
	                Toast.makeText(getApplicationContext(), "Error while trying to create the file", Toast.LENGTH_LONG).show();
	                return;
	            }
	            Log.i(TAG, "Created a file with content: " + result.getDriveFile().getDriveId());
	            Toast.makeText(getApplicationContext(), "Success save file", Toast.LENGTH_LONG).show();
	        }
	    };
	    
	    
	    
	    @Override
	    public void onConnectionFailed(ConnectionResult result) {
	        // Called whenever the API client fails to connect.
	        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
	        if (!result.hasResolution()) {
	            // show the localized error dialog.
	            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
	            return;
	        }
	        // The failure has a resolution. Resolve it.
	        // Called typically when the app is not yet authorized, and an
	        // authorization
	        // dialog is displayed to the user.
	        try {
	            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
	        } catch (SendIntentException e) {
	            Log.e(TAG, "Exception while starting resolution activity", e);
	        }
	    }
	    
	    
	    @Override
	    public void onConnected(Bundle connectionHint) {
	        Log.i(TAG, "API client connected.");
	        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
	    }

	    @Override
	    public void onConnectionSuspended(int cause) {
	        Log.i(TAG, "GoogleApiClient connection suspended");
	    }
}
