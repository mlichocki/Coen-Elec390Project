package com.example.mitchelllichocki.elec390project;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.security.Provider;


public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //TextView latitudeText;
    //TextView longitudeText;
    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    double myLatitude;
    double myLongitude;
    String username = null;

    //public LocationService() {
        //Toast.makeText(this, "service not created", Toast.LENGTH_SHORT).show();
    //}
    //LocationService(String username){       this.username = username;    }

    // *****   ON CREATE   ***** //

    @Override
    public void onCreate() {
        super.onCreate();

        // basic framework to get api services
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60*1000); //in milliseconds -- = 1 min
        locationRequest.setFastestInterval(1000); // 1 sec
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        googleApiClient.connect();
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
    }



    // *****   GPS LOCATION APPLICATION TYPICAL FUNCTIONS   ***** //

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
            Toast.makeText(this, "googleApiClient connected - requesting location", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "googleApiClient is NOT connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();
        Toast.makeText(this, "the service STOPPED", Toast.LENGTH_SHORT).show();
    }

    private void requestLocationUpdates() {
        // check for PERMISSIONS first
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if location permissions are not granted - then ask for them
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 390);
            Toast.makeText(this, "permissions NOT granted", Toast.LENGTH_SHORT).show();
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Toast.makeText(this, "SUCCESS requesting LocationUpdates", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "the connection was suspended", Toast.LENGTH_SHORT).show();
        // we can send a message to the "parent" here
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "failed to connect", Toast.LENGTH_SHORT).show();
        // we can send a message to the "parent" here
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        //latitudeText.setText("Latitude: " + String.valueOf(myLatitude));
        //longitudeText.setText("Longitude: " + String.valueOf(myLongitude));
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
    }

    public void requestMyLocation() {
        //this may be red because it wants us to ask for permissions
        //but the permissions were already asked (and granted) in the child activity
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    public double getMyLatitude() {
        if(myLatitude > 0 || (Math.abs(myLatitude) > 180)){
            return (double) 1000;
        }
        else {
            return myLatitude;
        }
    }
    public double getMyLongitude() {
        if(myLongitude > 0 || (Math.abs(myLongitude) > 180)){
            return (double) 1000;
        }
        else {
            return myLongitude;
        }
    }

    public IBinder onBind(Intent intent){return null;}
}
