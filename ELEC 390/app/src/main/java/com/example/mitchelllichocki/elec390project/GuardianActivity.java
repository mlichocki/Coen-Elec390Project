package com.example.mitchelllichocki.elec390project;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GuardianActivity extends AppCompatActivity {

    ArrayList<String> children = new ArrayList<>();
    String username;
    //Button showNotificationBtn;
    //int NotificationID = 22;
    //Notifications Notifs = new Notifications();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("username", null);
        Type type = new TypeToken<String>() {}.getType();
        username = gson.fromJson(json, type);

        if(!isServiceRunning(NotificationService.class)) {
            Intent intent = new Intent(GuardianActivity.this, NotificationService.class);
            intent.putExtra("username", username);
            startService(intent);
            //Toast.makeText(this, "NotificationService Started", Toast.LENGTH_SHORT);
        }
        else{
            //Toast.makeText(this, "NotificationService Stopped", Toast.LENGTH_SHORT);
        }

        //showNotificationBtn = (Button) findViewById(R.id.showNotif);
        Button btn = (Button)findViewById(R.id.Add_Child);
        //Notifs.InitializeNotifications();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianActivity.this, AddChildActivity.class);
                startActivity(intent);
            }
        });

        Button ViewChildrenBtn = (Button) findViewById(R.id.ViewChildrenBtn);


        ViewChildrenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(GuardianActivity.this, MapDisplayActivity.class);
                startActivity(intent);
            }
        });


     /*   showNotificationBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){
                //finish();
                Notifs.showNotification(context, NotificationID);
            }

        });
        */

    }

/*
    public void stopNotification (View view){

        if (isNotifiedActive){
            notificationManager.cancel(NotifID.get(0));
        }
    }
*/

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // Loop through the running services
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // If the service is running then return true
                return true;
            }
        }
        return false;
    }
}