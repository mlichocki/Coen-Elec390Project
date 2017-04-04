package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddChildActivity extends AppCompatActivity {

    //Creation of edittext and button

    Button usrbtn;
    EditText usr,psd, name;
    String guardianUsername;
    ArrayList<String> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_activity);

        Intent intent = getIntent();
        children = intent.getStringArrayListExtra("children");
        guardianUsername = intent.getStringExtra("guardianUsername");

        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.psd);
        name = (EditText) findViewById(R.id.name);
        usrbtn = (Button) findViewById(R.id.usrbtn);
        final BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        usrbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                //IF STATEMENT FOR PROPER CREDENTIALS
                if ( !((usr.getText().toString().equals("") || psd.getText().toString().equals("")) || name.getText().toString().equals("")) ){
                    Toast.makeText(getApplicationContext(),"Child Successfully Added", Toast.LENGTH_LONG).show();
                    backgroundWorker.addChild(guardianUsername, usr.getText().toString(), name.getText().toString(), psd.getText().toString(), children);
                }
            }
        });
    }
}
