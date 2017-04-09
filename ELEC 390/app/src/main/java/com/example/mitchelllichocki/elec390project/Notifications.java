package com.example.mitchelllichocki.elec390project;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Felix on 2017-04-03.
 */

public class Notifications {

    HashMap<Integer, String> NotifMap = new HashMap<>();


    //ArrayList<String> NotifContentTitle = new ArrayList<>();
    ArrayList<String> NotifContentText = new ArrayList<>();
    ArrayList<Integer> NotifID = new ArrayList<>();
    NotificationManager notificationManager;
    boolean isNotifiedActive = false;


    public void showNotification (Context context, Integer NotificationID){

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(context)
                .setContentTitle("SecureTrack")
                .setContentText(NotifMap.get(NotificationID))
                .setSmallIcon(R.drawable.quantum_ic_cast_white_36);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationID, notificationBuilder.build());
        isNotifiedActive = true;

    }

    public void InitializeNotifications (){



        NotifMap.put(1, "A Child has entered the Beacon");
        NotifMap.put(22, "A child has left a Beacon");
        NotifMap.put(7, "A Child has missed his Arrival Time");
        NotifMap.put(44, "A Child's GPS has been disabled");


    }
}
