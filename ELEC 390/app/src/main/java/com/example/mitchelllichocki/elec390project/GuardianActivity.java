package com.example.mitchelllichocki.elec390project;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GuardianActivity extends AppCompatActivity {

    ArrayList<String> children = new ArrayList<>();
    String username;
    Button showNotificationBtn;
    int NotificationID = 22;
    Notifications Notifs = new Notifications();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);


        showNotificationBtn = (Button) findViewById(R.id.showNotif);

        Button btn = (Button)findViewById(R.id.Add_Child);
        Notifs.InitializeNotifications();
        final Context context = this;
        Intent intent = getIntent();
        children = intent.getStringArrayListExtra("children");
        username = intent.getStringExtra("username");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianActivity.this, AddChildActivity.class);
                intent.putExtra("children", children);
                intent.putExtra("guardianUsername", username);
                startActivity(intent);
            }
        });

        Button ViewChildrenBtn = (Button) findViewById(R.id.ViewChildrenBtn);


        ViewChildrenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(GuardianActivity.this, MapDisplayActivity.class);
                intent.putExtra("children", children);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        showNotificationBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){
                //finish();
                Notifs.showNotification(context, NotificationID);
            }
        });

    }


/*
    public void stopNotification (View view){

        if (isNotifiedActive){
            notificationManager.cancel(NotifID.get(0));
        }
    }
*/

}