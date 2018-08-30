package com.apps.moi.streaming.motiondetection;


public interface MotionDetectorCallback {
    void onMotionDetected();
    void onTooDark();
}
