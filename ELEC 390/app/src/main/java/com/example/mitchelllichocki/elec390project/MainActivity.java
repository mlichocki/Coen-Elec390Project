package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        names.add("Mitch");
        names.add("Felix");
        names.add("Matt");

    }

    public void BUTTON(View view){
        Intent intent = new Intent(this, MapDisplayActivity.class);
        intent.putExtra("names", names);
        startActivity(intent);
    }
}
