package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class ChildActivity extends AppCompatActivity {

    Button coordinates_button, add_contact;
    LocationService locationService;
    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    double myLatitude, myLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        //start the location service when the app is created
        Intent i = new Intent(this, LocationService.class);
        startService(i);
        locationService = new LocationService();
        //locationProviderApi.requestLocationUpdates();
        //myLatitude = locationService.getMyLatitude();
        //myLongitude = locationService.getMyLongitude();

        add_contact = (Button) findViewById(R.id.AddContact);
        add_contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent a = new Intent(ChildActivity.this, AddingContact.class);
                startActivity(a);
            }

        });


        coordinates_button = (Button) findViewById(R.id.coordinatesButton3);
        coordinates_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //LocationManager locationManager = new LocationManager(ChildActivity.this);
                //Toast.makeText(ChildActivity.this, Double.toString(locationManager.getMyLatitude()) + '\n' + Double.toString(locationManager.getMyLongitude()), Toast.LENGTH_LONG).show();
                Toast.makeText(ChildActivity.this, "yo", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void requestLocationUpdates() {
        // check for PERMISSIONS first
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if location permissions are not granted - then ask for them
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 390);
            Toast.makeText(this, "permissions NOT granted - DOING something!", Toast.LENGTH_SHORT).show();
        }else{
            //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            locationService.requestMyLocation();
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
                        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                        locationService.requestMyLocation();
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
    protected void onDestroy(){
        super.onDestroy();
        Intent i = new Intent(this, LocationService.class);
        stopService(i);
        Toast.makeText(this, "the app was deleted", Toast.LENGTH_SHORT).show();
    }
}
