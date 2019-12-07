package com.demo.become;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "BATTERY", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            Toast.makeText(context, "LIMBA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Alt eveniment!", Toast.LENGTH_SHORT).show();
        }
    }
}
