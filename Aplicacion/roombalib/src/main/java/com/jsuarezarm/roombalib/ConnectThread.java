package com.jsuarezarm.roombalib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by jonay on 11/1/18.
 */

public class ConnectThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    private ConnectedThread connectedThread;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Parameters.ROOMBA_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mmSocket = tmp;
    }

    public void run() {
        // Cancel discover of devices


        // Connect socket
        try {
            mmSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                mmSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        // Manage the connection in a separated thread
        connectedThread = new ConnectedThread(mmSocket);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectedThread getConnectedThread() {
        return connectedThread;
    }

}
