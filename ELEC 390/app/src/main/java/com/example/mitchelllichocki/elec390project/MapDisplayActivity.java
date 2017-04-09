package com.example.mitchelllichocki.elec390project;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.mitchelllichocki.elec390project.R.id.spinner;
import static com.example.mitchelllichocki.elec390project.R.menu.menu_actionbar;

//Extends FragmentActivity and implements OnMapReadyCallback in order to make map functional
public class MapDisplayActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    double latitude = 1000, longitude = 1000;
    int test;
    Marker marker = null;
    GoogleMap map;
    ArrayList<String> names = new ArrayList<>(), childrenUsername = new ArrayList<>();
    String childSelected, username;
    DraggableCircle savedRegion;
    LatLng savedPosition = null; //default
    double savedRadius = 50.0; //default
    double lat, lon;
    Button setBeacon;
    private List<DraggableCircle> mCircles = new ArrayList<>(1);
    boolean initialization = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        //Create a Map Fragment and pass the id of the map via R.id.<id>
        // where <id> was set in activity_map_display.xml
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //Assign the map to the fragment
        mapFragment.getMapAsync(MapDisplayActivity.this);

        final int refreshRate = 1000 * 10; //Time rates are in milliseconds

        //If this is the first instance of the activity starting
        if (savedInstanceState == null) {
            ArrayList<String> tempChildren;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("children", null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            tempChildren = gson.fromJson(json, type);
            json = sharedPreferences.getString("username", null);
            type = new TypeToken<String>() {}.getType();
            username = gson.fromJson(json, type);

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

        setBeacon = (Button)findViewById(R.id.setBeacon);
        setBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePosition();
                if(savedPosition != null){
                    //saved in the database
                    BackgroundWorker backgroundWorker = new BackgroundWorker(MapDisplayActivity.this);
                    backgroundWorker.setBeacon(map, username, childSelected, savedPosition.latitude, savedPosition.longitude, savedRadius);

                    // this code only allows one saved region at a time
                    if (mCircles != null){mCircles.clear();}
                    map.clear();
                    savedRegion = new DraggableCircle(savedPosition, savedRadius); // default radius is 50 meters
                    savedRegion.hideMarkers();
                    mCircles.add(savedRegion);
                    //making sure we only save the same region once
                    savedPosition = null;
                    savedRadius = 50.0;

                    Toast.makeText(getApplicationContext(), "Beacon Set!", Toast.LENGTH_SHORT).show();
                    // make sure to save this beacon's coordinates
                } else {
                    Toast.makeText(getApplicationContext(), "Click on the map to add a beacon", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Continually refresh the map every refreshRate seconds
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateMapDisplay(childSelected);
                //Run this script every 10 seconds
                handler.postDelayed(this, refreshRate);
            }
        }, 1000); //mapLoadTime is the delay for the map to load

    }


    //Function is called as soon as the map is ready.
    //Kind of like a "default" setup for the map.
    @Override
    public void onMapReady(final GoogleMap map) {
        //Set the map to a basic grid style
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //getLatLong(childSelected);
        //set the starting position marker
        /*marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lat: " + latitude + " | " + "Long: " + longitude));
        */
        //Center the camera of the map to those coordinates with a zoom level of 15 (street view)
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        //Assign the Google Map object "map" to this map
        this.map = map;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng centerOfRegion) {
                lat = centerOfRegion.latitude;
                lon = centerOfRegion.longitude;
                if(!((lat > 90) || (lat < 0) || (Math.abs(lon) > 180))){
                    savedPosition = centerOfRegion;
                }else{
                    savedPosition = null;
                }

                if (mCircles != null){
                    mCircles.clear();
                    //map.clear();
                    if (savedRegion != null){
                        DraggableCircle savedCircle = new DraggableCircle(savedRegion.getCenter(), savedRegion.getRadius());
                        savedCircle.hideMarkers();
                        mCircles.add(savedCircle);
                    }
                }
                DraggableCircle circle = new DraggableCircle(centerOfRegion, 50); // default radius is 50 meters
                mCircles.add(circle);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){
            @Override
            public void onMarkerDragStart(Marker marker) {onMarkerMoved(marker);}

            @Override
            public void onMarkerDrag(Marker marker) {onMarkerMoved(marker);}

            @Override
            public void onMarkerDragEnd(Marker marker) {onMarkerMoved(marker);}

            private void onMarkerMoved(Marker marker){
                for (DraggableCircle draggableCircle : mCircles){
                    if (draggableCircle.onMarkerMoved(marker))
                        break;
                }
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
                    initialization = false;

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
        if(marker != null) {
            marker.remove();
        }
        if((latitude > 90) || (latitude < 0) || (Math.abs(longitude) > 180)){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5, -73.566667), 1));
            if(!initialization) {
                Toast.makeText(this, "Unable to locate child!", Toast.LENGTH_SHORT).show();
            } initialization = false;
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
        BackgroundWorker backgroundWorker = new BackgroundWorker(MapDisplayActivity.this);
        backgroundWorker.fetchCoordinates(username, childrenUsername.get(names.indexOf(name)), new BackgroundWorker.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.isNull("latitude") || jsonObject.isNull("longitude") ){
                        latitude = 1000;
                        longitude = 1000;
                    }
                    else {
                        latitude = Double.parseDouble(jsonObject.getString("latitude"));
                        longitude = Double.parseDouble(jsonObject.getString("longitude"));
                    }
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

    // beacon region
    private class DraggableCircle{
        private final Marker mCenter;
        private final Marker mRadius;
        private final Circle mCircle;
        private double mRadiusMeters;

        public DraggableCircle(LatLng center, double radiusMeters){
            mRadiusMeters = radiusMeters;
            mCenter = map.addMarker(new MarkerOptions().position(center).draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mRadius = map.addMarker(new MarkerOptions().position(toRadiusLatLng(center, radiusMeters)).draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mCircle = map.addCircle(new CircleOptions().center(center).radius(radiusMeters).strokeColor(Color.argb(200, 50, 150, 250)).fillColor(Color.argb(100, 50, 150, 250)));
        }

        public boolean onMarkerMoved(Marker marker){
            if (marker.equals(mCenter)){
                savedPosition = marker.getPosition();
                mCircle.setCenter(savedPosition);
                mRadius.setPosition(toRadiusLatLng(marker.getPosition(), mRadiusMeters));
                return true;
            }
            if (marker.equals(mRadius)){
                mRadiusMeters = toRadiusMeters(mCenter.getPosition(), mRadius.getPosition());
                mCircle.setRadius(mRadiusMeters);
                savedRadius = mRadiusMeters;
                return true;
            }
            return false;
        }

        public void hideMarkers(){
            mRadius.setVisible(false);
            mCenter.setVisible(false);
        }

        public LatLng getCenter() {
            return mCenter.getPosition();
        }

        public double getRadius() {
            return mRadiusMeters;
        }

    }

    private static LatLng toRadiusLatLng(LatLng center, double radiusMeters){
        double radiusAngle = Math.toDegrees(radiusMeters / 6371009)/Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius){
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return result[0];
    }

}
