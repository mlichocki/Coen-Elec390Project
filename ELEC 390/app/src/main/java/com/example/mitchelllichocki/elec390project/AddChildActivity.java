package com.example.mitchelllichocki.elec390project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddChildActivity extends AppCompatActivity {

    //Creation of edittext and button

    Button usrbtn;
    EditText usr,psd,name,phone;
    String guardianUsername;
    ArrayList<String> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_activity);

        Intent intent = getIntent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("username", null);
        Type type = new TypeToken<String>() {}.getType();
        guardianUsername = gson.fromJson(json, type);

        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.psd);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        usrbtn = (Button) findViewById(R.id.usrbtn);
        final BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        usrbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                //IF STATEMENT FOR PROPER CREDENTIALS
                if ( !((usr.getText().toString().equals("") || psd.getText().toString().equals("")) || name.getText().toString().equals("")) ){
                    Toast.makeText(getApplicationContext(),"Child Successfully Added", Toast.LENGTH_LONG).show();
                    backgroundWorker.addChild(guardianUsername, usr.getText().toString(), name.getText().toString(), psd.getText().toString(), children);



                    // SHARED PREFERENCE STUFF - MATT - APRIL5TH

                    SharedPreferences sharedPref = getSharedPreferences("Contact_C", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Name", name.getText().toString());
                    editor.putString("Number", phone.getText().toString());
                    editor.apply();





                }

            }
        });
    }
}
