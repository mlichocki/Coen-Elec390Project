package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mitchelllichocki.elec390project.R.id.spinner;
import static com.example.mitchelllichocki.elec390project.R.menu.menu_actionbar;

//Extends FragmentActivity and implements OnMapReadyCallback in order to make map functional
public class MapDisplayActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    double latitude, longitude;
    Marker marker = null;
    GoogleMap map;
    ArrayList<String> names = new ArrayList<>(), childrenUsername = new ArrayList<>();
    String childSelected, username;
    LatLng savedPosition;
    double lat, lon;
    Button setBeacon;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        final int refreshRate = 1000 * 30; //Time rates are in milliseconds

        //If this is the first instance of the activity starting
        if (savedInstanceState == null) {
            ArrayList<String> tempChildren;
            Intent intent = getIntent();
            tempChildren = intent.getStringArrayListExtra("children");
            username = intent.getStringExtra("username");

            if (tempChildren != null) {
                for (int i = 0; i < tempChildren.size(); i++) {
                    names.add(tempChildren.get(i));
                    childrenUsername.add(tempChildren.get(++i));
                }
            }
            //If not the first instance then restore from previous instance
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
        //By default the first child in the ArrayList is displayed
        childSelected = names.get(0);

        //set the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Create a Map Fragment and pass the id of the map via R.id.<id>
        // where <id> was set in activity_map_display.xml
        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        //Assign the map to the fragment
        mapFragment.getMapAsync(this);

        //Continually refresh the map every refreshRate seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                updateMapDisplay(childSelected);
                //Run this script every 10 seconds
                handler.postDelayed(this, refreshRate);
            }
        }, 0); //mapLoadTime is the delay for the map to load


        setBeacon = (Button)findViewById(R.id.setBeacon);
        setBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePosition();
                if((lat != 0.0)&&(lon != 0.0)){
                    savedPosition = new LatLng(lat, lon);
                    //save in the database instead
                    //BackgroundWorker backgroundWorker = new BackgroundWorker();
                    //backgroundWorker.seBeacon(lat, lon);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(savedPosition));
                    Toast.makeText(getApplicationContext(), "Point Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Function is called as soon as the map is ready.
    //Kind of like a "default" setup for the map.
    @Override
    public void onMapReady(final GoogleMap map) {
        //Set the map to a basic grid style
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getLatLong(childSelected);
        //set the starting position marker
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lat: " + latitude + " | " + "Long: " + longitude));

        //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        //Assign the Google Map object "map" to this map
        this.map = map;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng pointTouch) {
                map.clear();
                map.addMarker(new MarkerOptions().position(pointTouch));
                lat = pointTouch.latitude;
                lon = pointTouch.longitude;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create the action bar, this generates the drop down menu for the kids' names
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_actionbar, menu);

        MenuItem item = menu.findItem(spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        if(childSelected != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    childSelected = spinner.getSelectedItem().toString().trim();
                    updateMapDisplay(childSelected);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else{
            spinner.setVisibility(View.GONE);
            spinner.setBackground(null);
        }

        return true;
    }

    private void updateMapDisplay(String name){
        //Retrieve the coordinates
        //child's username: childrenUsername.get(names.indexOf(name));
        getLatLong(name);
        //Remove the marker of the "old" position
        marker.remove();
        if((latitude > 90) || (latitude < 0) || (Math.abs(longitude) > 180)){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5, -73.566667), 1));
            Toast.makeText(this,"Unable to locate child!",Toast.LENGTH_SHORT).show();
        }
        else {
            //Add a new marker at for the newest positions
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(name).snippet("Lat: " + latitude + "\n" + "Long: " + longitude));
            //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        }
    }

    //Retrieve the latitude and longitude of the most recent position
    public void getLatLong(String name){
        backgroundWorker.fetchCoordinates(username, childrenUsername.get(names.indexOf(name)), new BackgroundWorker.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    latitude = Double.parseDouble(jsonObject.getString("latitude"));
                    longitude = Double.parseDouble(jsonObject.getString("longitude"));
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //Save the following data when the activity is exited
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("names", names);
        savedInstanceState.putStringArrayList("childrenUsername", childrenUsername);
        savedInstanceState.putString("username", username);
    }


    //Restore the saved data when the activity resumes
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        names = savedInstanceState.getStringArrayList("names");
        childrenUsername = savedInstanceState.getStringArrayList("childrenUsername");
        username = savedInstanceState.getString("username");
    }

}
