package com.apps.moi.streaming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Moi on 22/06/2018.
 */

public class Home extends Activity {

    private EditText url, user, password;
    String redirect = "null";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* try {
            Bundle b = getIntent().getExtras();
            redirect = b.getString("redirect");

        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }*/
        setContentView(R.layout.home);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if( myPreferences.getString("url", "null").equals("null") ){


            url        = (EditText) findViewById(R.id.url);
            user       = (EditText) findViewById(R.id.user);
            password   = (EditText) findViewById(R.id.password);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goToStreaming(View view){

        if( validateForm() ){
            Intent main_activity = new Intent(getApplicationContext(), Movimiento.class);
            main_activity.putExtra("url",url.getText().toString() );
            main_activity.putExtra("user",user.getText().toString() );
            main_activity.putExtra("password",password.getText().toString() );

            startActivity(main_activity);

        }else{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Error!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Rellena todos los campos antes de comenzar el streaming")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // cierra el cuadro de dialogo
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public void goToConfiguration(View view){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",this.getPackageName(), null);
        intent.setData(uri);
        this.startActivity(intent);
    }


    public boolean validateForm(){

        if(!validaUrl() || !validaUser() || !validaPassword())
            return false;

        return true;
    }

    public boolean validaUrl(){
        url = (EditText) findViewById(R.id.url);

        if( url.getText().toString().trim().isEmpty() )     return false;
        else                                                return true;

    }

    public boolean validaUser(){
        user = (EditText) findViewById(R.id.user);

        if( user.getText().toString().trim().isEmpty() )    return false;
        else                                                return true;

    }

    public boolean validaPassword(){
        password = (EditText) findViewById(R.id.password);

        if( password.getText().toString().trim().isEmpty() ) return false;
        else                                                 return true;

    }

}
