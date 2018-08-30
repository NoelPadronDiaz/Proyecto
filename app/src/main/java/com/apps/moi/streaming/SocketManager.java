package com.apps.moi.streaming;

import android.app.Application;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;


/**
 * Created by Moi on 31/05/2018.
 */

public class SocketManager extends Application {

    private com.github.nkzawa.socketio.client.Socket mSocket;
    private static final String URL = "http://192.168.1.35:3000";


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public com.github.nkzawa.socketio.client.Socket getmSocket() {
        return mSocket;
    }

}