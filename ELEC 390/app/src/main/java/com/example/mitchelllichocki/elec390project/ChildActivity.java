package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ChildActivity extends AppCompatActivity {

    Button emergency_text;
    Button emergency_call;

    ArrayList<String> names = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        names.add("Mitch");
        names.add("Felix");
        names.add("Matt");

        emergency_text= (Button) findViewById(R.id.EM_TEXT);
        emergency_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent textIntent= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:15149495656"));
                textIntent.putExtra("sms_body","URGENT: NEED HELP! - VIA TRACKER");
                startActivity(textIntent);
            }


        });


        emergency_call= (Button) findViewById(R.id.EM_CALL);
        emergency_call.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent callIntent=new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:15149495656"));
                startActivity(callIntent);
            }
        });

    }

    public void BUTTON(View view){
        Intent intent = new Intent(this, MapDisplayActivity.class);
        intent.putExtra("names", names);
        startActivity(intent);
    }
}
