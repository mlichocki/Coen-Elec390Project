package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChildActivity extends AppCompatActivity {

    Button coordinates_button, add_contact;
    //LocationService locationService = new LocationService(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);



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
                LocationManager locationManager = new LocationManager(ChildActivity.this);
                Toast.makeText(ChildActivity.this, Double.toString(locationManager.getMyLatitude()) + '\n' + Double.toString(locationManager.getMyLongitude()), Toast.LENGTH_LONG).show();

            }
        });
    }
}
