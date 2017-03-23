package com.vroneinc.vrone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {
    // Code for BT permission
    //private final static int REQUEST_ENABLE_BT = 87;

    private Context context;

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

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    // Bluetooth adapter for communication
    private BluetoothAdapter mBluetoothAdapter = null;

    // Bluetooth listener
    private BluetoothListener mBluetoothListener = null;

    private String mMacAddress;
    private BluetoothDevice mBluetoothDevice = null;

    // UI buttons
    private Button connectButton;
    private Button unityButton;
    private Button forumButton;
    private Button findButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        context = getApplicationContext();

        // Initialize bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.i("FAILED", "Bluetooth is not supported");
            finish();
        }

        // add listener for bluetooth connect button
        connectButton = (Button) findViewById(R.id.connectbutton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDevice(true);
            }
        });

        // add listener for unity start button
        unityButton = (Button) findViewById(R.id.unitybutton);
        unityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connectDevice(true);
                startUnityButtonPressed(v);
            }
        });

        // add listener for unity forum button
        forumButton = (Button) findViewById(R.id.forumbutton);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connectDevice(true);
                startForumButtonPressed(v);
            }
        });

        // add listener for find controller button
        findButton = (Button) findViewById(R.id.findbutton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CameraDemoActivity.class));
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
        } else if (mBluetoothListener == null) {
            // Initialize the BluetoothChatService to perform bluetooth connections
            getPairedDevice();
            mBluetoothListener = new BluetoothListener(this, mHandler);
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
                // Start the Bluetooth chat services
                mBluetoothListener.start();
            }
        }*/
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

                    //if (!readMessage.equals("\r\n"))
                    // TODO: do whatever needs to be done for the command here

                    My_Plugin.parseCommand(readMessage);

                    Log.i("READ", mConnectedDeviceName + ":  " + readMessage);
                    break;
                case BluetoothListener.Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothListener.Constants.DEVICE_NAME);
                    if (null != context) {
                        Toast.makeText(context, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BluetoothListener.Constants.MESSAGE_TOAST:
                    if (null != context) {
                        Toast.makeText(context, msg.getData().getString(BluetoothListener.Constants.TOAST),
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
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "BT not enabled",
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
                if (deviceName.equals("Team17")) {
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
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    // when the forum start button is pressed
    public void startForumButtonPressed(View v) {
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
    }
}