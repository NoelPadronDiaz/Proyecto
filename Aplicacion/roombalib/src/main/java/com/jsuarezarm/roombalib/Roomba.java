package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 11/1/18.
 */

public class Roomba {

    private BluetoothCommunication mBluetoothCommunication;

    public Roomba(String name) {
        mBluetoothCommunication = new BluetoothCommunication(name);
    }

    public void connect() {
        try {
            mBluetoothCommunication.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        mBluetoothCommunication.disconnect();
    }

    public void sendCommand(byte[] command) {
        if(mBluetoothCommunication.getConnectThread().getConnectedThread() != null) {
            mBluetoothCommunication.getConnectThread().getConnectedThread().write(command);
        }
    }

}
