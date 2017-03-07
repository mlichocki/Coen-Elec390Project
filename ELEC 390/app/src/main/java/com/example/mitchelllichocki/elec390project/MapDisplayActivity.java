package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

//Extends FragmentActivity and implements OnMapReadyCallback in order to make map functional
public class MapDisplayActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    double latitude, longitude;
    Marker marker = null;
    GoogleMap map;
    ArrayList<String> names = new ArrayList<>();
    String previous = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        final int refreshRate = 1000*5, mapLoadTime = 1000*5; //Time rates are in milliseconds

        Intent intent = getIntent();
        names = intent.getStringArrayListExtra("names");

        //By default the first child in the ArrayList is displayed
        final String childSelected = names.get(0);

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
                //Retrieve the coordinates
                getLatLong(childSelected);
                //Remove the marker of the "old" position
                marker.remove();
                //Add a new marker at for the newest positions
                marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Marker"));
                //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                //Run this script every 10 seconds
                handler.postDelayed(this, refreshRate);
            }
        }, mapLoadTime); //mapLoadTime is the delay for the map to load
    }


    //Function is called as soon as the map is ready.
    //Kind of like a "default" setup for the map.
    @Override
    public void onMapReady(GoogleMap map) {
        getLatLong(names.get(0));
        //set the starting position marker
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker"));

        //Set the map to a basic grid style
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.474541, -73.841568), 15));

        //Assign the Google Map object "map" to this map
        this.map = map;
    }

    //Retrieve the latitude and longitude of the most recent position
    public void getLatLong(String name){

        if(previous != name) {
            //Just to test updating the map, to be replaced by method for retrieving coordinates
            switch (name) {
                case "Mitch":
                    latitude = 45.474541;
                    longitude = -73.841568;
                    break;
                case "Marie":
                    latitude = 45.511324;
                    longitude = -73.702861;
                    break;
                case "Connor":
                    latitude = 45.473052;
                    longitude = -73.737033;

            }
        }

        previous = name;
        latitude = latitude + 0.25;
        longitude = longitude + 0.25;
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
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        return true;
    }
}
