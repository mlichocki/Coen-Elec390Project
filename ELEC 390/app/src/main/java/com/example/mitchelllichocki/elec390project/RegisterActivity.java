package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = (EditText)findViewById(R.id.Username);
        final EditText password = (EditText)findViewById(R.id.Password);
        final EditText email = (EditText)findViewById(R.id.Email);
        Button saveButton = (Button)findViewById(R.id.SaveBtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(true){
                    BackgroundWorker backgroundWorker = new BackgroundWorker(RegisterActivity.this);
                    backgroundWorker.register(username.getText().toString(), password.getText().toString(), email.getText().toString(), "Guardian");
                }
                else {
                    if (username.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Incomplete Registration", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Credentials Saved", Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("username", username.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
