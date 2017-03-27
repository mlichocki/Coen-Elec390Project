package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChildActivity extends AppCompatActivity {

    Button emergency_text, emergency_call, help, coordinates_button;
    String username;
    final int refreshRate = 1000*5, mapLoadTime = 1000*5;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    double test = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        emergency_text = (Button) findViewById(R.id.EM_TEXT);
        emergency_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent textIntent= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:15149495656"));
                textIntent.putExtra("sms_body","URGENT: NEED HELP! - Secure Track");
                startActivity(textIntent);
            }


        });


        emergency_call = (Button) findViewById(R.id.EM_CALL);
        emergency_call.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent callIntent=new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:15149495656"));
                startActivity(callIntent);
            }
        });

        help = (Button) findViewById(R.id.HelpBtn);
        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent textIntent= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:15149495656"));
                textIntent.putExtra("sms_body","URGENT: NEED HELP! - Secure Track");
                startActivity(textIntent);
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                //backgroundWorker.postCoordinates(username, test, test);
                //test = test - 1;
            }
        }, mapLoadTime); //mapLoadTime is the delay for the map to load
    }
}
