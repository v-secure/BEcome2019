package com.demo.asistent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "com.demo.asistent.main_channel_1";
    NotificationManagerCompat notificationManagerCompat;
    NotificationManager notificationManager;
    TextView textView;
    int notificationId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManager = getSystemService(NotificationManager.class);
        createNotificationChannel();

        textView = (TextView) findViewById(R.id.textView1);
        textView.setText(getTextFromSharedPreferences());

        Intent myIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, myIntent, 0);

        final NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this,CHANNEL_ID);
        notification.setSmallIcon(R.drawable.notification_icon);
        notification.setContentTitle("Titlul notificarii");
        notification.setContentText("Content text care ar trebui " +
                "sa fie un pic mai descriptiv");
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notification.setAutoCancel(false);
        notification.setContentIntent(pendingIntent);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                notificationManagerCompat.notify(notificationId,notification.build());
                notificationId++;
                setTextFromSharedPreferences("Alt text");
            }
        });
    }

    private String getTextFromSharedPreferences(){
        SharedPreferences sharedPreferences =
                getSharedPreferences("myfile",Context.MODE_PRIVATE);
        String myText = sharedPreferences.getString("shared_text","Default value");
        return myText;
    }

    private void setTextFromSharedPreferences(String value){
        SharedPreferences sharedPreferences =
                getSharedPreferences("myfile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("shared_text",value);
        editor.apply();
    }


    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelName = "Main Channel for Asistant";
            String description = "The channel where we receive Assistant notifications";
            NotificationChannel notificationChannel =
                        new NotificationChannel(CHANNEL_ID,channelName,importance);
            notificationChannel.setDescription(description);
            notificationManager.createNotificationChannel(notificationChannel);
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
