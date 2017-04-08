package com.example.mitchelllichocki.elec390project;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class ChildService extends Service {
    double lastTransLat = 0, lastTransLong = 0;
    String username;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);

    public ChildService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        if(intent != null) {
            if (intent.hasExtra("username")) {
                username = intent.getStringExtra("username");
            }
        }
        final Handler handler = new Handler();
        final int refreshRate = 1000*10;
        final LocationService locationService = new LocationService(username);

        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                double latitude = locationService.getMyLatitude();
                double longitude = locationService.getMyLongitude();
                //Only update the server when there is a change in positioning of 10 meters or more
                double distanceTraveledLat = Math.abs(latitude - lastTransLat)*1000*1000/9;
                double distanceTraveledLong = Math.abs(longitude - lastTransLong)*Math.cos(2*Math.PI*latitude/360)*1000*1000/9;
                double distanceTraveled = Math.sqrt(Math.pow(distanceTraveledLat, 2) + Math.pow(distanceTraveledLong, 2));
                if(distanceTraveled >= 10) {
                    backgroundWorker.postCoordinates(username, latitude, longitude, "update");
                    lastTransLat = latitude;
                    lastTransLong = longitude;
                }

                handler.postDelayed(this, refreshRate);
            }
        }, 0);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backgroundWorker.postCoordinates(username, lastTransLat, lastTransLong, "disconnected");
    }
}
