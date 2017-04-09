package com.example.mitchelllichocki.elec390project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mitchelllichocki on 2017-03-21.
 */

public class BackgroundWorker{

    public final static String KEY_IP_ADDRESS = "coenelec390.gear.host";
    public final static String KEY_LOGIN_URL = "http://" + KEY_IP_ADDRESS + "/login.php";
    public final static String KEY_GETCHILDREN_URL = "http://" + KEY_IP_ADDRESS + "/getchildren.php";
    public final static String KEY_REGISTER_URL = "http://" + KEY_IP_ADDRESS + "/register.php";
    public final static String KEY_ADDCHILD_URL = "http://" + KEY_IP_ADDRESS + "/addchild.php";
    public final static String KEY_POSTCOORD_URL = "http://" + KEY_IP_ADDRESS + "/postcoord.php";
    public final static String KEY_FETCHCOORD_URL = "http://" + KEY_IP_ADDRESS + "/fetchcoord.php";
    public final static String KEY_SETBEACON_URL = "http://" + KEY_IP_ADDRESS + "/setbeacon.php";
    public Context context;
    private static BackgroundWorker instance = null;

    public BackgroundWorker(Context context){
        this.context = context;
    }

    public void login(final String username, final String password){

        StringRequest stringRequest=new StringRequest(Request.Method.POST,KEY_LOGIN_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String role = jsonObject.getString("role").toUpperCase();
                    if(role.equals("GUARDIAN")){
                        guardianLogin(username);
                    }
                    else if(role.equals("CHILD")){
                        Intent intent = new Intent(context, ChildActivity.class);
                        intent.putExtra("username", username);

                        Intent childService = new Intent(context,  ChildService.class);
                        childService.putExtra("username", username);

                        context.startService(childService);
                        context.startActivity(intent);
                    }
                    else if(role.equals("PASSWORD")){
                        Toast.makeText(context,"INVALID PASSWORD",Toast.LENGTH_SHORT).show();
                    }
                    else if(role.equals("FAIL")){
                        Toast.makeText(context,"INVALID LOGIN",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"Login Error: Contact Support",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
            Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show(); //Error here, Beware!!!!!
            }

        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void guardianLogin(final String username){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_GETCHILDREN_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                try{

                    JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(context, GuardianActivity.class);
                    ArrayList<String> children = new ArrayList<>();

                    if(!jsonObject.isNull("children")) {

                        JSONArray associatedChildren = jsonObject.getJSONArray("children");
                        for (int i = 0; i < associatedChildren.length(); i++) {
                            JSONObject tempJSON = associatedChildren.getJSONObject(i);
                            children.add(tempJSON.getString("name"));
                            children.add(tempJSON.getString("username"));

                        }
                    }

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(children);
                    editor.remove("children");
                    editor.putString("children", json);
                    json = gson.toJson(username);
                    editor.remove("username");
                    editor.putString("username", json);
                    editor.commit();
                    context.startActivity(intent);

                }
                catch(JSONException e){

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void addChild(final String guardianUsername, final String childUsername, final String name, final String password, final ArrayList<String> children){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,KEY_ADDCHILD_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = sharedPreferences.getString("children", null);
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> children = gson.fromJson(json, type);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(context, GuardianActivity.class);
                    String status = jsonObject.getString("status").toUpperCase();
                    //If the account is successfully created (no username account with the entered username was found)
                    if (status.equals("SUCCESS")) {
                        //Add the JSON String for the child's name
                        children.add(jsonObject.getString("name"));
                        //Add the JSON String for the child's username
                        children.add(jsonObject.getString("username"));
                        //Assign the ArrayList<String> children to the JSON String json
                        json = gson.toJson(children);
                        //Clear the stored data in SharedPreferences under the name "children" (avoid multiples)
                        editor.remove("children");
                        //Place the JSON String json in the sharedPreferences
                        editor.putString("children", json);
                        //Commit the SharedPreferences change
                        editor.commit();
                        //Inform user of successful registration
                        Toast.makeText(context, "ACCOUNT REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        //Take the user back to the general GuardianActivity page
                        context.startActivity(intent);
                    } else if (status.equals("ADDED")) {
                        //Add the JSON String for the child's name
                        children.add(jsonObject.getString("name"));
                        //Add the JSON String for the child's username
                        children.add(jsonObject.getString("username"));
                        //Assign the ArrayList<String> children to the JSON String json
                        json = gson.toJson(children);
                        //Clear the stored data in SharedPreferences under the name "children" (avoid multiples)
                        editor.remove("children");
                        //Place the JSON String json in the sharedPreferences
                        editor.putString("children", json);
                        //Commit the SharedPreferences change
                        editor.commit();
                        //Inform user of successful registration
                        Toast.makeText(context, "ACCOUNT ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        //Take the user back to the general GuardianActivity page
                        context.startActivity(intent);
                    } else if (status.equals("PASSWORD")) {
                        //Inform user of account existance but wrong password
                        Toast.makeText(context, "ADD CHILD ERROR: ACCOUNT FOUND, WRONG PASSWORD", Toast.LENGTH_SHORT).show();

                    }
                    //If there is a problem with the server creating / looking for the account
                    else if (status.equals("ERROR")) {
                        //Inform user of problem creating or adding the child account (server issue)
                        Toast.makeText(context, "ADD CHILD ERROR: CONTACT SUPPORT", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guardianUsername", guardianUsername);
                params.put("childUsername", childUsername);
                params.put("password", password);
                params.put("name", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void register(final String username, final String password, final String email, final String role){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_REGISTER_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(context, LoginActivity.class);
                    if(!jsonObject.isNull("status")) {
                        String registerStatus = jsonObject.getString("status").toUpperCase();
                        if(registerStatus.equals("SUCCESS")){
                            intent.putExtra("username", username);
                            context.startActivity(intent);
                        }
                        else if(registerStatus.equals("INUSE")){
                            Toast.makeText(context,"USERNAME ALREADY IN USE",Toast.LENGTH_SHORT).show();
                        }
                        else if(registerStatus.equals("FAIL")){
                            Toast.makeText(context,"REGISTRATION FAILED",Toast.LENGTH_SHORT).show();
                        }
                        else{

                        }
                    }
                    else{

                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("role", role);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    /*
    The postCoordinates function checks the beacon status from each parent in the <child's name> table
    and compares them with the child's current location. If the child is within this beacon a value of 2 is assigned,
    if they are outside of this beacon a value of 1 is assigned. If the beacon's parameters are null then a value of 0 is set.
    Once the beacon check is complete, each associated <guardian name> table is then updated with the child's current location.
     */
    public void postCoordinates(final String username, final double latitude, final double longitude, final String status){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_POSTCOORD_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if(error instanceof TimeoutError) {
                            Toast.makeText(context, "Server timeout error!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("status", status.toUpperCase());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void fetchCoordinates(final String guardianUsername, final String childUsername, final VolleyCallback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_FETCHCOORD_URL,

                new Response.Listener<String>(){

            @Override
            public void onResponse(String response){
                callback.onSuccess(response);
            }

        },

                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show();

                    }

                }){

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guardianUsername", guardianUsername);
                params.put("childUsername", childUsername);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public void setBeacon(final GoogleMap map, final String guardianUsername, final String childUsername, final double Blatitude, final double Blongitude, final double Bradius){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_SETBEACON_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String role = jsonObject.getString("status").toUpperCase();
                    if(role.equals("SUCCESS")){
                        map.clear();
                        map.addMarker(new MarkerOptions().position(new LatLng(Blatitude, Blongitude)));
                        Toast.makeText(context, "Point Saved!", Toast.LENGTH_SHORT).show();
                    }
                    else if(role.equals("FAIL")){
                        map.clear();
                        Toast.makeText(context, "Beacon Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guardianUsername", guardianUsername);
                params.put("childUsername", childUsername);
                params.put("Blatitude", Double.toString(Blatitude));
                params.put("Blongitude", Double.toString(Blongitude));
                params.put("Bradius", Double.toString(Bradius));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
