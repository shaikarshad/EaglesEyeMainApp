package com.base.ours.eagleseyemainapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;

public class StartActivity extends AppCompatActivity {

    //GoogleMap mMap;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //connectWebSocket();
        Button logInButton= (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                EditText mEdit = (EditText) findViewById(R.id.editText1);
                String userName = mEdit.getText().toString();
                if (userName == null || userName.isEmpty()) {
                    buildErrorPopup();
                } else {
                    ClientConnection.euID = userName;
                    Intent intent = new Intent(StartActivity.this, MapsActivity.class);
                    //Intent intent = new Intent(StartActivity.this, RouteListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void buildErrorPopup() {
        AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
        alertDialog.setTitle("Invalid EUID");
        alertDialog.setMessage("EUID cannot be empty, please enter a valid EUID");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
