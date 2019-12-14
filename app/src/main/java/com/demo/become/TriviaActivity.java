package com.demo.become;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        Intent myIntent = new Intent(this, MyService.class);
        startService(myIntent);
    }
    LocalBroadcastManager lbm;
    BroadcastReceiver br;

    @Override
    protected void onResume() {
        super.onResume();
        lbm.registerReceiver(br, new IntentFilter("avem_intrebare"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        lbm.unregisterReceiver(br);
    }
}
