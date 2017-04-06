package com.vroneinc.vrone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

public class MainActivity extends AuthBaseActivity {
    // Code for BT permission
    //private final static int REQUEST_ENABLE_BT = 87;

    private Context mContext;
    private Resources mResources;


    // a bluetooth “socket” to a bluetooth device
    /*private BluetoothSocket mmSocket = null;
    // input/output “streams” with which we can read and write to device
    // use of “static” important, it means variables can be accessed
    // without an object, this is useful as other activities can use
    // these streams to communicate after they have been opened.
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    // indicates if we are connected to a device
    private boolean Connected = false;*/

    private static final String TAG = "MainActivity";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int REQUEST_ENABLE_CAMERA = 5;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    // Bluetooth adapter for communication
    private BluetoothAdapter mBluetoothAdapter = null;

    // Bluetooth listener
    private static BluetoothListener mBluetoothListener = null;

    private String mMacAddress;
    private BluetoothDevice mBluetoothDevice = null;

    // UI buttons
    private Button unityButton;
    private Button forumButton;
    private Button findButton;

    // User id
    private static String mUserId = null;
    private static boolean mUserIdSent = false;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mContext = getApplicationContext();
        mResources = getResources();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();

        // TODO action bar in Unity text is vrone (make it VR-One)

        // Initialize bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.i("FAILED", "Bluetooth is not supported");
            finish();
        }

        // add listener for unity start button
        unityButton = (Button) findViewById(R.id.unitybutton);
        unityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUnityButtonPressed(v);
            }
        });

        // add listener for unity forum button
        forumButton = (Button) findViewById(R.id.forumbutton);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForumButtonPressed(v);
            }
        });

        // add listener for camera button
        forumButton = (Button) findViewById(R.id.camerabutton);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(v);
            }
        });

        // add listener for find controller button
        findButton = (Button) findViewById(R.id.findbutton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MapActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        // If BT not on, request it
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            getPairedDevice();
            if (mBluetoothListener == null) {
                // Initialize the BluetoothListener to perform bluetooth connections (if null)
                mBluetoothListener = new BluetoothListener(this, mHandler);
            }
            if (mBluetoothListener.getState() != BluetoothListener.STATE_CONNECTED) {
                // check if we are already connected, if not, connect
                connectDevice(true);
            }
        }
    }

    public void signOut() {
        super.signOut(mGoogleApiClient);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mBluetoothListener != null) {
            MenuItem optionBT = menu.findItem(R.id.toggle_bluetooth);
            if (mBluetoothListener.getState() == BluetoothListener.STATE_CONNECTED) {
                optionBT.setTitle(mResources.getString(R.string.disconnect_bluetooth));
            } else if (mBluetoothListener.getState() != BluetoothListener.STATE_CONNECTING) {
                optionBT.setTitle(mResources.getString(R.string.connect_bluetooth));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.sign_out_menu) {//sign out
            signOut();
            return true;
        } else if (i == R.id.toggle_bluetooth) {
            if (mBluetoothListener != null) {
                if (mBluetoothListener.getState() == BluetoothListener.STATE_CONNECTED) {
                    // if we are connected, disconnect Bluetooth
                    mBluetoothListener.stop();
                } else if (mBluetoothListener.getState() != BluetoothListener.STATE_CONNECTING) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        // this is a very unlikely case, but good to have in case bluetooth is somehow disabled
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        // if we are not connected (and not currently connecting), connect Bluetooth
                        connectDevice(true);
                    }
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothListener != null) {
            mBluetoothListener.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        /*if (mBluetoothListener != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothListener.getState() == BluetoothListener.STATE_NONE) {
                // Start the Bluetooth services
                mBluetoothListener.start();
            }
        }*/
    }

    // Static method to send the user id to controller via Bluetooth
    public static void sendUserIdToController() {
        if (!mUserIdSent) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && mBluetoothListener != null) {
                mUserId = user.getUid();
                // add a null terminator to the end
                // (so the controller knows when to stop)
                mUserId += "\0";
                byte send[] = mUserId.getBytes();
                mBluetoothListener.write(send);
                mUserIdSent = true;
            }
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothListener.Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothListener.STATE_CONNECTED:
                            Log.i("Connected", mConnectedDeviceName);
                            break;
                        case BluetoothListener.STATE_CONNECTING:
                            Log.i("Connecting to", mConnectedDeviceName);
                            break;
                        case BluetoothListener.STATE_LISTEN:
                        case BluetoothListener.STATE_NONE:
                            Log.i("Not yet connected", "True");
                            break;
                    }
                    break;
                case BluetoothListener.Constants.MESSAGE_WRITE:
                    /*byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);*/
                    break;
                case BluetoothListener.Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    CommandParser.parseCommand(readMessage);

                    Log.i("READ", mConnectedDeviceName + ":  " + readMessage);
                    break;
                case BluetoothListener.Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothListener.Constants.DEVICE_NAME);
                    if (null != mContext) {
                        Toast.makeText(mContext, mResources.getString(R.string.bt_connected_message, mConnectedDeviceName),
                                        Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BluetoothListener.Constants.MESSAGE_TOAST:
                    if (null != mContext) {
                        Toast.makeText(mContext, msg.getData().getString(BluetoothListener.Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    getPairedDevice();
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    getPairedDevice();
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Get the paired device
                    getPairedDevice();
                    // Bluetooth is now enabled, so set up the listener
                    mBluetoothListener = new BluetoothListener(this, mHandler);
                    connectDevice(true);
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, mResources.getString(R.string.bt_not_enabled),
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

    /*
     * Function to get the paired device, this is for calling after BT is enabled
     */
    private void getPairedDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //BluetoothDevice btDevice = null;

        //if (mBluetoothDevice == null && pairedDevices.size() > 0) {
        if (pairedDevices.size() > 0) {
            Log.i("SIZE", Integer.toString(pairedDevices.size()));
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                Log.i("NAME", deviceName);
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals(mResources.getString(R.string.bt_device_name))) {
                    mMacAddress = deviceHardwareAddress;
                    mConnectedDeviceName = deviceName;
                    mBluetoothDevice = device;
                    break;
                }
            }
        }

    }

    /**
     * Establish connection with other device
     *
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(boolean secure) {
        // Get the device MAC address
        String address = mMacAddress;
        // Get the BluetoothDevice object
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothDevice device = mBluetoothDevice;
        // Attempt to connect to the device
        mBluetoothListener.connect(device, secure);
    }


    // when the unity start button is pressed
    public void startUnityButtonPressed(View v) {
        Intent intent = new Intent(MainActivity.this, PalaceSelectionActivity.class);
        startActivity(intent);
    }

    // when the forum start button is pressed
    public void startForumButtonPressed(View v) {
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
    }

    //  when the camera start button is pressed
    private void dispatchTakePictureIntent(View v) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCameraIntent();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {android.Manifest.permission.CAMERA}, REQUEST_ENABLE_CAMERA);
        }
    }

    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ENABLE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                } else {
                    Toast toast = Toast.makeText(mContext, getResources().getString(R.string.camera_denied_toast), Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
            // TODO other possible permissions here
        }
    }

    @Override
    public void onBackPressed() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            backPressGoHome();
        }
        else {
            // if signed in as anonymous, go back to SignInActivity
            super.onBackPressed();
        }
    }

}