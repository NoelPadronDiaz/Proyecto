package com.jsuarezarm.roombalib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * Created by jonay on 11/1/18.
 */

public class BluetoothCommunication {

    private String deviceName;
    private BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;

    private ConnectThread connectThread;

    public BluetoothCommunication(String deviceName) {
        this.deviceName = deviceName;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connect() throws Exception {
        if(mBluetoothAdapter == null) {
            throw new Exception("Device does not support Bluetooth.");
        } else {
            if(!mBluetoothAdapter.isEnabled()) {
                throw new Exception("Bluetooth is disabled.");
            } else {

                // Get device
                Set<BluetoothDevice> bind = mBluetoothAdapter.getBondedDevices();
                boolean found = false;
                for(BluetoothDevice dev : bind) {
                    if(dev.getName().equals(deviceName)) {
                        mDevice = dev;
                        found = true;
                    }
                }
                if(!found) {
                    throw new Exception("Device not found.");
                }

                // Connect
                connectThread = new ConnectThread(mDevice);
                connectThread.run();

            }
        }
    }

    public void disconnect() {
        connectThread.cancel();
    }

    public ConnectThread getConnectThread() {
        return connectThread;
    }

}
