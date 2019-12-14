package com.demo.become;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Service", "Am primit intent!");
        try {
            URL myUrl = new URL("https://opentdb.com/api.php?amount=1&category=32&difficulty=hard&type=multiple");
            InputStream myStream = myUrl.openConnection().getInputStream();
            BufferedReader myBuffer = new BufferedReader(new InputStreamReader(myStream));
            StringBuilder myBuilder = new StringBuilder();
            String line;
            while ((line = myBuffer.readLine()) != null) {
                myBuilder.append(line).append('\n');
            }
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
            Intent myIntent = new Intent("avem_intrebare");
            myIntent.putExtra("intrebare", myBuilder.toString());
            lbm.sendBroadcast(myIntent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
