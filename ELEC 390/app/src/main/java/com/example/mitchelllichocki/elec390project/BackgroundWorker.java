package com.example.mitchelllichocki.elec390project;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchelllichocki on 2017-03-21.
 */

public class BackgroundWorker{

    public final static String KEY_IP_ADDRESS = "coenelec390.gear.host";
    public final static String KEY_LOGIN_URL = "http://" + KEY_IP_ADDRESS + "/login2.php";
    public final static String KEY_GETCHILDREN_URL = "http://" + KEY_IP_ADDRESS + "/getchildren.php";
    public final static String KEY_REGISTER_URL = "http://" + KEY_IP_ADDRESS + "/register2.php";
    public final static String KEY_ADDCHILD_URL = "http://" + KEY_IP_ADDRESS + "/addchild2.php";
    public final static String KEY_POSTCOORD_URL = "http://" + KEY_IP_ADDRESS + "/postcoord2.php";
    public Context context;
    public double[] beacon;

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
                        String guardian = jsonObject.getString("guardian");
                        intent.putExtra("guardian", guardian);
                        intent.putExtra("username", username);
                        context.startActivity(intent);
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
                    if(!jsonObject.isNull("children")) {
                        JSONArray associatedChildren = jsonObject.getJSONArray("children");
                        ArrayList<String> children = new ArrayList<>();
                        for (int i = 0; i < associatedChildren.length(); i++) {
                            JSONObject tempJSON = associatedChildren.getJSONObject(i);
                            children.add(tempJSON.getString("name"));
                            children.add(tempJSON.getString("username"));
                        }
                        intent.putExtra("children", children);
                        intent.putExtra("username", username);
                    }
                    else{

                    }
                    addChild("mitch", "Felix", "Felix", "Felix", null);
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
            public void onResponse(String response){

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(context, GuardianActivity.class);
                    children.add(jsonObject.getString("name"));
                    children.add(jsonObject.getString("username"));
                    intent.putExtra("children", children);
                    intent.putExtra("username", guardianUsername);
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
                params.put("password", password);
                params.put("email", email);
                params.put("role", role);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public double [] postCoordinates(final String username, final double latitude, final double longitude){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_POSTCOORD_URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    double Blat = jsonObject.getDouble("Blatitude");
                    double Blong = jsonObject.getDouble("Blongitude");
                    double notification = (double) jsonObject.getInt("notification");
                    beacon = new double[]{Blat, Blong, notification};
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
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("notification", "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return beacon;
    }
}
