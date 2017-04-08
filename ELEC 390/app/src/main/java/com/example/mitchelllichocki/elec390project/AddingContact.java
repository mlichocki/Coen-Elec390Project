package com.example.mitchelllichocki.elec390project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.net.Uri;


public class AddingContact extends AppCompatActivity {

    protected    EditText Name1;
    protected    EditText Number1;
    protected    EditText Name2;
    protected    EditText Number2;
    protected    EditText Name3;
    protected    EditText Number3;
    Button editContactsButton = null;
    Button saveButton = null;
    Spinner spinner;
    Spinner spinner2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_contact);


        //calling spinner



        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){


                SharedPreferences sharedPref = getSharedPreferences("Contact_G", Context.MODE_PRIVATE);

                switch (position){
                    case 0:
                        break;
                    case 1:

                        Intent callIntent1=new Intent(Intent.ACTION_DIAL);
                        callIntent1.setData(Uri.parse("tel:" + sharedPref.getString("Number1", "")));
                        startActivity(callIntent1);

                        break;
                    case 2:

                        Intent callIntent2=new Intent(Intent.ACTION_DIAL);
                        callIntent2.setData(Uri.parse("tel:" + sharedPref.getString("Number2", "")));
                        startActivity(callIntent2);

                        break;
                    case 3:

                       Intent callIntent3=new Intent(Intent.ACTION_DIAL);
                        callIntent3.setData(Uri.parse("tel:" + sharedPref.getString("Number3", "")));
                        startActivity(callIntent3);

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

    });


        //texting spinner



        spinner2 = (Spinner)findViewById(R.id.spinner2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){


                SharedPreferences sharedPref = getSharedPreferences("Contact_G", Context.MODE_PRIVATE);

                switch (position){
                    case 0:
                        break;
                    case 1:

                        Intent textIntent1= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:" + sharedPref.getString("Number1", "")));
                        textIntent1.putExtra("sms_body","Need Help");
                        startActivity(textIntent1);

                        break;
                    case 2:

                        Intent textIntent2= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:" + sharedPref.getString("Number2", "")));
                        textIntent2.putExtra("sms_body","Need Help");
                        startActivity(textIntent2);

                        break;
                    case 3:

                        Intent textIntent3= new Intent (Intent.ACTION_VIEW, Uri.parse ("sms:" + sharedPref.getString("Number3", "")));
                        textIntent3.putExtra("sms_body","Need Help");
                        startActivity(textIntent3);

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });


        Name1 = (EditText) findViewById(R.id.Name1);
        Number1 = (EditText) findViewById(R.id.Number1);
        Name2 = (EditText)findViewById(R.id.Name2);
        Number2 = (EditText)findViewById(R.id.Number2);
        Name3 = (EditText) findViewById(R.id.Name3);
        Number3 = (EditText) findViewById(R.id.Number3);

        editContactsButton = (Button)findViewById(R.id.editContactsButton);
        saveButton = (Button)findViewById(R.id.saveButton);

        //Edit Button Action
        editContactsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                makeEditable();
            }});

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SaveInfo();
                makeUneditable();
            }
        });

    }

    protected void onStart(){
        super.onStart();
        LoadInfo();
        makeUneditable();
    }


    //save info

    private void SaveInfo (){

        SharedPreferences sharedPref = getSharedPreferences("Contact_G", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Name1", Name1.getText().toString());
        editor.putString("Number1", Number1.getText().toString());
        editor.putString("Name2", Name2.getText().toString());
        editor.putString("Number2", Number2.getText().toString());
        editor.putString("Name3", Name3.getText().toString());
        editor.putString("Number3", Number3.getText().toString());
        editor.commit();
        makeUneditable();
        Toast.makeText(this, "Contact Saved", Toast.LENGTH_LONG).show();


    }

    private void LoadInfo(){
        SharedPreferences sharedPref = getSharedPreferences("Contact_G", Context.MODE_PRIVATE);

        Name1.setText(sharedPref.getString("Name1",""));
        Number1.setText(sharedPref.getString("Number1",""));
        Name2.setText(sharedPref.getString("Name2",""));
        Number2.setText(sharedPref.getString("Number2",""));
        Name3.setText(sharedPref.getString("Name3",""));
        Number3.setText(sharedPref.getString("Number3",""));



    }


    //Method that makes the text fields editable again and the save button visible
    public void makeEditable(){
        Name1.setFocusableInTouchMode(true);
        Number1.setFocusableInTouchMode(true);
        Name2.setFocusableInTouchMode(true);
        Number2.setFocusableInTouchMode(true);
        Name3.setFocusableInTouchMode(true);
        Number3.setFocusableInTouchMode(true);
        saveButton.setVisibility(View.VISIBLE);

    }

    public void makeUneditable(){
        Name1.setFocusableInTouchMode(false);
        Number1.setFocusableInTouchMode(false);
        Name2.setFocusableInTouchMode(false);
        Number2.setFocusableInTouchMode(false);
        Name3.setFocusableInTouchMode(false);
        Number3.setFocusableInTouchMode(false);
        saveButton.setVisibility(View.GONE);
    }


}























