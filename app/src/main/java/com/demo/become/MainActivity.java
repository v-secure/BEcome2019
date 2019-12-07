package com.demo.become;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_MAIN);
                myIntent.setClassName("com.demo.asistent", "com.demo.asistent.MainActivity");
                ActivityInfo activityInfo = myIntent.resolveActivityInfo(getPackageManager(), myIntent.getFlags());

                if (activityInfo != null && activityInfo.exported) {
                    startActivity(myIntent);
                } else {
                    Log.w("Asistent", "Nu este prezenta aplicatia principala!");
                }
            }
        });

        myReceiver = new MyReceiver();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "NU AVEM PERMISIUNE", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        } else {
            Toast.makeText(this, "AVEM PERMISIUNE!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Multumim!", Toast.LENGTH_SHORT).show();
            } else {
                // nu avem permisiune
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECEIVE_SMS)) {
                    // insa user-ul este dispus sa primeasca mai multe informatii despre motivul
                    // pentru care solicitam permisiunea
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                    myAlert.setTitle("Motiv permisiune");
                    myAlert.setMessage("Detalii de ce avem nevoie");
                    myAlert.setPositiveButton("Am inteles", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                        }
                    });
                    myAlert.show();
                } else {
                    // userul a refuzat sa aprobe permisiunea si nici nu doreste alte informatii
                    doNotRegisterReceiver = true;
                }
            }
        }
    }

    private MyReceiver myReceiver;
    private boolean doNotRegisterReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if (!doNotRegisterReceiver) {
            IntentFilter myFilter = new IntentFilter();
            myFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(myReceiver, myFilter);
        } else {
            // ne asiguram ca am facut "disable" la componenta care avea nevoie de permisiune
            myReceiver = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // confirmam ca exista componenta inainte de a face unregister
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
