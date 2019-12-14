package com.demo.become;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyOtherService extends Service {
    IBinder myBinder = new LocalBinder();

    IMyHelloInterface.Stub ipcBinder = new IMyHelloInterface.Stub() {
        @Override
        public String sayHello() throws RemoteException {
            return "Stub: hello";
        }

        @Override
        public String sayAnotherHello() throws RemoteException {
            return "Stub: another hello";
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyOtherService", Thread.currentThread().getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL myUrl = new URL("https://opentdb.com/api.php?amount=1&category=32&difficulty=hard&type=multiple");
                    InputStream myStream = myUrl.openConnection().getInputStream();
                    BufferedReader myBuffer = new BufferedReader(new InputStreamReader(myStream));
                    StringBuilder myBuilder = new StringBuilder();
                    String line;
                    while ((line = myBuffer.readLine()) != null) {
                        myBuilder.append(line).append('\n');
                    }
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(MyOtherService.this);
                    Intent myIntent = new Intent("avem_intrebare");
                    myIntent.putExtra("intrebare", myBuilder.toString());
                    lbm.sendBroadcast(myIntent);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ipcBinder;
    }

    public String sayHello() {
        return "Hello World!";
    }


    public String sayAnotherHello() {
        return "Another Hello World!";
    }

    public class LocalBinder extends Binder {
        MyOtherService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyOtherService.this;
        }
    }
}
