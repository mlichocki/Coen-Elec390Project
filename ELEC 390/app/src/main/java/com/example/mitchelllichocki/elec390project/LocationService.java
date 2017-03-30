package com.example.mitchelllichocki.elec390project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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


public class LocationService extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView latitudeText;
    TextView longitudeText;
    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    static Double myLatitude;
    static Double myLongitude;


    // *****   ON CREATE   ***** //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_service);

        latitudeText = (TextView) findViewById(R.id.myLatitude);
        longitudeText = (TextView) findViewById(R.id.myLongitude);

        // basic framework to get api services
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60*1000); //in milliseconds -- = 1 min
        locationRequest.setFastestInterval(1000); // 1 sec
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }


    // *****   GPS LOCATION APPLICATION TYPICAL FUNCTIONS   ***** //

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        // check for PERMISSIONS first
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if location permissions are not granted - then ask for them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 390);
            Toast.makeText(this, "permissions NOT granted - DOING something!", Toast.LENGTH_SHORT).show();
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Toast.makeText(this, "SUCCESS requesting LocationUpdates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 390:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    // once permissions are granted -> request for location
                    if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                        Toast.makeText(this, "SUCCESS onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    // if the permissions are not granted, there is no point for the app to keep running -> close the application
                    Toast.makeText(this, "app needs permissions to run", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
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
        latitudeText.setText("Latitude: " + String.valueOf(myLatitude));
        longitudeText.setText("Longitude: " + String.valueOf(myLongitude));
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
    }


    // *****   ANDROID APPLICATION TYPICAL FUNCTIONS   ***** //

    @Override
    protected void onStart() {
        // WE WANT TO START GOOGLE API WHEN THE APPLICATION STARTS
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        // WE WANT TO REQUEST LOCATIONS IF THE GOOGLE API IS CONNECTED
        super.onResume();
        if (googleApiClient.isConnected()){
            requestLocationUpdates();
            Toast.makeText(this, "googleApiClient connected - onResume - requesting location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        // THIS METHOD SUSPENDS THE LOCATION UPDATE REQUESTS
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        //if we do not add the removeLocationUpdates then the app will run in the background and will keep collecting data
        //for now we removeLocationUpdates to save power
    }

    @Override
    protected void onStop() {
        // THIS METHOD DISCONNECTS THE CLIENT WHEN THE APPLICATION STOPS RUNNING
        super.onStop();
        googleApiClient.disconnect();
        // if the app is stopped/disconnected for good
    }


    public static Double getMyLatitude() {
        if(myLatitude == null){
            return (double) 1000;
        }
        else {
            return myLatitude;
        }
    }
    public static Double getMyLongitude() {
        if(myLongitude == null){
            return (double) 1000;
        }
        else {
            return myLongitude;
        }
    }


}
