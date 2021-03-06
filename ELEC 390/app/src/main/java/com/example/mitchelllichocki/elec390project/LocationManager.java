package com.example.mitchelllichocki.elec390project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by mitchelllichocki on 2017-04-09.
 */

public class LocationManager extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private String username = null;

    static Double myLatitude, myLongitude;

    public LocationManager(Context CONTEXT) {

        context = CONTEXT;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("username", null);
        Type type = new TypeToken<String>() {
        }.getType();
        username = gson.fromJson(json, type);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000); //in milliseconds -- = 1 min
        locationRequest.setFastestInterval(1000); // 1 sec
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        googleApiClient.connect();
        //requestLocationUpdates();
        Log.d("LocationManager", "googleApiClient.connect()");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("LocationManager", "onConnected(@Nullable Bundle bundle)");
        requestLocationUpdates();
        Log.d("LocationManager", "requestLocationUpdates()");
    }

    private void requestLocationUpdates(){
        Log.d("LocationManager", "requestLocationUpdates()");
        // check for PERMISSIONS first
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if location permissions are not granted - then ask for them
            Log.d("LocationManager", "permissions NOT granted");
            ActivityCompat.requestPermissions((Activity) context , new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 390);
            //Toast.makeText(context, "permissions NOT granted - DOING something!", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("LocationManager", "permissions granted");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            //Toast.makeText(context, "SUCCESS requesting LocationUpdates", Toast.LENGTH_SHORT).show();
        }
    }

    /**********************************************************************************************************************************/
    /**Potential Error**/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("LocationManager", "onRequestPermissionsResult: requestCode = " + requestCode);
        switch (requestCode) {
            case 390:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    // once permissions are granted -> request for location
                    if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                        //Toast.makeText(this, "SUCCESS onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    // if the permissions are not granted, there is no point for the app to keep running -> close the application
                    //Toast.makeText(this, "app needs permissions to run", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            default:{
                Log.d("LocationManager", "super.onRequestPermissionsResult(requestCode, permissions, grantResults)");
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    /**********************************************************************************************************************************/

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("LocationManager", "onConnectionSuspended");
        //Toast.makeText(this, "the connection was suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LocationManager", "onConnectionFailed");
        //Toast.makeText(this, "failed to connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationManager", "onLocationChanged");
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        //Toast.makeText(context, "onLocationChanged", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        // WE WANT TO START GOOGLE API WHEN THE APPLICATION STARTS
        Log.d("LocationManager", "onStart()");
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        // WE WANT TO REQUEST LOCATIONS IF THE GOOGLE API IS CONNECTED
        Log.d("LocationManager", "onResume()");
        super.onResume();
        if (googleApiClient.isConnected()){
            Log.d("LocationManager", "if (googleApiClient.isConnected())");
            requestLocationUpdates();
            //Toast.makeText(this, "googleApiClient connected - onResume - requesting location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        // THIS METHOD SUSPENDS THE LOCATION UPDATE REQUESTS
        Log.d("LocationManager", "onPause()");
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        //if we do not add the removeLocationUpdates then the app will run in the background and will keep collecting data
        //for now we removeLocationUpdates to save power
    }

    @Override
    protected void onStop() {
        Log.d("LocationManager", "onStop()");
        // THIS METHOD DISCONNECTS THE CLIENT WHEN THE APPLICATION STOPS RUNNING
        super.onStop();
        googleApiClient.disconnect();
        // if the app is stopped/disconnected for good
    }


    public static Double getMyLatitude() {
        Log.d("LocationManager", "getMyLatitude()");
        if(myLatitude == null){
            return (double) 5000;
        }
        else {
            return myLatitude;
        }
    }
    public static Double getMyLongitude() {
        Log.d("LocationManager", "getMyLongitude()");
        if(myLongitude == null){
            return (double) 5000;
        }
        else {
            return myLongitude;
        }
    }

}
