package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GuardianActivity extends AppCompatActivity {

    ArrayList<String> children = new ArrayList<>();
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        Intent intent = getIntent();
        children = intent.getStringArrayListExtra("children");
        username = intent.getStringExtra("username");
        
        Button btn = (Button)findViewById(R.id.Add_Child);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianActivity.this, AddChildActivity.class);
                intent.putExtra("children", children);
                intent.putExtra("guardianUsername", username);
                startActivity(intent);
            }
        });

        Button ViewChildrenBtn = (Button) findViewById(R.id.ViewChildrenBtn);


        ViewChildrenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(GuardianActivity.this, MapDisplayActivity.class);
                intent.putExtra("children", children);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }

}