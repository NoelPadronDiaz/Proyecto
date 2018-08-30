package com.apps.moi.streaming;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import static com.apps.moi.streaming.MainActivity.TAG;

public class DatabaseFirebase implements Serializable {

    //
    private FirebaseDatabase database;
    private Context contexto;


    public DatabaseFirebase(Context contexto){
        this.contexto = contexto;
        database = FirebaseDatabase.getInstance();
    }

    public void setMotion(int num){
        DatabaseReference myRef = database.getReference("motion");

        myRef.setValue(num);
    }


    public void setDeviceData(String device_name, String device_id, String url){
        DatabaseReference myRef = database.getReference("devices_data");

        DeviceData data = new DeviceData();
        data.setDeviceId(device_id);
        data.setDeviceName(device_name);
        data.setUrl(url);
        myRef.child(device_id).setValue(data);
    }


    public void removeDeviceData(String device_id){
        DatabaseReference myRef = database.getReference("devices_data");
        myRef.child(device_id).removeValue();
    }


    // Read from the database the current value of num_streamings and modifies it
    public void getAndModifyNumStreaming(final String operation){
        DatabaseReference myRef = database.getReference("num_streamings");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                Long value = dataSnapshot.getValue(Long.class);
                //Log.d(TAG, "Value is: " + value);

                int i = value.intValue();
                setNumStreamingToDatabase(i, operation);
            }

            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
    }


    private void setNumStreamingToDatabase(int n, String operation){
        DatabaseReference myRef = database.getReference("num_streamings");

        if(operation.equals("+"))
            myRef.setValue(n + 1);

        if(operation.equals("-"))
            myRef.setValue(n - 1);
    }

}


