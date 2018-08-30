package com.apps.moi.streaming;

import android.util.Log;

public class DeviceData {

    private String device_id, device_name, url;

    public DeviceData() {
    }

    //getter y setter
    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public void setDeviceName (String device_name) {
        this.device_name = device_name;
    }

    public void setUrl (String url) {
        this.url = url.substring(7);
    }

    public String getDeviceId() {
        return device_id;
    }

    public String getDeviceName() {
        return device_name;
    }

    public String getUrl() {
        return url;
    }
}
