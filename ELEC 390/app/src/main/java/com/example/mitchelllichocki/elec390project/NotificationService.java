package com.example.mitchelllichocki.elec390project;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationService extends Service {

    HashMap<Integer, String> NotifMap = new HashMap<>();
    String username = null;
    //String ChildName;
    //ArrayList<String> NotifContentTitle = new ArrayList<>();
    //ArrayList<String> NotifContentText = new ArrayList<>();
    //ArrayList<Integer> NotifID = new ArrayList<>();
    NotificationManager notificationManager;
    boolean isNotifiedActive = false;
    Context context;



    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        InitializeNotifications();
        Log.d("NotificationService", "onStartCommand");
        if(intent != null) {
            if (intent.hasExtra("username")) {
                username = intent.getStringExtra("username");
            }
            else{
                Log.d("NotificationService", "username == null");
            }
        }else{
            Log.d("NotificationService", "itent == null");
        }
        final Handler handler = new Handler();
        final int refreshRate = 1000*5;

        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Log.d("NotificationService", "In Handler");
                if(username != null) {
                    Log.d("NotificationService", "getNotifications: " + username);
                    getNotifications(username);
                }
                handler.postDelayed(this, refreshRate);
            }
        }, 0);
        return START_REDELIVER_INTENT;
    }


    public void showNotification (Integer NotificationID){
        Log.d("NotificationService", "Notifications is about to show: " + NotificationID);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setContentTitle("SecureTrack")
                .setContentText(NotifMap.get(NotificationID))
                .setSmallIcon(R.drawable.quantum_ic_cast_white_36);
        Log.d("NotificationService", "Notifications is RIGHT about to show: " + NotificationID);

        notificationManager = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);
        Log.d("NotificationService", "Notifications is RIGHT about to show *2*: " + NotificationID);
        notificationManager.notify(NotificationID, notificationBuilder.build());
        Log.d("NotificationService", "Notifications is RIGHT about to show *3* : " + NotificationID);
        isNotifiedActive = true;
        Log.d("NotificationService", "Notifications has been shown: " + NotificationID);
    }

    public void InitializeNotifications (){

        NotifMap.put(0, "No notifications to display");
        NotifMap.put(1, "The child has left the beacon");
        NotifMap.put(2, "The child has entered the beacon");
        NotifMap.put(10, "The child has disconnected");
        NotifMap.put(11, "The Child has entered the beacon and disconnected");
        NotifMap.put(12, "The Child has left the beacon and disconnected");

    }

    public void getNotifications(final String guardianUsername){
        Log.d("NotificationService", "getNotifications");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://coenelec390.gear.host/getnotifications.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("NotificationService", "Entered Public Void");
                try{
                    Log.d("NotificationService", "Json has been geted");
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("NotificationService", "get response from server");
                    if(!jsonObject.isNull("notifications")) {
                        JSONArray tempnotifications = jsonObject.getJSONArray("notifications");
                        ArrayList<String> notifications = new ArrayList<>();
                        Log.d("NotificationService", "get response from server 2: " + tempnotifications.length());
                        for (int i = 0; i < tempnotifications.length(); i++) {
                            Log.d("NotificationService", "get response from server 2 -> For statement");
                            JSONObject tempJSON = tempnotifications.getJSONObject(i);
                            Log.d("NotificationService", "Notification ID = " + Integer.parseInt(tempJSON.getString("notification")));
                            switch(Integer.parseInt(tempJSON.getString("notification"))){
                                case 0: //no notification to display
                                    Log.d("NotificationService", "Pre-notification 0");
                                    //showNotification(0);
                                    Log.d("NotificationService", "Post-notification 0");
                                    break;
                                case 1: //The child has left the beacon
                                    showNotification(1);
                                    break;
                                case 2: //The child has entered the beacon
                                    showNotification(2);
                                    break;
                                case 10: //The child has disconnected
                                    showNotification(10);
                                    break;
                                case 11: //The child has entered the beacon and disconnected
                                    showNotification(11);
                                    break;
                                case 12: //The child has left the beacon and disconnected
                                    showNotification(12);
                                    break;
                                //To display the notification you should show the disconnected child's name in the notification. Get the child's name via: tempJSON.getString("name")
                            }
                        }
                    }
                    else{
                    }
                }
                catch(JSONException e){
                    Log.d("NotificationService", "Caught Error");
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if (error instanceof TimeoutError){
                            Log.d("NotificationService", "Timeout Label");
                        }
                        else {
                            Log.d("NotificationService", "OnError Response Toast Message");
                            Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guardianUsername", guardianUsername);
                Log.d("NotificationService", "Map Params String");
                return params;
            }
        };
        Log.d("NotificationService", "Before request Volley");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d("NotificationService", "After Request Volley");

    }

}
