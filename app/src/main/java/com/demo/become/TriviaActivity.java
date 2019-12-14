package com.demo.become;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriviaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Handler mainHandler = new Handler();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Thread", Thread.currentThread().getName());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.intrebare)).setText("Intrebare din thread");
                            }
                        });
                    }
                }).start();
                Log.d("Thread", "gata");
            }
        });

        lbm = LocalBroadcastManager.getInstance(this);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                Log.d("TRIVIA", "Am primit intrebarea!");
                String dateIntrebare = intent.getStringExtra("intrebare");
                Log.d("TRIVIA", dateIntrebare);
                try {
                    JSONObject myJson = new JSONObject(dateIntrebare);
                    JSONObject jsonQuestion = (JSONObject) myJson.getJSONArray("results").get(0);
                    Log.d("TRIVIA", jsonQuestion.getString("question"));
                    String raspunsBun = jsonQuestion.getString("correct_answer");
                    Log.d("TRIVIA", raspunsBun);
                    JSONArray raspunsuriGresite = jsonQuestion.getJSONArray("incorrect_answers");
                    for (int i =0; i < raspunsuriGresite.length(); i++) {
                        String jsonGresit = (String) raspunsuriGresite.get(i);
                        Log.d("TRIVIA", jsonGresit.toString());
                    }

                    ((TextView) findViewById(R.id.intrebare)).setText(jsonQuestion.getString("question"));
                    ((TextView) findViewById(R.id.raspuns1)).setText(raspunsBun);
                    ((TextView) findViewById(R.id.raspuns1)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Ai trecut clasa!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(TriviaActivity.this, MyService.class);
                            startService(myIntent);
                        }
                    });
                    ((TextView) findViewById(R.id.raspuns2)).setText((String) raspunsuriGresite.get(0));
                    ((TextView) findViewById(R.id.raspuns3)).setText((String) raspunsuriGresite.get(1));
                    ((TextView) findViewById(R.id.raspuns4)).setText((String) raspunsuriGresite.get(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Intent myIntent = new Intent(this, MyOtherService.class);
        //startService(myIntent);
    }
    LocalBroadcastManager lbm;
    BroadcastReceiver br;

    @Override
    protected void onResume() {
        super.onResume();
        lbm.registerReceiver(br, new IntentFilter("avem_intrebare"));
        Intent myIntent = new Intent(this, MyOtherService.class);
        bindService(myIntent, connection, Context.BIND_AUTO_CREATE);
        // run normal
    }

    @Override
    protected void onPause() {
        super.onPause();
        lbm.unregisterReceiver(br);
        Log.d("TRIVIA", "onPause");
        unbindService(connection);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.d("TRIVIA", "onServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IMyHelloInterface binder = IMyHelloInterface.Stub.asInterface(service);
            // mBound = true;
            try {
                Log.d("TRIVIA", binder.sayHello());
                Log.d("TRIVIA", binder.sayAnotherHello());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
             //mBound = false;
            Log.d("TRIVIA", "onServiceDisconnected");
        }
    };
}
