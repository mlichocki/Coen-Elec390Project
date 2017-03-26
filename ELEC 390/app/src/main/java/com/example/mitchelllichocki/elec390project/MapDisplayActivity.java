package com.example.mitchelllichocki.elec390project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.example.mitchelllichocki.elec390project.R.id.spinner;
import static com.example.mitchelllichocki.elec390project.R.menu.menu_actionbar;

//Extends FragmentActivity and implements OnMapReadyCallback in order to make map functional
public class MapDisplayActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    double latitude, longitude;
    Marker marker = null;
    GoogleMap map;
    ArrayList<String> names = new ArrayList<>(), children = new ArrayList<>();
    String previous = "", childSelected, username;
    LocationService locationService;
    LatLng savedPosition;
    double lat, lon;
    Button setBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        final int refreshRate = 1000*5, mapLoadTime = 1000*5; //Time rates are in milliseconds
        ArrayList<String> tempChildren = new ArrayList<>();
        Intent intent = getIntent();
        tempChildren = intent.getStringArrayListExtra("children");
        username = intent.getStringExtra("username");

        if(tempChildren != null) {
            for (int i = 0; i < tempChildren.size(); i++) {
                names.add(tempChildren.get(i));
                children.add(tempChildren.get(++i));
            }
            //By default the first child in the ArrayList is displayed
            childSelected = names.get(0);
        }
        else{

        }

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
        }, mapLoadTime); //mapLoadTime is the delay for the map to load


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
        getLatLong(childSelected);
        //set the starting position marker
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lat: " + latitude + " | " + "Long: " + longitude));

        //Set the map to a basic grid style
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
                    //Retrieve the coordinates
                    getLatLong(childSelected);
                    //Remove the marker of the "old" position
                    marker.remove();
                    //Add a new marker at for the newest positions
                    marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Lat: " + latitude + " | " + "Long: " + longitude));
                    //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

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
        getLatLong(name);
        //Remove the marker of the "old" position
        marker.remove();
        //Add a new marker at for the newest positions
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name).snippet("Lat: " + latitude + "\n" + "Long: " + longitude));
        //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    //Retrieve the latitude and longitude of the most recent position
    public void getLatLong(String name){

        if(name != null) {
            //latitude = locationService.getMyLatitude();
            //longitude = locationService.getMyLongitude();
        }
        else{

        }

        latitude = 45.497236;
        longitude = -73.579023;

    }



}
