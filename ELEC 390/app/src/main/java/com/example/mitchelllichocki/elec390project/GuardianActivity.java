package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GuardianActivity extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<>();
    Button ViewChildrenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        Button ViewChildrenBtn = (Button) findViewById(R.id.ViewChildrenBtn);

        names.add("Mitch");
        names.add("Felix");
        names.add("Matt");

        ViewChildrenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GuardianActivity.this, MapDisplayActivity.class);
                intent.putExtra("names", names);
                startActivity(intent);
            }
        });
    }

}
