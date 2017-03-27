package com.example.matthew.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

public class AddChildActivty extends AppCompatActivity {

    //Creation of edittext and button

    Button usrbtn;
    EditText usr,psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_activty);

        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.psd);
        usrbtn = (Button) findViewById(R.id.usrbtn);

        usrbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                //IF STATEMENT FOR PROPER CREDENTIALS
                if (usr.getText().toString().equals("Child") && psd.getText().toString().equals("Child")){
                Toast.makeText(getApplicationContext(),"Child Successfully Added", Toast.LENGTH_LONG).show();
                Intent myintent = new Intent (AddChildActivty.this, SuccessfulAdd.class);
                startActivity(myintent);
                }

                // ELSE STATEMENT FOR BLANK TEXT FIELDS
                else if (usr.getText().toString().equals("") || psd.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Field(s) left blank!", Toast.LENGTH_LONG).show();
              }
            }
        });
    }
}
