package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChildActivity extends AppCompatActivity {

    Button coordinates_button, add_contact;
    String username;
    //final int refreshRate = 1000*5;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    LocationService locationService;
    //double lastTransLat = 0, lastTransLong = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        locationService = new LocationService(username);


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
                Intent i = new Intent(ChildActivity.this, LocationService.class);
                startActivity(i);
            }
        });

        /* Using ChildService to always run this function following a successful child login.
        final Handler handler = new Handler();
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
        }, 0); //mapLoadTime is the delay for the map to load
        */
    }
}
