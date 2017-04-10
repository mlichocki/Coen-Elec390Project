package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
<<<<<<< HEAD
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
=======
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
>>>>>>> 70951e3ad1c97bdf69feb7522031973133568800
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class ChildActivity extends AppCompatActivity {

    Button add_contact;

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

    }
}
