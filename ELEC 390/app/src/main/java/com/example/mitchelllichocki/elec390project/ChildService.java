package com.example.mitchelllichocki.elec390project;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ChildService extends Service {
    double lastTransLat = 0, lastTransLong = 0;
    String username;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    LocationManager locationManager;

    public ChildService(){
        Log.d("ChildService", "ChildService()");
    }

    @Override
    public void onCreate(){
        Log.d("ChildService", "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Log.d("ChildService", "onStartCommand(Intent intent, int flags, int startID)");
        if(intent != null) {
            if (intent.hasExtra("username")) {
                username = intent.getStringExtra("username");
                Log.d("ChildService", username);
            }
        }

        Log.d("ChildService", "Pre locationManager");
        locationManager = new LocationManager(getApplicationContext());

        Log.d("ChildService", "Post locationManager");
        final Handler handler = new Handler();
        final int refreshRate = 1000*10;

        handler.postDelayed(new Runnable() {
            @Override
            public void run(){

                double latitude = locationManager.getMyLatitude();
                double longitude = locationManager.getMyLongitude();
                Log.d("ChildService", "Latitude: " + Double.toString(latitude));
                Log.d("ChildService", "Longitude: " + Double.toString(longitude));
                /*
                Only update the server when there is a change in positioning of 10 meters or more
                double distanceTraveledLat = Math.abs(latitude - lastTransLat)*1000*1000/9;
                double distanceTraveledLong = Math.abs(longitude - lastTransLong)*Math.cos(2*Math.PI*latitude/360)*1000*1000/9;
                double distanceTraveled = Math.sqrt(Math.pow(distanceTraveledLat, 2) + Math.pow(distanceTraveledLong, 2));
                if(distanceTraveled >= 10) {
                */
                    backgroundWorker.postCoordinates(username, latitude, longitude, "update");
                    lastTransLat = latitude;
                    lastTransLong = longitude;
                //}

                handler.postDelayed(this, refreshRate);
            }
        }, 0);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ChildService", "onDestroy()");
        backgroundWorker.postCoordinates(username, lastTransLat, lastTransLong, "disconnected");
    }
}
