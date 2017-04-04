package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    protected EditText username;
    protected EditText password;

    //Hardcoded profiles
    protected String childUsername = "Child", childPassword = "Child", guardianUsername = "Guardian", guardianPassword = "Guardian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        final CheckBox rememberMe = (CheckBox)findViewById(R.id.cbRememberMe);
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
                BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                backgroundWorker.login(username.getText().toString(), password.getText().toString());

            }

        });
    }
}
