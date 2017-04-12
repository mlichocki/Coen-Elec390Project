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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ChildActivity extends AppCompatActivity {

    Button add_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("username", null);
        Type type = new TypeToken<String>() {}.getType();
        String username = gson.fromJson(json, type);

        if(!isServiceRunning(NotificationService.class)) {
            Intent childService = new Intent(this, ChildService.class);
            childService.putExtra("username", username);
            startService(childService);
            Toast.makeText(this, "NotificationService Started", Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(this, "NotificationService Stopped", Toast.LENGTH_SHORT);
        }


        add_contact = (Button) findViewById(R.id.AddContact);
        add_contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent a = new Intent(ChildActivity.this, AddingContact.class);
                startActivity(a);
            }

        });

    }

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
