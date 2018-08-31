package com.apps.moi.streaming;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.moi.streaming.motiondetection.MotionDetector;
import com.apps.moi.streaming.motiondetection.MotionDetectorCallback;


public class Motion extends AppCompatActivity {

    private TextView txtStatus;
    private MotionDetector motionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        //Intent i = new Intent(this, Main2Activity.class);

        final ImageButton btn = (ImageButton) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Motion.this, "Bien",Toast.LENGTH_SHORT);
                //Intent intent = new Intent(v.getContext(), Main2Activity.class);
                //startActivityForResult(intent, 0);
            }
        });

        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(80);
                btn.setVisibility(View.VISIBLE);
                txtStatus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTooDark() {
                txtStatus.setText("Too dark here");
            }
        });

        ////// Config Options
        //motionDetector.setCheckInterval(500);
        //motionDetector.setLeniency(20);
        //motionDetector.setMinLuma(1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionDetector.onResume();

        if (motionDetector.checkCameraHardware()) {
            txtStatus.setText("Camera found");
        } else {
            txtStatus.setText("No camera available");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionDetector.onPause();
    }
}
