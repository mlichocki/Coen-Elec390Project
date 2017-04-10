package com.example.mitchelllichocki.elec390project;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix on 2017-04-03.
 */

public class NotificationService {

    HashMap<Integer, String> NotifMap = new HashMap<>();


    //ArrayList<String> NotifContentTitle = new ArrayList<>();
    ArrayList<String> NotifContentText = new ArrayList<>();
    ArrayList<Integer> NotifID = new ArrayList<>();
    NotificationManager notificationManager;
    boolean isNotifiedActive = false;


    public NotificationService(){};


    public void showNotification (Context context, Integer NotificationID){

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(context)
                .setContentTitle("SecureTrack")
                .setContentText(NotifMap.get(NotificationID))
                .setSmallIcon(R.drawable.quantum_ic_cast_white_36);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationID, notificationBuilder.build());
        isNotifiedActive = true;

    }

    public void InitializeNotifications (){

        NotifMap.put(1, "A Child has entered the Beacon");
        NotifMap.put(22, "A child has left a Beacon");
        NotifMap.put(7, "A Child has missed his Arrival Time");
        NotifMap.put(44, "A Child's GPS has been disabled");

    }
/*
    public void getNotifications(final String guardianUsername){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://coenelec390.gear.host/getnotifications.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.isNull("children")) {
                        JSONArray tempnotifications = jsonObject.getJSONArray("notifications");
                        ArrayList<String> notifications = new ArrayList<>();
                        for (int i = 0; i < tempnotifications.length(); i++) {
                            JSONObject tempJSON = tempnotifications.getJSONObject(i);
                            switch(tempJSON.getString("notification")){
                                case 0: //no notification to display
                                    break;
                                case 1: //The child has left the beacon
                                    break;
                                case 2: //The child has entered the beacon
                                    break;
                                case 10: //The child has disconnected
                                    break;
                                case 11: //The child has entered the beacon and disconnected
                                    break;
                                case 12: //The child has left the beacon and disconnected
                                    break;
                                //To display the notification you should show the disconnected child's name in the notification. Get the child's name via: tempJSON.getString("name")
                            }
                        }
                    }
                    else{
                    }

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
            Toast.makeText(context,error.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("guardianUsername", guardianUsername);
            return params;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
}
*/
}
