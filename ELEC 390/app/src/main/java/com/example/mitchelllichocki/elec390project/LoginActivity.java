package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    protected EditText username;
    protected EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        final Button loginButton = (Button)findViewById(R.id.loginButton);
        TextView registerLink = (TextView) findViewById(R.id.registerLink);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(r);
            }
        });

        //Login Button action
        loginButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if (username.getText().toString().equals("") || password.getText().toString().equals("") ){
                    Toast toast = Toast.makeText(getApplicationContext(), "Incomplete Login", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                    backgroundWorker.login(username.getText().toString(), password.getText().toString());
                }

            }

        });
    }
}
