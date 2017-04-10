package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ChildActivity extends AppCompatActivity {

    Button coordinates_button, add_contact;
    //LocationService locationService = new LocationService(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("username", null);
        Type type = new TypeToken<String>() {}.getType();
        String username = gson.fromJson(json, type);


        Intent childService = new Intent(this, ChildService.class);
        childService.putExtra("username", username);
        startService(childService);


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
                Log.d("ChildActivity", "new LocationManager(ChildActivity.this)");
                Toast.makeText(ChildActivity.this, Double.toString(locationManager.getMyLatitude()) + '\n' + Double.toString(locationManager.getMyLongitude()), Toast.LENGTH_SHORT).show();
                Log.d("ChildActivity", "Toast: Latitude = " + Double.toString(locationManager.getMyLatitude()) + " & Longitude = " + Double.toString(locationManager.getMyLongitude()));
            }
        });
    }
}
