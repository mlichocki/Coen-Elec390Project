package com.example.mitchelllichocki.elec390project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddingContact extends AppCompatActivity {

    EditText Name;
    EditText Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_contact);

        Name = (EditText) findViewById(R.id.Name);
        Number = (EditText) findViewById(R.id.Number);
    }

    //save info

    public void SaveInfo (View view){

        SharedPreferences sharedPref = getSharedPreferences("Contact_G", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Name", Name.getText().toString());
        editor.putString("Number", Number.getText().toString());
        editor.apply();

        Toast.makeText(this, "Contact Saved", Toast.LENGTH_LONG).show();

    }


}























