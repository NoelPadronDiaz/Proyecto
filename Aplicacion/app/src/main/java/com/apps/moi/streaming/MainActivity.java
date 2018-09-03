package com.apps.moi.streaming;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import com.apps.moi.streaming.motiondetection.MotionDetector;
import com.apps.moi.streaming.motiondetection.MotionDetectorCallback;


public class MainActivity extends Activity implements RtspClient.Callback, Session.Callback, SurfaceHolder.Callback {

    private com.github.nkzawa.socketio.client.Socket mSocket;
    private Handler handler = new Handler();
    private Runnable runnable;
    private DatabaseFirebase database;
    private MotionDetector motionDetector;
    private int contador;
    private DeviceData deviceData;

    String device_name;
    String device_id;

    // log tag
    public final static String TAG = MainActivity.class.getSimpleName();

    // surfaceview
    private static SurfaceView mSurfaceView;

    // Rtsp session
    private Session mSession;
    private static RtspClient mClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos la database y las variables con los datos del dispositivo
        database    = new DatabaseFirebase(this);
        device_name = Build.MANUFACTURER + " " + Build.MODEL;
        device_id   = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceData = new DeviceData();


        //OBTENER LOS DATOS PARA EL STREAMING y ESTABLECERLOS EN EL APPCONFIG
        try {
            Bundle b = getIntent().getExtras();
            AppConfig.STREAM_URL = b.getString("url");
            AppConfig.PUBLISHER_USERNAME = b.getString("user");
            AppConfig.PUBLISHER_PASSWORD = b.getString("password");


            //Guardar datos junto con el resto de datos de la app en el fichero preferences
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("url", AppConfig.STREAM_URL);
            myEditor.putString("user", AppConfig.PUBLISHER_USERNAME);
            myEditor.putString("password", AppConfig.PUBLISHER_PASSWORD);
            myEditor.apply();
            myEditor.commit();

        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }



        //SOCKET
        SocketManager app = (SocketManager)getApplication();
        mSocket = app.getmSocket();

        mSocket.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT, onConnect);
        mSocket.on(com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(com.github.nkzawa.socketio.client.Socket.EVENT_ERROR, onError);
        mSocket.on("move", onMessageBack);

        mSocket.connect();



        //RTSP & SURFACE
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

//        motionDetector = new MotionDetector(this, (android.view.SurfaceView) findViewById(R.id.surfaceView1));
//        Log.d("motion","antes");
//        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
//            @Override
//            public void onMotionDetected() {
//                Log.d("motion","si");
//                contador++;
//                if(contador >= 2){
//                    database.setDeviceData(device_name, device_id, AppConfig.STREAM_URL,1);
//                    database.setMotionGeneral(1);
//                }
//            }
//
//            @Override
//            public void onTooDark() {
//                //txtStatus.setText("Too dark here");
//                Toast.makeText(MainActivity.this, "Oscuro", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Log.d("motion","sale");

        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(this);


        // Initialize RTSP client
        initRtspClient();
    }


    @Override
    protected void onResume() {
        super.onResume();

//        motionDetector.onResume();

        // Start camera preview
        mSession.startPreview();

        // Start video stream
        mClient.startStream();
        //toggleStreaming();
        database.getAndModifyNumStreaming("+");

    }

    @Override
    protected void onPause(){
        super.onPause();

        // already streaming, stop streaming
        // stop camera preview
        mSession.stopPreview();
        Log.d("hola: ", "pause");

        // stop streaming
        mClient.stopStream();
        //toggleStreaming();
        //database.getAndModifyNumStreaming("-");
        //motionDetector.onPause();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void goToHomeActivity(View view){
        /*Intent home_activity = new Intent(this, Home.class);
        home_activity.putExtra("redirect", "true");
        startActivity(home_activity);*/
        finish();


        // Restamos uno al contador de streamings en linea
        //database.getAndModifyNumStreaming("-");
        database.removeDeviceData(device_id);
    }

    /* METODOS MANIPULACION DE SOCKET */
    private Emitter.Listener onMessageBack = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject) args[0];

            try {
                Log.d("VALOR: ", obj.get("move_to").toString());
                if( obj.get("move_to").toString().equals("w") ){
                    Log.d("MESSAGE: ", "MOVE TO W!!");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("MainActivity: ", "socket connected");
        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() { Log.d("MainActivity: ", "Disconnected....");
                }
            });
        }
    };


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() { Log.d("MainActivity: ", "Failed to connect...");
                }
            });
        }
    };


    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("MainActivity: ", "Error to connect...");
        }
    };





    //**                    **//
    //
    //      RTSP & SURFACE
    //
    //**                    **//
    private void initRtspClient() {

        // Configures the SessionBuilder
        mSession = SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setAudioQuality(new AudioQuality(8000, 16000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
                .setSurfaceView(mSurfaceView).setPreviewOrientation(0)
                .setCallback(this).build();


        // Configures the RTSP client
        mClient = new RtspClient();
        mClient.setSession(mSession);
        mClient.setCallback(this);
        mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);
        String ip, port, path;


        // We parse the URI written in the Editext
        Pattern uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Matcher m = uri.matcher(myPreferences.getString("url","null"));

        m.find();
        ip = m.group(1);
        port = m.group(2);
        path = m.group(3);



        mClient.setCredentials(myPreferences.getString("user","null"), myPreferences.getString("password","null"));
        mClient.setServerAddress(ip, Integer.parseInt(port));
        mClient.setStreamPath("/" + path);


        // Sumamos uno al contador de streamings al conectarse por rtsp
        database.getAndModifyNumStreaming("+");

        database.setDeviceData(device_name, device_id, AppConfig.STREAM_URL,0);
    }


    private void toggleStreaming() {
        if (!mClient.isStreaming()) {
            // Start camera preview
            mSession.startPreview();

            // Start video stream
            mClient.startStream();
        } else {
            // already streaming, stop streaming
            // stop camera preview
            mSession.stopPreview();

            // stop streaming
            mClient.stopStream();
        }
    }


    @Override
    public void onDestroy() {
        // Restamos uno al contador de streamings en linea
        //database.getAndModifyNumStreaming("-");
        Log.d("hola: ", "destroy");

        database.removeDeviceData(device_id);

        mClient.release();
        mSession.release();
        mSurfaceView.getHolder().removeCallback(this);
        database.getAndModifyNumStreaming("-");
        deviceData.motion = 0;
        database.setMotionGeneral(0);

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onSessionError(int reason, int streamType, Exception e) {
        switch (reason) {
            case Session.ERROR_CAMERA_ALREADY_IN_USE:
                break;
            case Session.ERROR_CAMERA_HAS_NO_FLASH:
                break;
            case Session.ERROR_INVALID_SURFACE:
                break;
            case Session.ERROR_STORAGE_NOT_READY:
                break;
            case Session.ERROR_CONFIGURATION_NOT_SUPPORTED:
                break;
            case Session.ERROR_OTHER:
                break;
        }

        if (e != null) {
            alertError(e.getMessage());
            e.printStackTrace();
        }
    }


    private void alertError(final String msg) {
        final String error = (msg == null) ? "Unknown error: " : msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(error).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRtspUpdate(int message, Exception exception) {
        switch (message) {
            case RtspClient.ERROR_CONNECTION_FAILED:
            case RtspClient.ERROR_WRONG_CREDENTIALS:
                alertError(exception.getMessage());
                exception.printStackTrace();
                break;
        }
    }




    @Override
    public void onPreviewStarted() {
    }

    @Override
    public void onSessionConfigured() {
    }

    @Override
    public void onSessionStarted() {
    }

    @Override
    public void onSessionStopped() {
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onBitrareUpdate(long bitrate) {
    }
}