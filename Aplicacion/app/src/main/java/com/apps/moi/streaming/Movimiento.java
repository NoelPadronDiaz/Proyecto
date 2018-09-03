package com.apps.moi.streaming;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.apps.moi.streaming.motiondetection.MotionDetector;
import com.apps.moi.streaming.motiondetection.MotionDetectorCallback;

public class Movimiento extends AppCompatActivity {

    private MotionDetector motionDetector;
    private String url, user, password;
    private int contador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);
        contador=0;

        Bundle b = getIntent().getExtras();
        url = b.getString("url");
        user = b.getString("user");
        password = b.getString("password");

        motionDetector = new MotionDetector(this, (android.view.SurfaceView) findViewById(R.id.surfaceView1));
        Log.d("pruebasmo","antes");
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                Log.d("pruebasmo", "si");
                contador++;
                Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
                main_activity.putExtra("url", url.toString());
                main_activity.putExtra("user", user.toString());
                main_activity.putExtra("password", password.toString());
                if (contador >= 2){
                    motionDetector.releaseCamera();
                    startActivity(main_activity);
                    finish();
                }
            }

            @Override
            public void onTooDark() {
                //txtStatus.setText("Too dark here");
                Toast.makeText(Movimiento.this, "Oscuro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        motionDetector.onResume();
    }
}
